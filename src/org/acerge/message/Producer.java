package org.acerge.message;

public interface Producer {
	public void SetDestination(MessageQueue queue);
	public void send(Message msg);
}
