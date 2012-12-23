package oz;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import oz.ui.UI;
import oz.network.Network;
import oz.modules.*;
import oz.modules.about.About;
import oz.modules.contacts.Contacts;
import oz.modules.messages.Messages;
import oz.modules.profile.Profile;
import oz.modules.settings.Settings;

public class Oz
{
	public static void main(String[] args)
	{
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : 0;

		Oz oz = new Oz(port);
		oz.run();
	}

	@SuppressWarnings("unused")
	public Oz(int port)
	{
		// Load settings
		m_settings = new Settings();
		try
		{
			m_settings.load("settings.ozs");
			if (port > 0)
				m_settings.setNetworkPort(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Create display
		m_display = new Display();

		// Log user in
		Login.login();

		// Launch modules
		if (User.getUser().isValid())
		{
			m_network = new Network(m_settings);
			m_ui = new UI(m_display);
			About about = new About();
			about.setUI(m_ui);
			m_settings.setUI(m_ui);
			Files files = new Files(m_network);
			Profile profile = new Profile(m_ui);
			Contacts contacts = new Contacts(m_network, m_ui, profile, files);
			Messages messages = new Messages(m_network, m_ui, contacts);
			Keys key = new Keys(m_network);
		}
	}

	public void run()
	{
		if (User.getUser().isValid())
		{
			m_network.start();
			m_ui.run();
			m_display.dispose();
			m_network.stopListen();
		}
	}

	Settings	m_settings;
	Display		m_display;
	UI			m_ui;
	Network		m_network;
}
