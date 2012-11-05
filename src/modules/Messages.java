package modules;

import ui.UI;
import network.Network;

public class Messages implements Module
{
	private Network m_network;
	private UI m_ui;
	
	public Messages(Network network, UI ui)
	{
		m_network = network;
		m_ui = ui;
		m_network.setCommand("MSG", this);
	}
	
	public boolean executeCommand(String command)
	{
		System.out.println(command);
		return true;
	}
}
