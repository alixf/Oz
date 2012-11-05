package ui;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class UI
{	
	public UI()
	{
		m_display = new Display();
		m_shell = new Shell(m_display);

		FormLayout layout = new FormLayout();
		m_shell.setText("Oz : Share your world");
		m_shell.setLayout(layout);

		m_header = new Header(m_display, m_shell);
		
		m_shell.open();
	}
	
	public void run()
	{
		while (!m_shell.isDisposed())
		{
			if (!m_display.readAndDispatch())
				m_display.sleep();
		}
		m_display.dispose();
	}
	
	private Display m_display;
	private Shell m_shell;
	private Header m_header;
}
