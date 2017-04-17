import java.util.*;
import java.io.*;

public class Checkposts {
    // Instance Variables
    int V;
    List<List<Integer>> E;
    List<List<Integer>> InvE;
    long[] costs;
    int[] scc;

    ArrayList<Long> min_cost;
    ArrayList<Integer> options;

    // Constructors
    public Checkposts(int _V, long[] c) {
        V = _V;
        E = new ArrayList<List<Integer>>(V);
        for (int i = 0 ; i < V ; i++) {
            E.add(null);
        }
        InvE = new ArrayList<List<Integer>>(V);
        for (int i = 0 ; i < V ; i++) {
            InvE.add(null);
        }
        scc = new int[V];
        costs = c;

        options = new ArrayList<Integer>();
        min_cost = new ArrayList<Long>();
    }

    void addEdge(int from, int to) {
        if (E.get(from) == null) {
            E.set(from, new ArrayList<Integer>());
        }
        E.get(from).add(to);

        if (InvE.get(to) == null) {
            InvE.set(to, new ArrayList<Integer>());
        }
        InvE.get(to).add(from);
    }

    // Functions
    void dfs(int i, boolean[] visited, Stack<Integer> ordering) {
        if (!visited[i]) {
            visited[i] = true;
            if (E.get(i) != null) {
                for (int to : E.get(i)) {
                    dfs(to, visited, ordering);
                }
            }
            ordering.add(i);
        }
    }

    void assign(int i, int scc_i) {
        if (scc[i] == 0) {
            scc[i] = scc_i;

            if (costs[i] == min_cost.get(scc_i - 1)) {
                options.set(scc_i - 1, options.get(scc_i - 1) + 1);
            }

            if (costs[i] < min_cost.get(scc_i - 1)) {
                min_cost.set(scc_i - 1, costs[i]);
                options.set(scc_i - 1, 1);
            }

            if (InvE.get(i) != null) {
                for (int j : InvE.get(i)) {
                    assign(j, scc_i);
                }
            }
        }
    }

    void solve() {
        Stack<Integer> ordering = new Stack<Integer>();
        boolean[] visited = new boolean[V];

        for (int i = 0 ; i < V ; i++) {
            dfs(i, visited, ordering);
        }

        int scc_i = 1;

        while (!ordering.isEmpty()) {
            int i = ordering.pop();
            if (scc[i] == 0) {
                options.add(1);
                min_cost.add(1000000009L);
                assign(i, scc_i);
                scc_i++;
            }
        }

        long total_costs = 0;
        long total_options = 1;

        for (long c : min_cost) {
            total_costs += c;
        }

        for (int o : options) {
            total_options = total_options * o % 1000000007;
        }

        System.out.printf("%d %d\n", total_costs, total_options);
    }

    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);
        int N = sin.nextInt();
        long[] c = new long[N];
        for (int i = 0 ; i < N ; i++) {
            c[i] = sin.nextLong();
        }

        Checkposts solver = new Checkposts(N, c);

        int E = sin.nextInt();

        for (int i = 0 ; i < E ; i++) {
            int from = sin.nextInt() - 1;
            int to = sin.nextInt() - 1;
            solver.addEdge(from, to);
        }

        solver.solve();
    }
}
