package modules;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import ui.UI;
import network.Client;
import network.Network;

public class Contacts implements Module
{
	public Contacts(Network network, UI ui)
	{
		m_network = network;
		m_ui = ui;
		m_network.setCommand("ADD", this);

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
	public boolean executeCommand(String command, Client client)
	{
		String commandCode = command.split(m_network.getSeparator())[0];
		if (commandCode.equals("ADD"))
		{
			Map<String, String> fields = m_network.parsePacket(command);

			if (!fields.containsKey("username"))
			{
				System.err.println("The field \"username\" is missing"); // TODO error manager
				return false;
			}

			System.out.println("Adding : " + fields.get("username"));

			return true;
		}

		return false;
	}

	public boolean addContact(String address)
	{
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("username", "Hello");
		String packet = m_network.makePacket("ADD", fields);

		try
		{
			if (m_network.send(packet, address))
			{
				return true;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	class ContactsWidget extends Composite
	{
		public ContactsWidget(Composite parent)
		{
			super(parent, SWT.NONE);
			setLayout(new RowLayout(SWT.VERTICAL));

			Button addButton = new Button(this, SWT.PUSH);
			addButton.setText("Ajouter un contact");
			addButton.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					final Shell addContactShell = new Shell(m_ui.getDisplay(), SWT.SHELL_TRIM & (~SWT.RESIZE));
					addContactShell.setText("Ajouter un contact");
					addContactShell.setLayout(new RowLayout(SWT.VERTICAL));
					RowData layoutData = new RowData();
					layoutData.width = 200;

					Label addressLabel = new Label(addContactShell, SWT.CENTER);
					addressLabel.setText("Adresse du contact :");
					addressLabel.setLayoutData(layoutData);
					final Text addressText = new Text(addContactShell, SWT.SINGLE | SWT.BORDER);
					addressText.setLayoutData(layoutData);
					Button confirmButton = new Button(addContactShell, SWT.CENTER);
					confirmButton.setText("Valider");
					confirmButton.setLayoutData(layoutData);
					confirmButton.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent e)
						{
							if (addContact(addressText.getText()))
							{
								addContactShell.close();
							}
							else
							{
								// TODO Display error
							}
						}
					});
					Button cancelButton = new Button(addContactShell, SWT.CENTER);
					cancelButton.setText("Annuler");
					cancelButton.setLayoutData(layoutData);
					cancelButton.addSelectionListener(new SelectionAdapter()
					{
						public void widgetSelected(SelectionEvent e)
						{
							addContactShell.close();
						}
					});

					addContactShell.pack();
					addContactShell.open();

					while (!addContactShell.isDisposed())
					{
						if (!addContactShell.getDisplay().readAndDispatch())
							addContactShell.getDisplay().sleep();
					}
				}
			});
		}
	}

	Network			m_network;
	UI				m_ui;
	ContactsWidget	m_contactsWidget;
}
