package oz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.MappedByteBuffer;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Settings
{
	public Settings()
	{
	}

	public Settings(String file) throws IOException
	{
		load(file);
	}

	public void load(String file) throws IOException
	{
		new JSONDeserializer<Settings>().deserializeInto(readFile(file), this);
	}

	public void save(String file) throws IOException
	{
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class");
		serializer.include("*");
		writeFile(file, serializer.serialize(this));
	}

	private String readFile(String file) throws IOException
	{
		FileInputStream stream = new FileInputStream(new File(file));
		try
		{
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally
		{
			stream.close();
		}
	}

	private void writeFile(String file, String content) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(Charset.defaultCharset().encode(content).array());
		fos.close();
	}

	public int getNetworkPort()
	{
		return m_networkPort;
	}

	public void setNetworkPort(int networkPort)
	{
		m_networkPort = networkPort;
	}

	public String getTrackerAddress()
	{
		return m_trackerAddress;
	}

	public void setTrackerAddress(String trackerAddress)
	{
		m_trackerAddress = trackerAddress;
	}

	public int getTrackerPort()
	{
		return m_trackerPort;
	}

	public void setTrackerPort(int trackerPort)
	{
		m_trackerPort = trackerPort;
	}

	private int			m_networkPort;
	private String		m_trackerAddress;
	private int			m_trackerPort;
}
