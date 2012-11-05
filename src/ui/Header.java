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
		// Logo
		Image logoImage = new Image(display, "images/Oz-logo.png");
		Label logoLabel = new Label(shell, SWT.NONE);
		FormData logoLabelData = new FormData();
		logoLabelData.left = new FormAttachment(0, 0);
		logoLabelData.top = new FormAttachment(0, 0);
		logoLabel.setImage(logoImage);
		logoLabel.setLayoutData(logoLabelData);
		
		// Separator Label
		Label separatorLabel = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
		FormData separatorLabelData = new FormData();
		separatorLabelData.left = new FormAttachment(0, 5);
		separatorLabelData.right = new FormAttachment(100, -5);
		separatorLabelData.top = new FormAttachment(logoLabel, 0, SWT.BOTTOM);
		separatorLabel.setLayoutData(separatorLabelData);
		
		// Profile button
		Button profileButton = new Button(shell, SWT.PUSH);
		profileButton.setText("Profil");
		FormData profileButtonData = new FormData();
		profileButtonData.top = new FormAttachment(0, 5);
		profileButtonData.left = new FormAttachment(logoLabel, 5, SWT.RIGHT);
		profileButtonData.bottom = new FormAttachment(separatorLabel, -5, SWT.TOP);
		profileButton.setLayoutData(profileButtonData);
		
		// Contacts button
		Button contactButton = new Button(shell, SWT.PUSH);
		contactButton.setText("Contacts");
		FormData contactButtonData = new FormData();
		contactButtonData.top = new FormAttachment(0, 5);
		contactButtonData.left = new FormAttachment(profileButton, 5, SWT.RIGHT);
		contactButtonData.bottom = new FormAttachment(separatorLabel, -5, SWT.TOP);
		contactButton.setLayoutData(contactButtonData);
		
		// Messages button
		Button messagesButton = new Button(shell, SWT.PUSH);
		messagesButton.setText("Messages");
		FormData messagesButtonData = new FormData();
		messagesButtonData.top = new FormAttachment(0, 5);
		messagesButtonData.left = new FormAttachment(contactButton, 5, SWT.RIGHT);
		messagesButtonData.bottom = new FormAttachment(separatorLabel, -5, SWT.TOP);
		messagesButton.setLayoutData(messagesButtonData);
		
		// Settings button
		Image settingsImage = new Image(display, "images/gear.png");
		Button settingsButton = new Button(shell, SWT.PUSH);
		settingsButton.setImage(settingsImage);
		FormData settingsButtonData = new FormData();
		settingsButtonData.right = new FormAttachment (100, -5);
		settingsButtonData.top = new FormAttachment (0, 5);
		settingsButtonData.bottom = new FormAttachment (separatorLabel, -5, SWT.TOP);
		settingsButton.setLayoutData(settingsButtonData);

		// Logo
		Image avatarImage = new Image(display, "images/avatar.png");
		Label avatarLabel = new Label(shell, SWT.BORDER);
		FormData avatarLabelData = new FormData();
		avatarLabelData.top = new FormAttachment(0, 7);
		avatarLabelData.right = new FormAttachment(settingsButton, -100, SWT.LEFT);
		avatarLabel.setImage(avatarImage);
		avatarLabel.setLayoutData(avatarLabelData);
		
		// Name label
		Label nameLabel = new Label(shell, SWT.NONE);
		nameLabel.setText("Name Name");
		FormData nameLabelData = new FormData();
		nameLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		nameLabelData.top = new FormAttachment(0, 10);
		nameLabel.setLayoutData(nameLabelData);
		
		// Name label
		Label usernameLabel = new Label(shell, SWT.NONE);
		usernameLabel.setText("(username)");
		FormData usernameLabelData = new FormData();
		usernameLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		usernameLabelData.top = new FormAttachment(0, 26);
		usernameLabel.setLayoutData(usernameLabelData);

		// Status label
		Label statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setText("Online");
		FormData statusLabelData = new FormData();
		statusLabelData.left = new FormAttachment(avatarLabel, 6, SWT.RIGHT);
		statusLabelData.top = new FormAttachment(0, 43);
		statusLabel.setLayoutData(statusLabelData);
	}
}
