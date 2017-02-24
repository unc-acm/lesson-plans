import java.util.*;
import java.io.*;

public class XorSum {
    // Instance Variables
    static class XorTrie {
        int[][] edges;
        int size;
        int maxSize;

        public XorTrie(int _ms) {
            maxSize = _ms;
            edges = new int[maxSize][2];

            for (int i = 0 ; i < maxSize ; i++) {
                edges[i][0] = edges[i][1] = -1;
            }

            size = 1; // root is taken
        }

        public String toString() {
            String result = "";
            for (int i = 0 ; i < size ; i++) {
                result += "" + i + ": (0:" + edges[i][0] + ", 1:" + edges[i][1] + ")\n";
            }
            return result;
        }

        void insert(int n) {
            int currentNode = 0;

            for (int i = 30 ; i >= 0 ; i--) { // we skip the sign bit
                int bit = ((n & (1 << i)) != 0) ? 1 : 0;
                // explanation: (1 << i) --> leftshift 1 by i bits, creates a
                // number where the ith bit is 1 and everything else is 0
                // n binary and with that number gives you 0 if that bit is 0 or
                // 2^i if that bit is 1, so we just check against 0 to see if
                // that bit is 0 or 1

                if (edges[currentNode][bit] == -1) { // edge dne
                    edges[currentNode][bit] = size;
                    size++;
                }

                currentNode = edges[currentNode][bit];
            }
        }

        int maxXor(int n) { // returns max value of n xor'ed with any value in array
            int currentNode = 0;
            int result = 0;

            for (int i = 30 ; i >= 0 ; i--) { // we skip the sign bit
                int reverseBit = ((n & (1 << i)) == 0) ? 1 : 0; // we want the opposite of the actual bit for xor
                int xorCurrent = (edges[currentNode][reverseBit] != -1) ? 1 : 0;
                result = result | (xorCurrent << i);

                if (xorCurrent == 1) {
                    currentNode = edges[currentNode][reverseBit];
                }
                else {
                    currentNode = edges[currentNode][(reverseBit - 1) * (reverseBit - 1)]; // math is magical
                    // (x - 1)^2 flips 0 and 1
                }
            }

            return result;
        }
    }

    // Functions
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int i = 0 ; i < T ; i++) {
            int N = Integer.parseInt(br.readLine());
            XorTrie xt = new XorTrie(N * 32); // liberal upper bound of possible nodes necessary
            int max = 0;
            int left = 0;
            xt.insert(left);

            // solution relies on the fact that xorSum from i to j is equal to
            // (xorSum from 0 to i) xor (xorSum from 0 to j)
            // This can be proven by the fact that xor is communicative and a
            // xor a == 0 and 0 xor a == a
            //
            // so we insert into the trie xorSum from 0 to i at each index i and
            // query for j before i such that (xorSum from 0 to i) xor (xorSum
            // from 0 to j) is the greatest

            for (int j = 0 ; j < N ; j++) {
                int a = Integer.parseInt(br.readLine());
                left = left ^ a;
                xt.insert(left);
                int currentMax = xt.maxXor(left);
                if (currentMax > max) {
                    max = currentMax;
                }
            }
            System.out.println(max);
        }
    }
}
