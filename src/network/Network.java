package network;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modules.Module;

public class Network extends Thread
{
	public Network()
	{
		m_run = true;
		m_port = 4242;
		m_receiveBufferSize = 1000;
		m_clients = new LinkedList<Client>();
		m_charset = Charset.forName("UTF-8");
		m_decoder = m_charset.newDecoder();
		m_encoder = m_charset.newEncoder();
		m_commands = new Hashtable<String, Module>();
		m_separator = "/";
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
		Selector selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);

		while (m_run)
		{
			selector.select();
			Set<SelectionKey> keys = selector.selectedKeys();
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

					// Register client socket
					SocketChannel sc = client.getSocket().getChannel();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ, client);

				}
				else if (key.isReadable())
				{
					SocketChannel channel = (SocketChannel) key.channel();
					Client client = (Client) key.attachment();

					ByteBuffer bb = ByteBuffer.allocate(m_receiveBufferSize);

					int readSize = channel.read(bb);
					if (readSize < 0) // Client disconnected
					{
						key.cancel();
						m_clients.remove(client);
					}
					else
					// Execute command
					{
						bb.position(0);
						String command = m_decoder.decode(bb).toString().substring(0, readSize - 1);
						parseCommand(client, command);
					}
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

	private void send(String packet, Client client) throws IOException
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
		System.out.println("Sending "+packet+" to clients");
		try
		{
			ByteBuffer bb = m_encoder.encode(CharBuffer.wrap(packet));

			if (clientList == null)
				clientList = m_clients;
			Iterator<Client> it = clientList.iterator();
			while (it.hasNext())
			{
				System.out.println("sent : " + packet);
				Client client = it.next();
				client.getSocket().getChannel().write(bb);
			}
		}
		catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
	}

	private boolean parseCommand(Client client, String command)
	{
		Pattern fieldsPattern = Pattern.compile("(?<!\\\\)" + m_separator);
		String commandCode = fieldsPattern.split(command)[0];

		Module module = m_commands.get(commandCode);
		if (module == null)
			return false;
		module.executeCommand(command, client);
		return true;
	}

	public boolean setCommand(String command, Module module)
	{
		return m_commands.put(command, module) != null;
	}

	private String escapePacketData(String str)
	{
		return escape(escape(str, m_separator), "\n");
	}

	private String unescapePacketData(String str)
	{
		return unescape(unescape(str, "\n"), m_separator);
	}

	public Map<String, String> parsePacket(String command) // TODO check errors
	{
		Pattern fieldsPattern = Pattern.compile("(?<!\\\\)" + m_separator);
		String[] lines = fieldsPattern.split(command);

		Pattern paramsPattern = Pattern.compile("(?<!\\\\):");

		Map<String, String> fields = new HashMap<String, String>();

		for (int i = 1; i < lines.length; i++)
		{
			String[] fieldsMembers = paramsPattern.split(lines[i]);

			if (fieldsMembers.length > 0)
			{
				String key = unescapePacketData(fieldsMembers[0].toLowerCase());

				String value = "";
				if (fieldsMembers.length > 1)
				{
					value = unescapePacketData(fieldsMembers[1]);
					for (int j = 1; j < fieldsMembers.length; j++)
						value += (':' + unescapePacketData(fieldsMembers[i]));
				}

				fields.put(key, value);
			}
		}

		return fields;
	}

	public String makePacket(String commandCode, Map<String, String> fields)
	{
		String res = escapePacketData(commandCode) + '\n';
		for (Map.Entry<String, String> entry : fields.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			res += escapePacketData(key) + ':' + escapePacketData(value) + '\n';
		}
		res += '\n';

		return res;
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

	public static String escape(String haystack, String needle)
	{
		Pattern patternNeedle = Pattern.compile(needle);
		Matcher matcherNeedle = patternNeedle.matcher(haystack);
		return matcherNeedle.replaceAll("\\\\" + needle);
	}

	public static String unescape(String haystack, String needle)
	{
		Pattern patternNeedle = Pattern.compile("\\\\" + needle);
		Matcher matcherNeedle = patternNeedle.matcher(haystack);
		return matcherNeedle.replaceAll(needle);
	}

	private boolean						m_run;
	private int							m_port;
	private int							m_receiveBufferSize;
	private List<Client>				m_clients;
	private Charset						m_charset;
	private CharsetDecoder				m_decoder;
	private CharsetEncoder				m_encoder;
	private Hashtable<String, Module>	m_commands;
	private String						m_separator;

}
