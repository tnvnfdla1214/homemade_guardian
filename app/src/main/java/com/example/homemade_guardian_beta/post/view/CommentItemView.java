package com.example.homemade_guardian_beta.post.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.Comment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CommentItemView extends LinearLayout {
    private Context context;
    private LayoutInflater layoutInflater;
    //private ArrayList<SimpleExoPlayer> playerArrayList = new ArrayList<>();
    private int moreIndex = -1;

    public CommentItemView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CommentItemView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }


    private void initView(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_post, this, true);                                                 // part19 : view_post는 게시물 클릭시 페이지, item_post는 메인에서 나열된 게시물
        //addView(layoutInflater.inflate(R.layout.view_post, this, true));                                          // part19 : merge를 사용하면 직접 실행 (45')
    }

    public void setMoreIndex(int moreIndex){
        this.moreIndex = moreIndex;                                                                                 // part19 : 어댑터에서도 쓸 수 있게 (46'50")
    }
    // 실질적으로 파이에스토에 내의 댓글의 내용들을 담는 행위
    public void setComment(Comment comment) {
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(comment.getTimestamp()));

        LinearLayout contentsLayout = findViewById(R.id.contentsLayout);

        TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);
        textView.setText(comment.getComment());
        contentsLayout.addView(textView);

    }
}
