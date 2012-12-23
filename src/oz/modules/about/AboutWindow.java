package oz.modules.about;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class AboutWindow
{
	private final static int	VMARGIN	= 5;
	private final static int	HMARGIN	= 5;

	public AboutWindow()
	{
		/*
		 * Images
		 */
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		Image logo250 = new Image(Display.getCurrent(), "images/logo-250.png");

		/*
		 * Shell
		 */
		m_shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		m_shell.setImage(logo16);
		m_shell.setText("Oz : À propos");
		m_shell.setLayout(new FormLayout());

		/*
		 * Logo Label
		 */
		m_logoLabel = new Label(m_shell, SWT.CENTER);
		m_logoLabel.setImage(logo250);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, VMARGIN);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		m_logoLabel.setLayoutData(layoutData);

		m_textLink = new Link(m_shell, SWT.CENTER);
		m_textLink.setText("Oz est un réseau social distribué.\n" +
						"\n" +
						"Auteurs :\n" +
						"Alix FUMOLEAU\n" +
						"Jean BATISTA\n" +
						"\n" +
						"Votre adresse IP locale : " + getIPAddress() + "\n" +
						"Votre adresse IP publique : " + getPublicIPAddress() + "\n" +
						"\n" +
						"<a href=\"https://github.com/eolhing/Oz\">Dépôt Github</a>\n" +
						"<a href=\"http://eolhing.github.com/Oz/\">Documentation</a>");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_logoLabel, VMARGIN, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		m_textLink.setLayoutData(layoutData);
		m_textLink.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				if (desktop.isSupported(java.awt.Desktop.Action.BROWSE))
				{
					try
					{
						java.net.URI uri = new java.net.URI(event.text);
						desktop.browse(uri);
					}
					catch (URISyntaxException | IOException e)
					{
					}
				}
			}
		});

		m_shell.pack();
	}

	private String getIPAddress()
	{
		try
		{
			return java.net.InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private String getPublicIPAddress()
	{
		try
		{
			URL url = new URL("http://hackjack.info/ip");
			final Scanner IP = new Scanner(url.openStream());
			String IPAddress = IP.nextLine();
			IP.close();
			return IPAddress;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return "Non connecté à Internet";
	}

	public void open()
	{
		m_shell.open();
	}

	private Shell	m_shell;
	private Label	m_logoLabel;
	private Link	m_textLink;

	Spinner			m_networkPort;
	Text			m_trackerAddress;
	Spinner			m_trackerPort;
}
