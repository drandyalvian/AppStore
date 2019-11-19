package com.example.company.appstore.Owner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.company.appstore.KepalaCabang.AbsensiConst;
import com.example.company.appstore.KepalaCabang.ListAbsensiAct;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GajiAdapter  extends RecyclerView.Adapter<GajiAdapter.MyViewHolder> {

    private DatabaseReference reference;

    Context context;
    ArrayList<GajiConst> gajiConst;
    public GajiAdapter(ArrayList<GajiConst> p, Context c){
        context = c;
        gajiConst = p;
    }

    @NonNull
    @Override
    public GajiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GajiAdapter.MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_gaji,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GajiAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.tNama.setText(gajiConst.get(i).getNama());
        myViewHolder.tGaji.setText(gajiConst.get(i).getGaji_pokok());
        myViewHolder.tgl1.setText(gajiConst.get(i).getTgl_gaji());

        final String getkey = gajiConst.get(i).getKey();
        final String cabangkey = gajiConst.get(i).getCabang();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, InputGajiAct.class);
                go.putExtra("key",getkey); //Lempar key
                go.putExtra("cabang",cabangkey);
                context.startActivity(go);
            }
        });

        myViewHolder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogview_pinjaman);
                dialog.show();

                Button btnprint = dialog.findViewById(R.id.btnprint);
                btnprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return gajiConst.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tNama,tGaji, tgl1;
        Button print;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            tNama = itemView.findViewById(R.id.tNama);
            tGaji = itemView.findViewById(R.id.tGaji);
            tgl1 = itemView.findViewById(R.id.tgl1);
            print = itemView.findViewById(R.id.print);

        }
    }
}
