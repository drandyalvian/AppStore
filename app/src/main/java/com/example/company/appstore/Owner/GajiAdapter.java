package com.example.company.appstore.Owner;

import android.app.Activity;
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
import android.widget.Toast;


import com.example.company.appstore.FileUtils;
import com.example.company.appstore.R;
import com.example.company.appstore.permission.PermissionsActivity;
import com.example.company.appstore.permission.PermissionsChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.company.appstore.permission.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static com.example.company.appstore.permission.PermissionsChecker.REQUIRED_PERMISSION;

public class GajiAdapter  extends RecyclerView.Adapter<GajiAdapter.MyViewHolder> {

    private DatabaseReference reference;

    PermissionsChecker checker;

    Context context, mContext;
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
    public void onBindViewHolder(@NonNull final GajiAdapter.MyViewHolder myViewHolder, final int i) {

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

//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.dialogview_pinjaman);
//                dialog.show();
//
//                Button btnprint = dialog.findViewById(R.id.btnprint);
//                btnprint.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                final String nama = gajiConst.get(i).getNama();
                final String komisi = gajiConst.get(i).getKompensasi();
                final String gajiPokok = gajiConst.get(i).getGaji_pokok();
                final String uangMakan = gajiConst.get(i).getUang_makan();
                final String pinjaman = gajiConst.get(i).getPinjaman();
                final Double gajiTotal = Double.parseDouble(gajiPokok) + Double.parseDouble(uangMakan) + Double.parseDouble(komisi);
                final Double gajiDiterima = gajiTotal - Double.parseDouble(pinjaman);
                final String namaCabang = gajiConst.get(i).getNama_cabang();

                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);



                checker = new PermissionsChecker(context);
                mContext = context.getApplicationContext();
                ExportAct exportAct = new ExportAct();
//                if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
//                    PermissionsActivity.startActivityForResult((Activity) context, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
//                    Toast.makeText(context, FileUtils.getAppPath(mContext)+" "+nama, Toast.LENGTH_SHORT).show();
//                    exportAct.createPdf(FileUtils.getAppPath(mContext) + nama+".pdf", nama, formatRupiah.format(Double.parseDouble(komisi)), formatRupiah.format(Double.parseDouble(gajiPokok)), formatRupiah.format(Double.parseDouble(pinjaman)), formatRupiah.format(Double.parseDouble(uangMakan)), formatRupiah.format(gajiTotal), formatRupiah.format((double)gajiDiterima), namaCabang);
//                } else {
//                    exportAct.createPdf(FileUtils.getAppPath(mContext) + nama+".pdf", nama, formatRupiah.format(Double.parseDouble(komisi)), formatRupiah.format(Double.parseDouble(gajiPokok)), formatRupiah.format(Double.parseDouble(pinjaman)), formatRupiah.format(Double.parseDouble(uangMakan)), formatRupiah.format(gajiTotal), formatRupiah.format((double)gajiDiterima), namaCabang);
//                    Toast.makeText(context, FileUtils.getAppPath(mContext)+" "+nama, Toast.LENGTH_SHORT).show();
//
//
//                }



                Intent intent = new Intent(context, PrintAct.class);
                context.startActivity(intent);
                ((Activity) context).finish();
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
