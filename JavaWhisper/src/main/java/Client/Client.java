package Client;

import Encryption.*;
import MessageExchange.*;

import java.net.InetAddress;
import java.io.IOException;
import static java.lang.Thread.sleep;
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
    
    private Socket socket;
    
    final private MessageEncryptorSender sender;
    final private MessageDecryptorReceiver receiver;
    
    final private MessageWriter msg_sender;
    private Thread msg_receiver;

    public Client(boolean hasGUI, boolean chatStarted) {
        sender= new MessageEncryptorSender();
        
        MessageDisplayer displayer;
        if (hasGUI) {
            ClientGUI GUI= new ClientGUI(chatStarted);
            GUI.setMessageSender(sender);
            GUI.setVisible(true);
            displayer= GUI;
            msg_sender= GUI;
        } else {
            displayer= new TermDisplayer();
            msg_sender= new TermWriter();
        }
        
        receiver= new MessageDecryptorReceiver(displayer);
    }

    public MessageEncryptorSender getSender() {
        return sender;
    }

    public MessageDecryptorReceiver getReceiver() {
        return receiver;
    }
    
    /**
     * Generate both public and private key
     */
    private void generateKeys() {
        KeyGenerator generator= new KeyGenerator();

        sharablePublicKey = generator.generatePublicKey();
        LOGGER.info("Clé publique : "+sharablePublicKey);

        PrivateKey myPrivateKey = generator.generatePrivateKey();
        LOGGER.debug("Clé privée : "+ myPrivateKey);
        receiver.setPrivateKey(myPrivateKey);
    }
    
    /**
     * Receive key from other client
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void receiveKey() throws IOException, ClassNotFoundException {
        sender.setPublicKey(receiver.receiveKey(socket));
    }
    
    /**
     * Send sharable key
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void sendKey() throws IOException, ClassNotFoundException {
        sender.sendKey(sharablePublicKey, socket);
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
            LOGGER.info("Key exchanged");
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
        
        int timeout= 25;
        
        while (timeout > 0){
            try {
                socket = new Socket(InetAddress.getByName(address), port);
                try {
                    sendKey();
                    receiveKey();
                    timeout= 0;
                } catch (SocketException | UnknownHostException e) {
                    LOGGER.error("SocketException", e);
                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.error("IOException", e);
                }
            } catch (IOException ioe) {
                if (timeout > 0) {
                    timeout--;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ie) {
                        LOGGER.error(ie.getMessage());
                    }
                }
                else throw ioe;
            }
        }
    }
    
    /**
     * Ends the connection between
     * @throws IOException
     */
    public void closeConnection() throws IOException{
        socket.close();
    }
    
    public void startChat() {
        msg_sender.startSending();
        
        // Lancement du Thread de réception de messages
        msg_receiver= new Thread(receiver);
        msg_receiver.start();

    }
    
    public void stopChat() {
        try {
            msg_sender.stopSending();
            msg_receiver.join();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
    }

    /**
     * @param args : Parameters of the connection :
     *  "args[0]" is the address and "args[1]" the port
     */
    public static void main(String[] args) {

        String address;
        if (args.length < 1) {
            address = "localhost";
        } else {
            address = args[0];
        }
        //address = "192.168.99.107";

        String s_port;
        if (args.length < 2) {
            s_port = "2000";
        } else {
            s_port = args[1];
        }
        
        boolean has_GUI= true;
        
        Client client;
        ConnectionAsker asker= new ConnectionAsker();
                
        try {
            client= asker.getAConnectedClient(s_port);
            client.startChat();
//            client.stopChat();
//            client.closeConnection();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
}
