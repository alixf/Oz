package oz.modules.profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import oz.User;
import oz.data.UserData;
import oz.modules.Module;
import oz.network.Client;
import oz.ui.UI;

/**
 * This module is used to manage user profiles
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Profile implements Module
{

	/** The edit view. */
	private EditView	m_editView;

	/** The menu widget. */
	private MenuWidget	m_menuWidget;

	/** The ui. */
	private UI			m_ui;

	/** The view. */
	private ProfileView	m_view;

	/**
	 * Instantiates a new profile.
	 * 
	 * @param ui the ui
	 */
	public Profile(UI ui)
	{
		m_ui = ui;

		m_view = new ProfileView(this, m_ui.getContent());
		m_editView = new EditView(this, m_ui.getContent());

		m_menuWidget = new MenuWidget(m_ui, User.getUser());
		m_menuWidget.addListener(SWT.MouseDown, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				show(User.getUser());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.modules.Module#executeCommand(java.lang.String, oz.network.Client)
	 */
	@Override
	public boolean executeCommand(String command, Client client)
	{
		return false;
	}

	/**
	 * Show.
	 * 
	 * @param user the user
	 */
	public void show(UserData user)
	{
		m_view.setUser(user);
		m_ui.getContent().show(m_view);
		m_view.layout();
	}

	/**
	 * Show edit view.
	 */
	public void showEditView()
	{
		m_ui.getContent().show(m_editView);
		m_editView.layout();
	}

	/**
	 * Update widget.
	 */
	public void updateWidget()
	{
		m_menuWidget.updateData();
	}
}
