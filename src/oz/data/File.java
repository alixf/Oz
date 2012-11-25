package oz.data;

/**
 * Instances of this class represent a file
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class File
{
	/**
	 * Return the name of the file
	 * 
	 * @return the name of the file
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * Set the name of the file
	 * 
	 * @param name a name for the file
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * Return the modification date of the file
	 * 
	 * @return the modification date of the file
	 */
	public long getMtime()
	{
		return m_mtime;
	}

	/**
	 * Set the modification date of the file
	 * 
	 * @param mtime an integer representing a modification date
	 */
	public void setMtime(long mtime)
	{
		m_mtime = mtime;
	}

	/**
	 * Return the content of the file
	 * 
	 * @return the content of the file
	 */
	public String getContent()
	{
		return m_content;
	}

	/**
	 * Set the content of the file
	 * 
	 * @param content a string representing the content of a file
	 */
	public void setContent(String content)
	{
		m_content = content;
	}

	/**
	 * Return the MIME type of the file
	 * 
	 * @return the MIME type of the file
	 */
	public String getMimeType()
	{
		return m_mimeType;
	}

	/**
	 * Set the MIME type of the file
	 * 
	 * @param mimeType a MIME type
	 */
	public void setMimeType(String mimeType)
	{
		m_mimeType = mimeType;
	}

	/**
	 * The name of the file
	 */
	private String	m_name;
	/**
	 * The modification date of the file
	 */
	private long	m_mtime;
	/**
	 * The MIME type of the file
	 */
	private String	m_mimeType;
	/**
	 * The content of the file
	 */
	private String	m_content;
}
