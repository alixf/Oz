package oz.network;

import java.nio.channels.SocketChannel;

/**
 * This encryption system is the identity encryption (no encryption)
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class IdentityEncryption implements EncryptionSystem
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onClientConnect(oz.network.Client)
	 */
	@Override
	public void onClientConnect(Client client)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onConnect(java.nio.channels.SocketChannel, oz.network.Client)
	 */
	@Override
	public void onConnect(SocketChannel channel, Client client)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onReceive(oz.network.Client, java.lang.String)
	 */
	@Override
	public String onReceive(Client client, String packet)
	{
		return packet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oz.network.EncryptionSystem#onSend(oz.network.Client, java.lang.String)
	 */
	@Override
	public String onSend(Client client, String packet)
	{
		return packet;
	}

}
