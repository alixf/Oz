package oz.data;

import flexjson.JSON;

/**
 * Instances of this class represent a network address
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Address
{
	/**
	 * Default Constructor
	 */
	public Address()
	{
		m_host = new String();
		m_port = 0;
	}

	/**
	 * Create an address from a host and a port
	 * 
	 * @param host the host of the address
	 * @param port the port of the adress
	 */
	public Address(String host, int port)
	{
		m_host = host;
		m_port = port;
	}

	/**
	 * Return the host of the adress
	 * 
	 * @return the host of the address
	 */
	@JSON
	public String getHost()
	{
		return m_host;
	}

	/**
	 * Set the host of the address
	 * 
	 * @param host the host of the address
	 */
	public void setHost(String host)
	{
		m_host = host;
	}

	/**
	 * Return the port of the address
	 * 
	 * @return the port of the address
	 */
	@JSON
	public int getPort()
	{
		return m_port;
	}

	/**
	 * Set the port of the address
	 * 
	 * @param port the port of the address
	 */
	public void setPort(int port)
	{
		m_port = port;
	}

	/**
	 * Check if the address is equal to another address
	 * 
	 * @param address another address to be compared to
	 * @return true if the to addresses are equals (that is, hosts are equals and ports are equals)
	 */
	public boolean equals(Address address)
	{
		return m_host.equals(address.getHost()) && m_port == address.getPort();
	}
	
	public String toString()
	{
		return "{"+m_host+","+m_port+"}";
	}

	/**
	 * The host of the address
	 */
	private String	m_host;
	/**
	 * The port of the address
	 */
	private int		m_port;
}
