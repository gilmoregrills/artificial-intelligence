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
			myMove = alphaBeta(board, me, 4, -2000, 2000, true);
		} else {
			myMove = alphaBeta(board, me, 4, -2000, 2000, false);
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
			terminalState.score = eval(board, max, me);
			return terminalState;

			//return the score for the terminal state! Either at win state or once depth has hit 0
		}
		else if (max == true) {
			System.out.println("it's max's turn");
			ArrayList<MoveScore> moves = prepareMoves(board);
			MoveScore returnedMove;
			MoveScore bestMove = new MoveScore(new Move(4, 4), -2000);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me; //create the subnode board
				//call alphaBeta on subnode board, store score + move in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, false);
				board[currentMove.move.row][currentMove.move.col] = null;//reset the board
			
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
			MoveScore bestMove = new MoveScore(new Move(4, 4), 2000);
			for (MoveScore currentMove : moves) {
				board[currentMove.move.row][currentMove.move.col] = me;//create the subnode board
				//call alphabeta on subnode, storing data in returnedMove
				returnedMove = alphaBeta(board, me, depth-1, alpha, beta, true);
				board[currentMove.move.row][currentMove.move.col] = null;
			
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
			

	public int eval(Color[][] board, boolean max, Color me) {		
		
		int boardScore = 0;
		int squareScore = 0;
		for (int i = 0; i < 8; i++) {//rows
			for (int j = 0; j < 8; j++) {//cols
				if (board[i][j] == Color.white) {
					System.out.println("square: "+i+","+j+"  is white");
					squareScore = scorePatterns(getPatterns(board, Color.white, i, j));
					boardScore += (Color.white == me) ? squareScore : squareScore*2;
				} else if (board[i][j] == Color.black) {
					System.out.println("square: "+i+","+j+" is black!");
					squareScore = scorePatterns(getPatterns(board, Color.black, i, j));
					boardScore -= (Color.black == me) ? squareScore : squareScore*2;
				} else {
					
				}
				//Whatever player *I* am, I want to give a multiplier to the opponent's
				//patterns, so that preventing them from developing patterns is > making
				//my own


				/*This is debug code, essentially it's random, 
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
		System.out.println("Total score for this board is: "+boardScore);
		return boardScore;
	}//eval()
	public int scorePatterns(ArrayList<Integer> patterns) {
		int score = 0;
			for (Integer pattern : patterns) {
				score += (pattern * pattern);
			}
		return score;
	}
	public ArrayList<Integer> getPatterns(Color[][] board, Color me, int row, int col) {
		 
			ArrayList<Integer> patterns = new ArrayList<Integer>();
			int pattern = 0;
			int counter = 0;
			int tmpRow = row;
			int tmpCol = col;
			for (int i = 1; i < 6; i++) {
				System.out.println("counter is: "+counter);
				pattern = i;
				counter = 0; //could be 0
				tmpRow = row;
				tmpCol = col;

				switch(pattern) {
					case 1:		
						System.out.println("Searching upwards");
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
						System.out.println("Searching up/right");
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
						System.out.println("Searching right");
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
						System.out.println("Searching down/right");
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
						System.out.println("Searching dooooown");
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
					default:
						break;
				}
				
				
			}
		System.out.println("we've made it to the return of search()");		
		return patterns;
	}//search()
}//class RobinPlayer160719011
