package oz.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import oz.data.UserData;

public class UI
{
	public UI(Display display, UserData user)
	{
		// Create display and layout
		m_display = display;
		m_shell = new Shell(m_display);

		// Set window properties
		m_shell.setText("Oz : Share your world");
		Image logoImage = new Image(m_display, "images/logo-150.png");
		m_shell.setImage(logoImage);
		m_shell.setLayout(new FormLayout());

		// Create header widget
		m_header = new Header(m_shell, user);
		FormData headerLayoutData = new FormData();
		headerLayoutData.left = new FormAttachment(0, 0);
		headerLayoutData.top = new FormAttachment(0, 0);
		headerLayoutData.right = new FormAttachment(100, 0);
		m_header.setLayoutData(headerLayoutData);

		// Create content widget
		m_content = new Content(m_shell);
		FormData contentLayoutData = new FormData();
		contentLayoutData.left = new FormAttachment(0, 0);
		contentLayoutData.top = new FormAttachment(m_header, 0, SWT.BOTTOM);
		contentLayoutData.right = new FormAttachment(100, 0);
		contentLayoutData.bottom = new FormAttachment(100, 0);
		m_content.setLayoutData(contentLayoutData);

		// Open window
		m_shell.open();
	}

	public void run()
	{
		// Event loop
		while (!m_shell.isDisposed())
		{
			if (!m_display.readAndDispatch())
				m_display.sleep();
		}
	}

	public Display getDisplay()
	{
		return m_display;
	}

	public Shell getShell()
	{
		return m_shell;
	}

	public Header getHeader()
	{
		return m_header;
	}

	public Content getContent()
	{
		return m_content;
	}

	private Display	m_display;
	private Shell	m_shell;
	private Header	m_header;
	private Content	m_content;
}
