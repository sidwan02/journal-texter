package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
  private static String salt = "r3m1UwAOAfK27rB5";
  private static byte[] keyBytes = "Th3Encrypt10nK3y".getBytes(); //Must be length 16

  public static String encodeMessage(String message) {
    String saltedMessage = salt + message;
    String encodedString = Base64.getEncoder().encodeToString(saltedMessage.getBytes());
    return encodedString;
  }

  public static String decodeMessage(String encodedMessage) {
    String decodedMessage = new String(Base64.getDecoder().decode(encodedMessage));
    return decodedMessage.substring(16);
  }

  public static byte[] encryptMessage(String message)
      throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
      BadPaddingException, IllegalBlockSizeException {

    byte[] messageBytes = message.getBytes();
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    return cipher.doFinal(messageBytes);
  }

  public static String decryptMessage(byte[] encryptedMessage)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return new String(cipher.doFinal(encryptedMessage));
  }
}
