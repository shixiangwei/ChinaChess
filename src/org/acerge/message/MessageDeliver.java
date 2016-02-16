package org.acerge.message;

public interface MessageDeliver {
	public void registerAConsumer(Consumer consumer);
	public void removeAConsumer(Consumer consumer);
	public void deliveryAMessage(Message m);
}
