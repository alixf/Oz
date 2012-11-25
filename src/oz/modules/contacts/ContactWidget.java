package oz.modules.contacts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import oz.network.Client;
import oz.tools.Images;

public class ContactWidget extends Composite
{
	public ContactWidget(Contacts contacts, Composite parent, Client client)
	{
		super(parent, SWT.BORDER);
		setLayout(new FormLayout());

		m_client = client;
		m_contacts = contacts;

		/*
		 * Image label
		 */
		m_image = new Label(this, SWT.NONE);
		FormData layoutData = new FormData();
		layoutData.left = new FormAttachment(0, 0);
		layoutData.top = new FormAttachment(0, 0);
		layoutData.bottom = new FormAttachment(100, 0);
		layoutData.width = 64;
		layoutData.height = 64;
		m_image.setLayoutData(layoutData);

		/*
		 * Full name label
		 */
		m_name = new Label(this, SWT.NONE);
		m_name.setText(client.getUserData().getBiography().getFirstName() + " " + client.getUserData().getBiography().getLastName());
		layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 5);
		layoutData.left = new FormAttachment(m_image, 5, SWT.RIGHT);
		m_name.setLayoutData(layoutData);

		/*
		 * Username label
		 */
		m_username = new Label(this, SWT.NONE);
		
		m_username.setText(client.getUserData().getUsername());
		layoutData = new FormData();
		layoutData.left = new FormAttachment(m_image, 5, SWT.RIGHT);
		layoutData.top = new FormAttachment(m_name, 2, SWT.BOTTOM);
		m_username.setLayoutData(layoutData);

		// Avatar request
		if (client.getUserData().getAvatar() != null)
		{
			m_contacts.addFileRequest(client, client.getUserData().getAvatar());
			m_image.setImage(Images.resize(new Image(Display.getCurrent(), "images/loading.png"), 64, 64));
		}
		else
			m_image.setImage(Images.resize(new Image(Display.getCurrent(), client.getUserData().getAvatarFilename()), 64, 64));
	}

	public void setImage(Image image)
	{
		m_image.setImage(Images.resize(image, 64, 64));
		getParent().layout();
	}

	public void updateData()
	{
		m_name.setText(m_client.getUserData().getBiography().getFirstName() + " " + m_client.getUserData().getBiography().getLastName());
		m_username.setText(m_client.getUserData().getUsername());
		layout();

		// Avatar request
		if (m_client.getUserData().getAvatar() != null)
			m_contacts.addFileRequest(m_client, m_client.getUserData().getAvatar());
	}

	Contacts	m_contacts;
	Client		m_client;
	Label		m_image;
	Label		m_name;
	Label		m_username;
}
