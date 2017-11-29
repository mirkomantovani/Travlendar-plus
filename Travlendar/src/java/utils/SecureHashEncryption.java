/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Mirko
 */
public class SecureHashEncryption {
    public static String encryptPassword(String password) {
        String hashedPassword = null;
        try {
            MessageDigest d = MessageDigest.getInstance("SHA-256");
            byte[] hash = d.digest(password.getBytes("UTF-8"));
            BigInteger bigInt = new BigInteger(1, hash);
            hashedPassword = bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return hashedPassword;
    }
}
