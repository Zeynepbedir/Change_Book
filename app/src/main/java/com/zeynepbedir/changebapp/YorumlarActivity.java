package com.zeynepbedir.changebapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeynepbedir.changebapp.Adapter.YorumAdapter;
import com.zeynepbedir.changebapp.Model.Yorum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YorumlarActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private YorumAdapter yorumAdapter ;
    private List<Yorum> yorumList;

    EditText edt_yorum_ekle;
    TextView txt_gonder;

    String gonderiId;
    String gonderenId;

    FirebaseUser mevcutKullanici;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar);

        Toolbar toolbar = findViewById(R.id.toolBar_yorumlarActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyler_view_yorumlarActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        yorumList = new ArrayList<>();
        yorumAdapter = new YorumAdapter(this,yorumList);
        recyclerView.setAdapter(yorumAdapter);

        edt_yorum_ekle=findViewById(R.id.edt_yorumlarActivity);
        txt_gonder=findViewById(R.id.txt_gonder_yorumlarActivity);
        Intent intent = getIntent();

        gonderiId = intent.getStringExtra("gonderiId");
        gonderenId = intent.getStringExtra("gonderenId");

        mevcutKullanici= FirebaseAuth.getInstance().getCurrentUser();

        txt_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_yorum_ekle.getText().toString().equals(""))
                {
                    Toast.makeText(YorumlarActivity.this, "Bu alanı boş bırakamazsınız...", Toast.LENGTH_SHORT).show();
                }
                else{
                    yorumekle();
                }
            }
        });

        yorumlariOku();

    }

    private void yorumekle() {

        DatabaseReference yorumYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("yorum",edt_yorum_ekle.getText().toString());
        hashMap.put("gonderen",mevcutKullanici.getUid());

        yorumYolu.push().setValue(hashMap);
        edt_yorum_ekle.setText("");
    }

    private void yorumlariOku(){
        DatabaseReference yorumOkuYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        yorumOkuYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yorumList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Yorum yorum = dataSnapshot.getValue(Yorum.class);
                    yorumList.add(yorum);
                }
                yorumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}