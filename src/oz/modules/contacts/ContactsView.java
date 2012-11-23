package oz.modules.contacts;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import oz.modules.Files;
import oz.network.Client;

class ContactsView extends Composite implements Files.Observer
{
	public ContactsView(Contacts contacts, Composite parent)
	{
		super(parent, SWT.NONE);
		m_contacts = contacts;

		// Layout
		setLayout(new RowLayout(SWT.VERTICAL));

		m_widgets = new HashMap<Client, ContactWidget>();

		// Add button
		Button addButton = new Button(this, SWT.PUSH);
		addButton.setText("Ajouter un contact");
		addButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				AddContactWindow addWindow = new AddContactWindow(m_contacts);
				addWindow.run();
			}
		});
	}

	public void createContactWidget(Client client)
	{
		m_widgets.put(client, new ContactWidget(m_contacts, this, client));
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
}