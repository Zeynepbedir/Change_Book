package com.zeynepbedir.changebapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

public class GonderiActivity extends AppCompatActivity {

    Uri resimUri;
    String myUri ="";

    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;

    ImageView image_Kapat, image_Eklendi, eklenen_Resim;
    TextView txt_Gonder;
    EditText kitap_adi, kitap_hakkinda, kitap_fiyat;
    AutoCompleteTextView kategori_Gonderi;
    private int imgIzinAlmaKodu =0, imgIzinVerildiKodu =1;
    private Bitmap secilenResim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi);

        AutoCompleteTextView act= findViewById(R.id.katagori_Gonderi);

        String [] kategori = getResources().getStringArray(R.array.kategori);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,kategori);
        act.setAdapter(adapter);

        eklenen_Resim=findViewById(R.id.eklenen_Resim);
        image_Kapat =findViewById(R.id.close_Gonderi);
        image_Eklendi =findViewById(R.id.eklenen_Resim);

        txt_Gonder =findViewById(R.id.txt_Gonderi);
        kitap_adi =findViewById(R.id.edt_Kitap_adi);
        kitap_hakkinda =findViewById(R.id.edt_Kitap_Hakkinda);
        kitap_fiyat=findViewById(R.id.edt_kitap_fiyat);
        kategori_Gonderi=findViewById(R.id.katagori_Gonderi);

        resimYukleYolu = FirebaseStorage.getInstance().getReference("gonderiler");

        image_Kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GonderiActivity.this,AnaSayfaActivity.class));
                finish();
            }
        });
        txt_Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
            }
        });


    }

    private String dosyaUzantisiAl(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void resimYukle(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gönderiliyor");
        progressDialog.show();

        //Resim yükleme kodları
        if (resimUri != null){
            StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    +"."+dosyaUzantisiAl(resimUri));

            yuklemeGorevi=dosyaYolu.putFile(resimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }



                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri indirmeUri = task.getResult();
                        myUri = indirmeUri.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

                        String gonderiId = veriYolu.push().getKey();

                        HashMap<String,Object> hashMap = new HashMap<>();

                        hashMap.put("gonderiId",gonderiId);
                        hashMap.put("gonderiResmi",myUri);
                        hashMap.put("gonderiAdi",kitap_adi.getText().toString());
                        hashMap.put("gonderiHakkinda",kitap_hakkinda.getText().toString());
                        hashMap.put("gonderiFiyati",kitap_fiyat.getText().toString());
                        hashMap.put("gonderiKategori",kategori_Gonderi.getText().toString());
                        hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        veriYolu.child(gonderiId).setValue(hashMap);
                        progressDialog.dismiss();

                        startActivity(new Intent(GonderiActivity.this,AnaSayfaActivity.class));
                        finish();
                    }else
                    {
                        Toast.makeText(GonderiActivity.this, "Gönderme Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GonderiActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Seçilen Resim Yok", Toast.LENGTH_SHORT).show();
        }

    }


    public void resimSec(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},imgIzinAlmaKodu);
        }else{
            Intent resmiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(resmiAl, imgIzinVerildiKodu);
        }
    }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == imgIzinAlmaKodu){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent resmiAl = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(resmiAl, imgIzinVerildiKodu);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == imgIzinVerildiKodu) {
            if (resultCode == RESULT_OK && data != null) {
                resimUri = data.getData();

                CropImage.activity(resimUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)  // Kesim oranını burada ayarlayabilirsiniz
                        .start(this);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resimUri = result.getUri();

                try {
                    if (Build.VERSION.SDK_INT >= 28) {
                        ImageDecoder.Source resimSource = ImageDecoder.createSource(this.getContentResolver(), resimUri);
                        secilenResim = ImageDecoder.decodeBitmap(resimSource);
                        eklenen_Resim.setImageBitmap(secilenResim);
                    } else {
                        secilenResim = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resimUri);
                        eklenen_Resim.setImageBitmap(secilenResim);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Resim kesme hatası: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}