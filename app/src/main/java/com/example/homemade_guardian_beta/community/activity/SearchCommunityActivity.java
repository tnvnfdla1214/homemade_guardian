package com.example.homemade_guardian_beta.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.community.adapter.CommunityAdapter;
import com.example.homemade_guardian_beta.community.adapter.SearchCommunityResultAdapter;
import com.example.homemade_guardian_beta.market.activity.SearchActivity;
import com.example.homemade_guardian_beta.market.activity.SearchResultActivity;
import com.example.homemade_guardian_beta.market.adapter.SearchResultAdapter;
import com.example.homemade_guardian_beta.model.community.CommunityModel;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

// 검색을 실행하려 하고 검색하고자 하는 단어를 입력 받는 액티비티이다.
//      Ex) 메인프레그먼트에서 검색버튼을 눌러 넘어온다.
//      Ex) 단어를 입력한 후 버튼을 누르면 SearchResultActivity로 넘어가게 된다.

public class SearchCommunityActivity extends BasicActivity {
    private EditText SearchCommunity;    //검색하고자 하는 단어 입력 받는 EditText
    private ImageView searchbtn;      //검색을 실행하는 버튼
    private ArrayList<CommunityModel> CommunityList;
    private SearchCommunityResultAdapter searchCommunityResultAdapter;
    private FirebaseFirestore firebaseFirestore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);
        setToolbarTitle("검색");
        firebaseFirestore = FirebaseFirestore.getInstance();

        SearchCommunity = findViewById(R.id.Search_PostTitle);
        searchbtn = findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(onClickListener);
        CommunityList = new ArrayList<>();
        searchCommunityResultAdapter = new SearchCommunityResultAdapter(SearchCommunityActivity.this, CommunityList);
        final RecyclerView recyclerView = findViewById(R.id.HotrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchCommunityActivity.this));
        recyclerView.setAdapter(searchCommunityResultAdapter);
        HotCommunity(true);

        SearchCommunity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                switch (actionId)
                {
                    case IME_ACTION_SEARCH :
                        String Search = SearchCommunity.getText().toString();
                        myStartActivity(SearchCommunityResultActivity.class,Search);
                        break;
                }
                return true;
            }
        });
    }
        private void HotCommunity(final boolean clear) {
            Date date = new Date();
            CollectionReference collectionReference = firebaseFirestore.collection("COMMUNITY");                // 파이어베이스의 posts에서
            collectionReference.orderBy("CommunityModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("CommunityModel_DateOfManufacture", date).whereEqualTo("CommunityModel_HotCommunity","O").limit(4).get()  // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개  collectionReference.whereGreaterThanOrEqualTo("title",  search).limit(10).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(clear){                      //part22 : clear를 boolean으로 써서 업데이트 도중에 게시물 클릭시 발생하는 오류 해결 (3'30")   // part15 : MainAdapter에서 setOnClickListener에서 시작 (35'30")
                                    CommunityList.clear();                                                           // part16 : List 안의 데이터 초기화
                                }                                                                               // part16 : postsUpdate로 이동 (15'50")
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                        CommunityList.add(new CommunityModel(                                                          //postList로 데이터를 넣는다.
                                                document.getData().get("CommunityModel_Title").toString(),
                                                document.getData().get("CommunityModel_Text").toString(),
                                                (ArrayList<String>) document.getData().get("CommunityModel_ImageList"),
                                                new Date(document.getDate("CommunityModel_DateOfManufacture").getTime()),
                                                document.getData().get("CommunityModel_Host_Uid").toString(),
                                                document.getId(),
                                                (ArrayList<String>) document.getData().get("CommunityModel_LikeList"),
                                                document.getData().get("CommunityModel_HotCommunity").toString(),
                                                Integer.parseInt(String.valueOf(document.getData().get("CommunityModel_CommentCount")))
                                        ));
                                }
                                searchCommunityResultAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchbtn:
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
