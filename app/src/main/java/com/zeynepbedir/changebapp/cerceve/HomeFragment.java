package com.zeynepbedir.changebapp.cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zeynepbedir.changebapp.Adapter.GonderiAdapter;
import com.zeynepbedir.changebapp.Model.Gonderi;
import com.zeynepbedir.changebapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> gonderiList;

    private FirebaseAuth auth;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView= view.findViewById(R.id.recyler_view_HomeFragment);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        recyclerView.setAdapter(gonderiAdapter);
        gonderiList = new ArrayList<>();
        gonderiAdapter = new GonderiAdapter(getContext(), gonderiList);

        recyclerView.setAdapter(gonderiAdapter);

        auth = FirebaseAuth.getInstance();

        gonderileriOku();

        return view;
    }

    private void gonderileriOku() {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        Query query = gonderiYolu.orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gonderiList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                    gonderiList.add(gonderi);
                }

                Collections.reverse(gonderiList); // Gönderileri ters çevirerek en yeni olandan başlayacak şekilde sırala
                gonderiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda burası tetiklenir
            }
        });
    }


}