package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Piece Class. It is an abstract class from which all the actual
 * pieces are inherited. It defines all the function common to all the pieces
 * The move() function an abstract function that has to be overridden in all the
 * inherited class It implements Cloneable interface as a copy of the piece is
 * required very often
 */
public abstract class Piece implements Cloneable {

	private int color;
	private String id = null;
	private String path;
	protected ArrayList<Cell> possibleMoves = new ArrayList<Cell>(); // Protected (access from child classes)

	public abstract ArrayList<Cell> getMoves(Cell pos[][], int x, int y); // Abstract Function. Must be overridden

	public void setId(String id) {
		this.id = id;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setColor(int c) {
		this.color = c;
	}

	public String getPath() {
		return path;
	}

	public String getId() {
		return id;
	}

	public int getColor() {
		return this.color;
	}

	public Piece getCopy() throws CloneNotSupportedException {
		// Return a "shallow" copy of the object. The copy has exact
		// same variable value but different reference
		return (Piece) this.clone();
	}

	protected void addMovesInLine(Cell state[][], int x0, int y0, int dx, int dy) {
		int tmpx = x0 + dx;
		int tmpy = y0 + dy;
		while (tmpx <= 7 && tmpx >= 0 && tmpy <= 7 && tmpy >= 0) {
			if (state[tmpx][tmpy].getPiece() == null) {
				possibleMoves.add(state[tmpx][tmpy]);
			} else if (state[tmpx][tmpy].getPiece().getColor() == this.getColor()) {
				break;
			} else {
				possibleMoves.add(state[tmpx][tmpy]);
				break;
			}
			tmpx += dx;
			tmpy += dy;
		}
	}
}
