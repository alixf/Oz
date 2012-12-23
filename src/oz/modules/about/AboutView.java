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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class AboutView
{
	private final static int VMARGIN = 15;
	private final static int HMARGIN = 15;
	private Browser m_browser;

	public AboutView()
	{
		m_shell = new Shell(SWT.SHELL_TRIM);
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_shell.setImage(logo16);
		m_shell.setText("Oz : À propos");
		m_shell.setLayout(new FormLayout());
		m_shell.setSize(500, 500);
		m_browser = new Browser(m_shell, SWT.BORDER);
		displayAbout();

		m_shell.pack();
	}

	private void displayAbout()
	{
		String aboutString =
			"<html>"
					+ "<head><title>Oz - À propos</title></head>"
					+ "<body style='overflow:hidden'>"
					+ "<h1>À propos de Oz</h1>"
					+ "Oz est un réseau social décentralisé.<br/>"
					+ "Développé par <i>Alix FUMOLEAU</i> et <i>Jean BATISTA</i>.<br/><br/>"
					+ "Votre adresse IP sur le réseau local : "
					+ getIPAddress()
					+ "<br/>"
					+ "Votre adresse IP publique : "
					+ getPublicIPAddress()
					+ "<br/><br/>"
					+ "<a href=\"http://eolhing.github.com/Oz/\">Documentation</a><br/>";

		m_browser.setText(aboutString);
		m_browser.setSize(500, 300);
		m_browser.setBackground(new Color(null, 240, 240, 240));
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, VMARGIN+7);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		m_browser.setLayoutData(layoutData);
		
		ToolBar toolbar = new ToolBar(m_shell, SWT.NONE);
		ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
		itemBack.setText("Retour");
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem)event.widget;
				String string = item.getText();
				if (string.equals("Retour")) m_browser.back(); 
		   }
		};
		itemBack.addListener(SWT.Selection, listener);
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
			e.printStackTrace();
		} catch (IOException e)
		{
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