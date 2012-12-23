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
import oz.modules.Files;
import oz.modules.Module;
import oz.modules.profile.Profile;
import oz.network.Client;
import oz.network.Network;
import oz.ui.UI;

// TODO: Auto-generated Javadoc
/**
 * The Class Contacts.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Contacts implements Module
{

	/** The m_files. */
	Files			m_files;

	/** The m_network. */
	Network			m_network;

	/** The m_profile. */
	Profile			m_profile;

	/** The m_ui. */
	UI				m_ui;

	/** The m_view. */
	ContactsView	m_view;

	/**
	 * Instantiates a new contacts.
	 * 
	 * @param network the network
	 * @param ui the ui
	 * @param profile the profile
	 * @param files the files
	 */
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
			@Override
			public void run()
			{
				Button button = m_ui.getHeader().addMenu("Contacts");
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
	 * Adds the contact.
	 * 
	 * @param address the address
	 * @return the client
	 */
	public Client addContact(final Address address)
	{
		String packet = m_network.makePacket("ADD", User.getUser());

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

	/**
	 * Adds the file request.
	 * 
	 * @param client the client
	 * @param request the request
	 */
	public void addFileRequest(Client client, String request)
	{
		m_files.addFileRequest(client, request, m_view);
	}

	/**
	 * Check add friend.
	 * 
	 * @param client the client
	 * @return the int
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.modules.Module#executeCommand(java.lang.String, oz.network.Client)
	 */
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
					@Override
					public void run()
					{
						m_view.createContactWidget(client.getUserData().getUserIdentifier(), client);
					}
				});
				break;
			case 2: // Already existing friend
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					@Override
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
					@Override
					public void run()
					{
						m_view.createContactWidget(client.getUserData().getUserIdentifier(), client);
					}
				});
				break;
			case 2: // Already existing friend
				m_ui.getDisplay().asyncExec(new Runnable()
				{
					@Override
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
				m_network.send(m_network.makePacket("USER", User.getUser()), client);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * Gets the contact list.
	 * 
	 * @return the contact list
	 */
	public List<Client> getContactList()
	{
		return m_network.getClients();
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
	 * Gets the profile.
	 * 
	 * @return the profile
	 */
	public Profile getProfile()
	{
		return m_profile;
	}

	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public Widget getView()
	{
		return m_view;
	}

	/**
	 * Retrieve contacts.
	 */
	public void retrieveContacts()
	{
		ContactRetriever contactRetriever = new ContactRetriever(m_network, m_view);
		contactRetriever.run();
	}
}
