package modules;

import ui.UI;
import network.Client;
import network.Network;

public class Messages implements Module
{
	private Network	m_network;
	private UI		m_ui;

	public Messages(Network network, UI ui)
	{
		m_network = network;
		m_ui = ui;
		m_network.setCommand("MSG", this);
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				m_ui.addMenu("Messages");
				m_ui.getShell().layout();
			}
		});
	}

	public boolean executeCommand(String command, Client client)
	{
		client.getSocket();
		return true;
	}
}
