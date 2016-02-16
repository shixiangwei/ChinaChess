package org.acerge.message.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.acerge.message.Header;
import org.acerge.message.Message;
import org.acerge.message.impl.ChessMessage;


public class ObjectCopyer {
	//private Message currentMsg=null;
	//private ByteBuffer objstream;
	public static Object getACopy(Object srcObject){
		if (srcObject==null) return null;
		ByteArrayInputStream bis=null;
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(srcObject);
			oos.close();
			bis=new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois=new ObjectInputStream(bis);
			Object obj=ois.readObject();
			ois.close();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	//for test
	public static void main(String[] args){
		Message msg=new ChessMessage(Header.AGREE,"agree",false);
		Message msg1=(Message)ObjectCopyer.getACopy(msg);
		Message msg2=(Message)ObjectCopyer.getACopy(msg);
		System.out.println(msg);
		System.out.println(msg1);
		System.out.println(msg2);
		System.out.println("msg1==msg2? "+msg1.equals(msg2));
	}
}
