package Server;

/**
 * @author flodavid
 */

import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.*;

public class Server {
    private final static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {
        //testASCII();

        String s_port;
        if (args.length < 1) {
            s_port = "2000";
        } else {
            s_port = args[0];
        }

        // créer serveur
        int port = Integer.parseInt(s_port);
        boolean continuer = true;

        KeyGenerator generator = new KeyGenerator();
        generator.initParameters();
        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : " + publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.info("Clé privée : " + privateKey);
        RSAEncryptor encryptor = new RSAEncryptor();


        try (ServerSocket socket = new ServerSocket(port)) {

            // Echange de clé
            Socket sock;
            sock = socket.accept();
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));
            PublicKey clientKey = (PublicKey) is.readObject();
            logger.info("Client's public key received.");

            ObjectOutputStream os = new ObjectOutputStream(sock.getOutputStream());
            os.writeObject(publicKey);
            logger.info("Server's public key sent.");
            os.flush();

            while (true) {

                //Reception

                String messageCrypte = (String) is.readObject();
                // décryptage du message recu
                String decryptedMSG = encryptor.decrypt(messageCrypte, privateKey);
                logger.info("Message décrypté : " + decryptedMSG);


                // préparation de la réponse
                String reponse = "Roger " + decryptedMSG;
                BigInteger[] encryptedHello = encryptor.encrypt(reponse, clientKey);
                String reponseCrypted = encryptedHello[0].toString();
                for (int i = 1; i < encryptedHello.length; i++) {
                    reponseCrypted += "%" + encryptedHello[i];
                }
                logger.info("Réponse crypté: " + reponseCrypted);


                //Envoi de la réponse cryptée
                os.writeObject(reponseCrypted);
                os.flush();


            }
        } catch (SocketException e) {
            logger.debug("SocketException", e);
        } catch (IOException e) {
            logger.debug("IOException", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
