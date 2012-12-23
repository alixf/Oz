package oz.modules.about;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class AboutView
{
	private final static int VMARGIN = 5;

	public AboutView()
	{
		m_shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_shell.setImage(logo16);
		m_shell.setText("Oz : À propos");
		m_shell.setLayout(new FormLayout());
		m_topAttachment = new FormAttachment(0, VMARGIN);

		displayAbout();

		m_shell.pack();
	}

	private void displayAbout()
	{
		String aboutString = "À propos de Oz.\n";
		aboutString += "Oz est un réseau social décentralisé.\n";
		aboutString += "\n";
		aboutString += "Votre adresse IP sur le réseau local : " + getIPAddress() + "\n";
		aboutString += "Votre adresse IP publique : " + getPublicIPAddress() + "\n";
		Label label = new Label(m_shell, SWT.NONE);
		label.setText(aboutString);
		FormData layoutData = new FormData();
		layoutData.top = m_topAttachment;
		label.setLayoutData(layoutData);
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
	private FormAttachment m_topAttachment;

	Spinner m_networkPort;
	Text m_trackerAddress;
	Spinner m_trackerPort;
}
