package oz.modules.contacts;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;

import oz.data.UserData;
import oz.ui.UI;
import oz.modules.Files;
import oz.modules.Module;
import oz.network.Client;
import oz.network.Network;

public class Contacts implements Module
{
	public Contacts(Network network, UI ui, UserData user, Files files)
	{
		m_network = network;
		m_ui = ui;
		m_user = user;
		m_files = files;

		// Register network commands
		m_network.setCommand("ADD", this);
		m_network.setCommand("USER", this);

		// Messages view
		m_view = new ContactsView(this, m_ui.getContent());
		// m_ui.getContent().show(m_contactsView);

		// Menu button
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				Button button = m_ui.getHeader().addMenu("Contacts");
				button.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent e)
					{
						m_ui.getContent().show(m_view);
						m_view.layout();
					}
				});
			}
		});
	}

	public void addFileRequest(Client client, String request)
	{
		m_files.addFileRequest(client, client.getUserData().getAvatar(), m_view);
	}

	@Override
	public boolean executeCommand(String command, final Client client)
	{
		String commandCode = command.split(m_network.getSeparator())[0];

		if (commandCode.equals("USER"))
		{
			client.setUserData(m_network.parsePacket(command, UserData.class));

			m_ui.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					m_view.updateContactWidget(client);
				}
			});
		}
		if (commandCode.equals("ADD"))
		{
			client.setUserData(m_network.parsePacket(command, UserData.class));

			m_ui.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					m_view.createContactWidget(client);

					try
					{
						m_network.send(m_network.makePacket("USER", m_user), client);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
		}

		return true;
	}

	public Client addContact(final String address)
	{
		String packet = m_network.makePacket("ADD", m_user);

		try
		{
			final Client client = m_network.createClient(address);
			client.getUserData().setUsername(address);
			if (client != null)
			{
				m_network.send(packet, client);

				m_ui.getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						m_view.createContactWidget(client);
					}
				});
			}
			return client;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public List<Client> getContactList()
	{
		return m_network.getClients();
	}

	public Widget getView()
	{
		return m_view;
	}

	Network			m_network;
	UI				m_ui;
	UserData		m_user;
	ContactsView	m_view;
	Files			m_files;
}