import java.awt.Color;
import java.util.*;
import java.util.Arrays;

class RobinPlayer160719011 extends GomokuPlayer {

	public Move chooseMove(Color[][] board, Color me) {
	//first arg is boardstate
	//second arg is color I am playing
		boolean max = true;
			
	}//chooseMove()

	public ArrayList prepareMoves(Color[][] board) {
		ArrayList<Move> moves = new ArrayList<Move>(); //needs to be arraylist!!
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				if (board[row][col] == null) {
					board[row][col] = new Move(row, col);
					moves.add(//the Move object
				}
			}
		}
		return moves;
	}

	
	public Move alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		ArrayList<Move> moves = prepareMoves(board);
		if depth = 0 || board == terminal??) {
			//return the score for the terminal state! basically stop the search and return
			//every move up the call stack!!!
			//this is either at the win-state or at the final user-defined layer if depth-bounded
		}
		if (max) {
			score = 0;
			for (legalMove in moves) {
				//apply move to newBoard
				score = Math.max(score, alphaBeta(newBoard, me, depth-1, alpha, beta, false));
				alpha = Math.max(alpha, score);
				if (beta <= alpha) {
					//break/cut off in some way
				}
			}
			return score; //or some variant???	
		}
		else {
			score = 0;
			for (legalMove in moves) {
				//apply move to newBoard
				score = Math.max(score, alphaBeta(newBoard, depth-1, alpha, beta, true));
				beta = Math.min(beta, score);
				if (beta <= alpha) {
					//break or cut off in some way
				}
			}
			return score; //or some variant???
		}
	}//alphaBeta()	
			

	public int[][] eval(Color[][] board, Color me, Color them) {

		
		//I THINK THIS EVAL FUNCTION IS COMPLETELY WRONG. I'M NOT EVALUATING THE UTILITY OF THE MOVE
		//I'M INSTEAD EVALUATING THE UTILITY OF THE WHOLE BOARD??
		//SHOULD CHECK FOR THE PRESENCE OF ROWS OF THE PLAYER COLOUR WHETHER MAX OR MIN
		//MORE POTENTIAL ROWS THE HIGHER THE UTILITY? FITS WITH THE THREAT THINGw
		
		if (board[col][row] == null) {
			int patternCounter = 0;
			int tmpRow = row;
			int tmpCol = col;
			//check for patterns iterating to the right, add to moves array if > current val
			//this should be a function tbh!!!
			do {
				tmpRow++;
				if (board[tmpCol][tmpRow] == me) {
					patternCounter++;
				} else if (board[tmpCol][tmpRow] == them) {
					patternCounter--;
				}
			} while (board[tmpCol][tmpRow] == me || board[tmpCol][tmpRow] == them);
			potentialMove[0] = Math.max(patternCounter, moves[col][row]);
			//check for patterns iterating up, add to moves array if > current val
			patternCounter = 0;
			tmpRow = row;
			tmpCol = col;
			do {
				tmpCol++;
				if (board[tmpCol][tmpRow] == me) {
					patternCounter++;
				} else if (board[tmpCol][tmpRow] == them) {
					patternCounter--;
				}
			} while (board[tmpCol][tmpRow] == me || board[tmpCol][tmpRow] == them);
			potentialMove[0] = Math.max(patternCounter, moves[col][row]);
			//check for patterns iterating up and to the right
			patternCounter = 0;
			tmpRow = row;
			tmpCol = col;
			do {
				tmpRow++;
				tmpCol++;
				if (board[tmpCol][tmpRow] == me) {
					patternCounter++;
				} else if (board[tmpCol][tmpRow] == them) {
					patternCounter--;
				}
			} while (board[tmpCol][tmpRow] == me || board[tmpCol][tmpRow] == them);
			potentialMove[0] = Math.max(patternCounter, moves[col][row]);
			//check for patterns iterating down and to the right
			patternCounter = 0;
			tmpRow = row;
			tmpCol = col;
			do {
				tmpRow--;
				tmpCol--;
				if (board[tmpCol][tmpRow] == me) {
					patternCounter++;
				} else if (board[tmpCol][tmpRow] == them) {
					patternCounter--;
				}
			} while (board[tmpCol][tmpRow] == me || board[tmpCol][tmpRow] == them);
			potentialMove[0] = Math.max(patternCounter, moves[col][row]);
			//check for patterns down
			patternCounter = 0;
			tmpRow = row;
			tmpCol = col;
			do {
				tmpCol--;
				if (board[tmpCol][tmpRow] == me) {
					patternCounter++;
				} else if (board[tmpCol][tmpRow] == them) {
					patternCounter--;
				}
			} while (board[tmpCol][tmpRow] == me || board[tmpCol][tmpRow] == them);
			potentialMove[0] = Math.max(patternCounter, moves[col][row]);
		} else if (board[col][row] != null) {
			potentialMove[0] = 0;
		}
		return potentialMove; //or return the BEST of moves??
	}//eval()
}//class RobinPlayer160719011
