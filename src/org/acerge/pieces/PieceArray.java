/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package org.acerge.pieces;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class PieceArray {
	private ArrayList list;
	public PieceArray(){
		list = new ArrayList();
	}
	public Qizi getPiece(int index){
		if (index>=0 && index<list.size()) 
			return (Qizi)(list.get(index));
		else return null;
	}
	public void add(Qizi qz){
		list.add(qz);
	}
	public Qizi remove(int index){
		if (index>=0 && index<list.size())
			return (Qizi)(list.remove(index));
		else 
			return null;
	}
	public boolean remove(Qizi qz){
		return list.remove(qz);
	}
	public int size(){
		return list.size();
	}
}
