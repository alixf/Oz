package oz.modules.contacts;

//import java.io.IOException;

import oz.User;
import oz.data.UserIdentifier;
import oz.network.Client;
import oz.network.Network;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactRetriever.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class ContactRetriever extends Thread
{

	/** The m_network. */
	private Network			m_network;

	/** The m_view. */
	private ContactsView	m_view;

	/**
	 * Instantiates a new contact retriever.
	 * 
	 * @param network the network
	 * @param view the view
	 */
	ContactRetriever(Network network, ContactsView view)
	{
		m_network = network;
		m_view = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
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
}
