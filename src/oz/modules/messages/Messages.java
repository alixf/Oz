package oz.modules.messages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
		// m_channels = new LinkedList<Channel>();
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

	public boolean executeCommand(String command, Client client)
	{
		String commandCode = m_network.getCommand(command);
		if (commandCode.equals("MSG"))
		{
			Message message = m_network.parsePacket(command, Message.class);
			m_view.addMessage(client.getUserData(), message);
			return true;
		}

		return false;
	}

	public void sendMessage(String text)
	{
		Message message = new Message(text, new java.util.Date().getTime());
		String packet = m_network.makePacket("MSG", message);
		try
		{
			m_network.send(packet, m_network.getClients());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_view.addMessage(m_user, message);
	}

	public void createChannel(List<Client> clients)
	{
		final Channel channel = new Channel();
		if (clients.size() > 0)
		{
			Iterator<Client> it = clients.iterator();
			while (it.hasNext())
				channel.addClient(it.next());

			m_ui.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					m_view.addChannel(channel);
				}
			});
		}
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

	class Channel
	{
		public Channel()
		{
			m_clients = new LinkedList<Client>();
		}

		public void addClient(Client client)
		{
			m_clients.add(client);
		}

		public List<Client> getClients()
		{
			return m_clients;
		}

		private LinkedList<Client>	m_clients;
	}

	private Network				m_network;
	private UI					m_ui;
	private UserData			m_user;
	private Contacts			m_contacts;
	private MessagesView		m_view;
	private SimpleDateFormat	m_dateFormat;
	// private LinkedList<Channel> m_channels;
	// private Channel m_channel;
}
