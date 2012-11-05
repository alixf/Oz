import ui.UI;

public class Oz
{
	public static void main(String[] args)
	{
		Oz oz = new Oz();
		oz.run();
	}
	
	public Oz()
	{
		m_ui = new UI();
	}

	public void run()
	{
		m_ui.run();
	}
	
	UI m_ui;
}
