package com.example.homemade_guardian_beta.Main.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.homemade_guardian_beta.R;

public class Loding_Dialog {
    private Context context;
    public Loding_Dialog(Context context)
    {
        this.context = context;
    }
    public void callDialog()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();

    }
    public void calldismiss(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.dismiss();
    }


}