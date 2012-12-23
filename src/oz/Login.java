package oz;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import oz.data.UserData;
import oz.security.XOR;
import flexjson.JSONDeserializer;

/**
 * This class is used to authentificate user and load user data
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Login
{

	/** Horizontal margin of the UI */
	private static final int	HMARGIN	= 10;

	/** Vertical margin of the UI */
	private static final int	VMARGIN	= 10;

	/**
	 * Attempt login
	 */
	static public void login()
	{
		Login login = new Login();
		login.run();
	}

	/** The shell. */
	Shell	m_shell;

	/**
	 * Instantiates a new login module.
	 */
	public Login()
	{
		// Create display and layout
		m_shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM & (~SWT.RESIZE));

		// Images
		Image logo150 = new Image(Display.getCurrent(), "images/logo-150.png");
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");

		// Set window properties
		m_shell.setText("Oz : Share your world - Login");
		m_shell.setImage(logo16);
		m_shell.setLayout(new FormLayout());

		Label logoLabel = new Label(m_shell, SWT.CENTER);
		logoLabel.setImage(logo150);
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(0, VMARGIN);
		logoLabel.setLayoutData(layoutData);

		final Combo username = new Combo(m_shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(logoLabel, VMARGIN, SWT.BOTTOM);
		username.setLayoutData(layoutData);

		Font font = username.getFont();
		FontData fontData = font.getFontData()[0];
		fontData.height *= 1.5;
		username.setFont(new Font(Display.getCurrent(), fontData));

		final Text password = new Text(m_shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(username, VMARGIN, SWT.BOTTOM);
		layoutData.width = 250;
		password.setLayoutData(layoutData);
		password.setFont(new Font(Display.getCurrent(), fontData));
		password.setFocus();

		final Button loginButton = new Button(m_shell, SWT.PUSH);
		loginButton.setText("Connexion");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(password, VMARGIN, SWT.BOTTOM);
		loginButton.setLayoutData(layoutData);
		loginButton.setFont(new Font(Display.getCurrent(), fontData));

		final Button signupButton = new Button(m_shell, SWT.PUSH);
		signupButton.setText("Créer un profil");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(loginButton, VMARGIN, SWT.BOTTOM);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		signupButton.setLayoutData(layoutData);
		signupButton.setFont(new Font(Display.getCurrent(), fontData));

		/*
		 * Get profiles
		 */
		String[] profiles = listProfiles();
		for (String profile : profiles)
			username.add(profile);
		if (profiles.length <= 0)
		{
			username.setEnabled(false);
			password.setEnabled(false);
			loginButton.setEnabled(false);
		}
		else
			username.select(0);

		/*
		 * Events
		 */
		loginButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (loadProfile(username.getText(), password.getText()))
					m_shell.close();
				else
				{
					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Ce mot de passe est incorrect");
					messageBox.open();
				}
			}
		});
		password.addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				if (loadProfile(username.getText(), password.getText()))
					m_shell.close();
				else
				{
					MessageBox messageBox = new MessageBox(m_shell, SWT.ICON_ERROR);
					messageBox.setMessage("Ce mot de passe est incorrect");
					messageBox.open();
				}
			}
		});
		signupButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Register.register();

				username.removeAll();
				String[] profiles = listProfiles();
				for (String profile : profiles)
					username.add(profile);

				if (profiles.length <= 0)
				{
					username.setEnabled(false);
					password.setEnabled(false);
					loginButton.setEnabled(false);
				}
				else
				{
					username.setEnabled(true);
					password.setEnabled(true);
					loginButton.setEnabled(true);
					username.select(0);
				}
			}
		});

		/*
		 * Pack window
		 */
		m_shell.pack();
	}

	/**
	 * List profiles.
	 * 
	 * @return the available profiles
	 */
	private String[] listProfiles()
	{
		File profilesDirectory = new File("users");
		profilesDirectory.mkdirs();
		String[] profiles = profilesDirectory.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File directory, String filename)
			{
				File profileDirectory = new File(directory.getPath() + "/" + filename);
				File profileFile = new File(directory.getPath() + "/" + filename + "/" + filename + ".ozp");
				return profileDirectory.isDirectory() && profileFile.exists();
			}
		});
		return profiles;
	}

	/**
	 * Load profile.
	 * 
	 * @param username the username
	 * @param password the password
	 * @return true, if successful
	 */
	public boolean loadProfile(String username, String password)
	{
		Path path = Paths.get("users/" + username + "/" + username + ".ozp");
		try
		{
			byte[] bytes = Files.readAllBytes(path);
			String str = XOR.decrypt(bytes, password);
			new JSONDeserializer<UserData>().use(null, UserData.class).deserializeInto(str, User.getUser());
			User.getUser().setPassword(password);
			User.getUser().setValid(true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	/**
	 * Run the login window
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
