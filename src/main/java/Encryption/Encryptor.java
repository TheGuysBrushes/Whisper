/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

/**
 * Describe all methods 
 * @author flodavid
 */
public interface Encryptor {

    /**
     * Encrypt a message with a given key
     * @param message
     * @param key
     * @return 
     */
    public String encrypt(String message, PublicKey key);
}
