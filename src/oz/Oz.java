package oz;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;

import oz.modules.Files;
import oz.modules.about.About;
import oz.modules.contacts.Contacts;
import oz.modules.messages.Messages;
import oz.modules.profile.Profile;
import oz.modules.settings.Settings;
import oz.network.Network;
import oz.ui.UI;

/**
 * The Main class of Oz.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Oz
{

	/**
	 * The main method.
	 * 
	 * @param args program arguments
	 */
	public static void main(String[] args)
	{
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : 0;

		Oz oz = new Oz(port);
		oz.run();
	}

	/** The display. */
	Display		m_display;

	/** The network. */
	Network		m_network;

	/** The settings. */
	Settings	m_settings;

	/** The ui. */
	UI			m_ui;

	/**
	 * Instantiates a new Oz program.
	 * 
	 * @param port the port
	 */
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
			About about = new About(m_ui);
			m_settings.setUI(m_ui);
			Files files = new Files(m_network);
			Profile profile = new Profile(m_ui);
			Contacts contacts = new Contacts(m_network, m_ui, profile, files);
			Messages messages = new Messages(m_network, m_ui, contacts);

			contacts.retrieveContacts();
		}
	}

	/**
	 * Run the program.
	 */
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
}
