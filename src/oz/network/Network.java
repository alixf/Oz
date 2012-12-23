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
import java.nio.channels.UnresolvedAddressException;
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

import oz.data.Address;
import oz.modules.Module;
import oz.modules.settings.Settings;
import oz.tools.Operations;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * This class is used to manipulate the network.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Network extends Thread
{

	/** The charset. */
	private Charset						m_charset;

	/** The clients. */
	private List<Client>				m_clients;

	/** The commands. */
	private Hashtable<String, Module>	m_commands;

	/** The decoder. */
	private CharsetDecoder				m_decoder;

	/** The encoder. */
	private CharsetEncoder				m_encoder;

	/** The encryption system. */
	private EncryptionSystem			m_encryption;

	/** The port. */
	private int							m_port;

	/** The receive buffer size. */
	private long						m_receiveBufferSize;

	/** The_run state. */
	private boolean						m_run;

	/** The selector. */
	private Selector					m_selector;

	/** The separator. */
	private String						m_separator;

	/** The server socket. */
	private ServerSocket				m_serverSocket;

	/**
	 * Instantiates a new network.
	 * 
	 * @param settings the settings
	 */
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
		m_encryption = new RSAEncryption(this);

		// Create listening socket
		try
		{
			ServerSocketChannel serverSocketChannel;
			serverSocketChannel = ServerSocketChannel.open();
			m_serverSocket = serverSocketChannel.socket();
			m_serverSocket.bind(new InetSocketAddress(m_port));
			m_serverSocket.setReuseAddress(true);
			System.out.println("Listening to port " + m_port);

			// Create selector
			m_selector = Selector.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(m_selector, SelectionKey.OP_ACCEPT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates the client.
	 * 
	 * @param address the address
	 * @return the client
	 */
	public Client createClient(Address address)
	{
		try
		{
			System.out.println("trying to connect ...");
			InetSocketAddress host = new InetSocketAddress(address.getHost(), address.getPort());
			SocketChannel channel = SocketChannel.open(host);
			channel.configureBlocking(false);
			Socket socket = channel.socket();

			if (socket.isConnected())
			{
				System.out.println("connected ...");

				// Create client
				Client client = new Client();
				client.setSocket(socket);
				client.getSocket().setTcpNoDelay(true);
				client.getUserData().getUserIdentifier().setAddress(address);
				channel.configureBlocking(false);
				m_encryption.onConnect(channel, client);

				m_clients.add(client);

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
		catch (UnresolvedAddressException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
		return null;
	}

	/**
	 * Gets the clients.
	 * 
	 * @return the clients
	 */
	public List<Client> getClients()
	{
		return m_clients;
	}

	/**
	 * Gets the command.
	 * 
	 * @param packet the packet
	 * @return the command
	 */
	public String getCommand(String packet)
	{
		return packet.split(m_separator)[0];
	}

	/**
	 * Gets the separator.
	 * 
	 * @return the fields separator
	 */
	public String getSeparator()
	{
		return m_separator;
	}

	/**
	 * Listen.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void listen() throws IOException
	{
		while (m_run)
		{
			m_selector.select();
			Set<SelectionKey> keys = m_selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext())
			{
				SelectionKey key = it.next();
				if (key.isAcceptable())
				{
					// Create client
					Client client = new Client();
					client.setSocket(m_serverSocket.accept());
					client.getSocket().setTcpNoDelay(true);
					client.getUserData().getUserIdentifier().setAddress(new Address(client.getSocket().getInetAddress().getHostName().toString(), m_port));
					m_clients.add(client);

					// Register client socket
					SocketChannel sc = client.getSocket().getChannel();
					sc.configureBlocking(false);
					sc.register(m_selector, SelectionKey.OP_READ, client);

					m_encryption.onClientConnect(client);
				}
				else if (key.isReadable())
				{
					SocketChannel channel = (SocketChannel) key.channel();
					Client client = (Client) key.attachment();

					if (!read(channel, client))
					{
						key.cancel();
						m_clients.remove(client);
					}
				}
			}
			keys.clear();
		}
		m_serverSocket.close();
		System.out.println("Stopped listening to port " + m_port);
	}

	/**
	 * Make packet.
	 * 
	 * @param command the command
	 * @param data the data
	 * @return the string
	 */
	public String makePacket(String command, Object data)
	{
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class");
		return command + " " + serializer.serialize(data);
	}

	/**
	 * Parses the command.
	 * 
	 * @param client the client
	 * @param packet the packet
	 * @return true, if successful
	 */
	private boolean parseCommand(Client client, String packet)
	{
		System.out.println("Parsing command : " + packet);

		Module module = m_commands.get(getCommand(packet));
		if (module == null)
		{
			System.err.println("no module set for command " + getCommand(packet));
			return false;
		}

		module.executeCommand(packet, client);

		return true;
	}

	/**
	 * Parses the packet.
	 * 
	 * @param <T> the generic type
	 * @param packet the packet
	 * @param c the c
	 * @return the t
	 */
	public <T> T parsePacket(String packet, Class<T> c)
	{
		String command = packet.split(m_separator)[0];
		String data = packet.substring(command.length() + m_separator.length());
		return new JSONDeserializer<T>().use(null, c).deserialize(data);
	}

	/**
	 * Parses the packet into.
	 * 
	 * @param <T> the generic type
	 * @param packet the packet
	 * @param c the c
	 * @param o the o
	 */
	public <T> void parsePacketInto(String packet, Class<T> c, T o)
	{
		String command = packet.split(m_separator)[0];
		String data = packet.substring(command.length() + m_separator.length());
		new JSONDeserializer<T>().use(null, c).deserializeInto(data, o);
	}

	/**
	 * Read.
	 * 
	 * @param channel the channel
	 * @param client the client
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean read(SocketChannel channel, Client client) throws IOException
	{
		int readSize = -1;
		ByteBuffer lengthBuffer = ByteBuffer.allocate(8);
		try
		{
			readSize = channel.read(lengthBuffer);
		}
		catch (IOException e)
		{
			return false;
		}

		if (readSize < 0) // Client disconnected
			return false;
		else
		{
			String command = "";
			long leftToRead = lengthBuffer.getLong(0);

			System.out.println("*** Reception : " + leftToRead + " bytes ***");

			while (leftToRead > 0 && readSize >= 0)
			{
				ByteBuffer dataBuffer = ByteBuffer.allocate((int) Math.min(m_receiveBufferSize, leftToRead));
				readSize = channel.read(dataBuffer);
				leftToRead -= readSize;
				System.out.println("read " + readSize + " bytes, left to read : " + leftToRead);
				dataBuffer.position(0);
				command += Operations.trimString(m_decoder.decode(dataBuffer).toString(), (char) 0);
			}

			System.out.println("Received packet (raw) : " + command);
			command = m_encryption.onReceive(client, command);
			if (command != null)
				parseCommand(client, command);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
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

	/**
	 * Send.
	 * 
	 * @param packet the packet
	 * @param client the client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void send(String packet, Client client) throws IOException
	{
		try
		{
			packet = m_encryption.onSend(client, packet);

			ByteBuffer dataBuffer = m_encoder.encode(CharBuffer.wrap(packet));
			ByteBuffer lengthBuffer = ByteBuffer.allocate(8).putLong(dataBuffer.array().length);

			System.out.println("*** Sending : " + dataBuffer.array().length + " bytes ***");
			System.out.println("Sending packet (raw) : " + packet);

			ByteBuffer packetBuffer = ByteBuffer.wrap(Operations.mergeByteBuffers(lengthBuffer.array(), dataBuffer.array()));
			client.getSocket().getChannel().write(packetBuffer);
		}
		catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Send.
	 * 
	 * @param packet the packet
	 * @param clientList the client list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void send(String packet, Iterable<Client> clientList) throws IOException
	{
		for (Client client : clientList)
		{
			send(packet, client);
		}
	}

	/**
	 * Sets the command.
	 * 
	 * @param command the command
	 * @param module the module
	 * @return true, if successful
	 */
	public boolean setCommand(String command, Module module)
	{
		return m_commands.put(command, module) != null;
	}

	/**
	 * Sets the separator.
	 * 
	 * @param separator the fields separator to set
	 */
	public void setSeparator(String separator)
	{
		m_separator = separator;
	}

	/**
	 * Stop listen.
	 */
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
}