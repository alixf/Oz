package oz.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Header extends Composite
{
	private static final int	HMARGIN	= 5;
	private static final int	VMARGIN	= 5;

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

	public FormAttachment getLeftAttachment()
	{
		return m_leftAttachment;
	}

	public void setLeftAttachment(FormAttachment attachment)
	{
		m_leftAttachment = attachment;
	}

	public FormAttachment getRightAttachment()
	{
		return m_rightAttachment;
	}

	public void setRightAttachment(FormAttachment attachment)
	{
		m_rightAttachment = attachment;
	}

	public FormAttachment getTopAttachment()
	{
		return new FormAttachment(0, VMARGIN);
	}

	public FormAttachment getBottomAttachment()
	{
		return new FormAttachment(m_separatorLabel, -VMARGIN, SWT.TOP);
	}

	public int getHorizontalMargin()
	{
		return HMARGIN;
	}

	public int getVerticalMargin()
	{
		return VMARGIN;
	}

	Display			m_display;
	Shell			m_shell;
	Label			m_separatorLabel;
	FormAttachment	m_leftAttachment;
	FormAttachment	m_rightAttachment;
	Label			m_usernameLabel;
}
