package oz.network;

import java.nio.channels.SocketChannel;

public class IdentityEncryption implements EncryptionSystem
{
	@Override
	public void onClientConnect(Client client)
	{
	}

	@Override
	public void onConnect(SocketChannel channel, Client client)
	{
	}

	@Override
	public String onReceive(Client client, String packet)
	{
		return packet;
	}

	@Override
	public String onSend(Client client, String packet)
	{
		return packet;
	}

}
