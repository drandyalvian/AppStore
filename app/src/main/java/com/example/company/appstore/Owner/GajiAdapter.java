package com.example.company.appstore.Owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.FileUtils;
import com.example.company.appstore.R;
import com.example.company.appstore.permission.PermissionsActivity;
import com.example.company.appstore.permission.PermissionsChecker;
import com.example.company.appstore.sqllite.SqlLiteHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.example.company.appstore.permission.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static com.example.company.appstore.permission.PermissionsChecker.REQUIRED_PERMISSION;

public class GajiAdapter extends RecyclerView.Adapter<GajiAdapter.MyViewHolder> {

    private DatabaseReference reference;

    PermissionsChecker checker;

    Context context, mContext;
    ArrayList<GajiConst> gajiConst;

    private int totalMasuk, cicilanKe;

    private Double gajiPokok, uangMakan, pinjaman, gajiTotal, gajiDiterima, komisi, jumlahGajiPokok, totalUangMakan, sisaPinjaman, cicilan;

    private String nama, namaCabang, fixGajiTotal;

    private TextView viewTotalCicilan, viewCicilanPinjaman, viewTotalGaji, viewNamaPegawai;

    private EditText kurangPinjaman;

    private Button pdf, print, save;

    ViewGroup viewGroup;
    View dialogView ;
    AlertDialog.Builder builder;

    private SqlLiteHelper sqlLiteHelper;

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
        sqlLiteHelper = new SqlLiteHelper(context);
        return new MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_list_gaji,
                viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        DecimalFormat df = new DecimalFormat("###.#");

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

                namaCabang = gajiConst.get(i).getNama_cabang();

                myViewHolder.tGaji.setText(formatRupiah.format(gajiTotal));


                sqlLiteHelper.insertToGaji(gajiConst.get(i).getNama().toString(), gajiTotal, totalMasuk, pinjaman, 0.0,0, komisi, gajiPokok, uangMakan, gajiDiterima, namaCabang, totalUangMakan );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sqlLiteHelper.insertToGaji(nama, gajiTotal, totalMasuk, pinjaman, 0,0);
                Intent go = new Intent(context, InputGajiAct.class);
                go.putExtra("key", getkey); //Lempar key
                go.putExtra("cabang", cabangkey);
                context.startActivity(go);
            }
        });

        myViewHolder.payroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewGroup = myViewHolder.itemView.findViewById(android.R.id.content);
                dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_gaji, viewGroup, false);
                builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                viewTotalCicilan = dialogView.findViewById(R.id.viewTotalCicilan);
                viewCicilanPinjaman = dialogView.findViewById(R.id.viewCicilanPinjaman);
                viewTotalGaji = dialogView.findViewById(R.id.viewTotalGaji);
                viewNamaPegawai = dialogView.findViewById(R.id.namaPegawai);
//                kurangPinjaman = dialogView.findViewById(R.id.kurangPinjaman);

                print = dialogView.findViewById(R.id.btnPrint);
                pdf = dialogView.findViewById(R.id.btnPdf);
//                save = dialogView.findViewById(R.id.btnSavePinjaman);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
                Cursor myCursor = db.rawQuery("SELECT * FROM "+ "gaji" +" WHERE "+"nama"+" = '" + gajiConst.get(i).getNama().toString() + "'",null);
                myCursor.moveToFirst();


                if (myCursor.getCount()>0) {
                    myCursor.moveToPosition(0);
                    viewNamaPegawai.setText(myCursor.getString(1).toString());
                    nama = myCursor.getString(1).toString();
                    gajiTotal = Double.parseDouble(myCursor.getString(2));
                    totalMasuk = Integer.parseInt(myCursor.getString(3));
                    pinjaman = Double.parseDouble(myCursor.getString(4));
                    cicilan = Double.parseDouble(myCursor.getString(5));
                    cicilanKe = Integer.parseInt(myCursor.getString(6));
                    komisi = Double.parseDouble(myCursor.getString(7));
                    gajiPokok = Double.parseDouble(myCursor.getString(8));
                    uangMakan = Double.parseDouble(myCursor.getString(9));
                    gajiDiterima = Double.parseDouble(myCursor.getString(10));
                    namaCabang = myCursor.getString(11);
                    totalUangMakan = Double.parseDouble(myCursor.getString(12));

                    jumlahGajiPokok = totalMasuk * gajiPokok;

                    if (Double.parseDouble(myCursor.getString(4)) == 0){
                        save.setEnabled(false);
                    }else{
                        save.setEnabled(true);
                    }
                }



                viewTotalCicilan.setText(formatRupiah.format(pinjaman));
                viewTotalGaji.setText(formatRupiah.format(gajiTotal));

                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checker = new PermissionsChecker(context);
                        mContext = context.getApplicationContext();
                        ExportAct exportAct = new ExportAct();
                        if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
                            PermissionsActivity.startActivityForResult((Activity) context, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
                            Toast.makeText(context, "File Disimpan : "+FileUtils.getAppPath(mContext) + " " + nama+".pdf", Toast.LENGTH_LONG).show();
                            exportAct.createPdf(FileUtils.getAppPath(mContext) + nama + ".pdf", nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));
                        } else {
                            exportAct.createPdf(FileUtils.getAppPath(mContext) + nama + ".pdf", nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));
                            Toast.makeText(context, "File Disimpan : "+FileUtils.getAppPath(mContext) + " " + nama +".pdf", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GajiAct)context).printGaji(view, nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey).child("Karyawan").child(getkey);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (pinjaman < Integer.parseInt(kurangPinjaman.getText().toString())){
                                    Toast.makeText(context, "Nominal melebihi pinjaman!", Toast.LENGTH_SHORT).show();
                                    kurangPinjaman.setError("Melebihi nominal pinjaman");
                                    kurangPinjaman.requestFocus();
                                }else{
                                    sisaPinjaman = pinjaman - Integer.parseInt(kurangPinjaman.getText().toString());
                                    pinjaman = sisaPinjaman;
                                    dataSnapshot.getRef().child("pinjaman").setValue(df.format(sisaPinjaman).toString());
                                }
                                viewTotalCicilan.setText(formatRupiah.format(pinjaman));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(context, "clicked!", Toast.LENGTH_SHORT).show();
                    }
                });


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
