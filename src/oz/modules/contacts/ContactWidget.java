package oz.modules.contacts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import oz.network.Client;

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
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		m_image.setLayoutData(formData);
		m_image.setSize(100, 100);

		/*
		 * Full name label
		 */
		m_name = new Label(this, SWT.NONE);
		m_name.setText(client.getUserData().getBiography().getFirstName() + " " + client.getUserData().getBiography().getLastName());
		formData = new FormData();
		formData.left = new FormAttachment(m_image, 5, SWT.RIGHT);
		m_name.setLayoutData(formData);

		/*
		 * Username label
		 */
		m_username = new Label(this, SWT.NONE);
		m_username.setText(client.getUserData().getUsername());
		formData = new FormData();
		formData.left = new FormAttachment(m_image, 5, SWT.RIGHT);
		formData.top = new FormAttachment(m_name, 2, SWT.BOTTOM);
		m_username.setLayoutData(formData);

		// Avatar request
		if (client.getUserData().getAvatar() != null)
			m_contacts.addFileRequest(client, client.getUserData().getAvatar());
	}

	public void setImage(Image image)
	{
		m_image.setImage(image);
		layout();
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
