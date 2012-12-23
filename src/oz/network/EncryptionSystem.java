package oz.network;

import java.nio.channels.SocketChannel;

public interface EncryptionSystem
{
	void onClientConnect(Client client);

	void onConnect(SocketChannel channel,Client client);

	String onReceive(Client client, String packet);

	String onSend(Client client, String packet);
}
