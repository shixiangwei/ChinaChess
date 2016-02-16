/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package org.acerge.message.support;

import org.acerge.message.Message;
import org.acerge.message.Producer;


public class OuterMsgReceiver extends Thread{
	private Connection connection;
	private Producer producer;
	private boolean acceptData;
	private boolean available(){
		if (producer==null || connection==null) return false;
		return connection.available();
	}
	public OuterMsgReceiver(){
		super("OuterReceiver");
		connection = null;
		producer = null;
	}
	public OuterMsgReceiver(Producer producer){
		super("OuterReceiver");
		this.producer = producer;
		connection = null;
	}

	public OuterMsgReceiver(Connection con){
		super("OuterReceiver");
		producer = null;
		connection = con;
	}

	public OuterMsgReceiver(Producer p, Connection con){
		super("OuterReceiver");
		this.producer = p;
		connection = con;
	}
	
	public void startReceiveData(){
		acceptData = true;
		this.start();
	}
	public void stopReceiveData(){
		acceptData = false;
	}

	public void receive(){
		Object data = null;
		if (available()){
			if ((data = connection.receiveData())!=null){
				producer.send((Message) data);
			}	
		}else{
			System.err.println("OuterMsgReceiver is not available, " +
				"you maybe need invoke setMsgQueue(msg) or/and setConnection(con)!!");
			stopReceiveData();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void run() {
		while (acceptData){
			receive();
		}			
	}
	public Producer getProducer() {
		return producer;
	}
	public void setProducer(Producer producer) {
		this.producer = producer;
	}
}
