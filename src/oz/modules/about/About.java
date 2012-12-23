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

public class About
{
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
			public void run()
			{
				m_settingsButton.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent e)
					{
						open();
					}
				});
			}
		});
		m_aboutWindow = new AboutWindow();
	}

	private void open()
	{
		m_aboutWindow.open();
	}

	private UI			m_ui;
	private AboutWindow	m_aboutWindow;
	private Button		m_settingsButton;
}