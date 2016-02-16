package org.acerge.message;

public class MessageQueueNotSetException extends Exception {
	public MessageQueueNotSetException(){
		super("Message queue not set!");
	}
}
