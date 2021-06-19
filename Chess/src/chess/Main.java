package chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author Ashish Kedia and Adarsh Mohata
 *
 */

/**
 * This is the Main Class of our project. All GUI Elements are declared,
 * initialized and used in this class itself. It is inherited from the JFrame
 * Class of Java's Swing Library.
 *
 * 
 * Things to fix:
 * 
 * When a pawn reaches the oppesite side it does not give the option to choose
 * what I want it to be
 * 
 * FIXED Not really working when moving peice to new cell, preventing checkmate.
 * There is different behavior between pawn, queen, bishop (and other) ?? When a
 * piece is able to take or go in front of an enemy piece to prevent chectmate
 * it will also allow the player to move piece out of the way, even though this
 * is not allow, when enemy on next turn takes the king the game gliches out.
 * 
 * There is no computer to practice against
 * 
 * The indanger method in king is not so smart written Make the method shorter
 * 
 * The constructer in Main is very long. And messy.
 * 
 */

public class Main extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1110;
	private static final int HEIGHT = 700;
	public static Main mainboard;
	public static int timeRemaining = 60;
	private Time timer;
	
	private King wk, bk;
	private Cell boardState[][];
	private boolean selected = false, end = false;
	private Cell c, previous;
	private int turn = 0;
	private ArrayList<Cell> destinationList = new ArrayList<Cell>();
	
	private ArrayList<Player> wplayer, bplayer;
	private Player tempPlayer;
	private Player white = null, black = null;
	private String wname = null, bname = null, winner = null;
	
	private JPanel board = new JPanel(new GridLayout(8, 8));
	private JPanel wdetails = new JPanel(new GridLayout(3, 3));
	private JPanel bdetails = new JPanel(new GridLayout(3, 3));
	private JPanel controlPanel, whitePlayer, blackPlayer, temp, displayTime, showPlayer;
	private JSplitPane split;
	private JLabel label, mov;
	private JLabel labelCHNC;
	private JComboBox<String> wcombo, bcombo;
	private JSlider timeSlider;
	private Button start, wselect, bselect, wnewPlayer, bnewPlayer;

	public static void main(String[] args) {
		mainboard = new Main();
		mainboard.setVisible(true);
		mainboard.setResizable(false);
	}

	private Main() {
		timeRemaining = 60;
		timeSlider = new JSlider();
		wname = null;
		bname = null;
		winner = null;
		ArrayList<String> Wnames = new ArrayList<String>();
		ArrayList<String> Bnames = new ArrayList<String>();

		// Get list of player objects from file. Each of them has name, wins, and losses.
		// excatly the same thing is done for black players. 
		wplayer = Player.fetchPlayers();
		Iterator<Player> witr = wplayer.iterator();
		while (witr.hasNext())
			Wnames.add(witr.next().name());
		bplayer = Player.fetchPlayers();
		Iterator<Player> bitr = bplayer.iterator();
		while (bitr.hasNext())
			Bnames.add(bitr.next().name());
		String[] WNames = {}, BNames = {};
		WNames = Wnames.toArray(WNames);
		BNames = Bnames.toArray(BNames);

		board = new JPanel(new GridLayout(8, 8));
		wdetails = new JPanel(new GridLayout(3, 3));
		bdetails = new JPanel(new GridLayout(3, 3));
		JPanel bcomboPanel = new JPanel();
		JPanel wcomboPanel = new JPanel();
		board.setMinimumSize(new Dimension(800, 700));
		ImageIcon img = new ImageIcon(this.getClass().getResource("icon.png"));
		this.setIconImage(img.getImage());

		// Define all 16 chess pieces. 
		Rook wr01 = new Rook("WR01", "White_Rook.png", 0);
		Rook wr02 = new Rook("WR02", "White_Rook.png", 0);
		Rook br01 = new Rook("BR01", "Black_Rook.png", 1);
		Rook br02 = new Rook("BR02", "Black_Rook.png", 1);
		Knight wk01 = new Knight("WK01", "White_Knight.png", 0);
		Knight wk02 = new Knight("WK02", "White_Knight.png", 0);
		Knight bk01 = new Knight("BK01", "Black_Knight.png", 1);
		Knight bk02 = new Knight("BK02", "Black_Knight.png", 1);
		Bishop wb01 = new Bishop("WB01", "White_Bishop.png", 0);
		Bishop wb02 = new Bishop("WB02", "White_Bishop.png", 0);
		Bishop bb01 = new Bishop("BB01", "Black_Bishop.png", 1);
		Bishop bb02 = new Bishop("BB02", "Black_Bishop.png", 1);
		Queen wq = new Queen("WQ", "White_Queen.png", 0);
		Queen bq = new Queen("BQ", "Black_Queen.png", 1);
		wk = new King("WK", "White_King.png", 0, 7, 4);
		bk = new King("BK", "Black_King.png", 1, 0, 4);
		Pawn[] wp = new Pawn[8];
		Pawn[] bp = new Pawn[8];
		for (int i = 0; i < 8; i++) {
			wp[i] = new Pawn("WP0" + (i + 1), "White_Pawn.png", 0);
			bp[i] = new Pawn("BP0" + (i + 1), "Black_Pawn.png", 1);
		}

		// Define board with 64 instances of cell class. Each cell.piece is either
		// a chess piece defined from above or null. 
		Cell cell;
		pieces.Piece P;
		boardState = new Cell[8][8];
		Piece[] blackPieces = { br01, bk01, bb01, bq, bk, bb02, bk02, br02 };
		Piece[] whitePieces = { wr01, wk01, wb01, wq, wk, wb02, wk02, wr02 };
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				P = null;
				if (i == 0)
					P = blackPieces[j];
				else if (i == 7)
					P = whitePieces[j];
				else if (i == 1)
					P = bp[j];
				else if (i == 6)
					P = wp[j];
				cell = new Cell(i, j, P);
				cell.addMouseListener(this);
				board.add(cell); // Visuel board with JPanels.
				boardState[i][j] = cell; // Cell objects we can refer to later.
			}
		}

		timeSlider.setMinimum(1);
		timeSlider.setMaximum(15);
		timeSlider.setValue(1);
		timeSlider.setMajorTickSpacing(2);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.addChangeListener(new TimeChange());

		board.setBorder(BorderFactory.createLoweredBevelBorder());
		Container content = getContentPane();
		setSize(WIDTH, HEIGHT);
		setTitle("Chess");
		
		// Start to define right side controlpanel
		content.setBackground(Color.black);
		controlPanel = new JPanel();
		content.setLayout(new BorderLayout());
		controlPanel.setLayout(new GridLayout(3, 3));
		controlPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("Lucida Calligraphy", Font.PLAIN, 20), Color.ORANGE));

		// Defining the Player Box in Control Panel. Same thing is done for white and black. 
		whitePlayer = new JPanel();
		whitePlayer.setBorder(BorderFactory.createTitledBorder(null, "White Player", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.BLACK));
		whitePlayer.setLayout(new BorderLayout());

		blackPlayer = new JPanel();
		blackPlayer.setBorder(BorderFactory.createTitledBorder(null, "Black Player", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("Open sans", Font.BOLD, 20), Color.BLACK));
		blackPlayer.setLayout(new BorderLayout());

		JPanel whitestats = new JPanel(new GridLayout(3, 3));
		JPanel blackstats = new JPanel(new GridLayout(3, 3));
		wcombo = new JComboBox<String>(WNames);
		bcombo = new JComboBox<String>(BNames);
		JScrollPane wscroll = new JScrollPane(wcombo);
		JScrollPane bscroll = new JScrollPane(bcombo);
		wcomboPanel.setLayout(new FlowLayout());
		bcomboPanel.setLayout(new FlowLayout());
		wselect = new Button("Select");
		bselect = new Button("Select");
		wselect.addActionListener(new SelectHandler(0));
		bselect.addActionListener(new SelectHandler(1));
		wnewPlayer = new Button("New Player");
		bnewPlayer = new Button("New Player");
		wnewPlayer.addActionListener(new Handler(0));
		bnewPlayer.addActionListener(new Handler(1));
		wcomboPanel.add(wscroll);
		wcomboPanel.add(wselect);
		wcomboPanel.add(wnewPlayer);
		bcomboPanel.add(bscroll);
		bcomboPanel.add(bselect);
		bcomboPanel.add(bnewPlayer);
		whitePlayer.add(wcomboPanel, BorderLayout.NORTH);
		blackPlayer.add(bcomboPanel, BorderLayout.NORTH);
		whitestats.add(new JLabel("Name   :"));
		whitestats.add(new JLabel("Played :"));
		whitestats.add(new JLabel("Won    :"));
		blackstats.add(new JLabel("Name   :"));
		blackstats.add(new JLabel("Played :"));
		blackstats.add(new JLabel("Won    :"));
		whitePlayer.add(whitestats, BorderLayout.WEST);
		blackPlayer.add(blackstats, BorderLayout.WEST);
		controlPanel.add(whitePlayer);
		controlPanel.add(blackPlayer);

		showPlayer = new JPanel(new FlowLayout());
		showPlayer.add(timeSlider);
		JLabel setTime = new JLabel("Set Timer(in mins):");
		start = new Button("Start");
		start.setBackground(Color.black);
		start.setForeground(Color.white);
		start.addActionListener(new Start());
		start.setPreferredSize(new Dimension(60, 40));
		setTime.setFont(new Font("Arial", Font.BOLD, 16));
		label = new JLabel("Time Starts now", SwingConstants.CENTER);
		label.setFont(new Font("SERIF", Font.BOLD, 30));
		displayTime = new JPanel(new FlowLayout());
		JPanel time = new JPanel(new GridLayout(3, 3));
		time.add(setTime);
		time.add(showPlayer);
		displayTime.add(start);
		time.add(displayTime);
		controlPanel.add(time);
		board.setMinimumSize(new Dimension(800, 700));

		// The Left Layout When Game is inactive
		temp = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				BufferedImage image = null;
				try {
					image = ImageIO.read(this.getClass().getResource("clash.jpg"));
				} catch (IOException ex) {
					System.out.println("not found");
				}

				g.drawImage(image, 0, 0, null);
			}
		};

		temp.setMinimumSize(new Dimension(800, 700));
		controlPanel.setMinimumSize(new Dimension(285, 900));

		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, temp, controlPanel);

		content.add(split);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * A function to switch turn between the two players. Is accessed from Time class. 
	 */
	public void changeTurn() {
		if (boardState[getKing(turn).getx()][getKing(turn).gety()].isCheck()) {
			turn ^= 1;
			gameend();
		}
		if (destinationList.isEmpty() == false)
			cleanDestinations(destinationList);
		if (previous != null)
			previous.deselect();
		previous = null;
		turn ^= 1;
		if (!end && timer != null) {
			timer.reset();
			timer.start();
			showPlayer.remove(labelCHNC);
			labelCHNC.setText((turn==0) ? "white" : "black");
			showPlayer.add(labelCHNC);
		}
	}

	private King getKing(int color) {
		if (color == 0)
			return wk;
		else
			return bk;
	}

	private void cleanDestinations(ArrayList<Cell> destlist) {
		ListIterator<Cell> it = destlist.listIterator();
		while (it.hasNext())
			it.next().removePossibleDestination();
	}

	private void highlightDestinations(ArrayList<Cell> destlist) {
		ListIterator<Cell> it = destlist.listIterator();
		while (it.hasNext())
			it.next().setPossibleDestination();
	}

	/**
	 * Returns true is the given move leads to current players king being under attack. 
	 * A move is defined by to cells. The first one with the moving piece and second one
	 * with a cell that can be captured. 
	 * <p>
	 * Results in an error if fromcell does not contain a piece. Results in error 
	 * if fromcell or tocell has the value null. 
	 * 
	 * @param fromcell	Cell with piece being moved.
	 * @param tocell	Cell which is being captured. 
	 * @return	True if king will be in danger. In other cases false.
	 */
	private boolean kingInDanger(Cell fromcell, Cell tocell) {
		// Create a copy of the current board. 
		Cell newboardstate[][] = new Cell[8][8];
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				try {
					newboardstate[i][j] = new Cell(boardState[i][j]);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					System.out.println("There is a problem with cloning !!");
				}
			}

		// Simulate a move.
		if (newboardstate[tocell.x][tocell.y].getPiece() != null)
			newboardstate[tocell.x][tocell.y].removePiece();
		newboardstate[tocell.x][tocell.y].setPiece(newboardstate[fromcell.x][fromcell.y].getPiece());
		int x = getKing(turn).getx();
		int y = getKing(turn).gety();
		if (newboardstate[tocell.x][tocell.y].getPiece() instanceof King) {
			((King) (newboardstate[tocell.x][tocell.y].getPiece())).setx(tocell.x);
			((King) (newboardstate[tocell.x][tocell.y].getPiece())).sety(tocell.y);
			x = tocell.x;
			y = tocell.y;
		}
		newboardstate[fromcell.x][fromcell.y].removePiece();

		// Use King isInDanger method to check for attacks. 
		return ((King) (newboardstate[x][y].getPiece())).isInDanger(newboardstate);
	}

	/**
	 * Removes all moves in destlist that puts the current players king under attack. 
	 * Where destlist are the cells that piece on fromcell is being moved to. 
	 * <p>
	 * Gives error if destlist is null or fromcell is null. Returns an empty list if
	 * all moves leads to the king being under attack.
	 * 
	 * @param destlist	A the destination cells to check individually.
	 * @param fromcell	The cell with piece that is being moved.
	 * @return	List of moves that prevents the king being attacked. 
	 */
	private ArrayList<Cell> filterDestination(ArrayList<Cell> destlist, Cell fromcell) {
		ArrayList<Cell> newlist = new ArrayList<Cell>();
		Cell newboardstate[][] = new Cell[8][8];
		ListIterator<Cell> it = destlist.listIterator();
		int x, y;
		while (it.hasNext()) {
			Cell tempc = it.next();

			// if not in danger, add this move to the list.
			if (kingInDanger(fromcell, tempc) == false)
				newlist.add(tempc);
		}
		return newlist;
	}

	public boolean checkmate(int color) {
		ArrayList<Cell> dlist = new ArrayList<Cell>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (boardState[i][j].getPiece() != null && boardState[i][j].getPiece().getColor() == color) {
					dlist.clear();
					dlist = boardState[i][j].getPiece().getMoves(boardState, i, j);
					dlist = filterDestination(dlist, boardState[i][j]);
					if (dlist.size() != 0)
						return false;
				}
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private void gameend() {
		cleanDestinations(destinationList);
		displayTime.disable();
		timer.countdownTimer.stop();
		if (previous != null)
			previous.removePiece();
		if (turn == 0) {
			white.updateGamesWon();
			white.updatePlayers();
			winner = white.name();
		} else {
			black.updateGamesWon();
			black.updatePlayers();
			winner = black.name();
		}
		JOptionPane.showMessageDialog(board, "Checkmate!!!\n" + winner + " wins");
		whitePlayer.remove(wdetails);
		blackPlayer.remove(bdetails);
		displayTime.remove(label);

		displayTime.add(start);
		showPlayer.remove(mov);
		showPlayer.remove(labelCHNC);
		showPlayer.revalidate();
		showPlayer.add(timeSlider);

		split.remove(board);
		split.add(temp);
		wnewPlayer.enable();
		bnewPlayer.enable();
		wselect.enable();
		bselect.enable();
		end = true;
		mainboard.disable();
		mainboard.dispose();
		mainboard = new Main();
		mainboard.setVisible(true);
		mainboard.setResizable(false);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		c = (Cell) arg0.getSource();
		if (previous == null) {
			if (c.getPiece() != null) {
				if (c.getPiece().getColor() != turn)
					return;
				selectCell(c);
			}
		} else {
			if (c.x == previous.x && c.y == previous.y) {
				c.deselect();
				cleanDestinations(destinationList);
				destinationList.clear();
				previous = null;
			} else if (c.getPiece() == null || previous.getPiece().getColor() != c.getPiece().getColor()) {
				if (c.isPossibleDestination()) {
					if (c.getPiece() != null)
						c.removePiece();
					c.setPiece(previous.getPiece());
					if (previous.isCheck())
						previous.removeCheck();
					previous.removePiece();
					if (getKing(turn ^ 1).isInDanger(boardState)) {
						boardState[getKing(turn ^ 1).getx()][getKing(turn ^ 1).gety()].setCheck();
						if (checkmate(getKing(turn ^ 1).getColor())) {
							previous.deselect();
							if (previous.getPiece() != null)
								previous.removePiece();
							gameend();
						}
					}
					if (getKing(turn).isInDanger(boardState) == false)
						boardState[getKing(turn).getx()][getKing(turn).gety()].removeCheck();
					if (c.getPiece() instanceof King) {
						((King) c.getPiece()).setx(c.x);
						((King) c.getPiece()).sety(c.y);
					}
					changeTurn();
					if (!end) {
						timer.reset();
						timer.start();
					}
				}
				if (previous != null) {
					previous.deselect();
					previous = null;
				}
				cleanDestinations(destinationList);
				destinationList.clear();
			} else if (previous.getPiece().getColor() == c.getPiece().getColor()) {
				previous.deselect();
				cleanDestinations(destinationList);
				selectCell(c);
			}
		}
		if (c.getPiece() != null && c.getPiece() instanceof King) {
			((King) c.getPiece()).setx(c.x);
			((King) c.getPiece()).sety(c.y);
		}
	}

	private void selectCell(Cell c) {
		c.select();
		previous = c;
		destinationList.clear();
		destinationList = c.getPiece().getMoves(boardState, c.x, c.y);
		if (c.getPiece() instanceof King)
			destinationList = filterDestination(destinationList, c);
		else {
			if (boardState[getKing(turn).getx()][getKing(turn).gety()].isCheck())
				destinationList = new ArrayList<Cell>(filterDestination(destinationList, c));
			else if (destinationList.isEmpty() == false && kingInDanger(c, destinationList.get(0)))
				destinationList.clear();
		}
		highlightDestinations(destinationList);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	class Start implements ActionListener {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (white == null || black == null) {
				JOptionPane.showMessageDialog(controlPanel, "Fill in the details");
				return;
			}
			System.out.println(white);
			white.updateGamesPlayed();
			white.updatePlayers();
			black.updateGamesPlayed();
			black.updatePlayers();
			wnewPlayer.disable();
			bnewPlayer.disable();
			wselect.disable();
			bselect.disable();
			split.remove(temp);
			split.add(board);
			showPlayer.remove(timeSlider);
			mov = new JLabel("Move:");
			mov.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
			mov.setForeground(Color.red);
			showPlayer.add(mov);
			labelCHNC = new JLabel((turn==0) ? "white" : "black");
			labelCHNC.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			labelCHNC.setForeground(Color.blue);
			showPlayer.add(labelCHNC);
			displayTime.remove(start);
			displayTime.add(label);
			timer = new Time(label);
			timer.start();
		}
	}

	class TimeChange implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			timeRemaining = timeSlider.getValue() * 60;
		}
	}

	class SelectHandler implements ActionListener {
		private int color;

		SelectHandler(int i) {
			color = i;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			tempPlayer = null;
			// A lot of if statements. Not good.
			String n = (color == 0) ? wname : bname;
			JComboBox<String> jc = (color == 0) ? wcombo : bcombo; 
			JComboBox<String> ojc = (color == 0) ? bcombo : wcombo;
			// Would be more intuitive to call these bplayers and wplayers I think. 
			ArrayList<Player> pl = (color == 0) ? wplayer : bplayer;
			// ArrayList<Player> otherPlayer=(color==0)?bplayer:wplayer;
			ArrayList<Player> opl = Player.fetchPlayers();
			if (opl.isEmpty())
				return;
			JPanel det = (color == 0) ? wdetails : bdetails;
			// More intuitive to call these something like wplayerPanel and bplayerPanel.
			JPanel PL = (color == 0) ? whitePlayer : blackPlayer; 
			if (selected == true)
				det.removeAll();
			n = (String) jc.getSelectedItem();
			Iterator<Player> it = pl.iterator();
			Iterator<Player> oit = opl.iterator();
			while (it.hasNext()) {
				Player p = it.next();
				if (p.name().equals(n)) {
					tempPlayer = p;
					break;
				}
			}
			while (oit.hasNext()) {
				Player p = oit.next();
				if (p.name().equals(n)) {
					opl.remove(p);
					break;
				}
			}

			if (tempPlayer == null)
				return;
			if (color == 0)
				white = tempPlayer;
			else
				black = tempPlayer;
			bplayer = opl;
			ojc.removeAllItems();
			for (Player s : opl)
				ojc.addItem(s.name());
			det.add(new JLabel(" " + tempPlayer.name()));
			det.add(new JLabel(" " + tempPlayer.gamesPlayed()));
			det.add(new JLabel(" " + tempPlayer.gamesWon()));

			PL.revalidate();
			PL.repaint();
			PL.add(det);
			selected = true;
		}
	}

	class Handler implements ActionListener {
		private int color;

		Handler(int i) {
			color = i;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String n = (color == 0) ? wname : bname;
			JPanel j = (color == 0) ? whitePlayer : blackPlayer;
			ArrayList<Player> N = Player.fetchPlayers();
			Iterator<Player> it = N.iterator();
			JPanel det = (color == 0) ? wdetails : bdetails;
			n = JOptionPane.showInputDialog(j, "Enter your name");

			if (n != null) {

				while (it.hasNext()) {
					if (it.next().name().equals(n)) {
						JOptionPane.showMessageDialog(j, "Player exists");
						return;
					}
				}

				if (n.length() != 0) {
					Player tem = new Player(n);
					tem.updatePlayers(); // updates this player only.
					if (color == 0)
						white = tem;
					else
						black = tem;
				} else
					return;
			} else
				return;
			det.removeAll();
			det.add(new JLabel(" " + n));
			det.add(new JLabel(" 0"));
			det.add(new JLabel(" 0"));
			j.revalidate();
			j.repaint();
			j.add(det);
			selected = true;
		}
	}
}
