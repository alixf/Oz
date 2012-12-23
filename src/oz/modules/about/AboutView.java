package oz.modules.about;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class AboutView
{
	private final static int VMARGIN = 15;
	private final static int HMARGIN = 15;

	public AboutView()
	{
		m_shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_shell.setImage(logo16);
		m_shell.setText("Oz : À propos");
		m_shell.setLayout(new FormLayout());
		m_shell.setSize(500,500);
		displayAbout();

		m_shell.pack();
	}

	private void displayAbout()
	{
		String aboutString = "<h1>À propos de Oz</h1>";
		aboutString += "Oz est un réseau social décentralisé.<br/>";
		aboutString += "Développé par <i>Alix FUMOLEAU</i> et <i>Jean BATISTA</i>.<br/><br/>";
		aboutString += "Votre adresse IP sur le réseau local : " + getIPAddress() + "<br/>";
		aboutString += "Votre adresse IP publique : " + getPublicIPAddress() + "<br/><br/>";
		aboutString += "<a href=\"https://github.com/eolhing/Oz\">Dépôt Github</a><br/>";
		aboutString += "<a href=\"http://eolhing.github.com/Oz/\">Documentation</a><br/>";
		Browser browser = new Browser(m_shell, SWT.BORDER);
		browser.setText(aboutString);
		browser.setSize(500, 300);
		browser.setBackground(new Color(null, 240, 240, 240));
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, VMARGIN);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		browser.setLayoutData(layoutData);
	}

	private String getIPAddress()
	{
		try
		{
			return java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e)
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
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Non connecté à Internet";
	}

	public void open()
	{
		m_shell.open();
	}

	private Shell m_shell;

	Spinner m_networkPort;
	Text m_trackerAddress;
	Spinner m_trackerPort;
}
