package com.leon.tfinterface;

import java.io.IOException;
import java.net.Socket;

import org.bson.Document;

import com.leon.tfinterface.PutToMongo;
import com.leon.tfinterface.CollectApi;

import com.mongodb.client.MongoCollection;

import com.leon.tfinterface.SocketApi;

class RunCollectDevice extends Thread {
	static String MODE = "T";
	
	String DB = "DEVICE_";
	SocketApi socketApi;
	
	static PutToMongo ptm = new PutToMongo();
	
	public RunCollectDevice(SocketApi SA) {
		socketApi = SA;
	}

	@Override
	public void run() {
		CollectApi colapi = new CollectApi();
		
		MongoCollection<Document> devices = ptm.createPool(DB+MODE);
		int thread_time = 20*1000; // each 10 seconds
		System.out.println("Start Device Collect Thread...");
		while(true) {
			colapi.upsertDevice(devices,socketApi);
			try {
				Thread.sleep(thread_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class RunCollectDeviceStatus extends Thread {
	static String MODE = "T";
	
	String DB = "DEVICE_STATUS_";
	SocketApi socketApi;
	
	static PutToMongo ptm = new PutToMongo();
	
	public RunCollectDeviceStatus(SocketApi SA) {
		socketApi = SA;
	}

	@Override
	public void run() {
		CollectApi colapi = new CollectApi();
		MongoCollection<Document> device_status = ptm.createPool(DB+MODE);
		int thread_time = 5*1000; // each 10 seconds
		System.out.println("Start Device Status Collect Thread...");
		while(true) {
			colapi.insertDeviceStatus(device_status,socketApi);
			try {
				Thread.sleep(thread_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class RunCollectSensorData extends Thread {
static String MODE = "T";
	
	String DB_DEVICE = "DEVICE_";
	String DB_SENSOR = "SENSOR_DATA_";
	SocketApi socketApi;
	SocketCli interfaceSocketApi;
	Socket socket;
	
	static PutToMongo ptm = new PutToMongo();
	
	public RunCollectSensorData(SocketApi SA) {
		// TODO Auto-generated constructor stub
		socketApi = SA;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		CollectApi colapi = new CollectApi();
		MongoCollection<Document> device = ptm.createPool(DB_DEVICE+MODE);
		MongoCollection<Document> sensor = ptm.createPool(DB_SENSOR+MODE);
		int thread_time = 5*1000;
		
		while(true) {
			colapi.getSensorData(device,sensor,socketApi);
			try {
				Thread.sleep(thread_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class RunCollectSensor extends Thread {
	static String MODE = "T";
	
	String DB_DEVICE = "DEVICE_";
	String DB_SENSOR = "SENSOR_";
	SocketApi socketApi;
	SocketCli interfaceSocketApi;
	Socket socket;
	
	
	static PutToMongo ptm = new PutToMongo();
	

	public RunCollectSensor(SocketApi SA, SocketCli SA2) {
		socketApi = SA;
		interfaceSocketApi = SA2; 
	}

	@Override
	public void run() {
		CollectApi colapi = new CollectApi();
		MongoCollection<Document> device = ptm.createPool(DB_DEVICE+MODE);
		MongoCollection<Document> sensor = ptm.createPool(DB_SENSOR+MODE);
		int thread_time = 2*1000; // each 2 seconds
		System.out.println("Start Sensors Collect Thread...");
		while(true) {
			colapi.getSensorReport(device,sensor,socketApi,interfaceSocketApi);
			try {
				Thread.sleep(thread_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

public class Collect {
	static SocketApi socketApi;
	static SocketCli interfaceSocketApi;
	static boolean is_started;
	static Thread th1;
	static Thread th2;
	static Thread th3;
	static Thread th4;
	
	public static void StartApp() {
		
		
		String kaistHOST = "143.248.56.32";
		int kaistPORT = 9090;
		
		
		// for local
		kaistHOST = "localhost";
		System.out.println("Work Socket on localhost....");
		Thread so;
		try {
			so = new SocketOn(kaistPORT);
			so.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			interfaceSocketApi = new SocketCli(kaistHOST,kaistPORT);
			System.out.println("Try to socket connect : " + kaistHOST + ":" + kaistPORT);
			
			
			is_started = true;
		}catch(Exception e) {
			is_started = false;
		}
		
		if(is_started){
			socketApi = new SocketApi(9999);
			
			socketApi.start();
			
			th1 = new RunCollectDevice(socketApi);
			th2 = new RunCollectDeviceStatus(socketApi);
			th3 = new RunCollectSensorData(socketApi);
			th4 = new RunCollectSensor(socketApi,interfaceSocketApi);
			
			th1.start();
			th2.start();
			th3.start();
			th4.start();
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException {
//		try {
//			if(!args[ 0 ].isEmpty()) {
//				kaistHOST = "localhost";
//				System.out.println("Work Socket on localhost....");
//				Thread so = new SocketOn(kaistPORT);
//				so.start();
//			}
//		} catch ( Exception ex ) {
//			
//		}
		
		// for local
//		kaistHOST = "localhost";
//		System.out.println("Work Socket on localhost....");
//		Thread so = new SocketOn(kaistPORT);
//		so.start();
		while(true) {
			if(!is_started){
				StartApp();
				System.out.println("Service not Started yet....");
			}
			Thread.sleep(30*1000);
		}
	}
}
