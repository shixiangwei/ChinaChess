package org.acerge.main;
	
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import org.acerge.pieces.Qizi;

	
public class PictureBoard extends JPanel{
	JLabel test = new JLabel("x=0,y=0");
	private final static Color fg = Color.black;
	private final static Color red = Color.red;
	private int boardGridSize=50;
	private final static int left=1 ,right=2,center=3;
	private int lineLoc;
	public PictureBoard() {
		setLineLoc();
		this.setLayout(null);
		this.setVisible(true);		
	}
	public void setLineLoc(){
		lineLoc = boardGridSize*2/3;
	}
	public int getLineLoc(){
		return lineLoc;
	}
	public void setBoardGridSize(int gridx){
		boardGridSize=gridx;
		setLineLoc();
	}
	public int getBoardGridSize(){
		return boardGridSize;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension d=getSize();
		Color fg3D = Color.lightGray;
		g2.setPaint(fg3D);
		g2.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
		g2.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
		g2.setPaint(fg);
		test.setBounds(boardGridSize*4+10,boardGridSize*5-20,100,50);
		this.add(test);
		paintBoard(g2);
	}
		
	public void drawX(double x0,double y0,Graphics2D g2){
		//
		g2.draw(new Line2D.Double(x0-boardGridSize,y0-boardGridSize,x0+boardGridSize,y0+boardGridSize));
		g2.draw(new Line2D.Double(x0-boardGridSize,y0+boardGridSize,x0+boardGridSize,y0-boardGridSize));
	}
    
	public void drawLocation(double x0,double y0,int loc/*left=1,right=2*,center=3*/,Graphics2D g2){
		double xlen=boardGridSize/3;
		if (loc==left){
			g2.draw(new Line2D.Double(x0-xlen/4,y0-xlen/4,x0-xlen/4,y0-xlen/4-xlen));
			g2.draw(new Line2D.Double(x0-xlen/4,y0-xlen/4,x0-xlen/4-xlen,y0-xlen/4));
			g2.draw(new Line2D.Double(x0-xlen/4,y0+xlen/4,x0-xlen/4,y0+xlen/4+xlen));
			g2.draw(new Line2D.Double(x0-xlen/4,y0+xlen/4,x0-xlen/4-xlen,y0+xlen/4));
		}
		else{
			g2.draw(new Line2D.Double(x0+xlen/4,y0-xlen/4,x0+xlen/4,y0-xlen/4-xlen));
			g2.draw(new Line2D.Double(x0+xlen/4,y0-xlen/4,x0+xlen/4+xlen,y0-xlen/4));
			g2.draw(new Line2D.Double(x0+xlen/4,y0+xlen/4,x0+xlen/4,y0+xlen/4+xlen));
			g2.draw(new Line2D.Double(x0+xlen/4,y0+xlen/4,x0+xlen/4+xlen,y0+xlen/4));
		}
		if (loc==center){
			g2.draw(new Line2D.Double(x0-xlen/4,y0-xlen/4,x0-xlen/4,y0-xlen/4-xlen));
			g2.draw(new Line2D.Double(x0-xlen/4,y0-xlen/4,x0-xlen/4-xlen,y0-xlen/4));
			g2.draw(new Line2D.Double(x0-xlen/4,y0+xlen/4,x0-xlen/4,y0+xlen/4+xlen));
			g2.draw(new Line2D.Double(x0-xlen/4,y0+xlen/4,x0-xlen/4-xlen,y0+xlen/4));    		
		}
    	
    	   
	}
	
	public void paintBoard(Graphics2D g2){          
        
		double gbwidth=boardGridSize*8d+lineLoc,gbheight=boardGridSize*9d+lineLoc;
		g2.setPaint(red);
		g2.draw3DRect((int)lineLoc/2, (int)lineLoc/2, (int)gbwidth,(int)gbheight,true);
		g2.draw3DRect((int)lineLoc/2+3, (int)lineLoc/2+3, (int)gbwidth - 6, (int)gbheight - 7, false);
		g2.setPaint(fg);		
		for (int i=0;i<10;i++){
			g2.draw(new Line2D.Double(lineLoc,lineLoc+boardGridSize*i,lineLoc+boardGridSize*8,lineLoc+boardGridSize*i));
		}
		for (int i=0;i<9;i++){
			g2.draw(new Line2D.Double(lineLoc+boardGridSize*i,lineLoc,lineLoc+boardGridSize*i,lineLoc+boardGridSize*4));
			if (i==0 || i==8)
				g2.draw(new Line2D.Double(lineLoc+boardGridSize*i,lineLoc+boardGridSize*4,lineLoc+boardGridSize*i,lineLoc+boardGridSize*9));			
			else g2.draw(new Line2D.Double(lineLoc+boardGridSize*i,lineLoc+boardGridSize*5,lineLoc+boardGridSize*i,lineLoc+boardGridSize*9));
		}
		drawX(lineLoc+boardGridSize*4,lineLoc+boardGridSize,g2);
		drawX(lineLoc+boardGridSize*4,lineLoc+boardGridSize*8,g2);
		//black Pao
		drawLocation(lineLoc+boardGridSize,lineLoc+boardGridSize*2,center,g2);
		drawLocation(lineLoc+boardGridSize*7,lineLoc+boardGridSize*2,center,g2);
		//red Pao
		drawLocation(lineLoc+boardGridSize,lineLoc+boardGridSize*7,center,g2);
		drawLocation(lineLoc+boardGridSize*7,lineLoc+boardGridSize*7,center,g2);
		//black Zu
		drawLocation(lineLoc,lineLoc+boardGridSize*3,right,g2);
		drawLocation(lineLoc+boardGridSize*2,lineLoc+boardGridSize*3,center,g2);
		drawLocation(lineLoc+boardGridSize*4,lineLoc+boardGridSize*3,center,g2);
		drawLocation(lineLoc+boardGridSize*6,lineLoc+boardGridSize*3,center,g2);
		drawLocation(lineLoc+boardGridSize*8,lineLoc+boardGridSize*3,left,g2);
		//red bin
		drawLocation(lineLoc,lineLoc+boardGridSize*6,right,g2);
		drawLocation(lineLoc+boardGridSize*2,lineLoc+boardGridSize*6,center,g2);
		drawLocation(lineLoc+boardGridSize*4,lineLoc+boardGridSize*6,center,g2);
		drawLocation(lineLoc+boardGridSize*6,lineLoc+boardGridSize*6,center,g2);
		drawLocation(lineLoc+boardGridSize*8,lineLoc+boardGridSize*6,left,g2);
		//----------------------------------------------------------------------
	}
	

	public Dimension getXYCoordinate(int x, int y){
		int r = (int)(boardGridSize*0.45); 
		int templocx = (int)((x-lineLoc+boardGridSize/2.0)/boardGridSize);
		int templocy = (int)((y-lineLoc+boardGridSize/2.0)/boardGridSize);
		if (templocx>=0 && templocx<=8 && templocy>=0 && templocy<=9){
			if ((sqr(x-templocx*boardGridSize-lineLoc)+ 
				sqr(y-templocy*boardGridSize-lineLoc))<sqr(r))
			{
				return new Dimension(templocx,9-templocy);
			}
		}
		return new Dimension(100,100); 
	}
	private int sqr(int x){
		return x*x;
	}
	public Dimension getQiziLocationOnBoard(Qizi qz){
		return new Dimension(qz.getCoordinateX(),qz.getCoordinateY());
	}
}