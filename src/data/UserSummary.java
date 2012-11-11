package data;

public class UserSummary
{
	public UserSummary()
	{
	}
	
	public UserSummary(String username, String address)
	{
		setUsername(username);
		setAddress(address);
	}

	public String getAddress()
	{
		return m_address;
	}

	public void setAddress(String address)
	{
		m_address = address;
	}

	public String getUsername()
	{
		return m_username;
	}

	public void setUsername(String username)
	{
		m_username = username;
	}

	private String	m_address;
	private String	m_username;
}
