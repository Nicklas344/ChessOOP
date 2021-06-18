package chess;

import java.awt.*;
import javax.swing.*;

import pieces.*;

/**
 * This is the Cell Class. It is the token class of our GUI. There are total of
 * 64 cells that together makes up the Chess Board
 */
public class Cell extends JPanel implements Cloneable {
	private static final long serialVersionUID = 1L;
	private boolean isPossibleDestination;
	private JLabel content;
	private Piece piece;
	int x, y; // is public because this is to be accessed by all the other class
	private boolean isSelected = false;
	private boolean isCheck = false;

	public Cell(int x, int y, Piece p) {
		this.x = x;
		this.y = y;
		
		setLayout(new BorderLayout());

		if ((x + y) % 2 == 0)
			setBackground(new Color(113, 198, 113));

		else
			setBackground(Color.white);

		if (p != null)
			setPiece(p);
	}

	/**
	 * A constructor that takes a cell as argument and returns a new cell will the
	 * same data but different reference.
	 * 
	 * @param cell Cell to create a copy of.
	 * @throws CloneNotSupportedException
	 */
	public Cell(Cell cell) throws CloneNotSupportedException {
		this.x = cell.x;
		this.y = cell.y;
		setLayout(new BorderLayout());
		if ((x + y) % 2 == 0)
			setBackground(new Color(113, 198, 113));
		else
			setBackground(Color.white);
		if (cell.getPiece() != null) {
			setPiece(cell.getPiece().getCopy());
		} else
			piece = null;
	}

	public void setPiece(Piece p) {
		piece = p;
		ImageIcon img = new javax.swing.ImageIcon(this.getClass().getResource(p.getPath()));
		content = new JLabel(img);
		this.add(content);
	}

	public void removePiece() {
		if (piece instanceof King) {
			piece = null;
			this.remove(content);
		} else {
			piece = null;
			this.remove(content);
		}
	}

	public Piece getPiece()	{
		return this.piece;
	}

	public void select() {
		this.setBorder(BorderFactory.createLineBorder(Color.red, 6));
		this.isSelected = true;
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void deselect() {
		this.setBorder(null);
		this.isSelected = false;
	}

	public void setPossibleDestination() {
		this.setBorder(BorderFactory.createLineBorder(Color.blue, 4));
		this.isPossibleDestination = true;
	}

	public void removePossibleDestination() {
		this.setBorder(null);
		this.isPossibleDestination = false;
	}

	public boolean isPossibleDestination() {
		return this.isPossibleDestination;
	}

	public void setCheck() {
		this.setBackground(Color.RED);
		this.isCheck = true;
	}

	public void removeCheck() {
		this.setBorder(null);
		if ((x + y) % 2 == 0)
			setBackground(new Color(113, 198, 113));
		else
			setBackground(Color.white);
		this.isCheck = false;
	}

	public boolean isCheck() {
		return isCheck;
	}
}