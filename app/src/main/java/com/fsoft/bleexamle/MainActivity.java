package com.fsoft.bleexamle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private Button mScanButton;

    private int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking whether BLE is supported on the device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, R.string.ble_is_supported, Toast.LENGTH_LONG).show();
            enableBluetooth();
        }

        // Setup UI
        mScanButton = (Button) findViewById(R.id.scan_button);

        // Handle event click
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanDeviceActivity();
            }
        });
    }

    private void enableBluetooth() {
        // If Ble is supported but disable, then request user to enable BT
        // This will be done in two steps:
        // (1) Initilizes Bluetooth Adapter
        // Get BluetoothManager to obtain instance of BluetoothAdapter, conduct overral
        // Bluetooth Management
        final BluetoothManager bluetoothManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "BT turn on successful", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "BT turn on failured", Toast.LENGTH_LONG).show();
                mScanButton.setEnabled(false);
            }
        }
    }

    private void startScanDeviceActivity() {
        // If BT turn on success, then we go to scan activity
        Intent intent = new Intent(this, DeviceScanActivity.class);
        startActivity(intent);
    }

}
