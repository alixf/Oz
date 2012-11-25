package oz.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import oz.User;
import oz.security.XOR;

import flexjson.JSONSerializer;

/**
 * Instances of this class represent the data of a user
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class UserData
{	
	public UserData()
	{
		m_username = new String();
		setBiography(new Biography());
		m_friends = new LinkedList<UserSummary>();
		m_friendGroups = new LinkedList<Group>();	
		m_followers = new LinkedList<UserSummary>();
		m_posts = new LinkedList<Message>();
	}

	public String getUsername()
	{
		return m_username;
	}

	public void setUsername(String username)
	{
		m_username = username;
	}

	public String getAvatar()
	{
		return m_avatar;
	}

	public void setAvatar(String avatar)
	{
		m_avatar = avatar;
	}

	public Biography getBiography()
	{
		return m_biography;
	}

	public void setBiography(Biography biography)
	{
		m_biography = biography;
	}

	public List<UserSummary> getFriends()
	{
		return m_friends;
	}

	public void setFriends(List<UserSummary> friends)
	{
		m_friends = friends;
	}

	public List<Group> getFriendGroups()
	{
		return m_friendGroups;
	}

	public void setFriendGroups(List<Group> friendGroups)
	{
		m_friendGroups = friendGroups;
	}

	public List<UserSummary> getFollowers()
	{
		return m_followers;
	}

	public void setFollowers(List<UserSummary> followers)
	{
		m_followers = followers;
	}

	public List<Message> getPosts()
	{
		return m_posts;
	}

	public void setPosts(List<Message> posts)
	{
		m_posts = posts;
	}
	
	public String getAvatarFilename()
	{
		return getAvatar() == null ? "images/defaultProfilePicture.png" : "users/" + getUsername() + "/files/" + getUsername() + "/" + getAvatar();
	}

	public void saveTo(String filename)
	{
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class");
		serializer.include("*");
		
		try
		{
			File profileFile = new File(filename);
			profileFile.getParentFile().mkdirs();
			profileFile.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(profileFile));
			bos.write(XOR.encrypt(serializer.serialize(this), User.getUser().getPassword()));
			bos.flush();
			bos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static class Biography
	{
		public Biography()
		{
		}

		public String getFirstName()
		{
			return m_firstName;
		}

		public void setFirstName(String firstName)
		{
			m_firstName = firstName;
		}

		public String getLastName()
		{
			return m_lastName;
		}

		public void setLastName(String lastName)
		{
			m_lastName = lastName;
		}

		public Integer getBirthDate()
		{
			return m_birthDate;
		}

		public void setBirthDate(Integer birthDate)
		{
			m_birthDate = birthDate;
		}

		public String getDescription()
		{
			return m_description;
		}

		public void setDescription(String description)
		{
			m_description = description;
		}

		private String	m_firstName;
		private String	m_lastName;
		private Integer	m_birthDate;
		private String	m_description;
	}

	private String				m_username;
	private String				m_avatar;
	private Biography			m_biography;
	private List<UserSummary>	m_friends;
	private List<Group>			m_friendGroups;
	private List<UserSummary>	m_followers;
	private List<Message>		m_posts;
}
