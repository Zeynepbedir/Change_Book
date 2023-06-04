package com.zeynepbedir.changebapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zeynepbedir.changebapp.Model.Gonderi;
import com.zeynepbedir.changebapp.R;

import java.util.List;

public class FotografAdapter extends RecyclerView.Adapter<FotografAdapter.ViewHolder>{

    private Context context;
    private List<Gonderi> mGonderiler;

    public FotografAdapter(Context context, List mGonderiler) {
        this.context = context;
        this.mGonderiler = mGonderiler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fotograflar_ogesi,parent, false);

        return new FotografAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gonderi gonderi = mGonderiler.get(position);

        Glide.with(context).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);
    }

    @Override
    public int getItemCount() {
        return mGonderiler.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView gonderi_resmi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_fotografOgesi);

        }
    }
}
