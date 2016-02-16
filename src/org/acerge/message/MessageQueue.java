package org.acerge.message;

public interface MessageQueue {
	public Message getAMessage();
	public void putAMessage(Message msg);
	public boolean isEmpty();
	public void removeAll();
}
