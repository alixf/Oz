package oz.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

// TODO: Auto-generated Javadoc
/**
 * The Class Header.
 */
public class Header extends Composite
{

	/** The Constant HMARGIN. */
	private static final int	HMARGIN	= 5;

	/** The Constant VMARGIN. */
	private static final int	VMARGIN	= 5;

	/** The m_display. */
	Display						m_display;

	/** The m_left attachment. */
	FormAttachment				m_leftAttachment;

	/** The m_right attachment. */
	FormAttachment				m_rightAttachment;

	/** The m_separator label. */
	Label						m_separatorLabel;

	/** The m_shell. */
	Shell						m_shell;

	/** The m_username label. */
	Label						m_usernameLabel;

	/**
	 * Instantiates a new header.
	 * 
	 * @param shell the shell
	 */
	public Header(Shell shell)
	{
		super(shell, SWT.NONE);

		m_shell = shell;
		m_display = shell.getDisplay();

		setLayout(new FormLayout());

		// Images
		Image logo64 = new Image(m_display, "images/logo-64.png");

		// Attachments
		m_leftAttachment = new FormAttachment(0, HMARGIN);
		m_rightAttachment = new FormAttachment(100, -HMARGIN);

		// Logo
		Label logoLabel = new Label(this, SWT.NONE);
		logoLabel.setImage(logo64);
		FormData layoutData = new FormData();
		layoutData.left = getLeftAttachment();
		layoutData.top = getTopAttachment();
		logoLabel.setLayoutData(layoutData);
		setLeftAttachment(new FormAttachment(logoLabel, getHorizontalMargin(), SWT.RIGHT));

		// Separator Label
		m_separatorLabel = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(logoLabel, VMARGIN, SWT.BOTTOM);
		m_separatorLabel.setLayoutData(layoutData);

		layout();
	}

	/**
	 * Adds the menu.
	 * 
	 * @param name the name
	 * @return the button
	 */
	public Button addMenu(String name)
	{
		// Profile button
		Button menuButton = new Button(this, SWT.PUSH);
		menuButton.setText(name);
		FormData profileButtonData = new FormData();
		profileButtonData.top = getTopAttachment();
		profileButtonData.left = getLeftAttachment();
		profileButtonData.bottom = getBottomAttachment();
		menuButton.setLayoutData(profileButtonData);
		setLeftAttachment(new FormAttachment(menuButton, HMARGIN, SWT.RIGHT));
		layout();

		return menuButton;
	}

	/**
	 * Gets the bottom attachment.
	 * 
	 * @return the bottom attachment
	 */
	public FormAttachment getBottomAttachment()
	{
		return new FormAttachment(m_separatorLabel, -VMARGIN, SWT.TOP);
	}

	/**
	 * Gets the horizontal margin.
	 * 
	 * @return the horizontal margin
	 */
	public int getHorizontalMargin()
	{
		return HMARGIN;
	}

	/**
	 * Gets the left attachment.
	 * 
	 * @return the left attachment
	 */
	public FormAttachment getLeftAttachment()
	{
		return m_leftAttachment;
	}

	/**
	 * Gets the right attachment.
	 * 
	 * @return the right attachment
	 */
	public FormAttachment getRightAttachment()
	{
		return m_rightAttachment;
	}

	/**
	 * Gets the top attachment.
	 * 
	 * @return the top attachment
	 */
	public FormAttachment getTopAttachment()
	{
		return new FormAttachment(0, VMARGIN);
	}

	/**
	 * Gets the vertical margin.
	 * 
	 * @return the vertical margin
	 */
	public int getVerticalMargin()
	{
		return VMARGIN;
	}

	/**
	 * Sets the left attachment.
	 * 
	 * @param attachment the new left attachment
	 */
	public void setLeftAttachment(FormAttachment attachment)
	{
		m_leftAttachment = attachment;
	}

	/**
	 * Sets the right attachment.
	 * 
	 * @param attachment the new right attachment
	 */
	public void setRightAttachment(FormAttachment attachment)
	{
		m_rightAttachment = attachment;
	}
}
