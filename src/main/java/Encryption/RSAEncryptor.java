/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import org.apache.log4j.Logger;

import java.math.BigInteger;

/**
 * @author flodavid
 */
public class RSAEncryptor implements Encryptor {
    private final static Logger logger = Logger.getLogger(RSAEncryptor.class);

//    private BigInteger r, u, v, n;

//    public RSAEncryptor(BigInteger u, BigInteger n){

//    }

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

        String encrypted_msg = "";
        for (int i= 0; i < ASCIIMessage.length; ++i) {
            BigInteger ASCIIChar= BigInteger.valueOf((long)(ASCIIMessage[i]));
            encrypted_message[i]= (ASCIIChar.modPow(key.get_e(), key.get_n()) );            
            encrypted_msg+= encrypted_message[i].toString() + ";";
        }
        
        logger.info("Message encrypté : "+ encrypted_msg);

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
    public String decrypt(BigInteger[] cryptedMessage, PrivateKey key) {
        String decryptedMessage;
        byte[] decryptedBytes = new byte[cryptedMessage.length];

        for (int i = 0; i < cryptedMessage.length; i++) {
            BigInteger charCrypted = cryptedMessage[i];
            BigInteger charDecrypted = charCrypted.modPow(key.getU(), key.getN());
            logger.debug(charCrypted + " decrypted to "+charDecrypted);
            decryptedBytes[i] = charDecrypted.byteValue();
        }

        decryptedMessage = fromASCII(decryptedBytes);

        return decryptedMessage;
    }

    /**
     * Decrypt a message with a given key
     *
     * @param cryptedMessage
     * @param key
     * @return
     */
    public String decrypt(String cryptedMessage, PrivateKey key) {
        String[] tabChar = cryptedMessage.split("%");

        BigInteger[] tab = new BigInteger[tabChar.length];
        for (int i=0; i<tabChar.length; i++) {
            tab[i] = new BigInteger(tabChar[i]);
        }

        return decrypt(tab,key);
    }

    /**
     * @param message
     * @return
     */
    public byte[] toASCII(String message) {

        byte encrypted_message[] = new byte[message.length()];

        for (int i = 0; i < message.length(); ++i) {
            encrypted_message[i] = (byte) (message.charAt(i));
        }

        String ascii_message = "" + encrypted_message[0];
        for (int i = 1; i < message.length(); ++i) {
            ascii_message += "; " + encrypted_message[i];
        }
        logger.debug("message to ASCII : " + ascii_message);

//        logger.info("Encrypted message : " + encrypted_message);
        return encrypted_message;
    }

    /**
     * @param encryptedMessage
     * @return
     */
    public String fromASCII(byte[] encryptedMessage) {
        String decryptedMessage = "";

        for (byte charByte : encryptedMessage) {
            logger.debug("byte ASCII : "+ charByte);
            decryptedMessage += (char) charByte;
        }

        return decryptedMessage;
    }
}
