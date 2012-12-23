package oz.modules.settings;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * This view is used to show settings
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class SettingsView
{

	/** The Constant HMARGIN. */
	private final static int	HMARGIN	= 5;

	/** The Constant VMARGIN. */
	private final static int	VMARGIN	= 5;

	/** The network port. */
	Spinner						m_networkPort;

	/** The settings. */
	private Settings			m_settings;

	/** The shell. */
	private Shell				m_shell;

	/** The top attachment. */
	private FormAttachment		m_topAttachment;

	/** The tracker address. */
	Text						m_trackerAddress;

	/** The tracker port. */
	Spinner						m_trackerPort;

	/**
	 * Instantiates a new settings view.
	 * 
	 * @param settings the settings
	 */
	public SettingsView(Settings settings)
	{
		m_settings = settings;
		m_shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE));
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_shell.setImage(logo16);
		m_shell.setText("Oz : Paramètres");
		m_shell.setLayout(new FormLayout());
		m_topAttachment = new FormAttachment(0, VMARGIN);

		m_networkPort = addIntSetting("Network port", m_settings.getNetworkPort());
		m_trackerAddress = addStringSetting("Tracker address", m_settings.getTrackerAddress());
		m_trackerPort = addIntSetting("Tracker port", m_settings.getTrackerPort());

		Button confirmButton = new Button(m_shell, SWT.CENTER);
		confirmButton.setText("Valider");
		FormData layoutData = new FormData();
		layoutData.top = m_topAttachment;
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		confirmButton.setLayoutData(layoutData);
		confirmButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				writeConfigFile();
				m_shell.close();
			}
		});
		Button cancelButton = new Button(m_shell, SWT.CENTER);
		cancelButton.setText("Annuler");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(confirmButton, VMARGIN, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		cancelButton.setLayoutData(layoutData);
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
	 * Adds the int setting.
	 * 
	 * @param labelString the label string
	 * @param defaultValue the default value
	 * @return the spinner
	 */
	private Spinner addIntSetting(String labelString, int defaultValue)
	{
		Label label = new Label(m_shell, SWT.NONE);
		label.setText(labelString);
		FormData layoutData = new FormData();
		layoutData.top = m_topAttachment;
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(50, 0);
		label.setLayoutData(layoutData);

		Spinner spinner = new Spinner(m_shell, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(65535);
		spinner.setSelection(defaultValue);
		layoutData = new FormData();
		layoutData.top = m_topAttachment;
		layoutData.left = new FormAttachment(label, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		spinner.setLayoutData(layoutData);

		m_topAttachment = new FormAttachment(spinner, VMARGIN, SWT.BOTTOM);
		return spinner;
	}

	/**
	 * Adds the string setting.
	 * 
	 * @param labelString the label string
	 * @param defaultValue the default value
	 * @return the text
	 */
	private Text addStringSetting(String labelString, String defaultValue)
	{
		Label label = new Label(m_shell, SWT.NONE);
		label.setText(labelString);
		FormData layoutData = new FormData();
		layoutData.top = m_topAttachment;
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(50, 0);
		label.setLayoutData(layoutData);

		Text text = new Text(m_shell, SWT.SINGLE | SWT.BORDER);
		if (defaultValue != null)
			text.setText(defaultValue);
		layoutData = new FormData();
		layoutData.width = 150;
		layoutData.top = m_topAttachment;
		layoutData.left = new FormAttachment(label, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		text.setLayoutData(layoutData);

		m_topAttachment = new FormAttachment(text, VMARGIN, SWT.BOTTOM);
		return text;
	}

	/**
	 * Open.
	 */
	public void open()
	{
		m_shell.open();
	}

	/**
	 * Write config file.
	 */
	private void writeConfigFile()
	{
		m_settings.setNetworkPort(m_networkPort.getSelection());
		m_settings.setTrackerAddress(m_trackerAddress.getText());
		m_settings.setTrackerPort(m_trackerPort.getSelection());
		try
		{
			m_settings.save("settings.ozs");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
