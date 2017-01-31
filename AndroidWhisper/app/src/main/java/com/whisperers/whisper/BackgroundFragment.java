package com.whisperers.whisper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by etudiant on 31/01/17.
 */

public class BackgroundFragment extends Fragment {

    private TaskCallBacks mMainActivityListener = null;
    private ChatAdapter adapter;
    private List<ComTask> threadList = new ArrayList<>();


    public void sendNewMessage(Whisper whisper){
        ComTask thread = new ComTask(this);
        threadList.add(thread);
        thread.execute(whisper);
    }

    public ChatAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ChatAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mMainActivityListener = (TaskCallBacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mMainActivityListener = null;
    }

    protected void onPostExecute (Whisper whisper){
        if (mMainActivityListener != null) mMainActivityListener.onItemDone(whisper);
    }

    static interface TaskCallBacks {
        public void onItemDone(Whisper whisper);
    }
}
