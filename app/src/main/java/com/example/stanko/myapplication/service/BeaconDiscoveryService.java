package com.example.stanko.myapplication.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.stanko.myapplication.R;
import com.example.stanko.myapplication.activity.MainActivity_;
import com.example.stanko.myapplication.model.Location;
import com.gimbal.android.Beacon;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

@EService
public class BeaconDiscoveryService extends Service {

    public static final String NEW_BEACON_SIGHTING = "newBeaconSighting";

    private static final String TAG = "BeaconDiscoveryService";
    private static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BeaconEventListener beaconSightingListener;
    private BeaconManager beaconManager;

    private static final List<Location> locations = new ArrayList<>();

    @Override
    public void onCreate() {
        Log.i(TAG, "Service started.");

        checkBluetoothAvailability();

        beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting beaconSighting) {
                super.onBeaconSighting(beaconSighting);

                final Beacon beacon = beaconSighting.getBeacon();
                Log.d(TAG, "Beacon sighted " + beacon.getName() + " id: " + beacon.getIdentifier());

                final Location newLocation = new Location();
                newLocation.uuid = beacon.getIdentifier();
                newLocation.signalStrength = beaconSighting.getRSSI();

                for (Location location: locations) {
                    if (location.uuid.equals(newLocation.uuid)) {
                        return;
                    }
                }

                locations.add(newLocation);
                sendBroadcast(new Intent(NEW_BEACON_SIGHTING));
            }
        };

        beaconManager = new BeaconManager();
        beaconManager.addListener(beaconSightingListener);
        beaconManager.startListening();
    }

    private void checkBluetoothAvailability() {
        if (bluetoothAdapter != null) {
            checkEnabled();
        } else {
            Toast.makeText(this, R.string.ble_not_supported, LENGTH_SHORT).show();
            Log.e(TAG, "Bluetooth not supported");
        }
    }

    private void checkEnabled() {
        if (!bluetoothAdapter.isEnabled()) {
            Log.w(TAG, "Bluetooth service is disabled. Enabling...");
            bluetoothAdapter.enable();
            Log.i(TAG, "Bluetooth enabled.");
        } else {
            Log.d(TAG, "Bluetooth is enabled and functioning properly.");
        }
    }

    public static List<Location> getLocations() {
        return locations;
    }

    @Override
    public void onDestroy() {
        beaconManager.removeListener(beaconSightingListener);
        beaconManager.stopListening();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
