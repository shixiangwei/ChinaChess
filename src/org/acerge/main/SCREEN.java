package org.acerge.main;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

public final class SCREEN {
	public static int WIDTH;
	public static int HEIGHT;
	static{
		GraphicsEnvironment ge;
		ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		WIDTH=ge.getDefaultScreenDevice().getDisplayMode().getWidth();
		HEIGHT=ge.getDefaultScreenDevice().getDisplayMode().getHeight();
	}
	public static Point getLocationForCenter(Dimension size){
		int x0=(WIDTH-size.width)/2;
		int y0=(HEIGHT-size.height)/2;
		return new Point(x0,y0);
	}
	public static void main(String[] args){
		System.out.println(System.getProperties());
		GraphicsEnvironment ge;
	}
}
