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
		fd.top = new FormAttachment(0, CONTACTSHMARGIN);;
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
		

		// Attachments
		m_contactsAttachment = new FormAttachment(addButton, CONTACTSVMARGIN, SWT.BOTTOM);
	}

	public void createContactWidget(Client client)
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
					Image image = new Image(getDisplay(), "files/" + client.getUserData().getUsername() + "/" + request);
					widget.setImage(image);
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