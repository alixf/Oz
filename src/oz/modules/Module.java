package oz.modules;

import oz.network.Client;

public interface Module
{
	public boolean executeCommand(String command, Client client);
}
