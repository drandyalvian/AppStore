package com.example.company.appstore.KepalaCabang;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class ListAbsensiAct extends AppCompatActivity {
    private static ListAbsensiAct instance;
    DatabaseReference reference;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    private Button btnSave;
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


    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String nKaryawan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_absensi);
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
        nKaryawan = nama_karyawan;


//database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Cabang").child(username_key_new).child("Karyawan")
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
                        adapter = new ListAbsensiAdapter(labsensiConsts, ListAbsensiAct.this, username_key_new);
                        rvView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public static ListAbsensiAct getInstance() {
        return instance;
    }

    //update absen
    public void updateAbsen(String key, String keterangan) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(username_key_new).child("Karyawan").child(nKaryawan).child("Absensi").child(key);
        ListAbsensiConst listAbsensiConst = new ListAbsensiConst(keterangan, key, key);
        db.setValue(listAbsensiConst);
    }

    public void deleteAbsen(String key) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(username_key_new).child("Karyawan").child(nKaryawan).child("Absensi").child(key);
        db.removeValue();
    }

    private void addAbsen(String tanggal) {
        // tambahkan semua ke dialog
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cabang").child(username_key_new).child("Karyawan").child(nKaryawan).child("Absensi").child(dateFormat.format(date));
        DatabaseReference countGaji = FirebaseDatabase.getInstance().getReference().child("Cabang").child(username_key_new).child("Karyawan").child(nKaryawan).child("Count_gaji").child(tanggal);
        ListAbsensiConst absensiConst = new ListAbsensiConst(
                "Hadir",
                tanggal,
                tanggal

        );

        CountGajiEntity entity = new CountGajiEntity(
                dateFormat.format(date)
        );

        countGaji.setValue(entity);

        db.setValue(absensiConst);
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

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+7"));

        tanggalAbsen = (TextView) dialogView.findViewById(R.id.tanggalAbsen);
        tanggalAbsen.setText(dateFormat.format(date));

        tanggalAbsen.setOnClickListener(new View.OnClickListener() {
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

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

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

}
