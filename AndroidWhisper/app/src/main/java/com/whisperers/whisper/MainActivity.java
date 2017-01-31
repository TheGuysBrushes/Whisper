package com.whisperers.whisper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView)findViewById(R.id.listview);
        chatAdapter= new ChatAdapter(this);
        chatAdapter.add(new Whisper("hello", true));
        chatAdapter.add(new Whisper("hello you", false));

        // Bind to our new adapter.
        listview.setAdapter(chatAdapter);

        ComTask comTask = new ComTask();
        comTask.execute();
    }
}

