package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.encryption;

import java.util.Base64;

public class Encryptor {
  private static String salt = "r3m1UwAOAfK27rB5";

  public static String encodeMessage(String message) {
    String saltedMessage = salt + message;
    String encodedString = Base64.getEncoder().encodeToString(saltedMessage.getBytes());
    return encodedString;
  }

  public static String decodeMessage(String encodedMessage) {
    String decodedMessage = new String(Base64.getDecoder().decode(encodedMessage));
    return decodedMessage.substring(16);
  }
}
