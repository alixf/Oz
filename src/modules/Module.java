package modules;

import network.Client;

public interface Module
{
	public boolean executeCommand(String command, Client client);
}
