package com.example.homemade_guardian_beta.post.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemade_guardian_beta.R;
import com.example.homemade_guardian_beta.model.post.PostModel;
import com.example.homemade_guardian_beta.post.adapter.SearchResultAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class SearchResultFragment extends Fragment {
    private String search;
    private ArrayList<PostModel> postList;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "SearchResultFragment";
    private SearchResultAdapter searchResultAdapter;

    public SearchResultFragment() {

    }
    public static final SearchResultFragment getInstance(String search) {
        SearchResultFragment f = new SearchResultFragment();
        Bundle bdl = new Bundle();
        bdl.putString("search", search);
        f.setArguments(bdl);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchresult, container, false);
        search = (String)  getActivity().getIntent().getSerializableExtra("search");

        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(getActivity(), postList);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchResultAdapter);
        postsUpdate(true);
        return  view;
    }
    private void postsUpdate(final boolean clear) {
        //updating = true;

        Date date = postList.size() == 0 || clear ? new Date() : postList.get(postList.size() - 1).getPostModel_DateOfManufacture();  //part21 : 사이즈가 없으면 현재 날짜 아니면 최근 말짜의 getCreatedAt로 지정 (27'40")
        CollectionReference collectionReference = firebaseFirestore.collection("POSTS");                // 파이어베이스의 posts에서
        collectionReference.orderBy("PostModel_DateOfManufacture", Query.Direction.DESCENDING).whereLessThan("PostModel_DateOfManufacture", date).limit(10).get()  // post14: 게시물을 날짜 기준으로 순서대로 나열 (23'40") // part21 : 날짜기준으로 10개  collectionReference.whereGreaterThanOrEqualTo("title",  search).limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String title;
                        if (task.isSuccessful()) {
                            if(clear){                      //part22 : clear를 boolean으로 써서 업데이트 도중에 게시물 클릭시 발생하는 오류 해결 (3'30")   // part15 : MainAdapter에서 setOnClickListener에서 시작 (35'30")
                                postList.clear();                                                           // part16 : List 안의 데이터 초기화
                            }                                                                               // part16 : postsUpdate로 이동 (15'50")
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                title = document.getData().get("PostModel_Title").toString();
                                if(title.toLowerCase().contains(search.toLowerCase())) {
                                    postList.add(new PostModel(                                                          //postList로 데이터를 넣는다.
                                            document.getData().get("PostModel_Title").toString(),
                                            document.getData().get("PostModel_Text").toString(),
                                            (ArrayList<String>) document.getData().get("PostModel_ImageList"),
                                            new Date(document.getDate("PostModel_DateOfManufacture").getTime()),
                                            document.getData().get("PostModel_Host_Uid").toString(),
                                            document.getId(),
                                            document.getData().get("PostModel_Category").toString(),
                                            (ArrayList<String>) document.getData().get("PostModel_LikeList")));
                                }
                                title = null;
                            }
                            searchResultAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d("로그","실패?");
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        //updating = false;
                    }
                });
    }
}
