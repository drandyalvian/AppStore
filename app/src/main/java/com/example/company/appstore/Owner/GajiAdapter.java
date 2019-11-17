package com.example.company.appstore.Owner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        final String getkey = gajiConst.get(i).getKey();

//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent go = new Intent(context, ListAbsensiAct.class);
//                go.putExtra("key",getkey); //Lempar key
//                context.startActivity(go);
//            }
//        });

        //get nominal gaji
        String cabang = gajiConst.get(i).getNama_cabang();
        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabang).child("Karyawan")
                .child(gajiConst.get(i).getKey()).child("Gaji").child("Rincian");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myViewHolder.tGaji.setText(dataSnapshot.child("gaji_pokok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return gajiConst.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tNama,  tGaji;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            tNama = itemView.findViewById(R.id.tNama);
            tGaji = itemView.findViewById(R.id.tGaji);

        }
    }
}
