package oz;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import oz.data.UserData;
import oz.ui.UI;
import oz.network.Network;
import oz.modules.*;
import oz.modules.contacts.Contacts;
import oz.modules.messages.Messages;

public class Oz
{
	public static void main(String[] args)
	{
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : 0;

		Oz oz = new Oz(port);
		oz.run();
	}

	public Oz(int port)
	{
		/*
		 * Load settings
		 */
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

		/*
		 * Create Display
		 */
		m_display = new Display();

		/*
		 * Create user profile
		 */
		m_user = Login.login(m_display);
		if (m_user != null)
		{
			/*
			 * Modules
			 */
			m_network = new Network(m_settings);
			m_ui = new UI(m_display, m_user);
			Files files = new Files(m_network);
			Contacts contacts = new Contacts(m_network, m_ui, m_user, files);
			@SuppressWarnings("unused")
			Messages messages = new Messages(m_network, m_ui, m_user, contacts);
		}
	}

	public void run()
	{
		if (m_user != null)
		{
			m_network.start();
			m_ui.run();
			m_display.dispose();
			m_network.stopListen();
		}
	}

	Settings	m_settings;
	Display		m_display;
	UserData	m_user;
	UI			m_ui;
	Network		m_network;
}
