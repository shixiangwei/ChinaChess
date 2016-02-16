package org.acerge.pieces;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class PiecesObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8909096566771666564L;
	//pieceName:[a,A=advisor],[b,B=bishop],[k,K=king],[p,P=pawn]
	//[r,R=rook],[c,C=cannon],[n,N=knight],Capital=White
	private int[] piecesType=null;
	private byte[][] pictureData=null;
	private byte[] header=null;
	private boolean decoded=false;
	private PiecesObject instance;
	public PiecesObject(){}
	public PiecesObject(byte[] h,int[] piecesType,byte[][] pictureData){
		this.header=h;
		this.piecesType=piecesType;
		this.pictureData=pictureData;
	}
	public int[] getPiecesType(){
		return piecesType;
	}
	public Image getImage(int pieceType){
		//return Toolkit.getDefaultToolkit().createImage(pictureData[pieceType]);
		ByteArrayInputStream bais = new ByteArrayInputStream(pictureData[pieceType]);  
		
		try {
			BufferedImage 	bi1 = ImageIO.read(bais);
			return bi1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//return Toolkit.getDefaultToolkit().createImage(pictureData[pieceType]);
	}
	public PiecesObject getInstance(){
		if(instance!=null) return instance;
		InputStream stream=null;
		ObjectInputStream ois=null;
		try {
			//stream = PiecesObject.class.getClassLoader().getResourceAsStream("data/pieceObj");
			stream = new FileInputStream("./data/pieceObj");
			ois = new ObjectInputStream(stream);
			PiecesObject piece=(PiecesObject)ois.readObject();
			ois.close();
			instance=piece;
			return instance;
		} catch (IOException e) {
			e.printStackTrace();instance=null;return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();instance=null;return null;
		}
	}
}
