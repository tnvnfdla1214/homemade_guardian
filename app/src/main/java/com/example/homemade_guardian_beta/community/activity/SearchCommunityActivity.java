package com.example.homemade_guardian_beta.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;

// 검색을 실행하려 하고 검색하고자 하는 단어를 입력 받는 액티비티이다.
//      Ex) 메인프레그먼트에서 검색버튼을 눌러 넘어온다.
//      Ex) 단어를 입력한 후 버튼을 누르면 SearchResultActivity로 넘어가게 된다.

public class SearchCommunityActivity extends BasicActivity {
    private EditText SearchCommunity;    //검색하고자 하는 단어 입력 받는 EditText
    private Button Title_Search_Button;    //검색을 실행하는 버튼

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);
        setToolbarTitle("검색");

        SearchCommunity = findViewById(R.id.Search_PostTitle);
        Title_Search_Button = findViewById(R.id.Search_PostTitle_Button);
        Title_Search_Button.setOnClickListener(onClickListener);
    }

    //검색버튼의 OnClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Search_PostTitle_Button:
                    String Search = SearchCommunity.getText().toString();
                    myStartActivity(SearchCommunityResultActivity.class,Search);
                    break;
            }
        }
    };

    //여기 SearchActivity에서 받은 search 값을 전달해준다.
    private void myStartActivity(Class c, String search) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent Intent_Search_Words = new Intent(SearchCommunityActivity.this, c);
        Intent_Search_Words.putExtra("Communitysearch", search);
        startActivityForResult(Intent_Search_Words, 0);
    }
}
