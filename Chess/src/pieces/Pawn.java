package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 *
 */
public class Pawn extends Piece {

	// COnstructors
	public Pawn(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	// Move Function Overridden
	@Override
	public ArrayList<Cell> move(Cell state[][], int x, int y) {
		// Pawn can move only one step except the first chance when it may move 2 steps
		// It can move in a diagonal fashion only for attacking a piece of opposite
		// color
		// It cannot move backward or move forward to attact a piece
		possiblemoves.clear();

		int dx = (getcolor() == 0) ? -1 : 1;
		if (x == (7 + 7 * dx) / 2) {
			return possiblemoves;
		}

		if (state[x + dx][y].getpiece() == null) {
			possiblemoves.add(state[x + dx][y]);
			if (x == (5 - 5 * dx) / 2 + 1) {
				if (state[(1 - dx) / 2 + 3][y].getpiece() == null)
					possiblemoves.add(state[(1 - dx) / 2 + 3][y]);
			}
		}

		if ((y > 0) && (state[x + dx][y - 1].getpiece() != null)
				&& (state[x + dx][y - 1].getpiece().getcolor() != this.getcolor()))
			possiblemoves.add(state[x + dx][y - 1]);
		if ((y < 7) && (state[x + dx][y + 1].getpiece() != null)
				&& (state[x + dx][y + 1].getpiece().getcolor() != this.getcolor()))
			possiblemoves.add(state[x + dx][y + 1]);

		return possiblemoves;
	}
}
