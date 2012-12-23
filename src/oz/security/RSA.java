package oz.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import oz.tools.Operations;

/**
 * This class provides RSA functions
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class RSA
{

	/**
	 * Convert base64 encoded public key.
	 * 
	 * @param base64EncodedPublicKey the base64 encoded public key
	 * @return the public key
	 */
	public static PublicKey convertBase64EncodedPublicKey(String base64EncodedPublicKey)
	{
		return convertEncodedPublicKey(Base64.decodeBase64(base64EncodedPublicKey));
	}

	/**
	 * Convert encoded public key.
	 * 
	 * @param encodedPublicKey the encoded public key
	 * @return the public key
	 */
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

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws Throwable the throwable
	 */
	public static void main(String[] args) throws Throwable
	{
		RSA sec = new RSA();

		String command = "Hello world";

		String publicKeyString = sec.getBase64EncodedPublicKey();
		System.out.println(publicKeyString);
		PublicKey publicKey = RSA.convertBase64EncodedPublicKey(publicKeyString);

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

	/** The charset. */
	String	m_charset;

	/** The cipher. */
	Cipher	m_cipher;

	/** The key pair. */
	KeyPair	m_keyPair;

	/**
	 * Instantiates a new rsa.
	 */
	public RSA()
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
	 * Block cipher.
	 * 
	 * @param bytes the bytes
	 * @param mode the mode
	 * @return the byte[]
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 */
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
				toReturn = Operations.mergeByteBuffers(toReturn, scrambled);
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
		toReturn = Operations.mergeByteBuffers(toReturn, scrambled);

		return toReturn;
	}

	/**
	 * Decrypt command.
	 * 
	 * @param encrypted the encrypted
	 * @return the string
	 */
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

	/**
	 * Encrypt command.
	 * 
	 * @param commandString the command string
	 * @param publicKey the public key
	 * @return the string
	 */
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

	/**
	 * Gets the base64 encoded public key.
	 * 
	 * @return the base64 encoded public key
	 */
	public String getBase64EncodedPublicKey()
	{
		return Base64.encodeBase64String(m_keyPair.getPublic().getEncoded());
	}

	/**
	 * Gets the charset.
	 * 
	 * @return the m_charset
	 */
	public String getCharset()
	{
		return m_charset;
	}

	/**
	 * Gets the encoded public key.
	 * 
	 * @return the encoded public key
	 */
	public byte[] getEncodedPublicKey()
	{
		return m_keyPair.getPublic().getEncoded();
	}

	/**
	 * Sets the charset.
	 * 
	 * @param charsetName the new charset
	 */
	public void setCharset(String charsetName)
	{
		m_charset = charsetName;
	}
}
