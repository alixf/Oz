package modules;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;

import data.File;

import network.Client;
import network.Network;

public class Files implements Module
{
	public Files(Network network)
	{
		m_network = network;
		m_network.setCommand("FILE", this);
		m_network.setCommand("GETFILE", this);
		
		Path path = Paths.get("images/avatar.png");
		try
		{	
			File file = new File();
			file.setName("images/avatar.png");
			file.setMtime(0);
			file.setContent(Base64.encodeBase64String(java.nio.file.Files.readAllBytes(path)));
			
			System.out.println(network.makePacket("FILE", file));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean executeCommand(String command, Client client)
	{
		String commandCode = m_network.getCommand(command);
		
		if(commandCode.equals("FILE"))
		{
			//data.File file = m_network.parsePacket(command, data.File.class);
		}
		if(commandCode.equals("GETFILE"))
		{
			
		}
		
		return false;
	}
	
	Network m_network;
}
