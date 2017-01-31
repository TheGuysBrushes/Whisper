/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import Encryption.Encryptor;
import Encryption.PrivateKey;
import Encryption.RSAEncryptor;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 *
 * @author Florian DAVID
 */
public class MessageDecryptorReceptor extends MessageReceptor {
    private final static Logger LOGGER = Logger.getLogger(MessageDecryptorReceptor.class);
    
    final private PrivateKey myPrivateKey;
    final private Encryptor encryptor;
    
    public MessageDecryptorReceptor(PrivateKey privateKey) {
        myPrivateKey= privateKey;
        encryptor = new RSAEncryptor();
    }
    
    /**
     * Receive and decrypt a message sent by the server
     * @return the received message sent by the server
     * @throws IOException
     */
    @Override
    protected String receiveMessage() throws IOException {

        if (myPrivateKey == null) {
            LOGGER.error("Je n'ai pas recu la clé public du serveur");
            return "";
        }

        // Réception de la réponse
        String response = super.receiveMessage();
        String decryptedReponse = encryptor.decrypt(response, myPrivateKey);

        return decryptedReponse;
    }
}
