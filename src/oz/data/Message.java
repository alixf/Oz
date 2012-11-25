package oz.data;

/**
 * Instances of this class represent a message
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Message
{
	/**
	 * Default constructor
	 */
	public Message()
	{
	}

	/**
	 * Create a message from a content and a date
	 * 
	 * @param content the content of the message
	 * @param date the date of the message
	 */
	public Message(String content, long date)
	{
		setContent(content);
		setDate(date);
	}

	/**
	 * Return the content of the message
	 * 
	 * @return the content of the message
	 */
	public String getContent()
	{
		return m_content;
	}

	/**
	 * Set the content of the message
	 * 
	 * @param content a string representing the content of a message
	 */
	public void setContent(String content)
	{
		m_content = content;
	}

	/**
	 * Return the date of the message
	 * 
	 * @return the date of the message
	 */
	public long getDate()
	{
		return m_date;
	}

	/**
	 * Set the date of the message
	 * 
	 * @param date a integer representing a date
	 */
	public void setDate(long date)
	{
		m_date = date;
	}

	/**
	 * Return the channel's id of the message
	 * 
	 * @return the channel's if of the message
	 */
	public String getChannelID()
	{
		return m_channelID;
	}

	/**
	 * Set the channel's if of the message
	 * 
	 * @param channel a channel's id
	 */
	public void setChannelID(String channel)
	{
		m_channelID = channel;
	}

	/**
	 * The content of the message
	 */
	private String	m_content;
	/**
	 * The date of the message
	 */
	private long	m_date;
	/**
	 * The channel's id of the message
	 */
	private String	m_channelID;
}
