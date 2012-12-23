package oz.data;

/**
 * Instances of this class represent the necessary data to identify a user
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class UserIdentifier
{
	/**
	 * Default constructor
	 */
	public UserIdentifier()
	{
	}

	/**
	 * Create a userSummary from an username and an address
	 * 
	 * @param username an username as a string
	 * @param address an adress
	 */
	public UserIdentifier(String username, Address address)
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
	 * Return the uuid of the user
	 * 
	 * @return the uuid of the user
	 */
	public String getUUID()
	{
		return m_uuid;
	}

	/**
	 * Set the uuid of the user
	 * 
	 * @param uuid an uuid
	 */
	public void setUUID(String uuid)
	{
		m_uuid = uuid;
	}

	/**
	 * The address of the user
	 */
	private Address	m_address;
	/**
	 * The username of the user
	 */
	private String	m_username;
	/**
	 * The UUID of the user
	 */
	private String m_uuid;
}
