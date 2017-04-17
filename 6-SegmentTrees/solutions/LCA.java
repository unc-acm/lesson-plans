import java.util.*;
import java.io.*;

public class LCA { // lowest common ancestor
    static class DepthNode {
        int depth;
        int node;

        public DepthNode(int _d, int _n) {
            depth = _d; node = _n;
        }
    }

    // Instance Variables
    List<DepthNode> segmentTree;
    int sz = 0; // number of elements in the lowest row

    // Constructors
    public static DepthNode min(DepthNode a, DepthNode b) {
        if (a.depth < b.depth) {
            return a;
        }
        else if (a.depth > b.depth) {
            return b;
        }
        else {
            return (a.node <= b.node) ? a : b;
        }
    }

    public LCA(List<DepthNode> elements) {
        sz = elements.size();
        segmentTree = new ArrayList<DepthNode>(2 * sz);
        for (int i = 0 ; i < 2 * sz ; i++) {
            segmentTree.add(new DepthNode(0, 0));
        }
        
        for (int i = sz ; i < 2 * sz ; i++) {
            segmentTree.set(i, elements.get(i - sz));
        }
        for (int i = sz - 1 ; i > 0 ; i--) {
            segmentTree.set(i, min(segmentTree.get(2 * i), segmentTree.get(2 * i + 1)));
        }
    }

    // Functions
    public DepthNode query(int lbound, int rbound) {
        DepthNode result = new DepthNode(1000000008, 1000000008);
        for (int l = lbound + sz, r = rbound + sz ; l <= r ; l /= 2, r /= 2) {
            if (l % 2 == 1) {
                result = min(segmentTree.get(l), result);
                l++;
            }
            if (r % 2 == 0) {
                result = min(result, segmentTree.get(r));
                r--;
            }
        }
        return result;
    }

    public static void tour(List<List<Integer>> graph,
                            List<DepthNode> depth,
                            List<Integer> firstVisit,
                            int curr, int level, int prev) {
        depth.add(new DepthNode(level, curr));

        if (firstVisit.get(curr) == -1) {
            firstVisit.set(curr, depth.size() - 1);
        }

        for (int neighbor : graph.get(curr)) {
            if (neighbor == prev) {
                continue;
            }
            tour(graph, depth, firstVisit, neighbor, level + 1, curr);
            depth.add(new DepthNode(level, curr));
        }
    }

    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);

        int n = sin.nextInt();
        int e = sin.nextInt();

        List<List<Integer>> graph = new ArrayList<List<Integer>>();
        for (int i = 0 ; i < n ; i++) {
            graph.add(new ArrayList<Integer>());
        }

        for (int i = 0 ; i < e ; i++) {
            int f = sin.nextInt();
            int t = sin.nextInt();
            graph.get(f).add(t);
            graph.get(t).add(f);
        }

        List<DepthNode> depth = new ArrayList<DepthNode>();
        List<Integer> firstVisit = new ArrayList<Integer>(n);
        for (int i = 0 ; i < n ; i++) {
            firstVisit.add(-1);
        }

        int curr = 0, level = 0;
        tour(graph, depth, firstVisit, curr, level, -1);

        LCA segTree = new LCA(depth);

        int q = sin.nextInt();

        for (int i = 0 ; i < q ; i++) {
            int a = sin.nextInt();
            int b = sin.nextInt();
            int firstA = firstVisit.get(a);
            int firstB = firstVisit.get(b);

            DepthNode result = segTree.query(firstA, firstB);
            System.out.println(result.node);
        }
    }
}
