package oz.data;

/**
 * Instances of this class represent the necessary data to identify a user
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class UserSummary
{
	/**
	 * Default constructor
	 */
	public UserSummary()
	{
	}

	/**
	 * Create a userSummary from an username and an address
	 * 
	 * @param username an username as a string
	 * @param address an adress
	 */
	public UserSummary(String username, Address address)
	{
		setUsername(username);
		setAddress(address);
	}

	/**
	 * Return the address of the user
	 * 
	 * @return the address of the user
	 */
	public Address getAddress()
	{
		return m_address;
	}

	/**
	 * Set the address of the user
	 * 
	 * @param address an address
	 */
	public void setAddress(Address address)
	{
		m_address = address;
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
	 * @param username an username
	 */
	public void setUsername(String username)
	{
		m_username = username;
	}

	/**
	 * The address of the user
	 */
	private Address	m_address;
	/**
	 * The username of the user
	 */
	private String	m_username;
}
