package oz.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import oz.Settings;
import oz.modules.Module;

public class Network extends Thread
{
	public Network(Settings settings)
	{
		m_run = true;
		m_port = settings.getNetworkPort();
		m_receiveBufferSize = 1000;
		m_clients = new LinkedList<Client>();
		m_charset = Charset.forName("UTF-8");
		m_decoder = m_charset.newDecoder();
		m_encoder = m_charset.newEncoder();
		m_commands = new Hashtable<String, Module>();
		m_separator = " ";
	}

	public void run()
	{
		try
		{
			listen();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void listen() throws IOException
	{
		// Create listening socket
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ServerSocket socket = ssc.socket();
		socket.bind(new InetSocketAddress(m_port));
		socket.setReuseAddress(true);
		System.out.println("Listening to port " + m_port);

		// Create selector
		m_selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.register(m_selector, SelectionKey.OP_ACCEPT);

		while (m_run)
		{
			m_selector.select();
			Set<SelectionKey> keys = m_selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext())
			{
				SelectionKey key = (SelectionKey) it.next();
				if (key.isAcceptable())
				{
					// Create client
					Client client = new Client();
					client.setSocket(socket.accept());
					m_clients.add(client);
					System.out.println("accept : there is now " + m_clients.size() + " clients");

					// Register client socket
					SocketChannel sc = client.getSocket().getChannel();
					sc.configureBlocking(false);
					sc.register(m_selector, SelectionKey.OP_READ, client);
				}
				else if (key.isReadable())
				{
					SocketChannel channel = (SocketChannel) key.channel();
					Client client = (Client) key.attachment();

					ByteBuffer bb = ByteBuffer.allocate(m_receiveBufferSize);

					String command = "";
					int readSize = 0;
					do
					{
						bb.position(0);
						readSize = channel.read(bb);

						if (readSize < 0) // Client disconnected
						{
							key.cancel();
							m_clients.remove(client);
						}
						else
						{
							bb.position(0);
							command += m_decoder.decode(bb).toString().substring(0, readSize);
						}
					} while (readSize > 0);

					parseCommand(client, command);
				}
			}
			keys.clear();
		}

		socket.close();
		System.out.println("Stopped listening to port " + m_port);
	}

	public void stopListen()
	{
		m_run = false;
		try
		{
			// Create a fake connection to unblock the selector and exit
			Socket socket = new Socket("localhost", m_port);
			socket.close();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Client createClient(String address)
	{
		String[] addressParts = address.split(":");
		address = addressParts[0];
		int port = (addressParts.length > 1) ? Integer.parseInt(addressParts[1]) : m_port;

		try
		{
			System.out.println("trying to connect ...");
			InetSocketAddress host = new InetSocketAddress(address, port);
			SocketChannel channel = SocketChannel.open(host);
			channel.configureBlocking(false);
			Socket socket = channel.socket();

			if (socket.isConnected())
			{
				System.out.println("connected ...");
				// Create client
				Client client = new Client();
				client.setSocket(socket);
				m_clients.add(client);
				System.out.println("add : there is now " + m_clients.size() + " clients");

				// Register client socket
				final ReentrantLock selectorLock = new ReentrantLock();
				selectorLock.lock();
				try
				{
					m_selector.wakeup();
					channel.register(m_selector, SelectionKey.OP_READ, client);
				}
				finally
				{
					selectorLock.unlock();
				}

				return client;
			}
		}
		catch (UnknownHostException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
		return null;
	}

	public void send(String packet, Client client) throws IOException
	{
		try
		{
			ByteBuffer bb = m_encoder.encode(CharBuffer.wrap(packet));
			client.getSocket().getChannel().write(bb);
		}
		catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
	}

	public void send(String packet, Iterable<Client> clientList) throws IOException
	{
		try
		{
			ByteBuffer bb = m_encoder.encode(CharBuffer.wrap(packet));

			if (clientList == null)
				clientList = m_clients;
			Iterator<Client> it = clientList.iterator();
			while (it.hasNext())
			{
				Client client = it.next();

				System.out.println("sending to " + client.getUserData().getUsername());
				client.getSocket().getChannel().write(bb);
			}
		}
		catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
	}

	private boolean parseCommand(Client client, String packet)
	{
		System.out.println("parsing command : " + packet);

		Module module = m_commands.get(getCommand(packet));
		if (module == null)
		{
			System.out.println("no module set for command " + getCommand(packet));
			return false;
		}

		module.executeCommand(packet, client);

		return true;
	}

	public boolean setCommand(String command, Module module)
	{
		return m_commands.put(command, module) != null;
	}

	public String getCommand(String packet)
	{
		return packet.split(m_separator)[0];
	}

	public <T> T parsePacket(String packet, Class<T> c) // TODO check errors
	{
		String command = packet.split(m_separator)[0];
		String data = packet.substring(command.length() + 1);
		return (T) new JSONDeserializer<T>().use(null, c).deserialize(data);
	}

	public String makePacket(String command, Object data)
	{
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class");
		serializer.include("*");
		return command + " " + serializer.serialize(data);
	}

	/**
	 * @return the fields separator
	 */
	public String getSeparator()
	{
		return m_separator;
	}

	/**
	 * @param separator
	 *            the fields separator to set
	 */
	public void setSeparator(String separator)
	{
		m_separator = separator;
	}

	public Iterable<Client> getClients()
	{
		return m_clients;
	}

	private boolean						m_run;
	private int							m_port;
	private int							m_receiveBufferSize;
	private Selector					m_selector;
	private List<Client>				m_clients;
	private Charset						m_charset;
	private CharsetDecoder				m_decoder;
	private CharsetEncoder				m_encoder;
	private Hashtable<String, Module>	m_commands;
	private String						m_separator;

}
