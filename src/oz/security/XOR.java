package oz.security;

import java.nio.charset.Charset;

/**
 * This class provides XOR functions
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class XOR
{

	/** The charset. */
	private static Charset	m_charset	= Charset.forName("UTF-8");

	/**
	 * Decrypt.
	 * 
	 * @param bytes the byte array to be decrypted
	 * @param key the key
	 * @return the decrypted string
	 */
	public static String decrypt(byte[] bytes, String key)
	{
		byte[] keyBytes = key.getBytes(m_charset);

		for (int i = 0; i < bytes.length; ++i)
			bytes[i] ^= keyBytes[i % keyBytes.length];

		return new String(bytes, m_charset);
	}

	/**
	 * Encrypt.
	 * 
	 * @param string the string to be encrypted
	 * @param key the key
	 * @return the encrypted byte array
	 */
	public static byte[] encrypt(String string, String key)
	{
		byte[] stringBytes = string.getBytes(m_charset);
		byte[] keyBytes = key.getBytes(m_charset);

		for (int i = 0; i < stringBytes.length; ++i)
			stringBytes[i] ^= keyBytes[i % keyBytes.length];

		return stringBytes;
	}
}