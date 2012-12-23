package oz.modules.profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import oz.User;
import oz.tools.Images;

/**
 * This view is used to modify informations about the user
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class EditView extends Composite
{

	/** The avatar filename. */
	String	m_avatarFilename;

	/** The avatar label. */
	Label	m_avatarLabel;

	/** The cancel button. */
	Button	m_cancelButton;

	/** The confirm button. */
	Button	m_confirmButton;

	/** The first name text. */
	Text	m_firstNameText;

	/** The last name text. */
	Text	m_lastNameText;

	/** The separator label. */
	Label	m_separatorLabel;

	/** The username text. */
	Text	m_usernameText;

	/**
	 * Instantiates a new edits the view.
	 * 
	 * @param profile the profile
	 * @param parent the parent
	 */
	public EditView(final Profile profile, Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		User user = User.getUser();
		m_avatarFilename = user.getAvatar();

		/*
		 * Font
		 */
		FontData fontData = parent.getFont().getFontData()[0];
		fontData.height *= 2;
		Font font = new Font(Display.getCurrent(), fontData);

		/*
		 * Cancel button
		 */
		m_cancelButton = new Button(this, SWT.PUSH);
		m_cancelButton.setText("Annuler");
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 5);
		layoutData.right = new FormAttachment(100, -5);
		m_cancelButton.setLayoutData(layoutData);
		m_cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				profile.show(User.getUser());
			}
		});

		/*
		 * Confirm button
		 */
		m_confirmButton = new Button(this, SWT.PUSH);
		m_confirmButton.setText("Valider");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 5);
		layoutData.right = new FormAttachment(m_cancelButton, -5, SWT.LEFT);
		m_confirmButton.setLayoutData(layoutData);
		m_confirmButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				saveProfile(profile);
				profile.show(User.getUser());
			}
		});

		/*
		 * Separator Label
		 */
		m_separatorLabel = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, 5);
		layoutData.right = new FormAttachment(100, -5);
		layoutData.top = new FormAttachment(m_confirmButton, 5, SWT.BOTTOM);
		m_separatorLabel.setLayoutData(layoutData);

		/*
		 * Avatar label
		 */
		m_avatarLabel = new Label(this, SWT.BORDER);
		m_avatarLabel.setImage(Images.resize(new Image(Display.getCurrent(), user.getAvatarFilename()), 128, 128));
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_separatorLabel, 10, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, 10);
		layoutData.width = 128;
		layoutData.height = 128;
		m_avatarLabel.setLayoutData(layoutData);
		m_avatarLabel.addListener(SWT.MouseDown, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				String[] filterNames = new String[] { "Image Files", "All Files (*)" };
				String[] filterExtensions = new String[] { "*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*" };
				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);
				String filename = dialog.open();
				if (filename != null)
				{
					m_avatarFilename = filename;
					m_avatarLabel.setImage(Images.resize(new Image(Display.getCurrent(), m_avatarFilename), 128, 128));
				}
			}
		});

		/*
		 * First name label
		 */
		m_firstNameText = new Text(this, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_avatarLabel, 10, SWT.TOP);
		layoutData.left = new FormAttachment(m_avatarLabel, 10, SWT.RIGHT);
		layoutData.width = 200;
		m_firstNameText.setLayoutData(layoutData);
		m_firstNameText.setFont(font);
		m_firstNameText.setText(user.getBiography().getFirstName());

		/*
		 * Last name label
		 */
		m_lastNameText = new Text(this, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_avatarLabel, 10, SWT.TOP);
		layoutData.left = new FormAttachment(m_firstNameText, 10, SWT.RIGHT);
		layoutData.width = 200;
		m_lastNameText.setLayoutData(layoutData);
		m_lastNameText.setFont(font);
		m_lastNameText.setText(user.getBiography().getLastName());

		/*
		 * Username label
		 */
		m_usernameText = new Text(this, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_firstNameText, 10, SWT.BOTTOM);
		layoutData.left = new FormAttachment(m_avatarLabel, 10, SWT.RIGHT);
		layoutData.right = new FormAttachment(m_lastNameText, 0, SWT.RIGHT);
		m_usernameText.setLayoutData(layoutData);
		m_usernameText.setFont(font);
		m_usernameText.setText(user.getUsername());
		m_usernameText.setEnabled(false);

	}

	/**
	 * Save profile.
	 * 
	 * @param profile the profile
	 */
	public void saveProfile(Profile profile)
	{
		User user = User.getUser();
		user.getBiography().setFirstName(m_firstNameText.getText());
		user.getBiography().setLastName(m_lastNameText.getText());
		if (m_avatarFilename != user.getAvatar())
		{
			Path newAvatarFilePath = Paths.get(m_avatarFilename);
			Path copyPath = Paths.get("users/" + user.getUsername() + "/files/" + user.getUsername());
			File copyDirectory = new File(copyPath.toString());
			copyDirectory.mkdirs();
			Path destPath = copyPath.resolve(newAvatarFilePath.getFileName());
			try
			{
				Files.copy(newAvatarFilePath, destPath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			user.setAvatar(destPath.getFileName().toString());
		}
		user.save();
		profile.updateWidget();
	}
}
