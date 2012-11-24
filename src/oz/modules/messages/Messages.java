package oz.modules.messages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import oz.data.Message;
import oz.data.UserData;

import oz.ui.UI;
import oz.modules.Module;
import oz.modules.contacts.Contacts;
import oz.network.Client;
import oz.network.Network;

public class Messages implements Module
{
	public Messages(Network network, UI ui, UserData user, Contacts contacts)
	{
		m_network = network;
		m_ui = ui;
		m_user = user;
		m_contacts = contacts;

		m_network.setCommand("MSG", this);

		// Create messages container
		m_channels = new LinkedList<Channel>();
		m_view = new MessagesView(this);

		m_dateFormat = new SimpleDateFormat("'Le' d/M/y à k:m:s", new Locale("FRANCE"));

		// Create menu button
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				Button button = m_ui.getHeader().addMenu("Messages");
				button.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						m_ui.getContent().show(m_view);
						m_view.layout();
					}
				});
			}
		});
	}

	public boolean executeCommand(String command, final Client client)
	{
		String commandCode = m_network.getCommand(command);
		if (commandCode.equals("MSG"))
		{
			final Message message = m_network.parsePacket(command, Message.class);

			// Get message's channel
			Channel channel = null;
			Iterator<Channel> it = m_channels.iterator();
			while (it.hasNext())
			{
				Channel c = it.next();
				if (c.getUniqueID().equals(message.getChannelID()))
					channel = c;
			}
			// Channel unknown, create Channel
			if (channel == null)
			{
				// New channel
				LinkedList<Client> clientList = new LinkedList<Client>();
				clientList.add(client);
				channel = createChannel(clientList);
			}

			// Add message to the channel
			channel.addMessage(client.getUserData(), message);
			if (channel == m_channel)
			{
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						m_view.addMessage(client.getUserData(), message);
					}
				});
			}

			return true;
		}

		return false;
	}

	public void sendMessage(String text)
	{
		Message message = new Message(text, new java.util.Date().getTime());
		message.setChannelID(m_channel.getUniqueID());
		String packet = m_network.makePacket("MSG", message);
		try
		{
			m_network.send(packet, m_network.getClients());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_channel.addMessage(m_user, message);
		m_view.addMessage(m_user, message);
	}

	public Channel createChannel(List<Client> clients)
	{
		if (clients.size() > 0)
		{
			// Create channel
			final Channel channel = new Channel();

			// Set clients
			Iterator<Client> it = clients.iterator();
			while (it.hasNext())
				channel.addClient(it.next());

			// Set unique id
			Random rand = new Random(new Date().getTime());
			String uniqueID = "";
			boolean unique = false;
			while (!unique)
			{
				uniqueID = m_user.getUsername() + Integer.toString(Math.abs(rand.nextInt()));
				unique = true;

				Iterator<Channel> jt = m_channels.iterator();
				while (jt.hasNext())
					if (jt.next().getUniqueID().equals(uniqueID))
						unique = false;
			}
			channel.setUniqueID(uniqueID);

			m_ui.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					m_view.addChannel(channel);
					setChannel(channel);
				}
			});

			m_channels.add(channel);
			return channel;
		}
		return null;
	}

	public Network getNetwork()
	{
		return m_network;
	}

	public UI getUI()
	{
		return m_ui;
	}

	public UserData getUser()
	{
		return m_user;
	}

	public Contacts getContactsModule()
	{
		return m_contacts;
	}

	public SimpleDateFormat getDateFormat()
	{
		return m_dateFormat;
	}

	public void setChannel(Channel channel)
	{
		m_channel = channel;
		m_view.showChannel(m_channel);
	}

	public static class Channel
	{
		public Channel()
		{
			m_clients = new LinkedList<Client>();
			m_messages = new LinkedList<UserMessage>();
		}

		public void addClient(Client client)
		{
			m_clients.add(client);
		}

		public List<Client> getClients()
		{
			return m_clients;
		}

		public void addMessage(UserData user, Message message)
		{
			m_messages.add(new UserMessage(user, message));
		}

		public LinkedList<UserMessage> getMessages()
		{
			return m_messages;
		}

		public String getUniqueID()
		{
			return m_uniqueID;
		}

		public void setUniqueID(String uniqueID)
		{
			m_uniqueID = uniqueID;
		}

		public static class UserMessage
		{
			public UserMessage(UserData user, Message message)
			{
				setUser(user);
				setMessage(message);
			}

			public Message getMessage()
			{
				return m_message;
			}

			public void setMessage(Message message)
			{
				m_message = message;
			}

			public UserData getUser()
			{
				return m_user;
			}

			public void setUser(UserData user)
			{
				m_user = user;
			}

			private Message		m_message;
			private UserData	m_user;
		}

		private LinkedList<Client>		m_clients;
		private LinkedList<UserMessage>	m_messages;
		private String					m_uniqueID;
	}

	private Network				m_network;
	private UI					m_ui;
	private UserData			m_user;
	private Contacts			m_contacts;
	private MessagesView		m_view;
	private SimpleDateFormat	m_dateFormat;
	private LinkedList<Channel>	m_channels;
	private Channel				m_channel;
}
