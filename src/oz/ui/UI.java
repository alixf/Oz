package oz.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class UI.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class UI
{

	/** The content. */
	private Content	m_content;

	/** The display. */
	private Display	m_display;

	/** The header. */
	private Header	m_header;

	/** The shell. */
	private Shell	m_shell;

	/**
	 * Instantiates a new ui.
	 * 
	 * @param display the display
	 */
	public UI(Display display)
	{
		m_display = display;
		m_shell = new Shell(m_display);

		// Images
		Image logo16 = new Image(m_display, "images/logo-16.png");

		// Set window properties
		m_shell.setText("Oz : Share your world");
		m_shell.setImage(logo16);
		m_shell.setLayout(new FormLayout());

		// Create header widget
		m_header = new Header(m_shell);
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

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public Content getContent()
	{
		return m_content;
	}

	/**
	 * Gets the display.
	 * 
	 * @return the display
	 */
	public Display getDisplay()
	{
		return m_display;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public Header getHeader()
	{
		return m_header;
	}

	/**
	 * Gets the shell.
	 * 
	 * @return the shell
	 */
	public Shell getShell()
	{
		return m_shell;
	}

	/**
	 * Run.
	 */
	public void run()
	{
		// Event loop
		while (!m_shell.isDisposed())
		{
			if (!m_display.readAndDispatch())
				m_display.sleep();
		}
	}
}
