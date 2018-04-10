package com.leon.tfinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.bson.Document;

import com.leon.tfinterface.SocketApi;
import com.leon.tfinterface.DeviceSpec;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import static com.mongodb.client.model.Filters.*;

import net.connection.FusekiAdapter;
import net.model.devices.Device;
import net.model.devices.DeviceStatus;
import net.model.observations.Observation;
import net.model.sensors.Sensor;
import net.model.sensors.SensorInfo;

public class CollectApi {

	private static String FT_HOST = "163.180.140.60";
	private static String FT_PORT = "3030";
	private static FusekiAdapter fuseki_adapter = new FusekiAdapter(FT_HOST,FT_PORT);
	
	/*
	 * Get and Upsert Devices
	 * ==============
		String DeviceID              
		String isSubSystemOf   
		String IP   
		String OS   
		String Service   
		List<String,String> Sensors, SensorNames 
		----> Upsert to Mongo
	*
	*/
	public void upsertDevice(MongoCollection<Document> collection,SocketApi socketApi) {
		ArrayList<DeviceSpec> devices = _getDevices();
		
		try {
			UpdateOptions options = new UpdateOptions().upsert(true);
				for(DeviceSpec device:devices) {
					Document doc = new Document("device_id",device.DeviceID)
							.append("ip", device.IP)
							.append("os", device.OS)
							.append("service", device.Service)
							.append("is_sub_system", device.isSubSystemOf)
							.append("sensors", device.Sensors)
							.append("timestamp", new Date());
	
					String json = doc.toJson();
					socketApi.broadcast(json);
					collection.updateOne(eq("device_id",device.DeviceID), new Document("$set", doc),options);
				}
		}catch(Exception e) {
			
		}
	}
	
	
	/*
	 * Get and Insert Devices Status
	 * =====================
		String DeviceID   
		DateTime timestamp
		double Cpu
		String Cpu_Info
		double CpuPercentage
		double Memory
		double RamPercentage
		double Disk
		double DiskPercentage
		String bootTime
		---> insert to Mongo
	 * 
	 */
	public void insertDeviceStatus(MongoCollection<Document> collection,SocketApi socketApi) {
		ArrayList<Document> insertList = new ArrayList<Document>();
		
		Set<String> devIds= fuseki_adapter.deviceQL.getDeviceIDs();
		
		try {
			for(String devId:devIds) {
					DeviceStatus devsys = fuseki_adapter.monitorQL.getLastestDeviceStatus(devId);
					if(devsys != null) {
						Document doc = new Document("device_id",devsys.DeviceID)
							.append("cpu", devsys.Cpu)
							.append("cpu_info", devsys.Cpu_Info)
							.append("cpu_usage", devsys.CpuPercentage)
							.append("memory", devsys.Memory)
							.append("memory_usage", devsys.RamPercentage)
							.append("disk", devsys.Disk)
							.append("disk_usage", devsys.DiskPercentage)
							.append("boot", devsys.bootTime)
							.append("timestamp", devsys.timestamp.asCalendar().getTime());
						String json = doc.toJson();
						socketApi.broadcast(json);
						insertList.add(doc);
					}
				}
				
				if(insertList.size()>0) {
					collection.insertMany(insertList);
				}
		}catch(Exception e) {
			
		}
	}
	
	public void getSensorData(MongoCollection<Document> collection_a, MongoCollection<Document> collection_b, SocketApi socketApi) {
		ArrayList<Document> insertList = new ArrayList<Document>();
		
		try {
			for(Document doc:collection_a.find()) {
				Document item = doc.get("sensors",Document.class);
				
				for(String it:item.keySet()) {
					Observation obs = fuseki_adapter.observationQL.getLatestObservation(item.get(it).toString(),doc.getString("device_id"));
					
//					Double as = fuseki_adapter.observationQL.getlastestObservation(obs.DeviceID);
					Document inputs = new Document("device_id",obs.DeviceID)
							.append("sensor_id",item.get(it))
							.append("observed_by",obs.observedBy)
							.append("value", obs.Obs_values)
							.append("timestamp", obs.timestamp.asCalendar().getTime());
					socketApi.broadcast(inputs.toJson());
					System.out.println(obs.Obs_values);
					insertList.add(inputs);
				}
			}
			
			if(insertList.size()>0) {
				collection_b.insertMany(insertList);	
			}
		}catch(Exception e) {
			
		}
	}
	/*
	 * Get and Insert Sensor Fault Reports
	 * =====================
		String DeviceID   
		DateTime timestamp
		String Sens_type
		int abnormal_value_status
		 {0 : normal, 1 : temporary, 2 : fault, 3 : off}
		  => sensor status
		int sensing_period_status
		 {0 : normal, 1 : temporary, 2 : fault, 3 : off}
		  => update status
		---> insert to Mongo
	 * 
	 */
	public void getSensorReport(MongoCollection<Document> collection_a, MongoCollection<Document> collection_b, SocketApi socketApi, SocketCli interfaceSocketApi) {
		ArrayList<Document> insertList = new ArrayList<Document>();
		try {
			for(Document doc:collection_a.find()) {
				Document item = doc.get("sensors",Document.class);
				try {
					for(String it:item.keySet()) {
						
						SensorInfo sensor_faults = fuseki_adapter.detectionQL.getLatestFaultDetection(item.getString(it), it);
						String device_id = sensor_faults.DeviceID;
						
						if(device_id == null) {
							device_id = doc.getString("device_id");
						}
						
						
						Document inputs = new Document("device_id",device_id)
								.append("sensor_type", sensor_faults.Sens_type)
								.append("sensor_status", sensor_faults.getAbnormal_status())
								.append("update_status", sensor_faults.getUpdate_status())
								.append("timestamp", sensor_faults.mtime.asCalendar().getTime());
						
						String json = inputs.toJson();
						socketApi.broadcast(json);
						if(sensor_faults.abnormal_value_status == 2) {
								doc.append("fault_detact", inputs);
								String faultDevice = doc.toJson();
								try {
									interfaceSocketApi.SendSocketMessage(faultDevice);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						
						insertList.add(inputs);
						
					}
				}catch(Exception e) {
					
				}
			}
			if(insertList.size()>0) {
				collection_b.insertMany(insertList);	
			}
		}catch(Exception ex) {
			
		}
	}

	/*
	 * Get Devices
	 * ==============
		String DeviceID              
		String isSubSystemOf   
		String IP   
		String OS   
		String Service   
		List<String,String> Sensors, SensorNames 
	*
	*/
	public static ArrayList<DeviceSpec> _getDevices(){
		Set<String> devIds= fuseki_adapter.deviceQL.getDeviceIDs();
		ArrayList<DeviceSpec> devSets = new ArrayList<DeviceSpec>();
		try {
			for(String devId:devIds) {
				Device dev = fuseki_adapter.deviceQL.getDeviceInformation(devId);
				DeviceSpec devSet = new DeviceSpec();
				devSet.DeviceID = dev.DeviceID;
				devSet.Service = dev.Service;
				devSet.OS = dev.OS;
				devSet.isSubSystemOf = dev.isSubSystemOf;
				devSet.IP = dev.IP;
				for(String sensor:dev.Sensors) {
					if(!sensor.toString().equals("NamedIndividual") ) {
						Sensor ssr = fuseki_adapter.deviceQL.getSensorInfo(devId,sensor);
						if(ssr != null) {
							devSet.Sensors.put(ssr.getType(), ssr.getId());
						}	
					}
				}
				devSets.add(devSet);
				
			}
		}catch(Exception e) {
			
		}
		return devSets;
	}
}
