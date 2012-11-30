package oz.modules.contacts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import oz.data.Address;
import oz.network.Client;

/**
 * Instances of this class are windows asking necessary information to add a contact
 * 
 * @author Alix 'eolhing' Fumoleau
 * @author Jean 'Jack3113' Batista
 */
class AddContactWindow
{
	/**
	 * Value of the vertical layout margin
	 */
	private static final int	VMARGIN	= 5;
	/**
	 * Value of the horizontal layout margin
	 */
	private static final int	HMARGIN	= 5;

	/**
	 * Create a window asking necessary information to add a contact
	 * 
	 * @param contacts The parent Contacts module
	 */
	public AddContactWindow(Contacts contacts)
	{
		m_contacts = contacts;

		// Window
		m_addContactShell = new Shell(m_contacts.getView().getDisplay(), SWT.SHELL_TRIM & (~SWT.RESIZE));
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_addContactShell.setImage(logo16);
		m_addContactShell.setText("Ajouter un contact");
		m_addContactShell.setLayout(new FormLayout());

		// Address label
		Label addressLabel = new Label(m_addContactShell, SWT.CENTER);
		addressLabel.setText("Adresse du contact :");
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, VMARGIN);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		addressLabel.setLayoutData(layoutData);

		// Address text
		final Text addressText = new Text(m_addContactShell, SWT.SINGLE | SWT.BORDER);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(addressLabel, VMARGIN, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.width = 200;
		addressText.setLayoutData(layoutData);

		// Port spinner
		final Spinner portSpinner = new Spinner(m_addContactShell, SWT.SINGLE | SWT.BORDER);
		portSpinner.setMinimum(0);
		portSpinner.setMaximum(65535);
		portSpinner.setSelection(4242);
		layoutData = new FormData();
		layoutData.top = new FormAttachment(addressLabel, VMARGIN, SWT.BOTTOM);
		layoutData.left = new FormAttachment(addressText, HMARGIN, SWT.RIGHT);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		portSpinner.setLayoutData(layoutData);

		// Confirm button
		Button confirmButton = new Button(m_addContactShell, SWT.CENTER);
		confirmButton.setText("Valider");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(addressText, VMARGIN, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		confirmButton.setLayoutData(layoutData);

		// Cancel button
		Button cancelButton = new Button(m_addContactShell, SWT.CENTER);
		cancelButton.setText("Annuler");
		layoutData = new FormData();
		layoutData.top = new FormAttachment(confirmButton, VMARGIN, SWT.BOTTOM);
		layoutData.left = new FormAttachment(0, HMARGIN);
		layoutData.right = new FormAttachment(100, -HMARGIN);
		layoutData.bottom = new FormAttachment(100, -VMARGIN);
		cancelButton.setLayoutData(layoutData);

		// Events
		confirmButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				Client client = m_contacts.addContact(new Address(addressText.getText(), portSpinner.getSelection()));
				if (client != null)
					m_addContactShell.close();
				else
				{
					MessageBox messageBox = new MessageBox(m_addContactShell, SWT.ICON_ERROR);
					messageBox.setMessage("Aucun utilisateur n'est associé à cette addresse");
					messageBox.open();
				}
			}
		});
		addressText.addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				Client client = m_contacts.addContact(new Address(addressText.getText(), portSpinner.getSelection()));
				if (client != null)
					m_addContactShell.close();
				else
				{
					MessageBox messageBox = new MessageBox(m_addContactShell, SWT.ICON_ERROR);
					messageBox.setMessage("Aucun utilisateur n'est associé à cette addresse");
					messageBox.open();
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

	/**
	 * Run the window's event loop
	 */
	public void run()
	{
		// Event loop
		while (!m_addContactShell.isDisposed())
		{
			if (!m_addContactShell.getDisplay().readAndDispatch())
				m_addContactShell.getDisplay().sleep();
		}
	}

	/**
	 * The contact module parent to the window
	 */
	Contacts	m_contacts;
	/**
	 * The window
	 */
	Shell		m_addContactShell;
}