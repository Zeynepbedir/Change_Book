package com.zeynepbedir.changebapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.zeynepbedir.changebapp.cerceve.AramaFragment;
import com.zeynepbedir.changebapp.cerceve.BildirimFragment;
import com.zeynepbedir.changebapp.cerceve.HomeFragment;
import com.zeynepbedir.changebapp.cerceve.ProfilFragment;

public class AnaSayfaActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView ;

    Fragment seciliCerceve = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();

        if (intent != null){

            String gonderen = intent.getString("gonderenId");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileId",gonderen);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new ProfilFragment()).commit();

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new HomeFragment()).commit();

        }



    }
    //activity de bulunan itemlere tıklayınca ne yapması gerekir
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //Ana çerçeveyi çağır
                            seciliCerceve=new HomeFragment();
                            break;

                        case R.id.nav_arama:
                            //arama çerçeveyi çağır
                            seciliCerceve=new AramaFragment();
                            break;

                        case R.id.nav_ekle:
                            //çerçeve boş olsun gonderi sayfasına gitsin

                            seciliCerceve=null;
                            startActivity(new Intent(AnaSayfaActivity.this,GonderiActivity.class));

                            break;

                        case R.id.nav_profil:
                            //profil çerçeveyi çağır
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();

                            seciliCerceve = new ProfilFragment();

                            break;

                    }
                    if (seciliCerceve!=null){
                        //ana sayfadaki çerceve kapsayıcıya secili cerceveyi ekler
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,seciliCerceve).commit();


                    }

                    return true;
                }
            };
}