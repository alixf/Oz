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

/**
 * This widget displays a message
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class MessageWidget extends Composite
{

	/** The date. */
	Label		m_date;

	/** The image. */
	Label		m_image;

	/** The message. */
	Message		m_message;

	/** The messages. */
	Messages	m_messages;

	/** The text. */
	Label		m_text;

	/**
	 * Instantiates a new message widget.
	 * 
	 * @param messages the messages
	 * @param parent the parent
	 * @param message the message
	 */
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
		m_text = new Label(this, SWT.NONE | SWT.WRAP);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener(int, org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void addListener(int eventType, Listener listener)
	{
		super.addListener(eventType, listener);
		for (Control control : getChildren())
			control.addListener(eventType, listener);
	}
}