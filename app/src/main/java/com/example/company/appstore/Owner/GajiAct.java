package com.example.company.appstore.Owner;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.zj.btsdk.BluetoothService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GajiAct extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface {

    Button back, print, btnReset;
    RelativeLayout inputgaji;
    EditText txtsearch;

    DatabaseReference reference, reference2;
    

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
        btnReset = findViewById(R.id.btnReset);

        rvView = (RecyclerView) findViewById(R.id.gaji_place);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        final String cabang = getIntent().getStringExtra("cabang");

        reference2 = FirebaseDatabase.getInstance().getReference().child("Cabang")
                .child(cabang).child("Karyawan");

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

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(GajiAct.this)
                        .setTitle("Reset Data Gaji")
                        .setMessage("Hal ini dilakukan jika sudah berhasil melakukan print gaji")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                        List<DataKaryawanConst> users = new ArrayList<>();
                                        while (iterator.hasNext()) {
                                            DataSnapshot dataSnapshotChild = iterator.next();
                                            DataKaryawanConst name = dataSnapshotChild.getValue(DataKaryawanConst.class);
                                            users.add(name);
                                        }

                                        int lengthku = (int) dataSnapshot.getChildrenCount();

                                        try {

                                            for (int i = 0 ; i < lengthku; i++){


                                                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabang)
                                                        .child("Karyawan").child(users.get(i).getKey_name()).child("Count_gaji");
                                                db2.removeValue();


                                                DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabang)
                                                        .child("Karyawan").child(users.get(i).getKey_name());
                                                dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        dataSnapshot.getRef().child("kompensasi").setValue(String.valueOf(0));

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



                                DatabaseReference db3 = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabang)
                                        .child("CountKomisi");
                                db3.removeValue();


                                DatabaseReference db4 = FirebaseDatabase.getInstance().getReference().child("Cabang").child(cabang)
                                        .child("CountKaryawan");
                                db4.removeValue();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        db4.removeValue();
                                    }
                                }, 5000);




                            }})
                        .setNegativeButton(android.R.string.no, null).show();

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

    public void printGaji(View view, String nama, String komisi, String gajiLembur, String gajiPokok, String pinjaman, Integer uangMakan, String gajiTotal, String gajiDiterima, String namaCabang, String totalMasuk, Integer totalUangMakan, String jumlahGajiPokok, String hitungCicilan, String sisaPinjaman, String checkedAngsuran, String tanggal) {



        try{
            if (!mService.isAvailable()) {
                Log.i(TAG, "printText: perangkat tidak support bluetooth");
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (isPrinterReady) {
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

            String coba = namaCabang + "\n" +
                    "================================";
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(coba, "");

            String lineNama = "\nNama : "+ nama;
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineNama, "");

            String lineDate = tanggal;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineDate, "");

            printText(leftRightAlign("\nGaji Pokok :", jumlahGajiPokok));

            String lineGajiPokok = "\nGaji Pokok : \n" + totalMasuk + " x " + gajiPokok;
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineGajiPokok, "");

            String lineHasilGajiPokok = jumlahGajiPokok ;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilGajiPokok, "");

            printText(leftRightAlign("\nGaji Lembur :", gajiLembur));

            String lineGajiLembur = "GajiLembur :";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineGajiLembur, "");

            String lineHasilGajiLembur = gajiLembur ;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilGajiLembur, "");

            printText(leftRightAlign("\nKomisi :", komisi));

            String lineKomisi = "Komisi :";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineKomisi, "");

            String lineHasilKomisi = komisi ;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilKomisi, "");

            if (uangMakan != 0) {

                printText(leftRightAlign("\nUang Makan :", formatRupiah.format(Double.parseDouble(String.valueOf(totalUangMakan)))));

                String lineUangMakan = "Uang Makan : \n";
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                mService.sendMessage(lineUangMakan, "");

                String lineHasilUangMakan = formatRupiah.format(Double.parseDouble(String.valueOf(totalUangMakan)));
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                mService.sendMessage(lineHasilUangMakan, "");
            }
            String garis = "================================";

            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(garis, "");

            printText(leftRightAlign("\nTotal Gaji :", gajiTotal));

            String lineTotalGaji = "Total Gaji :";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(lineTotalGaji, "");

            String lineHasilTotalGaji = gajiTotal;
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(lineHasilTotalGaji, "");

            if (checkedAngsuran.equals("0") && sisaPinjaman.equals("0")){

            }else if(!checkedAngsuran.equals("0")&& sisaPinjaman.equals("0")){

                printText(leftRightAlign("Bayar Angsuran :", pinjaman));

                String linePinjaman = "Bayar Angsuran :" ;
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                mService.sendMessage(linePinjaman, "");

                String lineHasilPinjaman = pinjaman;
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                mService.sendMessage(lineHasilPinjaman, "");


                printText(leftRightAlign("Sisa Pinjaman :", ":LUNAS"));

                String lineSisaPinjaman = "Sisa Pinjaman : ";
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                mService.sendMessage(lineSisaPinjaman, "");

                String lineHasilSisaPinjaman = "LUNAS" + "\n";
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                mService.sendMessage(lineHasilSisaPinjaman, "");



            }else if (checkedAngsuran.equals("0") && !sisaPinjaman.equals("0")){

                printText(leftRightAlign("Sisa Pinjaman :", formatRupiah.format(Double.parseDouble(sisaPinjaman))));

                String lineSisaPinjaman = "Sisa Pinjaman : ";
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                mService.sendMessage(lineSisaPinjaman, "");

                String lineHasilSisaPinjaman = formatRupiah.format(Double.parseDouble(sisaPinjaman)) + "\n";
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                mService.sendMessage(lineHasilSisaPinjaman, "");

            }else if(!checkedAngsuran.equals("0")&& !sisaPinjaman.equals("0")){

                printText(leftRightAlign("Bayar Angsuran :", pinjaman));

                String linePinjaman = "Bayar Angsuran :" ;
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                mService.sendMessage(linePinjaman, "");

                String lineHasilPinjaman = pinjaman;
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                mService.sendMessage(lineHasilPinjaman, "");

                printText(leftRightAlign("Sisa Pinjaman :", formatRupiah.format(Double.parseDouble(sisaPinjaman))));


                String lineSisaPinjaman = "Sisa Pinjaman : ";
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                mService.sendMessage(lineSisaPinjaman, "");

                String lineHasilSisaPinjaman = formatRupiah.format(Double.parseDouble(sisaPinjaman))+ "\n";
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                mService.sendMessage(lineHasilSisaPinjaman, "");


            }

//            if(Integer.parseInt(hitungCicilan) != 0 ) {
//                String linePinjaman = "Bayar Angsuran :" + hitungCicilan;
//                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
//                mService.sendMessage(linePinjaman, "");
//
//                String lineHasilPinjaman = pinjaman;
//                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
//                mService.sendMessage(lineHasilPinjaman, "");
//            }
//            if (Integer.parseInt(sisaPinjaman) != 0) {
//                String lineSisaPinjaman = "Sisa Pinjaman : ";
//                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
//                mService.sendMessage(lineSisaPinjaman, "");
//
//                String lineHasilSisaPinjaman = sisaPinjaman + "\n";
//                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
//                mService.sendMessage(lineHasilSisaPinjaman, "");
//            }

            mService.sendMessage(garis, "");

            printText(leftRightAlign("Gaji Diterima :", gajiDiterima));

            String lineGajiDiterima = "Gaji Diterima : ";
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.write(PrinterCommands.TXT_BOLD_ON);
            mService.sendMessage(lineGajiDiterima, "");

            String lineHasilGajiDiterima = gajiDiterima + "\n";
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.write(PrinterCommands.TXT_BOLD_ON);
            mService.sendMessage(lineHasilGajiDiterima, "");

            mService.write(PrinterCommands.ESC_ENTER);
        } else {
            try {
                if (mService.isBTopen()) {
                    startActivityForResult(new Intent(this, DeviceAct.class), RC_CONNECT_DEVICE);
                }else {
                    requestBluetooth();
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    public void printGajiNew(View view, String nama, String komisi, String gajiLembur, String gajiPokok, String pinjaman, Integer uangMakan, String gajiTotal, String gajiDiterima, String namaCabang, String totalMasuk, Integer totalUangMakan, String jumlahGajiPokok, String hitungCicilan, String sisaPinjaman, String checkedAngsuran, String tanggal) {

        try{
            if (!mService.isAvailable()) {
                Log.i(TAG, "printText: perangkat tidak support bluetooth");
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (isPrinterReady) {
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

            printCustom(namaCabang, 2, 1);

            printCustom(new String(new char[21]).replace("\0", "--"), 0, 1);

            printText(leftRightAlign("Nama : "+nama, tanggal));

            printText(leftRightAlign("Gaji Pokok :", ""));

            printText(leftRightAlign(totalMasuk + " x " + gajiPokok, jumlahGajiPokok));

            printText(leftRightAlign("Gaji Lembur :", gajiLembur));

            printText(leftRightAlign("Komisi :", komisi));

            if (uangMakan != 0) {

                printText(leftRightAlign("Uang Makan :", formatRupiah.format(Double.parseDouble(String.valueOf(totalUangMakan)))));

            }

            printCustom(new String(new char[21]).replace("\0", "--"), 0, 1);

            printText(leftRightAlign("Total Gaji :", gajiTotal));

            if(!checkedAngsuran.equals("0")&& sisaPinjaman.equals("0")){

                printText(leftRightAlign("Bayar Angsuran :", pinjaman));

                printText(leftRightAlign("Sisa Pinjaman :", ":LUNAS"));

            }else if (checkedAngsuran.equals("0") && !sisaPinjaman.equals("0")){

                printText(leftRightAlign("Sisa Pinjaman :", formatRupiah.format(Double.parseDouble(sisaPinjaman))));

            }else if(!checkedAngsuran.equals("0")&& !sisaPinjaman.equals("0")){

                printText(leftRightAlign("Bayar Angsuran :", pinjaman));

                printText(leftRightAlign("Sisa Pinjaman :", formatRupiah.format(Double.parseDouble(sisaPinjaman))));

            }

            printCustom(new String(new char[21]).replace("\0", "--"), 0, 1);

            printCustom(leftRightAlignGaji("Gaji Diterima :", gajiDiterima), 3, 1);

            printNewLine();
            printNewLine();
            printNewLine();

        } else {
            try {
                if (mService.isBTopen()) {
                    startActivityForResult(new Intent(this, DeviceAct.class), RC_CONNECT_DEVICE);
                }else {
                    requestBluetooth();
                }
            }catch (Exception e ){
                requestBluetooth();
                e.printStackTrace();
            }
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

    private String leftRightAlign(String leftText, String rightText) {
        String textCounter = leftText + rightText;

        if (textCounter.length() < 42) {
            int n = 42 - (leftText.length() + rightText.length());
            textCounter = leftText + new String(new char[n]).replace("\0", " ") + rightText;
        }

        return textCounter;
    }

    private String leftRightAlignGaji(String leftText, String rightText) {
        String textCounter = leftText + rightText;

        if (textCounter.length() < 30) {
            int n = 30 - (leftText.length() + rightText.length());
            textCounter = leftText + new String(new char[n]).replace("\0", " ") + rightText;
        }

        return textCounter;
    }

    private void printText(String msg) {
        mService.write(msg.getBytes());
        mService.write(PrinterCommands.FEED_LINE);
    }

    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        switch (size) {
            case 0:
                mService.write(cc);
                break;
            case 1:
                mService.write(bb);
                break;
            case 2:
                mService.write(bb2);
                break;
            case 3:
                mService.write(bb3);
                break;
        }

        switch (align) {
            case 0:
                //left align
                mService.write(PrinterCommands.ESC_ALIGN_LEFT);
                break;
            case 1:
                //center align
                mService.write(PrinterCommands.ESC_ALIGN_CENTER);
                break;
            case 2:
                //right align
                mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
                break;
        }

        mService.write(msg.getBytes());
        mService.write(new byte[]{0x0A});
    }

    private String centerRightAlign(String centerText, String rightText) {
        String textCounter = centerText + rightText;

        if (textCounter.length() < 42) {
            int n = 32 - (centerText.length() + rightText.length());
            textCounter = new String(new char[10]).replace("\0", " ") + centerText + new String(new char[n]).replace("\0", " ") + rightText;
        }

        return textCounter;
    }

    private void printNewLine() {
        mService.write(PrinterCommands.FEED_LINE);
    }


}
