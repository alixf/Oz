package oz.data;

public class File
{
	public String getName()
	{
		return m_name;
	}
	public void setName(String name)
	{
		m_name = name;
	}
	public long getMtime()
	{
		return m_mtime;
	}
	public void setMtime(long mtime)
	{
		m_mtime = mtime;
	}
	public String getContent()
	{
		return m_content;
	}
	public void setContent(String content)
	{
		m_content = content;
	}
	public String getMimeType()
	{
		return m_mimeType;
	}
	public void setMimeType(String mimeType)
	{
		m_mimeType = mimeType;
	}
	
	private String m_name;
	private long m_mtime;
	private String m_mimeType;

	private String m_content;
}
