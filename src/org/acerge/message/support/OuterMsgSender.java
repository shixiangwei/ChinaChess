package org.acerge.message.support;

import org.acerge.message.Message;



public class OuterMsgSender {
	private Connection connection;
	public boolean available(){
		return (connection!=null && connection.available());
	}
	public OuterMsgSender(Connection con){
		connection = con;
	}
	public OuterMsgSender(){
		connection = null;
	}
	
	public void send(Message msg){
		if(available())
			connection.sendData(msg);
		else
			System.err.println("OuterMsgSender is not available, " +
				"you maybe need invoke setConnection(con)!");

	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}

