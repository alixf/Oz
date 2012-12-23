package oz.network;

import java.net.Socket;
import java.security.PublicKey;

import oz.data.UserData;
import oz.data.UserIdentifier;

public class Client
{
	public Client()
	{
		m_userData = new UserData();
		m_userIdentifier = new UserIdentifier();
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
		m_userIdentifier.setUsername(m_userData.getUsername());
	}

	public UserIdentifier getUserSummary()
	{
		return m_userIdentifier;
	}

	public void setUserSummary(UserIdentifier userSummary)
	{
		m_userIdentifier = userSummary;
	}

	public PublicKey getPublicKey()
	{
		return m_publicKey;
	}

	public void setPublicKey(PublicKey publicKey)
	{
		m_publicKey = publicKey;
	}

	private Socket			m_socket;
	private UserData		m_userData;
	private UserIdentifier	m_userIdentifier;
	private PublicKey		m_publicKey;
}
