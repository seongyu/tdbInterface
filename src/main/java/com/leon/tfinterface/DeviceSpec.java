package com.leon.tfinterface;

import java.util.HashMap;

public class DeviceSpec{
	String DeviceID; 
	String isSubSystemOf;
	String IP;
	String OS;
	String Service;
	HashMap<String, String> Sensors;
	
	public DeviceSpec() {
		Sensors = new HashMap<String, String>();
	}
	
	public DeviceSpec(String DeviceID, String isSubSystemOf, String os, String IP, String service) {
        this.DeviceID = DeviceID;
        this.OS = os;
        this.Service = service;
        this.isSubSystemOf = isSubSystemOf;
        this.IP = IP;
	}
	
	public DeviceSpec(String DeviceID, String isSubSystemOf, String os, String IP, String service, HashMap<String, String> Sensors) {
        this.DeviceID = DeviceID;
        this.OS = os;
        this.Service = service;
        this.isSubSystemOf = isSubSystemOf;
        this.IP = IP;
        this.Sensors = Sensors;
	}
}

