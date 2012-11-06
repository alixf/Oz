package modules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ui.UI;
import network.Client;
import network.Network;

public class Messages implements Module
{
	public Messages(Network network, UI ui)
	{
		m_network = network;
		m_ui = ui;
		m_network.setCommand("MSG", this);

		// Create messages container
		m_messagesWidget = new MessagesWidget(m_ui.getContent());
		m_ui.getContent().show(m_messagesWidget);

		// Create menu button
		m_ui.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				Button button = m_ui.getHeader().addMenu("Messages");
				button.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{
						m_ui.getContent().show(m_messagesWidget);
						m_messagesWidget.layout();
					}
				});
			}
		});
	}

	public boolean executeCommand(String command, Client client)
	{
		client.getSocket();
		Map<String, String> fields = m_network.parsePacket(command);
		
		if(!fields.containsKey("content"))
		{
			System.out.println("The field \"content\" is missing"); //TODO error manager
			return false;
		}
		if(!fields.containsKey("date"))
		{
			System.out.println("The field \"date\" is missing"); //TODO error manager
			return false;
		}
		
		final String messageString = fields.get("content");
		Long messageTimestamp = Long.decode(fields.get("date"));
		SimpleDateFormat frenchDateFormat = new SimpleDateFormat("'Le' d/M/y 'à' k:m:s Z",new Locale("FRANCE"));
		final String messageDate = frenchDateFormat.format(new Date(messageTimestamp));

		m_ui.getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				m_messagesWidget.addMessage(messageString);
				m_messagesWidget.addMessage(messageDate);
			}
		});
		return true;
	}

	private class MessagesWidget extends Composite
	{
		public MessagesWidget(Composite parent)
		{
			super(parent, SWT.NONE);
			setLayout(new FormLayout());

			// Message input
			m_messagesInput = new Text(this, SWT.SINGLE | SWT.BORDER);
			FormData messagesInputData = new FormData();
			messagesInputData.left = new FormAttachment(0, 5);
			messagesInputData.bottom = new FormAttachment(100, -5);
			messagesInputData.right = new FormAttachment(100, -5);
			m_messagesInput.setLayoutData(messagesInputData);
			Font font = m_messagesInput.getFont();
			FontData fd = font.getFontData()[0];
			fd.height *= 1.5;
			m_messagesInput.setFont(new Font(getDisplay(), fd));

			// Messages container
			m_scrollContainer = new ScrolledComposite(this, SWT.V_SCROLL);
			FormData scrollContainerData = new FormData();
			scrollContainerData.left = new FormAttachment(0, 5);
			scrollContainerData.top = new FormAttachment(0, 5);
			scrollContainerData.right = new FormAttachment(100, -5);
			scrollContainerData.bottom = new FormAttachment(100, -100);
			m_scrollContainer.setLayoutData(scrollContainerData);
			m_scrollContainer.setLayout(new FillLayout());

			m_messagesContainer = new Composite(m_scrollContainer, SWT.NONE);
			m_messagesContainer.setLayout(new RowLayout(SWT.VERTICAL));

			m_scrollContainer.setContent(m_messagesContainer);
			m_scrollContainer.setExpandHorizontal(true);
			m_scrollContainer.setExpandVertical(true);

			layout();
		}

		public void addMessage(String message)
		{
			Label newMessage = new Label(m_messagesContainer, SWT.BORDER);
			newMessage.setText(message);

			m_scrollContainer.setMinSize(m_messagesContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			m_messagesContainer.layout();
		}

		ScrolledComposite m_scrollContainer;
		Composite m_messagesContainer;
		Text m_messagesInput;
	}

	private Network m_network;
	private UI m_ui;
	private MessagesWidget m_messagesWidget;
}
