package com.example.homemade_guardian_beta.market.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.homemade_guardian_beta.Main.Fragment.Market_Borrow_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.Market_Food_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.Market_Quest_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.Market_Thing_Fragment;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.market.fragment.SearchResultFragment;

//SearchActivity에서 버튼을 눌러 넘어 온 액티비티이다.
//      Ex) SearchResultFragment에서 Fragment를 이용하여 결과물을 출력한다. <-> SearchResultAdapter와 연결된다.

public class SearchResultActivity extends BasicActivity {
    private SearchResultFragment SearchResultFragment;  //SearchResultFragment를 통해 Fragment를 씀
    private Market_Food_Fragment marketFoodFragment;
    private Market_Thing_Fragment marketThingFragment;
    private Market_Borrow_Fragment marketBorrowFragment;
    private Market_Quest_Fragment marketQuestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        String Info = getIntent().getStringExtra("Info");
        String Search = getIntent().getStringExtra("search");

        // chatting area

        if(Info.equals("0")) {
            setToolbarTitle("검색 결과");
            SearchResultFragment = SearchResultFragment.getInstance(Search);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, SearchResultFragment)
                    .commit();
        }
        if(Info.equals("1")) {
            marketFoodFragment = new Market_Food_Fragment();
            setToolbarTitle("음식교환");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, marketFoodFragment)
                    .commit();
        }
        if(Info.equals("2")) {
            marketThingFragment = new Market_Thing_Fragment();
            setToolbarTitle("물건교환");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, marketThingFragment)
                    .commit();
        }
        if(Info.equals("3")) {
            marketBorrowFragment = new Market_Borrow_Fragment();
            setToolbarTitle("대여하기");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, marketBorrowFragment)
                    .commit();
        }
        if(Info.equals("4")) {
            marketQuestFragment = new Market_Quest_Fragment();
            setToolbarTitle("퀘스트");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, marketQuestFragment)
                    .commit();
        }
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
