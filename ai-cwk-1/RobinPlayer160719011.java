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
			myMove = alphaBeta(board, me, 3, -200, 200, true);
		} else {
			myMove = alphaBeta(board, me, 3, -200, 200, false);
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
			System.out.println("it's max's turn");
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
				if (bestMove.score >= alpha) {
					alpha = bestMove.score;
					//assign bestMove returnedmove??
				}
				//pruning - still not sure about this breakoff
				if (beta <= alpha) {
					bestMove.score = beta;
					//bestMove.move = null;
					return bestMove;
				}
			}
			return bestMove;
		}
		else {
			System.out.println("It's min's turn!");
			ArrayList<MoveScore> moves = prepareMoves(board);
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(4, 4), 200);
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
				if (bestMove.score <= beta) {
					beta = bestMove.score;
					//assign bestMove returnedMove? 
				}
				//pruning - still not sure about this breakoff
				if (beta <= alpha) {
					bestMove.score = alpha;
					//bestMove.move = null; 
					return bestMove;
				}
			}
			return bestMove; //or some variant???
		}
	}//alphaBeta()	
			

	public int eval(Color[][] board, Color me, boolean max) {		
		//22:02 bugfixing - for loops are functional
	        // to start with I'm going to check each occupied square for patterns up, down, upright, downright, and right from it
		int totalScore = 0;
		int squareCounter = 0;
		for (int i = 0; i < 8; i++) {//rows
			for (int j = 0; j < 8; j++) {//cols
				if (board[i][j] == Color.white) {
					System.out.println("square: "+i+","+j+"  is white");
					totalScore += search(board, Color.white, i, j);
				} else if (board[i][j] == Color.black) {
					System.out.println("square: "+i+","+j+" is black!");
					totalScore -= search(board, Color.black, i, j);
				} else if (board[i][j] == null) {
					System.out.println("square is null");
				}
		
				/*This is my debug code, essentially it's random, 
				 * but it tests pruning etc too.
				 *
				Random random = new Random();
				int randomScore = (random.nextInt(10) - 10);
				if (board[i][j] == Color.black) {
					totalScore = randomScore;
				} else if (board[i][j] == Color.white) {
					totalScore = randomScore;
				}
				*/
			}
		}
		System.out.println("Total score for this board is: "+totalScore);
		return totalScore;
	}//eval()
	public int search(Color[][] board, Color me, int row, int col) {
		 
			int total = 0;
			int pattern = 0;
			int counter = 0;
			int tmpRow = row;
			int tmpCol = col;
			for (int i = 0; i < 5; i = pattern) {
				System.out.println("this is loop: "+i+" we're searching: "+tmpRow+","+tmpCol+" for pattern: "+pattern+" counter: "+counter);

				if (board[tmpRow][tmpCol] == me) {
					counter++;
					System.out.println("color found: "+board[tmpRow][tmpCol]);
				} else {
					pattern++;
					total += counter * counter;
					counter = 0;	
					System.out.println("color found: "+board[tmpRow][tmpCol]);
					tmpRow = row;
					tmpCol = col;
				}
				switch(pattern) {
					case 0:
						if (tmpRow > 0) {
							tmpRow--;
							System.out.println("we can search upwards! tmpRow is now: "+tmpRow);
						} else {
							pattern++;
							total += counter * counter;
							counter = 0;	
							System.out.println("no searching upwards, now we check pattern "+pattern);
							tmpRow = row;
						}
						break;
					case 1:
						if (tmpRow > 0 && tmpCol < 7) {
							tmpRow--;
							tmpCol++;
							System.out.println("we can search up/right! tmpRow is now: "+tmpRow+" tmpCol is now"+tmpCol);
						} else {
							pattern++;
							total += counter * counter;
							counter = 0;	
							tmpRow = row;
							tmpCol = col;
							System.out.println("no searching up/right, now we check pattern "+pattern);
						}
						break;
					case 2:
						if (tmpCol < 7) {
							tmpCol++;
							System.out.println("we can search right! tmpCol is now: "+tmpCol);
						} else {
							pattern++;
							total += counter * counter;
							counter = 0;	
							tmpCol = col;
							System.out.println("no searching right, now we check pattern"+pattern);
						}
						break;
					case 3:
						if (tmpRow < 7 && tmpCol < 7) {
							tmpCol++;
							tmpRow++;
							System.out.println("we can search down/right! tmpRow is :"+tmpRow+" tmpCol is :"+tmpCol);
						} else {
							pattern++;
							total += counter * counter;
							counter = 0;	
							tmpCol = col;
							tmpRow = row;
							System.out.println("no searching down/right, now we check pattern "+pattern);
						}
						break;
					case 4:
						if (tmpRow < 7) {
							tmpRow++;
							System.out.println("we can search down! tmpRow is: "+tmpRow+" tmpCol is: "+tmpCol);
						} else {
							pattern++;
							total += counter * counter;
							counter = 0;	
							tmpRow = row;
							System.out.println("no searching down, and this was the last pattern");
						}
						break;
					default:
						return total;
				}
				
				
			}
		System.out.println("we've made it to the return of search()");		
		return total;
	}//search()
}//class RobinPlayer160719011
