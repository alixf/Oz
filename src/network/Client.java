package network;

import java.net.Socket;

public class Client
{

	private Socket	m_socket;

	/**
	 * @return the socket
	 */
	public Socket getSocket()
	{
		return m_socket;
	}

	/**
	 * @param m_socket
	 *            the socket to set
	 */
	public void setSocket(Socket m_socket)
	{
		this.m_socket = m_socket;
	}
}
