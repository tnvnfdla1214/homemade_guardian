package com.example.homemade_guardian_beta.Main.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.community.adapter.SearchCommunityResultAdapter;
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

public class Market_Food_Fragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<MarketModel> MarketList;
    private SearchResultAdapter searchResultAdapter;
    private boolean updating;
    private boolean topScrolled;

    public Market_Food_Fragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchcommunity_result, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        MarketList = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(getActivity(), MarketList);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchResultAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {                          // part21 : 스크롤로 새로고침 (29'10")
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {        //part21 : 스크롤 손을 뗏을때(31')
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                if(newState == 1 && firstVisibleItemPosition == 0){                                      // part21 : 위로 새로고침 (39'40")
                    topScrolled = true;
                }
                if(newState == 0 && topScrolled){
                    MarketUpdate(true);
                    topScrolled = false;
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){                          //part21 : 스크롤 되는 내내(31')
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 8 <= lastVisibleItemPosition && !updating){                         // part21 : 아래에서 3번쩨 일때 && 로딩중일 때는 이벤트 작용 안하게 (35'10")
                    MarketUpdate(true);
                }
                if(0 < firstVisibleItemPosition){
                    topScrolled = false;
                }
            }
        });
        MarketUpdate(true);
        return  view;
    }
    private void MarketUpdate(final boolean clear) {
        updating = true;

        Date date = MarketList.size() == 0 || clear ? new Date() : MarketList.get(MarketList.size() - 1).getMarketModel_DateOfManufacture();  //part21 : 사이즈가 없으면 현재 날짜 아니면 최근 말짜의 getCreatedAt로 지정 (27'40")
        CollectionReference collectionReference = firebaseFirestore.collection("MARKETS");                // 파이어베이스의 posts에서
        //아래꺼는 색인이 없으면 안뜨는듯
        collectionReference.orderBy("MarketModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("MarketModel_DateOfManufacture", date).whereEqualTo("MarketModel_Category","음식").limit(10).get()
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
                        } else {
                        }
                        updating = false;
                    }
                });

    }
}
