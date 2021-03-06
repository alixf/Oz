package oz.modules.contacts;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import oz.data.UserIdentifier;
import oz.modules.Files;
import oz.network.Client;

/**
 * This view is used to show contacts
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
class ContactsView extends Composite implements Files.Observer
{

	/** The Constant CONTACTSHMARGIN. */
	private static final int		CONTACTSHMARGIN	= 5;

	/** The Constant CONTACTSVMARGIN. */
	private static final int		CONTACTSVMARGIN	= 5;

	/** The contacts module. */
	Contacts						m_contacts;

	/** The contacts attachment. */
	private FormAttachment			m_contactsAttachment;

	/** The widgets. */
	HashMap<String, ContactWidget>	m_widgets;

	/**
	 * Instantiates a new contacts view.
	 * 
	 * @param contacts the contacts
	 * @param parent the parent
	 */
	public ContactsView(Contacts contacts, Composite parent)
	{
		super(parent, SWT.NONE);
		m_contacts = contacts;

		// Layout
		setLayout(new FormLayout());

		m_widgets = new HashMap<String, ContactWidget>();

		// Add button
		Button addButton = new Button(this, SWT.PUSH);
		addButton.setText("Ajouter un contact");
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, CONTACTSHMARGIN);
		fd.left = new FormAttachment(0, CONTACTSHMARGIN);
		addButton.setLayoutData(fd);
		addButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				AddContactWindow addWindow = new AddContactWindow(m_contacts);
				addWindow.run();
			}
		});

		// Retrieve button
		Button trackerButton = new Button(this, SWT.PUSH);
		trackerButton.setText("Tracker");
		fd = new FormData();
		fd.top = new FormAttachment(0, CONTACTSHMARGIN);
		fd.left = new FormAttachment(addButton, CONTACTSHMARGIN, SWT.RIGHT);
		trackerButton.setLayoutData(fd);
		trackerButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				retrieveContact();
			}

		});

		// Attachments
		m_contactsAttachment = new FormAttachment(addButton, CONTACTSVMARGIN, SWT.BOTTOM);
	}

	/**
	 * Creates the contact widget.
	 * 
	 * @param user the user
	 * @param client the client
	 */
	public void createContactWidget(UserIdentifier user, final Client client)
	{
		// Create widget
		ContactWidget contactWidget = new ContactWidget(m_contacts, this, user, client);
		m_widgets.put(user.getUUID(), contactWidget);

		// Set layout data
		FormData fd = new FormData();
		fd.top = m_contactsAttachment;
		fd.left = new FormAttachment(0, CONTACTSHMARGIN);
		fd.right = new FormAttachment(100, -CONTACTSHMARGIN);
		contactWidget.setLayoutData(fd);
		m_contactsAttachment = new FormAttachment(contactWidget, CONTACTSVMARGIN, SWT.BOTTOM);

		if (client != null)
		{
			contactWidget.addListener(SWT.MouseDown, new Listener()
			{
				@Override
				public void handleEvent(Event event)
				{
					m_contacts.getProfile().show(client.getUserData());
				}
			});
		}

		layout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.modules.Files.Observer#fileNotify(oz.network.Client, java.lang.String)
	 */
	@Override
	public void fileNotify(final Client client, final String request)
	{
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				ContactWidget widget = m_widgets.get(client);
				if (widget != null)
				{
					widget.setImage(new Image(Display.getCurrent(), client.getUserData().getAvatarFilename()));
				}
			}
		});
	}

	/**
	 * Gets the contacts.
	 * 
	 * @return the contacts
	 */
	public Contacts getContacts()
	{
		return m_contacts;
	}

	/**
	 * Retrieve contact.
	 */
	private void retrieveContact()
	{
		ContactRetriever cr = new ContactRetriever(m_contacts.getNetwork(), this);
		cr.run();
	}

	/**
	 * Update contact widget.
	 * 
	 * @param client the client
	 */
	public void updateContactWidget(Client client)
	{
		ContactWidget widget = m_widgets.get(client.getUserData().getUserIdentifier().getUUID());
		if (widget != null)
			widget.updateData();
		else
			System.err.println("updateContactWidget : Widget doesn't exist");

		layout();
	}
}