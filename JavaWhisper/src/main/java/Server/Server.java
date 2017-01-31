package Server;

/**
 * @author flodavid
 */

import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;
import MessageExchange.MessageDecryptorReceptor;
import MessageExchange.MessageEncryptorSender;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class);
        
    public PublicKey sharablePublicKey;
    private PrivateKey myPrivateKey;
    
    private PublicKey clientPublicKey;
    
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
    

    public static void main(String[] args) {

        String s_port;
        if (args.length < 1) {
            s_port = "2000";
        } 
        else {
            s_port = args[0];
        }

        // créer serveur
        int port = Integer.parseInt(s_port);

        Server server = new Server();
        
        LOGGER.info("Clé privée : " + server.myPrivateKey);
        RSAEncryptor encryptor = new RSAEncryptor();
        server.generateKeys();

        try (ServerSocket socket = new ServerSocket(port)) {

            /*** Echange de clés ***/
            // Réception de la clé du client
            Socket sock;
            sock = socket.accept();
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));
            server.clientPublicKey = (PublicKey) is.readObject();
            LOGGER.info("Client's public key received.");

            // Envoi de la clé partageable
            ObjectOutputStream os = new ObjectOutputStream(sock.getOutputStream());
            os.writeObject(server.sharablePublicKey);
            LOGGER.info("Server's public key sent.");
            os.flush();

            
            // Lancement du Thread de réception de messages
            MessageDecryptorReceptor receptorRunnable= new MessageDecryptorReceptor(server.myPrivateKey);
           // receptorRunnable.initConnection(sock);
            Thread msg_receptor= new Thread(receptorRunnable);
            msg_receptor.start();
                        
            // Lancement du Thread d'envoi de messages
            MessageEncryptorSender senderRunnable= new MessageEncryptorSender(server.clientPublicKey);
            //senderRunnable.initConnection(sock);
            Thread msg_sender= new Thread(senderRunnable);
            msg_sender.start();

        } catch (SocketException e) {
            LOGGER.debug("SocketException", e);
        } catch (IOException e) {
            LOGGER.debug("IOException", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
