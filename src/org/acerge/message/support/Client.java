/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */

package org.acerge.message.support;
import java.io.*;
import java.net.*;
/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
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
