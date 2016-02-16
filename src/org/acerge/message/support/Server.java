
package org.acerge.message.support;
import java.io.*;
import java.net.*;

public class Server extends Thread{
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	DataOutputStream currentOut=null;
	DataInputStream currentIn=null;
	int port;
	public boolean isConnected;

	public Server(int portNum){
		port = portNum;
	}
	public void createConnect(){
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port:" + port);
		}
		try {
			clientSocket = serverSocket.accept();
			currentIn = new DataInputStream(new BufferedInputStream(
			clientSocket.getInputStream()));
			currentOut =new DataOutputStream(new BufferedOutputStream(
					clientSocket.getOutputStream()));
			isConnected = true;
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);	
		}				
	}

	public void sendAMessage(String message){
		try {
			currentOut.writeUTF(message);
			currentOut.flush();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String receiveAMessage(){
		String message;
		try{
			message = currentIn.readUTF();	
		}catch (IOException e){
			System.err.println("Cannot receive a message!");
			e.printStackTrace();
			return null;
		}
		return message;	
	}
}
