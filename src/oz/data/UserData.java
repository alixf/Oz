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
import flexjson.JSON;

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
		m_userIdentifier = new UserIdentifier();
		setBiography(new Biography());
		m_friends = new LinkedList<UserIdentifier>();
		m_friendGroups = new LinkedList<Group>();
		m_followers = new LinkedList<UserIdentifier>();
		m_posts = new LinkedList<Message>();
	}

	public UserIdentifier getUserIdentifier()
	{
		return m_userIdentifier;
	}
	
	public void setUserIdentifier(UserIdentifier userIdentifier)
	{
		m_userIdentifier = userIdentifier;
	}

	/**
	 * Return the username of the user
	 * 
	 * @return the username of the user
	 */
	@JSON(include=false)
	public String getUsername()
	{
		return m_userIdentifier.getUsername();
	}

	/**
	 * Set the username of the user
	 * 
	 * @param username a username
	 */
	public void setUsername(String username)
	{
		m_userIdentifier.setUsername(username);
	}
	
	/**
	 * Return the uuid of the user
	 * 
	 * @return the uuid of the user
	 */
	@JSON(include=false)
	public String getUUID()
	{
		return m_userIdentifier.getUUID();
	}

	/**
	 * Set the uuid of the user
	 * 
	 * @param u a uuid
	 */
	public void setUUID(String uuid)
	{
		m_userIdentifier.setUUID(uuid);
	}

	/**
	 * Return the avatar of the user
	 * 
	 * @return the avatar of the user as a string
	 */
	@JSON
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
	@JSON
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
	@JSON
	public List<UserIdentifier> getFriends()
	{
		return m_friends;
	}

	/**
	 * Set the friend list of the user
	 * 
	 * @param friends a friend list
	 */
	public void setFriends(List<UserIdentifier> friends)
	{
		m_friends = friends;
	}

	/**
	 * Return the friend group list of the user
	 * 
	 * @return the friend group list of the user
	 */
	@JSON
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
	@JSON
	public List<UserIdentifier> getFollowers()
	{
		return m_followers;
	}

	/**
	 * Set the follower list of the user
	 * 
	 * @param followers a follower list
	 */
	public void setFollowers(List<UserIdentifier> followers)
	{
		m_followers = followers;
	}

	/**
	 * Return the post list of the user
	 * 
	 * @return the post list of the user
	 */
	@JSON
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
	@JSON(include=false)
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
		@JSON
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
		@JSON
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
		@JSON
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
		@JSON
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
	 * The user identifier of the user
	 */
	private UserIdentifier			m_userIdentifier;
	/**
	 * The avatar of the user
	 */
	private String					m_avatar;
	/**
	 * The biography of the user
	 */
	private Biography				m_biography;
	/**
	 * The friend list of the user
	 */
	private List<UserIdentifier>	m_friends;
	/**
	 * The friend group list of the user
	 */
	private List<Group>				m_friendGroups;
	/**
	 * The follower list of the user
	 */
	private List<UserIdentifier>	m_followers;
	/**
	 * The post list of the user
	 */
	private List<Message>			m_posts;
}
