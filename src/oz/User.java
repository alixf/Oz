package oz;

import oz.data.UserData;
import flexjson.JSON;

/**
 * Singleton class to represent the logged user.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class User extends UserData
{

	/** The user instance. */
	private static User	instance	= new User();

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public static User getUser()
	{
		return instance;
	}

	/** The password of the user. */
	private String	m_password;

	/** The valid state of the user. */
	private boolean	m_valid;

	/**
	 * Instantiates a new user.
	 */
	private User()
	{
		super();
		m_password = new String();
		m_valid = false;
	}

	/**
	 * Gets the user's password.
	 * 
	 * @return the user's password
	 */
	@JSON(include = false)
	public String getPassword()
	{
		return m_password;
	}

	/**
	 * Checks the user is valid.
	 * 
	 * @return true, if the user is valid
	 */
	@JSON(include = false)
	public boolean isValid()
	{
		return m_valid;
	}

	/**
	 * Save the user data to its profile file
	 */
	public void save()
	{
		super.saveTo("users/" + getUsername() + "/" + getUsername() + ".ozp");
	}

	/**
	 * Sets the user's password.
	 * 
	 * @param password the new password
	 */
	public void setPassword(String password)
	{
		m_password = password;
	}

	/**
	 * Sets the user's valid state
	 * 
	 * @param valid the new valid
	 */
	public void setValid(boolean valid)
	{
		m_valid = valid;
	}
}
