package oz.modules;

import oz.network.*;
import oz.security.RSA;

public class Keys implements Module
{
	public Keys(Network network)
	{
		m_network = network;
		m_network.setCommand("KEY", this);
	}

	@Override
	public boolean executeCommand(String command, Client client)
	{

		String commandCode = m_network.getCommand(command);
		if (commandCode.equals("KEY"))
		{
			client.setPublicKey(RSA.convertBase64EncodedPublicKey(command.substring(4)));
			return true;
		}
		return false;
	}

	private Network	m_network;
}
