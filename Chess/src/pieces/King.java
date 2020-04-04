package pieces;

import java.util.ArrayList;
import java.lang.Math;

import chess.Cell;

public class King extends Piece{

	private int x,y; //Extra variables for King class to keep a track of king's position

	//King Constructor
	public King(String i,String p,int c,int x,int y)
	{
		setx(x);
		sety(y);
		setId(i);
		setPath(p);
		setColor(c);
	}

	//general value access functions
	public void setx(int x)
	{
		this.x=x;
	}
	public void sety(int y)
	{
		this.y=y;
	}
	public int getx()
	{
		return x;
	}
	public int gety()
	{
		return y;
	}
	//Move Function for King Overridden from Pieces
	public ArrayList<Cell> move(Cell state[][],int x,int y)
	{
		//King can move only one step. So all the adjacent 8 cells have been considered.
		possiblemoves.clear();
		int posx[]={x,x,x+1,x+1,x+1,x-1,x-1,x-1};
		int posy[]={y-1,y+1,y-1,y,y+1,y-1,y,y+1};
		for(int i=0;i<8;i++)
			if((posx[i]>=0&&posx[i]<8&&posy[i]>=0&&posy[i]<8))
				if((state[posx[i]][posy[i]].getpiece()==null||state[posx[i]][posy[i]].getpiece().getcolor()!=this.getcolor()))
					possiblemoves.add(state[posx[i]][posy[i]]);
		return possiblemoves;
	}

	//Function to check if king is under threat
	//It checks whether there is any piece of opposite color that can attack king for a given board state
	public boolean isindanger(Cell state[][])
    {
		//Checking for attack from vertical, horisontal, and diagonal directions.

		int[] dxs = {-1, -1, -1 , 0, 0,  1, 1, 1};
		int[] dys = {-1,  0,  1, -1, 1, -1, 0, 1};
		for (int i = 0; i < dxs.length; i++) {
			if (isindangerfromline(state, getx(), gety(), dxs[i], dys[i]))
				return true;
		}


		//Checking for attack from the Knight of opposite color
		int posx[]={x+1,x+1,x+2,x+2,x-1,x-1,x-2,x-2};
		int posy[]={y-2,y+2,y-1,y+1,y-2,y+2,y-1,y+1};
		for(int i=0;i<8;i++)
			if((posx[i]>=0&&posx[i]<8&&posy[i]>=0&&posy[i]<8))
				if (pieceOfColorAt(state[posx[i]][posy[i]], 1-getcolor(), Knight.class))
					return true;

		//Checking for attack from King of opposite color
		int pox[]={x+1,x+1,x+1,x,x,x-1,x-1,x-1};
		int poy[]={y-1,y+1,y,y+1,y-1,y+1,y-1,y};
		{
			for(int i=0;i<8;i++)
				if((pox[i]>=0&&pox[i]<8&&poy[i]>=0&&poy[i]<8))
				  if (pieceOfColorAt(state[pox[i]][poy[i]], 1-getcolor(), King.class))
						return true;
		}

		//Checking for attack from pawn of oppesite color.
		if(getcolor()==0)
		{
			if(x>0 && y>0 && pieceOfColorAt(state[x-1][y-1], 1, Pawn.class))
				return true;
			if(x>0 && y<7 && pieceOfColorAt(state[x-1][y+1], 1, Pawn.class))
				return true;
		}
		else
		{
			if(x<7 && y>0 && pieceOfColorAt(state[x+1][y-1], 0, Pawn.class))
				return true;
			if(x<7 && y<7 && pieceOfColorAt(state[x+1][y+1], 0, Pawn.class))
				return true;
		}

		return false;
	}

	private boolean pieceOfColorAt(Cell cell, int color, Class pieceType) {
		if (cell.getpiece() == null) return false;
		if (cell.getpiece().getcolor() != color) return false;
		return (cell.getpiece().getClass() == pieceType);
	}

	// Checks if the is danger from either a rook, bishop, or queen from a specific direction.
	private boolean isindangerfromline(Cell state[][], int x0, int y0, int dx, int dy) {
		if (Math.abs(dx) + Math.abs(dy) != 1 && Math.abs(dx) + Math.abs(dy) != 2) {
			Exception e = new Exception("Arguments dx and dy have to define a vertical, horisontal, or diagonal line");
			e.printStackTrace();
			return false;
		}
		int tmpx = x0 + dx;
		int tmpy = y0 + dy;
		while (tmpx <= 7 && tmpx >= 0 && tmpy <= 7 && tmpy >= 0)
		{
			Piece piece = state[tmpx][tmpy].getpiece();
			if (piece == null) {
				tmpx += dx;
				tmpy += dy;
				continue;
			}

			if (piece.getcolor() == this.getcolor())
				break;
			else
			{
				if (dx == 0 || dy == 0)	{
					if ((piece instanceof Rook) || (piece instanceof Queen))
						return true;
					else
						break;
				}	else {
					if ((piece instanceof Bishop) || (piece instanceof Queen))
						return true;
					else
						break;
				}
			}
		}
		return false;
	}
}
