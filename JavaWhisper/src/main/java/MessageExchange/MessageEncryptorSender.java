/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import Encryption.Encryptor;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;

import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Work
 */
public class MessageEncryptorSender extends MessageSender {
    private final static Logger LOGGER = Logger.getLogger(MessageEncryptorSender.class);
   
    final private PublicKey serverPublicKey;
    final private Encryptor encryptor;

    
    public MessageEncryptorSender(PublicKey publicKey) {
        serverPublicKey= publicKey;
        encryptor = new RSAEncryptor();
    }
    
    /**
     * Encrypt and send a message to the server
     * @param message : message to send
     * @throws IOException
     */
    @Override
    public void sendMessage(String message) throws IOException {

        if (serverPublicKey == null) {
            LOGGER.error("Je n'ai pas recu la clé public du serveur");
            return;
        }
        
        // Envoi d'un message
        String messageCrypted = encryptor.encryptToString(message, serverPublicKey);
        super.sendMessage(messageCrypted);
    }
    
}
