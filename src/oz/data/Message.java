package oz.data;

public class Message
{
	public Message()
	{
	}

	public Message(String content, long date)
	{
		setContent(content);
		setDate(date);
	}

	public String getContent()
	{
		return m_content;
	}

	public void setContent(String content)
	{
		m_content = content;
	}

	public long getDate()
	{
		return m_date;
	}

	public void setDate(long date)
	{
		m_date = date;
	}

	public String getChannel()
	{
		return m_channel;
	}

	public void setChannel(String channel)
	{
		m_channel = channel;
	}

	private String	m_content;
	private long	m_date;
	private String	m_channel;
}
