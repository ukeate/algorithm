package base.hash;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Hash {
    private MessageDigest hash;
    public Hash(String algorithm) {
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String hashCode(String input) {
        return DatatypeConverter.printHexBinary(hash.digest(input.getBytes())).toUpperCase();
    }

    public static void main(String[] args) {
        for (String s : Security.getAlgorithms("MessageDigest")) {
            System.out.println(s);
        }
        System.out.println("=====");
        String algorithm = "MD5";
        Hash hash = new Hash(algorithm);
        System.out.println(hash.hashCode("abc"));
    }
}
