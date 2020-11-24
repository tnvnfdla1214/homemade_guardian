package com.example.homemade_guardian_beta.Main.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
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
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loading);
        ImageView Loding_charactor = (ImageView) dialog.findViewById(R.id.Loding_charactor);
        Glide.with(Loding_charactor.getContext()).load(R.drawable.loading).into(new DrawableImageViewTarget(Loding_charactor));
        dialog.show();

    }
    public void calldismiss(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loading);
        dialog.dismiss();
    }


}