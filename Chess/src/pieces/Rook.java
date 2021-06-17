package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Rook class inherited from abstract Piece class
 *
 */
public class Rook extends Piece {

	// Constructor
	public Rook(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	// Move function defined
	@Override
	public ArrayList<Cell> move(Cell state[][], int x, int y) {
		// Rook can move only horizontally or vertically
		possiblemoves.clear();
		addMovesInLine(state, x, y, 1, 0);
		addMovesInLine(state, x, y, -1, 0);
		addMovesInLine(state, x, y, 0, 1);
		addMovesInLine(state, x, y, 0, -1);
		return possiblemoves;
	}
}
