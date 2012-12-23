package oz.data;

import flexjson.JSON;

/**
 * Instances of this class represent a file.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class File
{

	/** The content of the file. */
	private String	m_content;

	/** The MIME type of the file. */
	private String	m_mimeType;

	/** The modification date of the file. */
	private long	m_mtime;

	/** The name of the file. */
	private String	m_name;

	/**
	 * Return the content of the file.
	 * 
	 * @return the content of the file
	 */
	@JSON
	public String getContent()
	{
		return m_content;
	}

	/**
	 * Return the MIME type of the file.
	 * 
	 * @return the MIME type of the file
	 */
	@JSON
	public String getMimeType()
	{
		return m_mimeType;
	}

	/**
	 * Return the modification date of the file.
	 * 
	 * @return the modification date of the file
	 */
	@JSON
	public long getMtime()
	{
		return m_mtime;
	}

	/**
	 * Return the name of the file.
	 * 
	 * @return the name of the file
	 */
	@JSON
	public String getName()
	{
		return m_name;
	}

	/**
	 * Set the content of the file.
	 * 
	 * @param content a string representing the content of a file
	 */
	public void setContent(String content)
	{
		m_content = content;
	}

	/**
	 * Set the MIME type of the file.
	 * 
	 * @param mimeType a MIME type
	 */
	public void setMimeType(String mimeType)
	{
		m_mimeType = mimeType;
	}

	/**
	 * Set the modification date of the file.
	 * 
	 * @param mtime an integer representing a modification date
	 */
	public void setMtime(long mtime)
	{
		m_mtime = mtime;
	}

	/**
	 * Set the name of the file.
	 * 
	 * @param name a name for the file
	 */
	public void setName(String name)
	{
		m_name = name;
	}
}
