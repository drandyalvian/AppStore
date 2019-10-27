package com.example.company.appstore.KepalaCabang;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.company.appstore.R;

import java.util.ArrayList;

public class LaporanUangAdapter extends RecyclerView.Adapter<LaporanUangAdapter.MyViewHolder> {

    //listener untuk delete
    FirebaseDataListener listener;

    Context context;
    ArrayList<LaporanUangConst> laporanUangConsts;
    public LaporanUangAdapter(ArrayList<LaporanUangConst> p, Context c, Context b){
        context = c;
        laporanUangConsts = p;

        listener = (LaporanUangAct) b;
    }

    @NonNull
    @Override
    public LaporanUangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LaporanUangAdapter.MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_laporan_uang,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanUangAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.xtgl.setText(laporanUangConsts.get(i).getTanggal());
        myViewHolder.xnominal.setText(laporanUangConsts.get(i).getNominal());

        myViewHolder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onDeleteData(laporanUangConsts.get(i), i);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return laporanUangConsts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xtgl,  xnominal;
        Button delete;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xtgl = itemView.findViewById(R.id.xtgl);
            xnominal = itemView.findViewById(R.id.xnominal);
            delete = itemView.findViewById(R.id.delete);

        }
    }

//delete listener
    public interface FirebaseDataListener{
        void onDeleteData(LaporanUangConst laporanUangConst, int i);
    }
}
