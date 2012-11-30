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

import oz.User;
import oz.modules.Files;
import oz.network.Client;

class ContactsView extends Composite implements Files.Observer
{
	private static final int	CONTACTSVMARGIN	= 5;
	private static final int	CONTACTSHMARGIN	= 5;

	public ContactsView(Contacts contacts, Composite parent)
	{
		super(parent, SWT.NONE);
		m_contacts = contacts;

		// Layout
		setLayout(new FormLayout());

		m_widgets = new HashMap<Client, ContactWidget>();

		// Add button
		Button addButton = new Button(this, SWT.PUSH);
		addButton.setText("Ajouter un contact");
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, CONTACTSHMARGIN);
		fd.left = new FormAttachment(0, CONTACTSHMARGIN);
		addButton.setLayoutData(fd);
		addButton.addSelectionListener(new SelectionAdapter()
		{
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
			public void widgetSelected(SelectionEvent e)
			{
				retrieveContact();
			}

		});

		// Attachments
		m_contactsAttachment = new FormAttachment(addButton, CONTACTSVMARGIN, SWT.BOTTOM);
	}
	
	private void retrieveContact()
	{
		ContactRetriever cr = new ContactRetriever(m_contacts.getNetwork(), this);
		cr.run();
	}

	public void createContactWidget(final Client client)
	{
		// Create widget
		ContactWidget contactWidget = new ContactWidget(m_contacts, this, client);
		m_widgets.put(client, contactWidget);

		// Set layout data
		FormData fd = new FormData();
		fd.top = m_contactsAttachment;
		fd.left = new FormAttachment(0, CONTACTSHMARGIN);
		fd.right = new FormAttachment(100, -CONTACTSHMARGIN);
		contactWidget.setLayoutData(fd);
		m_contactsAttachment = new FormAttachment(contactWidget, CONTACTSVMARGIN, SWT.BOTTOM);

		contactWidget.addListener(SWT.MouseDown, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				m_contacts.getProfile().show(client.getUserData());
			}
		});
		
		layout();
	}

	public void updateContactWidget(Client client)
	{
		ContactWidget widget = m_widgets.get(client);
		if (widget != null)
			widget.updateData();
		else
			System.err.println("updateContactWidget : Widget doesn't exist");

		layout();
	}

	@Override
	public void fileNotify(final Client client, final String request)
	{
		getDisplay().asyncExec(new Runnable()
		{
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

	public Contacts getContacts()
	{
		return m_contacts;
	}

	Contacts						m_contacts;
	HashMap<Client, ContactWidget>	m_widgets;
	private FormAttachment			m_contactsAttachment;
}