package oz.data;

import flexjson.JSON;

/**
 * Instances of this class represent the necessary data to identify a user.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class UserIdentifier
{

	/** The address of the user. */
	private Address	m_address;

	/** The username of the user. */
	private String	m_username;

	/** The UUID of the user. */
	private String	m_uuid;

	/**
	 * Default constructor.
	 */
	public UserIdentifier()
	{
	}

	/**
	 * Create a userSummary from an username and an address.
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
	 * Return the address of the user.
	 * 
	 * @return the address of the user
	 */
	@JSON
	public Address getAddress()
	{
		return m_address;
	}

	/**
	 * Return the username of the user.
	 * 
	 * @return the username of the user
	 */
	@JSON
	public String getUsername()
	{
		return m_username;
	}

	/**
	 * Return the uuid of the user.
	 * 
	 * @return the uuid of the user
	 */
	@JSON
	public String getUUID()
	{
		return m_uuid;
	}

	/**
	 * Set the address of the user.
	 * 
	 * @param address an address
	 */
	public void setAddress(Address address)
	{
		m_address = address;
	}

	/**
	 * Set the username of the user.
	 * 
	 * @param username an username
	 */
	public void setUsername(String username)
	{
		m_username = username;
	}

	/**
	 * Set the uuid of the user.
	 * 
	 * @param uuid an uuid
	 */
	public void setUUID(String uuid)
	{
		m_uuid = uuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "{" + getUsername() + "," + getAddress() + "," + getUUID() + "}";
	}
}
