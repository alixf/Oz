package oz.data;

import java.util.List;

/**
 * Instances of this class represent a group of users
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Group
{
	/**
	 * Default constructor
	 */
	public Group()
	{
	}

	/**
	 * Create a group from its name
	 * 
	 * @param name the name of the group
	 */
	public Group(String name)
	{
		setName(name);
	}

	/**
	 * Return the name of the group
	 * 
	 * @return the name of the group
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * Set the name of the group
	 * 
	 * @param name a name for the group
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * Return the users list of the group
	 * 
	 * @return the users list of the group
	 */
	public List<UserSummary> getUsers()
	{
		return m_users;
	}

	/**
	 * Set the users list of the group
	 * 
	 * @param users a users list
	 */
	public void setUsers(List<UserSummary> users)
	{
		m_users = users;
	}

	/**
	 * The name of the group
	 */
	private String				m_name;
	/**
	 * The users list of the group
	 */
	private List<UserSummary>	m_users;
}
