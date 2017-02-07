package com.whisperers.whisper;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import MessageExchange.Whisper;

public class MainActivity extends ListActivity implements BackgroundFragment.TaskCallBacks {
    private ChatAdapter chatAdapter;
    private BackgroundFragment mTaskFragment;
    private static final String TAG_TASKS_FRAGMENT = "TASK_FRAGMENT";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getFragmentManager().putFragment(outState, TAG_TASKS_FRAGMENT, mTaskFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Whisper.setMyName("Bobdroid");

        chatAdapter = new ChatAdapter(this);
        chatAdapter.add(new Whisper("Hello"));
        chatAdapter.add(new Whisper("Hello you !"));

        FragmentManager fm = getFragmentManager();
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mTaskFragment = (BackgroundFragment) fm.getFragment(savedInstanceState, TAG_TASKS_FRAGMENT);
        }
        if (mTaskFragment == null) {
            mTaskFragment = new BackgroundFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASKS_FRAGMENT).commit();
        }
        if (mTaskFragment.getAdapter() != null) {
            populate();
        } else {
            mTaskFragment.setAdapter(new ChatAdapter(this));
        }

        setListAdapter(chatAdapter);
        TextView texte = (TextView) findViewById(R.id.messageText);

        ImageView imageSend = (ImageView) findViewById(R.id.imageSend);
        imageSend.setOnClickListener((View v) -> {
            Log.d("WHISPER", "Click send button");
            String message = texte.getText().toString();
            texte.setText("");
            Whisper whisper = new Whisper(message);
            chatAdapter.add(new Whisper(whisper));
            chatAdapter.notifyDataSetChanged();
            mTaskFragment.sendNewMessage(whisper);
        });
    }

    private void populate() {
        int i;
        for (i = 0; i < mTaskFragment.getAdapter().getCount(); ++i) {
            chatAdapter.add(mTaskFragment.getAdapter().getItem(i));
        }
    }

    @Override
    public void onMessageReceived(Whisper whisper) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatAdapter.add(new Whisper(whisper));
                chatAdapter.notifyDataSetChanged();
            }
        });
        Log.d("WHISPER", "Message complete");
    }
}

