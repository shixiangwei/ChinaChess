/*
 * 创建日期 2005-3-18
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package org.acerge.main;

import javax.swing.*;
import javax.swing.event.*;

import org.acerge.rule.*;


import java.awt.*;
import java.awt.event.*;
/***********************************
 *	SysConfigInfo.class
 *
 *	private String userName
 *	private int selectedRb;//redOrblack;
 *	private int selectedSc;//server(1) or client(2)
 *	private String ipAddress;//ipAddress or hostName
 *	private int portNum;//port number;
 *	private int battleModel;//对战模式 1:单机双人 2:网络对战 3:人机对战
 *
 */
public class SetSysCfgDialog extends JDialog{

	private JTextField nameTf;//name
	private JRadioButton redRb, blackRb; //select red or black
	private JRadioButton serverRb, clientRb;
	private JTextField ipAddressTf,portNumTf;
	private JRadioButton modelSingleRb , modelNetRb, modelPvsCRb;
	private JPanel panel1,panel2,panel3;
	private JButton confirmBt,cancelBt;
	
	private SysConfigInfo sysCfg;
	private Container content;
	private static SetSysCfgDialog sscd;
	private boolean sysIsEditable;
	private SetSysCfgDialog(Frame frame,SysConfigInfo cfg){
		super(frame,true);
		sysCfg = cfg;
//**********************************************************	
		panel1 = new JPanel();
		panel1.setSize(200, 125);
		panel1.setBorder(BorderFactory.createEtchedBorder());
		panel1.setLayout(null);
		//JLabel label3 = new JLabel("设置连接");
		//label3.setSize(100,25);
		//label3.Location(5)	
		ServerClientRbListener scrl = new ServerClientRbListener();
		serverRb = new JRadioButton("作为服务器(Server)");
		serverRb.setSize(140,25);
		serverRb.setLocation(5,5);
		serverRb.addChangeListener(scrl);
		clientRb = new JRadioButton("作为客户端(Client)");
		clientRb.setSize(140,25);
		clientRb.setLocation(5, 35);
		clientRb.addChangeListener(scrl);
		
		ButtonGroup g1 = new ButtonGroup();
		g1.add(serverRb);
		g1.add(clientRb);
		JLabel label11 = new JLabel("主机名:");
		label11.setSize(45,25);
		label11.setLocation(20,65);
		JLabel label12 = new JLabel("端口号:");
		label12.setSize(45,25);
		label12.setLocation(20,95);
		ipAddressTf = new JTextField();
		ipAddressTf.setSize(100,25);
		ipAddressTf.setLocation(65,65);
		ipAddressTf.setText("127.0.0.1");
		
		portNumTf = new JTextField();
		portNumTf.setSize(100,25);
		portNumTf.setLocation(65,95);
		portNumTf.setText("4444");
		
		panel1.add(serverRb);
		panel1.add(clientRb);
		panel1.add(label11);
		panel1.add(ipAddressTf);
		panel1.add(label12);
		panel1.add(portNumTf);

//***********************************************************		
		panel2 = new JPanel();
		panel2.setSize(120, 125);
		panel2.setBorder(BorderFactory.createEtchedBorder());
		panel2.setLayout(null);
		JLabel label2 = new JLabel("选择对战模式");
		label2.setSize(100,25);
		label2.setLocation(5,5);
		ModelRbListener mrl = new ModelRbListener();
		modelSingleRb = new JRadioButton("单机双人");
		modelSingleRb.setSize(90, 25);
		modelSingleRb.setLocation(20, 35);
		modelNetRb = new JRadioButton("网络对战");
		modelNetRb.setSize(90,25);
		modelNetRb.setLocation(20,65);
		modelPvsCRb = new JRadioButton("人机对战");
		modelPvsCRb.setSize(90,25);
		modelPvsCRb.setLocation(20, 95);
		modelSingleRb.addChangeListener(mrl);
		modelNetRb.addChangeListener(mrl);
		modelPvsCRb.addChangeListener(mrl);
		
		ButtonGroup g2 = new ButtonGroup();
		
		g2.add(modelSingleRb);
		g2.add(modelNetRb);
		g2.add(modelPvsCRb);
		
		panel2.add(label2);
		panel2.add(modelSingleRb);
		panel2.add(modelNetRb);
		panel2.add(modelPvsCRb);
//****************************************************************
		panel3 = new JPanel();
		panel3.setSize(330, 65);
		panel3.setBorder(BorderFactory.createEtchedBorder());
		panel3.setLayout(null);
		JLabel label31 = new JLabel("选择红方或黑方");
		label31.setSize(100,25);
		label31.setLocation(5,5);
		redRb = new JRadioButton("红方");
		redRb.setSize(60,25);
		redRb.setLocation(105,5);
		blackRb = new JRadioButton("黑方");
		blackRb.setSize(60,25);
		blackRb.setLocation(180,5);

		JLabel label32 = new JLabel("输入姓名");
		label32.setSize(100,25);
		label32.setLocation(5,35);
		nameTf = new JTextField();
		nameTf.setSize(90,25);
		nameTf.setLocation(110,35);

		ButtonGroup g3 = new ButtonGroup();
		g3.add(redRb);
		g3.add(blackRb);
		
		panel3.add(label31);
		panel3.add(redRb);
		panel3.add(blackRb);
		panel3.add(label32);
		panel3.add(nameTf);
//***********************************************************
		
		content = this.getContentPane();
		content.setLayout(null);
		panel1.setLocation(140,10);//size:200,125
		content.add(panel1);
		panel2.setLocation(10,10);//size:120,125
		content.add(panel2);
		panel3.setLocation(10,140);//size:100,65
		content.add(panel3);
//************************************************************
		ButtonActionListener bl = new ButtonActionListener();
		confirmBt = new JButton("Confirm");
		confirmBt.setSize(90,25);
		confirmBt.setLocation(30,220);
		confirmBt.setActionCommand("confirm");
		confirmBt.addActionListener(bl);
		
		cancelBt = new JButton("Cancel");
		cancelBt.setSize(90,25);
		cancelBt.setLocation(230,220);
		cancelBt.setActionCommand("cancel");
		cancelBt.addActionListener(bl);
		
		content.add(confirmBt);
		content.add(cancelBt);
		
		this.setSize(360,300);
		Point loc=SCREEN.getLocationForCenter(getSize());
		this.setLocation(loc);
		this.setResizable(false);	
	}
	private void display(){
		if(sysCfg.getSelectedRb()==Rule.PLAYER_RED){
			redRb.setSelected(true);
		}else{
			blackRb.setSelected(true);
		}
		if(sysCfg.getBattleModel()==1){
			modelSingleRb.setSelected(true);
		}else if(sysCfg.getBattleModel()==2){
			modelNetRb.setSelected(true);
			portNumTf.setText(Integer.toString(sysCfg.getPortNum()));
			if(sysCfg.getSelectedSc()==1){
				serverRb.setSelected(true);
			}else{
				clientRb.setSelected(true);
				ipAddressTf.setText(sysCfg.getIpAddress());
			}
		}else{
			modelPvsCRb.setSelected(true);
		}
	}
	public static void createAndDisplay(Frame frame,SysConfigInfo cfg,
		boolean editable, String stitle)
	{
		if (sscd==null){
			sscd = new SetSysCfgDialog(frame,cfg);
		}else{
			sscd.setSysCfg(cfg);
		}
		sscd.sysIsEditable = editable;
		sscd.display();
		sscd.setTitle(stitle);
		sscd.setVisible(true);
	}
	public void closeDialog(){
		this.setVisible(false);
		this.dispose();
	}

	public SysConfigInfo getSysCfg() {
		return sysCfg;
	}
	public void setSysCfg(SysConfigInfo info) {
		sysCfg = info;
	}
	private void saveChange() throws NumberFormatException {
		if (modelNetRb.isSelected()){
			sysCfg.setBattleModel(2);
			sysCfg.setPortNum(Integer.parseInt(portNumTf.getText()));
			sysCfg.setUserName(nameTf.getText());
			if(clientRb.isSelected()){
				sysCfg.setSelectedSc(2);
				sysCfg.setIpAddress(ipAddressTf.getText());
			}else{
				sysCfg.setSelectedSc(1);
			}
		}else if (modelSingleRb.isSelected()){
			sysCfg.setBattleModel(1);
		}else if (modelPvsCRb.isSelected()){
			sysCfg.setBattleModel(3);
		}
		if (redRb.isSelected()){
			sysCfg.setSelectedRb(Rule.PLAYER_RED);
		}else{
			sysCfg.setSelectedRb(Rule.PLAYER_BLACK);
		}
	}
	/**
	 * private class 
	 */
	// for confirm and cancel ActionListener
	private class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("confirm")){
				try{
					if (sysIsEditable) saveChange();
					closeDialog();
				}catch(NumberFormatException exc){
					System.err.println("Invalid data you inputed!!!"+exc.getMessage());
				}
			}else if (e.getActionCommand().equals("cancel")){
				closeDialog();
			}
		}		
	}
		
	private class ServerClientRbListener implements ChangeListener{
		public void stateChanged(ChangeEvent e) {
			if(serverRb.isSelected()){
				ipAddressTf.setEditable(false);
				redRb.setEnabled(true);
				blackRb.setEnabled(true);
			}else if(clientRb.isSelected()){
				ipAddressTf.setEditable(true);
				redRb.setEnabled(false);
				blackRb.setEnabled(false);
			}
		}		
	}
	private class ModelRbListener implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			if(modelNetRb.isSelected()){
				serverRb.setEnabled(true);
				clientRb.setEnabled(true);
				ipAddressTf.setEnabled(true);
				portNumTf.setEnabled(true);
				nameTf.setEnabled(true);
			}else{// if(modelSingleRb.isSelected()){
				serverRb.setEnabled(false);
				clientRb.setEnabled(false);
				ipAddressTf.setEnabled(false);
				portNumTf.setEnabled(false);
				nameTf.setEnabled(false);				
			}
		}
	}
}
