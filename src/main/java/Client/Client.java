package Client;

import java.net.InetAddress;

import Encryption.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 * Client that can connect to a server and send and receive messages
 * @author flodavid
 */
public class Client {
    private final static Logger logger = Logger.getLogger(Client.class);
    
    public PublicKey SharablePublicKey;
    private PrivateKey myPrivateKey;
    
    private PublicKey serverPublicKey;
    
    private ObjectInputStream inS;
    private ObjectOutputStream outS;
    
    private Socket socket;

    Encryptor encryptor;
    
    public Client() {
        encryptor = new RSAEncryptor();
    }
    
    /**
     * Generate both public and private key
     */
    private void generateKeys() {
        KeyGenerator generator= new KeyGenerator();
        
        SharablePublicKey = generator.generatePublicKey();
        logger.info("Clé publique : "+SharablePublicKey);

        myPrivateKey = generator.generatePrivateKey();
        logger.debug("Clé privée : "+ myPrivateKey);        
    }
    
    /**
     * Initialize connection with a distant or local server
     * @param address : IP address of the server
     * @param s_port : port used for connection
     */
    public void initConnection(String address, String s_port) throws IOException{
        
        generateKeys();
        
        int port = Integer.parseInt(s_port);
        
        socket = new Socket(InetAddress.getByName(address), port);
        try {
            // Envoi clé
            outS = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outS.writeObject(SharablePublicKey);
            outS.flush();

            // Reception clé
            inS = new ObjectInputStream(socket.getInputStream());
            serverPublicKey = (PublicKey) inS.readObject();
            
        } catch (SocketException e) {
            logger.debug("SocketException", e);
        } catch (UnknownHostException e) {
            logger.debug("UnknownHostException", e);
        } catch (IOException e) {
            logger.debug("IOException", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ends the connection between
     * @throws IOException 
     */
    public void closeConnection() throws IOException{
        socket.close();
    }

    /**
     * Encrypt and send a message to the server
     * @param message : message to send
     * @throws IOException 
     */
    public void encryptSendMessage(String message) throws IOException {
                                
        if (serverPublicKey == null) {
            logger.error("Je n'ai pas recu la clé public du serveur");
            return;
        }
        
        // Envoi d'un message
        String messageCrypted = encryptor.encryptToString(message, serverPublicKey);

        outS.writeObject(messageCrypted);
        outS.flush();
    }

    /**
     * Receive and decrypt a message sent by the server
     * @return the received message sent by the server
     * @throws IOException 
     */
    public String receiveDecryptMessage() throws IOException {
                                
        if (myPrivateKey == null) {
            logger.error("Je n'ai pas recu la clé public du serveur");
            return "";
        }
        
        try {
            // Réception de la réponse
            String response = (String) inS.readObject();
            String decryptedReponse = encryptor.decrypt(response, myPrivateKey);
            logger.info("Réponse : " + decryptedReponse);
            
            return decryptedReponse;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Encrypts and sends a message, then receive and decrypt a message form the server
     * @param message : message to send
     * @throws IOException 
     */
    private void PingPong(String message) throws IOException {
        encryptSendMessage(message);
        String response= receiveDecryptMessage();
        
        logger.info("Réponse reçue : " + response);
    }

    /**
     * @param args : Parameters of the connection :
     *  "args[0]" is the address and "args[1]" the port
     */
    public static void main(String[] args) {
                        
        String message;
        if (args.length < 1) {
            message = "hello";
        } else {
            message = args[0];
        }
        
        String address;
        if (args.length < 2) {
            address = "localhost";
        } else {
            address = args[1];
        }
        address = "192.168.99.107";
        
        String s_port;
        if (args.length < 3) {
            s_port = "2000";
        } else {
            s_port = args[2];
        }

        Client client = new Client();
        try {
            client.initConnection(address, s_port);
        
            client.PingPong(message);
            client.PingPong(message + " (1)");
            client.PingPong(message + " (2)");
            client.PingPong(message + " (3)");
            
            client.closeConnection();
        } catch (IOException e) {
            logger.debug("IOException", e);
        }
        
    }
}
