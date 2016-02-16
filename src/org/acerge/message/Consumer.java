package org.acerge.message;

public interface Consumer {
	public void consumeMessage(Message massage);
	public void RegisterAListener(MessageListener listener);
	public void removeAListener(MessageListener listener);
}
