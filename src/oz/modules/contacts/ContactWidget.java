package oz.modules.contacts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import oz.data.UserIdentifier;
import oz.network.Client;
import oz.tools.Images;

/**
 * This widget display a contact
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class ContactWidget extends Composite
{

	/** The client. */
	Client		m_client;

	/** The contacts module. */
	Contacts	m_contacts;

	/** The image. */
	Label		m_image;

	/** The name. */
	Label		m_name;

	/** The username. */
	Label		m_username;

	/**
	 * Instantiates a new contact widget.
	 * 
	 * @param contacts the contacts
	 * @param parent the parent
	 * @param user the user
	 * @param client the client
	 */
	public ContactWidget(Contacts contacts, Composite parent, UserIdentifier user, Client client)
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
		layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 5);
		layoutData.left = new FormAttachment(m_image, 5, SWT.RIGHT);
		m_name.setLayoutData(layoutData);

		/*
		 * Username label
		 */
		m_username = new Label(this, SWT.NONE);

		m_username.setText(user.getUsername());
		layoutData = new FormData();
		layoutData.left = new FormAttachment(m_image, 5, SWT.RIGHT);
		layoutData.top = new FormAttachment(m_name, 2, SWT.BOTTOM);
		m_username.setLayoutData(layoutData);

		// Avatar request
		if (m_client != null)
		{
			m_name.setText(m_client.getUserData().getBiography().getFirstName() + " " + m_client.getUserData().getBiography().getLastName());
			if (m_client.getUserData().getAvatar() != null)
			{
				m_contacts.addFileRequest(m_client, m_client.getUserData().getAvatar());
				m_image.setImage(Images.resize(new Image(Display.getCurrent(), "images/loading.png"), 64, 64));
			}
			else
				m_image.setImage(Images.resize(new Image(Display.getCurrent(), m_client.getUserData().getAvatarFilename()), 64, 64));
		}
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
	 * Gets the client.
	 * 
	 * @return the client
	 */
	public Client getClient()
	{
		return m_client;
	}

	/**
	 * Sets the client.
	 * 
	 * @param client the new client
	 */
	public void setClient(Client client)
	{
		m_client = client;
	}

	/**
	 * Sets the image.
	 * 
	 * @param image the new image
	 */
	public void setImage(Image image)
	{
		m_image.setImage(Images.resize(image, 64, 64));
		getParent().layout();
	}

	/**
	 * Update data.
	 */
	public void updateData()
	{
		if (m_client != null)
		{
			m_name.setText(m_client.getUserData().getBiography().getFirstName() + " " + m_client.getUserData().getBiography().getLastName());
			m_username.setText(m_client.getUserData().getUsername());
			layout();

			// Avatar request
			if (m_client.getUserData().getAvatar() != null)
				m_contacts.addFileRequest(m_client, m_client.getUserData().getAvatar());
		}
	}
}
