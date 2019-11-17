package com.example.company.appstore.Owner;

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

    Context context;
    ArrayList<AbsensiConst> absensiConsts;
    public DataKaryawanAdapter(ArrayList<AbsensiConst> p, Context c){
        context = c;
        absensiConsts = p;
    }

    @NonNull
    @Override
    public DataKaryawanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DataKaryawanAdapter.MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_karyawan,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataKaryawanAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.xnama.setText(absensiConsts.get(i).getNama());
        myViewHolder.xalamat.setText(absensiConsts.get(i).getAlamat());

        final String cabang = absensiConsts.get(i).getNama_cabang();
        final String key = absensiConsts.get(i).getKey();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, EditKaryawanAct.class);
                go.putExtra("keyCabang",cabang); //Lempar key
                go.putExtra("key",key); //Lempar key
                context.startActivity(go);
            }
        });

    }

    @Override
    public int getItemCount() {
        return absensiConsts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama,  xalamat;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xnama = itemView.findViewById(R.id.xnama);
            xalamat = itemView.findViewById(R.id.xalamat);

        }
    }
}
