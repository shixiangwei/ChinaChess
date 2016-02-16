package org.acerge.message;

import java.io.Serializable;

public class Header implements Serializable{
	private String headerString;
	private Header(String hs){this.headerString=hs;}
	public boolean equals(Object ob){
		return (ob instanceof Header) && ((Header)ob).headerString.equals(headerString);
	}
	public int hashCode(){return headerString.hashCode();}
	public static final Header PIECE_MOVEED=new Header("piece moved");
	public static final Header CHART=new Header("chart message");
	public static final Header RED_TIME_USED=new Header("red time used!");
	public static final Header BLACK_TIME_USED=new Header("black time used!");
	public static final Header SET_TIME_RULE=new Header("set time rule");
	public static final Header DISAGREE=new Header("disagree");
	public static final Header AGREE=new Header("agree");
	public static final Header SYSINFO=new Header("Sysinfo from opponent.");
	public static final Header BLACK_FAILED=new Header("black failed");
	public static final Header RED_FAILED=new Header("red failed");
}
