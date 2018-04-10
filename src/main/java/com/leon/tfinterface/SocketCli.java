package com.leon.tfinterface;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class SocketCli extends Socket implements Serializable {
	public static Socket socket;
	
	public SocketCli(String host, int port) throws IOException {
		// TODO Auto-generated constructor stub
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);
	}
	
	public void SendSocketMessage(String msg) throws IOException {
		OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(msg+"\n");
        bw.flush();
	}
}
