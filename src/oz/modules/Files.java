package oz.modules;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import oz.data.File;

import oz.network.Client;
import oz.network.Network;

public class Files implements Module
{
	public interface Observer
	{
		public void fileNotify(Client client, String request);
	}

	public static class Request
	{
		public Request(Client client, String requestString)
		{
			this.client = client;
			this.requestString = requestString;

		}

		@Override
		public boolean equals(Object o)
		{
			if (o == null || o.getClass() != getClass())
				return false;

			final Request other = (Request) o;
			return other.client.equals(client) && other.requestString.equals(requestString);
		}

		public int hashCode()
		{
			// TODO Improve that for better performance
			int hc = (client.getUserData().getUsername() == null ? 0 : client.getUserData().getUsername().length()) + requestString.length();
			return hc;
		}

		public Client	client;
		public String	requestString;
	}

	public Files(Network network)
	{
		m_network = network;
		m_requests = new HashMap<Request, Observer>();
		m_network.setCommand("FILE", this);
		m_network.setCommand("GETFILE", this);
	}

	@Override
	public boolean executeCommand(String command, Client client)
	{
		String commandCode = m_network.getCommand(command);

		if (commandCode.equals("FILE"))
		{
			File file = m_network.parsePacket(command, File.class);
			try
			{
				String filepath = "files/" + client.getUserData().getUsername() + "/" + file.getName();
				new java.io.File(filepath).getParentFile().mkdirs();
				System.out.println("Created " + filepath);
				FileOutputStream fos = new FileOutputStream(filepath);
				fos.write(Base64.decodeBase64(file.getContent()));
				fos.close();

				Observer observer = m_requests.get(new Request(client, file.getName()));
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
			Path path = Paths.get(file.getName());
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

	public void addFileRequest(Client client, String request, Observer observer)
	{
		File file = new File();
		file.setName(request);
		try
		{
			m_network.send(m_network.makePacket("GETFILE", file), client);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_requests.put(new Request(client, request), observer);
	}

	Network						m_network;
	HashMap<Request, Observer>	m_requests;
}
