package com.example.homemade_guardian_beta.Main.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.homemade_guardian_beta.Main.Fragment.Deal_Complete_Post_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.My_Review_Writen_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.My_Writen_Post_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.Proceeding_Post_Fragment;
import com.example.homemade_guardian_beta.Main.Fragment.To_Review_Writen_Fragment;

import com.example.homemade_guardian_beta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//SearchActivity에서 버튼을 눌러 넘어 온 액티비티이다.
//      Ex) SearchResultFragment에서 Fragment를 이용하여 결과물을 출력한다. <-> SearchResultAdapter와 연결된다.

public class MyInfoPostActivity extends BasicActivity {
    private Deal_Complete_Post_Fragment Deal_Complete_Post_Fragment;
    private My_Review_Writen_Fragment My_Review_Writen_Fragment;
    private My_Writen_Post_Fragment My_Writen_Post_Fragment;
    private Proceeding_Post_Fragment Proceeding_Post_Fragment;
    private To_Review_Writen_Fragment To_Review_Writen_Fragment;

    private String CurrentUid;
    private FirebaseUser CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        String Info = getIntent().getStringExtra("Info");

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        CurrentUid =CurrentUser.getUid();

        if(Info.equals("0")) {
            // Deal_Complete_Post_Fragment
            setToolbarTitle("거래 완료");
            Deal_Complete_Post_Fragment = Deal_Complete_Post_Fragment.getInstance(CurrentUid);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, Deal_Complete_Post_Fragment)
                    .commit();
        }

        if(Info.equals("1")) {
            // My_Review_Writen_Fragment
            setToolbarTitle("내가 작성한 리뷰");
            My_Review_Writen_Fragment = My_Review_Writen_Fragment.getInstance(Info);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, My_Review_Writen_Fragment)
                    .commit();
        }

        if(Info.equals("2")) {
            // My_Writen_Post_Fragment
            setToolbarTitle("내가 쓴 게시물");
            My_Writen_Post_Fragment = My_Writen_Post_Fragment.getInstance(Info);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, My_Writen_Post_Fragment)
                    .commit();
        }

        if(Info.equals("3")) {
            // Proceeding_Post_Fragment
            setToolbarTitle("진행중인 포스트");
            Proceeding_Post_Fragment = Proceeding_Post_Fragment.getInstance(Info);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, Proceeding_Post_Fragment)
                    .commit();
        }

        if(Info.equals("4")) {
            // To_Review_Writen_Fragment
            setToolbarTitle("나에게 작성한 리뷰");
            To_Review_Writen_Fragment = To_Review_Writen_Fragment.getInstance(Info);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MyInfo_Post_Fragment, To_Review_Writen_Fragment)
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
