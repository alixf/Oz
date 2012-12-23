package oz.network;

public class IdentityEncryption implements EncryptionSystem
{
	@Override
	public void onClientConnect(Client client)
	{
	}

	@Override
	public void onConnect(Client client)
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
