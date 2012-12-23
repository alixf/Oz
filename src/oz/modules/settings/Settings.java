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

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import oz.ui.UI;

public class Settings
{
	public Settings()
	{
	}

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
			public void run()
			{
				settingsButton.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent e)
					{
						openSettingsWindow();
					}
				});
			}
		});
	}

	private void openSettingsWindow()
	{
		SettingsView settingsView = new SettingsView(this);
		settingsView.open();
	}

	public Settings(String file) throws IOException
	{
		load(file);
	}

	public void load(String file) throws IOException
	{
		new JSONDeserializer<Settings>().deserializeInto(readFile(file), this);
	}

	public void save(String file) throws IOException
	{
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class");
		serializer.include("*");
		writeFile(file, serializer.serialize(this));
	}

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

	private void writeFile(String file, String content) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(Charset.defaultCharset().encode(content).array());
		fos.close();
	}

	public int getNetworkPort()
	{
		return m_networkPort;
	}

	public void setNetworkPort(int networkPort)
	{
		m_networkPort = networkPort;
	}

	public String getTrackerAddress()
	{
		return m_trackerAddress;
	}

	public void setTrackerAddress(String trackerAddress)
	{
		m_trackerAddress = trackerAddress;
	}

	public int getTrackerPort()
	{
		return m_trackerPort;
	}

	public void setTrackerPort(int trackerPort)
	{
		m_trackerPort = trackerPort;
	}

	private UI		m_ui;
	private int		m_networkPort;
	private String	m_trackerAddress;
	private int		m_trackerPort;
}