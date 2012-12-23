package oz.modules.contacts;

//import java.io.IOException;

import oz.User;
//import oz.data.UserData;
import oz.data.UserIdentifier;
//import oz.network.Client;
import oz.network.Network;

public class ContactRetriever extends Thread
{
	ContactRetriever(Network network, ContactsView view)
	{
		// m_network = network;
		// m_view = view;
	}

	@Override
	public void run()
	{
		// Retrieve contacts
		for (UserIdentifier user : User.getUser().getFriends())
		{
			System.out.println("Add contact : " + user.getUsername() + " : " + user.getAddress());
			/*
			 * final Client client = m_network.createClient(user.getAddress());
			 * client.getUserData().setUsername(user.getUsername());
			 * 
			 * if (client != null)
			 * {
			 * client.getUserData().setUsername(user.getUsername());
			 * 
			 * String packet = m_network.makePacket("USER", (UserData) User.getUser());
			 * try
			 * {
			 * m_network.send(packet, client);
			 * }
			 * catch (IOException e)
			 * {
			 * e.printStackTrace();
			 * }
			 * 
			 * m_view.createContactWidget(client);
			 * }
			 */
		}
	}

	// private Network m_network;
	// private ContactsView m_view;
}
