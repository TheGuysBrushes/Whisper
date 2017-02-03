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
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author Florian DAVID
 */
public class MessageEncryptorSender extends MessageSender {
    private final static Logger LOGGER = Logger.getLogger(MessageEncryptorSender.class);
   
    final private Encryptor encryptor;
    private PublicKey otherPublicKey;

    public MessageEncryptorSender() {
        encryptor = new RSAEncryptor();
    }
    
    public void setPublicKey(PublicKey publicKey) {
        otherPublicKey= publicKey;
    }
    
    /**
     * Encrypt and send a message to the server
     * @param message : message to send
     * @throws IOException
     */
    @Override
    public void sendMessage(String message) throws IOException {

        if (otherPublicKey == null) {
            LOGGER.error("Je n'ai pas recu la clé public du serveur");
            return;
        }
        
        // Envoi d'un message
        String messageCrypted = encryptor.encryptToString(message, otherPublicKey);
        super.sendMessage(messageCrypted);
    }
    
    /**
     * Send a public key to another client
     * @param publicKey : sharable public key to send
     * @param socket : Socket on which the key will be send
     * @throws IOException
     */
    public void sendKey(PublicKey publicKey, Socket socket) throws IOException {
        super.initConnection(socket);
        getOutS().writeObject(publicKey);
        getOutS().flush();
    }
    
}
