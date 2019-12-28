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

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

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

    DatabaseReference reference, reference2;
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



        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("KepalaCabang").child(cabang);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataKaryawanConsts = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    DataKaryawanConst kConst = dataSnapshot1.getValue(DataKaryawanConst.class);

                    kConst.setKey(dataSnapshot1.getKey());

                    dataKaryawanConsts.add(kConst);
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


        Button btnSave2 =(Button) dialogView2.findViewById(R.id.btnsaves);
        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExcelSheet(fbulan.getSelectedItem().toString(), ftahun.getText().toString(), cabangku);
                alertDialog.hide();

            }
        });
    }

    private void createExcelSheet(String fbulan, String ftahun, String cabang)
    {
//        String Fnamexls="dataKaryawan"+System.currentTimeMillis()+ ".xls";
        String Fnamexls="Rekap_"+cabangku+"_"+fbulan+ftahun+".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/recap");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));
        Toast.makeText(this, "storage/recap/"+Fnamexls, Toast.LENGTH_LONG).show();

        WritableWorkbook workbook;
        try {
            int a = 1;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.createSheet(cabangku+" "+fbulan+ftahun, 0);
            Label labela0 = new Label(0, 2, "Nama"); //COLOM A, ROW 2
            Label labela1 = new Label(0, 3, " "); //COLOM A, ROW 2
            Label labela2 = new Label(0, 4, "Nama Satu");
            Label labela3 = new Label(0, 5, "Nama Dua");
            Label labela4 = new Label(0, 6, "Nama Tiga");

            Label labelb1 = new Label(1, 2, fbulan+" "+ftahun);
            Label labelb2 = new Label(1, 3, "1");
            Label labelb3 = new Label(2, 3, "2");
            Label labelb4 = new Label(3, 3, "3");
            Label labelb5 = new Label(4, 3, "4");
            Label labelb6 = new Label(5, 3, "5");
            Label labelb7 = new Label(6, 3, "6");
            Label labelb8 = new Label(7, 3, "7");
            Label labelb9 = new Label(8, 3, "8");
            Label labelb10 = new Label(9, 3, "9");
            Label labelb11 = new Label(10, 3, "..30");

            Label labelc1 = new Label(11, 2, "Gaji");
            Label labelc2 = new Label(11, 3, " ");
            Label labelc3 = new Label(11, 4, "Rp, 1.500.0000");
            Label labelc4 = new Label(11, 5, "Rp, 1.500.0000");
            Label labelc5 = new Label(11, 6, "Rp, 1.500.0000");
//            Label labelb1 = new Label(0,1,"first");
//            Label labelc1 = new Label(0,0,"HEADING");
//            Label labeld1 = new Label(1,0,"Heading2");
//            Label labele1 = new Label(1,1,String.valueOf(a));
            try {
                sheet.addCell(labela0);
                sheet.addCell(labela1);
                sheet.addCell(labela2);
                sheet.addCell(labela3);
                sheet.addCell(labela4);

                sheet.addCell(labelb1);
                sheet.addCell(labelb2);
                sheet.addCell(labelb3);
                sheet.addCell(labelb4);
                sheet.addCell(labelb5);
                sheet.addCell(labelb6);
                sheet.addCell(labelb7);
                sheet.addCell(labelb8);
                sheet.addCell(labelb9);
                sheet.addCell(labelb10);
                sheet.addCell(labelb11);

                sheet.addCell(labelc1);
                sheet.addCell(labelc2);
                sheet.addCell(labelc3);
                sheet.addCell(labelc4);
                sheet.addCell(labelc5);


//                sheet.addCell(label1);
//                sheet.addCell(label0);
//                sheet.addCell(label4);
//                sheet.addCell(label3);
            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //createExcel(excelSheet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
