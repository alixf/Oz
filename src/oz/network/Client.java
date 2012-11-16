package oz.network;

import java.net.Socket;

import oz.data.UserData;

public class Client
{
	public Client()
	{
		m_userData = new UserData();
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
	}

	private Socket		m_socket;
	private UserData	m_userData;
}
