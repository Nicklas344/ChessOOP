package chess;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayerUtil {

	public static String[] fetchNames() {
		File file = new File(System.getProperty("user.dir") + File.separator + "namewinloses.dat");
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			String[] emptyList = {};
			return emptyList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
		return null;
	}
	
	public static int[] fetchWins() {
		return null;
	}
	
	public static int[] fetchLoses() {
		return null;
	}
	
	public static Object[] fetchData(String fileName) throws ClassNotFoundException, IOException {
		File inputFile = new File(System.getProperty("user.dir") + File.separator + fileName);
		String[] names;
		int[] wins, loses;

		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(inputFile));
		} catch (EOFException e) {}
		if (input == null) {
			throw new IOException("Could not open file.");
		}
		names = (String[]) input.readObject();
		wins = (int[]) input.readObject();
		loses = (int[]) input.readObject();
		
		Object[] data = {names, wins, loses};
		return data;
	}
	
	public static void updateData(String[] names, int[] wins, int[] loses, String fileName) 
			throws IOException {
		File inputFile = new File(System.getProperty("user.dir") + File.separator + fileName);
		File outputFile = new File(System.getProperty("user.dir") + File.separator + "tempdatafile.dat");

		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(outputFile));
		
		output.writeObject(names);
		output.writeObject(wins);
		output.writeObject(loses);
		output.flush();
		output.close();
		
		inputFile.delete();
		File newf = new File(System.getProperty("user.dir") + File.separator + fileName);
		outputFile.renameTo(newf);
	}
}
