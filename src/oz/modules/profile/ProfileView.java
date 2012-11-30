package oz.modules.profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import oz.data.UserData;
import oz.tools.Images;

public class ProfileView extends Composite
{
	public ProfileView(Composite parent)
	{
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		
		m_nameLabel = new Label(this, SWT.NONE);
		m_usernameLabel = new Label(this, SWT.NONE);
		m_avatarLabel = new Label(this, SWT.BORDER);
		
		Font font = m_nameLabel.getFont();
		FontData fontData = font.getFontData()[0];
		fontData.height *= 2;
		font = new Font(Display.getCurrent(), fontData);
		
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 10);
		layoutData.left = new FormAttachment(0, 10);
		m_avatarLabel.setLayoutData(layoutData);
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_avatarLabel, 10, SWT.TOP);
		layoutData.left = new FormAttachment(m_avatarLabel, 10, SWT.RIGHT);
		m_nameLabel.setLayoutData(layoutData);
		m_nameLabel.setFont(font);
		
		layoutData = new FormData();
		layoutData.top = new FormAttachment(m_nameLabel, 10, SWT.BOTTOM);
		layoutData.left = new FormAttachment(m_avatarLabel, 10, SWT.RIGHT);
		m_usernameLabel.setLayoutData(layoutData);
		m_usernameLabel.setFont(font);
	}

	public void setUser(UserData user)
	{
		m_nameLabel.setText(user.getBiography().getFirstName()+" "+user.getBiography().getLastName());
		m_usernameLabel.setText("("+user.getUsername()+")");
		m_avatarLabel.setImage(Images.resize(new Image(Display.getCurrent(), user.getAvatarFilename()), 128, 128));
		layout();
	}
		Label m_nameLabel;
	Label m_usernameLabel;
	Label m_avatarLabel;
}
