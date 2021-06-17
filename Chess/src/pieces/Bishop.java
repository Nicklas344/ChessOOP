package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Bishop Class. The Move Function defines the basic rules for
 * movement of Bishop on a chess board
 *
 *
 */
public class Bishop extends Piece {

	// Constructor
	public Bishop(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	// move function defined. It returns a list of all the possible destinations of
	// a Bishop
	// The basic principle of Bishop Movement on chess board has been implemented
	@Override
	public ArrayList<Cell> getMoves(Cell state[][], int x, int y) {
		// Bishop can Move diagonally in all 4 direction (NW,NE,SW,SE)
		// This function defines that logic
		possibleMoves.clear();
		addMovesInLine(state, x, y, 1, 1);
		addMovesInLine(state, x, y, 1, -1);
		addMovesInLine(state, x, y, -1, 1);
		addMovesInLine(state, x, y, -1, -1);
		return possibleMoves;
	}
}
