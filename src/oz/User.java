package oz;

import flexjson.JSON;
import oz.data.UserData;

public class User extends UserData
{
	private static User instance = new User();
	
	private User()
	{
		super();
		m_password = new String();
		m_valid = false;
	}
	
	public static User getUser()
	{	
		return instance;
	}
	
	@JSON(include=false)
	public String getPassword()
	{
		return m_password;
	}

	public void setPassword(String password)
	{
		m_password = password;
	}
	
	/**
	 * @return the m_valid
	 */
	public boolean isValid()
	{
		return m_valid;
	}

	/**
	 * @param m_valid the m_valid to set
	 */
	public void setValid(boolean valid)
	{
		m_valid = valid;
	}

	public void save()
	{
		super.saveTo("users/" + getUsername() + "/" + getUsername() + ".ozp");
	}
	
	private String m_password;
	private boolean m_valid;
}
