package com.example.homemade_guardian_beta.Main.Fragment;

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
import com.example.homemade_guardian_beta.market.adapter.SearchResultAdapter;
import com.example.homemade_guardian_beta.model.market.MarketModel;
import com.example.homemade_guardian_beta.model.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class Deal_Complete_Post_Fragment extends Fragment {
    private String CurrentUid;
    private ArrayList<MarketModel> MarketList;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "SearchResultFragment";
    private SearchResultAdapter searchResultAdapter;

    MarketModel marketModel;
    UserModel userModel;
    ArrayList<String> Market_dealList = new ArrayList<>();

    int i=0;

    public Deal_Complete_Post_Fragment() {

    }
    public static final Deal_Complete_Post_Fragment getInstance(String CurrentUid) {
        Deal_Complete_Post_Fragment f = new Deal_Complete_Post_Fragment();
        Bundle bdl = new Bundle();
        bdl.putString("CurrentUid", CurrentUid);
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
        CurrentUid =  getArguments().getString("CurrentUid");

        firebaseFirestore = FirebaseFirestore.getInstance();
        MarketList = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(getActivity(), MarketList);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(searchResultAdapter);
        MarketUpdate(true);
        return  view;
    }
    private void MarketUpdate(final boolean clear) {

        final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("USERS").document(CurrentUid);
        documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                Market_dealList = userModel.getUserModel_Market_dealList();
                for(i =0;i<Market_dealList.size();i++){
                    final DocumentReference documentReferenceMyUser = FirebaseFirestore.getInstance().collection("MARKETS").document(Market_dealList.get(i));
                    documentReferenceMyUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            marketModel = documentSnapshot.toObject(MarketModel.class);
                            Log.d("asd","marketModel :" + marketModel.getMarketModel_Title());

                            MarketList.add(marketModel);
                            searchResultAdapter.notifyDataSetChanged();
                        }
                    });
                }
                //searchResultAdapter.notifyDataSetChanged();

            }
        });

    }
}
