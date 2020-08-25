package com.example.homemade_guardian_beta.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homemade_guardian_beta.R;

// 검색을 실행하려 하고 검색하고자 하는 단어를 입력 받는 액티비티이다.
//      Ex) 메인프레그먼트에서 검색버튼을 눌러 넘어온다.
//      Ex) 단어를 입력한 후 버튼을 누르면 SearchResultActivity로 넘어가게 된다.

public class SearchActivity extends BasicActivity {
    private EditText SearchPost;    //검색하고자 하는 단어 입력 받는 EditText
    private Button SearchButton;    //검색을 실행하는 버튼

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbarTitle("검색");

        SearchPost = findViewById(R.id.searchPost);

        SearchButton = findViewById(R.id.searchbutton);
        SearchButton.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchbutton:
                    String search = SearchPost.getText().toString();
                    myStartActivity(SearchResultActivity.class,search);
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
