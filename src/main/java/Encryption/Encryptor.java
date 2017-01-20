/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.math.BigInteger;

/**
 * Describe all methods 
 * @author flodavid
 */
public interface Encryptor {

    /**
     * Encrypt a message with a given key
     * @param ASCIIMessage
     * @param key
     * @return 
     */
    public BigInteger[] encrypt(String ASCIIMessage, PublicKey key);

    /**
     * Decrypt a message with a given key
     * @param cryptedMessage
     * @param key
     * @return
     */
    public String decrypt(int[] cryptedMessage, PrivateKey key);
}
