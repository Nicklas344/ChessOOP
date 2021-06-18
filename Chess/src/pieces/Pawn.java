package pieces;

import java.util.ArrayList;

import chess.Cell;

/**
 * This is the Pawn Class inherited from the piece
 */
public class Pawn extends Piece {
	
	public Pawn(String i, String p, int c) {
		setId(i);
		setPath(p);
		setColor(c);
	}

	@Override
	public ArrayList<Cell> getMoves(Cell state[][], int x, int y) {
		// Pawn can move only one step except the first chance when it may move 2 steps
		// It can move in a diagonal fashion only for attacking a piece of opposite
		// color. It cannot move backward or move forward to attack a piece
		possibleMoves.clear();

		int dx = (getColor() == 0) ? -1 : 1;
		if (x == (7 + 7 * dx) / 2) {
			return possibleMoves;
		}

		if (state[x + dx][y].getPiece() == null) {
			possibleMoves.add(state[x + dx][y]);
			if (x == (5 - 5 * dx) / 2 + 1) {
				if (state[(1 - dx) / 2 + 3][y].getPiece() == null)
					possibleMoves.add(state[(1 - dx) / 2 + 3][y]);
			}
		}

		if ((y > 0) && (state[x + dx][y - 1].getPiece() != null)
				&& (state[x + dx][y - 1].getPiece().getColor() != this.getColor()))
			possibleMoves.add(state[x + dx][y - 1]);
		if ((y < 7) && (state[x + dx][y + 1].getPiece() != null)
				&& (state[x + dx][y + 1].getPiece().getColor() != this.getColor()))
			possibleMoves.add(state[x + dx][y + 1]);

		return possibleMoves;
	}
}
