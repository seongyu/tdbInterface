package com.leon.tfinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class SocketOn extends Thread {
	ServerSocket serverSocket;
	private static Socket socket;
	
	public SocketOn(int port) throws IOException {
		// TODO Auto-generated constructor stub
		serverSocket = new ServerSocket(port);
	}
	
	@Override
	public void run() {
		System.out.println("socket is available : "+ serverSocket.getLocalPort());
		while(true) {
			try {
				socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String msg = br.readLine();
	            System.out.println(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class SocketServerTest {
	public static void main(String[] args) throws IOException {
		int kaistPORT = 9997;
		Thread thc = new SocketOn(kaistPORT);
		System.out.println("Thread Start in Socket Test");
		thc.start();
		
	}
}