import java.awt.Color;
import java.util.*;
import java.util.ArrayList;

class RobinPlayer160719011 extends GomokuPlayer {

	public Move chooseMove(Color[][] board, Color me) {
	//first arg is boardstate
	//second arg is color I am playing
		//so if I'm black (black goes first) I am max, if not I am min
		if (me == Color.black) {
			alphaBeta(board, me, 5, -100, 100, true);
		} else {
			alphaBeta(board, me, 5, -100, 100, false);
		}
		//return move??
	}//chooseMove()

	public ArrayList<Move> prepareMoves(Color[][] board) {
		ArrayList<Move> moves = new ArrayList<Move>(); //needs to be arraylist!!
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				if (board[row][col] == null) {
					moves.add(new Move(row, col));
				}
			}
		}
		return moves;
	}//prepareMoves

	
	public int alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		ArrayList<Move> moves = prepareMoves(board);//calls prepareMoves to get legal moves
		if (depth == 0 || board == terminal) {
			//return the score for the terminal state! basically tells the function to stop
			//the search and return each move up the call stack
			//this is either at the win-state or at the final user-defined layer if depth-bounded
		}
		if (max) {
			int score = -100;
			for (Move move : moves) {
				board[move.row][move.col] = me; //create the subnode board
				score = Math.max(score, alphaBeta(board, me, depth-1, alpha, beta, false));
				alpha = Math.max(alpha, score);
				board[move.row][move.col] = null;
				if (beta <= alpha) {
					return beta;//or the other way around?
				}
			}
			return score; //or some variant???	
		}
		else {
			int score = 100;
			for (Move move : moves) {
				board[move.row][move.col] = me;
				score = Math.max(score, alphaBeta(board, me, depth-1, alpha, beta, true));
				beta = Math.min(beta, score);
				board[move.row][move.col] = null;
				if (beta <= alpha) {
					return alpha;//or the other way around?
				}
			}
			return score; //or some variant???
		}
	}//alphaBeta()	
			

	public int[][] eval(Color[][] board, Color me) {

	//I THINK THIS EVAL FUNCTION IS COMPLETELY WRONG. I'M NOT EVALUATING THE UTILITY OF THE MOVE
	//I'M INSTEAD EVALUATING THE UTILITY OF THE WHOLE BOARD??
	//SHOULD CHECK FOR THE PRESENCE OF ROWS OF THE PLAYER COLOUR WHETHER MAX OR MIN
	//MORE POTENTIAL ROWS THE HIGHER THE UTILITY? FITS WITH THE THREAT THINGw	
	//square contains piece of your colour, check all around it for patterns? then add to main scoring variable
	//is there a way to check squares off? once they've been included in a pattern they're not to be checked again
	//depending on what the patternCounter reaches (ie, how long your line is) you pass back a higher score multiplier??
	}//eval()
}//class RobinPlayer160719011
