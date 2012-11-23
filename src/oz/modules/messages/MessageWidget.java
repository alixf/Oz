package oz.modules.messages;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import oz.data.Message;

public class MessageWidget extends Composite
{

	public MessageWidget(Messages messages, Composite parent, Message message)
	{
		super(parent, SWT.BORDER);
		setLayout(new FormLayout());

		m_messages = messages;
		m_message = message;

		m_text = new Label(this, SWT.NONE);
		m_text.setText(message.getContent());
		m_image = new Label(this, SWT.NONE);
		m_date = new Label(this, SWT.NONE);
		m_date.setText(m_messages.getDateFormat().format(new Date(message.getDate())));

	}

	Messages	m_messages;
	Message		m_message;
	Label		m_text;
	Label		m_image;
	Label		m_date;
}