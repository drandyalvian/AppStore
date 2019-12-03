package com.example.company.appstore.Owner;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.company.appstore.BluetoothHandler;
import com.example.company.appstore.PrinterCommands;
import com.example.company.appstore.R;
import com.zj.btsdk.BluetoothService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("SetTextI18n")
public class PrintAct extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface {

    @BindView(R.id.tv_status)
    TextView tvStatus;

    private final String TAG = PrintAct.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;
    @BindView(R.id.viewPrint)
    TextView viewPrint;

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        setupBluetooth();

    }

    @AfterPermissionGranted(RC_BLUETOOTH)
    private void setupBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
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
        tvStatus.setText("Terhubung dengan perangkat");
    }

    @Override
    public void onDeviceConnecting() {
        tvStatus.setText("Sedang menghubungkan...");
    }

    @Override
    public void onDeviceConnectionLost() {
        isPrinterReady = false;
        tvStatus.setText("Koneksi perangkat terputus");
    }

    @Override
    public void onDeviceUnableToConnect() {
        tvStatus.setText("Tidak dapat terhubung ke perangkat");
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

    @OnClick(R.id.btn_print_text)
    public void printText(@Nullable View view) {
//        if (!mService.isAvailable()) {
//            Log.i(TAG, "printText: perangkat tidak support bluetooth");
//            return;
//        }
        if (isPrinterReady) {
            String coba = "Toko Andhika\n" +
                    "Cabang 1\n" +
                    "-------------------------------";
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(coba, "");


            String coba4 = "\nGaji Pokok:\n" +
                    "30 x 20.000 :       600.000" +
                    "\nKomisi:\n" +
                    "                    100.000" +
                    "\nUang Makan:\n" +
                    "30 x 20.000 :       600.000\n" +
                    "--------------------------------\n" +
                    "Total Gaji :        1.200.000\n" +
                    "Pijaman :           0\n" +
                    "--------------------------------\n" +
                    "Gaji Diterima :     1.200.0000";

            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(coba4, "");
            mService.write(PrinterCommands.ESC_ENTER);
        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceAct.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
        }
    }

    public void print(View view) {
        if (!mService.isAvailable()) {
            Log.i(TAG, "printText: perangkat tidak support bluetooth");
            return;
        }
        if (isPrinterReady) {
            String coba = "Toko Andhika\n" +
                    "Cabang 1\n" +
                    "-------------------------------";
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(coba, "");


            String coba4 = "\nGaji Pokok:\n" +
                    "30 x 20.000 :       600.000" +
                    "\nKomisi:\n" +
                    "                    100.000" +
                    "\nUang Makan:\n" +
                    "30 x 20.000 :       600.000\n" +
                    "--------------------------------\n" +
                    "Total Gaji :        1.200.000\n" +
                    "Pijaman :           0\n" +
                    "--------------------------------\n" +
                    "Gaji Diterima :     1.200.0000";

            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(coba4, "");
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
