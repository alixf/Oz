package oz.modules.contacts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import oz.network.Client;

class AddContactWindow
{
	public AddContactWindow(Contacts contacts)
	{
		m_contacts = contacts;

		// Window
		m_addContactShell = new Shell(m_contacts.getView().getDisplay(), SWT.SHELL_TRIM & (~SWT.RESIZE));
		m_addContactShell.setText("Ajouter un contact");
		m_addContactShell.setLayout(new RowLayout(SWT.VERTICAL));
		RowData layoutData = new RowData();
		layoutData.width = 200;

		// Address label
		Label addressLabel = new Label(m_addContactShell, SWT.CENTER);
		addressLabel.setText("Adresse du contact :");
		addressLabel.setLayoutData(layoutData);

		// Address text
		final Text addressText = new Text(m_addContactShell, SWT.SINGLE | SWT.BORDER);
		addressText.setLayoutData(layoutData);

		// Confirm button
		Button confirmButton = new Button(m_addContactShell, SWT.CENTER);
		confirmButton.setText("Valider");
		confirmButton.setLayoutData(layoutData);

		// Cancel button
		Button cancelButton = new Button(m_addContactShell, SWT.CENTER);
		cancelButton.setText("Annuler");
		cancelButton.setLayoutData(layoutData);

		// Events
		confirmButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				Client client = m_contacts.addContact(addressText.getText());
				if (client != null)
					m_addContactShell.close();
				else
				{
					// TODO Display error
				}
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				m_addContactShell.close();
			}
		});

		// Open window
		m_addContactShell.pack();
		m_addContactShell.open();
	}

	public void run()
	{
		// Event loop
		while (!m_addContactShell.isDisposed())
		{
			if (!m_addContactShell.getDisplay().readAndDispatch())
				m_addContactShell.getDisplay().sleep();
		}
	}

	Contacts	m_contacts;
	Shell		m_addContactShell;
}