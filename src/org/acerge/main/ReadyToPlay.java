/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package org.acerge.main;

/**
 * @author Administrator
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
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
