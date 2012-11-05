import ui.UI;
import network.Network;
import modules.*;

public class Oz
{
	public static void main(String[] args)
	{
		Oz oz = new Oz();
		oz.run();
	}
	
	public Oz()
	{
		m_network = new Network();
		m_ui = new UI();
		Messages messages = new Messages(m_network,m_ui);
	}

	public void run()
	{
		m_network.start();
		m_ui.run();
	}
	
	UI m_ui;
	Network m_network;
}
