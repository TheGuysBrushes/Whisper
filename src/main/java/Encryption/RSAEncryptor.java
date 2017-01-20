/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import org.apache.log4j.Logger;
import java.math.BigInteger;

import java.math.BigInteger;

/**
 * @author flodavid
 */
public class RSAEncryptor implements Encryptor {
    private final static Logger logger = Logger.getLogger(RSAEncryptor.class);

    private Integer r, u, v, n;

    /**
     * Encrypt a message with a given key
     *
     * @param message
     * @param key
     * @return
     */
    @Override
    public BigInteger[] encrypt(String message, PublicKey key) {
        byte[] ASCIIMessage= toASCII(message);

        BigInteger encrypted_message[]= new BigInteger[ASCIIMessage.length];

        for (int i= 0; i < ASCIIMessage.length; ++i) {
            BigInteger ASCIIChar= BigInteger.valueOf((long)(ASCIIMessage[i]));
            encrypted_message[i]= (ASCIIChar.modPow(key.get_e(), key.get_n()) );
        }
        
        return encrypted_message;
    }

    /**
     * Decrypt a message with a given key
     *
     * @param cryptedMessage
     * @param key
     * @return
     */
    @Override
    public String decrypt(int[] cryptedMessage, PrivateKey key) {
        String decryptedMessage;
        int[] decryptedBytes = new int[cryptedMessage.length];

        for (int i = 0; i < cryptedMessage.length; i++) {
            BigInteger charCrypted = BigInteger.valueOf(cryptedMessage[i]);
            BigInteger charDecrypted = charCrypted.modPow(BigInteger.valueOf(u), BigInteger.valueOf(n));
            decryptedBytes[i] = charDecrypted.intValue();
        }

        decryptedMessage = fromASCII(decryptedBytes);

        return decryptedMessage;
    }

    /**
     * @param message
     * @return
     */
    public byte[] toASCII(String message) {
                
        byte encrypted_message[]= new byte[message.length()];
        
        for (int i= 0; i < message.length(); ++i) {
            encrypted_message[i]= (byte)(message.charAt(i));
        }

        String ascii_message = "" + encrypted_message[0];
        for (int i = 1; i < message.length(); ++i) {
            ascii_message += "; " + encrypted_message[i];
        }
        logger.info("message to ASCII : " + ascii_message);

        logger.info("Encrypted message : " + encrypted_message);
        return encrypted_message;
    }

    /**
     * @param encryptedMessage
     * @return
     */
    public String fromASCII(int[] encryptedMessage) {
        String decryptedMessage = "";

        for (int charInt : encryptedMessage) {
            decryptedMessage += (char) charInt;
        }

        return decryptedMessage;
    }
}
