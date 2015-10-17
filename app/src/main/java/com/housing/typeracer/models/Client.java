package com.housing.typeracer.models;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class Client {
    private String remoteEndpointId;
    private String remoteDeviceId;
    private String remoteEndpointName;
    private byte[] payload;

    public Client(String remoteEndpointId, String remoteDeviceId, String remoteEndpointName, byte[] payload) {
        this.remoteEndpointId = remoteEndpointId;
        this.remoteDeviceId = remoteDeviceId;
        this.remoteEndpointName = remoteEndpointName;
        this.payload = payload;
    }

    public String getRemoteEndpointId() {
        return remoteEndpointId;
    }

    public String getRemoteDeviceId() {
        return remoteDeviceId;
    }

    public String getRemoteEndpointName() {
        return remoteEndpointName;
    }

    public byte[] getPayload() {
        return payload;
    }
}
