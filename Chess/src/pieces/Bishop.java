package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Bishop Class. The Move Function defines the basic rules for
 * movement of Bishop on a chess board
 *
 */
public class Bishop extends Piece {

	public Bishop(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	@Override
	public ArrayList<Cell> getMoves(Cell state[][], int x, int y) {
		// Bishop can mmove diagonally in all 4 directions (NW,NE,SW,SE)
		possibleMoves.clear();
		addMovesInLine(state, x, y, 1, 1);
		addMovesInLine(state, x, y, 1, -1);
		addMovesInLine(state, x, y, -1, 1);
		addMovesInLine(state, x, y, -1, -1);
		return possibleMoves;
	}
}
