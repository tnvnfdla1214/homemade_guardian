package com.example.homemade_guardian_beta.Main.bottombar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homemade_guardian_beta.model.post.PostModel;
import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.post.activity.SearchActivity;
import com.example.homemade_guardian_beta.post.adapter.HomeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private HomeAdapter homeAdapter;
    private ArrayList<PostModel> postList;
    private boolean updating;
    private boolean topScrolled;

    public HomeFragment() {                                                                                 // part22 : 프레그먼트로 내용 이전 (21'40")
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar myToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Home");
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), postList);
        //homeAdapter.setOnPostListener(onPostListener);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        //view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);

        view.findViewById(R.id.searchbtn).setOnClickListener(onClickListener);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeAdapter);
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
                    postsUpdate(true);
                    topScrolled = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){                          //part21 : 스크롤 되는 내내(31')
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                if(totalItemCount - 8 <= lastVisibleItemPosition && !updating){                         // part21 : 아래에서 3번쩨 일때 && 로딩중일 때는 이벤트 작용 안하게 (35'10")
                    postsUpdate(false);
                }

                if(0 < firstVisibleItemPosition){
                    topScrolled = false;
                }
            }
        });
        postsUpdate(false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
//        homeAdapter.playerStop();                                                                       //part21 : 앱정지시 비디오 정지 실행문 (15'10")
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.searchbtn:

                    myStartActivity(SearchActivity.class);
                    break;
            }
        }
    };

    private void postsUpdate(final boolean clear) {
        updating = true;
        Date date = postList.size() == 0 || clear ? new Date() : postList.get(postList.size() - 1).getPostModel_DateOfManufacture();  //part21 : 사이즈가 없으면 현재 날짜 아니면 최근 말짜의 getCreatedAt로 지정 (27'40")
        CollectionReference collectionReference = firebaseFirestore.collection("POSTS");                // 파이어베이스의 posts에서
        Log.d("로그","스크롤 333");
        collectionReference.orderBy("PostModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("PostModel_DateOfManufacture", date).limit(10).get()       // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){                      //part22 : clear를 boolean으로 써서 업데이트 도중에 게시물 클릭시 발생하는 오류 해결 (3'30")   // part15 : MainAdapter에서 setOnClickListener에서 시작 (35'30")
                                postList.clear();                                                           // part16 : List 안의 데이터 초기화
                            }                                                                               // part16 : postsUpdate로 이동 (15'50")
                            Log.d("로그","스크롤 555");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d("로그","스크롤 3333");
                                postList.add(new PostModel(                                                          //postList로 데이터를 넣는다.
                                        document.getData().get("PostModel_Title").toString(),
                                        (ArrayList<String>) document.getData().get("PostModel_ImageList"),
                                        new Date(document.getDate("PostModel_DateOfManufacture").getTime()),
                                        document.getData().get("PostModel_Host_Uid").toString(),
                                        document.getId()));
                            }
                            homeAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d("로그","실패?");
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}

