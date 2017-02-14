package com.whisperers.whisper.thread;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.whisperers.whisper.BackgroundFragment;

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
import MessageExchange.Whisper;

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

    private Whisper messageToSend;
    private Whisper messageReceived;

    private Socket socket;
    String address = "192.168.43.78";
    int port = 2000;

    private boolean connected = false;

    private volatile boolean running = true; // Run unless told to pause

    BackgroundFragment fragment;

    public ComThread(BackgroundFragment fragment, String address) {
        encryptor = new RSAEncryptor();
        messageToSend = null;
        messageReceived = null;
        this.fragment = fragment;
        this.address = address;
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
            sendKey(sharablePublicKey);
            Log.d(TAG, "Public key sent");

            inS = new BufferedInputStream(socket.getInputStream());
            oinS = new ObjectInputStream(inS);
            receiveKey();
            Log.d(TAG, "Public key received");
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Echec envoi clé", e);
        }

        connected = true;
        Log.i(TAG, "run: Socket connecté et clés échangées");

        do {
            while (!running)
                yield();
            sendReceiveMessage();
        } while (messageReceived == null || !messageReceived.getContent().equals("quit"));
    }

    public void sendReceiveMessage() {
        if (messageToSend != null) {
            Log.i(TAG, "Envoi du message : " + messageToSend);
            try {
                Log.i(TAG, "sendReceiveMessage: "+messageToSend.toString());
                sendMessage();
            } catch (IOException e) {
                Log.e(TAG, "run: ", e);
            }
        }

        try {
            if (inS.available() > 1) {
                messageReceived = (Whisper) oinS.readObject();
                if (messageReceived != null) {
                    Log.i(TAG, "sendReceiveMessage: "+messageReceived.toString());
                    messageReceived.decrypt(encryptor, myPrivateKey);
                    fragment.onMessageReceived(messageReceived);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Erreur message", e);
        }
        try {
            sleep(500);
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

        messageToSend.encrypt(encryptor, serverPublicKey);
        outS.writeObject(messageToSend);
        outS.flush();

        messageToSend = null;
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

    public void setMessage(Whisper message) {
        this.messageToSend = message;
    }

    public void pauseThread() throws InterruptedException {
        running = false;
    }

    public void resumeThread() {
        running = true;
    }

    public boolean isConnected() {
        return connected;
    }

}
