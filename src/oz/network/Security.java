package oz.network;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;

public class Security
{
	public Security() throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024, new SecureRandom());
		m_keyPair = keyGen.generateKeyPair();
		m_cipher = Cipher.getInstance("RSA");
		// By default we use UTF-8 encoding
		m_charset = "UTF-8";
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

	public static PublicKey convertEncodedPublicKey(byte[] encodedPublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException
	{
		return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedPublicKey));
		// we can also use PKCS8EncodedKeySpec
	}

	public static PublicKey convertBase64EncodedPublicKey(String base64EncodedPublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException
	{
		return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(base64EncodedPublicKey)));
	}

	public String encryptCommand(String plaintext, PublicKey publicKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException
	{
		// Cipher with instantiated algorithm and the public key.
		m_cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		// Convert the string into a byte array
		byte[] bytes = plaintext.getBytes(m_charset);

		byte[] encrypted = blockCipher(bytes, Cipher.ENCRYPT_MODE);

		String encryptedTranspherable = Base64.encodeBase64String(encrypted);
		// we can also use Hex encoding, but base64 encoding in more efficient.
		return new String(encryptedTranspherable);
	}

	public String decryptCommand(String encrypted) throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException
	{
		// Initialize cipher with instantiated algorithm and the private key to decrypt.
		m_cipher.init(Cipher.DECRYPT_MODE, m_keyPair.getPrivate());
		byte[] bts = Base64.decodeBase64(encrypted);

		byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);

		return new String(decrypted, m_charset);
	}

	private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException
	{
		// string initialize 2 buffers. scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];

		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

		// Another buffer : this one will hold the bytes that have to be modified in this step
		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++)
		{
			// if we filled our buffer array we have our block ready for
			// decryption or encryption
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

	KeyPair m_keyPair;
	Cipher m_cipher;
	String m_charset;
}