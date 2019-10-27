package com.example.company.appstore.KepalaCabang;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.company.appstore.R;

import java.util.ArrayList;

public class ListAbsensiAdapter extends RecyclerView.Adapter<ListAbsensiAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListAbsensiConst> labsensiConsts;
    public ListAbsensiAdapter(ArrayList<ListAbsensiConst> p, Context c){
        labsensiConsts = p;
        context = c;
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

        myViewHolder.xtgl.setText(labsensiConsts.get(i).getTanggal());
        if (labsensiConsts.get(i).getKeterangan().equals("Hadir")){
            myViewHolder.xboxhadir.setChecked(true);
        }else{
            myViewHolder.xboxhadir.setChecked(false);
        }

        myViewHolder.xboxhadir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    ListAbsensiAct.getInstance().updateAbsen(labsensiConsts.get(i).getKey(), "Alpha");

                }else{
                    ListAbsensiAct.getInstance().updateAbsen(labsensiConsts.get(i).getKey(), "Hadir");

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return labsensiConsts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xtgl;
        CheckBox xboxhadir;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            xtgl = itemView.findViewById(R.id.xtgl);
            xboxhadir = itemView.findViewById(R.id.xboxhadir);

        }
    }

}
