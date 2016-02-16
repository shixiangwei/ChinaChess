/*
 * �������� 2005-3-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package org.acerge.main;

import java.io.Serializable;

import org.acerge.rule.Rule;

public class SysConfigInfo implements Serializable{
	private String userName;
	private int selectedRb;//redOrblack;
	private int selectedSc;//server(1) or client(2)
	private String ipAddress;//ipAddress or hostName
	private int portNum;//port number;
	private int battleModel;//��սģʽ 1:����˫�� 2:�����ս 3:�˻���ս
	
	public SysConfigInfo(){
		userName = "superMan";
		selectedRb = Rule.PLAYER_RED;
		selectedSc = 2;
		portNum = 4444;
		battleModel = 3;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public int getPortNum() {
		return portNum;
	}
	public String getRedName() {
		return userName;
	}
	public int getSelectedRb() {
		return selectedRb;
	}
	public int getSelectedSc() {
		return selectedSc;
	}
	public void setIpAddress(String string) {
		ipAddress = string;
	}
	public void setPortNum(int i) {
		portNum = i;
	}
	public void setSelectedRb(int i) {
		selectedRb = i;
	}
	public void setSelectedSc(int soc) {
		selectedSc = soc;
	}
	public int getBattleModel() {
		return battleModel;
	}

	public String getUserName() {
		return userName;
	}

	public void setBattleModel(int i) {
		battleModel = i;
	}

	public void setUserName(String string) {
		userName = string;
	}

}
