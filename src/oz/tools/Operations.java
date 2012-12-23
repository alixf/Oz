package oz.tools;

/**
 * This class provides function to manipulate data structures
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Operations
{

	/**
	 * Merge two byte arrays.
	 * 
	 * @param prefix the prefix
	 * @param suffix the suffix
	 * @return the resulting byte array
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
	 * @param string the string to be trimmed
	 * @param toTrim the character which will be trimmed
	 * @return the trimmed string
	 */
	public static String trimString(String string, char toTrim)
	{
		int originalLen = string.length();
		int len = string.length();
		int st = 0;
		while ((st < len) && (string.charAt(st) == toTrim))
			st++;
		while ((st < len) && (string.charAt(len - 1) == toTrim))
			len--;
		return ((st > 0) || (len < originalLen)) ? string.substring(st, len) : string.toString();
	}
}
