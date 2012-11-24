package oz.network;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;

import org.apache.commons.codec.binary.Base64;

public class Security
{
	public Security()
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024, new SecureRandom());
			m_keyPair = keyGen.generateKeyPair();
			m_cipher = Cipher.getInstance("RSA");
			// By default we use UTF-8 encoding
			m_charset = "UTF-8";
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the m_charset
	 */
	public String getCharset()
	{
		return m_charset;
	}

	/**
	 * @param charset
	 *            the charsetName to set
	 */
	public void setCharset(String charsetName)
	{
		m_charset = charsetName;
	}

	public byte[] getEncodedPublicKey()
	{
		return m_keyPair.getPublic().getEncoded();
	}

	public String getBase64EncodedPublicKey()
	{
		return Base64.encodeBase64String(m_keyPair.getPublic().getEncoded());
	}

	public static PublicKey convertEncodedPublicKey(byte[] encodedPublicKey)
	{
		try
		{
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedPublicKey));
			// we can also use PKCS8EncodedKeySpec
		}
		catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static PublicKey convertBase64EncodedPublicKey(String base64EncodedPublicKey)
	{
		return convertEncodedPublicKey(Base64.decodeBase64(base64EncodedPublicKey));
	}

	public String encryptCommand(String commandString, PublicKey publicKey)
	{
		String encryptedString = null;
		try
		{
			// Cipher with instantiated algorithm and the public key.
			m_cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			// Convert the string into a byte array
			byte[] bytes = commandString.getBytes(m_charset);

			byte[] encrypted = blockCipher(bytes, Cipher.ENCRYPT_MODE);

			encryptedString = Base64.encodeBase64String(encrypted);
			// we can also use Hex encoding, but base64 encoding in more efficient.
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return encryptedString;
	}

	public String decryptCommand(String encrypted)
	{
		String commandString = null;
		try
		{
			// Initialize cipher with instantiated algorithm and the private key to decrypt.
			m_cipher.init(Cipher.DECRYPT_MODE, m_keyPair.getPrivate());
			byte[] bts = Base64.decodeBase64(encrypted);
			byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);
			commandString = new String(decrypted, m_charset).trim();
		}
		catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		}
		catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		return commandString;
	}

	private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException
	{
		// string initialize 2 buffers. scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];

		// if we encrypt we use 100 byte long blocks. Decryption requires 128
		// byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

		// Another buffer : this one will hold the bytes that have to be modified in this step
		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++)
		{
			// if we filled our buffer array we have our block ready for decryption or encryption
			if ((i > 0) && (i % length == 0))
			{
				// execute the operation
				scrambled = m_cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn, scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if new length would be longer than remaining bytes in the byte array we shorten it.
				if (i + length > bytes.length)
				{
					newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i % length] = bytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
		scrambled = m_cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn, scrambled);

		return toReturn;
	}

	private byte[] append(byte[] prefix, byte[] suffix)
	{
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++)
			toReturn[i] = prefix[i];
		for (int i = 0; i < suffix.length; i++)
			toReturn[i + prefix.length] = suffix[i];
		return toReturn;
	}

	public static void main(String[] args) throws Throwable
	{
		Security sec = new Security();

		String command = "Hello world";

		String publicKeyString = sec.getBase64EncodedPublicKey();
		PublicKey publicKey = Security.convertBase64EncodedPublicKey(publicKeyString);

		String encrytedCommand = sec.encryptCommand(command, publicKey);

		// Sending

		String decryptedCommand = sec.decryptCommand(encrytedCommand);

		System.out.println(decryptedCommand.equals(command));
		if (!decryptedCommand.equals(command))
		{
			System.out.println(decryptedCommand.length());
			System.out.println(command.length());
		}
	}

	KeyPair	m_keyPair;
	Cipher	m_cipher;
	String	m_charset;
}