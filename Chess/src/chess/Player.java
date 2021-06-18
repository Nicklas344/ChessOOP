package chess;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * This is the Player Class It provides the functionality of keeping track of
 * all the users Objects of this class is updated and written in the Game's Data
 * Files after every Game
 *
 */
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer gamesPlayed;
	private Integer gamesWon;

	public Player(String name) {
		this.name = name.trim();
		// this.lname = lname.trim();
		gamesPlayed = new Integer(0);
		gamesWon = new Integer(0);
		System.out.println("Constructor" + gamesPlayed);
	}

	public String name() {
		return name;
	}

	public Integer gamesPlayed() {
		return gamesPlayed;
	}

	public Integer gamesWon() {
		return gamesWon;
	}

	public Integer winpercent() {
		return new Integer((gamesWon * 100) / gamesPlayed);
	}

	public void updateGamesPlayed() {
		System.out.println(gamesPlayed);
		gamesPlayed++;
	}

	public void updateGamesWon() {
		gamesWon++;
	}

	public static ArrayList<Player> fetchPlayers() {
		Player tempplayer;
		ObjectInputStream input = null;
		ArrayList<Player> players = new ArrayList<Player>();
		try {
			File infile = new File(System.getProperty("user.dir") + File.separator + "chessgamedata.dat");
			input = new ObjectInputStream(new FileInputStream(infile));
			try {
				while (true) {
					tempplayer = (Player) input.readObject();
					players.add(tempplayer);
				}
			} catch (EOFException e) {
				input.close();
			}
		} catch (FileNotFoundException e) {
			players.clear();
			return players;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				input.close();
			} catch (IOException e1) {
			}
			JOptionPane.showMessageDialog(null, "Unable to read the required Game files !!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return players;
	}

	public void updatePlayers() {
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		Player temp_player;
		File inputfile = null;
		File outputfile = null;
		try {
			inputfile = new File(System.getProperty("user.dir") + File.separator + "chessgamedata.dat");
			outputfile = new File(System.getProperty("user.dir") + File.separator + "tempfile.dat");
		} catch (SecurityException e) {
			JOptionPane.showMessageDialog(null, "Read-Write Permission Denied !! Program Cannot Start");
			System.exit(0);
		}
		boolean playerdonotexist;
		try {

			if (outputfile.exists() == false) // Make sure tempfile output exists.
				outputfile.createNewFile();
			if (inputfile.exists() == false) // Write to temp if chessgamedata doesn't exist.
			{
				output = new ObjectOutputStream(new java.io.FileOutputStream(outputfile, true));
				output.writeObject(this);
			} else {
				// chessgamedata does exist.
				input = new ObjectInputStream(new FileInputStream(inputfile));
				output = new ObjectOutputStream(new FileOutputStream(outputfile));
				playerdonotexist = true;
				try {
					// Not so happy about this part. Why not update them all at once, that makes
					// more sense to me. We are creating input-, output object streams for each player
					// object. Onless you are only updating one of them, then I guess it makes enough sense.
					while (true) {
						temp_player = (Player) input.readObject(); // Has multiple objects?
						if (temp_player.name().equals(name())) {
							output.writeObject(this);
							playerdonotexist = false;
						} else
							output.writeObject(temp_player); // Put it back.
					}
				} catch (EOFException e) {
					input.close();
				}
				if (playerdonotexist)
					output.writeObject(this); // Append to end of file.
			}
			inputfile.delete(); // Do a swich-a-ruu delete inputfile and rename temp outputfile.
			output.close();
			File newf = new File(System.getProperty("user.dir") + File.separator + "chessgamedata.dat");
			if (outputfile.renameTo(newf) == false)
				System.out.println("File Renameing Unsuccessful");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to read/write the required Game files !! Press ok to continue");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File");
		} catch (Exception e) {

		}
	}
}
