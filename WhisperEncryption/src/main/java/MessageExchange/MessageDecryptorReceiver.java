/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageExchange;

import Encryption.Encryptor;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 *
 * @author Florian DAVID
 */
public class MessageDecryptorReceiver extends MessageReceiver {
    private final static Logger LOGGER = Logger.getLogger(MessageDecryptorReceiver.class);
    
    private PrivateKey myPrivateKey;
    final private Encryptor encryptor;
    
    public MessageDecryptorReceiver(MessageDisplayer msgDisplayer) {
        super(msgDisplayer);
        encryptor = new RSAEncryptor();
    }
    
    public void setPrivateKey(PrivateKey privateKey) {
        myPrivateKey= privateKey;
    }
    
    /**
     * Receive and decrypt a message sent by the server
     * @return the received message sent by the server
     * @throws IOException
     */
    @Override
    public String receiveMessage() throws IOException {

        if (myPrivateKey == null) {
            LOGGER.error("Je n'ai pas recu la clé public du serveur");
            return "";
        }

        // Réception de la réponse
        String response = super.receiveMessage();
        String decryptedReponse = encryptor.decrypt(response, myPrivateKey);

        return decryptedReponse;
    }
    
            
    /**
     * Receive a key sent by another client
     * @param socket : Socket from where the key are received and messages will
     * be sent and received
     * @return the received key
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public PublicKey receiveKey(Socket socket) throws IOException, ClassNotFoundException {
    
//        try {
            super.initConnection(socket);
            // Réception de la clé
            return (PublicKey) getInS().readObject();
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("Exception : "+ e);
//            return new PublicKey(BigInteger.ONE, BigInteger.ONE);
//        }
    }
}
