package com.whisperers.whisper.thread;

import android.util.Log;

import com.whisperers.whisper.BackgroundFragment;
import com.whisperers.whisper.Whisper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Encryption.Encryptor;
import Encryption.KeyGenerator;
import Encryption.PrivateKey;
import Encryption.PublicKey;
import Encryption.RSAEncryptor;

/**
 * Created by etudiant on 02/02/17.
 */

public class ComThread extends Thread {
    private final String TAG = "THREAD_SENDER";
    final private Encryptor encryptor;

    private PublicKey serverPublicKey;
    public PublicKey sharablePublicKey;
    private PrivateKey myPrivateKey;

    private ObjectInputStream oinS;
    private InputStream inS;
    private ObjectOutputStream outS;

    private String messageToSend;
    private String messageReceived;
    private Socket socket;
    String address = "192.168.43.78";
    int port = 2000;

    private volatile boolean running = true; // Run unless told to pause

    BackgroundFragment fragment;

    public ComThread(BackgroundFragment fragment) {
        encryptor = new RSAEncryptor();
        messageToSend = "";
        messageReceived = "";
        // this.socket = socket;
        this.fragment = fragment;
        Log.i(TAG, "Création thread communication");
    }

    /**
     * Override of the run method
     */
    @Override
    public void run() {
        Log.i(TAG, "Lancement du thread communication");
        generateKeys();
        try {
            socket = new Socket(InetAddress.getByName(address), port);

            outS = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            Log.d(TAG, "object output stream " + outS);
            sendKey(sharablePublicKey);
            Log.d(TAG, "Public key sent");

            inS = new BufferedInputStream(socket.getInputStream());
            oinS = new ObjectInputStream(inS);
            Log.d(TAG, "object input stream " + oinS);
            receiveKey();
            Log.d(TAG, "Public key received");
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Echec envoi clé", e);
        }

        do {
            while (!running)
                yield();
            sendReceiveMessage();
        } while (!messageReceived.equals("quit"));
    }

    public void sendReceiveMessage() {
        Log.i(TAG, "Boucle");
        if (!messageToSend.isEmpty()) {
            Log.i(TAG, "Envoi du message : " + messageToSend);
            try {
                sendMessage();
            } catch (IOException e) {
                Log.e(TAG, "run: ", e);
            }
        }

        try {
            Log.i(TAG, "Data dispo : " + inS.available());
            if (inS.available() > 1) {
                String response = (String) oinS.readObject();

                if (response != null) {
                    Log.i(TAG, "Reception du message : " + response);
                    Whisper whisper = new Whisper(receiveDecryptMessage(response), false);
                    fragment.onMessageReceived(whisper);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Erreur message", e);
        }
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            Log.d(TAG, "Exception", e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            outS.close();
            inS.close();
            socket.close();
        } catch (IOException e) {
            Log.d(TAG, "Exception", e);
        }
    }

    /**
     * Generate both public and private key
     */
    private void generateKeys() {
        KeyGenerator generator = new KeyGenerator();

        sharablePublicKey = generator.generatePublicKey();
        Log.i(TAG, "Clé publique : " + sharablePublicKey);

        myPrivateKey = generator.generatePrivateKey();
        Log.d(TAG, "Clé privée : " + myPrivateKey);
    }

    /**
     * Encrypt and send a message to the server
     *
     * @throws IOException
     */
    public void sendMessage() throws IOException {

        if (serverPublicKey == null) {
            Log.e(TAG, "Je n'ai pas recu la clé public du serveur");
            return;
        }

        // Envoi d'un message
        String messageCrypted = encryptor.encryptToString(messageToSend, serverPublicKey);
        outS.writeObject(messageCrypted);
        outS.flush();

        messageToSend = "";
    }

    /**
     * Receive and decrypt a default_message sent by the server
     *
     * @return the received default_message sent by the server
     * @throws IOException
     */
    public String receiveDecryptMessage(String response) throws IOException {

        if (myPrivateKey == null) {
            Log.i("MESSAGING", "Je n'ai pas recu la clé public du serveur");
            return "";
        }

        // Réception de la réponse
        String decryptedReponse = encryptor.decrypt(response, myPrivateKey);
        Log.i("MESSAGING", "Réponse : " + decryptedReponse);
        return decryptedReponse;
    }

    /**
     * Send a public key to another client
     *
     * @param publicKey : sharable public key to send
     * @throws IOException
     */
    public void sendKey(PublicKey publicKey) throws IOException {
        outS.writeObject(publicKey);
        outS.flush();
    }

    /**
     * Send a public key to another client
     *
     * @throws IOException
     */
    public void receiveKey() throws IOException, ClassNotFoundException {
        serverPublicKey = (PublicKey) oinS.readObject();
        Log.i(TAG, "Server key received : " + serverPublicKey);
    }

    public void setPublicKey(PublicKey publicKey) {
        serverPublicKey = publicKey;
    }

    public void setMessage(String message) {
        this.messageToSend = message;
    }

    public void pauseThread() throws InterruptedException
    {
        running = false;
    }

    public void resumeThread()
    {
        running = true;
    }
}
