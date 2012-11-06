package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Header
{
	public Header(Display display, Shell shell)
	{
		m_display = display;
		m_shell = shell;

		// Logo
		Image logoImage = new Image(m_display, "images/Oz-logo.png");
		Label logoLabel = new Label(m_shell, SWT.NONE);
		FormData logoLabelData = new FormData();
		logoLabelData.left = new FormAttachment(0, 0);
		logoLabelData.top = new FormAttachment(0, 0);
		logoLabel.setImage(logoImage);
		logoLabel.setLayoutData(logoLabelData);

		// Separator Label
		m_separatorLabel = new Label(m_shell, SWT.HORIZONTAL | SWT.SEPARATOR);
		FormData separatorLabelData = new FormData();
		separatorLabelData.left = new FormAttachment(0, 5);
		separatorLabelData.right = new FormAttachment(100, -5);
		separatorLabelData.top = new FormAttachment(logoLabel, 0, SWT.BOTTOM);
		m_separatorLabel.setLayoutData(separatorLabelData);
		
		m_menuLeftAttachment = new FormAttachment(logoLabel, 5, SWT.RIGHT);

		// Settings button
		Image settingsImage = new Image(m_display, "images/gear.png");
		Button settingsButton = new Button(m_shell, SWT.PUSH);
		settingsButton.setImage(settingsImage);
		FormData settingsButtonData = new FormData();
		settingsButtonData.right = new FormAttachment(100, -5);
		settingsButtonData.top = new FormAttachment(0, 5);
		settingsButtonData.bottom = new FormAttachment(m_separatorLabel, -5, SWT.TOP);
		settingsButton.setLayoutData(settingsButtonData);

		// Logo
		Image avatarImage = new Image(m_display, "images/avatar.png");
		Label avatarLabel = new Label(m_shell, SWT.BORDER);
		FormData avatarLabelData = new FormData();
		avatarLabelData.top = new FormAttachment(0, 7);
		avatarLabelData.right = new FormAttachment(settingsButton, -100, SWT.LEFT);
		avatarLabel.setImage(avatarImage);
		avatarLabel.setLayoutData(avatarLabelData);

		// Name label
		Label nameLabel = new Label(m_shell, SWT.NONE);
		nameLabel.setText("Name Name");
		FormData nameLabelData = new FormData();
		nameLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		nameLabelData.top = new FormAttachment(0, 10);
		nameLabel.setLayoutData(nameLabelData);

		// Name label
		m_usernameLabel = new Label(m_shell, SWT.NONE);
		m_usernameLabel.setText("(username)");
		FormData usernameLabelData = new FormData();
		usernameLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		usernameLabelData.top = new FormAttachment(0, 26);
		m_usernameLabel.setLayoutData(usernameLabelData);

		// Status label
		Label statusLabel = new Label(m_shell, SWT.NONE);
		statusLabel.setText("Online");
		FormData statusLabelData = new FormData();
		statusLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		statusLabelData.top = new FormAttachment(0, 43);
		statusLabel.setLayoutData(statusLabelData);
	}

	public Button addMenu(String name)
	{
		// Profile button
		Button menuButton = new Button(m_shell, SWT.PUSH);
		menuButton.setText(name);
		FormData profileButtonData = new FormData();
		profileButtonData.top = new FormAttachment(0, 5);
		profileButtonData.left = m_menuLeftAttachment;
		profileButtonData.bottom = new FormAttachment(m_separatorLabel, -5, SWT.TOP);
		menuButton.setLayoutData(profileButtonData);
		m_menuLeftAttachment = new FormAttachment(menuButton, 5, SWT.RIGHT);
		
		return menuButton;
	}

	Display			m_display;
	Shell			m_shell;
	Label			m_separatorLabel;
	FormAttachment	m_menuLeftAttachment;
	Label			m_usernameLabel;
	
	public Label getNameLabel()
	{
		return m_usernameLabel;
	}
}
