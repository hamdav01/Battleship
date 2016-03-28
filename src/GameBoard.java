import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class GameBoard extends JPanel implements MouseListener {
	private BattleShipGUI gui;  // parent
	private int width;
	private int height;
	private float gridWidth;
	private float gridHeight;
	private int[][] board;
	private int antalSkott = 0;
	private int antalTräffar = 0;
	private ArrayList<Integer> ships = new ArrayList<Integer>();
	// Instansvariabler för antal träffar m.m?
	public GameBoard(BattleShipGUI gui) {
		this(null, gui);
		
	}

	public GameBoard(int[][] board, BattleShipGUI gui) {
		this.gui = gui;
		setBoard(board);
		addMouseListener(this);
	}

	public void setBoard(int[][] board) {
		this.board = board;

		if (board != null) {
			width = board[0].length;
			height = board.length;
		}
		setPreferredSize(new Dimension(width * 50, height * 50));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets ins = this.getInsets();

		int noOfGridsX = width + 1;
		int noOfGridsY = height + 1;
		int boardWidth = getWidth() - ins.left - ins.right;
		int boardHeight = getHeight() - ins.top - ins.bottom;
		gridWidth = (float)boardWidth / noOfGridsX;
		gridHeight = (float)boardHeight / noOfGridsY;

		g.setFont(g.getFont().deriveFont(Math.min(gridWidth,gridHeight) / 2));

		for (int x = 1; x < noOfGridsX; x++) {
			g.drawLine(ins.left + (int)(x * gridWidth), ins.top, ins.left + (int)(x * gridWidth), boardHeight);
			if (x > 0) {
				drawStringInGrid(g, "X" + (x - 1), x, 0);
			}
		}

		for (int y = 1; y < noOfGridsY; y++) {
			g.drawLine(ins.left, ins.top + (int)(y * gridHeight), boardWidth, ins.top + (int)(y * gridHeight));
			if (y > 0) {
				drawStringInGrid(g, "Y" + (y - 1), 0, y);
			}
		}
		
		if ( board != null) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if(board[y][x] == -1) {	
						drawCross(g,  x,  y);
					}
				}
			}
		}
		if(ships.size() != 0) {
			for(int i=0;i<ships.size()-1;i+=2) {
				if(board[ships.get(i)][ships.get(i+1)] == -1) {
					continue;
				}
				drawStringInGrid(g, Integer.toString(board[ships.get(i)][ships.get(i+1)]), ships.get(i+1) + 1, ships.get(i) + 1);
			}
		}
	}
	/**
	 * Lägger till ett skepp som blivit träffad i en array så de kan skrivas ut.
	 * @param x - koordinat
	 * @param y - koordinat
	 */
	private void addShipToArray(int x,int y) {
		for(int i=0;i<ships.size();i++) {
			if(ships.get(i) == x) {
				if(i+1<ships.size()) {
					if(ships.get(i+1) == y) {
						return;
					}
				}
			}
		}
		addXLine(x,y,'+');
		addXLine(x,y,'-');
		addYLine(x,y,'-');
		addYLine(x,y,'+');
	}
	/**
	 * Kollar i x-led ifall de finns några skepp
	 * @param x - koordinat
	 * @param y - koordinat
	 * @param operator - ifall minus eller plus skall användas
	 */
	private void addXLine(int x, int y,char operator) {
		int i = 0;
		while(true) {
			switch(operator) {
				case '+': i++; break;
				case '-': i--; break;
			}
			int test = i+y;
			if(i+y>=width || y+i < 0) {
				break;
			}
			if(board[x][y+i] == board[x][y]) {
				ships.add(x);
				ships.add(y+i);
				continue;
			}
			break;
		}
	}
	/**
	 * Kollar i y-led ifall de finns några skepp
	 * @param x - koordinat
	 * @param y - koordinat
	 * @param operator - ifall minus eller plus skall användas
	 */
	private void addYLine(int x, int y,char operator) {
		int i = 0;
		while(true) {
			switch(operator) {
				case '+': i++; break;
				case '-': i--; break;
			}
			if(i+x>=height || x+i<0) {
				break;
			}
			else if(board[x+i][y] == board[x][y]) {
				ships.add(x+i);
				ships.add(y);
				continue;
			}
			break;
		}
	}
	/**
	 * Ritar ut ett kors i en grid ruta
	 * @param g - Graphic object.
	 * @param x - koordinat
	 * @param y - koordinat
	 * @return void
	 */
	private void drawCross(Graphics g, int x, int y) {
		Graphics2D gg = (Graphics2D)g;
		gg.setStroke(new BasicStroke(4));
		Color c  = gg.getColor();
		gg.setColor(Color.RED);

		int x1 = (int)((((x+1)*gridWidth)+10));
		int x2 = (int)((((x+1)*gridWidth)+gridWidth)-10);
		int y1 = (int)((((y+1)*gridHeight)+gridHeight)-10);
		int y2 = (int)((((y+1)*gridHeight)+10));
		gg.drawLine(x1,y1,x2,y2);
		gg.drawLine(x1,y2,x2,y1);
		gg.setColor(c);
	}
	/**
	 * Ritar ut en sträng på en grid ruta.
	 * @param g - Graphic object.
	 * @param s - strängen som skall ritas ut.
	 * @param x - koordinat
	 * @param y - koordinat
	 * @return void
	 */
	private void drawStringInGrid(Graphics g, String s, int x, int y) {
		FontMetrics fm   = g.getFontMetrics(g.getFont());
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(s, g);

		int textHeight = (int)(rect.getHeight());
		int textWidth  = (int)(rect.getWidth());

		g.drawString(s, (int)(x * gridWidth + gridWidth / 2) - (textWidth / 2), (int)(y * gridHeight + gridHeight / 2) - (textHeight / 2)   + fm.getAscent());
	}
	/**
	 * Bestämmer vad som skall göras efter ett skott.
	 * @param g - Graphic object.
	 * @param s - strängen som skall ritas ut.
	 * @param x - koordinat
	 * @param y - koordinat
	 * @return void
	 */
	public void shoot(int x, int y) {
		if(board[y][x] == -1) {
			return;
		}
		else if (board[y][x] != 0) {
			antalTräffar++;
			addShipToArray(y,x);
		}
		board[y][x] = -1;
		antalSkott++;
		
		repaint();
		gui.incStatus(antalSkott, antalTräffar);
		// Lägg till kod här för att uppdatera antal skjutna skott och statusfältet m.m.
	}

	public void mouseClicked(MouseEvent e) {
		int x = (int)(e.getX() / gridWidth);
		int y = (int)(e.getY() / gridHeight);

		if (x > 0 && y > 0) {
			shoot(x - 1, y - 1);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}