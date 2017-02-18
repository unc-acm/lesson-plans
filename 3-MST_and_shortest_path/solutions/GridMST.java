import java.util.*;
import java.io.*;

public class GridMST {
    // Instance Variables
    public static final int GRID_SIZE = 1000;
    public static final int UNVISITED = -1;

    static class BoardMarker {
        int origin; // id of the point originated this makrer
        int dist; // manhattan dist to home

        public BoardMarker(int _o, int _d) {
            origin = _o;
            dist = _d;
        }
    }

    static class QueueMemeber {
        int x, y;
        BoardMarker content;

        public QueueMemeber(int _x, int _y, int _o, int _d) {
            x = _x;
            y = _y;
            content = new BoardMarker(_o, _d);
        }
    }

    public static int generateMST(BoardMarker[][] board,
                                  ArrayList<Queue<QueueMemeber>> bfsQueues,
                                  HashMap<Integer, HashSet<Integer>> forestLookup,
                                  int N, int max_x, int max_y) {
        int totalWeight = 0;

        int current_step_size = 0;

        while (true) {
            // haha
            boolean skippedAll = true;
            
            for (int i = 0 ; i < N ; i++) {
                if (bfsQueues.get(i).isEmpty()) {
                    continue;
                }

                QueueMemeber current = bfsQueues.get(i).peek();

                if (current.content.dist > current_step_size) {
                    continue;
                }

                skippedAll = false;

                bfsQueues.get(i).poll();

                int x = current.x;
                int y = current.y;
                int d = current.content.dist;
 
                if (board[x][y].origin == i) {
                    continue;
                }
                else if (board[x][y].origin == UNVISITED) { // unvisited
                    board[x][y] = new BoardMarker(i, d);

                    if (x > 0) {
                        bfsQueues.get(i).offer(new QueueMemeber(x - 1, y, i, d + 1));
                    }
                    
                    if (x < max_x - 1) {
                        bfsQueues.get(i).offer(new QueueMemeber(x + 1, y, i, d + 1));
                    }

                    if (y > 0) {
                        bfsQueues.get(i).offer(new QueueMemeber(x, y - 1, i, d + 1));
                    }

                    if (y < max_y - 1) {
                        bfsQueues.get(i).offer(new QueueMemeber(x, y + 1, i, d + 1));
                    }
                }
                else { // already here!
                    if (forestLookup.get(i).contains(board[x][y].origin)) {
                        continue;
                    }

                    totalWeight += board[x][y].dist + d;

                    int otherID = board[x][y].origin;

                    System.out.printf("%d - %d at (%d, %d): %d + %d\n", i, otherID, x, y, board[x][y].dist,  d);

                    HashSet<Integer> combined = new HashSet<Integer>(forestLookup.get(otherID));
                    combined.addAll(forestLookup.get(i));

                    if (combined.size() == N) {
                        // tree is spanning!
                        return totalWeight;
                    }

                    Iterator<Integer> it = combined.iterator();

                    while (it.hasNext()) {
                        forestLookup.put(it.next(), combined);
                    }
                }
            }

            if (skippedAll) {
                current_step_size++;
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        int max_x = 0, max_y = 0;
        int[][] points = new int[N][2];

        HashMap<Integer, HashSet<Integer>> forestLookup = new HashMap<Integer, HashSet<Integer>>();
        ArrayList<Queue<QueueMemeber>> bfsQueues = new ArrayList<Queue<QueueMemeber>>(N);

        for (int i = 0 ; i < N ; i++) {
            bfsQueues.add(new LinkedList<QueueMemeber>());
        }

        for (int i = 0 ; i < N ; i++) {
            int x = s.nextInt();
            int y = s.nextInt();

            max_x = (x > max_x) ? x : max_x;
            max_y = (y > max_y) ? y : max_y;

            HashSet<Integer> tmp = new HashSet<Integer>();
            tmp.add(i);

            forestLookup.put(i, tmp);

            bfsQueues.get(i).offer(new QueueMemeber(x, y, i, 0));
        }

        max_x++;
        max_y++;

        BoardMarker[][] board = new BoardMarker[max_x][max_y];

        for (int i = 0 ; i < max_x ; i++) {
            for (int j = 0 ; j < max_y ; j++) {
                board[i][j] = new BoardMarker(UNVISITED, UNVISITED);
            }
        }

        System.out.println(generateMST(board, bfsQueues, forestLookup, N, max_x, max_y));
    }
}
