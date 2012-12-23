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

import oz.User;
import oz.data.Message;
import oz.data.UserData;
import oz.modules.Module;
import oz.modules.contacts.Contacts;
import oz.network.Client;
import oz.network.Network;
import oz.ui.UI;

// TODO: Auto-generated Javadoc
/**
 * The Class Messages.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Messages implements Module
{

	/**
	 * The Class Channel.
	 */
	public static class Channel
	{

		/**
		 * The Class UserMessage.
		 */
		public static class UserMessage
		{

			/** The m_message. */
			private Message		m_message;

			/** The m_user. */
			private UserData	m_user;

			/**
			 * Instantiates a new user message.
			 * 
			 * @param user the user
			 * @param message the message
			 */
			public UserMessage(UserData user, Message message)
			{
				setUser(user);
				setMessage(message);
			}

			/**
			 * Gets the message.
			 * 
			 * @return the message
			 */
			public Message getMessage()
			{
				return m_message;
			}

			/**
			 * Gets the user.
			 * 
			 * @return the user
			 */
			public UserData getUser()
			{
				return m_user;
			}

			/**
			 * Sets the message.
			 * 
			 * @param message the new message
			 */
			public void setMessage(Message message)
			{
				m_message = message;
			}

			/**
			 * Sets the user.
			 * 
			 * @param user the new user
			 */
			public void setUser(UserData user)
			{
				m_user = user;
			}
		}

		/** The m_clients. */
		private LinkedList<Client>		m_clients;

		/** The m_messages. */
		private LinkedList<UserMessage>	m_messages;

		/** The m_unique id. */
		private String					m_uniqueID;

		/**
		 * Instantiates a new channel.
		 */
		public Channel()
		{
			m_clients = new LinkedList<Client>();
			m_messages = new LinkedList<UserMessage>();
		}

		/**
		 * Adds the client.
		 * 
		 * @param client the client
		 */
		public void addClient(Client client)
		{
			m_clients.add(client);
		}

		/**
		 * Adds the message.
		 * 
		 * @param user the user
		 * @param message the message
		 */
		public void addMessage(UserData user, Message message)
		{
			m_messages.add(new UserMessage(user, message));
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
		 * Gets the messages.
		 * 
		 * @return the messages
		 */
		public LinkedList<UserMessage> getMessages()
		{
			return m_messages;
		}

		/**
		 * Gets the unique id.
		 * 
		 * @return the unique id
		 */
		public String getUniqueID()
		{
			return m_uniqueID;
		}

		/**
		 * Sets the unique id.
		 * 
		 * @param uniqueID the new unique id
		 */
		public void setUniqueID(String uniqueID)
		{
			m_uniqueID = uniqueID;
		}
	}

	/** The m_channel. */
	private Channel				m_channel;

	/** The m_channels. */
	private LinkedList<Channel>	m_channels;

	/** The m_contacts. */
	private Contacts			m_contacts;

	/** The m_date format. */
	private SimpleDateFormat	m_dateFormat;

	/** The m_network. */
	private Network				m_network;

	/** The m_ui. */
	private UI					m_ui;

	/** The m_view. */
	private MessagesView		m_view;

	/**
	 * Instantiates a new messages.
	 * 
	 * @param network the network
	 * @param ui the ui
	 * @param contacts the contacts
	 */
	public Messages(Network network, UI ui, Contacts contacts)
	{
		m_network = network;
		m_ui = ui;
		m_contacts = contacts;

		m_network.setCommand("MSG", this);

		// Create messages container
		m_channels = new LinkedList<Channel>();
		m_view = new MessagesView(this);

		m_dateFormat = new SimpleDateFormat("'Le' d/M/y à k:m:s", new Locale("FRANCE"));

		// Create menu button
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			@Override
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

	/**
	 * Creates the channel.
	 * 
	 * @param clients the clients
	 * @return the channel
	 */
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
				uniqueID = User.getUser().getUsername() + Integer.toString(Math.abs(rand.nextInt()));
				unique = true;

				Iterator<Channel> jt = m_channels.iterator();
				while (jt.hasNext())
					if (jt.next().getUniqueID().equals(uniqueID))
						unique = false;
			}
			channel.setUniqueID(uniqueID);

			m_ui.getDisplay().asyncExec(new Runnable()
			{
				@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.modules.Module#executeCommand(java.lang.String, oz.network.Client)
	 */
	@Override
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
				channel.setUniqueID(message.getChannelID());
			}

			// Add message to the channel
			channel.addMessage(client.getUserData(), message);
			if (channel == m_channel)
			{
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					@Override
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

	/**
	 * Gets the contacts module.
	 * 
	 * @return the contacts module
	 */
	public Contacts getContactsModule()
	{
		return m_contacts;
	}

	/**
	 * Gets the date format.
	 * 
	 * @return the date format
	 */
	public SimpleDateFormat getDateFormat()
	{
		return m_dateFormat;
	}

	/**
	 * Gets the network.
	 * 
	 * @return the network
	 */
	public Network getNetwork()
	{
		return m_network;
	}

	/**
	 * Gets the ui.
	 * 
	 * @return the ui
	 */
	public UI getUI()
	{
		return m_ui;
	}

	/**
	 * Send message.
	 * 
	 * @param text the text
	 */
	public void sendMessage(String text)
	{
		Message message = new Message(text, new java.util.Date().getTime());
		message.setChannelID(m_channel.getUniqueID());
		String packet = m_network.makePacket("MSG", message);
		try
		{
			m_network.send(packet, m_channel.getClients());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_channel.addMessage(User.getUser(), message);
		m_view.addMessage(User.getUser(), message);
	}

	/**
	 * Sets the channel.
	 * 
	 * @param channel the new channel
	 */
	public void setChannel(Channel channel)
	{
		m_channel = channel;
		m_view.showChannel(m_channel);
	}
}
