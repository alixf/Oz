package oz;

import java.io.IOException;

import oz.data.UserData;
import oz.ui.UI;
import oz.network.Network;
import oz.modules.*;

public class Oz
{
	public static void main(String[] args)
	{
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : 4242;

		Oz oz = new Oz(port);
		oz.run();
	}

	public Oz(int port)
	{
		/*
		 * Create user profile
		 */
		// TODO Load this from file maybe (encrypted with password, would act as a login)
		UserData user = new UserData();
		user.setUsername("Jim");
		user.getBiography().setFirstName("Jim");
		user.getBiography().setLastName("Raynor");
		user.setAvatar("avatar.png");

		/*
		 * Load settings
		 */
		Settings settings = new Settings();
		try
		{
			settings.load("settings.ozs");
			System.out.println(settings.getNetworkPort());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		m_network = new Network(settings);
		m_ui = new UI(user);
		@SuppressWarnings("unused")
		Contacts contacts = new Contacts(m_network, m_ui, user);
		@SuppressWarnings("unused")
		Messages messages = new Messages(m_network, m_ui, user);
		@SuppressWarnings("unused")
		Files files = new Files(m_network);
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
