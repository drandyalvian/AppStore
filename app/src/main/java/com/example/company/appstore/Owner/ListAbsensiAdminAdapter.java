package com.example.company.appstore.Owner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.ListAbsensiAct;
import com.example.company.appstore.KepalaCabang.ListAbsensiConst;
import com.example.company.appstore.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ListAbsensiAdminAdapter extends RecyclerView.Adapter<ListAbsensiAdminAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListAbsensiConst> labsensiConsts;
    DatabaseReference reference;
    String key;
    public ListAbsensiAdminAdapter(ArrayList<ListAbsensiConst> p, Context c, String cabang){
        labsensiConsts = p;
        context = c;
        key = cabang;
    }

    @NonNull
    @Override
    public ListAbsensiAdminAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ListAbsensiAdminAdapter.MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_absensi,
                viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAbsensiAdminAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.xtgl.setText(labsensiConsts.get(i).getTanggal());

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Hapus")
                        .setMessage("Apakah anda yakin?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ListAbsensiAdmin.getInstance().deleteAbsen(labsensiConsts.get(i).getKey());
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

        TextView xtgl;
        Button delete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xtgl = itemView.findViewById(R.id.xtgl);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
