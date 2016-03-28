/**
* En klass f�ra att skapa ny spel p� olika vis.
*/
public class LoadNewGame {
	/**
	 * Funktion som best�mmer hur ett br�de skall skapas
	 * @param load - p� vilket s�tt den skall skapas
	 * @param gui - variabel till gui
	 * @param str - string som kan vara url eller filnamn
	 * @return - Ett br�de i form av int[][]
	 */
	public int[][] createBoard(HowToLoad load,BattleShipGUI gui,String str) {
		int[][] board = null;
		if(load.equals(HowToLoad.REGULAR)) {
			int antalSkeppsDelar = 0;
			int[] ships = {3,2,1};
			for(int i : ships) {
				antalSkeppsDelar += i;
			}
			board = BattleShipUtils.createBoard(5,5, ships);
			gui.setAntalSkeppsDelar(antalSkeppsDelar);
		}
		else if(load.equals(HowToLoad.FROMFILE)) {
			if(str.indexOf(".txt") == -1) {
				str += ".txt";
			}
			board = BattleShipUtils.createBoardFromFile(str, gui);
		}
		else if(load.equals(HowToLoad.FROMURL)) {
			board = BattleShipUtils.createBoardFromInternet("http://dt062g.programvaruteknik.nu/battleship/createBoard.php?width=5&height=4&ships=3,2,2", gui);
		}
		return board;
	}
}
