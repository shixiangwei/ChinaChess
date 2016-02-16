package org.acerge.message.impl;

import java.util.ArrayList;

import org.acerge.message.Message;
import org.acerge.message.MessageQueue;


public class ChessMessageQueue implements MessageQueue{
	private ArrayList msgQueue=new ArrayList();
	public synchronized Message getAMessage() {
		if(msgQueue.size()>0) return (Message) msgQueue.remove(0);
		return null;
	}

	public synchronized void putAMessage(Message msg) {
		msgQueue.add(msg);
	}

	public synchronized boolean isEmpty() {
		return msgQueue.size()==0;
	}
	public synchronized void removeAll(){
		msgQueue.clear();
	}
}
