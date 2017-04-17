import java.util.*;
import java.io.*;

public class Wedding {
    // Instance Variables
    int N;
    List<List<Integer>> E;
    List<List<Integer>> invE;
    int[] assignments;
    static final int LEFT = 1;
    static final int RIGHT = 2;
    static final int UNASSIGNED = 0;

    // Constructors
    public Wedding(int n) {
        N = n;
        E = new ArrayList<List<Integer>>(4 * N);
        for (int i = 0 ; i < 4 * N ; i++) {
            E.add(new ArrayList<Integer>());
        }
        invE = new ArrayList<List<Integer>>(4 * N);
        for (int i = 0 ; i < 4 * N ; i++) {
            invE.add(new ArrayList<Integer>());
        }

        for (int i = 0 ; i < N ; i++) {
            addImplication(i, getNot(getSpouse(i)));
            addImplication(getNot(getSpouse(i)), i);
            addImplication(getSpouse(i), getNot(i));
            addImplication(getNot(i), getSpouse(i));
        }

        assignments = new int[4 * N];
    }

    int getSpouse(int person) {
        if (person >= N) {
            return person - N;
        }
        else {
            return person + N;
        }
    }

    int getNot(int node) {
        if (node >= 2 * N) {
            return node - 2 * N;
        }
        else {
            return node + 2 * N;
        }
    }

    void addImplication(int from, int to) {
        E.get(from).add(to);
        invE.get(to).add(from);
    }

    void dfs(int i, boolean[] visited, Stack<Integer> ordering) {
        if (!visited[i]) {
            visited[i] = true;
            for (int to : E.get(i)) {
                dfs(to, visited, ordering);
            }
            ordering.add(i);
        }
    }

    boolean assign(int i) {
        if (assignments[i] == UNASSIGNED) {
            Queue<Integer> candidates = new LinkedList<Integer>();
            candidates.offer(i);

            while (!candidates.isEmpty()) {
                int c = candidates.poll();

                if (assignments[c] == LEFT) {
                    continue;
                }

                if (assignments[c] == RIGHT) {
                    return false;
                }

                assignments[c] = LEFT;
                assignments[getNot(c)] = RIGHT;
                //assignments[getSpouse(c)] = RIGHT;
                //assignments[getNot(getSpouse(c))] = LEFT;

                for (int j : invE.get(c)) {
                    candidates.offer(j);
                }
            }
        }
        return true;
    }

    void solve() {
        Stack<Integer> ordering = new Stack<Integer>();
        boolean[] visited = new boolean[4 * N];

        for (int i = 0 ; i < 4 * N ; i++) {
            dfs(i, visited, ordering);
        }

        while (!ordering.isEmpty()) {
            int i = ordering.pop();
            if (assignments[i] == UNASSIGNED) {
                if (!assign(i)) {
                    System.out.println("bad luck");
                    return;
                }
            }
        }

        String output = "";
        for (int i = 0 ; i < N ; i++) {
            if (assignments[i] == LEFT) {
                output += "" + (i + 1) + "h ";
            }
            else {
                output += "" + (i + 1) + "w ";
            }
        }

        System.out.println(output);
    }

    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);

        int n = sin.nextInt() - 1; // bride and groom don't matter
        int a = sin.nextInt();
        sin.nextLine();

        while (n != 0 && a != 0) {

            Wedding solver = new Wedding(n);

            for (int i = 0 ; i < a ; i++) {
                String[] line = sin.nextLine().split(" ");
                int u = line[0].charAt(0) - '1';
                int v = line[1].charAt(0) - '1';

                if (line[0].charAt(1) == 'w') {
                    u = solver.getSpouse(u);
                }

                if (line[1].charAt(1) == 'w') {
                    v = solver.getSpouse(v);
                }

                solver.addImplication(u, solver.getNot(v));
                solver.addImplication(solver.getNot(v), u);
                solver.addImplication(v, solver.getNot(u));
                solver.addImplication(solver.getNot(u), v);
            }

            solver.solve();

            n = sin.nextInt() - 1;
            a = sin.nextInt();
            sin.nextLine();
        }
    }
}
