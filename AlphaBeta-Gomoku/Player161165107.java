//Rajakumaran Rajarathinam
//Student no. 161165107
//Title: Baby Skynet
//
//       _     _
//       \`\ /`/
//        \ V /               
//        /. .\       
//       =\ T /=                  
//        / ^ \     
//       /\\ //\
//     __\ " " /__           
//    (____/^\____)
//
//    Lucky Rabbit


import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Collections;
import java.util.Random;
import java.util.HashMap;
import java.lang.Comparable;
import java.util.Objects;




class Maps {
    public static final HashMap<String, Integer> pointsTable;
    static
    {
        pointsTable = new HashMap<String, Integer>();
        pointsTable.put("_MMMM_", 5000);
        pointsTable.put("_MMMM", 500);
        pointsTable.put("M_MMM", 500);
        pointsTable.put("MM_MM", 500);
        pointsTable.put("_MMM__", 400);
        pointsTable.put("_M_MM_", 400);
        pointsTable.put("MMMMM", 50000);
    }

    public static final ArrayList<String> forcedMoves;
    static {
        forcedMoves = new ArrayList<String>();
        forcedMoves.add("_MMMM");
        forcedMoves.add("_M_MM_");
        forcedMoves.add("_MMM_");
        forcedMoves.add("MMM_M");
        forcedMoves.add("MM_MM");
    }

    public static final ArrayList<Space> openings;
    static {
        openings = new ArrayList<Space>();
        openings.add(new Space(4, 4));
        openings.add(new Space(3, 4));
        openings.add(new Space(5, 4));
        openings.add(new Space(4, 3));
        openings.add(new Space(4, 5));
        openings.add(new Space(4, 6));
        openings.add(new Space(6, 4));
    }

    public static boolean checkForced(StringBuilder combo) {
        String c = combo.toString();
        for (String key : forcedMoves) {
            if (c.contains(key) || c.contains(new StringBuilder(key).reverse().toString())) {
                return true;
            }
        }
        return false;
    }

    public static int threatPoints(StringBuilder combo) {
        String c = combo.toString();
        for (String key : pointsTable.keySet()) {
            if (c.contains(key) || c.contains(new StringBuilder(key).reverse().toString())) {
                return pointsTable.get(key);
            }
        }
        return 0;
    }
}



class Threat implements Comparable<Threat> {
    public StringBuilder combo;
    public ArrayList<Space> spaces;
    public boolean forced;
    public boolean team;
    public int points;

    public Threat(StringBuilder combo, ArrayList<Space> spaces, int points, boolean team) {
        this.combo = this.clipper(combo);
        this.spaces = spaces;
        this.points = points;
        this.team = team;
        this.forced = Maps.checkForced(combo);
        if (!this.team) {
            this.points = -this.points;
        }
    }

    //Debugging
    public String toString() {
        return String.format("Threat: " + combo + ", " + points + "points");
    }

    public StringBuilder clipper(StringBuilder combo) {
        StringBuilder s = new StringBuilder("");
        int count = 0;
        for (int i = 0; i < combo.length(); i++) {
            char ch = combo.charAt(i);
            if (ch != 'M') {
                s.append(ch);
            } else {
                if (count >=5) {
                    break;
                }
                count++;
                s.append(ch);
            }
        }

        return s;
    }

    //To compare different threats
    public int compareTo(Threat t) {
        if (this.points > t.points) {
            return 1;
        } else if (this.points < t.points) {
            return -1;
        } else {
            return 0;
        }
    }

    public Space getMove() {
        int first = this.combo.indexOf("_");
        for (int i = 0; i < this.combo.length(); i++) {
            if (this.combo.charAt(i) == '_') {
                if (i < this.combo.length()-1) {
                    if (this.combo.charAt(i+1) == 'M') {
                        first = i;
                        break;
                    }
                }
                if (i > 1) {
                    if (this.combo.charAt(i-1) == 'M') {
                        first = i;
                        break;

                    }
                }
            }
        }
        return this.spaces.get(first);

    }

    public ArrayList<Space> getMoves() {
        ArrayList<Space> output = new ArrayList<Space>();
        StringBuilder s = this.combo;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_') {
                output.add(this.spaces.get(i));
            }
        }
        return output;
    }
}

class Space {
    public final int row;
    public final int col;

    public Space(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object x) {
        Space space = (Space) x;

        return this.row == space.row &&
            this.col == space.col;

    }

    public String toString() {
        return String.format("(%d, %d)", this.row, this.col);
    }
}


//Literally only exists so I can return two objects in a method
class StrSpace {
    public ArrayList<Space> s;
    public StringBuilder c;
    public StrSpace() {
        this.s = new ArrayList<Space>();
        this.c = new StringBuilder();
    }
}


class State {
    public ArrayList<Space> checked;
    public ArrayList<Threat> threats;
    public Color[][] board;
    public Threat force;
    public Color mycolor;
    public int turn;
    public boolean end;
    public int score;

    public State(Color[][] board, Color me) {
        this.checked = new ArrayList<Space>();
        this.threats = new ArrayList<Threat>();
        this.force = null;
        this.board = board;
        this.mycolor = me;
        this.score = 0;
        this.end = false;
    }

    private boolean isValid(int row, int col, Color me) {
        if(row < 0 || col < 0 || row >= GomokuBoard.ROWS || col >= GomokuBoard.COLS) {
            return false;
        }
        if (this.board[row][col] == me || this.board[row][col] == null) {
            return true;
        } else {
            return false;
        }
    }

    private StrSpace tillBroken(int row, int col, int direction, StrSpace combo, Color me) {
        if (isValid(row, col, me)) {
            Space s = new Space(row, col);
            combo.s.add(s);
            if (this.board[row][col] == me) {
                this.checked.add(s);
                combo.c.append("M");
            } else {
                combo.c.append("_");
                if (combo.c.length() >= 5) {
                    return combo;
                }
            }

            switch(direction) {
                case 0: row++;
                        break;
                case 1: col++;
                        break;
                case 2: row++;
                        col--;
                        break;
                case 3: row++;
                        col++;
                        break;
                case 4: row--;
                        break;
                case 5: col--;
                        break;
                case 6: row--;
                        col++;
                        break;
                case 7: row--;
                        col--;
                        break;
                default:
                        break;
            }
            return tillBroken(row, col, direction, combo, me);
        } else {
            return combo;
        }

    }

    private void checkThreats(Space s, Color me) {
        StrSpace S = tillBroken(s.row, s.col, 0, new StrSpace(), me);
        StrSpace E = tillBroken(s.row, s.col, 1, new StrSpace(), me);
        StrSpace SW = tillBroken(s.row, s.col, 2, new StrSpace(), me);
        StrSpace SE = tillBroken(s.row, s.col, 3, new StrSpace(), me);
        StrSpace N = tillBroken(s.row-1, s.col, 4, new StrSpace(), me);
        StrSpace W = tillBroken(s.row, s.col-1, 5, new StrSpace(), me);
        StrSpace NE = tillBroken(s.row-1, s.col+1, 6, new StrSpace(), me);
        StrSpace NW = tillBroken(s.row-1, s.col-1, 7, new StrSpace(), me);
        ArrayList<Space> H= new ArrayList<Space>();
        ArrayList<Space> V= new ArrayList<Space>();
        ArrayList<Space> DR= new ArrayList<Space>();
        ArrayList<Space> DL= new ArrayList<Space>();
        Collections.reverse(W.s);
        Collections.reverse(N.s);
        Collections.reverse(NE.s);
        Collections.reverse(NW.s);
        H.addAll(W.s);
        H.addAll(E.s);
        V.addAll(N.s);
        V.addAll(S.s);
        DR.addAll(NE.s);
        DR.addAll(SW.s);
        DL.addAll(NW.s);
        DL.addAll(SE.s);



        addThreat(W.c.reverse().append(E.c), H, me);
        addThreat(N.c.reverse().append(S.c), V, me);
        addThreat(NE.c.reverse().append(SW.c), DR, me);
        addThreat(NW.c.reverse().append(SE.c), DL, me);

    }

    private void addThreat(StringBuilder combo, ArrayList<Space> spaces, Color me) {

        //If it is impossible to make a 5, throw it out
        if (combo.length() < 5) {
            return;
        }
        // For min and max
        int points = 0;
        //Number of rest squares
        int rest = 0;
        for (int i = 0; i < combo.length(); i++) {
            if (combo.charAt(i) == 'M') {
                rest++;
            }
        }
        points += rest*rest;
        points += Maps.threatPoints(combo);

        if (me == this.mycolor) {
            this.score += points;
        } else {
            this.score -= points;
        }

        Threat t = new Threat(combo, spaces, points, this.mycolor == me);
        if (combo.toString().contains("MMMMM")) {
            this.end = true;
        }
        if (t.forced) {
            this.force = t;
        }
        this.threats.add(t);
    }

    public void scanBoard() {
        for (int x = 0; x < GomokuBoard.ROWS; x++) {
            for (int y = 0; y < GomokuBoard.COLS; y++) {
                if (this.board[x][y] != null) {
                    Space s = new Space(x, y);
                    if (!this.checked.contains(s)) {
                        checkThreats(s, this.board[x][y]);
                    }
                }
            }
        }

    }



}
public class Player161165107 extends GomokuPlayer{
    private Color mycolor;
    private Color ocolor;


    private void setBoard(Color me) {
        if (me == Color.BLACK) {
            this.mycolor = Color.BLACK;
            this.ocolor = Color.WHITE;
        } else {
            this.mycolor = Color.WHITE;
            this.ocolor = Color.BLACK;
        }
    }

    private Space openingMove(State state) {
        int i = 0;
        while (true) {
            Space space = Maps.openings.get(i);
            if (state.board[space.row][space.col] == null) {
                return space;
            }
            i++;
        }
    }

    private State genState(State state, Space space, Color me) {
        Color[][] nuboard = new Color[state.board.length][];
        for (int i = 0; i < state.board.length; i++) {
            nuboard[i] = new Color[state.board[i].length];
            for (int j = 0; j < state.board[i].length; j++) {
                nuboard[i][j] = state.board[i][j];
            }
        }
        nuboard[space.row][space.col] = me;
        State nustate = new State(nuboard, this.mycolor);
        nustate.scanBoard();
        //I had an issue where the bot would forego a winning 5
        //in order to get two winning 5 that share one square, essentially
        //doubling it's score. This cap prevents that
        if (state.score > 50000) {
            state.score = 50000;
        } else if (state.score < -50000) {
            state.score = -50000;
        }
        nustate.turn++;
        return nustate;

    }

    private int maxibon(State state, int alpha, int beta, int depth) {
        if (state.end || depth == 0) {
            return state.score;
        }
        int king = alpha;;
        if (state.force != null) {
            Space s = state.force.getMove();
            State nustate = genState(state, s, this.mycolor);
            int a = minibon(nustate, king, beta, depth);
            if (a > king) {
                king = a;
            }
        } else {
            if (state.threats.isEmpty()) {
outerloop:
                for (int row = 0; row < GomokuBoard.ROWS; row++) {
                    for (int col = 0; col < GomokuBoard.COLS; col++) {
                        if (state.board[row][col] == null) {
                            Space s = new Space(row, col);
                            State nustate = genState(state, s, this.mycolor);
                            int a = minibon(nustate, king, beta, depth-1);
                            if (a > king) {
                                king = a;
                            }
                            if (beta <= king) {
                                break outerloop;
                            }
                        }
                    }
                }
            }
outerloop:
            for (Threat t : state.threats) {
                Space s = t.getMove();
                State nustate = genState(state, s, this.mycolor);
                int a = minibon(nustate, king, beta, depth-1);
                if (a > king) {
                    king = a;
                }
                if (beta <= king) {
                    break outerloop;
                }
            }
        }




        return king;
    }


    private int minibon(State state, int alpha, int beta, int depth) {
        if (state.end || depth == 0) {
            return state.score;
        }
        int king = beta;
        if (state.force != null) {
            Space s = state.force.getMove();
            State nustate = genState(state, s, this.mycolor);
            int a = maxibon(nustate, alpha, king, depth); //Don't decrement depth in forced moves - No branching
            king = a;
        } else {

            if (state.threats.isEmpty()) {
outerloop:
                for (int row = 0; row < GomokuBoard.ROWS; row++) {
                    for (int col = 0; col < GomokuBoard.COLS; col++) {
                        if (state.board[row][col] == null) {
                            Space s = new Space(row, col);
                            State nustate = genState(state, s, this.ocolor);
                            int a = maxibon(nustate, alpha, king, depth-1);
                            if (a < king) {
                                king = a;
                            }
                            if (king <= alpha) {
                                break outerloop;
                            }
                        }
                    }
                }
            }
outerloop:
            for (Threat t : state.threats) {
                Space s = t.getMove();
                State nustate = genState(state, s, this.ocolor);
                int a = maxibon(nustate, alpha, king, depth-1);
                if (a < king) {
                    king = a;
                }
                if (king <= alpha) {
                    break outerloop;
                }
            }
        }


        return king;
    }


    private Space runSims(State state) {
        int depth = 4;
        if (state.turn < 3) {
            return openingMove(state);
        }

        int king = Integer.MIN_VALUE;
        Space queen = new Space(0, 0);

        if (state.force != null) {
            Space s = state.force.getMove();
            State nustate = genState(state, s, this.mycolor);
            int a = minibon(nustate, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
            king = a;
            queen = s;
        } else {
            if (state.threats.isEmpty()) {
                for (int row = 0; row < GomokuBoard.ROWS; row++) {
                    for (int col = 0; col < GomokuBoard.COLS; col++) {
                        if (state.board[row][col] == null) {
                            Space s = new Space(row, col);
                            State nustate = genState(state, s, this.mycolor);
                            int a = minibon(nustate, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
                            if (a > king) {
                                king = a;
                                queen = s;
                            }
                        }
                    }
                }
            }
            for (Threat t : state.threats) {
                Space s = t.getMove();
                State nustate = genState(state, s, this.mycolor);
                int a = minibon(nustate, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
                if (a > king) {
                    king = a;
                    queen = s;
                }
            }
        }


        return queen;
    }

    public int checkTurn(Color[][] board) {
        int turn = 0;
        for (int row = 0; row < GomokuBoard.ROWS; row++) {
            for (int col = 0; col < GomokuBoard.COLS; col++) {
                if (board[row][col] != null) {
                    turn++;
                }
            }
        }
        return turn;
    }

    public Move chooseMove(Color[][] board, Color me) {
        setBoard(me);
        State state = new State(board, this.mycolor);
        state.scanBoard();
        state.turn = checkTurn(board);
        Space move = runSims(state);
        return new Move(move.row, move.col);
    }

    public static void main (String[] args) {
        // GomokuReferee took too long
        Random rn = new Random();
        Color[][] c = new Color[8][8];
        int[] b = {
            0, 0, 1, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 2, 2, 2, 2, 1, // 1
            0, 1, 2, 0, 2, 0, 0, 0, // 2
            0, 0, 2, 1, 1, 2, 0, 0, // 3
            0, 0, 0, 1, 0, 0, 0, 0, // 4
            0, 0, 1, 0, 0, 0, 0, 0, // 5
            0, 0, 0, 2, 0, 0, 0, 0, // 6
            0, 0, 0, 0, 0, 0, 0, 0  // 7
        //  0  1  2  3  4  5  6  7
        };

        int start = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                switch (b[start]) {
                    case 0: c[x][y] = null;
                            break;
                    case 1: c[x][y] = Color.BLACK;
                            break;
                    case 2: c[x][y] = Color.WHITE;
                            break;
                    default: c[x][y] = null;
                             break;
                }
                start ++;
            }
        }
        Player161165107 p = new Player161165107();
        System.out.println("Start");
        System.out.println(p.chooseMove(c, Color.BLACK));
    }
}
