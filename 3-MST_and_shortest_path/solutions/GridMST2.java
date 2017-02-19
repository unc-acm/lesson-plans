import java.util.*;
import java.io.*;

public class GridMST2 {

    static class QueueMember {
        int x, y, id;

        public QueueMember(int _x, int _y, int _id) {
            x = _x;
            y = _y;
            id = _id;
        }
    }

    public static int generateMST(int[][] board,
            Queue<QueueMember> bfsQueue,
            ArrayList<HashSet<Integer>> forestLookup,
            int[][] points,
            int N, int max_x, int max_y) {
        
        int totalWeight = 0;

        while (!bfsQueue.isEmpty()) {
            QueueMember current = bfsQueue.poll();

            int x = current.x; int y = current.y;
            int id = current.id;

            if (board[x][y] == id) {
                continue;
            }

            if (board[x][y] == -1) {
                board[x][y] = id;

                if (x > 0) {
                    bfsQueue.offer(new QueueMember(x - 1, y, id));
                }

                if (x < max_x - 1) {
                    bfsQueue.offer(new QueueMember(x + 1, y, id));
                }

                if (y > 0) {
                    bfsQueue.offer(new QueueMember(x, y - 1, id));
                }

                if (y < max_y - 1) {
                    bfsQueue.offer(new QueueMember(x, y + 1, id));
                }
            }

            else {
                if (forestLookup.get(id).contains(board[x][y])) {
                    continue;
                }

                int otherID = board[x][y];

                totalWeight += Math.abs(points[id][0] - points[otherID][0]);
                totalWeight += Math.abs(points[id][1] - points[otherID][1]);

                // System.out.printf("%d - %d at (%d, %d): %d + %d\n", i, otherID, x, y, board[x][y].dist,  d);

                HashSet<Integer> combined = new HashSet<Integer>(forestLookup.get(otherID));
                combined.addAll(forestLookup.get(id));

                if (combined.size() == N) {
                    // tree is spanning!
                    return totalWeight;
                }

                Iterator<Integer> it = combined.iterator();

                while (it.hasNext()) {
                    forestLookup.set(it.next(), combined);
                }
            }
        }

        return totalWeight;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        int max_x = 0, max_y = 0;
        int[][] points = new int[N][2];

        ArrayList<HashSet<Integer>> forestLookup = new ArrayList<HashSet<Integer>>();
        //HashMap<Integer, HashSet<Integer>> forestLookup = new HashMap<Integer, HashSet<Integer>>();
        Queue<QueueMember> bfsQueue = new LinkedList<QueueMember>();

        for (int i = 0 ; i < N ; i++) {
            int x = s.nextInt();
            int y = s.nextInt();

            points[i][0] = x; points[i][1] = y;

            max_x = (x > max_x) ? x : max_x;
            max_y = (y > max_y) ? y : max_y;

            HashSet<Integer> tmp = new HashSet<Integer>();
            tmp.add(i);

            forestLookup.add(tmp);

            bfsQueue.offer(new QueueMember(x, y, i));
        }

        max_x++;
        max_y++;

        int[][] board = new int[max_x][max_y];

        for (int i = 0 ; i < max_x ; i++) {
            for (int j = 0 ; j < max_y ; j++) {
                board[i][j] = -1;
            }
        }

        System.out.println(generateMST(board, bfsQueue, forestLookup, points, N, max_x, max_y));   
    }
}
