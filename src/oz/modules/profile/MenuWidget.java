package oz.modules.profile;

import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import oz.data.UserData;
import oz.tools.Images;
import oz.ui.UI;

public class MenuWidget extends Composite
{
	private static final int	HMARGIN	= 5;
	private static final int	VMARGIN	= 4;

	public MenuWidget(UI ui, UserData user)
	{
		super(ui.getHeader(), SWT.BORDER);
		setLayout(new FormLayout());

		m_ui = ui;
		m_user = user;

		// Images
		Image avatarImage = Images.resize(new Image(getDisplay(), m_user.getAvatarFilename()), 64, 64);

		// Picture
		m_picture = new Label(this, SWT.BORDER);
		m_picture.setImage(avatarImage);
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 0);
		layoutData.width = 64;
		layoutData.height = 64;
		m_picture.setLayoutData(layoutData);

		// Name label
		m_name = new Label(this, SWT.NONE);
		m_name.setText(m_user.getBiography().getFirstName() + " " + m_user.getBiography().getLastName());
		layoutData = new FormData();
		layoutData.left = new FormAttachment(m_picture, HMARGIN, SWT.RIGHT);
		layoutData.top = new FormAttachment(0, VMARGIN);
		m_name.setLayoutData(layoutData);

		// Username label
		m_username = new Label(this, SWT.NONE);
		m_username.setText("(" + m_user.getUsername() + ")");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(m_picture, HMARGIN, SWT.RIGHT);
		layoutData.top = new FormAttachment(m_name, VMARGIN, SWT.BOTTOM);
		m_username.setLayoutData(layoutData);

		// Status label
		m_status = new Label(this, SWT.NONE);
		try
		{
			m_status.setText(java.net.InetAddress.getLocalHost().getHostAddress());
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		layoutData = new FormData();
		layoutData.left = new FormAttachment(m_picture, HMARGIN, SWT.RIGHT);
		layoutData.top = new FormAttachment(m_username, VMARGIN, SWT.BOTTOM);
		m_status.setLayoutData(layoutData);

		layoutData = new FormData();
		layoutData.top = m_ui.getHeader().getTopAttachment();
		layoutData.bottom = m_ui.getHeader().getBottomAttachment();
		layoutData.right = m_ui.getHeader().getRightAttachment();
		setLayoutData(layoutData);
		m_ui.getHeader().setRightAttachment(new FormAttachment(this, -m_ui.getHeader().getHorizontalMargin(), SWT.LEFT));
	}

	@Override
	public void addListener(int eventType, Listener listener)
	{
		super.addListener(eventType, listener);
		for (Control control : getChildren())
			control.addListener(eventType, listener);
	}

	public void updateData()
	{
		Image avatarImage = Images.resize(new Image(getDisplay(), m_user.getAvatarFilename()), 64, 64);
		m_picture.setImage(avatarImage);
		m_name.setText(m_user.getBiography().getFirstName() + " " + m_user.getBiography().getLastName());
		m_username.setText("(" + m_user.getUsername() + ")");
		layout();
	}

	private UI			m_ui;
	private UserData	m_user;

	private Label		m_picture;
	private Label		m_username;
	private Label		m_name;
	private Label		m_status;
}
