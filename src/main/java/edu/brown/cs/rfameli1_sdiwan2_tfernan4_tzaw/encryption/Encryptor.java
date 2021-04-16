package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypts data using a static key.
 */
public final class Encryptor {
  /**
   * Private constructor for utility class.
   */
  private Encryptor() { }

  private static final byte[] KEY_BYTES = "Th3Encrypt10nK3y".getBytes(); //Must be length 16

  /**
   * Encrypts a string into a byte array using a static key.
   *
   * @param message The message to encrypt.
   * @return The encrypted byte array.
   * @throws InvalidKeyException       If the key is an invalid length.
   * @throws NoSuchPaddingException    If a mechanism is requested but not available in the
   * environment.
   * @throws NoSuchAlgorithmException  If the requested algorithm does not exist.
   * @throws BadPaddingException       If the mechanism is not valid.
   * @throws IllegalBlockSizeException Thrown when the length of the data to the block cipher
   * is incorrect.
   */
  public static byte[] encryptMessage(String message)
      throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
      BadPaddingException, IllegalBlockSizeException {

    byte[] messageBytes = message.getBytes();
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKey secretKey = new SecretKeySpec(KEY_BYTES, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    return cipher.doFinal(messageBytes);
  }

  /**
   * Decrypts the users data from a byte array to a string.
   *
   * @param encryptedMessage The byte array to decrypt using the key.
   * @return The decrypted message as a string.
   * @throws InvalidKeyException       If the key is an invalid length.
   * @throws NoSuchPaddingException    If a mechanism is requested but not available in the
   * environment.
   * @throws NoSuchAlgorithmException  If the requested algorithm does not exist.
   * @throws BadPaddingException       If the mechanism is not valid.
   * @throws IllegalBlockSizeException Thrown when the length of the data to the block cipher
   * is incorrect.
   */
  public static String decryptMessage(byte[] encryptedMessage)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKey secretKey = new SecretKeySpec(KEY_BYTES, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return new String(cipher.doFinal(encryptedMessage));
  }
}
