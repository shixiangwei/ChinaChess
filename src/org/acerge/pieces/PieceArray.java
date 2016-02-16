/*
 * �������� 2005-3-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package org.acerge.pieces;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
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
