package com.example.homemade_guardian_beta.community.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.community.fragment.SearchCommunityResultFragment;

//SearchActivity에서 버튼을 눌러 넘어 온 액티비티이다.
//      Ex) SearchResultFragment에서 Fragment를 이용하여 결과물을 출력한다. <-> SearchResultAdapter와 연결된다.

public class SearchCommunityResultActivity extends BasicActivity {
    private SearchCommunityResultFragment SearchCommunityResultFragment;  //SearchResultFragment를 통해 Fragment를 씀

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcommunity_result);
        setToolbarTitle("검색 결과");
        String Search = getIntent().getStringExtra("Communitysearch");

        // chatting area
        SearchCommunityResultFragment = SearchCommunityResultFragment.getInstance(Search);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.SearchCommunityResultFragment, SearchCommunityResultFragment)
                .commit();
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
}
