package data;

public class Post
{
	public Post(String content, long date)
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

	private String	m_content;
	private long	m_date;
}