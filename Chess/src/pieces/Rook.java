package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Rook class inherited from abstract Piece class
 *
 */
public class Rook extends Piece {

	public Rook(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	@Override
	public ArrayList<Cell> getMoves(Cell state[][], int x, int y) {
		// Rook can move only horizontally or vertically
		possibleMoves.clear();
		addMovesInLine(state, x, y, 1, 0);
		addMovesInLine(state, x, y, -1, 0);
		addMovesInLine(state, x, y, 0, 1);
		addMovesInLine(state, x, y, 0, -1);
		return possibleMoves;
	}
}
