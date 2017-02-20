import java.awt.Color;
import java.util.*;
import java.util.ArrayList;

class RobinPlayer160719011 extends GomokuPlayer {

	private class MoveScore {
		public Move move;
		public int score;

		private MoveScore(Move a, int b) {
			move = a;
			score = b;
		}
	}

	public Move chooseMove(Color[][] board, Color me) {
	//first arg is boardstate
	//second arg is color I am playing
		MoveScore myMove;
		//so if I'm black (black goes first) I am max, if not I am min
		if (me == Color.white) {
			myMove = alphaBeta(board, me, 5, -200, 200, true);
		} else {
			myMove = alphaBeta(board, me, 5, -200, 200, false);
		}
		return myMove.move;
	}//chooseMove()

	public ArrayList<MoveScore> prepareMoves(Color[][] board) {
		ArrayList<MoveScore> moves = new ArrayList<MoveScore>(); //needs to be arraylist!!
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				if (board[row][col] == null) {
					moves.add(new MoveScore(new Move(row, col), 0));	
				}
			}
		}
		return moves;
	}//prepareMoves

	
	public MoveScore alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		ArrayList<MoveScore> moves = prepareMoves(board);//calls prepareMoves to get legal moves
		if (depth == 0) {//also needs to be if the board has hit a win condition!!!
			MoveScore terminalState = new MoveScore(null, 0);
			terminalState.score = eval(board, me, max);
			return terminalState;

			//return the score for the terminal state! basically tells the function to stop
			//the search and return each move up the call stack
			//this is either at the win-state or at the final user-defined layer if depth-bounded
		}
		if (max) {
			MoveScore returnedMove;
			MoveScore bestMove = null;
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me; //create the subnode board
				//call alphaBeta on subnode board, store score + move in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, false);
				board[currentMove.move.row][currentMove.move.col] = null;//reset the board
				//if returnedMove comes w/ higher score - reduce to ternary op??
				if (returnedMove.score > bestMove.score || bestMove == null) {
					bestMove.score = returnedMove.score;
					bestMove.move = returnedMove.move;
				}
				//if returnedMove has higher score than alpha - reduce to ternary op?
				if (bestMove.score >= alpha) {
					alpha = bestMove.score;
				}
				//pruning - still not sure about this breakoff
				if (beta <= alpha) {
					bestMove.score = alpha;
					bestMove.move = null;
					return bestMove;
				}
			}
			return bestMove;
		}
		else {
			MoveScore returnedMove;
			MoveScore bestMove = null;
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me;//create the subnode board
				//call alphabeta on subnode, storing data in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, true);
				board[currentMove.move.row][currentMove.move.col] = null;//undo the move
				//if returnedMove comes w/ lower score - reduce to ternary op??
				if (returnedMove.score < bestMove.score || bestMove == null) {
					bestMove.score = returnedMove.score;
					bestMove.move = returnedMove.move;
				}
				//if returnedMove has lower score than beta - reduce to ternary op
				if (bestMove.score <= beta) {
					beta = bestMove.score;
				}
				//pruning - still not sure about this breakoff
				if (beta <= alpha) {
					bestMove.score = beta;
					bestMove.move = null;
					return bestMove;
				}
			}
			return bestMove; //or some variant???
		}
	}//alphaBeta()	
			

	public int eval(Color[][] board, Color me, boolean max) {
		Random random = new Random();
		int randomReturn = (random.nextInt(100)-100);
		return randomReturn;

	//I THINK THIS EVAL FUNCTION IS COMPLETELY WRONG. I'M NOT EVALUATING THE UTILITY OF THE MOVE
	//I'M INSTEAD EVALUATING THE UTILITY OF THE WHOLE BOARD??
	//SHOULD CHECK FOR THE PRESENCE OF ROWS OF THE PLAYER COLOUR WHETHER MAX OR MIN
	//MORE POTENTIAL ROWS THE HIGHER THE UTILITY? FITS WITH THE THREAT THINGw	
	//square contains piece of your colour, check all around it for patterns? then add to main scoring variable
	//is there a way to check squares off? once they've been included in a pattern they're not to be checked again
	//depending on what the patternCounter reaches (ie, how long your line is) you pass back a higher score multiplier??
	}//eval()
}//class RobinPlayer160719011
