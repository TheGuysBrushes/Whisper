package com.whisperers.whisper;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Work on 1/30/2017.
 */

public class ChatAdapter extends ArrayAdapter<Whisper> {

    ChatAdapter(Context context) {
        super(context, R.layout.line_message);
    }

    // utilise le fichier xml pour afficher chaque élément de l'ArrayAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Whisper item= getItem(position);

//		applique le texte à la vue
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.line_message, parent, false);



        TextView textMessage= (TextView)view.findViewById(R.id.textMsg);
        if (item != null) {
            textMessage.setText(item.getContent());
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (item.hasBeenSendByMe()) {
            params.gravity = Gravity.RIGHT;
            textMessage.setLayoutParams(params);

            textMessage.setBackgroundResource(R.drawable.blue_radius);
        } else {
            params.gravity = Gravity.LEFT;
            textMessage.setLayoutParams(params);

            textMessage.setBackgroundResource(R.drawable.green_radius);
        }

        textMessage.setTextColor(Color.WHITE);

        return view;
    }

}
