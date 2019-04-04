package com.hyf.intelligence.iot.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyf.intelligence.iot.R;


public class MyDialog extends Dialog {

    private String title;
    private String content;
    private String leftStr;
    private String rightStr;

    private View.OnClickListener listener;


    public MyDialog(Context context,  String content, View.OnClickListener listener) {
        super(context, R.style.MyDialog);
        this.content = content;
        this.listener = listener;
    }
    public MyDialog(Context context, String title, String content, View.OnClickListener listener) {
        super(context, R.style.MyDialog);
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    public MyDialog(Context context, String title, String content, String leftText, String rightText, View.OnClickListener listener) {
        super(context, R.style.MyDialog);

        this.title = title;
        this.content = content;
        this.listener = listener;
        this.leftStr = leftText;
        this.rightStr = rightText;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        TextView tvTitle = findViewById(R.id.title);
        TextView tvContent = findViewById(R.id.content);
        TextView leftText = findViewById(R.id.left_text);
        TextView rightText = findViewById(R.id.right_text);

        if(title == null){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        tvContent.setText(content);
        if (leftStr != null && !leftStr.equals("")){
            leftText.setText(leftStr);
        }

        if (rightStr != null && !rightStr.equals("")){
            rightText.setText(rightStr);
        }

        leftText.setOnClickListener(listener);
        rightText.setOnClickListener(listener);

    }
}
