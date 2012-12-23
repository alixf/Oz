package oz.modules.messages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import oz.network.Client;

/**
 * This window is used to add a messages channel
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class AddChannelWindow
{

	/** The add channel shell. */
	Shell					m_addChannelShell;

	/** The client buttons. */
	HashMap<Button, Client>	m_clientButtons;

	/** The selected clients. */
	List<Client>			m_selectedClients;

	/**
	 * Instantiates a new window.
	 * 
	 * @param messages the messages module
	 */
	public AddChannelWindow(Messages messages)
	{
		m_addChannelShell = new Shell(messages.getUI().getDisplay(), SWT.SHELL_TRIM & (~SWT.RESIZE));
		Image logo16 = new Image(Display.getCurrent(), "images/logo-16.png");
		m_addChannelShell.setImage(logo16);
		m_addChannelShell.setText("Ajouter un fil de discussion");
		m_addChannelShell.setLayout(new RowLayout(SWT.VERTICAL));
		RowData layoutData = new RowData();
		layoutData.width = 200;

		m_selectedClients = new LinkedList<Client>();
		m_clientButtons = new HashMap<Button, Client>();

		Iterator<Client> it = messages.getContactsModule().getContactList().iterator();
		while (it.hasNext())
		{
			Client client = it.next();
			Button button = new Button(m_addChannelShell, SWT.CHECK);
			button.setText(client.getUserData().getUsername());
			m_clientButtons.put(button, client);
		}

		Button confirmButton = new Button(m_addChannelShell, SWT.CENTER);
		confirmButton.setText("Valider");
		confirmButton.setLayoutData(layoutData);
		confirmButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				for (Entry<Button, Client> entry : m_clientButtons.entrySet())
				{
					if (entry.getKey().getSelection())
						m_selectedClients.add(entry.getValue());
				}
				m_addChannelShell.close();
			}
		});
		Button cancelButton = new Button(m_addChannelShell, SWT.CENTER);
		cancelButton.setText("Annuler");
		cancelButton.setLayoutData(layoutData);
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				m_addChannelShell.close();
			}
		});

		m_addChannelShell.pack();
		m_addChannelShell.open();
	}

	/**
	 * Run.
	 * 
	 * @return the list
	 */
	public List<Client> run()
	{
		while (!m_addChannelShell.isDisposed())
		{
			if (!m_addChannelShell.getDisplay().readAndDispatch())
				m_addChannelShell.getDisplay().sleep();
		}
		return m_selectedClients;
	}
}