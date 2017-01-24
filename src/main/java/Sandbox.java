import Encryption.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.*;

/**
 * Created by etudiant on 18/01/17.
 */
public class Sandbox {
    private final static Logger logger = Logger.getLogger(Sandbox.class);

    public static void testASCII() {
        RSAEncryptor encryptor = new RSAEncryptor();
        byte[] ascii_message = encryptor.toASCII("Bonjour !");
        logger.info("Traduction de l'ASCII : " + encryptor.fromASCII(ascii_message));
    }

    public static void testEncryptDecrypt(String message) {

        KeyGenerator generator = new KeyGenerator();
        generator.initParameters();

        BigInteger n = new BigInteger("5141");
        BigInteger e = new BigInteger("7");
        BigInteger u = new BigInteger("4279");

        generator.defineParameters(n, new BigInteger("0"), e);
        generator.defineU(u);

        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : " + publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.info("Clé privée : " + privateKey);

        Encryptor encryptor = new RSAEncryptor();

        BigInteger[] encryptedHello = encryptor.encrypt(message, publicKey);

        String decryptedMSG = encryptor.decrypt(encryptedHello, privateKey);

        logger.info("Message décrypté  : " + decryptedMSG);
    }


    public static void main(String[] args) {
        //testASCII();

        String mode;
        if (args.length < 1) {
            mode = "c";
        } else {
            mode = "s";
        }

        String address;
        if (args.length > 1) {
            address = args[2];
        } else {
            address = "localhost";
        }

        address = "192.168.99.107";

        if ("c".equals(mode)) {
            // créer client
            try (DatagramSocket socket = new DatagramSocket()) {
                String s = "message";
                byte[] buf = s.getBytes();
                InetAddress adresse = InetAddress.getByName(address);
                int port = Integer.parseInt("2000");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, adresse, port);
                socket.send(packet);

                //Reception
                byte[] inBuf = new byte[1024];
                DatagramPacket packetReceive = new DatagramPacket(inBuf, inBuf.length);
                socket.receive(packetReceive);

                String reponse = new String(packetReceive.getData(), 0, packetReceive.getLength());
                logger.info("Réponse : "+ reponse);
            }catch (SocketException e) {
               logger.debug("SocketException",e);
            } catch (UnknownHostException e) {
                logger.debug("UnknownHostException", e);
            } catch (IOException e) {
                logger.debug("IOException", e);
            }
        } else {
            // créer serveur
            int port = Integer.parseInt("2000");
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
                PublicKey o = (PublicKey) is.readObject();

                ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
                os.writeObject(privateKey);
                os.flush();

                while (true) {

                    //Reception
                    String messageCrypte = (String) is.readObject();

                    // décryptage du message recu
                    String decryptedMSG = encryptor.decrypt(messageCrypte, privateKey);
                    logger.info("Message décrypté : " + decryptedMSG);


                    // préparation de la réponse
                    String reponse = "Roger";
                    BigInteger[] encryptedHello = encryptor.encrypt(reponse, publicKey);
                    String reponseCrypted = encryptedHello[0].toString();
                    for (int i = 1; i < encryptedHello.length; i++) {
                        reponseCrypted += "%" + encryptedHello[i];
                    }
                    logger.info("Réponse crypté: " + reponseCrypted);


                    //Envoi de la réponse cryptée
                    os.writeObject(reponseCrypted);
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
}
