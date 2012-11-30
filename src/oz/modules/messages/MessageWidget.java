package oz.modules.messages;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import oz.data.Message;

public class MessageWidget extends Composite
{
	public MessageWidget(Messages messages, Composite parent, Message message)
	{
		super(parent, SWT.BORDER);

		// Layout
		setLayout(new FormLayout());

		m_messages = messages;
		m_message = message;

		/*
		 * Text
		 */
		m_text = new Label(this, SWT.NONE);
		m_text.setText(message.getContent());
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		m_text.setLayoutData(fd);

		/*
		 * Image
		 */
		m_image = new Label(this, SWT.NONE);

		/*
		 * Date
		 */
		m_date = new Label(this, SWT.NONE);
		m_date.setText(m_messages.getDateFormat().format(new Date(message.getDate())));
		fd = new FormData();
		fd.top = new FormAttachment(m_text, 5, SWT.BOTTOM);
		fd.left = new FormAttachment(0, 5);
		fd.bottom = new FormAttachment(100, -5);
		m_date.setLayoutData(fd);

		layout();
	}

	@Override
	public void addListener (int eventType, Listener listener)
	{
		super.addListener(eventType, listener);
		for(Control control : getChildren())
			control.addListener(eventType, listener);
	}

	Messages	m_messages;
	Message		m_message;
	Label		m_text;
	Label		m_image;
	Label		m_date;
}