package com.zeynepbedir.changebapp.cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeynepbedir.changebapp.Adapter.FotografAdapter;
import com.zeynepbedir.changebapp.Model.Gonderi;
import com.zeynepbedir.changebapp.Model.Kullanici;
import com.zeynepbedir.changebapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    ImageView resimSecenekler;
    TextView txt_gonderiler, txt_Ad,txt_kullaniciAdi;

    Button btn_profil_duzenle;

    ImageButton imagebtn_Fotografrim, imagebtn_kaydedilenFotograflar;

    private List<String> kaydettiklerim;

    //kaydettiğim gönderi görme recler
    RecyclerView recyclerViewKaydettiklerim;
    FotografAdapter fotografAdapterKaydettiklerim;
    private List<Gonderi> List_kaydettiklerim;

//fotografları profilde görme
    RecyclerView recyclerViewFotgraf;
    FotografAdapter fotografAdapter;
    List<Gonderi> gonderiList;

    FirebaseUser mevcutKullanici;
    String profilId;



    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profilId=prefs.getString("profileId","none");

        resimSecenekler = view.findViewById(R.id.resimSecenekler_profilsyf);

        txt_gonderiler = view.findViewById(R.id.txt_gonderiler_profilsyf);
        txt_Ad = view.findViewById(R.id.txt_ad_profilsyf);
        txt_kullaniciAdi = view.findViewById(R.id.kullaniciadi_profilsyf);

        btn_profil_duzenle=view.findViewById(R.id.profiliDüzenle_profilsyf);

        imagebtn_Fotografrim=view.findViewById(R.id.imagebtn_fotograflarim_profilsyf);
        imagebtn_kaydedilenFotograflar=view.findViewById(R.id.imagebtn_kaydedilenfotograflar_profilsyf);

        //fotografları profilde görme
        recyclerViewFotgraf = view.findViewById(R.id.recyler_view_profilsyf);
        recyclerViewFotgraf.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerViewFotgraf.setLayoutManager(linearLayoutManager);
        gonderiList = new ArrayList<>();
        fotografAdapter = new FotografAdapter(getContext(),gonderiList);
        recyclerViewFotgraf.setAdapter(fotografAdapter);

        //kaydettiğim gönderi görme recler
        recyclerViewKaydettiklerim = view.findViewById(R.id.recyler_view_kaydet_profilsyf);
        recyclerViewKaydettiklerim.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_kaydettiklerim = new GridLayoutManager(getContext(),3);
        recyclerViewKaydettiklerim.setLayoutManager(linearLayoutManager_kaydettiklerim);
        List_kaydettiklerim =new ArrayList<Gonderi>();
        fotografAdapterKaydettiklerim= new FotografAdapter(getContext(),List_kaydettiklerim);
        recyclerViewKaydettiklerim.setAdapter(fotografAdapterKaydettiklerim);
        recyclerViewFotgraf.setVisibility(View.VISIBLE);
        recyclerViewKaydettiklerim.setVisibility(View.GONE);


        //metotları burada çağırdım
        kullaniciBilgi();
        gonderiSayisi();
        fotograflarim();
        kaydettiklerim();

        if (profilId.equals(mevcutKullanici.getUid())){
            btn_profil_duzenle.setText("Profili Düzenle");
        }else{
            imagebtn_kaydedilenFotograflar.setVisibility(View.GONE);
        }

        btn_profil_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = btn_profil_duzenle.getText().toString();
                if (btn.equals("Profili Düznle")){
                    //profil düzenleme
                }
            }
        });

        imagebtn_Fotografrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewFotgraf.setVisibility(View.VISIBLE);
                recyclerViewKaydettiklerim.setVisibility(View.GONE);
            }
        });

        imagebtn_kaydedilenFotograflar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewFotgraf.setVisibility(View.GONE);
                recyclerViewKaydettiklerim.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
    private void kullaniciBilgi(){
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profilId);

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null){
                    return;
                }
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                txt_kullaniciAdi.setText(kullanici.getKullanniciadi());
                txt_Ad.setText(kullanici.getAd());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void gonderiSayisi(){
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);

                    if (gonderi.getGonderen().equals(profilId)){
                        i++;
                    }
                }

                txt_gonderiler.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void fotograflarim(){
        DatabaseReference fotografYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
        fotografYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gonderiList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
                    if (gonderi.getGonderen().equals(profilId)){
                        gonderiList.add(gonderi);
                    }
                }
                Collections.reverse(gonderiList);
                fotografAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kaydettiklerim(){

        kaydettiklerim=new ArrayList<>();
        DatabaseReference kaydettiklerimYolu = FirebaseDatabase.getInstance().getReference("Kaydettiklerim")
                .child(mevcutKullanici.getUid());
        kaydettiklerimYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    kaydettiklerim.add(dataSnapshot.getKey());
                }
                kaydettiklerimiOku();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kaydettiklerimiOku() {
        DatabaseReference gonderidenOku = FirebaseDatabase.getInstance().getReference("Gonderiler");

        gonderidenOku.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List_kaydettiklerim.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);

                    for (String id : kaydettiklerim){
                        if (gonderi.getGonderiId().equals(id)){
                            List_kaydettiklerim.add(gonderi);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}