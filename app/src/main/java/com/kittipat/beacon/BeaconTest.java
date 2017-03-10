package com.kittipat.beacon;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.MacAddress;

import java.util.UUID;


public class BeaconTest extends Beacon {
    public BeaconTest(UUID proximityUUID, MacAddress macAddress, int major, int minor, int measuredPower, int rssi) {
        super(proximityUUID, macAddress, major, minor, measuredPower, rssi);
    }

    @Override
    public String toString() {
        return getMajor() +" " + getMinor();
    }
}
