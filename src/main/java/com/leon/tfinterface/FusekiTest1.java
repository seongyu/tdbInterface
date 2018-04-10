package com.leon.tfinterface;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.jena.datatypes.xsd.XSDDateTime;

import net.model.report.FaultsReport;
import net.connection.FusekiAdapter;
import net.model.devices.Device;
import net.model.devices.DeviceStatus;
import net.model.observations.Observation;
import net.model.report.FiredReport;
import net.model.sensors.Sensor;
import net.model.sensors.SensorInfo;
import net.model.service.FireDetectionService;
import net.query.Notation;


public class FusekiTest1 {
    public static void main(String[] args) {
    	String host = "163.180.140.60";
    	String port = "3030";
        FusekiAdapter fuseki_adapter = new FusekiAdapter(host,port);

//      //add device
//      int i = 4;
//      Device dev;
//      for(int j =0; j<=i; j++) {
//          dev = ExampleData.createDevice(100 + j);
//          fuseki_adapter.deviceQL.addDevice(dev);
//          for (Sensor sens : dev.hasSensors) {
//              Observation obs = ExampleData.createObservation(sens.getId(), dev.DeviceID);
//              fuseki_adapter.observationQL.addObservation(obs);
//              DeviceStatus dvs = ExampleData.createDeviceStatus(dev.getDeviceID());
//              fuseki_adapter.monitorQL.addDeviceStatus(dvs);
//              
//          
//
//          }
//      }
//
//
//      SensorInfo tmps1 = new SensorInfo("TestNode100Thermometer","Thermometer",2,2 ); //"Fault"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps1,new XSDDateTime(Calendar.getInstance()));
//      SensorInfo tmps2 = new SensorInfo("TestNode100HumiditySensor","HumiditySensor",2,2 ); 	//"Fault"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps2,new XSDDateTime(Calendar.getInstance()));
//
//      SensorInfo tmps3 = new SensorInfo("TestNode101Thermometer","Thermometer",0,0); //"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps3,new XSDDateTime(Calendar.getInstance()));
//      SensorInfo tmps4 = new SensorInfo("TestNode101HumiditySensor","HumiditySensor",0,0 ); 	//"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps4,new XSDDateTime(Calendar.getInstance()));
//
//      SensorInfo tmps5 = new SensorInfo("TestNode102Thermometer","Thermometer",0,0 ); //"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps5,new XSDDateTime(Calendar.getInstance()));
//      SensorInfo tmps6 = new SensorInfo("TestNode102HumiditySensor","HumiditySensor",0,0 ); 	//"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps6,new XSDDateTime(Calendar.getInstance()));
//
//      SensorInfo tmps7 = new SensorInfo("TestNode103Thermometer","Thermometer",0,0 ); //"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps7,new XSDDateTime(Calendar.getInstance()));
//      SensorInfo tmps8 = new SensorInfo("TestNode103HumiditySensor","HumiditySensor",0,0 ); 	//"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps8,new XSDDateTime(Calendar.getInstance()));
//
//      SensorInfo tmps9 = new SensorInfo("TestNode104Thermometer","Thermometer",0,0 ); //"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps9,new XSDDateTime(Calendar.getInstance()));
//      SensorInfo tmps0 = new SensorInfo("TestNode104HumiditySensor","HumiditySensor",0,0 ); 	//"Normal"
//      fuseki_adapter.detectionQL.addFaultDetection(tmps0,new XSDDateTime(Calendar.getInstance()));
//


//      System.out.println("1.-----------------------------");
//      Set<String> devIDs= fuseki_adapter.deviceQL.getDeviceIDs();
//      System.out.println(devIDs);
////
//      System.out.println("2.-----------------------------");
//      Device dev = fuseki_adapter.deviceQL.getDeviceInformation("Rasp5");
//      System.out.println(dev.DeviceID+": " + dev.IP + ", " +dev.OS + ", " + dev.isSubSystemOf+ ", " + dev.Sensors);
//
//      System.out.println("3.-----------------------------");
//      DeviceStatus dev_status = fuseki_adapter.monitorQL.getLastestDeviceStatus("Rasp5");
//      System.out.println(dev_status.DeviceID+": "+ dev_status.getTimestamp() + ", " + dev_status.Cpu + ", " +dev_status.Cpu_Info + ", " + dev_status.CpuPercentage);
//
//      System.out.println("4.-----------------------------");
//      Sensor sens1 = fuseki_adapter.deviceQL.getSensorInfo("Rasp5","HumiditySensor");
//      if(sens1!=null ){sens1.Print();}
//      else System.out.println("isNull");
//
//      System.out.println("5.-----------------------------");
//      Sensor sens2 = fuseki_adapter.deviceQL.getSensorInfo("Rasp5", "Thermometer");
//      sens2.Print();
//
//      System.out.println("6.-----------------------------");
//      String[] locs = fuseki_adapter.deviceQL.getAllLocations();
//      for (String loc:locs){
//      	System.out.println(loc);
//      }
//      System.out.println("7.-----------------------------");
//      String[] sublocs= fuseki_adapter.deviceQL.getSubLocations("ElectronicBuilding");
//      for (String loc:sublocs){
//      	System.out.println(loc);
//      }
//
//      System.out.println("8.-----------------------------");
//      List<String> devs= fuseki_adapter.deviceQL.getDevInRoom("NetLabRoom");
//      System.out.println(devs);
//
//      System.out.println("9.-----------------------------");
//      List<String> devs1= fuseki_adapter.deviceQL.getDevInRoom("Room351");
//      System.out.println(devs1);
//
//      System.out.println("10.-----------------------------");
//      String SensorID1 = fuseki_adapter.deviceQL.getIdSensor("Rasp5","Thermometer");
//      Observation obs1 = fuseki_adapter.observationQL.getLatestObservation(SensorID1,"Rasp5");
//      obs1.Print();
//
//      System.out.println("11.-----------------------------");
//      List<Observation> obs_list = fuseki_adapter.observationQL.getRecentObs("TestNode102HumiditySensor","TestNode100",60*3600*24); //previous 60days
//      for (Observation obs:obs_list){
//      	obs.Print();
//      }
//      List<Observation> obs_list2 = fuseki_adapter.observationQL.getRecentObs("TestNode102Thermometer","TestNode100",60*3600*24); //previous 60days
//      for (Observation obs:obs_list2){
//          obs.Print();
//      }
//
//      System.out.println("12.-----------------------------");
//      String SensorID2 = fuseki_adapter.deviceQL.getIdSensor("Rasp5","HumiditySensor");
      Observation obs2 = fuseki_adapter.observationQL.getLatestObservation("Rasp2HumiditySensor","Rasp2");
//      Double as = fuseki_adapter.observationQL.getlastestObservation(obs2.DeviceID);
//      obs2.Obs_values = as;
      obs2.Print();
//      System.out.println("12.-----------------------------");
//      List<Observation> obs_list2 = fuseki_adapter.observationQL.getRecentObs(SensorID2,"Rasp5",60*3600*24); //previous 60days
//      for (Observation obs:obs_list2){
//      	obs.Print();
//      }
//
//
//      System.out.println("13.-----------------------------");
//      SensorInfo sensor_faults = fuseki_adapter.detectionQL.getLatestFaultDetection("Rasp2HumiditySensor", "HumiditySensor"
//      		+ "");
//      sensor_faults.Print();
//      System.out.println("Rasp5Thermometer"+": "+ sensor_faults.getTimeStamp() + ", " + sensor_faults.getAbnormal_status() + ", " + sensor_faults.getUpdate_status());

//	    System.out.println("14.-----------------------------");
//	    String SensorID3 = fuseki_adapter.deviceQL.getIdSensor("Rasp5","Thermometer");
//	    ArrayList<FaultsReport> faults = fuseki_adapter.detectionQL.getRecentFaultStatus(SensorID3,0);
//	    for (FaultsReport fault:faults){
//	    	fault.Print();
//	    }
//
//      System.out.println("15.-----------------------------");
//      ArrayList<FiredReport> fired_report = fuseki_adapter.detectionQL.getFireDetectionReport(60);
//      for (FiredReport rep:fired_report){
//	    	rep.Print();
//	    }
//
//
//	    System.out.println("16.-----------------------------");
//	    FireDetectionService service = fuseki_adapter.serviceQL.getFireDetectionSrv("FireDetectionService");
//	    service.Print();
//      System.out.println("17.-----------------------------");
//      HashMap<String, List<String>> tmp = fuseki_adapter.detectionQL.get_latest_sens_sta("HumiditySensor");
//      for (String key : tmp.keySet()
//              ) {
//          System.out.println(key + ": " + tmp.get(key));
//      }
//      System.out.println("18.-----------------------------");
//      HashMap<String, List<String>> tmp2 = fuseki_adapter.detectionQL.get_latest_sens_sta("Thermometer");
//      for (String key : tmp2.keySet()
//              ) {
//          System.out.println(key + ": " + tmp2.get(key));
//
//      }

    }

}