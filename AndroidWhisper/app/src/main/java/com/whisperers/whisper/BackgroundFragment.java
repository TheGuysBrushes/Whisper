package com.whisperers.whisper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.whisperers.whisper.thread.ComThread;

import java.net.Socket;

import MessageExchange.Whisper;

/**
 * Created by etudiant on 31/01/17.
 */

public class BackgroundFragment extends Fragment {
    private final static String TAG = "BACKGROUND_FRAGMENT";

    private ChatAdapter adapter;

    private ComThread comThread;

    private TaskCallBacks mMainActivityListener = null;

    private String address;

    interface TaskCallBacks {
        void onMessageReceived(Whisper whisper);
    }

    public void sendNewMessage(Whisper whisper) {
        adapter.add(new Whisper(whisper));
        comThread.setMessage(whisper);
    }


    public void onMessageReceived(Whisper newWhisper) {
        adapter.add(new Whisper(newWhisper));
        if (mMainActivityListener != null) mMainActivityListener.onMessageReceived(newWhisper);
    }

    public ChatAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ChatAdapter adapter) {
        this.adapter = adapter;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setmMainActivityListener(TaskCallBacks mMainActivityListener) {
        this.mMainActivityListener = mMainActivityListener;
    }

   public boolean isConnected(){
       return comThread.isConnected();
   }

    public void runFragment(){
        comThread = new ComThread(this, address);
        comThread.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mMainActivityListener = null;
        try {
            comThread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, "Exception", e);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            comThread.pauseThread();
        } catch (InterruptedException e) {
            Log.d(TAG, "Exception", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        comThread.resumeThread();
    }
}
