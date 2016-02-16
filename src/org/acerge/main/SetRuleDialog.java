package org.acerge.main;

import javax.swing.*;

import org.acerge.rule.*;

import java.awt.*;
import java.awt.event.*;

public class SetRuleDialog extends JDialog{
	private TimeRuleConfig timeRuleConfig;
	private JTextField firstHours,firstMinutes,firstSecond;
	private JTextField secondMinutes,secondSeconds;
	private JButton confirm,cancel;
	private boolean ruleIsEditable;
	private static JPanel t1;

	private static SetRuleDialog ruleDialog;
	private SetRuleDialog(Frame frame,TimeRuleConfig tmcfg){
		super(frame, true);//cannot resize
		timeRuleConfig = tmcfg;
		
		JLabel flabel1 = new JLabel("第一时限用时");
		flabel1.setSize(95,25);
		JLabel flabel2 = new JLabel("小时");
		flabel2.setSize(30,25);
		JLabel flabel3 = new JLabel("分钟");
		flabel3.setSize(30,25);
		JLabel flabel4 = new JLabel("秒");
		flabel4.setSize(15,25);
		
		JLabel slabel1 = new JLabel("以后每走一步加时");
		slabel1.setSize(105,25);
		JLabel slabel2 = new JLabel("分钟");
		slabel2.setSize(30,25);
		JLabel slabel3 = new JLabel("秒");
		slabel3.setSize(15,25);

		firstHours = new JTextField();
		firstHours.setSize(30,25);
		firstMinutes = new JTextField();
		firstMinutes.setSize(30,25);
		firstSecond = new JTextField();
		firstSecond.setSize(30,25);

		secondMinutes = new JTextField();
		secondMinutes.setSize(30,25);
		secondSeconds = new JTextField();
		secondSeconds.setSize(30,25);		
		
		t1 = new JPanel();
		t1.setBorder(BorderFactory.createEtchedBorder());
		//t1.setLayout(new BoxLayout(t1, BoxLayout.X_AXIS));
		t1.setLayout(null);
		t1.setSize(350,85);
		flabel1.setLocation(5,5);
		t1.add(flabel1);
		firstHours.setLocation(110,5);
		t1.add(firstHours);
		flabel2.setLocation(145,5);
		t1.add(flabel2);
		firstMinutes.setLocation(180,5);
		t1.add(firstMinutes);
		flabel3.setLocation(215,5);
		t1.add(flabel3);
		firstSecond.setLocation(250,5);
		t1.add(firstSecond);
		flabel4.setLocation(285,5);
		t1.add(flabel4);
//************************************
		slabel1.setLocation(5,35);
		t1.add(slabel1);
		secondMinutes.setLocation(110,35);
		t1.add(secondMinutes);
		slabel2.setLocation(145,35);
		t1.add(slabel2);
		secondSeconds.setLocation(180,35);
		t1.add(secondSeconds);
		slabel3.setLocation(215,35);
		t1.add(slabel3);
	
		Container content = this.getContentPane();
		//content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setLayout(null);
		
		t1.setLocation(10,30);
		content.add(t1);

		confirm = new JButton("Confirm");
		confirm.setActionCommand("Confirm");
		confirm.setSize(90,25);
		ActionListener l = new ButtonActionListener();
		confirm.addActionListener(l);
		cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.addActionListener(l);
		cancel.setSize(90,25);
		
		confirm.setLocation(70,150);
		cancel.setLocation(200,150);
		content.add(confirm);
		content.add(cancel);

		this.setSize(380,230);
		Point loc=SCREEN.getLocationForCenter(getSize());
		this.setLocation(loc);
		this.setResizable(false);
	}

	private void setTimeRuleConfig(TimeRuleConfig config) {
		timeRuleConfig = config;
	}
	
	private void display(){
		long fhours,fminutes,fseconds;
		long shours,sminutes,sseconds;
		long thours,tminutes,tseconds;
		int fsteps,ssteps;

		fhours = timeRuleConfig.getBaseTime() / 3600;
		fminutes = timeRuleConfig.getBaseTime() % 3600 / 60;
		fseconds = timeRuleConfig.getBaseTime() % 60;
		
		sminutes = timeRuleConfig.getAddTimePerStep() / 60;
		sseconds = timeRuleConfig.getAddTimePerStep() % 60;
		
		firstHours.setText(Long.toString(fhours));
		firstMinutes.setText(Long.toString(fminutes));
		firstSecond.setText(Long.toString(fseconds));
		
		secondMinutes.setText(Long.toString(sminutes));
		secondSeconds.setText(Long.toString(sseconds));		
	}

	public TimeRuleConfig getTimeRuleConfig(){
		if (ruleDialog!=null)
			return timeRuleConfig;
		else
			return null; 
	}
	public void closeDialog(){
		this.setVisible(false);
		this.dispose();
	}

	public static SetRuleDialog createAndDisplay(Frame frame, TimeRuleConfig trc, boolean canChange,String titleMsg){
		if (ruleDialog == null) {
			ruleDialog = new SetRuleDialog(frame, trc);
		}else{
			ruleDialog.setTimeRuleConfig(trc); 
		}
		ruleDialog.display();
		ruleDialog.setRuleEditable(canChange);
		if (titleMsg!=null){
			ruleDialog.setTitle(titleMsg);
		}else{
			ruleDialog.setTitle("限时规则设置");
		}
		ruleDialog.setVisible(true);
		return ruleDialog;
	}
	private void setRuleEditable(boolean editable){
		if (editable){
			firstHours.setEditable(true);
			firstMinutes.setEditable(true);
			firstSecond.setEditable(true);
			secondMinutes.setEditable(true);
			secondSeconds.setEditable(true);
			ruleIsEditable = true;
		}else{
			firstHours.setEditable(false);
			firstMinutes.setEditable(false);
			firstSecond.setEditable(false);
			secondMinutes.setEditable(false);
			secondSeconds.setEditable(false);
			ruleIsEditable = false;

		}
	}
	private void saveChange() throws NumberFormatException{
		long fhours,fminutes,fseconds;
		long shours,sminutes,sseconds;
		long thours,tminutes,tseconds;
		int fsteps,ssteps;
		fhours = Long.parseLong(firstHours.getText());
		fminutes = Long.parseLong(firstMinutes.getText());
		fseconds = Long.parseLong(firstSecond.getText());
		
		sminutes = Long.parseLong(secondMinutes.getText());
		sseconds = Long.parseLong(secondSeconds.getText());
			
		long total1 = fhours * 3600 + fminutes * 60 + fseconds;
		long total2 = sminutes * 60 + sseconds;
		timeRuleConfig.setBaseTime(total1);
		timeRuleConfig.setAddTimePerStep(total2);
	}
	//for JButton conform and cancel ActionListener
	private class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Confirm")){
				try{
					if (ruleIsEditable) saveChange();
					closeDialog();
				}catch(NumberFormatException exc){
					System.err.println("Invalid data you inputed!!!"+exc.getMessage());
				}
			}else if (e.getActionCommand().equals("Cancel")){
				closeDialog();
			}
		}		
	}
}
