package oz.data;

/**
 * Instances of this class represent the necessary data to identify a user
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class UserSummary
{
	public UserSummary()
	{
	}

	public UserSummary(String username, Address address)
	{
		setUsername(username);
		setAddress(address);
	}

	public Address getAddress()
	{
		return m_address;
	}

	public void setAddress(Address address)
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

	private Address	m_address;
	private String	m_username;
}
