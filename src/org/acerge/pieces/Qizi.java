
package org.acerge.pieces;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;

import org.acerge.main.*;


import java.util.*;
public class Qizi extends JButton {
	protected int coordinateX;//coordinate
	protected int coordinateY;
	protected int pieceType;
	protected int pieceSize;
	protected char pieceName;
	protected boolean isDead;//״̬
	protected Image image;
	//copy 
	public Qizi(Qizi qz){
		this(qz.pieceName,qz.pieceType, qz.coordinateX, qz.coordinateY, qz.image, qz.pieceSize);
	}
	public String toString(){
		return ""+getPieceName() + coordinateX +"," + coordinateY;
	}
	public char getPieceName(){
		return pieceName;
	}
	public int getPieceType(){
		return pieceType;
	}
	public void setGridSize(int gsize){
		pieceSize = (int)(gsize*0.9);
		this.setSize(pieceSize,pieceSize);
	}
	public int getCoordinateX(){
		return coordinateX; 
	}
	public int getCoordinateY(){
		return coordinateY;
	}
	public void setCoordinate(int x,int y){
		coordinateX = x;
		coordinateY = y;
	}
	public void setCoordinate(int square){
		coordinateX = square/10;
		coordinateY = square%10;
	}
	public int getCoordinate(){
		return coordinateX*10+coordinateY;
	}
	public void setCoordinateX(int x){
		coordinateX = x;
	}
	public void setCoordinateY(int y){
		coordinateY = y;
	}
	public void setToDead(){
		isDead = true;
	}
	public void setToAlive(){
		isDead = false;
	}
	private Qizi(){
		isDead = false;
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setBorderPainted(false);
		this.setOpaque(false);
		this.setVisible(false);
	}
	public Qizi(char name, int type){
		this();
		pieceName = name;pieceType = type;
	}
	public Qizi(char name, int type, Image img,int gsize){
		this();
		pieceName = name;pieceType = type;setImage(img,gsize);
	}
	
	public Qizi(char name,int type, int x, int y, Image img, int gsize){
		this();
		coordinateX = x;
		coordinateY = y;
		pieceType = type;
		pieceName = name;
		setImage(img,gsize);
	}
	public void setPieceSize(int newSize){
		setImage(image, newSize);
	}
	public void setImage(Image img){
		setImage(img, pieceSize);
	}
	public void setImage(Image img,int gsize){
		pieceSize = gsize;
		image = img.getScaledInstance(pieceSize,pieceSize,Image.SCALE_SMOOTH);
		this.setSize(pieceSize,pieceSize);
		this.setIcon(new ImageIcon(image));
	}

	public void resetState(){
		isDead = false;
		this.setBorderPainted(false);
		this.setVisible(false);
		this.setEnabled(true);
		isDead = false;						
	}
}


