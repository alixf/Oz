package oz.network;

import java.net.Socket;
import java.security.PublicKey;

import oz.data.UserData;

// TODO: Auto-generated Javadoc
/**
 * The Class Client.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Client
{

	/** The m_public key. */
	private PublicKey	m_publicKey;

	/** The m_socket. */
	private Socket		m_socket;

	/** The m_user data. */
	private UserData	m_userData;

	/**
	 * Instantiates a new client.
	 */
	public Client()
	{
		m_userData = new UserData();
		m_publicKey = null;
	}

	/**
	 * Gets the public key.
	 * 
	 * @return the public key
	 */
	public PublicKey getPublicKey()
	{
		return m_publicKey;
	}

	/**
	 * Gets the socket.
	 * 
	 * @return the socket
	 */
	public Socket getSocket()
	{
		return m_socket;
	}

	/**
	 * Gets the user data.
	 * 
	 * @return the user data
	 */
	public UserData getUserData()
	{
		return m_userData;
	}

	/**
	 * Sets the public key.
	 * 
	 * @param publicKey the new public key
	 */
	public void setPublicKey(PublicKey publicKey)
	{
		m_publicKey = publicKey;
	}

	/**
	 * Sets the socket.
	 * 
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket)
	{
		m_socket = socket;
	}

	/**
	 * Sets the user data.
	 * 
	 * @param userData the new user data
	 */
	public void setUserData(UserData userData)
	{
		m_userData = userData;
	}
}
