//NOTE FROM PAST ROBIN:
//IT'S CURRENTLY PLAYING IN FUNCTION BECAUSE THE FIRST POSSIBLE CHILD STATE(IE, PREVIOUS MOVE +1)
//IS ALWAYS COMING BACK AS HAVING A RAD SCORE
//MIGHT BE A PROBLEM WITH MY BESTSTATE +/- 200 STUFF, MAKE NULL?

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
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == null) {
					moves.add(new MoveScore(new Move(row, col), 0)); //maybe null - 22:56	
				}
			}
		}
		return moves;
	}//prepareMoves

	
	public MoveScore alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		if (depth <= 0) {//also needs to be if the board has hit a win condition!!!
			MoveScore terminalState = new MoveScore(new Move(4, 4), 0); 
			terminalState.score = eval(board, me, max);
			return terminalState;

			//return the score for the terminal state! Either at win state or once depth has hit 0
		}
		else if (max == true) {
			ArrayList<MoveScore> moves = prepareMoves(board);
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(4, 4), -200);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me; //create the subnode board
				//call alphaBeta on subnode board, store score + move in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, false);
				board[currentMove.move.row][currentMove.move.col] = null;//reset the board
				//if returnedMove comes w/ higher score - reduce to ternary op??
				if (returnedMove.score > bestMove.score || bestMove == null) {//possibly should just be bestMove = null
					bestMove.score = returnedMove.score;
					bestMove.move = currentMove.move;
				}
				//if returnedMove has higher score than alpha - reduce to ternary op?
				if (returnedMove.score >= alpha) {
					alpha = returnedMove.score;
				}
				//pruning - still not sure about this breakoff
				if (beta <= alpha) {
					bestMove.score = beta;
					bestMove.move = null;
					return bestMove;
				}
			}
			return bestMove;
		}
		else {
			ArrayList<MoveScore> moves = prepareMoves(board);
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(0, 0), 200);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me;//create the subnode board
				//call alphabeta on subnode, storing data in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, true);
				board[currentMove.move.row][currentMove.move.col] = null;
				//if returnedMove comes w/ lower score - reduce to ternary op??
				if (returnedMove.score < bestMove.score || bestMove == null) {
					bestMove.score = returnedMove.score;
					bestMove.move = currentMove.move;
				}
				//if returnedMove has lower score than beta - reduce to ternary op
				if (returnedMove.score <= beta) {
					beta = returnedMove.score;
				}
				//pruning - still not sure about this breakoff
				if (beta <= alpha) {
					bestMove.score = alpha;
					bestMove.move = null; 
					return bestMove;
				}
			}
			return bestMove; //or some variant???
		}
	}//alphaBeta()	
			

	public int eval(Color[][] board, Color me, boolean max) {
		/*
		//placeholder function, assigns random score +50 to -50		
		Random random = new Random();
		int randomReturn = (random.nextInt(100)-100);
		return randomReturn;
		*/
		
		
		//22:02 bugfixing - for loops are functional
	        // to start with I'm going to check each occupied square for patterns up, down, upright, downright, and right from it		
		int totalScore = 0;
		int squareCounter = 0;
		for (int i = 0; i < 8; i++) {//rows
			for (int j = 0; j < 8; j++) {//cols
				/*
				int row;
				int col;
				int patternCounter = 0;
				if (board[i][j] == Color.white) {
					row = i;
					col = j;
					do {
						col++;
						patternCounter += 10;
					} while (board[row][col] == Color.white);
					totalScore += patternCounter;
				} else if (board[i][j] == Color.black) {
					row = i;
					col = j;
					do {
						col++;
						patternCounter += 10;
					}while (board[row][col] == Color.black);
					totalScore += patternCounter;
				}
				*/
				if (board[4][4] == Color.black) {
					totalScore = 40;
				} else if (board[5][5] == Color.black) {
					totalScore = 40;
				}
			}
		}
		return totalScore;
	}//eval()
}//class RobinPlayer160719011
