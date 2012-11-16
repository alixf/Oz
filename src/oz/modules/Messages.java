package oz.modules;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import oz.data.Message;
import oz.data.UserData;

import oz.ui.UI;
import oz.network.Client;
import oz.network.Network;

public class Messages implements Module
{
	public Messages(Network network, UI ui, UserData user)
	{
		m_network = network;
		m_ui = ui;
		m_user = user;
		m_network.setCommand("MSG", this);

		// Create messages container
		m_messagesWidget = new MessagesWidget(m_ui.getContent());

		m_dateFormat = new SimpleDateFormat("'Le' d/M/y à k:m:s", new Locale("FRANCE"));
		
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
		String commandCode = m_network.getCommand(command);
		if (commandCode.equals("MSG"))
		{
			Message message = m_network.parsePacket(command, Message.class);
			
			m_messagesWidget.addMessage(client.getUserData(), message);
			
			return true;
		}

		return false;
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
			m_messagesInput.addListener(SWT.DefaultSelection, new Listener()
			{
				@Override
				public void handleEvent(Event event)
				{
					if (m_messagesInput.getText().length() > 0)
					{
						sendMessage(m_messagesInput.getText());
						m_messagesInput.setText("");
					}
				}
			});

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

		public void sendMessage(String messageContent)
		{

			Message message = new Message(messageContent, new java.util.Date().getTime());
			String packet = m_network.makePacket("MSG", message);
			
			try
			{
				m_network.send(packet, m_network.getClients());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			addMessage(m_user, message);
		}

		public void addMessage(final UserData userData, final Message message)
		{
			System.out.println("addMessage : "+message.getContent());
			m_ui.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					Label newMessage = new Label(m_messagesContainer, SWT.BORDER);
					newMessage.setText(userData.getUsername()+" - "+message.getContent()+" - "+m_dateFormat.format(new Date(message.getDate())));

					m_scrollContainer.setMinSize(m_messagesContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					m_messagesContainer.layout();
					
					m_scrollContainer.getVerticalBar().setSelection(m_scrollContainer.getVerticalBar().getMaximum());
					m_scrollContainer.layout();
				}
			});
		}

		ScrolledComposite	m_scrollContainer;
		Composite			m_messagesContainer;
		Text				m_messagesInput;
	}

	private Network			m_network;
	private UI				m_ui;
	private UserData		m_user;
	private MessagesWidget	m_messagesWidget;
	private SimpleDateFormat m_dateFormat;
}
