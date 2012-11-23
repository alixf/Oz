package oz.modules.messages;

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
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import oz.data.Message;
import oz.data.UserData;
import oz.modules.messages.Messages.Channel;

public class MessagesView extends Composite
{
	public MessagesView(Messages messages)
	{
		super(messages.getUI().getContent(), SWT.NONE);
		setLayout(new FormLayout());
		m_messages = messages;

		/*
		 * Message input
		 */
		m_messagesInput = new Text(this, SWT.SINGLE | SWT.BORDER);
		FormData messagesInputData = new FormData();
		messagesInputData.left = new FormAttachment(20, 0);
		messagesInputData.bottom = new FormAttachment(100, -5);
		messagesInputData.right = new FormAttachment(100, -5);
		m_messagesInput.setLayoutData(messagesInputData);
		Font font = m_messagesInput.getFont();
		FontData fd = font.getFontData()[0];
		fd.height *= 1.5;
		m_messagesInput.setFont(new Font(getDisplay(), fd));

		/*
		 * Message channels
		 */
		m_channelsScrollContainer = new ScrolledComposite(this, SWT.V_SCROLL);
		FormData channelsScrollContainerData = new FormData();
		channelsScrollContainerData.left = new FormAttachment(0, 5);
		channelsScrollContainerData.top = new FormAttachment(0, 5);
		channelsScrollContainerData.right = new FormAttachment(20, 0);
		channelsScrollContainerData.bottom = new FormAttachment(m_messagesInput, -5, SWT.TOP);
		m_channelsScrollContainer.setLayoutData(channelsScrollContainerData);
		m_channelsScrollContainer.setLayout(new FillLayout());
		m_channelsContainer = new Composite(m_channelsScrollContainer, SWT.BORDER);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginBottom = 0;
		layout.spacing = 0;
		m_channelsContainer.setLayout(layout);
		m_channelsScrollContainer.setContent(m_channelsContainer);
		m_channelsScrollContainer.setExpandHorizontal(true);
		m_channelsScrollContainer.setExpandVertical(true);

		/*
		 * Add Channel button
		 */
		Button addChannelButton = new Button(this, SWT.PUSH);
		addChannelButton.setText("+");
		addChannelButton.setToolTipText("Ajouter un fil de discussion");
		FormData addChannelButtonData = new FormData();
		addChannelButtonData.left = new FormAttachment(0, 5);
		addChannelButtonData.top = new FormAttachment(m_channelsScrollContainer, 5, SWT.BOTTOM);
		addChannelButtonData.bottom = new FormAttachment(100, -5);
		addChannelButton.setLayoutData(addChannelButtonData);

		/*
		 * Messages container
		 */
		m_messagesScrollContainer = new ScrolledComposite(this, SWT.V_SCROLL | SWT.BORDER);
		FormData messagesScrollContainerData = new FormData();
		messagesScrollContainerData.left = new FormAttachment(m_channelsScrollContainer, 0, SWT.RIGHT);
		messagesScrollContainerData.top = new FormAttachment(0, 5);
		messagesScrollContainerData.right = new FormAttachment(100, -5);
		messagesScrollContainerData.bottom = new FormAttachment(m_messagesInput, -5, SWT.TOP);
		m_messagesScrollContainer.setLayoutData(messagesScrollContainerData);
		m_messagesScrollContainer.setLayout(new FillLayout());
		m_messagesContainer = new Composite(m_messagesScrollContainer, SWT.NONE);
		m_messagesContainer.setLayout(new RowLayout(SWT.VERTICAL));
		m_messagesScrollContainer.setContent(m_messagesContainer);
		m_messagesScrollContainer.setExpandHorizontal(true);
		m_messagesScrollContainer.setExpandVertical(true);

		layout();

		/*
		 * Events
		 */
		m_messagesInput.addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				if (m_messagesInput.getText().length() > 0)
				{
					m_messages.sendMessage(m_messagesInput.getText());
					m_messagesInput.setText("");
				}
			}
		});
		addChannelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				AddChannelWindow addChannelWindow = new AddChannelWindow(m_messages);
				m_messages.createChannel(addChannelWindow.run());
			}
		});

		addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("View");
			}
		});
	}

	public void addMessage(final UserData user, final Message message)
	{
		new MessageWidget(m_messages, m_messagesContainer, message);

		m_messagesScrollContainer.setMinSize(m_messagesContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		m_messagesContainer.layout();
		m_messagesScrollContainer.getVerticalBar().setSelection(m_messagesScrollContainer.getVerticalBar().getMaximum());
		m_messagesScrollContainer.layout();
	}

	public void addChannel(final Channel channel)
	{
		ChannelWidget channelWidget = new ChannelWidget(m_messages, m_channelsContainer, channel);
		channelWidget.setLayoutData(new RowData(m_channelsContainer.getSize().x - 8, 50));

		m_channelsScrollContainer.setMinSize(m_channelsContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		m_channelsContainer.layout();
		m_channelsScrollContainer.layout();
	}

	private Messages			m_messages;
	private Text				m_messagesInput;
	private Composite			m_channelsContainer;
	private Composite			m_messagesContainer;
	private ScrolledComposite	m_channelsScrollContainer;
	private ScrolledComposite	m_messagesScrollContainer;
}