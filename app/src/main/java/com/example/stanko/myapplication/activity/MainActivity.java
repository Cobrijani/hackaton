package com.example.stanko.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.stanko.myapplication.R;
import com.example.stanko.myapplication.adapter.LocationListAdapter;
import com.example.stanko.myapplication.service.BeaconDiscoveryService;
import com.example.stanko.myapplication.service.BeaconDiscoveryService_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    ListView locationList;

    @ViewById
    Toolbar toolbar;

    @Bean
    LocationListAdapter adapter;

    @AfterViews
    void startService() {
        BeaconDiscoveryService_.intent(this).start();
    }

    @AfterViews
    void setViews() {
        setSupportActionBar(toolbar);
        locationList.setAdapter(adapter);
    }

    @Receiver(actions = BeaconDiscoveryService.NEW_BEACON_SIGHTING)
    void onBeaconSighted() {
        adapter.update(BeaconDiscoveryService.getLocations());
    }

}
