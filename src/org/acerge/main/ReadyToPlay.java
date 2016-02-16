/*
 * �������� 2005-3-18
 *
 * �����������ļ�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
package org.acerge.main;

/**
 * @author Administrator
 *
 * ��������������ע�͵�ģ��Ϊ
 * ���� > ��ѡ�� > Java > �������� > �����ע��
 */
public class ReadyToPlay {
	private boolean connectionReady;
	private boolean timeRuleReady;
	private boolean sysCfgReady;
	private int negotiateTimes;
	
	public ReadyToPlay(){
		connectionReady = false;
		timeRuleReady = false;
		sysCfgReady = false;
		negotiateTimes = 3;
	}
	public ReadyToPlay(boolean connected, boolean timeagree){
		connectionReady = connected;
		negotiateTimes = 2;
		timeRuleReady = timeagree;
	}

	public boolean isConnectionReady() {
		return connectionReady;
	}

	public boolean isTimeRuleReady() {
		return timeRuleReady;
	}

	public void setConnectionReady(boolean b) {
		connectionReady = b;
	}

	public void setTimeRuleReady(boolean b) {
		timeRuleReady = b;
	}
	
	public void negotiateTimeRule() 
		throws CannotGetAgreeOnMoreThanThreeTimes{
		if(negotiateTimes > 0 )
			negotiateTimes -- ;
		else
			throw new CannotGetAgreeOnMoreThanThreeTimes();
	}
	
	public boolean canPlay(){
		return connectionReady && timeRuleReady && sysCfgReady;
	}
	
	public boolean isSysCfgReady() {
		return sysCfgReady;
	}

	public void setSysCfgReady(boolean b) {
		sysCfgReady = b;
	}

}
