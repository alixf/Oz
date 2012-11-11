package data;

import java.util.List;

public class UserData
{	
	public UserData()
	{
		setBiography(new Biography());
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
	private List<Message>			m_posts;
}
