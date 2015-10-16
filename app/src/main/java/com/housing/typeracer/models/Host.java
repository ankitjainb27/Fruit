package com.housing.typeracer.models;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class Host {
    private String endpointId;
    private String deviceId;
    private String serviceId;
    private String endpointName;

    public Host(String endpointId, String deviceId, final String serviceId, String endpointName) {
        this.endpointId = endpointId;
        this.deviceId = deviceId;
        this.serviceId = serviceId;
        this.endpointName = endpointName;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getEndpointName() {
        return endpointName;
    }
}
