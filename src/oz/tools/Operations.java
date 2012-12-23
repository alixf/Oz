package oz.tools;

// TODO: Auto-generated Javadoc
/**
 * The Class Operations.
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Operations
{

	/**
	 * Merge byte buffers.
	 * 
	 * @param prefix the prefix
	 * @param suffix the suffix
	 * @return the byte[]
	 */
	public static byte[] mergeByteBuffers(byte[] prefix, byte[] suffix)
	{
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++)
			toReturn[i] = prefix[i];
		for (int i = 0; i < suffix.length; i++)
			toReturn[i + prefix.length] = suffix[i];
		return toReturn;
	}

	/**
	 * Trim string.
	 * 
	 * @param s the s
	 * @param toTrim the to trim
	 * @return the string
	 */
	public static String trimString(String s, char toTrim)
	{
		int originalLen = s.length();
		int len = s.length();
		int st = 0;
		while ((st < len) && (s.charAt(st) == toTrim))
			st++;
		while ((st < len) && (s.charAt(len - 1) == toTrim))
			len--;
		return ((st > 0) || (len < originalLen)) ? s.substring(st, len) : s.toString();
	}
}
