package notservlets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassEncrypt {
    public String Encrypt(String password) throws NoSuchAlgorithmException {
        // create an empty string, string builder and message digest ready to be hashed
        String hashpss;
        StringBuilder sb = new StringBuilder();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        // get bytes of inputted password and hash
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        // convert hashed password to string and return to be stored
        hashpss = sb.toString();
        return hashpss;
    }
}
