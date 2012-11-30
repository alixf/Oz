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
	/**
	 * Default constructor
	 */
	public UserData()
	{
		m_username = new String();
		setBiography(new Biography());
		m_friends = new LinkedList<UserSummary>();
		m_friendGroups = new LinkedList<Group>();	
		m_followers = new LinkedList<UserSummary>();
		m_posts = new LinkedList<Message>();
	}

	/**
	 * Return the username of the user
	 * 
	 * @return the username of the user
	 */
	public String getUsername()
	{
		return m_username;
	}

	/**
	 * Set the username of the user
	 * 
	 * @param username a username
	 */
	public void setUsername(String username)
	{
		m_username = username;
	}

	/**
	 * Return the avatar of the user
	 * 
	 * @return the avatar of the user as a string
	 */
	public String getAvatar()
	{
		return m_avatar;
	}

	/**
	 * Set the avatar of the user
	 * 
	 * @param avatar a string representing an avatar
	 */
	public void setAvatar(String avatar)
	{
		m_avatar = avatar;
	}

	/**
	 * Return the biography of the user
	 * 
	 * @return the biography of the user
	 */
	public Biography getBiography()
	{
		return m_biography;
	}

	/**
	 * Set the biography of the user
	 * 
	 * @param biography a biography
	 */
	public void setBiography(Biography biography)
	{
		m_biography = biography;
	}

	/**
	 * Return the friend list of the user
	 * 
	 * @return the friend list of the user
	 */
	public List<UserSummary> getFriends()
	{
		return m_friends;
	}

	/**
	 * Set the friend list of the user
	 * 
	 * @param friends a friend list
	 */
	public void setFriends(List<UserSummary> friends)
	{
		m_friends = friends;
	}

	/**
	 * Return the friend group list of the user
	 * 
	 * @return the friend group list of the user
	 */
	public List<Group> getFriendGroups()
	{
		return m_friendGroups;
	}

	/**
	 * Set the friend group list of the user
	 * 
	 * @param friendGroups a friend group list
	 */
	public void setFriendGroups(List<Group> friendGroups)
	{
		m_friendGroups = friendGroups;
	}

	/**
	 * Return the follower list of the user
	 * 
	 * @return the follower list of the user
	 */
	public List<UserSummary> getFollowers()
	{
		return m_followers;
	}

	/**
	 * Set the follower list of the user
	 * 
	 * @param followers a follower list
	 */
	public void setFollowers(List<UserSummary> followers)
	{
		m_followers = followers;
	}

	/**
	 * Return the post list of the user
	 * 
	 * @return the post list of the user
	 */
	public List<Message> getPosts()
	{
		return m_posts;
	}

	/**
	 * Set the post list of the user
	 * 
	 * @param posts a post list
	 */
	public void setPosts(List<Message> posts)
	{
		m_posts = posts;
	}
	
	/**
	 * Return the complete filename for the avatar of the user
	 * 
	 * @return the complete filename for the avatar of the user
	 */
	public String getAvatarFilename()
	{
		return getAvatar() == null ? "images/defaultProfilePicture.png" : "users/" + User.getUser().getUsername() + "/files/" + getUsername() + "/" + getAvatar();
	}

	/**
	 * Save the user profile to a file
	 * 
	 * @param filename a valid filename
	 */
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
	
	/**
	 * Instances of this class represents the biography of a user
	 * 
	 * @author Alix "eolhing" Fumoleau
	 * @author Jean "Jack3113" Batista
	 */
	public static class Biography
	{
		/**
		 * Default constructor
		 */
		public Biography()
		{
		}

		/**
		 * Get the first name of the biography
		 * 
		 * @return the first name of the biography
		 */
		public String getFirstName()
		{
			return m_firstName;
		}

		/**
		 * Set the first name of the biography
		 * 
		 * @param firstName a name as a string
		 */
		public void setFirstName(String firstName)
		{
			m_firstName = firstName;
		}

		/**
		 * Get the last name of the biography
		 * 
		 * @return the last name of the biography
		 */
		public String getLastName()
		{
			return m_lastName;
		}

		/**
		 * Set the last name of the biography
		 * 
		 * @param lastName a name as a string
		 */
		public void setLastName(String lastName)
		{
			m_lastName = lastName;
		}

		/**
		 * Return the birth date of the biography
		 * 
		 * @return the birth date of the biography as an integer representing a timestamp
		 */
		public Integer getBirthDate()
		{
			return m_birthDate;
		}

		/**
		 * Set the birth date of the biography
		 * 
		 * @param birthDate an integer representing the timestamp of the birth date
		 */
		public void setBirthDate(Integer birthDate)
		{
			m_birthDate = birthDate;
		}

		/**
		 * Get the description of the biography
		 * 
		 * @return the description of the biography
		 */
		public String getDescription()
		{
			return m_description;
		}

		/**
		 * Set the description of the biography
		 * 
		 * @param description a description as a string
		 */
		public void setDescription(String description)
		{
			m_description = description;
		}

		/**
		 * The first name of the biography
		 */
		private String	m_firstName;
		/**
		 * The last name of the biography
		 */
		private String	m_lastName;
		/**
		 * The birth date of the biography
		 */
		private Integer	m_birthDate;
		/**
		 * The description of the biography
		 */
		private String	m_description;
	}

	/**
	 * The username of the user
	 */
	private String				m_username;
	/**
	 * The avatar of the user
	 */
	private String				m_avatar;
	/**
	 * The biography of the user
	 */
	private Biography			m_biography;
	/**
	 * The friend list of the user
	 */
	private List<UserSummary>	m_friends;
	/**
	 * The friend group list of the user
	 */
	private List<Group>			m_friendGroups;
	/**
	 * The follower list of the user
	 */
	private List<UserSummary>	m_followers;
	/**
	 * The post list of the user
	 */
	private List<Message>		m_posts;
}
