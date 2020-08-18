package com.example.homemade_guardian_beta.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homemade_guardian_beta.R;

public class SearchActivity extends BasicActivity {
    private EditText searchPost;
    private Button searchbutton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbarTitle("검색");

        searchPost = findViewById(R.id.searchPost);

        searchbutton = findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.searchbutton:
                    Log.d ("로그w","1");
                    String search = searchPost.getText().toString();
                    Log.d ("로그w",search);
                    myStartActivity(SearchResultActivity.class,search);
                    Log.d ("로그w",search);
                    break;

            }
        }
    };
    private void myStartActivity(Class c, String search) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent intent = new Intent(SearchActivity.this, c);
        intent.putExtra("search", search);
        startActivityForResult(intent, 0);
    }
}
