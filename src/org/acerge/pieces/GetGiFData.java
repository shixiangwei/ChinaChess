package org.acerge.pieces;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class GetGiFData {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		GetGiFData ggf=new GetGiFData();
		//ggf.writeFile();
		ggf.readFile();
	}
	public void readFile() throws IOException, ClassNotFoundException{
		InputStream stream = new BufferedInputStream(new FileInputStream("./data/pieceObj"));
		ObjectInputStream ois=new ObjectInputStream(stream);
		PiecesObject piece=(PiecesObject)ois.readObject();
		BufferedImage i=(BufferedImage) piece.getImage(13);
		 ImageIO.write(i,"gif",new File("E:\\13.gif")); 
		ois.close();
	} 
	
	
	public void writeFile()	throws IOException{
		InputStream streams[] ={
			new BufferedInputStream(new FileInputStream("./image/wking.gif")),
			new BufferedInputStream(new FileInputStream("./image/wadvisor.gif")),
			new BufferedInputStream(new FileInputStream("./image/wbishop.gif")),
			new BufferedInputStream(new FileInputStream("./image/wknight.gif")),
			new BufferedInputStream(new FileInputStream("./image/wrook.gif")),
			new BufferedInputStream(new FileInputStream("./image/wcannon.gif")),
			new BufferedInputStream(new FileInputStream("./image/wpawn.gif")),
				
			new BufferedInputStream(new FileInputStream("./image/bking.gif")),
			new BufferedInputStream(new FileInputStream("./image/badvisor.gif")),
			new BufferedInputStream(new FileInputStream("./image/bbishop.gif")),
			new BufferedInputStream(new FileInputStream("./image/bknight.gif")),
			new BufferedInputStream(new FileInputStream("./image/brook.gif")),
			new BufferedInputStream(new FileInputStream("./image/bcannon.gif")),
			new BufferedInputStream(new FileInputStream("./image/bpawn.gif"))};
			
		int[] types={0,1,2,3,4,5,6,7,8,9,10,11,12,13};
		int[] fileSize=new int[14];
		int totalSize=0;
		byte[][] content=new byte[14][];
		for(int i=0;i<14;i++){
			fileSize[i]=streams[i].available();
			content[i]=new byte[fileSize[i]];
			streams[i].read(content[i]);
			streams[i].close();
		}
		byte[] header={7,9,1,1,0,1,2,5,3};
		PiecesObject piece=new PiecesObject(header,types,content);
		ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream("./data/pieceObj"));
		oos.writeObject(piece);
		oos.close();
		System.out.println("totalSize:"+totalSize);
		//OutputStream os=new BufferedOutputStream(new FileOutputStream("data.pic"));
	}
	
	public static void encode(byte[] data){
	}
}
