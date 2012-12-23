package oz.security;

import java.nio.charset.Charset;

public class XOR
{
	private static Charset	m_charset	= Charset.forName("UTF-8");

	public static byte[] encrypt(String string, String key)
	{
		byte[] stringBytes = string.getBytes(m_charset);
		byte[] keyBytes = key.getBytes(m_charset);

		for (int i = 0; i < stringBytes.length; ++i)
			stringBytes[i] ^= keyBytes[i % keyBytes.length];

		return stringBytes;
	}

	public static String decrypt(byte[] bytes, String key)
	{
		byte[] keyBytes = key.getBytes(m_charset);

		for (int i = 0; i < bytes.length; ++i)
			bytes[i] ^= keyBytes[i % keyBytes.length];

		return new String(bytes, m_charset);
	}
}