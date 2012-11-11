package modules;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import data.UserData;

import ui.UI;
import network.Client;
import network.Network;

public class Contacts implements Module
{
	public Contacts(Network network, UI ui, UserData user)
	{
		m_network = network;
		m_ui = ui;
		m_user = user;
		m_network.setCommand("ADD", this);
		m_network.setCommand("USER", this);

		// Create messages container
		m_contactsWidget = new ContactsWidget(m_ui.getContent());
		m_ui.getContent().show(m_contactsWidget);

		// Create menu button
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				Button button = m_ui.getHeader().addMenu("Contacts");
				button.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent e)
					{
						m_ui.getContent().show(m_contactsWidget);
						m_contactsWidget.layout();
					}
				});
			}
		});
	}

	@Override
	public boolean executeCommand(String command, final Client client)
	{
		String commandCode = command.split(m_network.getSeparator())[0];

		if (commandCode.equals("ADD") || commandCode.equals("USER"))
			client.setUserData(m_network.parsePacket(command, UserData.class));
		if (commandCode.equals("ADD"))
		{
			m_ui.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					Label newContactLabel = new Label(m_contactsWidget, SWT.BORDER);
					newContactLabel.setText(client.getUserData().getUsername());
					m_contactsWidget.layout();
				}
			});
		}
		
		return true;
	}

	public Client addContact(final String address)
	{
		String packet = m_network.makePacket("ADD", m_user);

		try
		{
			Client client = m_network.createClient(address);
			client.getUserData().setUsername(address);
			if(client != null)
			{
				System.out.println("sending data");
				m_network.send(packet, client);

				System.out.println("adding label");
				Label newContactLabel = new Label(m_contactsWidget, SWT.BORDER);
				newContactLabel.setText(client.getUserData().getUsername());
				m_contactsWidget.layout();
			}
			return client;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	class ContactsWidget extends Composite
	{
		public ContactsWidget(Composite parent)
		{
			super(parent, SWT.NONE);
			setLayout(new RowLayout(SWT.VERTICAL));

			// Add button
			Button addButton = new Button(this, SWT.PUSH);
			addButton.setText("Ajouter un contact");
			// Extends adapter to make a closure for contactsWidget to be passed in the callback
			class CustomAdapter extends SelectionAdapter
			{
				public CustomAdapter(ContactsWidget contactsWidget)
				{
					m_contactsWidget = contactsWidget;
				}

				public void widgetSelected(SelectionEvent e)
				{
					AddContactWindow addWindow = new AddContactWindow(m_contactsWidget);
					addWindow.run();
				}
			}
			addButton.addSelectionListener(new CustomAdapter(this));
		}
	}

	class AddContactWindow
	{
		public AddContactWindow(ContactsWidget contactsWidget)
		{
			m_addContactShell = new Shell(m_ui.getDisplay(), SWT.SHELL_TRIM & (~SWT.RESIZE));
			m_addContactShell.setText("Ajouter un contact");
			m_addContactShell.setLayout(new RowLayout(SWT.VERTICAL));
			RowData layoutData = new RowData();
			layoutData.width = 200;

			Label addressLabel = new Label(m_addContactShell, SWT.CENTER);
			addressLabel.setText("Adresse du contact :");
			addressLabel.setLayoutData(layoutData);
			final Text addressText = new Text(m_addContactShell, SWT.SINGLE | SWT.BORDER);
			addressText.setLayoutData(layoutData);
			Button confirmButton = new Button(m_addContactShell, SWT.CENTER);
			confirmButton.setText("Valider");
			confirmButton.setLayoutData(layoutData);
			confirmButton.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					Client client = addContact(addressText.getText());
					if (client != null)
					{
						m_addContactShell.close();
						/*
						Label newContactLabel = new Label(m_addContactShell, SWT.BORDER);
						newContactLabel.setText(client.getUserData().getUsername());
						*/
					}
					else
					{
						// TODO Display error
					}
				}
			});
			Button cancelButton = new Button(m_addContactShell, SWT.CENTER);
			cancelButton.setText("Annuler");
			cancelButton.setLayoutData(layoutData);
			cancelButton.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					m_addContactShell.close();
				}
			});

			m_addContactShell.pack();
			m_addContactShell.open();
		}

		public void run()
		{
			while (!m_addContactShell.isDisposed())
			{
				if (!m_addContactShell.getDisplay().readAndDispatch())
					m_addContactShell.getDisplay().sleep();
			}
		}

		Shell	m_addContactShell;
	}

	Network			m_network;
	UI				m_ui;
	UserData		m_user;
	ContactsWidget	m_contactsWidget;
}
