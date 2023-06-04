package com.zeynepbedir.changebapp.cerceve;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.zeynepbedir.changebapp.R;


public class AramaFragment extends Fragment {


    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private String[] katagori = {"İktisadi ve İdari Bilimler Fakültesi","Mühendislik Fakültesi",
                                "Ziraat ve Doğa Bilimleri Fakültesi","Güzel Sanatlar ve Tasarım Fakültesi",
                                 "İslami İlimler Fakültesi","Uygulamalı Bilimler Fakültesi",
                                  "Sağlık Bilimleri Fakültesi","Tıp Fakültesi","Diş Hekimliği Fakültesi",
                                  "Fen Fakültesi","İnsan ve Toplum Fakültes","Meslek Yüksek Okulu",
                                    "Sağlık Meslek Yüksek Okulu"};



    private void aramaYap(String aramaTerimi) {
            // Arama işlemleri
        }

        // Diğer metotlar ve işlemler

}