package com.example.company.appstore.Owner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.KepalaCabang.AbsensiAct;
import com.example.company.appstore.KepalaCabang.CountGajiEntity;
import com.example.company.appstore.KepalaCabang.ListAbsensiConst;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAbsensiAdmin extends AppCompatActivity {

    private static ListAbsensiAdmin instance;
    DatabaseReference reference, reference2;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.xnama)
    TextView xnama;
    private Button btnSave;
    private Button btnSave2;
    private TextView tanggalAbsen;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ListAbsensiConst> labsensiConsts;
    private DatePickerDialog datePickerDialog;
    public SimpleDateFormat dateFormatter;
    private AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    String mFilter;


    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String nKaryawan = "";
    String nCabang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_absensi_admin);
        ButterKnife.bind(this);
        instance = this;

        rvView = (RecyclerView) findViewById(R.id.labsensi_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        getUsernameLocal();


//mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String nama_karyawan = bundle.getString("key");
        final String xcabang = bundle.getString("keyCabang");
        nKaryawan = nama_karyawan;
        nCabang = xcabang;


//database

        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang").child(xcabang).child("Karyawan")
                .child(nama_karyawan).child("Absensi");

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Cabang").child(xcabang).child("Karyawan")
                .child(nama_karyawan).child("Absensi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        labsensiConsts = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            ListAbsensiConst laConst = dataSnapshot1.getValue(ListAbsensiConst.class);
                            laConst.setKey(dataSnapshot1.getKey());

                            labsensiConsts.add(laConst);

                        }
                        adapter = new ListAbsensiAdminAdapter(labsensiConsts, ListAbsensiAdmin.this, username_key_new);
                        rvView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        reference.child("Cabang").child(xcabang).child("Karyawan")
                .child(nama_karyawan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                xnama.setText(dataSnapshot.child("nama").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


// pindah act

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go = new Intent(ListAbsensiAdmin.this, DataKaryawanAct.class);
                go.putExtra("cabang",xcabang); //Lempar key
                go.putExtra("key",nama_karyawan); //Lempar key
                startActivity(go);
            }
        });

    }

    public static ListAbsensiAdmin getInstance() {
        return instance;
    }

    //update absen
    public void updateAbsen(String key, String keterangan, String filter) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(nCabang).child("Karyawan").child(nKaryawan).child("Absensi").child(key);
        ListAbsensiConst listAbsensiConst = new ListAbsensiConst(keterangan, key, key, filter);
        db.setValue(listAbsensiConst);
    }

    public void deleteAbsen(String key) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(nCabang).child("Karyawan").child(nKaryawan).child("Absensi").child(key);
        db.removeValue();
        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Cabang").child(nCabang)
                .child("Karyawan").child(nKaryawan).child("Count_gaji").child(key);
        db2.removeValue();
    }

    private void addAbsen(String tanggal) {
        // tambahkan semua ke dialog
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(nCabang).child("Karyawan").child(nKaryawan);
        ListAbsensiConst absensiConst = new ListAbsensiConst(
                "Hadir",
                tanggal,
                tanggal,
                tanggal.substring(3)


        );

        CountGajiEntity entity = new CountGajiEntity(
                dateFormat.format(date),dateFormat.format(date)
        );

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                db.child("Absensi").child(tanggal).setValue(absensiConst);
                db.child("Count_gaji").child(tanggal).setValue(entity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //mengambil data local
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

    }




    @OnClick(R.id.btnAdd)
    public void onViewClicked() {
        //addAbsen();
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialogview_add_absensi, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date date = new Date();
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));

        tanggalAbsen = (TextView) dialogView.findViewById(R.id.tanggalAbsen);
        tanggalAbsen.setText(dateFormat.format(date));

        Button addtgl = (Button) dialogView.findViewById(R.id.addtgl);

        addtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(dialogView);
            }
        });

        btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAbsen(tanggalAbsen.getText().toString());
                alertDialog.hide();
            }
        });


    }


    private void showDateDialog(View viewDialog) {

        Calendar newCalendar = Calendar.getInstance();

//        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tanggalAbsen = (TextView) viewDialog.findViewById(R.id.tanggalAbsen);
                tanggalAbsen.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void aFilter(String fbulan, String ftahun){

        Query query = reference2.orderByChild("filter").equalTo(fbulan+" "+ftahun);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    labsensiConsts.clear();
                    for (DataSnapshot dss: dataSnapshot.getChildren()){
                        final ListAbsensiConst absensi = dss.getValue(ListAbsensiConst.class);
                        labsensiConsts.add(absensi);

                    }

                    ListAbsensiAdminAdapter adapter = new ListAbsensiAdminAdapter (labsensiConsts,ListAbsensiAdmin.this, username_key);
                    rvView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(ListAbsensiAdmin.this, fbulan+" "+ftahun, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void allData(){
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Cabang").child(nCabang).child("Karyawan")
                .child(nKaryawan).child("Absensi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        labsensiConsts = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            ListAbsensiConst laConst = dataSnapshot1.getValue(ListAbsensiConst.class);
                            laConst.setKey(dataSnapshot1.getKey());

                            labsensiConsts.add(laConst);

                        }
                        adapter = new ListAbsensiAdminAdapter(labsensiConsts, ListAbsensiAdmin.this, username_key_new);
                        rvView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @OnClick(R.id.btn_filter)
    public void onViewClicked2() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView2 = LayoutInflater.from(this).inflate(R.layout.dialogview_filter, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView2);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText ftahun = (EditText) dialogView2.findViewById(R.id.ftahun);
//        EditText fbulan = (EditText) dialogView2.findViewById(R.id.fbulan);
        Button btnAll = (Button) dialogView2.findViewById(R.id.btnAll);

        Spinner fbulan = (Spinner) dialogView2.findViewById(R.id.fbulan);

        final ArrayAdapter pilihFilter = ArrayAdapter.createFromResource(this, R.array.pilih_filter, android.R.layout.simple_spinner_dropdown_item);
        fbulan.setAdapter(pilihFilter);


        btnSave2 = dialogView2.findViewById(R.id.btnsaves);
        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ftahun.getText().toString().equals("")) {

                    Toast.makeText(ListAbsensiAdmin.this, "Tidak boleh kosong !", Toast.LENGTH_SHORT).show();
                } else {
                    aFilter(fbulan.getSelectedItem().toString(), ftahun.getText().toString());
                    alertDialog.hide();
                }

            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.hide();
                allData();


            }
        });


    }
}
