package oz.modules;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;

import oz.data.File;

import oz.network.Client;
import oz.network.Network;

public class Files implements Module
{
	public Files(Network network)
	{
		m_network = network;
		m_network.setCommand("FILE", this);
		m_network.setCommand("GETFILE", this);
	}
	
	@Override
	public boolean executeCommand(String command, Client client)
	{
		String commandCode = m_network.getCommand(command);
		
		if(commandCode.equals("FILE"))
		{
			File file = m_network.parsePacket(command, File.class);
			try
			{
				FileOutputStream fos = new FileOutputStream("files/"+file.getName());
				fos.write(Base64.decodeBase64(file.getContent()));
				fos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			//TODO Notify
		}
		if(commandCode.equals("GETFILE"))
		{
			File file = m_network.parsePacket(command, File.class);
			Path path = Paths.get(file.getName());
			try
			{
				file.setContent(Base64.encodeBase64String(java.nio.file.Files.readAllBytes(path)));
				System.out.println(m_network.makePacket("FILE", file));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	Network m_network;
}
