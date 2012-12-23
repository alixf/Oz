package oz;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import oz.data.UserData;
import oz.security.XOR;

import com.eaio.uuid.UUID;

import flexjson.JSONSerializer;

/**
 * This class is used to create new users
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Register
{

	/** Horizontal margin of the UI */
	private static final int	HMARGIN	= 10;

	/** Vertical margin of the UI */
	private static final int	VMARGIN	= 10;

	/**
	 * Attempt to register a new user
	 */
	static public void register()
	{
		Register register = new Register();
		register.run();
	}

	/** The shell. */
	private Shell	m_shell;

	/**
	 * Instantiates a new registering module.
	 */
	public Register()
	{
		/*
		 * UI
		 */
		// Create window
		m_shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM & (~SWT.RESIZE));

		// Set window properties
		m_shell.setText("Oz : Share your world - Créer un profil");
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_shell.setImage(logo16);
		m_shell.setLayout(new FormLayout());

		/*
		 * Username
		 */
		final Label usernameLabel = new Label(m_shell, SWT.NONE);
		usernameLabel.setText("Nom d'utilisateur :");
		usernameLabel.setAlignment(SWT.RIGHT);
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.top = new FormAttachment(0, VMARGIN);
		layoutData.width = 150;
		usernameLabel.setLayoutData(layoutData);
		final Text usernameText = new Text(m_shell, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(usernameLabel, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(usernameLabel, 0, SWT.TOP);
		layoutData.width = 150;
		usernameText.setLayoutData(layoutData);

		/*
		 * First Name
		 */
		final Label firstNameLabel = new Label(m_shell, SWT.NONE);
		firstNameLabel.setText("Prénom :");
		firstNameLabel.setAlignment(SWT.RIGHT);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.top = new FormAttachment(usernameText, VMARGIN, SWT.BOTTOM);
		layoutData.width = 150;
		firstNameLabel.setLayoutData(layoutData);
		final Text firstNameText = new Text(m_shell, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(firstNameLabel, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(firstNameLabel, 0, SWT.TOP);
		layoutData.width = 150;
		firstNameText.setLayoutData(layoutData);

		/*
		 * Last Name
		 */
		final Label lastNameLabel = new Label(m_shell, SWT.NONE);
		lastNameLabel.setText("Nom :");
		lastNameLabel.setAlignment(SWT.RIGHT);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.top = new FormAttachment(firstNameText, VMARGIN, SWT.BOTTOM);
		layoutData.width = 150;
		lastNameLabel.setLayoutData(layoutData);
		final Text lastNameText = new Text(m_shell, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(lastNameLabel, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(lastNameLabel, 0, SWT.TOP);
		layoutData.width = 150;
		lastNameText.setLayoutData(layoutData);

		/*
		 * Password
		 */
		final Label passwordLabel = new Label(m_shell, SWT.NONE);
		passwordLabel.setText("Mot de passe :");
		passwordLabel.setAlignment(SWT.RIGHT);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.top = new FormAttachment(lastNameText, VMARGIN, SWT.BOTTOM);
		layoutData.width = 150;
		passwordLabel.setLayoutData(layoutData);
		final Text passwordText = new Text(m_shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(passwordLabel, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(passwordLabel, 0, SWT.TOP);
		layoutData.width = 150;
		passwordText.setLayoutData(layoutData);

		/*
		 * Confirm Password
		 */
		final Label confirmPasswordLabel = new Label(m_shell, SWT.NONE);
		confirmPasswordLabel.setText("Confirmation :");
		confirmPasswordLabel.setAlignment(SWT.RIGHT);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.top = new FormAttachment(passwordText, VMARGIN, SWT.BOTTOM);
		layoutData.width = 150;
		confirmPasswordLabel.setLayoutData(layoutData);
		final Text confirmPasswordText = new Text(m_shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(confirmPasswordLabel, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(confirmPasswordLabel, 0, SWT.TOP);
		layoutData.width = 150;
		confirmPasswordText.setLayoutData(layoutData);

		/*
		 * Confirm button
		 */
		final Button confirmButton = new Button(m_shell, SWT.PUSH);
		confirmButton.setText("Créer");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(confirmPasswordText, VMARGIN, SWT.BOTTOM);
		confirmButton.setLayoutData(layoutData);

		/*
		 * Cancel button
		 */
		final Button cancelButton = new Button(m_shell, SWT.PUSH);
		cancelButton.setText("Annuler");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(confirmButton, VMARGIN, SWT.BOTTOM);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		cancelButton.setLayoutData(layoutData);

		/*
		 * Events
		 */
		confirmButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				boolean error = false;
				if (usernameText.getText().length() < 4)
				{

					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Le nom d'utilisateur doit faire au moins 4 caractères");
					messageBox.open();
					error = true;
				}
				if (firstNameText.getText().length() < 1)
				{

					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Le prénom ne peut être vide");
					messageBox.open();
					error = true;
				}
				if (lastNameText.getText().length() < 1)
				{

					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Le nom ne peut être vide");
					messageBox.open();
					error = true;
				}
				if (passwordText.getText().length() < 6)
				{

					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Le mot de passe doit faire au moins 6 caractères");
					messageBox.open();
					error = true;
				}
				if (!passwordText.getText().equals(confirmPasswordText.getText()))
				{

					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Les mots de passe ne correspondent pas");
					messageBox.open();
					error = true;
				}

				if (!error)
				{
					// Create user
					UserData user = new UserData();
					user.setUsername(usernameText.getText());
					user.getUserIdentifier().setUUID(new UUID().toString());
					user.getBiography().setFirstName(firstNameText.getText());
					user.getBiography().setLastName(lastNameText.getText());

					JSONSerializer serializer = new JSONSerializer();
					serializer.exclude("*.class");

					try
					{
						File profileFile = new File("users/" + usernameText.getText() + "/" + usernameText.getText() + ".ozp");
						profileFile.getParentFile().mkdirs();
						profileFile.createNewFile();
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(profileFile));
						bos.write(XOR.encrypt(serializer.serialize(user), passwordText.getText()));
						bos.flush();
						bos.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					m_shell.close();
				}
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				m_shell.close();
			}
		});

		m_shell.pack();
	}

	/**
	 * Run the registering module.
	 */
	public void run()
	{
		m_shell.open();
		while (!m_shell.isDisposed())
		{
			if (!Display.getCurrent().readAndDispatch())
				Display.getCurrent().sleep();
		}
	}
}