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
        Whisper item= (Whisper)getItem(position);

//		applique le texte à la vue
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.line_message, parent, false);

//        TextView text= (TextView)convertView.findViewById(R.id.TextFic_Dos);
//        text.setText(nom);


        TextView acronym= (TextView)convertView.findViewById(R.id.fileName);
        if (item != null) {
            acronym.setText(item.getContent());
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        if (item.hasBeenSendByMe()) {
            acronym.setBackgroundColor(Color.argb(150,50,200,100));

            params.gravity = Gravity.RIGHT;
        } else {
            acronym.setBackgroundColor(Color.argb(150,50,100,200));

            params.gravity = Gravity.LEFT;
        }
        acronym.setLayoutParams(params);

        return convertView;
    }

}
