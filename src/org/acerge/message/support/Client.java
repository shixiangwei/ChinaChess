/*
 * �������� 2005-3-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */

package org.acerge.message.support;
import java.io.*;
import java.net.*;
/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class Client {
	Socket clientSocket = null;
	DataOutputStream out;
	DataInputStream in;
	private String serverIP;
	int port;
	public boolean isConnected;
	//HistoryState state = null;
	public Client(String ipAdd,int portnum){
		this.serverIP = ipAdd;
		port = portnum;
		isConnected = false;
	}
	
	public void createConnect(){
		try {
			clientSocket = new Socket(InetAddress.getByName(serverIP),port);
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			isConnected = true;
		}catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost");
		}catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: localhost.");
		} 
	}
	public void sendAMessage(String message){
		try {
			out.writeUTF(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String receiveAMessage(){
		String message;
		try{
			message = in.readUTF();	
		}catch (IOException e){
			System.err.println("Cannot receive a message!");
			e.printStackTrace();
			return null;
		}
		return message;	
	}
}
