package com.example.company.appstore.Owner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.appstore.BluetoothHandler;
import com.example.company.appstore.PrinterCommands;
import com.example.company.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GajiAct extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface {

    Button back, print;
    RelativeLayout inputgaji;
    EditText txtsearch;

    DatabaseReference reference;
    

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GajiConst> gajiConsts;

    private final String TAG = PrintAct.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaji);

        ButterKnife.bind(this);
        setupBluetooth();

        back = findViewById(R.id.back);
        inputgaji = findViewById(R.id.inputgaji);
        txtsearch = findViewById(R.id.txtsearch);

        rvView = (RecyclerView) findViewById(R.id.gaji_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        final String cabang = getIntent().getStringExtra("cabang");


        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cabang").child(cabang).child("Karyawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gajiConsts = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    GajiConst aConst = dataSnapshot1.getValue(GajiConst.class);
                    aConst.setKey(dataSnapshot1.getKey());
                    aConst.setCabang(cabang);
                    gajiConsts.add(aConst);

                }
                adapter = new GajiAdapter(gajiConsts, GajiAct.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(GajiAct.this, OwnerDashbordAct.class);
                startActivity(go);

            }
        });

//        inputgaji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(GajiAct.this);
//                dialog.setContentView(R.layout.dialogview_input_gaji);
//                dialog.show();
//
//            }
//        });
//        print.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final Dialog dialog = new Dialog(GajiAct.this);
//                dialog.setContentView(R.layout.dialogview_pinjaman);
//                dialog.show();
//
//            }
//        });


//        EditText editor = new EditText(this);
//        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

//        txtsearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!s.toString().isEmpty()){
//                    search(s.toString());
//                }else {
//
//                    search("");
//
//                }
//
//
//            }
//        });
    }

    //search fungsi
//    private void search(String s ) {
//
//
//        Query query = reference.orderByChild("nama")
//                .startAt(s)
//                .endAt(s+"\uf8ff"); //
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChildren()){
//                    gajiConsts.clear();
//                    for (DataSnapshot dss: dataSnapshot.getChildren()){
//                        final GajiConst gajiK = dss.getValue(GajiConst.class);
//                        gajiConsts.add(gajiK);
//
//
//                    }
//
//                    GajiAdapter adapter = new GajiAdapter(gajiConsts,GajiAct.this);
//                    rvView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @AfterPermissionGranted(RC_BLUETOOTH)
    private void setupBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "You need bluetooth permission",
                    RC_BLUETOOTH, params);
            return;
        }
        mService = new BluetoothService(this, new BluetoothHandler(this));
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
    }

    @Override
    public void onDeviceConnected() {
        isPrinterReady = true;
        Toast.makeText(this, "Terhubung dengan perangkat", Toast.LENGTH_SHORT).show();
        //tvStatus.setText("Terhubung dengan perangkat");
    }

    @Override
    public void onDeviceConnecting() {
        Toast.makeText(this, "Sedang menghubungkan...", Toast.LENGTH_SHORT).show();
//        tvStatus.setText("Sedang menghubungkan...");
    }

    @Override
    public void onDeviceConnectionLost() {
        isPrinterReady = false;
        Toast.makeText(this, "Koneksi perangkat terputus", Toast.LENGTH_SHORT).show();
//        tvStatus.setText("Koneksi perangkat terputus");
    }

    @Override
    public void onDeviceUnableToConnect() {
        Toast.makeText(this, "Tidak dapat terhubung ke perangkat", Toast.LENGTH_SHORT).show();
     //   tvStatus.setText("Tidak dapat terhubung ke perangkat");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_ENABLE_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: bluetooth aktif");
                } else
                    Log.i(TAG, "onActivityResult: bluetooth harus aktif untuk menggunakan fitur ini");
                break;
            case RC_CONNECT_DEVICE:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString(DeviceAct.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice mDevice = mService.getDevByMac(address);
                    mService.connect(mDevice);
                }
                break;
        }
    }

    public void printGaji(View view, String nama, String komisi, String gajiPokok, String pinjaman, String uangMakan, String gajiTotal, String gajiDiterima, String namaCabang, String totalMasuk, String totalUangMakan, String jumlahGajiPokok) {
        if (!mService.isAvailable()) {
            Log.i(TAG, "printText: perangkat tidak support bluetooth");
            return;
        }
        if (isPrinterReady) {
            String coba = "Toko Andhika\n" +
                    namaCabang + "\n" +
                    "================================";
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(coba, "");


            String lineGajiPokok = "\nGaji Pokok : \n" + totalMasuk + " x " + gajiPokok;
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineGajiPokok, "");

            String lineHasilGajiPokok = jumlahGajiPokok ;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilGajiPokok, "");

            String lineKomisi = "Komisi :";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineKomisi, "");

            String lineHasilKomisi = komisi ;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilKomisi, "");

            String lineUangMakan = "Uang Makan : \n" + totalMasuk + " x " + uangMakan;
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineUangMakan, "");

            String lineHasilUangMakan = totalUangMakan;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilUangMakan, "");

            String garis = "================================";

            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(garis, "");

            String lineTotalGaji = "Total Gaji :";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineTotalGaji, "");

            String lineHasilTotalGaji = gajiTotal;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilTotalGaji, "");

            String linePinjaman = "Pinjaman :";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(linePinjaman, "");

            String lineHasilPinjaman = pinjaman;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilPinjaman, "");

            mService.sendMessage(garis, "");

            String lineGajiDiterima = "Gaji Diterima : ";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineGajiDiterima, "");

            String lineHasilGajiDiterima = gajiDiterima + "\n";
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilGajiDiterima, "");

            mService.write(PrinterCommands.ESC_ENTER);
        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceAct.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
        }
    }

    private void requestBluetooth() {
        if (mService != null) {
            if (!mService.isBTopen()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, RC_ENABLE_BLUETOOTH);
            }
        }
    }


}
