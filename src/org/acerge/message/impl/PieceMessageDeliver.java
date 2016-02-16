package org.acerge.message.impl;

import java.util.ArrayList;
import java.util.List;



import org.acerge.message.Consumer;
import org.acerge.message.Message;
import org.acerge.message.MessageDeliver;
import org.acerge.message.MessageQueue;
import org.acerge.message.support.ObjectCopyer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PieceMessageDeliver extends Thread implements MessageDeliver{
	private static Log log=LogFactory.getLog(PieceMessageDeliver.class);
	private MessageQueue msgQueue=null;
	private List consumers=new ArrayList();
	public PieceMessageDeliver(MessageQueue msgQueue){
		this.msgQueue=msgQueue;
	}
	public void registerAConsumer(Consumer consumer) {
		consumers.add(consumer);
	}

	public void removeAConsumer(Consumer consumer) {
		consumers.remove(consumer);
	}
	public void deliveryAMessage(Message msg) {
		//copyer.SetMessage(msg);
		for(int i=0;i<consumers.size();i++){
			Message newMsg=(Message)ObjectCopyer.getACopy(msg);
			((Consumer)consumers.get(i)).consumeMessage(newMsg);
		}
	}
	public void run(){
		while(true){
			Message m=null;				
			synchronized(msgQueue){
				if(msgQueue.isEmpty())
					try {
						//msgQueue.notifyAll();
						msgQueue.wait();
						log.info("all message delivered!");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				m=msgQueue.getAMessage();
				msgQueue.notifyAll();
			}
			deliveryAMessage(m);
		}
	}
}
