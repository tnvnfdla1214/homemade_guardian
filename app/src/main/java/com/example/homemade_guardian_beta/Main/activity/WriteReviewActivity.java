package com.example.homemade_guardian_beta.Main.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.activity.BasicActivity;

public class WriteReviewActivity extends BasicActivity {
    private Context context;
    Button Review_Write_Button;
    EditText WriteReview_EditText;
    TextView Writen_Review_TextView;

    public WriteReviewActivity() {
    }
    public WriteReviewActivity(Context context, TextView WriteReview_EditText) {
        this.context = context;
        this.Writen_Review_TextView = Writen_Review_TextView;
    }
    public void callFunction(final TextView Writen_Review_TextView) {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.activity_writereview);
        dlg.show();
//        setContentView(R.layout.activity_writereview);
//        setToolbarTitle("");
        Review_Write_Button = (Button) dlg.findViewById(R.id.Review_Write_Button);
        WriteReview_EditText = (EditText) dlg.findViewById(R.id.WriteReview_EditText);
        WriteReview_EditText.setText(Writen_Review_TextView.getText());
        Review_Write_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Writen_Review_TextView.setText(WriteReview_EditText.getText());
                dlg.dismiss();
            }
        });
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_writereview);
//        setToolbarTitle("");
//        Review_Write_Button = (Button) findViewById(R.id.Review_Write_Button);
//        WriteReview_EditText = (EditText) findViewById(R.id.WriteReview_EditText);
//
//        Review_Write_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Writen_Review_TextView.setText(WriteReview_EditText.getText());
//                finish();
//            }
//        });
//
//
//    }

}
