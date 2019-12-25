package com.example.company.appstore.KepalaCabang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;

import java.util.ArrayList;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.MyViewHolder> {

    Context context;
    ArrayList<AbsensiConst> absensiConsts;
    public AbsensiAdapter(Context c, ArrayList<AbsensiConst> p){
        context = c;
        absensiConsts = p;
    }

    @NonNull
    @Override
    public AbsensiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absensi, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

//        return new MyViewHolder(LayoutInflater.
//                from(context).inflate(R.layout.item_absensi,
//                parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AbsensiAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.xnama.setText(absensiConsts.get(i).getNama());
        myViewHolder.xposisi.setText(absensiConsts.get(i).getPosisi());

        final String getkey = absensiConsts.get(i).getKey_name();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go = new Intent(context, ListAbsensiAct.class);
                go.putExtra("key",getkey); //Lempar key
//                go.putExtra("cabang", absensiConsts.get(i).getCabang());
                context.startActivity(go);
            }
        });

    }

    @Override
    public int getItemCount() {
        return absensiConsts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama,  xposisi;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xnama = itemView.findViewById(R.id.xnama);
            xposisi = itemView.findViewById(R.id.xposisi);

        }
    }
}
