/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package org.acerge.rule;

import org.acerge.message.Header;
import org.acerge.message.Producer;
import org.acerge.message.impl.ChessMessage;


public class TimeRule extends Thread{

	private long usedTime[]={0,0};// 0:red,1:black; unit: second(秒)
	private long[] millSeconds ={0,0};
	private boolean needCount;
	private Object synchObject=new Object();
	private long baseTime;
	private long addTime;
	private long totalTime[]={0,0};
	private boolean timeExhausted[]={false,false};
	private PlayerTimer playerTimer;
	private TimeRuleConfig currentConfig;
	private final TimeRuleConfig defaultTimeRuleConfig=new TimeRuleConfig();
	private Producer producer;
	public TimeRule(){
		resetTimeRule(defaultTimeRuleConfig);
	}
	public TimeRule(TimeRuleConfig trc){
		resetTimeRule(trc);
	}
	public void resetToDefault(){
		resetTimeRule(defaultTimeRuleConfig);
	}
	protected void countTime(int currentPlayer,long lastMillis){
		long current=millSeconds[currentPlayer]+System.currentTimeMillis()-lastMillis;
		millSeconds[currentPlayer]=current%1000;
		usedTime[currentPlayer]+=current/1000;
		timeExhausted[currentPlayer] = usedTime[currentPlayer]>totalTime[currentPlayer];
	}
	public void resetTimeRule(TimeRuleConfig trc){
		currentConfig=trc;
		usedTime[0] = 0;
		usedTime[1] = 0;
		baseTime=trc.getBaseTime();
		addTime=trc.getAddTimePerStep();
		totalTime[0]=totalTime[1]=baseTime;
		needCount = false;
	}
	public void resetTimeRule(){
		if(currentConfig!=null) resetTimeRule(currentConfig);
		else resetTimeRule(defaultTimeRuleConfig);
	}
	public void run(){
		long startMills;
		int player;
		synchronized(synchObject){
		while (true){
			if(!needCount){
				try {
					synchObject.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}// end while
			try {
				player=playerTimer.getCurrentPlayer();
				startMills=System.currentTimeMillis();
				synchObject.wait(1000);
				countTime(player,startMills);
				playerTimer.Display();
				if(timeExhausted[player]){
					if(player==0)
						producer.send(new ChessMessage(Header.RED_TIME_USED,null,true));
					else
						producer.send(new ChessMessage(Header.BLACK_TIME_USED,null,true));
					this.resetTimeRule();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}
	public void resetTimeAndBeginCount(){
		usedTime[0]=0;
		usedTime[1]=0;
		totalTime[0]=totalTime[1]=baseTime;
		needCount = true;
		if(this.isAlive())
			synchronized(synchObject){
			synchObject.notify();
			return;
		}
		start();
	}
	public void paulseCount(){
		needCount = false;
	}
	public void continueCount(){
		needCount = true;
		synchronized(synchObject){
			synchObject.notify();}
	}
	public String getDisplayString(long seconds){
		long h = seconds/3600;
		long m = (seconds % 3600)/60;
		long s = seconds % 60;
		return (h + ":" + m + ":" + s);
	}

	public long getTotalTime(int redOrBlack) {
		return totalTime[redOrBlack];
	}

	public long getUsedTime(int rb) {
		int index = 0;
		if (rb==Rule.PLAYER_RED)
			index = 0;
		else if(rb == Rule.PLAYER_BLACK)
			index = 1;

		return usedTime[index];
	}

	public PlayerTimer getPlayerTimer() {
		return playerTimer;
	}
	public void setPlayerTimer(PlayerTimer playTimer) {
		this.playerTimer = playTimer;
	}
	public void updateTotalTime(int redOrBlack){
		totalTime[redOrBlack]+=addTime;
	}
	public void setProducer(Producer producer) {
		this.producer=producer;
	}
}
