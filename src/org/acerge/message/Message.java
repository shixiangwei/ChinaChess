package org.acerge.message;

import java.io.Serializable;

public interface Message extends Serializable{
	public boolean isLocalMessage();
	public Header getMessageHeader();
	public void setMessageHeader(Header header);
	public Object getMessageBody();
	public void setMessageBody(Object ob);
}
