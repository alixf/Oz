package oz.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;

import oz.security.RSA;

public class RSAEncryption implements EncryptionSystem
{
	public RSAEncryption(Network network)
	{
		m_network = network;
		m_rsa = new RSA();
	}

	@Override
	public void onClientConnect(Client client)
	{
		sendKey(client);
	}

	public void onConnect(SocketChannel channel, Client client)
	{
		sendKey(client);
		try
		{
			if(!channel.isBlocking())
				channel.configureBlocking(true);
			m_network.read(channel, client);
			channel.configureBlocking(false);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String onReceive(Client client, String packet)
	{
		if (!packet.substring(0, 4).equals("KEY "))
			return m_rsa.decryptCommand(packet);
		else
		{
			String key = packet.substring(4);
			System.out.println("Set client public key : "+key);
			client.setPublicKey(RSA.convertBase64EncodedPublicKey(key));
		}
		return null;
	}

	@Override
	public String onSend(Client client, String packet)
	{
		if (client.getPublicKey() != null)
			return m_rsa.encryptCommand(packet, client.getPublicKey());
		return packet;
	}

	public void sendKey(Client client)
	{
		try
		{
			String key = m_rsa.getBase64EncodedPublicKey();
			String packet = "KEY " + key;
			m_network.send(packet, client);
		}
		catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private Network	m_network;
	private RSA		m_rsa;
}
