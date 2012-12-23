package oz.modules;

import oz.network.Client;

// TODO: Auto-generated Javadoc
/**
 * The Interface Module.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public interface Module
{

	/**
	 * Execute command.
	 * 
	 * @param command the command
	 * @param client the client
	 * @return true, if successful
	 */
	public boolean executeCommand(String command, Client client);
}
