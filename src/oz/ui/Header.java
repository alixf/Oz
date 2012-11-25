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

import oz.data.UserData;

public class Header extends Composite
{
	public Header(Shell shell, UserData user)
	{
		super(shell, SWT.NONE);

		m_shell = shell;
		m_display = shell.getDisplay();

		setLayout(new FormLayout());

		// Images
		Image logo64 = new Image(m_display, "images/logo-64.png");

		// Logo
		Label logoLabel = new Label(this, SWT.NONE);
		logoLabel.setImage(logo64);
		FormData logoLabelData = new FormData();
		logoLabelData.left = new FormAttachment(0, 0);
		logoLabelData.top = new FormAttachment(0, 0);
		logoLabel.setLayoutData(logoLabelData);

		// Separator Label
		m_separatorLabel = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		FormData separatorLabelData = new FormData();
		separatorLabelData.left = new FormAttachment(0, 5);
		separatorLabelData.right = new FormAttachment(100, -5);
		separatorLabelData.top = new FormAttachment(logoLabel, 0, SWT.BOTTOM);
		m_separatorLabel.setLayoutData(separatorLabelData);

		m_menuLeftAttachment = new FormAttachment(logoLabel, 5, SWT.RIGHT);

		// Settings button
		Image settingsImage = new Image(m_display, "images/gear.png");
		Button settingsButton = new Button(this, SWT.PUSH);
		settingsButton.setImage(settingsImage);
		FormData settingsButtonData = new FormData();
		settingsButtonData.right = new FormAttachment(100, -5);
		settingsButtonData.top = new FormAttachment(0, 5);
		settingsButtonData.bottom = new FormAttachment(m_separatorLabel, -5, SWT.TOP);
		settingsButton.setLayoutData(settingsButtonData);

		// Logo
		Image avatarImage = new Image(m_display, user.getAvatar() == null ? "images/avatar.png" : user.getAvatar());
		Label avatarLabel = new Label(this, SWT.BORDER);
		FormData avatarLabelData = new FormData();
		avatarLabelData.top = new FormAttachment(0, 7);
		avatarLabelData.right = new FormAttachment(settingsButton, -100, SWT.LEFT);
		avatarLabel.setImage(avatarImage);
		avatarLabel.setLayoutData(avatarLabelData);

		// Name label
		Label nameLabel = new Label(this, SWT.NONE);
		nameLabel.setText(user.getBiography().getFirstName() + " " + user.getBiography().getLastName());
		FormData nameLabelData = new FormData();
		nameLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		nameLabelData.top = new FormAttachment(0, 10);
		nameLabel.setLayoutData(nameLabelData);

		// Name label
		m_usernameLabel = new Label(this, SWT.NONE);
		m_usernameLabel.setText("(" + user.getUsername() + ")");
		FormData usernameLabelData = new FormData();
		usernameLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		usernameLabelData.top = new FormAttachment(0, 26);
		m_usernameLabel.setLayoutData(usernameLabelData);

		// Status label
		Label statusLabel = new Label(this, SWT.NONE);
		statusLabel.setText("Online");
		FormData statusLabelData = new FormData();
		statusLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		statusLabelData.top = new FormAttachment(0, 43);
		statusLabel.setLayoutData(statusLabelData);

		layout();
	}

	public Button addMenu(String name)
	{
		// Profile button
		Button menuButton = new Button(this, SWT.PUSH);
		menuButton.setText(name);
		FormData profileButtonData = new FormData();
		profileButtonData.top = new FormAttachment(0, 5);
		profileButtonData.left = m_menuLeftAttachment;
		profileButtonData.bottom = new FormAttachment(m_separatorLabel, -5, SWT.TOP);
		menuButton.setLayoutData(profileButtonData);
		m_menuLeftAttachment = new FormAttachment(menuButton, 5, SWT.RIGHT);
		layout();

		return menuButton;
	}

	Display			m_display;
	Shell			m_shell;
	Label			m_separatorLabel;
	FormAttachment	m_menuLeftAttachment;
	Label			m_usernameLabel;
}
