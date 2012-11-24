package oz;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import flexjson.JSONSerializer;

import oz.data.UserData;

public class Register
{
	private static final int	HMARGIN	= 10;
	private static final int	VMARGIN	= 10;

	static public void register(Display display)
	{
		Register register = new Register(display);
		register.run();
	}

	public Register(Display display)
	{
		/*
		 * UI
		 */
		// Create display and layout
		m_display = display;
		m_shell = new Shell(m_display, SWT.SHELL_TRIM & (~SWT.RESIZE));

		// Set window properties
		m_shell.setText("Oz : Share your world - Créer un profil");
		Image logoImage = new Image(m_display, "images/logo-150.png");
		m_shell.setImage(logoImage);
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
			public void widgetSelected(SelectionEvent event)
			{
				LinkedList<String> errors = new LinkedList<String>();
				if (usernameText.getText().length() < 4)
					errors.add("Le nom d'utilisateur doit faire au moins 4 caractères");
				if (firstNameText.getText().length() < 1)
					errors.add("Le prénom ne peut être vide");
				if (lastNameText.getText().length() < 1)
					errors.add("Le nom ne peut être vide");
				if (passwordText.getText().length() < 6)
					errors.add("Le mot de passe doit faire au moins 6 caractères");
				if (!passwordText.getText().equals(confirmPasswordText.getText()))
					errors.add("Les mots de passe ne correspondent pas");

				if (errors.size() == 0)
				{
					// Create user
					UserData user = new UserData();
					user.setUsername(usernameText.getText());
					user.getBiography().setFirstName(firstNameText.getText());
					user.getBiography().setLastName(lastNameText.getText());

					// Serialize user
					JSONSerializer serializer = new JSONSerializer();
					serializer.exclude("*.class");
					serializer.include("*");
					String str = serializer.serialize(user);

					// Write user file
					try
					{
						File profileFile = new File("profiles/" + usernameText.getText() + ".ozp");
						profileFile.createNewFile();
						BufferedOutputStream bos;
						bos = new BufferedOutputStream(new FileOutputStream(profileFile));
						bos.write(encrypt(str, passwordText.getText()));
						bos.flush();
						bos.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					m_shell.close();
				}
				else
					System.out.println(errors);
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				m_shell.close();
			}
		});

		/*
		 * Pack window
		 */
		m_shell.pack();
	}

	static public byte[] encrypt(String string, String key)
	{
		byte[] stringBytes = string.getBytes(Charset.forName("UTF-8"));
		byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));

		for (int i = 0; i < stringBytes.length; ++i)
			stringBytes[i] ^= keyBytes[i % keyBytes.length];

		return stringBytes;
	}

	public void run()
	{
		m_shell.open();
		while (!m_shell.isDisposed())
		{
			if (!m_display.readAndDispatch())
				m_display.sleep();
		}
	}

	Display	m_display;
	Shell	m_shell;
}
