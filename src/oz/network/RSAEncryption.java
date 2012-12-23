package oz.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;

import oz.security.RSA;

/**
 * This encryption method implements RSA Encryption
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class RSAEncryption implements EncryptionSystem
{

	/** The network. */
	private Network	m_network;

	/** The rsa instance. */
	private RSA		m_rsa;

	/**
	 * Instantiates a new rSA encryption.
	 * 
	 * @param network the network
	 */
	public RSAEncryption(Network network)
	{
		m_network = network;
		m_rsa = new RSA();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onClientConnect(oz.network.Client)
	 */
	@Override
	public void onClientConnect(Client client)
	{
		sendKey(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onConnect(java.nio.channels.SocketChannel, oz.network.Client)
	 */
	@Override
	public void onConnect(SocketChannel channel, Client client)
	{
		sendKey(client);
		try
		{
			if (!channel.isBlocking())
				channel.configureBlocking(true);
			m_network.read(channel, client);
			channel.configureBlocking(false);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onReceive(oz.network.Client, java.lang.String)
	 */
	@Override
	public String onReceive(Client client, String packet)
	{
		if (!packet.substring(0, 4).equals("KEY "))
			return m_rsa.decryptCommand(packet);
		else
		{
			String key = packet.substring(4);
			System.out.println("Set client public key : " + key);
			client.setPublicKey(RSA.convertBase64EncodedPublicKey(key));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onSend(oz.network.Client, java.lang.String)
	 */
	@Override
	public String onSend(Client client, String packet)
	{
		if (client.getPublicKey() != null)
			return m_rsa.encryptCommand(packet, client.getPublicKey());
		return packet;
	}

	/**
	 * Send key.
	 * 
	 * @param client the client
	 */
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
}
