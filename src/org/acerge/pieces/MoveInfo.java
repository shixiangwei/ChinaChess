/*
 * �������� 2005-3-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package org.acerge.pieces;

public class MoveInfo {
	private Qizi moveQizi,eatenQizi;
	private int newLocX,newLocY;
	public MoveInfo(){
		moveQizi = null;
		eatenQizi = null;
		newLocX = -1;
		newLocY = -1;
	}
	public Qizi getEatenQizi() {
		return eatenQizi;
	}

	public Qizi getMoveQizi() {
		return moveQizi;
	}

	public int getNewLocX() {
		return newLocX;
	}

	public int getNewLocY() {
		return newLocY;
	}

	public void setEatenQizi(Qizi qizi) {
		eatenQizi = qizi;
	}

	public void setMoveQizi(Qizi qizi) {
		moveQizi = qizi;
	}

	public void setNewLocX(int i) {
		newLocX = i;
	}

	public void setNewLocY(int i) {
		newLocY = i;
	}

}
