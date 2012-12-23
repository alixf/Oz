package oz.modules.contacts;

//import java.io.IOException;

import oz.User;
import oz.data.UserIdentifier;
import oz.network.Client;
import oz.network.Network;

public class ContactRetriever extends Thread
{
	ContactRetriever(Network network, ContactsView view)
	{
		m_network = network;
		m_view = view;
	}

	@Override
	public void run()
	{
		// Retrieve contacts
		for (UserIdentifier user : User.getUser().getFriends())
		{
			System.out.println("Add contact : " + user.getUsername() + " : " + user.getAddress());

			final Client client = (user.getAddress() != null) ? m_network.createClient(user.getAddress()) : null;

			if (client == null)
			{
				m_view.createContactWidget(user, null);
			}
			else
			{
				m_view.createContactWidget(user, null);
			}
		}
	}

	private Network			m_network;
	private ContactsView	m_view;
}
