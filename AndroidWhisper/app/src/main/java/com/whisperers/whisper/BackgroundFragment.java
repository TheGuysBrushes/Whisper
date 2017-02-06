package com.whisperers.whisper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.whisperers.whisper.thread.ComThread;
import com.whisperers.whisper.thread.ConnectionThread;

/**
 * Created by etudiant on 31/01/17.
 */

public class BackgroundFragment extends Fragment {
    private final static String TAG = "BACKGROUND_FRAGMENT";

    private ChatAdapter adapter;

    private ComThread comThread;

    private TaskCallBacks mMainActivityListener = null;

    interface TaskCallBacks {
        void onMessageReceived(Whisper whisper);
    }

    public void sendNewMessage(Whisper whisper) {
        adapter.add(whisper);
        comThread.setMessage(whisper.getContent());
    }

    public void onMessageReceived(Whisper newWhisper) {
        adapter.add(newWhisper);
        if (mMainActivityListener != null) mMainActivityListener.onMessageReceived(newWhisper);
    }

    public ChatAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ChatAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        comThread = new ComThread(this);
        comThread.start();
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
        if (context instanceof Activity) {
            mMainActivityListener = (TaskCallBacks) context;
        }
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
