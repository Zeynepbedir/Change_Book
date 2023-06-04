package com.zeynepbedir.changebapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeynepbedir.changebapp.Model.Gonderi;
import com.zeynepbedir.changebapp.Model.Kullanici;
import com.zeynepbedir.changebapp.R;
import com.zeynepbedir.changebapp.YorumlarActivity;

import java.util.List;

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.ViewHolder> {

    public Context mContext;
    public List<Gonderi> mGonderi;

    private FirebaseUser mevcutFirebaseUser;

    public GonderiAdapter(Context mContext,List<Gonderi> mGonderi ) {
        this.mContext = mContext;
        this.mGonderi = mGonderi;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gonderi_ogesi, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Gonderi gonderi = mGonderi.get(position);

        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);
        gonderenBilgileri(holder.txt_kullanici_adi, gonderi.getGonderen());
        gonderiBilgileri(holder.txt_kitapadi, holder.txt_fiyat, gonderi.getGonderiId());

        kaydetme(gonderi.getGonderiId(), holder.begeni_resmi);

        //resmi beğenme
        holder.begeni_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.begeni_resmi.equals("kaydet")){
                    FirebaseDatabase.getInstance().getReference().child("Kaydedilenler")
                            .child(mevcutFirebaseUser.getUid()).child(gonderi.getGonderiId()).setValue(true);
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Kaydedilenler").child(mevcutFirebaseUser.getUid())
                            .child(gonderi.getGonderiId()).removeValue();
                }
            }
        });

        holder.yorum_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profil_resmi, gonderi_resmi, begeni_resmi, yorum_resmi;
        public TextView txt_kullanici_adi, txt_kitapadi, txt_fiyat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_Gonderi_Ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_Gonderi_Ogesi);
            begeni_resmi = itemView.findViewById(R.id.begeni_Gonderi_Ogesi);
            yorum_resmi = itemView.findViewById(R.id.yorum_Gonderi_Ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_Gonderi_Ogesi);
            txt_kitapadi = itemView.findViewById(R.id.txt_kitapadi_Gonderi_Ogesi);
            txt_fiyat = itemView.findViewById(R.id.txt_kitap_fiyati_Gonderei_Ogesi);
        }
    }

    private void gonderenBilgileri(TextView kullanniciadi, String kullaniciId){

        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(kullaniciId);

        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                if (kullanici != null) {
                    kullanniciadi.setText(kullanici.getKullanniciadi());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void gonderiBilgileri(TextView kitapAdi, TextView fiyat, String gonderiId) {
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);

        gonderiYolu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Gonderi gonderi = snapshot.getValue(Gonderi.class);
                    if (gonderi != null) {
                        kitapAdi.setText(gonderi.getGonderiAdi());
                        fiyat.setText(gonderi.getGonderiFiyati());
                        // Diğer gönderi bilgilerini burada kullanabilirsiniz
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda yapılacak işlemleri burada tanımlayabilirsiniz
            }
        });
    }
    private void kaydetme (String gonderiId, ImageView imageView ){
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference kaydetmeYolu = FirebaseDatabase.getInstance().getReference().child("Kaydedilenler")
                .child(mevcutKullanici.getUid());

        kaydetmeYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(gonderiId).exists()){
                    imageView.setImageResource(R.drawable.ic_fav);
                    imageView.setTag("Kaydedildi");
                }
                else{
                    imageView.setImageResource(R.drawable.ic_begeni);
                    imageView.setTag("Kaydet");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
