package com.example.homemade_guardian_beta.post.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.fragment.SearchResultFragment;


//채팅방안 액티비티 -chatFragment랑 연결됨
public class SearchResultActivity extends BasicActivity {
    private DrawerLayout drawerLayout;
    private SearchResultFragment searchResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        setToolbarTitle("검색 결과");


        String search = getIntent().getStringExtra("search");

        Log.d ("로그w","1");

        // chatting area
        searchResultFragment = SearchResultFragment.getInstance(search);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, searchResultFragment )
                .commit();
        Log.d ("로그w","2");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onBackPressed() {
//        chatFragment.backPressed();
//        finish();
//    }

}
