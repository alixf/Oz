package oz.modules.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import oz.ui.UI;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * This class is used to manage software settings
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Settings
{

	/** The network port. */
	private int		m_networkPort;

	/** The tracker address. */
	private String	m_trackerAddress;

	/** The tracker port. */
	private int		m_trackerPort;

	/** The ui. */
	private UI		m_ui;

	/**
	 * Instantiates a new settings.
	 */
	public Settings()
	{
	}

	/**
	 * Instantiates a new settings.
	 * 
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Settings(String file) throws IOException
	{
		load(file);
	}

	/**
	 * Gets the network port.
	 * 
	 * @return the network port
	 */
	public int getNetworkPort()
	{
		return m_networkPort;
	}

	/**
	 * Gets the tracker address.
	 * 
	 * @return the tracker address
	 */
	public String getTrackerAddress()
	{
		return m_trackerAddress;
	}

	/**
	 * Gets the tracker port.
	 * 
	 * @return the tracker port
	 */
	public int getTrackerPort()
	{
		return m_trackerPort;
	}

	/**
	 * Load.
	 * 
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void load(String file) throws IOException
	{
		new JSONDeserializer<Settings>().deserializeInto(readFile(file), this);
	}

	/**
	 * Open settings window.
	 */
	private void openSettingsWindow()
	{
		SettingsView settingsView = new SettingsView(this);
		settingsView.open();
	}

	/**
	 * Read file.
	 * 
	 * @param file the file
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String readFile(String file) throws IOException
	{
		FileInputStream stream = new FileInputStream(new File(file));
		try
		{
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally
		{
			stream.close();
		}
	}

	/**
	 * Save.
	 * 
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void save(String file) throws IOException
	{
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class");
		serializer.include("*");
		writeFile(file, serializer.serialize(this));
	}

	/**
	 * Sets the network port.
	 * 
	 * @param networkPort the new network port
	 */
	public void setNetworkPort(int networkPort)
	{
		m_networkPort = networkPort;
	}

	/**
	 * Sets the tracker address.
	 * 
	 * @param trackerAddress the new tracker address
	 */
	public void setTrackerAddress(String trackerAddress)
	{
		m_trackerAddress = trackerAddress;
	}

	/**
	 * Sets the tracker port.
	 * 
	 * @param trackerPort the new tracker port
	 */
	public void setTrackerPort(int trackerPort)
	{
		m_trackerPort = trackerPort;
	}

	/**
	 * Sets the ui.
	 * 
	 * @param ui the new ui
	 */
	public void setUI(UI ui)
	{
		m_ui = ui;

		// Images
		Image settingsImage = new Image(Display.getCurrent(), "images/gear.png");

		// Settings button
		final Button settingsButton = new Button(ui.getHeader(), SWT.PUSH);
		settingsButton.setImage(settingsImage);
		FormData layoutData = new FormData();
		layoutData.right = m_ui.getHeader().getRightAttachment();
		layoutData.top = m_ui.getHeader().getTopAttachment();
		layoutData.bottom = m_ui.getHeader().getBottomAttachment();
		settingsButton.setLayoutData(layoutData);
		ui.getHeader().setRightAttachment(new FormAttachment(settingsButton, -m_ui.getHeader().getHorizontalMargin(), SWT.LEFT));

		m_ui.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				settingsButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						openSettingsWindow();
					}
				});
			}
		});
	}

	/**
	 * Write file.
	 * 
	 * @param file the file
	 * @param content the content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeFile(String file, String content) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(Charset.defaultCharset().encode(content).array());
		fos.close();
	}
}