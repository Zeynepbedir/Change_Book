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
import com.zeynepbedir.changebapp.AnaSayfaActivity;
import com.zeynepbedir.changebapp.Model.Kullanici;
import com.zeynepbedir.changebapp.Model.Yorum;
import com.zeynepbedir.changebapp.R;

import java.util.List;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ViewHolder>{

    private Context mContext;
    private List<Yorum> mYorumListesi;

    private FirebaseUser mevcutKullanici;

    public YorumAdapter(Context mContext, List<Yorum> mYorumListesi) {
        this.mContext=mContext;
        this.mYorumListesi = mYorumListesi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_ogesi,parent,false);

        return new YorumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        Yorum yorum = mYorumListesi.get(position);

        holder.txt_yorum.setText(yorum.getYorum());

        kullaniciBilgisiAl(holder.profil_resmi, holder.txt_kullanici_adi, yorum.getGonderen());

        holder.txt_kullanici_adi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, AnaSayfaActivity.class);
                intent.putExtra("gonderenId",yorum.getGonderen());
                mContext.startActivity(intent);

            }
        });
        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, AnaSayfaActivity.class);
                intent.putExtra("gonderenId",yorum.getGonderen());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mYorumListesi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profil_resmi;

        public TextView txt_kullanici_adi, txt_yorum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi=itemView.findViewById(R.id.profil_resmi_yorum_ogesi);
            txt_kullanici_adi=itemView.findViewById(R.id.txt_kullaniciadi_yorumOgesi);
            txt_yorum=itemView.findViewById(R.id.txt_yorum_ogesi);
        }
    }

    private void kullaniciBilgisiAl (ImageView imageView, TextView kullaniciadi, String gonderenId){
        DatabaseReference gonderenIdYolu = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(gonderenId);

        gonderenIdYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);

                kullaniciadi.setText(kullanici.getAd());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
