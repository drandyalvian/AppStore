package com.example.company.appstore.Owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.FileUtils;
import com.example.company.appstore.KepalaCabang.DashbordAct;
import com.example.company.appstore.KepalaCabang.LoginAct;
import com.example.company.appstore.R;
import com.example.company.appstore.permission.PermissionsActivity;
import com.example.company.appstore.permission.PermissionsChecker;
import com.example.company.appstore.sqllite.SqlLiteHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.os.Handler;

import butterknife.BindView;

import static com.example.company.appstore.permission.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static com.example.company.appstore.permission.PermissionsChecker.REQUIRED_PERMISSION;

public class GajiAdapter extends RecyclerView.Adapter<GajiAdapter.MyViewHolder> {

    private DatabaseReference reference, reference2;

    PermissionsChecker checker;

    Context context, mContext;
    ArrayList<GajiConst> gajiConst;

    private int totalMasuk, cicilanKe;

    private Double gajiPokok, gajiLembur, uangMakan, pinjaman, gajiTotal, gajiDiterima, komisi, jumlahGajiPokok,totalLembur, totalUangMakan, sisaPinjaman, cicilan;

    private String nama, namaCabang, fixGajiTotal, angsuranPasti, pinjamanChecked;

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

        reference =  FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey).child("Karyawan").child(getkey).child("Count_gaji").child("Tanggal");
        Query query1 = reference.orderByChild("keterangan").equalTo("Hadir");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("count", String.valueOf(dataSnapshot.getChildrenCount()));

                totalMasuk = (int)dataSnapshot.getChildrenCount();
                gajiPokok = Double.parseDouble(gajiConst.get(i).getGaji_pokok());
                gajiLembur = Double.parseDouble(gajiConst.get(i).getGaji_lembur());
                pinjaman = Double.parseDouble(gajiConst.get(i).getPinjaman());
                uangMakan = Double.parseDouble(gajiConst.get(i).getUang_makan());
                komisi = Double.parseDouble(gajiConst.get(i).getKompensasi());
                angsuranPasti = gajiConst.get(i).getAngsuran_pasti();
                pinjamanChecked = gajiConst.get(i).getPinjaman();



                totalUangMakan = totalMasuk * uangMakan;
                jumlahGajiPokok = gajiPokok * totalMasuk;
                totalLembur = gajiLembur * totalMasuk;
                gajiTotal = totalUangMakan + komisi + jumlahGajiPokok + totalLembur;
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

                final String[] checkSisaPinjaman = {""};
                final String[] checkAngsuran = {""};

                viewGroup = myViewHolder.itemView.findViewById(android.R.id.content);
                dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_gaji, viewGroup, false);
                builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                viewTotalCicilan = dialogView.findViewById(R.id.viewTotalCicilan);
                TextView viewTextSisaPinjaman = dialogView.findViewById(R.id.viewTextSisaPinjaman);
//                viewCicilanPinjaman = dialogView.findViewById(R.id.viewCicilanPinjaman);
                TextView viewReset = dialogView.findViewById(R.id.viewReset);
                viewTotalGaji = dialogView.findViewById(R.id.viewTotalGaji);
                viewNamaPegawai = dialogView.findViewById(R.id.namaPegawai);
                CheckBox check_setor_pinjaman = dialogView.findViewById(R.id.check_setor_pinjaman);

                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                String dateString = sdf.format(date);



                print = dialogView.findViewById(R.id.btnPrint);
                pdf = dialogView.findViewById(R.id.btnPdf);
//                save = dialogView.findViewById(R.id.btnSavePinjaman);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                SQLiteDatabase db = sqlLiteHelper.getReadableDatabase();
                Cursor myCursor = db.rawQuery("SELECT * FROM "+ "gaji" +" WHERE "+"nama"+" = '" + gajiConst.get(i).getNama().toString() + "'",null);
                myCursor.moveToFirst();

                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey).child("Karyawan").child(getkey);
                //mengembalikan checked angsuran menjadi 0
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("checked_angsuran").setValue(String.valueOf(0));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                checker = new PermissionsChecker(context);
                checker.lacksPermissions(REQUIRED_PERMISSION);
                //set data dialog view
                reference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int Gpokok = Integer.parseInt(dataSnapshot.child("gaji_pokok").getValue().toString());
                        int Umakan = Integer.parseInt(dataSnapshot.child("uang_makan").getValue().toString());
                        int Glembur = Integer.parseInt(dataSnapshot.child("gaji_lembur").getValue().toString());
                        int tKomisi = Integer.parseInt(dataSnapshot.child("kompensasi").getValue().toString());

                        viewNamaPegawai.setText(dataSnapshot.child("nama").getValue().toString());

                        DatabaseReference reference3 =  FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey)
                                .child("Karyawan").child(getkey).child("Count_gaji").child("Tanggal");
                        Query query2 = reference3.orderByChild("keterangan").equalTo("Hadir");
                        //
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int countG = (int) dataSnapshot.getChildrenCount();

                                viewTotalGaji.setText(formatRupiah.format(((Gpokok*countG)+(Umakan*countG)+(Glembur*countG)+tKomisi)));


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });

                //kondisi checked or uchecked angsuran
                reference2 =  FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey).child("Karyawan").child(getkey);
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        viewTotalCicilan.setText(formatRupiah.format(Double.parseDouble(dataSnapshot.child("pinjaman").getValue().toString())));
//                        viewCicilanPinjaman.setText("Cicilan ke : "+dataSnapshot.child("cicilan")
//                                .getValue().toString());

                        int aPinjaman = Integer.valueOf(dataSnapshot.child("pinjaman").getValue().toString());
                        int aAngsuran = Integer.valueOf(dataSnapshot.child("checked_angsuran").getValue().toString());
                        checkSisaPinjaman[0] = String.valueOf(aPinjaman);
                        checkAngsuran[0] = String.valueOf(aAngsuran);
                        check_setor_pinjaman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {


                            }
                        });


                        if (aPinjaman != 0 ){

                            check_setor_pinjaman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                                    int aPinjaman = Integer.valueOf(dataSnapshot.child("pinjaman").getValue().toString());
                                    int aAngsuran = Integer.valueOf(dataSnapshot.child("angsuran_pasti").getValue().toString());

//                                    int aCicilan = Integer.valueOf(dataSnapshot.child("cicilan").getValue().toString());

                                    if(checked){

                                        int hasil = aPinjaman - aAngsuran;
                                        Log.d("checkHasil", String.valueOf(hasil));
                                        checkSisaPinjaman[0] = String.valueOf(hasil);
                                        checkAngsuran[0] = String.valueOf(aAngsuran);
                                        viewTotalCicilan.setText(formatRupiah.format(Double.parseDouble(String.valueOf(hasil))));


                                    }else{
                                        int hasil = Integer.parseInt(checkSisaPinjaman[0]) + Integer.parseInt(checkAngsuran[0]);
                                        Log.d("checkHasil", String.valueOf(hasil));
                                        checkSisaPinjaman[0] = String.valueOf(hasil);
                                        checkAngsuran[0] = String.valueOf(aAngsuran);
                                        viewTotalCicilan.setText(formatRupiah.format(Double.parseDouble(String.valueOf(hasil))));

                                    }

                                }
                            });

                        }else if (aPinjaman == 0 && check_setor_pinjaman.isChecked()){

                            viewTextSisaPinjaman.animate().alpha(1).setDuration(200).start();
                            check_setor_pinjaman.animate().alpha(1).setDuration(200).start();
                            viewTotalCicilan.animate().alpha(1).setDuration(200).start();

                            check_setor_pinjaman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                                    int aPinjaman = Integer.valueOf(dataSnapshot.child("pinjaman").getValue().toString());
                                    int aAngsuran = Integer.valueOf(dataSnapshot.child("angsuran_pasti").getValue().toString());
//                                    int aCicilan = Integer.valueOf(dataSnapshot.child("cicilan").getValue().toString());

                                    if(checked){
                                        int hasil = aPinjaman - aAngsuran;
//                                        int plusCicilan = aCicilan+1;
                                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("pinjaman").setValue(String.valueOf(hasil));
                                                dataSnapshot.getRef().child("checked_angsuran").setValue(String.valueOf(aAngsuran));
//                                                dataSnapshot.getRef().child("cicilan").setValue(String.valueOf(plusCicilan));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
//                                    Log.d("kurang", String.valueOf(angka-20000));

                                    }else{
                                        int hasil = aPinjaman + aAngsuran;
//                                        int minCicilan = aCicilan-1;
                                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("pinjaman").setValue(String.valueOf(hasil));
                                                dataSnapshot.getRef().child("checked_angsuran").setValue(String.valueOf(0));
//                                                dataSnapshot.getRef().child("cicilan").setValue(String.valueOf(minCicilan));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
//                                    Log.d("tmabah", String.valueOf(angka+20000));
                                    }

                                }
                            });

                        }
                        else {

                            viewTextSisaPinjaman.animate().alpha(0).setDuration(200).start();
                            check_setor_pinjaman.animate().alpha(0).setDuration(200).start();
                            viewTotalCicilan.animate().alpha(0).setDuration(200).start();

                            check_setor_pinjaman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                                    if (checked){

                                        Toast.makeText(context, "Sisa pinjaman 0", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        alertDialog.hide();

                        mContext = context.getApplicationContext();
                        ExportAct exportAct = new ExportAct();

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String pdfUmakan;
                                String pdfnama = dataSnapshot.child("nama").getValue().toString();

                                String pdfnama_cabang = dataSnapshot.child("nama_cabang").getValue().toString();
                                String pdfGpokok = formatRupiah.format(Double.parseDouble(dataSnapshot.child("gaji_pokok").getValue().toString()));
                                int pdfGpokok2 = Integer.parseInt(dataSnapshot.child("gaji_pokok").getValue().toString());
                                String pdfGLembur = formatRupiah.format(Double.parseDouble(dataSnapshot.child("gaji_lembur").getValue().toString()));
                                int pdfGLembur2 = Integer.parseInt(dataSnapshot.child("gaji_lembur").getValue().toString());
                                String pdfKomisi = formatRupiah.format(Double.parseDouble(dataSnapshot.child("kompensasi").getValue().toString()));
                                int pdfGKomisi2 = Integer.parseInt(dataSnapshot.child("kompensasi").getValue().toString());
//
                                String pdfCheckedAngsuran = dataSnapshot.child("checked_angsuran").getValue().toString();

                                if (dataSnapshot.child("uang_makan").getValue().equals(0) || dataSnapshot.child("uang_makan").getValue().equals("0")) {
                                    pdfUmakan = "0";
                                }else{
                                    pdfUmakan = formatRupiah.format(Double.parseDouble(dataSnapshot.child("uang_makan").getValue().toString()));
                                }

                                int pdfUmakan2 = Integer.valueOf(dataSnapshot.child("uang_makan").getValue().toString());
                                int pdfAngsuran;
                                if (dataSnapshot.child("checked_angsuran").getValue().equals(0) || dataSnapshot.child("checked_angsuran").getValue().equals("0")) {
                                    pdfAngsuran = 0;
                                }else{
                                    pdfAngsuran = Integer.parseInt(dataSnapshot.child("checked_angsuran").getValue().toString());
                                }
//                                String pdfCicilan = dataSnapshot.child("cicilan").getValue().toString();
                                String pdfPinjaman;
                                if (dataSnapshot.child("pinjaman").getValue().equals("0") || dataSnapshot.child("pinjaman").getValue().equals(0)) {
                                    pdfPinjaman = "0";
                                }else{
                                    pdfPinjaman = formatRupiah.format(Double.parseDouble(dataSnapshot.child("pinjaman").getValue().toString()));
                                }
                                DatabaseReference reference3 =  FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey)
                                        .child("Karyawan").child(getkey).child("Count_gaji").child("Tanggal");
                                Query query3 = reference3.orderByChild("keterangan").equalTo("Hadir");

                                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int countG = (int) dataSnapshot.getChildrenCount();
                                        int pdfGtotal = ((pdfGpokok2*countG)+(pdfUmakan2*countG)+(pdfGLembur2*countG))+pdfGKomisi2;
                                        int pdfGditerima = (((pdfGpokok2*countG)+(pdfUmakan2*countG)+(pdfGLembur2*countG))+pdfGKomisi2)-Integer.parseInt(checkAngsuran[0]);
//                                        Log.d("count", String.valueOf(dataSnapshot.getChildrenCount()+formatRupiah.format(pdfGpokok2*countG)));

                                        if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
                                            PermissionsActivity.startActivityForResult((Activity) context, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);

                                            exportAct.createPdf(FileUtils.getAppPath(mContext) + pdfnama + ".pdf", pdfnama, pdfKomisi, formatRupiah.format(pdfGLembur2*countG), pdfGpokok,
                                                    formatRupiah.format(Double.parseDouble(checkAngsuran[0])), pdfUmakan, formatRupiah.format(pdfGtotal), formatRupiah.format(pdfGditerima),
                                                    ""+pdfnama_cabang, Integer.toString(countG), formatRupiah.format(pdfUmakan2*countG),
                                                    formatRupiah.format(pdfGpokok2*countG), "", checkSisaPinjaman[0], checkAngsuran[0], dateString);

                                            Toast.makeText(context, "File Disimpan : "+FileUtils.getAppPath(mContext) + " " + pdfnama+".pdf", Toast.LENGTH_SHORT).show();

                                        } else {
                                            exportAct.createPdf(FileUtils.getAppPath(mContext) + pdfnama + ".pdf", pdfnama, pdfKomisi, formatRupiah.format(pdfGLembur2*countG), pdfGpokok,
                                                    formatRupiah.format(Double.parseDouble(checkAngsuran[0])), pdfUmakan, formatRupiah.format(pdfGtotal), formatRupiah.format(pdfGditerima),
                                                    ""+pdfnama_cabang, Integer.toString(countG), formatRupiah.format(pdfUmakan2*countG),
                                                    formatRupiah.format(pdfGpokok2*countG), "", checkSisaPinjaman[0], checkAngsuran[0], dateString);

                                            Toast.makeText(context, "File Disimpan : "+FileUtils.getAppPath(mContext) + " " + pdfnama +".pdf", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }

                });

                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("pinjaman").setValue(checkSisaPinjaman[0]);
                                dataSnapshot.getRef().child("checked_angsuran").setValue(checkAngsuran[0]);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String printnama = dataSnapshot.child("nama").getValue().toString();
                                String printnama_cabang = dataSnapshot.child("nama_cabang").getValue().toString();
                                String printGpokok = formatRupiah.format(Double.parseDouble(dataSnapshot.child("gaji_pokok").getValue().toString()));
                                int printGpokok2 = Integer.parseInt(dataSnapshot.child("gaji_pokok").getValue().toString());
                                String printGLembur = formatRupiah.format(Double.parseDouble(dataSnapshot.child("gaji_lembur").getValue().toString()));
                                int printGLembur2 = Integer.parseInt(dataSnapshot.child("gaji_lembur").getValue().toString());
                                String printKomisi = formatRupiah.format(Double.parseDouble(dataSnapshot.child("kompensasi").getValue().toString()));
                                int printKomisi2 = Integer.parseInt(dataSnapshot.child("kompensasi").getValue().toString());
                                Integer printUmakan = Integer.parseInt(dataSnapshot.child("uang_makan").getValue().toString());
                                int printUmakan2 = Integer.valueOf(dataSnapshot.child("uang_makan").getValue().toString());
                                int printAngsuran = Integer.parseInt(dataSnapshot.child("checked_angsuran").getValue().toString());
                                String printCheckedAngsuran = dataSnapshot.child("checked_angsuran").getValue().toString();

//                                String printCicilan = dataSnapshot.child("cicilan").getValue().toString();
                                String printPinjaman = formatRupiah.format(Double.parseDouble(dataSnapshot.child("pinjaman").getValue().toString()));
                                Log.d("angsurchek", printCheckedAngsuran+""+printPinjaman);
//                                    String printPinjaman = formatRupiah.format(Double.parseDouble(dataSnapshot.child("pinjaman").getValue().toString()));
                                DatabaseReference reference3 =  FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabangkey)
                                        .child("Karyawan").child(getkey).child("Count_gaji").child("Tanggal");
                                Query query3 = reference3.orderByChild("keterangan").equalTo("Hadir");
                                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int countG = (int) dataSnapshot.getChildrenCount();
                                        int printGtotal = ((printGpokok2*countG)+(printUmakan2*countG)+(printGLembur2*countG))+printKomisi2;
                                        int printGditerima = ((printGpokok2*countG)+(printUmakan2*countG)+(printGLembur2*countG)+printKomisi2)-Integer.parseInt(checkAngsuran[0]);
//                                        Log.d("count", String.valueOf(dataSnapshot.getChildrenCount()+formatRupiah.format(pdfGpokok2*countG)));

                                        ((GajiAct)context).printGaji(view, printnama, printKomisi, formatRupiah.format(printGLembur2*countG), printGpokok, formatRupiah.format(Double.parseDouble(checkAngsuran[0])),
                                                printUmakan, formatRupiah.format(printGtotal), formatRupiah.format(printGditerima), ""+printnama_cabang,
                                                Integer.toString(countG), printUmakan2*countG, formatRupiah.format(printGpokok2*countG), "", checkSisaPinjaman[0], checkAngsuran[0], dateString);

                                        Log.d("checked", printCheckedAngsuran);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


//                        ((GajiAct)context).printGaji(view, nama, formatRupiah.format(komisi), formatRupiah.format(gajiPokok), formatRupiah.format(pinjaman), formatRupiah.format(uangMakan), formatRupiah.format(gajiTotal), formatRupiah.format(gajiDiterima), ""+namaCabang, Integer.toString(totalMasuk), formatRupiah.format(totalUangMakan), formatRupiah.format(jumlahGajiPokok));

                    }
                });




            }
        });




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
