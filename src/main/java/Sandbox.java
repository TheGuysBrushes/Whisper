import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.Encryptor;
import Encryption.RSAEncryptor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;

/**
 * Created by etudiant on 18/01/17.
 */
public class Sandbox {
    private final static Logger logger = Logger.getLogger(KeyGenerator.class);

    public static void testASCII() {
        RSAEncryptor encryptor = new RSAEncryptor();
        byte[] ascii_message = encryptor.toASCII("Bonjour !");
        logger.info("Traduction de l'ASCII : " + encryptor.fromASCII(ascii_message));
    }

    public static void main(String[] args) {
        //testASCII();

        if ("c".equals(args[0])) {
            // créer client
            try (DatagramSocket socket = new DatagramSocket()) {
                String s = "message";
                byte[] buf = s.getBytes();
                InetAddress adresse = InetAddress.getByName("localhost");
                int port = Integer.parseInt("2000");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, adresse, port);
                socket.send(packet);

                //Reception
                byte[] inBuf = new byte[1024];
                DatagramPacket packetReceive = new DatagramPacket(inBuf, inBuf.length);
                socket.receive(packetReceive);

                String reponse = new String(packetReceive.getData(), 0, packetReceive.getLength());
            }catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // créer serveur
            int port = Integer.parseInt("2000");
            boolean continuer = true;
            try (DatagramSocket socket = new DatagramSocket(port)) {
                while (continuer) {
                    //Reception
                    byte[] inBuf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(inBuf, inBuf.length);

                    socket.receive(packet);
                    String s = new String(packet.getData(), 0, packet.getLength());


                    System.out.println(s);

                    //Envoi
                    String reponse = "";
                    byte[] buf = reponse.getBytes();
                    DatagramPacket packetSend = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());

                    socket.send(packetSend);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
/*
        KeyGenerator generator = new KeyGenerator();
        BigInteger a = new BigInteger("5141");
        BigInteger b = new BigInteger("4992");

        generator.setE(a);
        generator.setM(b);

        PublicKey publicKey = generator.generatePublicKey();
        logger.info("Clé publique : " + publicKey);

        PrivateKey privateKey = generator.generatePrivateKey();
        logger.info("Clé privée : " + privateKey);

        RSAEncryptor encryptor = new RSAEncryptor();

        BigInteger[] encryptedHello = encryptor.encrypt("Bonjour !", publicKey);

        String decryptedMSG = encryptor.decrypt(encryptedHello, privateKey);

        logger.info("Message décrypté  : " + decryptedMSG);
*/

    }
}