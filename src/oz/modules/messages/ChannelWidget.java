package oz.modules.messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import oz.data.UserData;
import oz.modules.messages.Messages.Channel;
import oz.tools.Images;

/**
 * This widget displays a channel.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class ChannelWidget extends Composite
{

	/** The channel. */
	Channel		m_channel;

	/** The image. */
	Label		m_image;

	/** The message. */
	Label		m_message;

	/** The messages. */
	Messages	m_messages;

	/**
	 * Instantiates a new channel widget.
	 * 
	 * @param messages the messages
	 * @param parent the parent
	 * @param channel the channel
	 */
	public ChannelWidget(Messages messages, Composite parent, Channel channel)
	{
		super(parent, SWT.BORDER);
		setLayout(new FormLayout());

		m_channel = channel;
		m_messages = messages;
		UserData user = channel.getClients().get(0).getUserData();

		m_message = new Label(this, SWT.NONE);
		m_message.setText(m_message.getText());

		m_image = new Label(this, SWT.NONE);
		m_image.setImage(Images.resize(new Image(getDisplay(), user.getAvatarFilename()), 64, 64));

		addListener(SWT.MouseDown, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				m_messages.setChannel(m_channel);
			}
		});
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

	/**
	 * Gets the channel.
	 * 
	 * @return the channel
	 */
	public Channel getChannel()
	{
		return m_channel;
	}

	/**
	 * Sets the color.
	 * 
	 * @param r the r
	 * @param g the g
	 * @param b the b
	 */
	public void setColor(int r, int g, int b)
	{
		Device device = Display.getCurrent();
		setBackground(new Color(device, r, g, b));
		redraw();
	}
}
