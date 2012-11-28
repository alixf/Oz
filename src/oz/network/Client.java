package oz.network;

import java.net.Socket;
import java.security.PublicKey;

import oz.data.UserData;
import oz.data.UserSummary;

public class Client
{
	public Client()
	{
		m_userData = new UserData();
		m_userSummary = new UserSummary();
		m_publicKey = null;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket()
	{
		return m_socket;
	}

	/**
	 * @param socket
	 *            the socket to set
	 */
	public void setSocket(Socket socket)
	{
		m_socket = socket;
	}

	public UserData getUserData()
	{
		return m_userData;
	}

	public void setUserData(UserData userData)
	{
		m_userData = userData;
		m_userSummary.setUsername(m_userData.getUsername());
	}
	
	public UserSummary getUserSummary()
	{
		return m_userSummary;
	}
	
	public void setUserSummary(UserSummary userSummary)
	{
		m_userSummary = userSummary;
	}
	
	public PublicKey getPublicKey()
	{
		return m_publicKey;
	}

	public void setPublicKey(PublicKey publicKey)
	{
		m_publicKey = publicKey;
	}

	private Socket		m_socket;
	private UserData	m_userData;
	private UserSummary	m_userSummary;
	private PublicKey	m_publicKey;
}
