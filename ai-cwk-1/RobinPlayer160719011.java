import java.awt.Color;
import java.util.*;
import java.util.ArrayList;

class RobinPlayer160719011 extends GomokuPlayer {

	private class MoveScore {
		private MoveScore(Move move, int score) {
			move = move;
			score = score;
		}
	}

	public Move chooseMove(Color[][] board, Color me) {
	//first arg is boardstate
	//second arg is color I am playing
		//so if I'm black (black goes first) I am max, if not I am min
		if (me == Color.black) {
			myMove = alphaBeta(board, me, 5, -100, 100, true);
		} else {
			myMove = alphaBeta(board, me, 5, -100, 100, false);
		}
		return myMove.move;
	}//chooseMove()

	public ArrayList<MoveScore> prepareMoves(Color[][] board) {
		ArrayList<MoveScore> moves = new ArrayList<MoveScore>(); //needs to be arraylist!!
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				if (board[row][col] == null) {
					moves.add(new MoveScore(new Move(row, col), 0);	
				}
			}
		}
		return moves;
	}//prepareMoves

	
	public MoveScore alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		ArrayList<MoveScore> moves = prepareMoves(board);//calls prepareMoves to get legal moves
		if (depth == 0 || board == terminal) {
			//return the score for the terminal state! basically tells the function to stop
			//the search and return each move up the call stack
			//this is either at the win-state or at the final user-defined layer if depth-bounded
		}
		if (max) {
			MoveScore returnedMove;
			MoveScore bestMove = null;	
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me; //create the subnode board
				//below wont work right now, need call alphaBeta on returnedMove, compare .score on both
				//then assign the best to bestmove? 
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, false);
				bestMove = (returnedMove.score > currentMove.score) ? returnedMove : currentMove;
				//should below be doing a different comparison? 
				alpha = Math.max(alpha.score, returnedMove.score);//set alpha 
				board[currentMove.move.row][currentMove.move.col] = null;//reset the board
				if (beta <= alpha) {//so this part is my pruning bit
					bestMove = alpha;
					return bestMove;
				}
			}
			return bestMove;
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
