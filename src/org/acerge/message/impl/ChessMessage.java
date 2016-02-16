package org.acerge.message.impl;

import org.acerge.message.Header;
import org.acerge.message.Message;

public class ChessMessage implements Message{
	private boolean local;
	private Object body;
	private Header header;
	public ChessMessage(Header header,Object body,boolean local){
		this.header=header;
		this.body=body;
		this.local=local;
	}
	public boolean isLocalMessage() {
		return local;
	}
	public Header getMessageHeader() {
		return header;
	}
	public void setMessageHeader(Header header) {
		this.header=header;
	}
	public Object getMessageBody() {
		return body;
	}
	public void setMessageBody(Object body) {
		this.body=body;
	}
}
