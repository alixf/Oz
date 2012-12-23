package oz.network;

import java.nio.channels.SocketChannel;

// TODO: Auto-generated Javadoc
/**
 * The Interface EncryptionSystem.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public interface EncryptionSystem
{

	/**
	 * On client connect.
	 * 
	 * @param client the client
	 */
	void onClientConnect(Client client);

	/**
	 * On connect.
	 * 
	 * @param channel the channel
	 * @param client the client
	 */
	void onConnect(SocketChannel channel, Client client);

	/**
	 * On receive.
	 * 
	 * @param client the client
	 * @param packet the packet
	 * @return the string
	 */
	String onReceive(Client client, String packet);

	/**
	 * On send.
	 * 
	 * @param client the client
	 * @param packet the packet
	 * @return the string
	 */
	String onSend(Client client, String packet);
}
