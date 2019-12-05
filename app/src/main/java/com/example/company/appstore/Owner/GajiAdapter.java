package com.example.company.appstore.Owner;

import android.app.Activity;
import android.app.AlertDialog;
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

import butterknife.BindView;

import static com.example.company.appstore.permission.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static com.example.company.appstore.permission.PermissionsChecker.REQUIRED_PERMISSION;

public class GajiAdapter extends RecyclerView.Adapter<GajiAdapter.MyViewHolder> {

    private DatabaseReference reference;

    PermissionsChecker checker;

    Context context, mContext;
    ArrayList<GajiConst> gajiConst;

    private int totalMasuk;

    private Double gajiPokok, uangMakan, pinjaman, gajiTotal, gajiDiterima, komisi, jumlahGajiPokok, totalUangMakan;

    private String nama, namaCabang;

    private TextView viewTotalCicilan, viewCicilanPinjaman, viewTotalGaji, viewNamaPegawai;

    private Button pdf, print;

    ViewGroup viewGroup;
    View dialogView ;
    AlertDialog.Builder builder;



    public GajiAdapter(ArrayList<GajiConst> p, Context c) {
        context = c;
        gajiConst = p;
    }

    public GajiAdapter(int x){
        totalMasuk = x;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_gaji,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {






        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        myViewHolder.tNama.setText(gajiConst.get(i).getNama());



        final String getkey = gajiConst.get(i).getKey();
        final String cabangkey = gajiConst.get(i).getCabang();

        reference =  FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey).child("Karyawan").child(getkey).child("Count_gaji");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalMasuk = (int)dataSnapshot.getChildrenCount();
                gajiPokok = Double.parseDouble(gajiConst.get(i).getGaji_pokok());
                pinjaman = Double.parseDouble(gajiConst.get(i).getPinjaman());
                uangMakan = Double.parseDouble(gajiConst.get(i).getUang_makan());
                komisi = Double.parseDouble(gajiConst.get(i).getKompensasi());

                totalUangMakan = totalMasuk * uangMakan;
                jumlahGajiPokok = gajiPokok * totalMasuk;
                gajiTotal = totalUangMakan + komisi + jumlahGajiPokok;
                gajiDiterima = gajiTotal - pinjaman;

                nama = gajiConst.get(i).getNama();

                namaCabang = gajiConst.get(i).getNama_cabang();

                myViewHolder.tGaji.setText(formatRupiah.format(gajiTotal));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(context, InputGajiAct.class);
                go.putExtra("key", getkey); //Lempar key
                go.putExtra("cabang", cabangkey);
                context.startActivity(go);
            }
        });

        myViewHolder.payroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((GajiAct)context).printGaji(view, nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));
                viewGroup = myViewHolder.itemView.findViewById(android.R.id.content);
                dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_gaji, viewGroup, false);
                builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                viewTotalCicilan = dialogView.findViewById(R.id.viewTotalCicilan);
                viewCicilanPinjaman = dialogView.findViewById(R.id.viewCicilanPinjaman);
                viewTotalGaji = dialogView.findViewById(R.id.viewTotalGaji);
                viewNamaPegawai = dialogView.findViewById(R.id.namaPegawai);

                print = dialogView.findViewById(R.id.btnPrint);
                pdf = dialogView.findViewById(R.id.btnPdf);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                viewNamaPegawai.setText(gajiConst.get(i).getNama());

                viewTotalGaji.setText(formatRupiah.format(gajiTotal));

                viewTotalCicilan.setText(formatRupiah.format(Double.parseDouble(gajiConst.get(i).getPinjaman())));


            }
        });



//        myViewHolder.pdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checker = new PermissionsChecker(context);
//                mContext = context.getApplicationContext();
//                ExportAct exportAct = new ExportAct();
//                if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
//                    PermissionsActivity.startActivityForResult((Activity) context, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
//                    Toast.makeText(context, "File Disimpan : "+FileUtils.getAppPath(mContext) + " " + nama+".pdf", Toast.LENGTH_LONG).show();
//                    exportAct.createPdf(FileUtils.getAppPath(mContext) + nama + ".pdf", nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));
//                } else {
//                    exportAct.createPdf(FileUtils.getAppPath(mContext) + nama + ".pdf", nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));
//                    Toast.makeText(context, "File Disimpan : "+FileUtils.getAppPath(mContext) + " " + nama +".pdf", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return gajiConst.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tNama, tGaji;
        Button payroll;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tNama = itemView.findViewById(R.id.tNama);
            tGaji = itemView.findViewById(R.id.tGaji);
            payroll = itemView.findViewById(R.id.btnPayroll);





        }
    }
}
