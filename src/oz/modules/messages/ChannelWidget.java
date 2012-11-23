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

		m_image = new Label(this, SWT.NONE);
		Image image = new Image(getDisplay(), "files/" + user.getUsername() + "/" + user.getAvatar());
		m_image.setImage(image);

		Device device = Display.getCurrent();
		setBackground(new Color(device, 127, 190, 255));
		/*
		 * Label newChannel = new Label(this, SWT.BORDER);
		 * newChannel.setText(user.getUsername());
		 * newChannel.setImage(image);
		 * RowData rd = new RowData();
		 * rd.width = this.getSize().x;
		 * System.out.println(rd.width);
		 * newChannel.setLayoutData(rd);
		 */

		addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				System.out.println("Widget");
			}
		});
		m_image.addListener(SWT.DefaultSelection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				System.out.println("Image");
			}
		});
	}

	Messages	m_messages;
	Channel		m_channel;
	Label		m_message;
	Label		m_image;
}
