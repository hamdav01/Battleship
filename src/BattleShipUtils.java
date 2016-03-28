import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.print.DocFlavor.STRING;

public class BattleShipUtils {
	/**
	 * Funktion för att ladda in en board ifrån url
	 * @param url - url:en
	 * @param gui - gui så man kan sätta antalet skeppdelar.
	 * @return - brädet.
	 */
	public static int[][] createBoardFromInternet(String url, BattleShipGUI gui) {
		int height = 0,  width = 0;
		int antalSkeppsDelar = 0;
		int board[][] = null;
		try {
			URL u = new URL(url);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String line = in.readLine();
			in.close();
			String[] lines = line.split("<br>");
			
			String[] heightWidth = lines[0].split("x");
			String[] otherLines = line.split(heightWidth[0] + "x" + (heightWidth[1]));
			String theLines = otherLines[1];
			theLines = theLines.replace("<br>", "");
			theLines.trim();
			width = Integer.parseInt(heightWidth[0]);
			height = Integer.parseInt(heightWidth[1]);
			board = new int[height][width];
			int tempWidth = 0;
			int tempHeight = 0;
			
			for (int i = 0, n = theLines.length(); i < n; i++) {
				int value = Character.getNumericValue(theLines.charAt(i));
			    if(value != 0) {antalSkeppsDelar++;}
			    board[tempHeight%height][tempWidth%width] = value;
			    tempWidth++;
			    if(tempWidth%width == 0) {
			    	tempHeight++;
			    }
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.setAntalSkeppsDelar(antalSkeppsDelar);
		return board;
	}
	/**
	 * Funktion för att ladda in en board ifrån fil
	 * @param p_sFileName - filnamn
	 * @param gui - gui så man kan sätta antalet skeppdelar.
	 * @return - brädet.
	 */
	public static int[][] createBoardFromFile(String p_sFileName,BattleShipGUI gui) {
		int board[][] = null;
		int height = 0,  width = 0;
		int antalSkeppsDelar = 0;
		BufferedReader inFil = null;
		try {
			inFil = new BufferedReader(new FileReader(p_sFileName));
			String[] widthHeight = inFil.readLine().split("x");
			width = Integer.parseInt(widthHeight[0]);
			height = Integer.parseInt(widthHeight[1]);
			board = new int[height][width];
			int tempWidth = 0;
			int tempHeight = 0;
			String line = "";
			while( null != (line = inFil.readLine())) {
				for (int i = 0, n = line.length(); i < n; i++) {
				    int value = Character.getNumericValue(line.charAt(i));
				    if(value != 0) {antalSkeppsDelar++;}
				    board[tempHeight%height][tempWidth%width] = value;
				    tempWidth++;
				}
				tempHeight++;
			}
			inFil.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.setAntalSkeppsDelar(antalSkeppsDelar);
		return board;
	}
	/**
	 * Funktion för att ladda in en board ifrån på slumpmässightvis.
	 */
	public static int[][] createBoard(int width, int height, int[] ships) {
		// create board
		int board[][] = new int[height][width];

		Random rnd = new Random();
		boolean valid = true;
		int tries = 0;

		// For each ship
		for (int i = 0; i < ships.length; i++) {
			tries = 0;
			do {
				valid = true;
				int direction = rnd.nextInt(2);
				int shipStartX = -1;
				int shipStartY = -1;

				if (direction == 0) { // place ship horizontal
					shipStartX = rnd.nextInt(width - ships[i] + 1);
					shipStartY = rnd.nextInt(height);
				}
				else { // place ship vertical
					shipStartX = rnd.nextInt(width);
					shipStartY = rnd.nextInt(height - ships[i] + 1);
				}

				// check if placement of ship (shipStartX and Y) is valid
				for(int row = 0; row < height; row++) {
					for (int col = 0; col < width;  col++) {
						if (direction == 0 && (shipStartY == row || shipStartY == row - 1 || shipStartY == row + 1) && col >= shipStartX - 1 && col < shipStartX + ships[i] + 1) {
							if (board[row][col] != 0) {
								valid = false;
							}
						}
						if (direction == 1 && (shipStartX == col || shipStartX == col - 1 || shipStartX == col + 1) && row >= shipStartY - 1 && row < shipStartY + ships[i] + 1) {
							if (board[row][col] != 0) {
								valid = false;
							}
						}
					}
				}

				// place ship on board
				if (valid) {
					for(int row = 0; row < height; row++) {
						for (int col = 0; col < width;  col++) {
							if (direction == 0 && shipStartY == row && col >= shipStartX && col < shipStartX  + ships[i])
								board[row][col] = ships[i];
							if (direction == 1 && shipStartX == col && row >= shipStartY && row < shipStartY + ships[i])
								board[row][col]=ships[i];
						}
					}
				}

				tries++;
			} while(!valid && tries < 200);

			// Colud not place all ships
			if (tries >= 200) return null;
		}
		return board;
	}

	public static void printBoard(int[][] board) {
		if (board == null) {
			System.out.println("null");
			return;
		}

		for(int row=0; row< board.length; row++) { // rows
			for (int col=0; col<board[row].length;  col++) {// cols
				System.out.print(board[row][col]);
			}
			System.out.println();
		}
		System.out.println();
	}
}