import java.util.*;
import java.io.*;

public class RangeUpdates {
    // Instance Variables
    List<Integer> segmentTree;
    List<Integer> original;

    // Constructors
    public RangeUpdates(List<Integer> elements) {
        original = elements;
        segmentTree = new ArrayList<Integer>(2 * original.size());
        for (int i = 0 ; i < 2 * original.size() ; i++) {
            segmentTree.add(0);
        }
    }

    // Functions
    public int query(int p) {
        int result = original.get(p);

        for (int i = p + original.size() ; i > 1 ; i /= 2) {
            result += segmentTree.get(i);
        }
        return result;
    }

    public void modify(int l, int r, int v) { // increment values in range [l, r] by v
        for (int i = l + original.size(), j = r + original.size() ; i <= j ; i /= 2, j /= 2) {
            if (i % 2 == 1) {
                segmentTree.set(i, segmentTree.get(i) + v);
                i++;
            }
            if (j % 2 == 0) {
                segmentTree.set(j, segmentTree.get(j) + v);
                j--;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);

        int n = sin.nextInt(); // n elements in list
        int q1 = sin.nextInt(); // q1 initial queries
        int u = sin.nextInt(); // u updates
        int q2 = sin.nextInt(); // q2 queries after updates

        List<Integer> numbers = new ArrayList<Integer>(n);

        for (int i = 0 ; i < n ; i++) {
            int x = sin.nextInt();
            numbers.add(x);
        }

        RangeUpdates segTree = new RangeUpdates(numbers);

        System.out.println("Queries:");

        for (int i = 0 ; i < q1 ; i++) {
            int q = sin.nextInt();
            System.out.println(segTree.query(q));
        }

        System.out.println("Updates:");
        for (int i = 0 ; i < u ; i++) {
            int l = sin.nextInt();
            int r = sin.nextInt();
            int v = sin.nextInt();
            segTree.modify(l, r, v);
        }

        System.out.println("Queries:");
        for (int i = 0 ; i < q2 ; i++) {
            int q = sin.nextInt();
            System.out.println(segTree.query(q));
        }
    }
}
