package com.example.homemade_guardian_beta.Main.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.post.CommentModel;
import com.example.homemade_guardian_beta.model.user.ReviewModel;
import com.example.homemade_guardian_beta.post.activity.BasicActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-07.
 */

public class ReviewActivity extends BasicActivity {

    private Context context;
    private FirebaseFirestore Firestore =null;
    private ReviewModel ReviewModel;
    private int ReviewModel_Selected_Review;

    public ReviewActivity(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String To_User_Uid, final String PostModel_Post_Uid) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.review_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button WriteReview_Button = (Button) dlg.findViewById(R.id.WriteReview_Button);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);
        final TextView Writen_Review_TextView = (TextView) dlg.findViewById(R.id.Writen_Review_TextView);

        final CheckBox kind = dlg.findViewById(R.id.kind);
        final CheckBox correct = dlg.findViewById(R.id.correct);
        final CheckBox complete = dlg.findViewById(R.id.complete);
        final CheckBox bad = dlg.findViewById(R.id.bad);

        kind.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                kind.setChecked(true);
                correct.setChecked(false);
                complete.setChecked(false);
                bad.setChecked(false);
                ReviewModel_Selected_Review = 0;
            }
        }) ;

        correct.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                kind.setChecked(false);
                correct.setChecked(true);
                complete.setChecked(false);
                bad.setChecked(false);
                ReviewModel_Selected_Review = 1;
            }
        }) ;
        complete.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                kind.setChecked(false);
                correct.setChecked(false);
                complete.setChecked(true);
                bad.setChecked(false);
                ReviewModel_Selected_Review = 2;
            }
        }) ;
        bad.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                kind.setChecked(false);
                correct.setChecked(false);
                complete.setChecked(false);
                bad.setChecked(true);
                ReviewModel_Selected_Review = 3;
                WriteReviewActivity writeReviewActivity = new WriteReviewActivity(context,Writen_Review_TextView);
                writeReviewActivity.callFunction(Writen_Review_TextView);
            }
        }) ;
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                //main_label.setText(Writen_Review_TextView.getText().toString());
                // 커스텀 다이얼로그를 종료한다.


                String ReviewModel_Uid = null;
                ReviewModel_Uid = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid).collection("REVIEW").document().getId();

                Date DateOfManufacture = new Date();
                ReviewModel = new ReviewModel(ReviewModel_Uid, PostModel_Post_Uid,  To_User_Uid, Writen_Review_TextView.getText().toString(), ReviewModel_Selected_Review, DateOfManufacture);

                final DocumentReference docRef_Users_ReviewUid = FirebaseFirestore.getInstance().collection("USERS").document(To_User_Uid);
                final String Reviewmodel_Uid = ReviewModel_Uid;
                docRef_Users_ReviewUid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            WriteBatch Batch_REVIEW_ReviewUid = FirebaseFirestore.getInstance().batch();
                            Batch_REVIEW_ReviewUid.set(docRef_Users_ReviewUid.collection("REVIEW").document(Reviewmodel_Uid), ReviewModel.getReviewModel());
                            Batch_REVIEW_ReviewUid.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                    });



                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        WriteReview_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent Intent_WriteReview_Activity = new Intent(context, WriteReviewActivity.class);
//                context.startActivity(Intent_WriteReview_Activity);
                WriteReviewActivity writeReviewActivity = new WriteReviewActivity(context,Writen_Review_TextView);
                writeReviewActivity.callFunction(Writen_Review_TextView);
                //dlg.dismiss();
            }
        });
    }

}