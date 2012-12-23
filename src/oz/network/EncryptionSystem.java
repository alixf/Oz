package oz.network;

public interface EncryptionSystem
{
	void onClientConnect(Client client);

	void onConnect(Client client);

	String onReceive(Client client, String packet);

	String onSend(Client client, String packet);
}
