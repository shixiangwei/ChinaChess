package org.acerge.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.acerge.engine.*;
import org.acerge.message.*;
import org.acerge.message.impl.ChessMessage;
import org.acerge.message.impl.ChessMessageQueue;
import org.acerge.message.impl.MessageConsumer;
import org.acerge.message.impl.MessageProducer;
import org.acerge.message.impl.PieceMessageDeliver;
import org.acerge.message.support.MsgNetConnection;
import org.acerge.message.support.OuterMsgReceiver;
import org.acerge.message.support.OuterMsgSender;
import org.acerge.pieces.*;
import org.acerge.rule.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class ChessMainFrame extends JFrame {
	private static Log log = LogFactory.getLog(ChessMainFrame.class);
	private JLabel noteinfo;
	private JButton button_setSysCfg, button_resetAll;
	private JButton button_reverseBoard;

	private JButton button_cmpMove;

	private JButton button_turnBack;

	private JButton button_saveFile;

	private JButton button_openFile;

	private JButton button_preStep, button_nextStep, button_endStep;

	private JFileChooser fileChooserButton;

	private JTextField textField_redTime, textField_blackTime;

	private JButton button_getConnect;

	private JButton button_setRule, button_displayRule;

	private JButton button_start;

	JPanel panel0, panel1, panel2, panel3, panel4;// select red or black

	private Container content;

	private PictureBoard pictureBoard;

	private int player = 0;

	private boolean boardReversed = false;

	// private boolean boardChangedWhenComputerIsThingking = false;

	private boolean computerIsThingking = false;

	private final TimeRule timeRule = new TimeRule();

	private TimeRuleConfig timeRuleConfig = new TimeRuleConfig();

	private SysConfigInfo sysCfg = new SysConfigInfo();

	private ReadyToPlay readyToPlay;

	private boolean competitorStarted = false;

	// computer.

	private boolean started = false;

	private boolean isFirstStep = true;// 第一步

	private String competitorName;

	private static final String SET_RULE_COMMAND = "set time rule";

	private static final String DISPLAY_RULE_COMMAND = "display time rule";

	private int boardGridSize;// 棋板格子的大小

	private int view = 0;// 视角0：红方在下方

	private int lineLoc;

	private ActiveBoard activeBoard;

	private PieceArray pieceArr = new PieceArray();;

	private Qizi pieceIndex[] = new Qizi[90];;// [90]

	private PieceArray capturedArr = new PieceArray();;

	private Qizi lastSelected;

	private SortedMoveNodes AllMoves;

	private int[][] HisTable = new int[90][90];;

	private Translation translation;

	private SearchEngine searchEngine;

	/***************************************************************************
	 * message processer
	 **************************************************************************/
	private MessageQueue msgQueue = new ChessMessageQueue();

	private Producer producer = new MessageProducer(msgQueue);

	private Consumer consumer = new MessageConsumer();

	private PieceMessageDeliver deliver = new PieceMessageDeliver(msgQueue);

	private MessageListener localListener = new LocalMessageListener();

	private MessageListener remoteListener = new RemoteMessageListener();

	private MsgNetConnection netConnection = new MsgNetConnection();

	private OuterMsgSender outSender = new OuterMsgSender(netConnection);

	private OuterMsgReceiver outReceiver = new OuterMsgReceiver(producer,
			netConnection);

	/***************************************************************************
	 * button Listeners
	 **************************************************************************/
	private LastNextTurnBackButtonListener lnt = new LastNextTurnBackButtonListener();

	private OpenSaveButtonListener fhl = new OpenSaveButtonListener();

	private ConnectActionListener connectListener = new ConnectActionListener();

	private SysInfoButtonsListener sral = new SysInfoButtonsListener();

	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	public ChessMainFrame() {
		super();
		initialize();
		createGui();
	}

	public ChessMainFrame(int bs, int view, String appname) {
		super(appname);
		setBoardGridSize(bs);
		setView(view);
		initialize();
		createGui();
	}

	private void initialize() {
		JPanel jContentPane = new JPanel();
		jContentPane.setLayout(null);
		this.setContentPane(jContentPane);
		// this.getContentPane().setLayout(null);
		for (int k = 0; k < 90; k++)
			for (int j = 0; j < 90; j++)
				HisTable[k][j] = 0;
		setBoardGridSize(50);
		this.getActiveBoard();
		this.getSearchEngine();
		this.initActiveBoard();
		this.getTranslation();
		this.getPictureBoard();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		timeRuleConfig = new TimeRuleConfig();
		timeRule.setProducer(producer);
		timeRule.setPlayerTimer(new TimeCounter());
		sysCfg = new SysConfigInfo();
		readyToPlay = new ReadyToPlay();
		AllMoves = new SortedMoveNodes();
		deliver.registerAConsumer(consumer);
		consumer.RegisterAListener(localListener);
		consumer.RegisterAListener(remoteListener);
		outReceiver.setProducer(producer);
		deliver.start();
	}

	public void createGui() {
		JDialog.setDefaultLookAndFeelDecorated(true);
		content = this.getContentPane();
		// content.
		// content.setLayout(null);
		// this.setContentPane(newBoard);
		// newBoard.
		fileChooserButton = new JFileChooser();
		fileChooserButton.setCurrentDirectory(new File("./save"));
		this.setSize(new Dimension(getPictureBoard().getWidth() + 140,
				getPictureBoard().getHeight() + 80));
		this.setLocation(SCREEN.getLocationForCenter(getSize()));
		getNoteinfo().setBounds(10, pictureBoard.getHeight() + 15,
				pictureBoard.getWidth() - 20, 25);
		getPanel0().setLocation(pictureBoard.getWidth() + 15, 5);
		getPanel1().setLocation(pictureBoard.getWidth() + 15, 135);
		getPanel2().setLocation(pictureBoard.getWidth() + 15, 203);
		getPanel3().setLocation(pictureBoard.getWidth() + 15, 273);
		getPanel4().setLocation(pictureBoard.getWidth() + 15, 400);

		content.add(noteinfo);
		content.add(getPanel0());
		content.add(getPanel1());
		content.add(getPanel2());
		content.add(getPanel3());
		content.add(getPanel4());
		content.add(getPictureBoard());

		initPictureBoardAndPieces();
		displayAllQizi();

	}// end of createGui

	public void initActiveBoard() {
		getActiveBoard()
				.loadFen(
						"rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1");
		getTranslation()
				.setFenStr(
						"rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1");
		loadBook("./data/book.txt");
		player = 0;
	}

	public void initActiveBoard(String fenStr) {
		getActiveBoard().loadFen(fenStr);
		player = activeBoard.getPlayer();
	}

	/***************************************************************************
	 * add all picture pieces to content
	 **************************************************************************/
	public void initPictureBoardAndPieces() {
		initPieces();
		for (int i = 0; i < pieceArr.size(); i++)
			content.add(pieceArr.getPiece(i));
		content.add(pictureBoard);
	}

	public void reloadPreActiveBoard(ActiveBoard newBoard) {
		clearPicturePieces();
		this.activeBoard = newBoard;
		initPieces();
		initPictureBoardAndPieces();
		displayAllQizi();
		player = getActiveBoard().getPlayer();
	}

	private void initPieces() {
		// if (content==null) content = this.getContentPane();
		PieceFactory.setPieceSize((int) (getBoardGridSize() * 0.9));
		String s = activeBoard.getFenStr();
		Qizi tmpQizi;
		int row = 9;//
		int col = 0;
		for (int i = 0; i < 90; i++) {
			pieceIndex[i] = null;
		}
		PieceMouseListener PML = new PieceMouseListener();
		for (int i = 0; i < s.length() && row >= 0;) {
			char tmpChar = s.charAt(i);
			if (tmpChar >= '1' && tmpChar <= '9') {
				i++;
				col += tmpChar - '0';
			}
			if (s.charAt(i) == '/' || s.charAt(i) == ' ') {
				row--;
				col = 0;
			} else {
				tmpQizi = PieceFactory.getAPiece(s.charAt(i), col, row);
				tmpQizi.addMouseListener(PML);
				pieceArr.add(tmpQizi);
				pieceIndex[col * 10 + row] = pieceArr
						.getPiece(pieceArr.size() - 1);
				col++;
			}
			i++;
		}
	}

	public void loadBook(String book) {
		try {
			getSearchEngine().clearHash();
			getSearchEngine().clearHistTab();
			// arch.LoadBook("./data/BOOK.DAT");
			getSearchEngine().loadBook(book);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void clearPicturePieces() {
		for (int i = 0; i < pieceIndex.length; i++) {
			pieceIndex[i] = null;
		}
		Qizi tmpQz = null;
		int len = pieceArr.size();
		for (int i = 0; i < len; i++) {
			if ((tmpQz = pieceArr.remove(0)) != null) {
				content.remove(tmpQz);
			}
		}
	}

	public void reverseBoard() {
		boardReversed = !boardReversed;
		displayAllQizi();
	}

	// public void
	public void displayAllQizi() {
		for (int i = 0; i < pieceIndex.length; i++) {
			displayAQizi(pieceIndex[i]);
		}
	}

	public void displayAQizi(Qizi qz) {
		int x0, y0;
		if (qz == null)
			return;
		setQzLocation(qz);
		qz.setVisible(true);
	}

	public void setQzLocation(Qizi qz) {
		int x0, y0;
		x0 = qz.getCoordinateX();
		y0 = qz.getCoordinateY();
		if (boardReversed) {
			x0 = 8 - x0;
			y0 = 9 - y0;
		}
		qz.setLocation((int) (lineLoc + (x0 - 0.45) * boardGridSize),
				(int) (lineLoc + (9 - y0 - 0.45) * boardGridSize));
	}

	public boolean undo() {
		int oldSrc = activeBoard.lastMove().src;
		int oldDst = activeBoard.lastMove().dst;
		int oldCap = activeBoard.lastMove().cap;
		if (oldSrc < 0 || oldDst < 0) {
			log.info("End of undo!!.");
			return false;
		}
		activeBoard.undoMove();
		translation.moveCurrentToHis();
		if (lastSelected != null) {
			lastSelected.setBorderPainted(false);
		}
		lastSelected = pieceIndex[oldDst];
		lastSelected.setBorderPainted(true);
		changeLocation(lastSelected, oldSrc / 10, oldSrc % 10);
		if (oldCap != 0) {
			pieceIndex[oldDst] = capturedArr.remove(capturedArr.size() - 1);
			pieceIndex[oldDst].setVisible(true);
		}
		return true;
	}

	public boolean redo() {
		MoveNode tmpMove = translation.getHisMoveStruct();
		if (tmpMove != null) {
			int src = tmpMove.src;
			int dst = tmpMove.dst;
			movePiece(src, dst, false);
			translation.moveHisToCurrent();
			return true;
		} else {
			log.info("end of redo");
			return false;
		}
	}

	public void setAQizi(Qizi srcQz, int x, int y) {
		pieceIndex[x * 10 + y] = srcQz;
		srcQz.setCoordinate(x, y);
	}

	public void changeLocation(Qizi srcQz, int dstX, int dstY) {
		// if dst has a qizi, move it to captured place[dst][1] and set to
		// visible to false
		int src = srcQz.getCoordinate();
		int dst = dstX * 10 + dstY;
		if (pieceIndex[dst] != null) {
			pieceIndex[dst].setVisible(false);
			capturedArr.add(pieceIndex[dst]);
			pieceIndex[dst] = null;
		}
		pieceIndex[dst] = srcQz;
		pieceIndex[dst].setCoordinate(dstX, dstY);
		setQzLocation(pieceIndex[dst]);
		pieceIndex[dst].setBorderPainted(true);
		pieceIndex[src] = null;
	}

	// for mouseAction
	public boolean movePiece(Qizi qz, int dstX, int dstY, boolean needSave) {
		if (qz == null)
			return false;
		int srcX = qz.getCoordinateX(), srcY = qz.getCoordinateY();
		AllMoves.GenMoves(activeBoard, HisTable);
		MoveNode tmpMove = null;
		int tmpSrc, tmpDst, moveNum = AllMoves.MoveNum;
		for (int i = 0; i < moveNum; i++) {
			tmpSrc = AllMoves.MoveList[i].src;
			tmpDst = AllMoves.MoveList[i].dst;
			if (tmpSrc == srcX * 10 + srcY && tmpDst == dstX * 10 + dstY) {
				BitBoard pieceBoard = (activeBoard.getPieceBits(qz
						.getPieceType())).getLeftShift(0);// copy to
				// pieceBoard
				tmpMove = new MoveNode(tmpSrc, tmpDst);
				if (activeBoard.movePiece(tmpMove)) {
					if (needSave)
						translation.addMoveInfo(pieceBoard, qz, dstX, dstY);
					log.info(translation.getEMoveStr() + "  "
							+ translation.getCMoveStr());
					if (isFirstStep) {
						timeRule.resetTimeAndBeginCount();
						isFirstStep = false;
					}
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	// for remoteMove computer Move or redo or undo
	public void movePiece(int src, int dst, boolean needSave) {
		Qizi qz = pieceIndex[src];
		int dstX = dst / 10;
		int dstY = dst % 10;
		if (qz == null)
			return;
		if (lastSelected != null) {
			lastSelected.setBorderPainted(false);
		}
		if (movePiece(qz, dst / 10, dst % 10, needSave)) {
			// if true, model data and other information are saved,
			// following is to alter the view
			changeLocation(qz, dst / 10, dst % 10);
			lastSelected = qz;
			timeRule.updateTotalTime(player);
			player = 1 - player;
		}
	}

	public void decideFailer() {// send lose message
		AllMoves.GenMoves(activeBoard, HisTable);
		for (int i = 0; i < AllMoves.MoveNum; i++) {
			MoveNode tmp = AllMoves.MoveList[i];
			if (activeBoard.movePiece(tmp)) {
				activeBoard.undoMove();
				return;
			}
		}
		if (activeBoard.getPlayer() == 0)
			producer.send(new ChessMessage(Header.RED_FAILED, null, true));
		producer.send(new ChessMessage(Header.BLACK_FAILED, null, true));
	}

	public boolean ignoreMouseAction(Object eventSource) {
		if (computerIsThingking && eventSource!=button_cmpMove) {
			outputInfo("cannot take any action,computer is thingking");
			return true;
		}
		if (started) {
			if (eventSource == button_start || eventSource == button_setRule
					|| eventSource == button_setSysCfg
					|| eventSource == button_getConnect)
				return true;
		}
		if (eventSource instanceof Qizi)
			if (!started)
				return true;
		if (sysCfg.getBattleModel() == 2) {// network
			if (eventSource == button_cmpMove || eventSource == button_endStep
					|| eventSource == button_preStep
					|| eventSource == button_resetAll
					|| eventSource == button_turnBack
					|| eventSource == button_nextStep)
				return true;
			if (eventSource instanceof Qizi) {
				Qizi tmp = (Qizi) eventSource;
				int rb1 = (tmp.getPieceType() < 7 ? 0 : 1);
				if (player != sysCfg.getSelectedRb())
					return true;
				if (rb1 != sysCfg.getSelectedRb()) {
					if (lastSelected == null)
						return true;
				}

			}
			if (eventSource == pictureBoard) {
				if (lastSelected != null) {
					int rb2 = (lastSelected.getPieceType() < 7 ? 0 : 1);
					if (rb2 != sysCfg.getSelectedRb())
						return true;
				}
			}
		}
		return false;
	}

	public void afterMoved(boolean mouseAction) {
		if (mouseAction) {
			int src, dst;
			src = activeBoard.lastMove().src;
			dst = activeBoard.lastMove().dst;
			producer.send(new ChessMessage(Header.PIECE_MOVEED,
					src + "-" + dst, true));
		}
		decideFailer();
	}

	private void setTimeRule(TimeRuleConfig trc) {
		SetRuleDialog.createAndDisplay(this, trc, true, null);
	}

	private void setSysCfgInfo(SysConfigInfo scfi) {
		SetSysCfgDialog.createAndDisplay(this, scfi, true, "设置系统信息");
	}

	private void displayTimeRule(TimeRuleConfig trc, String title) {
		SetRuleDialog.createAndDisplay(this, trc, false, title);
	}

	private int showMessageBox(String showmsg) {
		return JOptionPane.showConfirmDialog(ChessMainFrame.this, showmsg,
				"Information", JOptionPane.YES_NO_OPTION);
	}

	public void resizePictureBoard(int newSize) {
		setBoardGridSize(newSize);
		clearPicturePieces();
		content.remove(pictureBoard);
		pictureBoard.setBoardGridSize(newSize);
		this.repaint();
		initPictureBoardAndPieces();
	}

	private void resetAll() {
		if (sysCfg.getBattleModel() == 2)
			sysCfg.setSelectedRb(1 - sysCfg.getSelectedRb());
		timeRule.paulseCount();
		clearPicturePieces();
		initActiveBoard();
		translation = new Translation();
		// resizePictureBoard(30);
		msgQueue.removeAll();
		initPictureBoardAndPieces();
		displayAllQizi();
		textField_redTime.setText("red:");
		textField_blackTime.setText("black:");
		started = false;
		isFirstStep = true;
		loadBook("./data/book.txt");
	}

	private void outputInfo(String info) {
		noteinfo.setText(info);
	}

	private void computerMove() {
		if (button_cmpMove.getText() == "Computer") {
			button_cmpMove.setText("Stop");
			new Thread() {
				public void run() {
					computerIsThingking = true;
					outputInfo("Computer is thinking...");
					// ActiveBoard
					// tmpactive=(ActiveBoard)ObjectCopyer.getACopy(activeBoard);
					searchEngine.setActiveBoard(activeBoard);
					MoveNode bestMove=null;
					try {
						bestMove = searchEngine.getBestMove();
					} catch (LostException e) {
						e.printStackTrace();
					}
					button_cmpMove.setText("Computer");
					if (bestMove == null) {
						outputInfo("Computer cannot find best Move!!!");
						button_cmpMove.setEnabled(true);
						return;
					}
					// if(boardChangedWhenComputerIsThingking){
					// reloadPreActiveBoard(tmpactive);
					// }
					movePiece(bestMove.src, bestMove.dst, true);
					afterMoved(false);
					outputInfo("computer moved:"
							+ String.copyValueOf(bestMove.location())
							+ "轮到你走棋，快点！");
					computerIsThingking = false;
				}
			}.start();
		} else {
			searchEngine.stopSearch();
			button_cmpMove.setText("Computer");
		}

	}

	/***************************************************************************
	 * private class section.
	 **************************************************************************/
	private class ConnectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (ignoreMouseAction(e.getSource()))
				return;
			if (sysCfg.getSelectedSc() == 1) {
				netConnection.setPointAsServer(sysCfg.getPortNum());
				log.info("Server started!!");
			} else {
				netConnection.setPointAsClient(sysCfg.getIpAddress(), sysCfg
						.getPortNum());
				log.info("Client started!!");
			}
			new Thread() {
				public void run() {
					netConnection.createConnection();
					if (netConnection.available()) {
						log.info("Server and Client connected!");
						log.info("Starting outReceiver");
						outReceiver.setConnection(netConnection);
						outReceiver.startReceiveData();
						readyToPlay.setConnectionReady(true);
						competitorStarted = true;
						if (sysCfg.getSelectedSc() == 1) {// as server
							outSender.send(new ChessMessage(Header.SYSINFO,
									sysCfg, false));
						}
					}
				}
			}.start();
		}
	}

	private class LocalMessageListener implements MessageListener {
		public void onMessage(Message msg) {
			if (!msg.isLocalMessage())
				return;
			Header head = msg.getMessageHeader();
			Object body = msg.getMessageBody();
			if (head.equals(Header.CHART)) {
				// sendout
			} else if (head.equals(Header.PIECE_MOVEED)) {
				if (outSender.available()) {
					outSender.send(new ChessMessage(head, body, false));
				}
			} else if (head.equals(Header.RED_TIME_USED)
					|| head.equals(Header.RED_FAILED)) {
				System.err.println("RED LOST!!!!!!!!!!!!!!");
				int n = showMessageBox("红方输了,是否保存?");
				if (n == JOptionPane.YES_OPTION)
					saveFile();
				resetAll();
				// button_openFile.getActionListeners()[0].actionPerformed(new
				// ActionEvent(button_openFile,12324,"ffff"));
			} else if (head.equals(Header.BLACK_TIME_USED)
					|| head.equals(Header.BLACK_FAILED)) {
				System.err.println("BLACK LOST!!!!!!!!!!!!!!");
				int n = showMessageBox("黑方输了,是否保存?");
				if (n == JOptionPane.YES_OPTION)
					saveFile();
				resetAll();
			}
		}
	}

	private class RemoteMessageListener implements MessageListener {
		public void onMessage(Message msg) {
			if (msg.isLocalMessage())
				return;
			if (!outSender.available())
				return;
			Header head = msg.getMessageHeader();
			Object body = msg.getMessageBody();
			if (head.equals(Header.SET_TIME_RULE)) {
				System.err.println("received time rule from competitor!");
				displayTimeRule((TimeRuleConfig) body, "协商规则");
				int n = showMessageBox("Do you agree the time rule ?");
				if (n == JOptionPane.YES_OPTION) {
					timeRuleConfig = (TimeRuleConfig) body;
					timeRule.resetTimeRule(timeRuleConfig);
					readyToPlay.setTimeRuleReady(true);
					outSender.send(new ChessMessage(Header.AGREE, null, false));
				} else {
					System.err
							.println("send a message of disagree on time rule from competitor!");
					outSender.send(new ChessMessage(Header.DISAGREE, null,
							false));
				}
			} else if (head.equals(Header.AGREE)) {
				readyToPlay.setTimeRuleReady(true);
				timeRule.resetTimeRule(timeRuleConfig);
				showMessageBox("Competitor agreed!!!");
			} else if (head.equals(Header.DISAGREE)) {
				showMessageBox("Competitor disagree!!!!!");
			} else if (head.equals(Header.SYSINFO)) {
				competitorStarted = true;
				readyToPlay.setSysCfgReady(true);
				SysConfigInfo cpt = (SysConfigInfo) body;
				competitorName = cpt.getUserName();
				log.info("Competitor name" + competitorName);
				sysCfg.setSelectedRb(1 - cpt.getSelectedRb());
			} else if (head.equals(Header.PIECE_MOVEED)) {
				String tmpStr = (String) body;
				int src = Integer.parseInt(tmpStr.substring(0, tmpStr
						.indexOf('-')));
				int dst = Integer.parseInt(tmpStr
						.substring(tmpStr.indexOf('-') + 1));
				// log.info("receive a Message to move!");
				movePiece(src, dst, true);
				afterMoved(false);
			}
		}
	}

	/***************************************************************************
	 * for set time rule, display time rule,set syscfg and start Button
	 **************************************************************************/
	private class SysInfoButtonsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// button_setRull
			if (ignoreMouseAction(e.getSource()))
				return;
			if (e.getActionCommand().equals(SET_RULE_COMMAND)) {
				setTimeRule(timeRuleConfig);
				timeRule.resetTimeRule(timeRuleConfig);
				timeRule.getPlayerTimer().setNeedCount(true);
				if (sysCfg.getBattleModel() == 2)// network
					outSender.send(new ChessMessage(Header.SET_TIME_RULE,
							timeRuleConfig, false));
				else {
					readyToPlay.setTimeRuleReady(true);
				}
			} else
			// button_displayRule
			if (e.getActionCommand().equals(DISPLAY_RULE_COMMAND)) {
				displayTimeRule(timeRuleConfig, "现在所用的规则");
			} else
			// button_start
			if (e.getSource() == button_start) {
				if (!readyToPlay.isTimeRuleReady()) {
					timeRule.resetToDefault();
					readyToPlay.setTimeRuleReady(true);
				}
				if (readyToPlay.canPlay()) {
					if (view != sysCfg.getSelectedRb())
						reverseBoard();
					started = true;
				}
			} else
			// button_setSysCfg
			if (e.getSource() == button_setSysCfg) {
				if (started)
					return;
				setSysCfgInfo(sysCfg);
				if (sysCfg.getBattleModel() == 2) {// network battle
					button_getConnect.setEnabled(true);
				} else {
					readyToPlay.setConnectionReady(true);
					// newBoard.setMovableQz(3);
					button_getConnect.setEnabled(false);
					competitorStarted = true;
				}
				readyToPlay.setSysCfgReady(true);
			}
		}

	}

	private class PictureBoardMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (ignoreMouseAction(e.getSource()))
				return;
			int x = e.getX();
			int y = e.getY();
			Dimension d = pictureBoard.getXYCoordinate(x, y);
			x = d.width;
			y = d.height;
			if (boardReversed) {
				x = 8 - x;
				y = 9 - y;
			}
			pictureBoard.test.setText("x=" + x + ",y=" + y);
			if (lastSelected != null) {
				int src = lastSelected.getCoordinate();
				if (movePiece(lastSelected, x, y, true)) {
					changeLocation(lastSelected, x, y);
					if (sysCfg.getBattleModel() == 3) {
						computerMove();
					}
					timeRule.updateTotalTime(player);
					player = 1 - player;
					afterMoved(true);
				}
			}
		}
	}

	private class PieceMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// log.info(((Qizi)e.getSource()).getCoordinate());
			if (ignoreMouseAction(e.getSource()))
				return;
			if (lastSelected == null) {
				lastSelected = (Qizi) e.getSource();
				lastSelected.setBorderPainted(true);
			} else {
				Qizi tmpQizi = (Qizi) e.getSource();
				int x1 = tmpQizi.getCoordinateX();
				int y1 = tmpQizi.getCoordinateY();
				int src = lastSelected.getCoordinate();
				if (movePiece(lastSelected, x1, y1, true)) {
					changeLocation(lastSelected, x1, y1);
					if (sysCfg.getBattleModel() == 3) {
						computerMove();
					}
					timeRule.updateTotalTime(player);
					player = 1 - player;
					afterMoved(true);
				} else {
					lastSelected.setBorderPainted(false);
					lastSelected = tmpQizi;
					lastSelected.setBorderPainted(true);
				}
			}
		}
	}

	private class OpenSaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Handle open button action.
			if (ignoreMouseAction(e.getSource()))
				return;
			if (e.getSource() == button_openFile) {
				openFile();
				// Handle save button action.
			} else if (e.getSource() == button_saveFile) {
				saveFile();
			}
		}

	}

	/***************************************************************************
	 * for lastButton,nextButton,turnBack,computerStop,resetAllBt
	 **************************************************************************/
	private class LastNextTurnBackButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (ignoreMouseAction(e.getSource()))
				return;
			if (e.getSource() == button_preStep) {
				undo();
			} else if (e.getSource() == button_nextStep) {
				redo();
			} else if (e.getSource() == button_turnBack) {
				log.info("Turn back!!(need rewrite!!");
				undo();
			} else if (e.getSource() == button_cmpMove) {
				computerMove();
			} else if (e.getSource() == button_endStep) {
				if (translation.getHisMoveStruct() != null) {
					while (redo())
						;
				} else {
					while (undo())
						;
				}
			} else if (e.getSource() == button_resetAll) {
				log.info("reset all");
				resetAll();
			}
		}
	}

	private class TimeCounter implements PlayerTimer {
		private boolean needCount;

		private String displayStr;

		public TimeCounter() {
			this.needCount = false;
		}

		public void setNeedCount(boolean needCount) {
			this.needCount = needCount;
		}

		public boolean isNeedCount() {
			return needCount;
		}

		public void Display() {
			displayStr = timeRule
					.getDisplayString(timeRule.getUsedTime(player));
			if (player == 0) {
				textField_redTime.setText("red:" + displayStr);
			} else {
				textField_blackTime.setText("black:" + displayStr);
			}
		}

		public int getCurrentPlayer() {
			return player;
		}

	}

	/***************************************************************************
	 * Following is getters and setters
	 **************************************************************************/
	public int getBoardGridSize() {
		return boardGridSize;
	}

	private void openFile() {
		int returnVal = fileChooserButton.showOpenDialog(ChessMainFrame.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooserButton.getSelectedFile();
			try {
				resetAll();
				getTranslation().loadFromFile(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.info(file.getAbsolutePath());
		} else {
		}

	}

	private void saveFile() {
		int returnVal = fileChooserButton.showSaveDialog(ChessMainFrame.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooserButton.getSelectedFile();
			log.info(file.getAbsolutePath());
			try {
				translation.saveToFile(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// This is where a real application would save the file.
		} else {
			//
		}

	}

	public void setBoardGridSize(int boardGridSize) {
		this.boardGridSize = boardGridSize;
		lineLoc = boardGridSize * 2 / 3;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public JButton getButton_cmpMove() {
		if (button_cmpMove == null) {
			button_cmpMove = new JButton("Computer");
			button_cmpMove.setSize(90, 25);
			button_cmpMove.setBorder(BorderFactory.createRaisedBevelBorder());
			button_cmpMove.addActionListener(lnt);
		}
		return button_cmpMove;
	}

	public JButton getButton_displayRule() {
		if (button_displayRule == null) {
			button_displayRule = new JButton("displayRule");
			button_displayRule.setSize(90, 25);
			button_displayRule.setMargin(new Insets(1, 1, 1, 1));
			button_displayRule.setBorder(BorderFactory
					.createRaisedBevelBorder());
			button_displayRule.setActionCommand(DISPLAY_RULE_COMMAND);
			button_displayRule.addActionListener(sral);
		}
		return button_displayRule;
	}

	public JButton getButton_getConnect() {
		if (button_getConnect == null) {
			button_getConnect = new JButton("connect");
			button_getConnect.setSize(90, 25);
			button_getConnect.setEnabled(false);
			button_getConnect
					.setBorder(BorderFactory.createRaisedBevelBorder());
			button_getConnect.addActionListener(connectListener);
		}
		return button_getConnect;
	}

	public JButton getButton_openFile() {
		if (button_openFile == null) {
			button_openFile = new JButton("Read");
			button_openFile.setSize(90, 25);
			button_openFile.setMnemonic(KeyEvent.VK_R);
			button_openFile.setBorder(BorderFactory.createRaisedBevelBorder());
			button_openFile.addActionListener(fhl);
		}
		return button_openFile;
	}

	public JButton getButton_resetAll() {
		if (button_resetAll == null) {
			button_resetAll = new JButton("resetAll");
			button_resetAll.setSize(90, 25);
			button_resetAll.setBorder(BorderFactory.createRaisedBevelBorder());
			button_resetAll.addActionListener(lnt);
		}
		return button_resetAll;
	}

	public JButton getButton_reverseBoard() {
		if (button_reverseBoard == null) {
			button_reverseBoard = new JButton("Reverse");
			button_reverseBoard.setSize(90, 25);
			button_reverseBoard.setBorder(BorderFactory
					.createRaisedBevelBorder());
			button_reverseBoard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					reverseBoard();
				}
			});
		}
		return button_reverseBoard;
	}

	public JButton getButton_saveFile() {
		if (button_saveFile == null) {
			button_saveFile = new JButton("Save");
			button_saveFile.setSize(90, 25);
			button_saveFile.setMnemonic(KeyEvent.VK_S);
			button_saveFile.setBorder(BorderFactory.createRaisedBevelBorder());
			button_saveFile.addActionListener(fhl);
		}
		return button_saveFile;
	}

	public JButton getButton_setRule() {
		if (button_setRule == null) {
			button_setRule = new JButton("set rule");
			button_setRule.setSize(90, 25);
			button_setRule.setBorder(BorderFactory.createRaisedBevelBorder());
			button_setRule.setActionCommand(SET_RULE_COMMAND);
			button_setRule.addActionListener(sral);
		}
		return button_setRule;
	}

	public JButton getButton_setSysCfg() {
		if (button_setSysCfg == null) {
			button_setSysCfg = new JButton("Setting");
			button_setSysCfg.setSize(90, 25);
			button_setSysCfg.setBorder(BorderFactory.createRaisedBevelBorder());
			button_setSysCfg.addActionListener(sral);
		}
		return button_setSysCfg;
	}

	public JButton getButton_start() {
		if (button_start == null) {
			button_start = new JButton("start");
			button_start.setActionCommand("start");
			button_start.setSize(90, 25);
			button_start.setBorder(BorderFactory.createRaisedBevelBorder());
			button_start.addActionListener(sral);
		}
		return button_start;
	}

	public JButton getButton_turnBack() {
		if (button_turnBack == null) {
			button_turnBack = new JButton("back");
			button_turnBack.setSize(90, 25);
			button_turnBack.setMnemonic(KeyEvent.VK_B);
			button_turnBack.setBorder(BorderFactory.createRaisedBevelBorder());
			button_turnBack.addActionListener(lnt);
		}
		return button_turnBack;
	}

	public JButton getButton_endStep() {
		if (button_endStep == null) {
			Image endImage = Toolkit.getDefaultToolkit().getImage(
					"./image/end.gif");
			Image endImage1 = Toolkit.getDefaultToolkit().getImage(
					"./image/end1.gif");
			button_endStep = new JButton();
			button_endStep.setSize(21, 21);
			button_endStep.setIcon(new ImageIcon(endImage));
			button_endStep.setBorder(BorderFactory.createRaisedBevelBorder());
			button_endStep.setPressedIcon(new ImageIcon(endImage1));
			button_endStep.addActionListener(lnt);
		}
		return button_endStep;
	}

	public JButton getButton_nextStep() {
		if (button_nextStep == null) {
			Image rightImage = Toolkit.getDefaultToolkit().getImage(
					"./image/right.gif");
			Image rightImage1 = Toolkit.getDefaultToolkit().getImage(
					"./image/right1.gif");
			button_nextStep = new JButton();
			button_nextStep.setSize(21, 21);
			button_nextStep.setIcon(new ImageIcon(rightImage));
			button_nextStep.setBorder(BorderFactory.createRaisedBevelBorder());
			button_nextStep.setPressedIcon(new ImageIcon(rightImage1));
			button_nextStep.addActionListener(lnt);
		}
		return button_nextStep;
	}

	public JButton getButton_preStep() {
		if (button_preStep == null) {
			Image leftImage = Toolkit.getDefaultToolkit().getImage(
					"./image/left.gif");
			Image leftImage1 = Toolkit.getDefaultToolkit().getImage(
					"./image/left1.gif");
			button_preStep = new JButton();
			button_preStep.setSize(21, 21);
			button_preStep.setIcon(new ImageIcon(leftImage));
			button_preStep.setBorder(BorderFactory.createRaisedBevelBorder());
			button_preStep.setPressedIcon(new ImageIcon(leftImage1));
			button_preStep.addActionListener(lnt);
		}
		return button_preStep;
	}

	public JPanel getPanel0() {
		if (panel0 == null) {
			panel0 = new JPanel();
			panel0.setSize(100, 125);
			panel0.setLayout(null);
			panel0.setBorder(BorderFactory.createEtchedBorder());

			getButton_turnBack().setLocation(5, 5);
			getButton_saveFile().setLocation(5, 35);
			getButton_openFile().setLocation(5, 65);
			getButton_preStep().setLocation(5, 95);
			getButton_endStep().setLocation(35, 95);
			getButton_nextStep().setLocation(70, 95);

			panel0.add(button_turnBack);
			panel0.add(button_saveFile);
			panel0.add(button_openFile);
			panel0.add(button_preStep);
			panel0.add(button_endStep);
			panel0.add(button_nextStep);
		}
		return panel0;
	}

	public JPanel getPanel1() {
		if (panel1 == null) {
			panel1 = new JPanel();
			panel1.setLayout(null);
			panel1.setSize(100, 65);
			panel1.setBorder(BorderFactory.createEtchedBorder());

			getButton_setSysCfg().setLocation(5, 5);
			getButton_resetAll().setLocation(5, 35);

			panel1.add(button_setSysCfg);
			panel1.add(button_resetAll);
		}
		return panel1;
	}

	public JPanel getPanel2() {
		if (panel2 == null) {
			panel2 = new JPanel();
			panel2.setLayout(null);
			panel2.setSize(100, 65);
			panel2.setBorder(BorderFactory.createEtchedBorder());

			getButton_reverseBoard().setLocation(5, 5);
			getButton_cmpMove().setLocation(5, 35);

			panel2.add(button_reverseBoard);
			panel2.add(button_cmpMove);
		}
		return panel2;
	}

	public JPanel getPanel3() {
		if (panel3 == null) {
			panel3 = new JPanel();
			panel3.setBorder(BorderFactory.createEtchedBorder());
			panel3.setLayout(null);
			panel3.setSize(100, 125);

			getButton_getConnect().setLocation(5, 5);
			getButton_setRule().setLocation(5, 35);
			getButton_displayRule().setLocation(5, 65);
			getButton_start().setLocation(5, 95);

			panel3.add(button_getConnect);
			panel3.add(button_setRule);
			panel3.add(button_displayRule);
			panel3.add(button_start);
		}
		return panel3;
	}

	public JPanel getPanel4() {
		if (panel4 == null) {
			panel4 = new JPanel();
			panel4.setBorder(BorderFactory.createEtchedBorder());
			panel4.setLayout(null);
			panel4.setSize(100, 65);

			textField_redTime = new JTextField("redTime");
			textField_blackTime = new JTextField("blackTime");
			textField_redTime.setEditable(false);
			textField_blackTime.setEditable(false);
			textField_redTime.setSize(90, 25);
			textField_blackTime.setSize(90, 25);
			textField_redTime.setLocation(5, 5);
			textField_blackTime.setLocation(5, 35);

			panel4.add(textField_redTime);
			panel4.add(textField_blackTime);
		}
		return panel4;
	}

	public PictureBoard getPictureBoard() {
		if (pictureBoard == null) {
			pictureBoard = new PictureBoard();
			pictureBoard.setBoardGridSize(boardGridSize);
			pictureBoard.setBounds(0, 0, boardGridSize * 28 / 3,
					boardGridSize * 32 / 3);
			pictureBoard.setOpaque(false);
			pictureBoard.addMouseListener(new PictureBoardMouseListener());
		}
		return pictureBoard;
	}

	public void setPictureBoard(PictureBoard pictureBoard) {
		this.pictureBoard = pictureBoard;
	}

	public ActiveBoard getActiveBoard() {
		if (activeBoard == null)
			activeBoard = new ActiveBoard();
		return activeBoard;
	}

	public SearchEngine getSearchEngine() {
		if (searchEngine == null) {
			searchEngine = new SearchEngine();
			searchEngine.setupControl(6, SearchEngine.CLOCK_S * 20,
					SearchEngine.CLOCK_M * 10);

		}
		return searchEngine;
	}

	public Translation getTranslation() {
		if (translation == null)
			translation = new Translation();
		return translation;
	}

	public JLabel getNoteinfo() {
		if (noteinfo == null) {
			noteinfo = new JLabel("请注意此处的提示信息！");
			noteinfo.setForeground(Color.RED);
			noteinfo.setFont(getFont());
		}
		return noteinfo;
	}
} // @jve:decl-index=0:visual-constraint="4,10"
