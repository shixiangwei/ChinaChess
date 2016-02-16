/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package org.acerge.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.acerge.engine.*;
import org.acerge.pieces.*;



public class Translation {
	private int firstBout = 1;
	private String gameEvent = "在家自战赛";
	private String gameRound ="1";
	private String gameDate;
	private String gameSite = "Home";
	private String redTeam = "Wild Wolf";
	private String redName = "WolfNo.1";
	private String blackTeam = "SuperComputers";
	private String blackName = "DeepGreen";
	private String result = "?-?";
	private String fenStr = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1";
	private String newLine = "\n";
	private ArrayList emoveStrList;
	private ArrayList cmoveStrList;
	
	private ArrayList hisEmoveStrList=new ArrayList(); //save for history s
	private ArrayList hisCmoveStrList=new ArrayList();
	private ArrayList hisMoveStructList=new ArrayList();
	//format: EMoveStr:C2+5     C+.5    N3-4
	//format: CMoveStr::炮2进五 前炮平5  马三退四
	private ArrayList moveStructList;
	//private String[] qzEName={"K","A","B","N","R","C","P"};
	private String[] qzCName={"帅","仕","相","马","车","炮","兵","将","士","象","马","车","炮","卒"};
	private String[] bCLocation={"０","１","２","３","４","５","６","７","８","９"};
	private String[] rCLocation={"  ","一","二","三","四","五","六","七","八","九"};
	//private boolean boardReversed;
	public Translation(){
		Calendar c = Calendar.getInstance();
		gameDate = c.get(Calendar.YEAR)+ "-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DATE);
		emoveStrList = new ArrayList();
		cmoveStrList = new ArrayList();
		moveStructList = new ArrayList();
	}
	public char getEName(char cName){
		switch (cName){
			case '帅':
			case '将':
				return 'K';
			case '仕':
			case '士':
				return 'A';
			case '相':
			case '象':
				return 'B';
			case '马':
				return 'N';
			case '车':
				return 'R';
			case '炮':
				return 'C';
			default:
				return 'P';
		}
	}
	public char getELoc(char cLoc){
		switch(cLoc){
			case '１':
			case '一':
				return '1';
			case '２':
			case '二':
				return '2';
			case '３':
			case '三':
				return '3';
			case '４':
			case '四':
				return '4';
			case '５':
			case '五':
				return '5';
			case '６':
			case '六':
				return '6';
			case '７':
			case '七':
				return '7';
			case '８':
			case '八':
				return '8';
			default:
				return '9';
		}
	}
	public char getEDir(char cDir){
		switch (cDir){
			case '进': return '+';
			case '退': return '-';
			default :return '.';//平
		}
	}
	public String cStrToEStr(String CStr){
		char cc1,cc2,cc3,cc4;
		char ec1,ec2,ec3,ec4;
		String EStr;
		cc1 = CStr.charAt(0);cc2=CStr.charAt(1);
		cc3=CStr.charAt(2);cc4=CStr.charAt(3);
		if (cc1=='前'){
			ec2 = '+';
			ec1 = getEName(cc2);
		}else if (cc1=='后'){
			ec2 = '-';
			ec1 = getEName(cc2);
		}else{
			ec1 = getEName(cc1);
			ec2 = getELoc(cc2);
		}
		ec3 = getEDir(cc3);
		ec4 = getELoc(cc4);
		return ""+ec1+ec2+ec3+ec4;
	}
	public void setFenStr(String fstr){
		fenStr=fstr;
	}
	public void addMoveInfo(final BitBoard qzBoard, final Qizi qz,int dstX,int dstY){
		//qzBoard是qz的位棋盘(移动前的状态)
		int pieceType = qz.getPieceType();
		char pieceName = qz.getPieceName();
		if (pieceName>='a') pieceName = (char) (pieceName + 'A' - 'a');
		char c1,c2,c3,c4;
		String cmoveStr="",emoveStr="";
		int srcX,srcY,srcX1,srcY1,srcX2,srcY2;
		int hi = BitBoard.MSB(qzBoard);
		int low = BitBoard.LSB(qzBoard);
		srcX=qz.getCoordinateX();
		srcY=qz.getCoordinateY();
		
		srcX1=ActiveBoard.FILE[low];
		srcY1=ActiveBoard.RANK[low];
		srcX2=ActiveBoard.FILE[hi];
		srcY2=ActiveBoard.RANK[hi];
		
		//-----------------------------------------------------------------------		
		int[] pawns={-1,-1,-1,-1,-1};
		if (pieceType == 6 || pieceType == 13){
			int k=0;
			for(int i=low;i<=hi;i++){
				if ((qzBoard.opAnd(PreMoveNodesGen.BitMask[i])).notZero()){
					pawns[k]=i;
					k++;
				}
			}
			boolean found=false;
			for (int i=0;i<k && !found;i++){
				if(ActiveBoard.FILE[pawns[i]]==srcX){
					srcX1 = ActiveBoard.FILE[pawns[i]];
					srcX2 = ActiveBoard.FILE[pawns[i]];
					srcY1 = ActiveBoard.RANK[pawns[i]];
					srcY2 = ActiveBoard.RANK[pawns[i]];
					for(int j=i+1;j<k;j++){
						if(ActiveBoard.FILE[pawns[j]]==srcX){
							srcX2 = ActiveBoard.FILE[pawns[j]];
							srcY2 = ActiveBoard.RANK[pawns[j]];
							found = true;
							break;
						}
					}
				}
				if(found) break;
			}
		}
		//-----------------------------------------------------------------------

		c1=pieceName;
		
		if(srcX1==srcX2 && srcY2!=srcY1 && pieceType!=1 && pieceType!=2
			&& pieceType!=8 && pieceType!=9){//士象无前后
			if(srcY==srcY1 && pieceType<7 || srcY==srcY2 && pieceType>=7){
				c2 = '-';
			}else{
				c2 = '+';
			}
		}else{
			//if (pieceType<7 && !boardReversed || pieceType>=7 && boardReversed){
			if (pieceType<7){
				c2 = (char) ('0' + 9 - srcX);
			}else{
				c2 = (char) ('0' + 1 + srcX);
			}
		}

		if(srcY==dstY) {
			c3='.';
			if (pieceType<7){
				c4=(char)('0' + 9 - dstX);
			}else{
				c4=(char)('0' + dstX + 1);
			}
		}else{
			if(dstX==srcX){//直线
				if(srcY<dstY && pieceType<7 || srcY>dstY && pieceType>=7){
					c3='+';
				}else{
					c3='-';
				}
				c4 = (char) ('0'+ Math.abs(dstY-srcY));
			}else{
				if(srcY<dstY && pieceType<7 || srcY>dstY && pieceType>=7){
					c3 = '+';
				}else{
					c3 = '-';
				}
				if (pieceType<7){
					c4 = (char) ('0' + 9 - dstX);
				}else{
					c4 = (char) ('0' + dstX + 1);
				}
			}
		}
		emoveStr = ""+c1+c2+c3+c4;
		// begin tanslation to Chinese 
		if (c2=='+') {
			cmoveStr="前"+qzCName[pieceType];
		}else if(c2=='-'){
			cmoveStr="后"+qzCName[pieceType];
		}else{
			cmoveStr=qzCName[pieceType]+(pieceType<7? rCLocation[c2-'0']:bCLocation[c2-'0']);
		}
		if (c3=='+'){
			cmoveStr+="进";
		}else if (c3=='-'){
			cmoveStr+="退";
		}else{
			cmoveStr+="平";
		}
		cmoveStr+=(pieceType<7? rCLocation[c4-'0']:bCLocation[c4-'0']);
		clearHis();
		emoveStrList.add(emoveStr);
		cmoveStrList.add(cmoveStr);
		moveStructList.add(new MoveNode(srcX*10+srcY,dstX*10+dstY));
	}
	public void clearHis(){
		if(hisMoveStructList.size()==0) return;
		hisEmoveStrList = new ArrayList();
		hisCmoveStrList = new ArrayList();
		hisMoveStructList = new ArrayList();
	}
	
	public void clearCurrent(){
		if(moveStructList.size()==0) return;
		emoveStrList = new ArrayList();
		cmoveStrList = new ArrayList();
		moveStructList = new ArrayList();
	}

	public String getEMoveStr(){
		if (emoveStrList.size()>0){
			return (String) emoveStrList.get(emoveStrList.size()-1);
		}
		return null;
	}
	
	public String getEMoveStr(int index){
		if (index<emoveStrList.size()){
			return (String) emoveStrList.get(index);
		}
		return null;
	}
	public String getHisEMoveStr(){
		if (hisEmoveStrList.size()>0){
			return (String) hisEmoveStrList.get(hisEmoveStrList.size()-1);
		}
		return null;		
	}
	public String getCMoveStr(){
		if(cmoveStrList.size()>0){
			return (String) cmoveStrList.get(cmoveStrList.size()-1);
		}
		return null;
	}

	public String getHisCMoveStr(){
		if(hisCmoveStrList.size()>0){
			return (String) hisCmoveStrList.get(hisCmoveStrList.size()-1);
		}
		return null;
	}
	public void moveHisToCurrent(){
		if(hisEmoveStrList.size()>0){
			emoveStrList.add(hisEmoveStrList.remove(hisEmoveStrList.size()-1));
			cmoveStrList.add(hisCmoveStrList.remove(hisCmoveStrList.size()-1));
			moveStructList.add(hisMoveStructList.remove(hisMoveStructList.size()-1));
		}
	}
	public void moveCurrentToHis(){
		if(emoveStrList.size()>0){
			hisEmoveStrList.add(emoveStrList.remove(emoveStrList.size()-1));
			hisCmoveStrList.add(cmoveStrList.remove(cmoveStrList.size()-1));
			hisMoveStructList.add(moveStructList.remove(moveStructList.size()-1));
		}
	}

	public String getCMoveStr(int index){
		if (index<moveStructList.size()){
			return (String) cmoveStrList.get(index);
		}
		return null;
	}
	public String[] getECMoveStr(){
		String[] tmpStr=new String[2];
		tmpStr[0]=getEMoveStr();
		tmpStr[1]=getCMoveStr();
		if(tmpStr[0]==null) return null;
		return tmpStr;
	}
	public String[] getECMoveStr(int index){
		if (index<emoveStrList.size()){
			String[] tmpStr=new String[2];
			tmpStr[0]=getEMoveStr(index);
			tmpStr[1]=getCMoveStr(index);
			return tmpStr;
		}
		return null;
	}

	public MoveNode getMoveStruct(){
		if (moveStructList.size()>0){
			return (MoveNode)moveStructList.get(moveStructList.size()-1);
		}
		return null;
	}
	public MoveNode getHisMoveStruct(){
		if (hisMoveStructList.size()>0){
			return (MoveNode)hisMoveStructList.get(hisMoveStructList.size()-1);
		}
		return null;
	}
	public MoveNode getMoveStruct(int index){
		if (index<moveStructList.size()){
			return (MoveNode)moveStructList.get(index);
		}
		return null;
	}
	/**
	 *save and read procedure 
	 *file Format:
	 *[Game "Chinese Chess"]
	 *[Event "在家自战赛"]
	 *[Round "1"]
	 *[Date ""]
	 *[Site "Home"]
	 *[RedTeam "Computer"]
	 *[Red "Engine"]
	 *[BlackTeam "Home"]
	 *[Black "GB"]
	 *[FenStr "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1"]	
	 *[Result "?-?"]
	 */
	public void saveToFile(File f) throws IOException{
		BufferedWriter outFile = new BufferedWriter(new FileWriter(f));
		String sign = "\"";
		// write header
		outFile.write("[Event " + sign + gameEvent + sign + "]" + newLine);
		outFile.write("[Round " + sign + gameRound + sign + "]" + newLine);
		outFile.write("[Date " + sign + gameDate + sign + "]" + newLine);
		outFile.write("[Site " + sign + gameSite + sign + "]" + newLine);
		outFile.write("[RedTeam " + sign + redTeam + sign + "]" + newLine);
		outFile.write("[Red " + sign + redName + sign + "]" + newLine);
		outFile.write("[BlackTeem " + sign + blackTeam + sign + "]" + newLine);
		outFile.write("[Black " + sign + blackName + sign + "]" + newLine);
		outFile.write("[FenStr " + sign + fenStr + sign + "]" + newLine);
		outFile.write("[Result " + sign + result + sign + "]" + newLine);
		
		//System.out.println("炮２平五".substring(1,2));//2
		int i,currBout=firstBout;
		String cmoveStr = getCMoveStr(0);
		String boutStr = ""+firstBout+".";
		if (boutStr.length()<3) boutStr = " " + boutStr;
		for (i=0;i<bCLocation.length;i++){
			if (cmoveStr.substring(1,2).equals(bCLocation[i])){
				outFile.write(boutStr + "        " + "  " + cmoveStr + newLine);
				currBout++;
				break;
			}
		}
		
		//System.out.println(bCLocation);				
		for (i=currBout-firstBout;;){			
			cmoveStr = getCMoveStr(i);
			if (cmoveStr != null){
				boutStr = ""+currBout+".";
				if (boutStr.length()<3) boutStr = " " + boutStr;
				outFile.write(boutStr+cmoveStr);
				i++;currBout++;
			}else{
				break;
			}
			cmoveStr = getCMoveStr(i);
			if (cmoveStr != null){
				outFile.write("  " + cmoveStr + newLine);
				i++;
			}else{
				break;
			}
		}
		outFile.flush();
		outFile.close();
		//begin writeBody;
		
	}
	public boolean loadFromFile(File f) throws IOException{
		BufferedReader inFile = new BufferedReader(new FileReader(f));
		String lineStr = "";
		int i,j;
		clearCurrent();
		clearHis();
		while ((lineStr=inFile.readLine())!=null){
			i = lineStr.indexOf("\"");
			j = lineStr.indexOf("\"",i+1);
			i = i + 1;
			if (lineStr.indexOf("Game")>=0){	 
			}else if((lineStr.indexOf("Event")>=0)){
				gameEvent = lineStr.substring(i, j);
			}else if((lineStr.indexOf("Round")>=0)){
				gameRound = lineStr.substring(i, j);
			}else if((lineStr.indexOf("Date")>=0)){
				gameDate = lineStr.substring(i, j);
			}else if((lineStr.indexOf("Site")>=0)){
				gameSite = lineStr.substring(i, j);
			}else if((lineStr.indexOf("RedTeam")>=0)){
				redTeam = lineStr.substring(i, j);
			}else if((lineStr.indexOf("Red")>=0)){
				redName = lineStr.substring(i, j);
			}else if((lineStr.indexOf("BlackTeam")>=0)){
				blackTeam = lineStr.substring(i, j);
			}else if((lineStr.indexOf("Black")>=0)){
				blackName = lineStr.substring(i, j);
			}else if((lineStr.indexOf("FenStr")>=0)){
				fenStr = lineStr.substring(i, j);
			}else if((lineStr.indexOf("Result")>=0)){
				result = lineStr.substring(i, j);
				break;				
			}else {
				inFile.close();
				return false;
			}
		}
		//add to hiscmoveStrList
		while ((lineStr=inFile.readLine())!=null){
			int index0,index1;
			String tmpStr;
			int start = 0;
			while((index0 = getQiziIndex(lineStr,start))>=0){
				tmpStr = lineStr.substring(index0,index0 + 4);
				hisCmoveStrList.add(0,tmpStr);
				start = index0 + 4;
			}
		}
		
		//translate and add to hisemoveStrList
		for (i=0;i<hisCmoveStrList.size();i++){
			hisEmoveStrList.add(0,cStrToEStr((String)hisCmoveStrList.get(i)));
		}
		//generate moveStruct
		ActiveBoard position = new ActiveBoard();
		position.loadFen(fenStr);
		for (i=0;i<hisEmoveStrList.size();i++){
			String tmpStr = (String) hisEmoveStrList.get(i);
			MoveNode move = getMoveStruct(position, tmpStr);
			if (move!=null){
				try{
					position.movePiece(move);
				}catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
					break;
				}
				hisMoveStructList.add(0,move);
			}else{
				break;
			}
		}
		//top = moveStructList.size();
		inFile.close();
		if (hisMoveStructList.size()>0)
			return true;
		else 
			return false;
	}
	public int getQiziIndex(String lineStr,int from){
		int[] index = new int[13];
		index[0] = lineStr.indexOf("车",from);
		index[1] = lineStr.indexOf("马",from);
		index[2] = lineStr.indexOf("炮",from);
		index[3] = lineStr.indexOf("兵",from);
		index[4] = lineStr.indexOf("卒",from);
		index[5] = lineStr.indexOf("象",from);
		index[6] = lineStr.indexOf("相",from);
		index[7] = lineStr.indexOf("将",from);
		index[8] = lineStr.indexOf("帅",from);
		index[9] = lineStr.indexOf("仕",from);
		index[10] = lineStr.indexOf("士",from);
		index[11] = lineStr.indexOf("前",from);
		index[12] = lineStr.indexOf("后",from);
		int value = 1000;//satisfy
		for (int i=0;i<index.length;i++){
			if (index[i]>=0 && index[i]<value)	
				value = index[i];
		}
		if (value!=1000) {
			return value;
		}else{
			return -1;
		}
	}
	private MoveNode getMoveStruct(ActiveBoard position, String emoveStr){
		int player = position.getPlayer();
		char c1=emoveStr.charAt(0);
		char c2=emoveStr.charAt(1);
		char c3=emoveStr.charAt(2);
		char c4=emoveStr.charAt(3);
		
		int piece = ActiveBoard.fenPiece(c1);
		piece = piece + player*7;
		BitBoard pieceBoard = position.getPieceBits(piece);
		int low = BitBoard.LSB(pieceBoard);
		int hi = BitBoard.MSB(pieceBoard);
		int srcX,srcY,srcX1,srcY1,srcX2,srcY2;
		int dstX,dstY;
		int i=0,j=0;
		srcX1 = ActiveBoard.FILE[low];
		srcX2 = ActiveBoard.FILE[hi];
		srcY1 = ActiveBoard.RANK[low];
		srcY2 = ActiveBoard.RANK[hi];
		//srcX = srcY = 0;
		int[] pawns={-1,-1,-1,-1,-1};
		//Pawn's max number is 5;
		if (piece == 6 || piece == 13){
			int k=0;
			for(i=low;i<=hi;i++){
				if ((pieceBoard.opAnd(PreMoveNodesGen.BitMask[i])).notZero()){
					pawns[k]=i;
					k++;
				}
			}
			if (c2!='+' && c2!='-'){
				if (player==0){
					srcX = 8-(c2 - '0' -1);
				}else{
					srcX = c2 -'0' - 1;
				}
				for (i=0;i<k;i++){
					if(srcX == ActiveBoard.FILE[pawns[i]]){
						break;//srcY=在外部
					}
				}
				srcY = ActiveBoard.RANK[pawns[i]];
			}else{
				boolean found=false;
				for (i=0;i<k && !found;i++){
					for(j=i+1;j<k;j++){			
						if(ActiveBoard.FILE[pawns[i]]==ActiveBoard.FILE[pawns[j]]){
							found = true;
							srcX1 = ActiveBoard.FILE[pawns[i]];
							srcX2 = ActiveBoard.FILE[pawns[j]];
							srcY1 = ActiveBoard.RANK[pawns[i]];
							srcY2 = ActiveBoard.RANK[pawns[j]];
							break;//srcX1,srcX2在外
						}
					}
				}
				if (c2=='+' && player==0 || c2=='-' && player==1){
					srcX = srcX2;srcY = srcY2;
				}else{
					srcX = srcX1;srcY = srcY1;
				}
			}
			//if(!found){
			//	for(int i=0;i<k;i++){
			//		int x = ChessPosition.FILE[pawns[i]];
			//		int y = 8 - (c2-'0'-1);
			//		if((piece==6 && ChessPosition.FILE[pawns[i]]==8 - (c2-'0'-1)) ||
			//			(piece==13 && ChessPosition.FILE[pawns[i]]==c2-'0'-1)){
			//			srcX1 = srcX2 =  ChessPosition.FILE[pawns[i]];
			//			srcY1 = srcY2 = ChessPosition.RANK[pawns[i]];
			//			break;
			//		}
			//	}
			//}
		}else{
			if (c2=='+' && player==0 || c2=='-' && player==1){
				srcX = srcX2;srcY = srcY2;
			}else if (c2=='-' && player==0 || c2=='+' && player==1){
				srcX = srcX1;srcY = srcY1;
			}else{
				if (player==0){
					srcX = 8-(c2 - '0' -1);
				}else{
					srcX = c2 -'0' - 1;
				}
				if (srcX == srcX1){
					srcY = srcY1;
				}else{
					srcY = srcY2;
				}
			}
		}
		switch (piece) {
			case 0://King
			case 4://Rook
			case 5://Cannon
			case 6://Pawn
				if(c3=='+'){
					dstX = srcX;dstY = srcY+c4-'0';
				}else if(c3=='-'){
					dstX = srcX;dstY = srcY-(c4-'0');
				}else{
					dstX = 8 - (c4 - '0' - 1) ; dstY = srcY;
				}
				break;
			case 1://Advisor
				dstX = 8 -(c4 - '0' -1);
				if(c3=='+'){
					if (srcX1==srcX2 && srcY1!=srcY2){
						srcY = srcY1;dstY = srcY1 + 1;
						if (dstY>2) {srcY = srcY2; dstY = srcY2 + 1;} 
					}else{
						dstY = srcY + 1;
					}
				}else{
					if (srcX1==srcX2 && srcY1!=srcY2){
						srcY = srcY1; dstY = srcY1 - 1;
						if (dstY<0) {srcY = srcY2; dstY = srcY2 - 1;} 
					}else{
						dstY = srcY - 1;
					}
				}
				break;
			case 2://Bishop				
				dstX = 8 -(c4 - '0' -1);
				if (c3=='+'){
					if (srcX1==srcX2 && srcY1!=srcY2){
						dstY = srcY1 + 2;srcY = srcY1; 
						if (dstY>4) {dstY = srcY2 + 2;srcY = srcY2; } 
					}else{
						dstY = srcY + 2;
					}
				}else{
					if (srcX1==srcX2 && srcY1!=srcY2){
						dstY = srcY1 - 2;srcY = srcY1; 
						if (dstY<0) {dstY = srcY2 + 2;srcY = srcY2; } 
					}else{
						dstY = srcY - 2;
					}
					dstY = srcY - 2;
				}
				break;
			case 3://Knight
				dstX = 8 - (c4 - '0' -1);
				if(c3=='+'){
					dstY = srcY + ((Math.abs(dstX - srcX)==1)?2:1);
				}else{
					dstY = srcY - ((Math.abs(dstX - srcX)==1)?2:1);
				}
				break;
			case 7://King
			case 11://Rook
			case 12://Cannon
			case 13://Pawn
				if(c3=='+'){
					dstX = srcX;dstY = srcY-(c4-'0');
				}else if(c3=='-'){
					dstX = srcX;dstY = srcY+(c4-'0');
				}else{
					dstX = c4 - '0' - 1 ; dstY = srcY;
				}
				break;
			case 8://Advisor在一条直线上不用前后表示
				dstX = c4 - '0' -1;
				if(c3=='+'){
					if (srcX1==srcX2 && srcY1!=srcY2){
						dstY = srcY1 - 1;srcY = srcY1; 
						if (dstY<7) {dstY = srcY2 - 1;srcY = srcY2; } 
					}else{
						dstY = srcY - 1;
					}
				}else{
					if (srcX1==srcX2 && srcY1!=srcY2){
						dstY = srcY1 + 1;srcY = srcY1; 
						if (dstY>9) {dstY = srcY2 + 1;srcY = srcY2; } 
					}else{
						dstY = srcY + 1;
					}
				}
				break;
			case 9://Bishop				
				dstX = c4 - '0' -1;
				if (c3=='+'){
					if (srcX1==srcX2 && srcY1!=srcY2){
						dstY = srcY1 - 2;srcY = srcY1; 
						if (dstY < 5) {dstY = srcY2 - 2; srcY = srcY2;} 
					}else{
						dstY = srcY - 2;
					}
				}else{
					if (srcX1==srcX2 && srcY1!=srcY2){
						dstY = srcY1 + 2;srcY = srcY1; 
						if (dstY > 9 ) {dstY = srcY2 + 2;srcY = srcY2; } 
					}else{
						dstY = srcY + 2;
					}
				}
				break;
			case 10://black Knight
				dstX = c4 - '0' -1;
				if(c3=='+'){
					dstY = srcY - ((Math.abs(dstX - srcX)==1)?2:1);
				}else{
					dstY = srcY + ((Math.abs(dstX - srcX)==1)?2:1);
				}
				break;
			default: return null;
		}
		return new MoveNode(srcX*10+srcY,dstX*10+dstY);
	}
	public File[] getFile(File path){
		File[] flist = path.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("QP")
					|| pathname.isDirectory())
					return true;
				else return false;
			}
		});
		return flist;
	}
	public int writeToBook(File f) throws IOException{
		RandomAccessFile inFile = new RandomAccessFile(f, "rw");
		ActiveBoard position = new ActiveBoard();
		position.loadFen(fenStr);
		int index = 0,value = 0;
		int step = 30;
		String tmpF, tmpM;
		MoveNode move;
		inFile.seek(inFile.length());
		int tmp = this.size();
		while(index<this.size() && value<20){//前8个回合
			move = this.getMoveStruct(index);
			if(move==null) break;
			tmpM = String.copyValueOf(move.location());
			tmpF = position.getFenStr();				
			position.movePiece(move);
			inFile.writeBytes(tmpM+" "+tmpF+newLine);
			index++;
			value++;
		}
		value = 8;
		if (result.equals("0-1")){
			while(index<this.size() && value<step){
				move = this.getMoveStruct(index);
				if(move==null) break;
				position.movePiece(move);
				index++;
				move = this.getMoveStruct(index);
				if(move==null) break;
				tmpM = String.copyValueOf(move.location());
				tmpF = position.getFenStr();
				inFile.writeBytes(tmpM+" "+tmpF+newLine);	
				position.movePiece(move);
				index++;
				value++;
			}
		}else if(result.equals("1-0")){
			while(index<this.size() && value<step){
				move = this.getMoveStruct(index);
				if(move==null) break;
				tmpM = String.copyValueOf(move.location());
				tmpF = position.getFenStr();				
				inFile.writeBytes(tmpM+" "+tmpF+newLine);
				position.movePiece(move);
				index++;
				move = this.getMoveStruct(index);
				if(move==null) break;
				position.movePiece(move);
				index++;
				value++;
			}			
		}
		inFile.close();
		return value;
	}

	public int getPreBook(){
		File f1 = new File("E:/websphere/ChineseChess/data/book.pre");
		File dir = new File("F:/ChessDoc/book");
		File flist[] = getFile(dir);
		int value = 0;
		for (int i=0;i<flist.length;i++){
			try {
				File tmpFile = flist[i];
				loadFromFile(flist[i]);
				value+=writeToBook(f1);
				
				if (i%200==0){
					System.out.println("File "+i+":"+ tmpFile+",Line:"+value);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	public int sortBook(File srcFile,File dstFile, int LineNum) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(srcFile));
		BufferedWriter out = new BufferedWriter(new FileWriter(dstFile));
		String lineStr;
		int i=0,j=0,index;
		//int outLoop = LineNum/divid + 1;
		String allLine[] = new String[LineNum];
		int WriteLine=0;
		j=0;
		for(j=0;j<LineNum;j++){
			allLine[j]="";
		}
		//System.out.println("OutLoop:"+i);
			
		j=0;
		while((lineStr = in.readLine())!=null && j<LineNum){
			allLine[j]=lineStr.substring(4) + "#" + lineStr.substring(0,4);
			j++;
		}
		Arrays.sort(allLine,0,j);
		for(i=j-1;i>=0;){
			lineStr = allLine[i];			
			index = lineStr.indexOf("#");
			if (index>0){
				out.write(lineStr.substring(index+1) + " " + lineStr.substring(0,index)+ newLine);
				WriteLine++;
				for (int k = i - 1;k>=0;k--){
					if(allLine[k].equals(lineStr)) {
						i--;
					}else{
						break;
					}
				}
				i--;
			}else{
				i--;
			}
		}
		in.close();
		out.close();
		System.out.println("Total Line:" + WriteLine);
		return WriteLine;	
	}
	public void getBook(File srcFile,File dstFile,int maxSize) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(srcFile));
		BufferedWriter out = new BufferedWriter(new FileWriter(dstFile));
		String lineStr;
		int i=0,j=0,index;
		String allLine[] = new String[maxSize];
		int WriteLine=0;
		j=0;
		String mStr,fStr,mStr0,fStr0;
		int value = 1;
//
  		lineStr = in.readLine();
		mStr0=lineStr.substring(0,4);
		index=4;
		char ch = lineStr.charAt(index);
		while(ch==' ') {
			index++;
			ch = lineStr.charAt(index);
		}
		fStr0=lineStr.substring(index);
		out.write(mStr0 + " " + value + " " + fStr0+newLine);
		j++;

		while((lineStr = in.readLine())!=null && j<maxSize){
			mStr=lineStr.substring(0,4);
			index=4;
			ch = lineStr.charAt(index);
			while(ch==' ') {
				index++;
				ch = lineStr.charAt(index);
			}
			fStr=lineStr.substring(index);
			if (fStr.equals(fStr0)){
				out.write(mStr + " " + value + " " + fStr+newLine);
			}else{
				mStr0=mStr;fStr0=fStr;value=(value+1);//%5111;
				out.write(mStr + " " + value + " " + fStr+newLine);
			}
			j++;
		}
		out.close();
		in.close();
	}
	
	private void transForm(File src, File dst) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(src));
		BufferedWriter out = new BufferedWriter(new FileWriter(dst));
		String lineStr,mStr,fStr;
		int index;
		while((lineStr=in.readLine())!=null){
			mStr=lineStr.substring(0,4);
			index=5;
			while(lineStr.charAt(index)>='0' && lineStr.charAt(index)<='9'){
				index++;
			}
			fStr=lineStr.substring(index+1);
			out.write(mStr+" "+fStr+newLine);
		}
		out.close();
		in.close();
	}
	public static void main(String args[]) throws IOException{
		Translation ts = new Translation();
		File f1 = new File("E:/websphere/ChineseChess/data/book.txt");
		File f2 = new File("E:/websphere/ChineseChess/data/book.pre");
		File f3 = new File("E:/websphere/ChineseChess/data/book1.pre");
		File f4 = new File("E:/websphere/ChineseChess/save/testsave3");
		File f5 = new File("E:/websphere/ChineseChess/data/BOOK2.pre");
		File f6 = new File("E:/websphere/ChineseChess/data/book3.pre");
		int maxLine;
		//maxLine = ts.getPreBook();
		//System.out.println("MaxLine1: "+ maxLine);
		maxLine = ts.sortBook(f2,f3,150000);
		System.out.println("MaxLine2: "+ maxLine);
		//ts.getBook(f3,f1,maxLine);
		//ts.loadFromFile(f4);
		//ts.writeToBook(f3);
		//ts.transForm(f5,f6);
		System.out.println("test");		
	}
	/**
	 * @return
	 */
	public String getBlackName() {
		return blackName;
	}

	/**
	 * @return
	 */
	public String getBlackTeam() {
		return blackTeam;
	}

	/**
	 * @return
	 */
	public String getFenStr() {
		return fenStr;
	}

	/**
	 * @return
	 */
	public int getFirstBout() {
		return firstBout;
	}

	/**
	 * @return
	 */
	public String getGameDate() {
		return gameDate;
	}

	/**
	 * @return
	 */
	public String getGameEvent() {
		return gameEvent;
	}

	/**
	 * @return
	 */
	public String getGameRound() {
		return gameRound;
	}

	/**
	 * @return
	 */
	public String getGameSite() {
		return gameSite;
	}

	/**
	 * @return
	 */
	public String getRedName() {
		return redName;
	}

	/**
	 * @return
	 */
	public String getRedTeam() {
		return redTeam;
	}

	/**
	 * @return
	 */
	public String getResult() {
		return result;
	}

	public int size() {
		return moveStructList.size();
	}

	/**
	 * @param string
	 */
	public void setBlackName(String string) {
		blackName = string;
	}

	/**
	 * @param string
	 */
	public void setBlackTeam(String string) {
		blackTeam = string;
	}

	/**
	 * @param i
	 */
	public void setFirstBout(int i) {
		firstBout = i;
	}

	/**
	 * @param string
	 */
	public void setGameDate(String string) {
		gameDate = string;
	}

	/**
	 * @param string
	 */
	public void setGameEvent(String string) {
		gameEvent = string;
	}

	/**
	 * @param string
	 */
	public void setGameRound(String string) {
		gameRound = string;
	}

	/**
	 * @param string
	 */
	public void setGameSite(String string) {
		gameSite = string;
	}

	/**
	 * @param string
	 */
	public void setRedName(String string) {
		redName = string;
	}

	/**
	 * @param string
	 */
	public void setRedTeam(String string) {
		redTeam = string;
	}

	/**
	 * @param string
	 */
	public void setResult(String string) {
		result = string;
	}

}
