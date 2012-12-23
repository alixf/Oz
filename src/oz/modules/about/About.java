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
	public About()
	{
	}

	public void setUI(UI ui)
	{
		m_ui = ui;
		
		// Images
		Image settingsImage = new Image(Display.getCurrent(), "images/logo-16.png");
		
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
						openAboutWindow();
					}
				});
			}
		});
	}


	private void openAboutWindow()
	{
		AboutView aboutView = new AboutView();
		aboutView.open();
	}

	private UI		m_ui;
}