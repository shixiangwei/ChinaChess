package org.acerge.message.impl;

import java.util.ArrayList;

import org.acerge.message.Consumer;
import org.acerge.message.Message;
import org.acerge.message.MessageListener;
import org.acerge.message.support.ObjectCopyer;



public class MessageConsumer implements Consumer{
	private ArrayList listeners=new ArrayList();
	public void consumeMessage(Message msg) {
		for(int i=0;i<listeners.size();i++){
			//copyer.SetMessage(msg);
			Message newMsg=(Message)ObjectCopyer.getACopy(msg);
			((MessageListener)listeners.get(i)).onMessage(newMsg);
		}
	}
	public void RegisterAListener(MessageListener listener) {
		listeners.add(listener);
	}
	public void removeAListener(MessageListener listener) {
		listeners.remove(listener);
	}

}
