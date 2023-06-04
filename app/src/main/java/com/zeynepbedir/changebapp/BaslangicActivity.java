package com.zeynepbedir.changebapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaslangicActivity extends AppCompatActivity {

    Button btn_baslangisGiris;
    Button btn_baslangisKaydol;

    FirebaseUser baslangicKullanici;

    @Override
    protected void onStart() { //başladığında ne yapması gerektiğini burada yazıyorum
        super.onStart();

        //Firebase auth sınıfının bir örneğini döndür
        //getCurrentUser ile Kullanıcı verilerini getir
        baslangicKullanici = FirebaseAuth.getInstance().getCurrentUser();

        //Eğer veri tabanında varsa direkt anasayfaya gönder
        //uygulamaya bir defa giriş yapılınca şifre girmeden anasayfaya bağlanmayı sağlar

        if (baslangicKullanici != null){
            startActivity(new Intent(BaslangicActivity.this,AnaSayfaActivity.class));
        }
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_baslangic);



        btn_baslangisGiris = findViewById(R.id.btn_giris);
        btn_baslangisKaydol = findViewById(R.id.btn_kaydol);

        btn_baslangisGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaslangicActivity.this,GirisActivity.class));
            }
        });
        btn_baslangisKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaslangicActivity.this,KaydolActivity.class));
            }
        });
    }


}