import java.util.*;
import java.io.*;

public class ShortestPathQuery {

    public static int INF = 1000000001; // 1e9 + 1

    static class Edge {
        int to;
        int weight, color;

        public Edge(int _t, int _w, int _c) {
            to = _t;
            weight = _w;
            color = _c;
        }
    }

    static class VertexComparison implements Comparable<VertexComparison> {
        Edge e;
        int[] dists;

        public VertexComparison(Edge _e, int[] _d) {
            e = _e;
            dists = _d;
        }

        @Override
        public int compareTo(VertexComparison o) {
            if (Integer.compare(dists[0], o.dists[0]) != 0) {
                return Integer.compare(dists[0], o.dists[0]);
            }
            else {
                return Integer.compare(dists[1], o.dists[1]);
            }
        }
    }

    // Functions
    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] inputs = br.readLine().split(" ");

        int n = Integer.parseInt(inputs[0]); //sc.nextInt();
        int m = Integer.parseInt(inputs[1]); //sc.nextInt();
        //int C = Integer.parseInt(inputs[2]) //sc.nextInt();

        List<List<Edge>> graph = new ArrayList<List<Edge>>();

        for (int i = 0 ; i < n ; i++) {
            graph.add(new ArrayList<Edge>());
        }

        for (int i = 0 ; i < m ; i++) {
            inputs = br.readLine().split(" ");
            int u = Integer.parseInt(inputs[0]) - 1; //sc.nextInt() - 1;
            int v = Integer.parseInt(inputs[1]) - 1; //sc.nextInt() - 1;
            int w = Integer.parseInt(inputs[2]); //sc.nextInt();
            int c = Integer.parseInt(inputs[3]); //sc.nextInt();

            graph.get(u).add(new Edge(v, w, c));
        }

        inputs = br.readLine().split(" ");
        int s = Integer.parseInt(inputs[0]) - 1; //sc.nextInt() - 1;
        int q = Integer.parseInt(inputs[1]); //sc.nextInt();

        // map of query -> dist
        HashMap<Integer, Integer> solutions = new HashMap<Integer, Integer>();
        int[] queries = new int[q];

        for (int i = 0 ; i < q ; i++) {
            int t = Integer.parseInt(br.readLine()) - 1;
            queries[i] = t;
            solutions.put(t, -1);
        }

        boolean[] visited = new boolean[n];

        // we keep 2 distances because of coloring issues
        int[][] distTo = new int[n][2];
        int[][] colorNode = new int[n][2];

        for (int i = 0 ; i < n ; i++) {
            if (i == s) {
                distTo[i][0] = distTo[i][1] = 0;
            }
            else {
                distTo[i][0] = distTo[i][1] = INF;
            }

            colorNode[i][0] = colorNode[i][1] = -1;
        }

        PriorityQueue<VertexComparison> frontier = new PriorityQueue<VertexComparison>();

        frontier.offer(new VertexComparison(new Edge(s, 0, -1), new int[]{0, 0}));
        
        int solved = 0;

        while (solved < q) { 
            int v = frontier.poll().e.to;

            if (visited[v]) {
                continue;
            }

            visited[v] = true;

            if (solutions.get(v) != null) {
                solutions.put(v, distTo[v][0]);
                solved++;
            }

            for (int i = 0 ; i < graph.get(v).size() ; i++) {
                Edge e = graph.get(v).get(i);
                int neighbor = e.to;

                if (visited[neighbor]) {
                    continue;
                }

                int currentCost = (colorNode[v][0] != e.color) ? distTo[v][0] : distTo[v][1];

                if (currentCost + e.weight < distTo[neighbor][0]) {
                    distTo[neighbor][1] = distTo[neighbor][0];
                    distTo[neighbor][0] = currentCost + e.weight;

                    colorNode[neighbor][1] = colorNode[neighbor][0];
                    colorNode[neighbor][0] = e.color;
                }

                else if (currentCost + e.weight < distTo[neighbor][1]) {
                    distTo[neighbor][1] = currentCost + e.weight;
                    colorNode[neighbor][1] = e.color;
                }

                frontier.offer(new VertexComparison(e, distTo[neighbor]));
            }
        }

        for (int query : queries) {
            if (solutions.get(query) >= INF) {
                System.out.println(-1);
            }
            else {
                System.out.println(solutions.get(query));
            }
        }
    }
}
