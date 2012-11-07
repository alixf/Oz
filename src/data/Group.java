package data;

import java.util.List;

public class Group
{
	public Group(String name)
	{
		setName(name);
	}

	public String getName()
	{
		return m_name;
	}

	public void setName(String name)
	{
		m_name = name;
	}

	public List<UserSummary> getUsers()
	{
		return m_users;
	}

	public void setUsers(List<UserSummary> users)
	{
		m_users = users;
	}

	private String				m_name;
	private List<UserSummary>	m_users;
}
