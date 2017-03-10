package com.kittipat.beacon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.eddystone.Eddystone;
import com.estimote.sdk.eddystone.EddystoneTelemetry;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private static final UUID ESTIMOTE_PROXIMITY_UUID = UUID.fromString("24baa021-4431-4078-bb8d-4238283fa463");
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", ESTIMOTE_PROXIMITY_UUID, null, null);
    private Boolean notificationIBeacon = true;
    private Boolean notificationEddyStone = true;
    private TextView txtUUID , txtIBeaconMacAddress , txtMeasuredPower , txtProximityUUID , txtIBeaconRssi
                    ,txtNamespaceId , txtCalibratedTxPower , txtMacAddress , txtDescribeContents , txtEddyStoneRssi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        setView();
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                Log.e("TAG", "Ranged beacons: " + beacons);
                if (beacons.size() > 0) {
                        if(beacons.get(0).getRssi() > -80){
                            txtUUID.setText(String.valueOf(ESTIMOTE_PROXIMITY_UUID));
                            txtIBeaconRssi.setText(String.valueOf(beacons.get(0).getRssi()));
                            txtIBeaconMacAddress.setText(String.valueOf(beacons.get(0).getMacAddress()));
                            txtMeasuredPower.setText(String.valueOf(beacons.get(0).getMeasuredPower()));
                            txtProximityUUID.setText(String.valueOf(beacons.get(0).getProximityUUID()));
                            showNotificationIBeacon("Hi", "My name is IBeacon" , notificationIBeacon);
                            notificationIBeacon = false;
                        }else {
                            notificationIBeacon = true;
                            txtUUID.setText(String.valueOf(ESTIMOTE_PROXIMITY_UUID));
                            txtIBeaconRssi.setText(String.valueOf(beacons.get(0).getRssi()));
                            txtIBeaconMacAddress.setText(String.valueOf(beacons.get(0).getMacAddress()));
                            txtMeasuredPower.setText(String.valueOf(beacons.get(0).getMeasuredPower()));
                            txtProximityUUID.setText(String.valueOf(beacons.get(0).getProximityUUID()));
                        }
                }else {
                    txtUUID.setText("N/A");
                    txtIBeaconRssi.setText("N/A");
                    txtIBeaconMacAddress.setText("N/A");
                    txtMeasuredPower.setText("N/A");
                    txtProximityUUID.setText("N/A");
                }
            }
        });

        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List nearables) {
                Log.e("TAG", "Discovered nearables: " + nearables);
            }
        });

        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> eddystones) {
                if(eddystones.size() > 0 ){
                        if(eddystones.get(0).rssi > -80){
                            showNotificationEddyStone("Hi", "My name is EddyStones",notificationEddyStone);
                            txtEddyStoneRssi.setText(String.valueOf(eddystones.get(0).rssi));
                            txtMacAddress.setText(String.valueOf(eddystones.get(0).macAddress));
                            txtNamespaceId.setText(String.valueOf(eddystones.get(0).namespace));
                            txtCalibratedTxPower.setText(String.valueOf(eddystones.get(0).calibratedTxPower));
                            txtDescribeContents.setText(String.valueOf(eddystones.get(0).describeContents()));
                            EddystoneTelemetry tlm = eddystones.get(0).telemetry;
                            notificationEddyStone = false;
                        }else {
                            notificationEddyStone = true;
                            txtEddyStoneRssi.setText(String.valueOf(eddystones.get(0).rssi));
                            txtMacAddress.setText(String.valueOf(eddystones.get(0).macAddress));
                            txtNamespaceId.setText(String.valueOf(eddystones.get(0).namespace));
                            txtCalibratedTxPower.setText(String.valueOf(eddystones.get(0).calibratedTxPower));
                            txtDescribeContents.setText(String.valueOf(eddystones.get(0).describeContents()));
                        }
                }else {
                    txtEddyStoneRssi.setText("N/A");
                    txtMacAddress.setText("N/A");
                    txtNamespaceId.setText("N/A");
                    txtCalibratedTxPower.setText("N/A");
                    txtDescribeContents.setText("N/A");
                }
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                // Beacons ranging.
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                // Nearable discovery.
                String startNear = beaconManager.startNearableDiscovery();
                Log.e("NearStart", startNear);

                // Eddystone scanning.
                String startEddy = beaconManager.startEddystoneScanning();
                Log.e("NearEddy", startEddy);
            }
        });
    }

    private void setView() {
        txtUUID = (TextView) findViewById(R.id.txtUUID);
        txtCalibratedTxPower = (TextView) findViewById(R.id.txtCalibratedTxPower);
        txtDescribeContents = (TextView) findViewById(R.id.txtDescribeContents);
        txtNamespaceId = (TextView) findViewById(R.id.txtNamespaceId);
        txtIBeaconMacAddress = (TextView) findViewById(R.id.txtIBeaconMacAddress);
        txtMacAddress = (TextView) findViewById(R.id.txtMacAddress);
        txtMeasuredPower = (TextView) findViewById(R.id.txtMeasuredPower);
        txtProximityUUID = (TextView) findViewById(R.id.txtProximityUUID);
        txtIBeaconRssi = (TextView) findViewById(R.id.txtRssi);
        txtEddyStoneRssi = (TextView) findViewById(R.id.txtEddyStoneRSSI);
    }

    protected static double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine distance, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }

    public void showNotificationEddyStone(String title, String message, Boolean showNotification) {
        if(showNotification){
            Intent notifyIntent = new Intent(this, MainActivity.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                    new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }
    }

    public void showNotificationIBeacon(String title, String message, Boolean showNotification) {
        if(showNotification) {
            Intent notifyIntent = new Intent(this, MainActivity.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                    new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(2, notification);
        }
    }
}
