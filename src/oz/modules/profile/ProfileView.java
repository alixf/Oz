package oz.modules.profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import oz.User;
import oz.data.UserData;
import oz.tools.Images;

public class ProfileView extends Composite
{
	public ProfileView(final Profile profile, Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FormLayout());

		/*
		 * Font
		 */
		FontData fontData = parent.getFont().getFontData()[0];
		fontData.height *= 2;
		Font font = new Font(Display.getCurrent(), fontData);

		/*
		 * Edit Button
		 */
		m_editButton = new Button(this, SWT.PUSH);
		m_editButton.setText("Modifier mon profil");
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 5);
		layoutData.right = new FormAttachment(100, -5);
		m_editButton.setLayoutData(layoutData);
		m_editButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				profile.showEditView();
			}
		});

		/*
		 * Separator Label
		 */
		m_separatorLabel = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, 5);
		layoutData.right = new FormAttachment(100, -5);
		layoutData.top = new FormAttachment(m_editButton, 5, SWT.BOTTOM);
		m_separatorLabel.setLayoutData(layoutData);

		/*
		 * Avatar label
		 */
		m_avatarLabel = new Label(this, SWT.BORDER);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_separatorLabel, 10, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, 10);
		layoutData.width = 128;
		layoutData.height = 128;
		m_avatarLabel.setLayoutData(layoutData);

		/*
		 * Name label
		 */
		m_nameLabel = new Label(this, SWT.NONE);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_avatarLabel, 10, SWT.TOP);
		layoutData.left = new FormAttachment(m_avatarLabel, 10, SWT.RIGHT);
		m_nameLabel.setLayoutData(layoutData);
		m_nameLabel.setFont(font);

		/*
		 * Username label
		 */
		m_usernameLabel = new Label(this, SWT.NONE);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_nameLabel, 10, SWT.BOTTOM);
		layoutData.left = new FormAttachment(m_avatarLabel, 10, SWT.RIGHT);
		m_usernameLabel.setLayoutData(layoutData);
		m_usernameLabel.setFont(font);
	}

	public void setUser(UserData user)
	{
		boolean currentUserProfile = user.getUserIdentifier().equals(User.getUser().getUserIdentifier());
		m_nameLabel.setText(user.getBiography().getFirstName() + " " + user.getBiography().getLastName());
		m_usernameLabel.setText("(" + user.getUsername() + ")");
		m_avatarLabel.setImage(Images.resize(new Image(Display.getCurrent(), user.getAvatarFilename()), 128, 128));
		m_editButton.setVisible(currentUserProfile);
		m_separatorLabel.setVisible(currentUserProfile);
		((FormData) m_avatarLabel.getLayoutData()).top = currentUserProfile ? new FormAttachment(m_separatorLabel, 10, SWT.BOTTOM) : new FormAttachment(0, 10);
		layout();
	}

	Button	m_editButton;
	Label	m_separatorLabel;
	Label	m_nameLabel;
	Label	m_usernameLabel;
	Label	m_avatarLabel;
}
