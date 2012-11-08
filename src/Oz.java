import data.UserData;
import ui.UI;
import network.Network;
import modules.*;

public class Oz
{
	public static void main(String[] args)
	{
		Oz oz = new Oz();
		oz.run();
	}

	public Oz()
	{
		// Create user profile
		//TODO Load this from file maybe (encrypted with password, would act as a login)
		UserData user = new UserData();
		user.setUsername("Jim");
		user.getBiography().setFirstName("Jim");
		user.getBiography().setLastName("Raynor");

		m_network = new Network();
		m_ui = new UI(user);
		@SuppressWarnings("unused")
		Contacts contacts = new Contacts(m_network, m_ui);
		@SuppressWarnings("unused")
		Messages messages = new Messages(m_network, m_ui);
	}

	public void run()
	{
		m_network.start();
		m_ui.run();
		m_network.stopListen();
	}

	UI		m_ui;
	Network	m_network;
}
