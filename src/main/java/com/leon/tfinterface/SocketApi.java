package com.leon.tfinterface;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SocketApi extends WebSocketServer implements Serializable{
	private static final long serialVersionUID = -1L;
	public ArrayList<WebSocket> conns = new ArrayList<WebSocket>();
	public static SocketApi sa;
	
	public SocketApi (int port) {
		super(new InetSocketAddress(port));
	}
	
	public SocketApi (InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake hands) {
		
		conns.add(conn);
		System.out.println(conn.getRemoteSocketAddress().getHostString());
		broadcast("New connection created : " + hands.getResourceDescriptor());
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		conns.remove(conn);
		broadcast("Left connection : " + conn);
		System.out.println("Left connection : " + conn);
	}
	
	@Override
	public void onMessage(WebSocket conn, String msg) {
		// TODO Auto-generated method stub
		broadcast(msg);
	}
	
	@Override
	public void onMessage(WebSocket conn, ByteBuffer msg) {
		// TODO Auto-generated method stub
		broadcast(msg.array());
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("WebSocketServer started successfully");
	}
	
	@Override
	public void onError(WebSocket conn, Exception err) {
		// TODO Auto-generated method stub
		try {
			sa.stop(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		err.printStackTrace();
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		WebSocketImpl.DEBUG = true;
		int PORT = 9002;
		String HOST = "0.0.0.0";

		InetSocketAddress address= new InetSocketAddress(HOST,PORT);
		try {
			PORT = Integer.parseInt(args[0]);
		}catch (Exception e) {
			// TODO: handle exception
		}
		sa = new SocketApi(address);
		sa.start();
		
//		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		
//		while(true) {
//			String in = sysin.readLine();
//			sa.broadcast(in);
//			if(in.equals("exit")) {
//				sa.stop(1000);
//				break;
//			}
//		}
	}
	
}