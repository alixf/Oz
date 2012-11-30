package oz.modules.profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import oz.User;
import oz.data.UserData;
import oz.modules.Module;
import oz.network.Client;
import oz.ui.UI;

public class Profile implements Module
{
	public Profile(UI ui)
	{
		m_ui = ui;

		m_view = new ProfileView(m_ui.getContent());
		
		MenuWidget menuWidget = new MenuWidget(m_ui, User.getUser());
		menuWidget.addListener(SWT.MouseDown, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				show(User.getUser());
			}
		});
	}

	@Override
	public boolean executeCommand(String command, Client client)
	{
		return false;
	}

	public void show(UserData user)
	{
		m_view.setUser(user);
		m_ui.getContent().show(m_view);
		m_view.layout();
	}

	private UI			m_ui;
	private ProfileView	m_view;
}
