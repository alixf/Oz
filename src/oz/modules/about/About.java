package oz.modules.about;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import oz.ui.UI;

// TODO: Auto-generated Javadoc
/**
 * The Class About.
 */
public class About
{

	/** The m_about window. */
	private AboutWindow	m_aboutWindow;

	/** The m_settings button. */
	private Button		m_settingsButton;

	/** The m_ui. */
	private UI			m_ui;

	/**
	 * Instantiates a new about.
	 * 
	 * @param ui the ui
	 */
	public About(UI ui)
	{
		m_ui = ui;

		/*
		 * Images
		 */
		Image settingsImage = new Image(Display.getCurrent(), "images/logo-16.png");

		/*
		 * Settings button
		 */
		m_settingsButton = new Button(ui.getHeader(), SWT.PUSH);
		m_settingsButton.setImage(settingsImage);
		FormData layoutData = new FormData();
		layoutData.right = m_ui.getHeader().getRightAttachment();
		layoutData.top = m_ui.getHeader().getTopAttachment();
		layoutData.bottom = m_ui.getHeader().getBottomAttachment();
		m_settingsButton.setLayoutData(layoutData);
		ui.getHeader().setRightAttachment(new FormAttachment(m_settingsButton, -m_ui.getHeader().getHorizontalMargin(), SWT.LEFT));
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				m_settingsButton.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						open();
					}
				});
			}
		});
		m_aboutWindow = new AboutWindow();
	}

	/**
	 * Open.
	 */
	private void open()
	{
		m_aboutWindow.open();
	}
}