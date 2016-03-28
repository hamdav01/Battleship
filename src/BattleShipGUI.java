import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleShipGUI extends JFrame implements ActionListener {
	private GameBoard gameBoard = null;
	private JLabel status = new JLabel();
	private int antalSkeppsDelar = 0;
	private JMenuItem nyttSpel = new JMenuItem("Nytt spel");
	private JMenuItem nyttSpelFil = new JMenuItem("Nytt spel ifrån fil");
	private JMenuItem nyttSpelUrl = new JMenuItem("Nytt spel ifrån url");
	private JMenuBar mb = new JMenuBar();
	private JMenu iMen = new JMenu("Arkiv");

	public BattleShipGUI() {
		// Sätt titel på fönstret
		super("Battle Ship");

		// Vad ska ske när vi stänger fönstret?
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Centrera fönstret på skärmen
		setLocationRelativeTo(null);

		// Ange vilken layout som ska användas i fönstret
		setLayout(new BorderLayout());
		// sätter upp meny
		
		// Initiera alla komponenter
		initComponents();
		
		// Sätt storleken på fönstret
		pack();

		// Gör fönstret synligt
		setVisible(true);
	}

	private void initComponents() {
		add(status,BorderLayout.SOUTH);
		setJMenuBar(mb);
		mb.add(iMen);
		iMen.add(nyttSpel);
		iMen.add(nyttSpelFil);
		iMen.add(nyttSpelUrl);
		nyttSpel.addActionListener(this);
		nyttSpelFil.addActionListener(this);
		nyttSpelUrl.addActionListener(this);
		antalSkeppsDelar = 0;
		int[][] board = new LoadNewGame().createBoard(HowToLoad.REGULAR, this,"");
		StartNewGame(board);
	}
	public void setAntalSkeppsDelar(int antal) {
		antalSkeppsDelar = antal;
	}
	private void StartNewGame(int[][] board) {
		gameBoard = new GameBoard(board,this);
		add(gameBoard, BorderLayout.CENTER);
		status.setText("Antal skott: 0 Antal träffar 0(" + antalSkeppsDelar + ")");
	}
	public void incStatus(int skott,int träff) {
		status.setText("Antal skott: " + skott + " Antal träffar: " + träff + "(" + antalSkeppsDelar + ")");
		if(träff >= antalSkeppsDelar) {
			int dialogResult = JOptionPane.showConfirmDialog (null, "Du har vunnit vill du spela igen?");
			if(dialogResult == JOptionPane.YES_OPTION){ 
				this.remove(gameBoard);
				int[][] board = new LoadNewGame().createBoard(HowToLoad.REGULAR, this,"");
				StartNewGame(board);
			}
			else {
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		// Skapa vår JFrame på ett trådsäkert sätt
		SwingUtilities.invokeLater(() -> new BattleShipGUI());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == nyttSpel) {
			this.remove(gameBoard);
			int[][] board = new LoadNewGame().createBoard(HowToLoad.REGULAR, this,"");
			StartNewGame(board);
			
		}
		else if(e.getSource() == nyttSpelFil) {
			String response = JOptionPane.showInputDialog(null,
	                "Filnam",
	                "Skriv namnet på filen som håller i spelplanen",
	                JOptionPane.QUESTION_MESSAGE);
			if(response == null) {
				return;
			}
			this.remove(gameBoard);
			int[][] board = new LoadNewGame().createBoard(HowToLoad.FROMFILE, this,response);
			StartNewGame(board);
		}
		else if(e.getSource() == nyttSpelUrl) {
			this.remove(gameBoard);
			int[][] board = new LoadNewGame().createBoard(HowToLoad.FROMURL, this,"");
			StartNewGame(board);
		}
	}
}