package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import modules.Module;

public class Network extends Thread
{
	
	private int m_receiveBufferSize;
	private List<Client> m_clients;
	private Charset m_charset;
	private CharsetEncoder m_encoder; 
	private CharsetDecoder m_decoder;
	private Hashtable<String, Module> m_commands;
	
	/*
	 * Constructor
	 */
	
	public Network()
	{
		m_receiveBufferSize = 1000;
		m_clients = new LinkedList<Client>();
		m_charset = Charset.forName("UTF-8");
		m_encoder = m_charset.newEncoder();
		m_decoder = m_charset.newDecoder();
		m_commands = new Hashtable<String, Module>();
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
		int port = 4242;
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ServerSocket socket = ssc.socket();
		socket.bind(new InetSocketAddress(port));
		socket.setReuseAddress(true);
		System.out.println("Listening to port " + port);
	
		// Create selector
		Selector selector = Selector.open();
		ssc.configureBlocking(false);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		while (true)
		{
			selector.select
			();
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while(it.hasNext())
			{
				SelectionKey key = (SelectionKey) it.next();
				if(key.isAcceptable())
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
				else if(key.isReadable())
				{
					SocketChannel channel = (SocketChannel) key.channel();
					Client client = (Client) key.attachment();
					
					ByteBuffer bb = ByteBuffer.allocate(m_receiveBufferSize);
					
					int readSize = channel.read(bb);
					if(readSize < 0) // Client disconnected
					{
						key.cancel();
						m_clients.remove(client);
					}
					else // Execute command
					{
						bb.position(0);
						String command = m_decoder.decode(bb).toString().substring(0, readSize-1);
						parseCommand(client, command);
					}
				}
			}
			keys.clear();
		}
	}
	
	private boolean parseCommand(Client client, String command)
	{
		//TODO split command
		String commandCode = command.split(" ")[0];
		
		//TODO Hash Table
		Module module = m_commands.get(commandCode);
		if(module == null)
			return false;
		module.executeCommand(command);
		return true;
	}
	
	public boolean setCommand(String command, Module module)
	{
		return m_commands.put(command,module) != null;
	}
}
