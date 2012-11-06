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
	
	public Display getDisplay()
	{
		return m_display;
	}

	public void setDisplay(Display display)
	{
		m_display = display;
	}
	
	public Shell getShell()
	{
		return m_shell;
	}

	public void setShell(Shell shell)
	{
		m_shell = shell;
	}
	
	public Header getHeader()
	{
		return m_header;
	}
	
	public Button addMenu(String name)
	{
		return m_header.addMenu(name);
	}
	
	private Display	m_display;
	private Shell	m_shell;
	private Header	m_header;
}
