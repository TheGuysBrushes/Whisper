package com.whisperers.whisper.thread;

import android.util.Log;

import com.whisperers.whisper.BackgroundFragment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by etudiant on 06/02/17.
 */

public class ConnectionThread extends Thread {
    private final static String TAG = "CONNECTION_THREAD";

    String address = "192.168.43.78";
    int port = 2000;

    private ComThread comThread;
    private Socket socket = null;

    private BackgroundFragment fragment;

    public ConnectionThread(BackgroundFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            //comThread = new ComThread(socket,fragment);
            comThread.start();
        } catch (IOException e) {
            Log.d(TAG,"Exception creation com thread",e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        Log.d(TAG,"Desctruction du thread de connection");
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.d(TAG, "Exception", e);
            }
        }
        try {
            comThread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, "Exception", e);
        }
    }

    public ComThread getComThread() {
        return comThread;
    }

    public Socket getSocket() {
        return socket;
    }
}
