import java.awt.Color;
import java.util.Collection;

class RobinPlayer160719011 extends GomokuPlayer {

	public Move chooseMove(Color[][] board, Color me) {
	//first arg is boardstate
	//second arg is color I am playing
		
		while (true) {
			int row = (int) (Math.random() *8);
			int col = (int) (Math.random() *8);
			if (board[row][col] == null) {
				return new Move(row, col);
			}	
		}
	} //chooseMove()
}//class RobinPlayer160719011
