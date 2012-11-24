package oz;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import flexjson.JSONDeserializer;

import oz.data.UserData;

public class Login
{
	private static final int	HMARGIN	= 10;
	private static final int	VMARGIN	= 10;

	static public UserData login(Display display)
	{
		Login login = new Login(display);
		login.run();
		return login.getUser();
	}

	public Login(Display display)
	{
		/*
		 * UI
		 */
		// Create display and layout
		m_display = display != null ? display : new Display();
		m_shell = new Shell(m_display, SWT.SHELL_TRIM & (~SWT.RESIZE));

		// Set window properties
		m_shell.setText("Oz : Share your world - Login");
		Image logoImage = new Image(m_display, "images/logo-150.png");
		m_shell.setImage(logoImage);
		m_shell.setLayout(new FormLayout());

		Label logoLabel = new Label(m_shell, SWT.CENTER);
		logoLabel.setImage(logoImage);
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
		username.setFont(new Font(m_display, fontData));

		final Text password = new Text(m_shell, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(username, VMARGIN, SWT.BOTTOM);
		layoutData.width = 250;
		password.setLayoutData(layoutData);
		password.setFont(new Font(m_display, fontData));
		password.setFocus();

		final Button loginButton = new Button(m_shell, SWT.PUSH);
		loginButton.setText("Connexion");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(password, VMARGIN, SWT.BOTTOM);
		loginButton.setLayoutData(layoutData);
		loginButton.setFont(new Font(m_display, fontData));

		final Button signupButton = new Button(m_shell, SWT.PUSH);
		signupButton.setText("Créer un profil");
		layoutData = new FormData();
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.top = new FormAttachment(loginButton, VMARGIN, SWT.BOTTOM);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		signupButton.setLayoutData(layoutData);
		signupButton.setFont(new Font(m_display, fontData));

		/*
		 * Get profiles
		 */
		File[] profileFiles = listProfiles();
		for (File file : profileFiles)
			username.add(file.getName().substring(0, file.getName().length() - 4));
		if (profileFiles.length <= 0)
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
			public void widgetSelected(SelectionEvent e)
			{
				m_user = openProfile(username.getText(), password.getText());
				if (m_user != null)
					m_shell.close();
				// TODO Wrong password error message
			}
		});
		password.addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				m_user = openProfile(username.getText(), password.getText());
				if (m_user != null)
					m_shell.close();
				// TODO Wrong password error message
			}
		});
		signupButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				Register.register(m_display);

				username.removeAll();
				File[] profileFiles = listProfiles();
				for (File file : profileFiles)
					username.add(file.getName().substring(0, file.getName().length() - 4));
				if (profileFiles.length <= 0)
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

	private File[] listProfiles()
	{
		File profilesDirectory = new File("profiles");
		profilesDirectory.mkdirs();
		File[] profileFiles = profilesDirectory.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File directory, String filename)
			{
				return filename.substring(filename.length() - 4, filename.length()).equals(".ozp");
			}
		});
		return profileFiles;
	}

	public UserData openProfile(String username, String password)
	{
		UserData user = null;
		Path path = Paths.get("profiles/" + username + ".ozp");
		try
		{
			byte[] bytes = Files.readAllBytes(path);
			String str = decrypt(bytes, password);
			user = new JSONDeserializer<UserData>().use(null, UserData.class).deserialize(str);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			user = null;
		}

		return user;
	}

	static public String decrypt(byte[] bytes, String key)
	{
		byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));

		for (int i = 0; i < bytes.length; ++i)
			bytes[i] ^= keyBytes[i % keyBytes.length];

		return new String(bytes, Charset.forName("UTF-8"));
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

	public UserData getUser()
	{
		return m_user;
	}

	Display		m_display;
	Shell		m_shell;
	UserData	m_user;
}
