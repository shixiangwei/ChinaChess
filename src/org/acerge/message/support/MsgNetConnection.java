package org.acerge.message.support;

import java.io.*;
import java.net.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class MsgNetConnection extends Thread implements Connection{
	private int pointType;//1:Master,2:Client
	private Server masterPoint;
	private Client clientPoint;
	private boolean available;
	private static Log log=LogFactory.getLog(MsgNetConnection.class);
	public MsgNetConnection(){
		pointType = 0;
		masterPoint = null;
		clientPoint = null;
	}
	public DataOutputStream getDataOutputStream(){
		if (pointType == 1) 
			return masterPoint.currentOut;
		else 
			return clientPoint.out;
	}	
	public DataInputStream getDataInputStream(){
		if (pointType == 1) 
			return masterPoint.currentIn;
		else 
			return clientPoint.in;
	}
	public int getPointType() {
		return pointType;
	}

	public void setPointAsServer(int port){
		pointType = 1;
		masterPoint = new Server(port);
		clientPoint = null;
	}
	public void setPointAsClient(String ipAddress, int port){
		pointType = 2;
		clientPoint = new Client(ipAddress, port);
		masterPoint = null;
	}
	public boolean available() {
		return available;
	}
	public void sendData(Object data) {
		ObjectOutputStream out;
		DataOutputStream dos = this.getDataOutputStream();
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(dos));
			out.writeObject(data);
			out.flush();
		} catch (SocketException exc){
			available = false;
			log.info(exc.getMessage());
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}
	public Object receiveData() {
		ObjectInputStream in ;
		DataInputStream dis = this.getDataInputStream();
		Object result = null;	
		try {
			in = new ObjectInputStream(new BufferedInputStream(dis));
			result = in.readObject();
		} catch (SocketException e){
			available = false;
			log.info(e.getMessage());
		} catch (IOException e) {
			log.info(e.getMessage());
			available = false;
			return null;
		} catch (ClassNotFoundException e) {
			log.info(e.getMessage());
			return null;
		}
		return result;
	}
	public void createConnection(){
		if (pointType == 1){
			System.out.println("Accept connecting from Client ....");
			masterPoint.createConnect();
			if (masterPoint.isConnected) {
				available = true;
				System.out.println("A client has connected!!");
			} 				
		}else{
			while(!clientPoint.isConnected){
				log.info("Trying connected to Server ... ");
				clientPoint.createConnect();
				if (clientPoint.isConnected){
					available = true;
					log.info("Have Already Connected to Server!!");
					return;
				}else{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.info(e.getMessage());
					}					
				}
			}
			if (!available){
				log.info("Cannot get connection!!!");
			}
		}
	}
}
