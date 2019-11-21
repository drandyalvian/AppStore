package com.example.company.appstore.Owner;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.company.appstore.KepalaCabang.AbsensiAdapter;
import com.example.company.appstore.KepalaCabang.AbsensiConst;
import com.example.company.appstore.KepalaCabang.LaporanUangConst;
import com.example.company.appstore.KepalaCabang.ListAbsensiAct;
import com.example.company.appstore.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DataKaryawanAdapter extends RecyclerView.Adapter<DataKaryawanAdapter.MyViewHolder> {
    DataKaryawanAct listener;

    Context context;
    ArrayList<AbsensiConst> absensiConsts;
    public DataKaryawanAdapter(ArrayList<AbsensiConst> p, Context c, Context b){
        context = c;
        absensiConsts = p;

        listener = (DataKaryawanAct) b;
    }

    @NonNull
    @Override
    public DataKaryawanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DataKaryawanAdapter.MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_karyawan,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DataKaryawanAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.xnama.setText(absensiConsts.get(i).getNama());
        myViewHolder.xalamat.setText(absensiConsts.get(i).getAlamat());

        final String cabang = absensiConsts.get(i).getCabang();
        final String key = absensiConsts.get(i).getKey();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, EditKaryawanAct.class);
                go.putExtra("keyCabang",cabang); //Lempar key
                go.putExtra("key",key); //Lempar key
                context.startActivity(go);
                ((Activity) context).finish();

            }
        });

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onDeleteData(absensiConsts.get(i), i);

            }
        });

    }
//
    @Override
    public int getItemCount() {
        return absensiConsts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama,  xalamat, delete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xnama = itemView.findViewById(R.id.xnama);
            xalamat = itemView.findViewById(R.id.xalamat);
            delete = itemView.findViewById(R.id.delete);

        }
    }

    public interface FirebaseDataListener{
        void onDeleteData(AbsensiConst absensiConsts, int i);
    }
}
