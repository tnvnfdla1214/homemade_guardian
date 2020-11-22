package com.example.homemade_guardian_beta.market.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import com.example.homemade_guardian_beta.Main.activity.BasicActivity;
import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.community.activity.SearchCommunityResultActivity;
import com.example.homemade_guardian_beta.market.adapter.MarketAdapter;
import com.example.homemade_guardian_beta.market.adapter.SearchResultAdapter;
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

// 검색을 실행하려 하고 검색하고자 하는 단어를 입력 받는 액티비티이다.
//      Ex) 메인프레그먼트에서 검색버튼을 눌러 넘어온다.
//      Ex) 단어를 입력한 후 버튼을 누르면 SearchResultActivity로 넘어가게 된다.

public class SearchActivity extends BasicActivity {
    private ArrayList<MarketModel> MarketList;
    private EditText SearchMarket;    //검색하고자 하는 단어 입력 받는 EditText
    private ImageView searchbtn;    //검색을 실행하는 버튼
    private TextView firstfood, firstthing, firstborrow, firstquest;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayout food_layout, thing_layout, borrow_layout, quest_layout;
    private SearchResultAdapter searchResultAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbarTitle("검색");
        firebaseFirestore = FirebaseFirestore.getInstance();

        SearchMarket = findViewById(R.id.Search_PostTitle);
        searchbtn = findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(onClickListener);
        firstfood = findViewById(R.id.firstfood);
        firstthing = findViewById(R.id.firstthing);
        firstborrow = findViewById(R.id.firstborrow);
        firstquest = findViewById(R.id.firstquest);
        food_layout = findViewById(R.id.food_layout);
        thing_layout = findViewById(R.id.thing_layout);
        borrow_layout = findViewById(R.id.borrow_layout);
        quest_layout = findViewById(R.id.quest_layout);
        food_layout.setOnClickListener(onClickListener);
        thing_layout.setOnClickListener(onClickListener);
        borrow_layout.setOnClickListener(onClickListener);
        quest_layout.setOnClickListener(onClickListener);
        MarketList = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(SearchActivity.this, MarketList);
        final RecyclerView recyclerView = findViewById(R.id.HotrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(searchResultAdapter);
        HotMarket(true);

        SearchMarket.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                switch (actionId)
                {
                    case IME_ACTION_SEARCH :
                        String Search = SearchMarket.getText().toString();
                        myStartActivity(SearchResultActivity.class,Search,"0");
                        break;
                }
                return true;
            }
        });

        Date date =  new Date();  //part21 : 사이즈가 없으면 현재 날짜 아니면 최근 말짜의 getCreatedAt로 지정 (27'40")
        CollectionReference foodReference = firebaseFirestore.collection("MARKETS");                // 파이어베이스의 posts에서
        foodReference.orderBy("MarketModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("MarketModel_DateOfManufacture", date).whereEqualTo("MarketModel_Category","음식").limit(1).get()        // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {                                                                             // part16 : postsUpdate로 이동 (15'50")
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firstfood.setText(document.getData().get("MarketModel_Title").toString());

                            }
                        }
                    }
                });

        CollectionReference thingReference = firebaseFirestore.collection("MARKETS");                // 파이어베이스의 posts에서
        thingReference.orderBy("MarketModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("MarketModel_DateOfManufacture", date).whereEqualTo("MarketModel_Category","생필품").limit(1).get()        // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {                                                                             // part16 : postsUpdate로 이동 (15'50")
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firstthing.setText(document.getData().get("MarketModel_Title").toString());

                            }
                        }
                    }
                });

        CollectionReference borrowReference = firebaseFirestore.collection("MARKETS");                // 파이어베이스의 posts에서
        borrowReference.orderBy("MarketModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("MarketModel_DateOfManufacture", date).whereEqualTo("MarketModel_Category","대여").limit(1).get()        // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {                                                                             // part16 : postsUpdate로 이동 (15'50")
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firstborrow.setText(document.getData().get("MarketModel_Title").toString());

                            }
                        }
                    }
                });

        CollectionReference questReference = firebaseFirestore.collection("MARKETS");                // 파이어베이스의 posts에서
        questReference.orderBy("MarketModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("MarketModel_DateOfManufacture", date).whereEqualTo("MarketModel_Category","용역").limit(1).get()        // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {                                                                             // part16 : postsUpdate로 이동 (15'50")
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firstquest.setText(document.getData().get("MarketModel_Title").toString());

                            }
                        }
                    }
                });

    }
    //검색버튼의 OnClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchbtn:
                    String Search = SearchMarket.getText().toString();
                    myStartActivity(SearchResultActivity.class,Search,"0");
                    break;
                case R.id.food_layout:
                    myStartActivity(SearchResultActivity.class,null,"1");
                    break;
                case R.id.thing_layout:
                    myStartActivity(SearchResultActivity.class,null,"2");
                    break;
                case R.id.borrow_layout:
                    myStartActivity(SearchResultActivity.class,null,"3");
                    break;
                case R.id.quest_layout:
                    myStartActivity(SearchResultActivity.class,null,"4");
                    break;
            }
        }
    };

    private void HotMarket(final boolean clear) {
        Date date = new Date() ;
        CollectionReference collectionReference = firebaseFirestore.collection("MARKETS");                // 파이어베이스의 posts에서
        collectionReference.orderBy("MarketModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("MarketModel_DateOfManufacture", date).whereEqualTo("MarketModel_HotMarket","O").limit(2).get()  // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개  collectionReference.whereGreaterThanOrEqualTo("title",  search).limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){                      //part22 : clear를 boolean으로 써서 업데이트 도중에 게시물 클릭시 발생하는 오류 해결 (3'30")   // part15 : MainAdapter에서 setOnClickListener에서 시작 (35'30")
                                MarketList.clear();                                                           // part16 : List 안의 데이터 초기화
                            }                                                                               // part16 : postsUpdate로 이동 (15'50")
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    MarketList.add(new MarketModel(                                                          //postList로 데이터를 넣는다.
                                            document.getData().get("MarketModel_Title").toString(),
                                            document.getData().get("MarketModel_Text").toString(),
                                            (ArrayList<String>) document.getData().get("MarketModel_ImageList"),
                                            new Date(document.getDate("MarketModel_DateOfManufacture").getTime()),
                                            document.getData().get("MarketModel_Host_Uid").toString(),
                                            document.getId(),
                                            document.getData().get("MarketModel_Category").toString(),
                                            (ArrayList<String>) document.getData().get("MarketModel_LikeList"),
                                            document.getData().get("MarketModel_HotMarket").toString(),
                                            document.getData().get("MarketModel_reservation").toString(),
                                            document.getData().get("MarketModel_deal").toString(),
                                            Integer.parseInt(String.valueOf(document.getData().get("MarketModel_CommentCount")))
                                    ));
                            }
                            searchResultAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    //여기 SearchActivity에서 받은 search 값을 전달해준다.
    private void myStartActivity(Class c, String search, String info) {                                          // part : 여기서는 수정 버튼을 눌렀을 때 게시물의 정보도 같이 넘겨준다.
        Intent Intent_Search_Words = new Intent(SearchActivity.this, c);
        Intent_Search_Words.putExtra("search", search);
        Intent_Search_Words.putExtra("Info", info);
        startActivityForResult(Intent_Search_Words, 0);
    }
}
