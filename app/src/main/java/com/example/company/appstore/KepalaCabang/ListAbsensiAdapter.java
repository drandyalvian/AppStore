package com.example.company.appstore.KepalaCabang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListAbsensiAdapter extends RecyclerView.Adapter<ListAbsensiAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListAbsensiConst> labsensiConsts;
    DatabaseReference reference;
    String key;
    public ListAbsensiAdapter(ArrayList<ListAbsensiConst> p, Context c, String cabang){
        labsensiConsts = p;
        context = c;
        key = cabang;
    }

    @NonNull
    @Override
    public ListAbsensiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ListAbsensiAdapter.MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_absensi,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAbsensiAdapter.MyViewHolder myViewHolder, final int i) {

//        myViewHolder.xket.setText(labsensiConsts.get(i).getKeterangan());
//        myViewHolder.xtgl.setText(labsensiConsts.get(i).getTanggal().toString());
        if (labsensiConsts.get(i).getKeterangan().equals("Alpha")){
            myViewHolder.xtgl.setText(labsensiConsts.get(i).getTanggal().toString());
            myViewHolder.xket.setText(labsensiConsts.get(i).getKeterangan());
            myViewHolder.xket.setTextColor(Color.RED);

        }else {
            myViewHolder.xtgl.setText(labsensiConsts.get(i).getTanggal().toString());
            myViewHolder.xket.setText(labsensiConsts.get(i).getKeterangan());
            myViewHolder.xket.setTextColor(Color.BLUE);
        }

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Hapus")
                        .setMessage("Apakah anda yakin?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ListAbsensiAct.getInstance().deleteAbsen(labsensiConsts.get(i).getKey());
                                Toast.makeText(context, "Berhasil dihapus!", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return labsensiConsts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xtgl, xket;
        Button delete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xtgl = itemView.findViewById(R.id.xtgl);
            xket = itemView.findViewById(R.id.xket);
            delete = itemView.findViewById(R.id.delete);

        }
    }

}
