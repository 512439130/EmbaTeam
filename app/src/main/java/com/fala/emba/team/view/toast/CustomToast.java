package com.fala.emba.team.view.toast;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fala.emba.team.R;


public class CustomToast extends Toast {

    private TextView textView;
    private String text;
    private View view;
    private int time;
    private Context context;

    public CustomToast(Context context, String text, int time) {
        super(context);
        this.context = context;
        this.text = text;
        this.time = time;
        init();
    }

    private void init() {
        view = View.inflate(context, R.layout.view_custom_toast, null);
        setView(view);
        textView = view.findViewById(R.id.textView);
        textView.setText(text);
//        setGravity(Gravity.CENTER, 0, 100);
        setDuration(time);
    }

}
