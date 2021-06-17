package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Queen Class inherited from the abstract Piece class
 *
 */
public class Queen extends Piece {

	// Constructors
	public Queen(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	// Move Function Defined
	@Override
	public ArrayList<Cell> getMoves(Cell state[][], int x, int y) {
		// Queen has most number of possible moves
		// Queen can move any number of steps in all 8 direction
		// The possible moves of queen is a combination of Rook and Bishop
		possibleMoves.clear();

		// Add moves in both vertical, horisontal, and diagonal directions.
		int[] dxs = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dys = { -1, 0, 1, -1, 1, -1, 0, 1 };
		for (int i = 0; i < dxs.length; i++) {
			addMovesInLine(state, x, y, dxs[i], dys[i]);
		}

		return possibleMoves;
	}
}
