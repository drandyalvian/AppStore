package com.example.company.appstore.Owner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.AbsensiConst;
import com.example.company.appstore.KepalaCabang.LaporanUangConst;
import com.example.company.appstore.KepalaCabang.ListAbsensiConst;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import android.os.Handler;;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class DataKaryawanAct extends AppCompatActivity implements DataKaryawanAdapter.FirebaseDataListener {


    Button back, btnplus2;
    LinearLayout profilk;
    EditText txtsearch;

    String cabangku;

    DatabaseReference reference, reference2, reference3, reference4, reference5, reference6;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataKaryawanConst> dataKaryawanConsts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_karyawan);
        ButterKnife.bind(this);

        back = findViewById(R.id.back);
        txtsearch = findViewById(R.id.txtsearch);

        btnplus2 = findViewById(R.id.btnplus2);

        rvView = (RecyclerView) findViewById(R.id.karyawan_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        final String cabang = getIntent().getStringExtra("cabang");
        cabangku = cabang;

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");

        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("KepalaCabang").child(cabang);


        reference5 = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataKaryawanConsts = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    DataKaryawanConst kConst = dataSnapshot1.getValue(DataKaryawanConst.class);

                    kConst.setKey(dataSnapshot1.getKey());

                    dataKaryawanConsts.add(kConst);
//                    Log.d("dataku", String.valueOf(kConst));

                }

                adapter = new DataKaryawanAdapter(dataKaryawanConsts, DataKaryawanAct.this, DataKaryawanAct.this);
                rvView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //pencarian data

        //membuat awal kata auto huruf besar (senistive search)


        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {

                    search("");

                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this, OwnerDashbordAct.class);
                startActivity(go);

            }
        });

        btnplus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(DataKaryawanAct.this, AddKaryawanAct.class);
                go.putExtra("cabang", cabang);
                startActivity(go);

            }
        });
    }


    @Override
    public void onDeleteData(DataKaryawanConst dataKaryawanConst, int i) {

        if (reference != null) {

//            reference.child(DataKaryawanConst.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//
//                    Toast.makeText(DataKaryawanAct.this,"success delete", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }


    //search fungsi
    private void search(String s) {


        Query query = reference.orderByChild("nama")
                .startAt(s)
                .endAt(s + "\uf8ff"); //

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    dataKaryawanConsts.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        final DataKaryawanConst dataK = dss.getValue(DataKaryawanConst.class);
                        dataKaryawanConsts.add(dataK);


                    }

                    DataKaryawanAdapter adapter = new DataKaryawanAdapter(dataKaryawanConsts, DataKaryawanAct.this, DataKaryawanAct.this);
                    rvView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btnRecap)
    public void onViewClicked() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView2 = LayoutInflater.from(this).inflate(R.layout.dialogview_recap, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView2);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText ftahun = (EditText) dialogView2.findViewById(R.id.ftahun);
        TextView dcabang = (TextView) dialogView2.findViewById(R.id.dcabang);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dcabang.setText(dataSnapshot.child("nama_cabang").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Spinner fbulan = (Spinner) dialogView2.findViewById(R.id.fbulan);

        final ArrayAdapter pilihFilter = ArrayAdapter.createFromResource(this, R.array.pilih_filter, android.R.layout.simple_spinner_dropdown_item);
        fbulan.setAdapter(pilihFilter);


        Button btnSave2 = (Button) dialogView2.findViewById(R.id.btnsaves);
        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave2.setText("TUNGGU..");
                createExcelSheet(fbulan.getSelectedItem().toString(), ftahun.getText().toString(), cabangku);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.hide();
                    }
                }, 5000);


            }
        });
    }

    private void createExcelSheet(String fbulan, String ftahun, String cabang) {
//        String Fnamexls="dataKaryawan"+System.currentTimeMillis()+ ".xls";
        String Fnamexls = cabangku + "_" + fbulan + ftahun + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/AppStore/recap");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(DataKaryawanAct.this, "storage/AppSore/recap/" + Fnamexls, Toast.LENGTH_LONG).show();
            }
        }, 5000);

//        Toast.makeText(this, "storage/AppSore/recap/" + Fnamexls, Toast.LENGTH_LONG).show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                WritableWorkbook workbook;
                try {
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    //workbook.createSheet("Report", 0);
                    WritableSheet sheet = workbook.createSheet(cabangku + " " + fbulan + ftahun, 0);
                    Label labela0 = new Label(0, 2, "Nama"); //COLOM A, ROW 2
                    Label labela1 = new Label(0, 3, " ");

                    int i = 0;
                    int a = 4;
                    int b = 1;
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    int length = (int) dataSnapshot.getChildrenCount();
                    String[] sampleString = new String[length];

                    do{

                        final int c = b;
                        final int d = a;
                        final int x = i;

                        sampleString[i] = iterator.next().getKey().toString();
                        Label labeln = new Label(0, a, sampleString[i]);
                        Log.d(Integer.toString(i), sampleString[i]);



//                        reference3 = FirebaseDatabase.getInstance().getReference()
//                                .child("Cabang").child(cabang).child("Karyawan").child(sampleString[i]).child("Absensi");
                        reference3 = FirebaseDatabase.getInstance().getReference()
                                .child("Cabang").child(cabang).child("Recap").child(sampleString[i]).child(fbulan+" "+ftahun);

//                        Query query1 = reference3.orderByChild("keterangan").equalTo("Hadir");

                        Query query2 = reference.orderByChild("key").equalTo(sampleString[i]);

//                        Query query3 = FirebaseDatabase.getInstance().getReference()
//                                .child("Cabang").child(cabang).child("Karyawan").child(sampleString[0]).child("Absensi")
//                                .orderByChild("filter").equalTo(fbulan+" "+ftahun);

                        reference6 = FirebaseDatabase.getInstance().getReference()
                                .child("Cabang").child(cabang).child("Recap").child(sampleString[0]).child(fbulan+" "+ftahun);


                        reference3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                List<ListAbsensiConst> users2 = new ArrayList<>();
                                while (iterator.hasNext()) {
                                    DataSnapshot dataSnapshotChild = iterator.next();
                                    ListAbsensiConst user2 = dataSnapshotChild.getValue(ListAbsensiConst.class);
                                    users2.add(user2);
                                }

                                int lengthku = (int) dataSnapshot.getChildrenCount();
                                String[] sampleString = new String[lengthku];

                                for (int i = 0, i2 = 1; i < length; i++, i2++) {
                                    try {

                                        for (int j = 0, j2 = 1; j < lengthku; j++, j2++) {
                                            sampleString[j] = users2.get(i).getKeterangan();
                                            Label labela1 = new Label(i2, d, sampleString[j]);
                                            Log.d("kolom"+i2+"baris"+d,sampleString[j] );

                                            try {
                                                sheet.addCell(labela1);
                                            } catch (WriteException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }catch (Exception e){
                                        Log.d("erorku", e.getMessage());
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        reference6.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                List<ListAbsensiConst> users1 = new ArrayList<>();
                                while (iterator.hasNext()) {
                                    DataSnapshot dataSnapshotChild = iterator.next();
                                    ListAbsensiConst user1 = dataSnapshotChild.getValue(ListAbsensiConst.class);
                                    users1.add(user1);
                                }

                                int lengthku = (int) dataSnapshot.getChildrenCount();
                                String[] sampleString = new String[lengthku];
                                int clength = lengthku+1;
                                Log.d("Gaji " ,"kolom"+clength+"baris"+2 );
                                Label labelgaji = new Label(clength, 2, "Gaji Total");


                                for (int j = 0, j2 = 1; j < lengthku; j++, j2++) {
//                                    sampleString[j] = iterator.next().getKey().substring(0,2);
                                    sampleString[j] = users1.get(j).getKeterangan();
                                    Label labela2 = new Label(j2, 4, sampleString[j]);
                                    Label labeltgl = new Label(j2, 3, String.valueOf(j2));
                                    Log.d("kolom"+j2+"baris"+4,sampleString[j] );

                                    try {
                                        sheet.addCell(labela2);
                                        sheet.addCell(labeltgl);
                                        sheet.addCell(labelgaji);
                                    } catch (WriteException e) {
                                        e.printStackTrace();
                                    }
//                            System.out.print("kolom"+j2+"baris"+i2+" "+sampleString[j]);
                                }

//                               total gaji
                                reference5.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                                        List<GajiConst> users = new ArrayList<>();
                                        while (dataSnapshots.hasNext()) {
                                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                                            GajiConst user = dataSnapshotChild.getValue(GajiConst.class);
                                            users.add(user);
                                        }

                                        try {

                                            for (int i = 0, i2 = 4 ; i < users.size(); i++, i2++) {

                                                final int x = i;
                                                final int x2 = i2;
                                                int angkaGaji = Integer.parseInt(users.get(i).getGaji_pokok());
                                                int angkaUangMakan = Integer.parseInt(users.get(i).getUang_makan());
                                                int angkaLembur = Integer.parseInt(users.get(i).getGaji_lembur());
                                                int angkaKomisi = Integer.parseInt(users.get(i).getKompensasi());

                                                DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                                                        .child(cabang).child("Recap").child(users.get(i).getKey_name());
                                                int finalI = i;
                                                reference7.child(fbulan+" "+ftahun).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                                        List<LaporanUangConst> komisis = new ArrayList<>();
                                                        while (iterator.hasNext()) {
                                                            DataSnapshot dataSnapshotChild = iterator.next();
                                                            LaporanUangConst nkomisi = dataSnapshotChild.getValue(LaporanUangConst.class);
                                                            komisis.add(nkomisi);
                                                        }

                                                        int lengthuang = (int) dataSnapshot.getChildrenCount();

                                                        int hasil = 0;

                                                        try {

                                                            for (int u = 0 ; u < lengthuang; u++){

                                                                hasil +=  Integer.valueOf(komisis.get(u).getNominal());


                                                            }

                                                        }catch (Exception e){

                                                        }

                                                        int finalHasil = hasil;


                                                        reference4 = FirebaseDatabase.getInstance().getReference()
                                                                .child("Cabang").child(cabang).child("Recap").child(String.valueOf(users.get(finalI).getKey_name())).child(fbulan+" "+ftahun);
                                                        Query query4 = reference4.orderByChild("keterangan").equalTo("Hadir");
                                                        query4.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Log.d("komisiku", String.valueOf(finalHasil));

                                                                int jumlahGaji = (int) (angkaGaji*dataSnapshot.getChildrenCount());
                                                                int jumlahUangMakan = (int) (angkaUangMakan*dataSnapshot.getChildrenCount());
                                                                int jumlahLembur = (int) (angkaLembur*dataSnapshot.getChildrenCount());
                                                                String gajiTotal = NumberFormat.getNumberInstance().format(Double.parseDouble(String.valueOf(jumlahGaji+jumlahUangMakan+jumlahLembur+finalHasil)));
                                                                Log.d("hasilnya", String.valueOf(gajiTotal));

                                                                Label labelgaji2 = new Label(clength, x2, "Rp."+gajiTotal);
                                                                Log.d("aKolom " +clength +" baris "+x2 ,"Rp. "+gajiTotal);
//                                                        Log.d("aKolom " +clength +" baris "+x2 ,String.valueOf(dataSnapshot.getChildrenCount()));

                                                                try {
                                                                    sheet.addCell(labelgaji2);
                                                                } catch (WriteException e) {
                                                                    e.printStackTrace();
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
                                        }catch (Exception e){

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

                        try {
                            sheet.addCell(labeln);
                        } catch (WriteException e) {
                            e.printStackTrace();
                        }

                        i++;
                        a++;
                        b++;

                    }while (i < length);



                    Label labelb1 = new Label(1, 2, fbulan+" "+ftahun);

                    try {
                        sheet.addCell(labela0);
                        sheet.addCell(labela1);

                        sheet.addCell(labelb1);
                    } catch (RowsExceededException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (WriteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                workbook.write();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                workbook.close();
                            } catch (WriteException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 5000);



                    //createExcel(excelSheet);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
