package oz.modules.contacts;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;

import oz.User;
import oz.data.Address;
import oz.data.UserData;
import oz.data.UserIdentifier;
import oz.ui.UI;
import oz.modules.Files;
import oz.modules.Module;
import oz.modules.profile.Profile;
import oz.network.Client;
import oz.network.Network;

public class Contacts implements Module
{
	public Contacts(Network network, UI ui, Profile profile, Files files)
	{
		m_network = network;
		m_ui = ui;
		m_profile = profile;
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
		m_files.addFileRequest(client, request, m_view);
	}

	@Override
	public boolean executeCommand(String command, final Client client)
	{
		String commandCode = command.split(m_network.getSeparator())[0];

		if (commandCode.equals("USER"))
		{
			Address address = client.getUserData().getUserIdentifier().getAddress();
			client.setUserData(m_network.parsePacket(command, UserData.class));
			client.getUserData().getUserIdentifier().setAddress(address);

			// Add to friends
			switch (checkAddFriend(client))
			{
			case 0: // New friend
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						m_view.createContactWidget(client.getUserData().getUserIdentifier(), client);
					}
				});
				break;
			case 2: // Already existing friend
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						m_view.updateContactWidget(client);
					}
				});
				break;
			default:
				break;
			}

		}
		if (commandCode.equals("ADD"))
		{
			Address address = client.getUserData().getUserIdentifier().getAddress();
			client.setUserData(m_network.parsePacket(command, UserData.class));
			client.getUserData().getUserIdentifier().setAddress(address);

			// Add to friends
			switch (checkAddFriend(client))
			{
			case 0: // New friend
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						m_view.createContactWidget(client.getUserData().getUserIdentifier(), client);
					}
				});
				break;
			case 2: // Already existing friend
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					public void run()
					{
						m_view.updateContactWidget(client);
					}
				});
				break;
			default:
				break;
			}

			try
			{
				m_network.send(m_network.makePacket("USER", (UserData) User.getUser()), client);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return true;
	}

	private int checkAddFriend(Client client)
	{
		System.out.println("CHECKADDFRIEND WITH UUID " + client.getUserData().getUserIdentifier().getUUID() + " AND ADDRESS " + client.getUserData().getUserIdentifier().getAddress());

		if (User.getUser().getUserIdentifier().getUUID().equals(client.getUserData().getUserIdentifier().getUUID()))
			return 1;

		for (UserIdentifier userID : User.getUser().getFriends())
		{
			if (userID.getUUID().equals(client.getUserData().getUserIdentifier().getUUID()))
			{
				userID.setAddress(client.getUserData().getUserIdentifier().getAddress());
				System.out.println("UPDATED FRIEND " + client.getUserData().getUsername());
				User.getUser().save();
				return 2;
			}
		}

		User.getUser().getFriends().add(client.getUserData().getUserIdentifier());
		System.out.println("ADDED TO FRIEND " + client.getUserData().getUsername());
		User.getUser().save();
		return 0;
	}

	public Client addContact(final Address address)
	{
		String packet = m_network.makePacket("ADD", (UserData) User.getUser());

		try
		{
			final Client client = m_network.createClient(address);
			if (client != null)
			{
				client.getUserData().setUsername(address.getHost() + ":" + address.getPort());
				client.getUserData().getUserIdentifier().setUsername(address.getHost() + ":" + address.getPort());

				// client.getUserData().getUserIdentifier().setAddress(address);
				System.out.println("CLIENT IS FOUND, ADD CONTACT WITH ADDRESS " + client.getUserData().getUserIdentifier().getAddress());

				m_network.send(packet, client);

				/*
				 * m_ui.getDisplay().asyncExec(new Runnable()
				 * {
				 * public void run()
				 * {
				 * m_view.createContactWidget(client);
				 * }
				 * });
				 */
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

	public Network getNetwork()
	{
		return m_network;
	}

	public Profile getProfile()
	{
		return m_profile;
	}

	public void retrieveContacts()
	{
		ContactRetriever contactRetriever = new ContactRetriever(m_network, m_view);
		contactRetriever.run();
	}

	Network			m_network;
	UI				m_ui;
	ContactsView	m_view;
	Profile			m_profile;
	Files			m_files;
}
