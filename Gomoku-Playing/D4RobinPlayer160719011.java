/**
 * @author Robin Farrow-Yonge
 * Student Number: 160719011
 */
import java.awt.Color;
import java.util.*;
import java.util.ArrayList;

class D4RobinPlayer160719011 extends GomokuPlayer {

	//the horribly-named MoveScore object holds a move, and the score returned by eval();
	private class MoveScore {
		public Move move;
		public int score;
 
		private MoveScore(Move a, int b) {
			move = a;
			score = b;
		}
	}

	public Move chooseMove(Color[][] board, Color me) {
		MoveScore myMove;
		//white is always max, black is always min
		if (me == Color.white) {
			myMove = alphaBeta(board, me, 4, -2000, 2000, true);
		} else {
			myMove = alphaBeta(board, me, 4, -2000, 2000, false);
		}
		return myMove.move;
	}

	public ArrayList<MoveScore> prepareMoves(Color[][] board) {
		//iterates through every index of the board, if there's no piece there it adds 
		//it as a potential move to the "moves" ArrayList
		ArrayList<MoveScore> moves = new ArrayList<MoveScore>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == null) {
					moves.add(new MoveScore(new Move(row, col), 0));
				}
			}
		}
		return moves;
	}

	
	public MoveScore alphaBeta(Color[][] board, Color me, int depth, int alpha, int beta, boolean max) {
		if (depth <= 0) {//would also be good to return if I hit a win condition, need to test for that 
			MoveScore terminalState = new MoveScore(new Move(4, 4), 0); 
			terminalState.score = eval(board, me);
			return terminalState;
		}
		ArrayList<MoveScore> moves = prepareMoves(board);//prepare the moves
		if (max == true) {
			//System.out.println("it's max's turn");
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(4, 4), -2000);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = Color.white; //create the subnode board
				//call alphaBeta on subnode board, store score + move in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, false);
				board[currentMove.move.row][currentMove.move.col] = null;//reset the board	
				if (returnedMove.score > bestMove.score) {
					bestMove.score = returnedMove.score;
					bestMove.move = currentMove.move;
				}
				//if returnedMove has higher score than alpha
				alpha = (bestMove.score >= alpha) ? bestMove.score : alpha;
				//the pruning part
				if (beta <= alpha) {
					bestMove.score = beta;
					bestMove.move = null;
					return bestMove;
				}
			}
			return bestMove;
		} else {
			//System.out.println("It's min's turn!");
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(4, 4), 2000);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = Color.black;//create the subnode board
				//call alphabeta on subnode, storing data in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, true);
				board[currentMove.move.row][currentMove.move.col] = null;	
				if (returnedMove.score < bestMove.score) {
					bestMove.score = returnedMove.score;
					bestMove.move = currentMove.move;
				}
				//if returnedMove has lower score than beta
				beta = (bestMove.score <= beta) ? bestMove.score : beta;
				//the pruning part
				if (beta <= alpha) {
					bestMove.score = alpha;
					bestMove.move = null;
					return bestMove;
				}
			}
			return bestMove;
		}
	}
			

	public int eval(Color[][] board, Color me) {		
		//takes the board, send the index of each non-null index to scorePatterns by way of getPatterns
		int boardScore = 0;
		int squareScore = 0;
		for (int i = 0; i < 8; i++) {//rows
			for (int j = 0; j < 8; j++) {//cols
				if (board[i][j] == Color.white) {
					squareScore = scorePatterns(getPatterns(board, Color.white, i, j));
					boardScore += (Color.white == me) ? squareScore : squareScore*1;//opponent score multiplier
				} else if (board[i][j] == Color.black) {
					squareScore = scorePatterns(getPatterns(board, Color.black, i, j));
					boardScore -= (Color.black == me) ? squareScore : squareScore*1;//encourages more or less defensive play
				}
			}
		}
		return boardScore;//return the total score
	}
	public int scorePatterns(ArrayList<Integer> patterns) {
		//takes list of patterns found from a particular square and multiplies them
		//so that longer patterns appear more valuable than multiple shorter patterns
		int score = 0;
			for (Integer pattern : patterns) {
				score += (pattern * pattern * pattern);
			}
		return score;
	}
	public ArrayList<Integer> getPatterns(Color[][] board, Color me, int row, int col) {
		//not a particularly elegant function (though the patterns are quite pretty in solarized)
		//takes a board position, increments row and/or col numbers checking for the same pieces
		//if found, it increments a counter and adds that counter to the patterns list at the end 
		ArrayList<Integer> patterns = new ArrayList<Integer>();
		int pattern = 0;
		int counter = 0;
		int tmpRow = row;
		int tmpCol = col;
		for (int i = 1; i < 9; i++) {
			pattern = i;
			counter = 0;
			tmpRow = row;
			tmpCol = col;
			switch(pattern) {
				case 1:		
					for (int j = 1; j < 6; j++) {
						if (tmpRow > 0 && board[tmpRow-1][tmpCol] == me) {
							tmpRow--;
							counter++;
						} else {
							patterns.add(counter);
							break;
						}
					}
					break;
				case 2:
					for (int k = 1; k < 6; k++) {
						if (tmpRow > 0 && tmpCol < 7 && board[tmpRow-1][tmpCol+1] == me) {
							tmpRow--;
							tmpCol++;
							counter++;
						} else { 
							patterns.add(counter);
							break;
						}
					}
					break;
				case 3:
					for (int l = 1; l < 6; l++) {
						if (tmpCol < 7 && board[tmpRow][tmpCol+1] == me) {
							tmpCol++;
							counter++;
						} else {
							patterns.add(counter);
							break;
						}
					}
					break;
				case 4:
					for (int m = 1; m < 6; m++) {
						if (tmpCol < 7 && tmpRow < 7 && board[tmpRow+1][tmpCol+1] == me) {
							tmpCol++;
							tmpRow++;
							counter++;
						} else {
							patterns.add(counter);
							break;
						}
					}
					break;
				case 5:					
					for (int n = 1; n < 6; n++) {
						if (tmpRow < 7 && board[tmpRow+1][tmpCol] == me) {
							tmpRow++;	
							counter++;
						} else {
							patterns.add(counter);
							break;
						}
					}
					break;
				case 6:
					for (int k = 1; k < 6; k++) {
						if (tmpRow < 7 && tmpCol > 0 && board[tmpRow+1][tmpCol-1] == me) {
							tmpRow++;
							tmpCol--;
							counter++;
						} else { 
							patterns.add(counter);
							break;
						}
					}
					break;
				case 7:
					for (int l = 1; l < 6; l++) {
						if (tmpCol > 0 && board[tmpRow][tmpCol-1] == me) {
							tmpCol--;
							counter++;
						} else {
							patterns.add(counter);
							break;
						}
					}
					break;
				case 8:
					for (int m = 1; m < 6; m++) {
						if (tmpCol > 0 && tmpRow > 0 && board[tmpRow-1][tmpCol-1] == me) {
							tmpCol--;
							tmpRow--;
							counter++;
						} else {
							patterns.add(counter);
							break;
						}
					}
					break;
				default:
					break;
			}
				
				
		}	
		return patterns;
	}
}
