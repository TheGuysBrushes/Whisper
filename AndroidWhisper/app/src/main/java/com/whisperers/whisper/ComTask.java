package com.whisperers.whisper;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import Encryption.Encryptor;
import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;

/**
 * Created by etudiant on 26/01/17.
 */

public class ComTask extends AsyncTask<String, Void, Void> {
    private static String TAG = "WHISPER";

    public PublicKey SharablePublicKey;
    private PrivateKey myPrivateKey;

    private PublicKey serverPublicKey;

    private ObjectInputStream inS;
    private ObjectOutputStream outS;

    private Socket socket;

    Encryptor encryptor;

    String message = "hello";
    String address = "192.168.43.78";
//        address = "localhost";


    String s_port = "2000";

    private void generateKeys() {
        KeyGenerator generator = new KeyGenerator();

        SharablePublicKey = generator.generatePublicKey();

        myPrivateKey = generator.generatePrivateKey();
    }

    public void initConnection(String address, String s_port) throws IOException {

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
            Log.w(TAG,e);
        } catch (UnknownHostException e) {
            Log.w(TAG,e);
        } catch (IOException e) {
            Log.w(TAG,e);
        } catch (ClassNotFoundException e) {
            Log.w(TAG,e);
        }
    }

    public void encryptSendMessage(String message) throws IOException {

        if (serverPublicKey == null) {
            Log.i("","Je n'ai pas recu la clé public du serveur");
            return;
        }

        // Envoi d'un message
        String messageCrypted = encryptor.encryptToString(message, serverPublicKey);

        outS.writeObject(messageCrypted);
        outS.flush();
    }

    /**
     * Receive and decrypt a message sent by the server
     *
     * @return the received message sent by the server
     * @throws IOException
     */
    public String receiveDecryptMessage() throws IOException {

        if (myPrivateKey == null) {
            Log.i("","Je n'ai pas recu la clé public du serveur");
            return "";
        }

        try {
            // Réception de la réponse
            String response = (String) inS.readObject();
            String decryptedReponse = encryptor.decrypt(response, myPrivateKey);
            Log.i("","Réponse : " + decryptedReponse);

            return decryptedReponse;
        } catch (ClassNotFoundException e) {
            Log.w(TAG,e);
            return "";
        }
    }

    private void PingPong(String message) throws IOException {
        encryptSendMessage(message);
        String response = receiveDecryptMessage();
        Log.i("PINGPONG", "PingPong: " + response);
    }

    public void closeConnection() throws IOException{
        socket.close();
    }
    public ComTask() {
        encryptor = new RSAEncryptor();
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            initConnection(address, s_port);

            PingPong(message);
            PingPong(message + " (1)");
            PingPong(message + " (2)");
            PingPong(message + " (3)");

            closeConnection();
        } catch (IOException e) {
            Log.w(TAG,e);
        }
        return null;
    }
}
