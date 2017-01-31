package Client;

import Encryption.*;
import MessageExchange.MessageDecryptorReceptor;
import MessageExchange.MessageEncryptorSender;

import java.net.InetAddress;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * Client that can connect to a server and send and receive messages
 * @author flodavid
 */
public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class);
    
    public PublicKey sharablePublicKey;
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

        sharablePublicKey = generator.generatePublicKey();
        LOGGER.info("Clé publique : "+sharablePublicKey);

        myPrivateKey = generator.generatePrivateKey();
        LOGGER.debug("Clé privée : "+ myPrivateKey);
    }
    
    /**
     * Receive key from other client
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void receiveKey() throws IOException, ClassNotFoundException {
        inS = new ObjectInputStream(socket.getInputStream());
        serverPublicKey = (PublicKey) inS.readObject();
    }
    
    /**
     * Send sharable key
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void sendKey() throws IOException, ClassNotFoundException {
        outS = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        outS.writeObject(sharablePublicKey);
        outS.flush();
    }

    /**
     * Initialize connection waiting for connection from another client
     * @param s_port : Port of connection
     * @throws IOException
     */
    public void initConnection(String s_port) throws IOException {
        
        generateKeys();
        
        int port = Integer.parseInt(s_port);
        
        ServerSocket serverSocket = new ServerSocket(port);
        
        socket= serverSocket.accept();
        
        try {
            receiveKey();
            sendKey();
        } catch (SocketException | UnknownHostException e) {
            LOGGER.error("SocketException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException", e);
        }
    }

    /**
     * Initialize connection with a distant or local server
     * @param address : IP address of the server
     * @param s_port : port used for connection
     * @throws IOException
     */
    public void initConnection(String address, String s_port) throws IOException {
        
        generateKeys();

        int port = Integer.parseInt(s_port);

        socket = new Socket(InetAddress.getByName(address), port);
        try {
            sendKey();
            receiveKey();
        } catch (SocketException | UnknownHostException e) {
            LOGGER.error("SocketException", e);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("IOException", e);
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
            LOGGER.error("Je n'ai pas recu la clé public du serveur");
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
            LOGGER.error("Je n'ai pas recu la clé public du serveur");
            return "";
        }

        try {
            // Réception de la réponse
            String response = (String) inS.readObject();
            String decryptedReponse = encryptor.decrypt(response, myPrivateKey);
            LOGGER.info("Réponse : " + decryptedReponse);

            return decryptedReponse;
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException " + e);
            return "";
        }
    }
    
    public void startReceiving() {

    }
    
    public void startChat() {
        
            // Lancement du Thread d'envoi de messages
            MessageEncryptorSender senderRunnable= new MessageEncryptorSender(serverPublicKey);
            senderRunnable.initConnection(socket);
            Thread msg_sender= new Thread(senderRunnable);
            msg_sender.start();

            // Lancement du Thread de réception de messages
            MessageDecryptorReceptor receptorRunnable= new MessageDecryptorReceptor(myPrivateKey);
            receptorRunnable.initConnection(socket);
            Thread msg_receptor= new Thread(receptorRunnable);
            msg_receptor.start();
        
            try {
                msg_sender.join();
                msg_receptor.join();
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException", e);
            }
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
        //address = "192.168.99.107";


        String s_port;
        if (args.length < 3) {
            s_port = "2000";
        } else {
            s_port = args[2];
        }

        Client client = new Client();

        try {
            client.initConnection(address, s_port);
            client.startChat();
            client.closeConnection();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
}
