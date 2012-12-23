package oz.modules;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import oz.User;
import oz.data.File;
import oz.network.Client;
import oz.network.Network;

/**
 * This module class manages file transfer and storage
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Files implements Module
{

	/**
	 * The Interface Observer.
	 */
	public interface Observer
	{

		/**
		 * File notify.
		 * 
		 * @param client the client
		 * @param request the request
		 */
		public void fileNotify(Client client, String request);
	}

	/**
	 * The Class Request.
	 */
	public static class Request
	{

		/** The client. */
		public Client	client;

		/** The request string. */
		public String	requestString;

		/**
		 * Instantiates a new request.
		 * 
		 * @param client the client
		 * @param requestString the request string
		 */
		public Request(Client client, String requestString)
		{
			this.client = client;
			this.requestString = requestString;

		}

		/** 
		 * Return whether an object is equal to the request 
		 *
		 * @return True if the object is equal to the request
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o)
		{
			if (o == null || o.getClass() != getClass())
				return false;

			final Request other = (Request) o;
			return other.client.equals(client) && other.requestString.equals(requestString);
		}

		/**
		 * Get the hashCode of the Request
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			// TODO Improve that for better performance
			int hc = (client.getUserData().getUsername() == null ? 0 : client.getUserData().getUsername().length()) + requestString.length();
			return hc;
		}

		/**
		 * Get the string representation of the request
		 * 
		 * @return The String representation of the request
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "{" + client.getUserData().getUsername() + " : " + requestString + "}";
		}
	}

	/** The network. */
	Network						m_network;

	/** The requests. */
	HashMap<Request, Observer>	m_requests;

	/**
	 * Instantiates a new files module.
	 * 
	 * @param network the network
	 */
	public Files(Network network)
	{
		m_network = network;
		m_requests = new HashMap<Request, Observer>();
		m_network.setCommand("FILE", this);
		m_network.setCommand("GETFILE", this);
	}

	/**
	 * Adds a file request.
	 * 
	 * @param client the client
	 * @param request the request
	 * @param observer the observer
	 */
	public void addFileRequest(Client client, String request, Observer observer)
	{
		File file = new File();
		file.setName(client.getUserData().getUsername() + "/" + request);
		try
		{
			m_network.send(m_network.makePacket("GETFILE", file), client);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_requests.put(new Request(client, file.getName()), observer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.modules.Module#executeCommand(java.lang.String, oz.network.Client)
	 */
	@Override
	public boolean executeCommand(String command, Client client)
	{
		String commandCode = m_network.getCommand(command);

		if (commandCode.equals("FILE"))
		{
			File file = m_network.parsePacket(command, File.class);
			try
			{
				String filepath = "users/" + User.getUser().getUsername() + "/files/" + file.getName();
				new java.io.File(filepath).getParentFile().mkdirs();

				FileOutputStream fos = new FileOutputStream(filepath);
				fos.write(Base64.decodeBase64(file.getContent()));
				fos.close();

				Observer observer = m_requests.get(new Request(client, "" + file.getName()));
				if (observer != null)
					observer.fileNotify(client, file.getName());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (commandCode.equals("GETFILE"))
		{
			File file = m_network.parsePacket(command, File.class);
			Path path = Paths.get("users/" + User.getUser().getUsername() + "/files/" + file.getName());
			try
			{
				file.setContent(Base64.encodeBase64String(java.nio.file.Files.readAllBytes(path)));
				m_network.send(m_network.makePacket("FILE", file), client);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return false;
	}
}
