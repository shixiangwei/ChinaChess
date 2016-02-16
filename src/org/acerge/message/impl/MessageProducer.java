package org.acerge.message.impl;

import org.acerge.message.Message;
import org.acerge.message.MessageQueue;
import org.acerge.message.Producer;

public class MessageProducer implements Producer{
	private MessageQueue queue;
	public MessageProducer(MessageQueue queue){
		this.queue=queue;
	}
	public void SetDestination(MessageQueue queue) {
		this.queue=queue;
	}

	public void send(Message msg){
		if(queue==null) {
			System.err.println("From MessageProducer:message queue is null!");
			return;
		}
		synchronized(queue){
			queue.putAMessage(msg);
			queue.notifyAll();
		}
	}

}
