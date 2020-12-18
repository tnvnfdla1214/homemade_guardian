package com.example.homemade_guardian_beta.Main.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.homemade_guardian_beta.R;

// 게시물 작성이나, 수정 등 액티비티 간의 로딩 부분을 채워주는 Dialog

public class Loding_Dialog {

    private Context context;
    public Loding_Dialog(Context context)
    {
        this.context = context;
    }
    public void callDialog()
    {
        final Dialog dialog = new Dialog(context);
        Log.d("석규규규규규","111");
//        final AlertDialog.Builder alertDialogdialog = new AlertDialog.Builder(context);
//        AlertDialog dialog = alertDialogdialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("석규규규규규","222");
       // setCancelable : Dialog 뒷 부분의 터치나 뒤로가기를 허용하지 않는 코드
        dialog.setCancelable(false);
        Log.d("석규규규규규","333");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.d("석규규규규규","444");
        dialog.setContentView(R.layout.dialog_loading);
        Log.d("석규규규규규","555");
        ImageView Loding_charactor = (ImageView) dialog.findViewById(R.id.Loding_charactor);
        Log.d("석규규규규규","666");
        Glide.with(Loding_charactor.getContext()).load(R.drawable.loading).into(new DrawableImageViewTarget(Loding_charactor));
        Log.d("석규규규규규","777");
        dialog.show();
        Log.d("석규규규규규","888");
    }

    public void calldismiss(){
        Log.d("석규규규규규","1");
        final Dialog dialog = new Dialog(context);
        Log.d("석규규규규규","2");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("석규규규규규","3");
        dialog.setCancelable(false);
        Log.d("석규규규규규","4");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.d("석규규규규규","5");
        dialog.setContentView(R.layout.dialog_loading);
        Log.d("석규규규규규","6");
        dialog.dismiss();
        Log.d("석규규규규규","7");
    }
}