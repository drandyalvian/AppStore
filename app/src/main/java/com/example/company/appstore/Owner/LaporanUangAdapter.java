package com.example.company.appstore.Owner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.LaporanUangConst;
import com.example.company.appstore.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class LaporanUangAdapter extends RecyclerView.Adapter<LaporanUangAdapter.MyViewHolder> {
    LaporanOwnerAct listener;

    Context context;
    ArrayList<LaporanUangConst> laporanUangConsts;
    public LaporanUangAdapter(ArrayList<LaporanUangConst> p, Context c, Context b){
        context = c;
        laporanUangConsts = p;

        listener = (LaporanOwnerAct) b;
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

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        double nominal = Double.parseDouble(laporanUangConsts.get(i).getNominal());

        myViewHolder.xtgl.setText(laporanUangConsts.get(i).getTanggal());
        myViewHolder.xnominal.setText(formatRupiah.format ((double)nominal));

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView xtgl,  xnominal;
        Button delete;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xtgl = itemView.findViewById(R.id.xtgl);
            xnominal = itemView.findViewById(R.id.xnominal);
            delete = itemView.findViewById(R.id.delete);

        }
    }
    public interface FirebaseDataListener{
        void onDeleteData(LaporanUangConst laporanUangConst, int i);
    }
}
