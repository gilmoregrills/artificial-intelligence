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
		//so if I'm white (white goes first) I am max, if not I am min
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
					moves.add(new MoveScore(new Move(row, col), 0)); //maybe null	
				}
			}
		}
		return moves;
	}//prepareMoves

	
	public MoveScore alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		if (depth <= 0) {//also needs to be if the board has hit a win condition!!!
			MoveScore terminalState = new MoveScore(new Move(4, 4), 0); //returns move from bottom of stack, why?
			terminalState.score = eval(board, me, max);
			return terminalState;

			//return the score for the terminal state! basically tells the function to stop
			//the search and return each move up the call stack
			//this is either at the win-state or at the final user-defined layer if depth-bounded
		}
		else if (max == true) {
			//21:23 20th Feb - it always tries to go in 4,4 - work out which one is the root problem
			ArrayList<MoveScore> moves = prepareMoves(board);//calls prepareMoves to get legal moves
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(0, 0), -200);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me; //create the subnode board
				//call alphaBeta on subnode board, store score + move in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, false);
				board[currentMove.move.row][currentMove.move.col] = null;//reset the board -- 21:37 might be a problem with the move I return with terminalState
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
			ArrayList<MoveScore> moves = prepareMoves(board);//calls prepareMoves to get legal moves
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(0, 0), 200);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me;//create the subnode board
				//call alphabeta on subnode, storing data in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, true);
				board[currentMove.move.row][currentMove.move.col] = null;//undo the move -- 21:39 I think both of these should be board[returnedMove.move.row] etc
				//if returnedMove comes w/ lower score - reduce to ternary op??
				if (returnedMove.score < bestMove.score || bestMove == null) { //might be something wrong with this evaluation, potentially always sets bestMove to returnedMove
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
				}
			}
			return bestMove; //or some variant???
		}
	}//alphaBeta()	
			

	public int eval(Color[][] board, Color me, boolean max) {
		//placeholder function, assigns random score +50 to -50		
		Random random = new Random();
		int randomReturn = (random.nextInt(100)-100);
		return randomReturn;

	//square contains piece of your colour, check all around it for patterns? then add to main scoring variable
	//is there a way to check squares off? once they've been included in a pattern they're not to be checked again
	//depending on what the patternCounter reaches (ie, how long your line is) you pass back a higher score multiplier??
	}//eval()
}//class RobinPlayer160719011
