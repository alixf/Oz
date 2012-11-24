package oz.modules.messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import oz.data.UserData;
import oz.modules.messages.Messages.Channel;

public class ChannelWidget extends Composite
{
	public ChannelWidget(Messages messages, Composite parent, Channel channel)
	{
		super(parent, SWT.BORDER);
		setLayout(new FormLayout());

		m_channel = channel;
		m_messages = messages;
		UserData user = channel.getClients().get(0).getUserData();

		m_message = new Label(this, SWT.NONE);
		m_message.setText(m_message.getText());

		if(user.getAvatar() != null)
		{
			m_image = new Label(this, SWT.NONE);
			Image image = new Image(getDisplay(), "files/" + user.getUsername() + "/" + user.getAvatar());
			m_image.setImage(image);
			m_image.addListener(SWT.MouseDown, new Listener()
			{
				@Override
				public void handleEvent(Event event)
				{
					m_messages.setChannel(m_channel);
				}
			});
		}
		
		addListener(SWT.MouseDown, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				m_messages.setChannel(m_channel);
			}
		});
	}

	public void setColor(int r, int g, int b)
	{
		Device device = Display.getCurrent();
		setBackground(new Color(device, r, g, b));
		redraw();
	}

	public Channel getChannel()
	{
		return m_channel;
	}

	Messages	m_messages;
	Channel		m_channel;
	Label		m_message;
	Label		m_image;
}
