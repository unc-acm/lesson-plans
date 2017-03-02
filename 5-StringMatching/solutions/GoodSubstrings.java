import java.util.*;
import java.io.*;

public class GoodSubstrings {
    // Instance Variables
    public static final long P = 67280421310721L;
    public static final int B = 26;

    // Functions
    static long mod(long a, long b) {
        // calculates a % b's positive value even if a < b
        return (a % b + b) % b;
    }

    static int solve(String input, boolean[] bad, int limit) {
        Set<Long> hashes = new TreeSet<Long>();
        int N = input.length();

        for (int i = 0 ; i < N ; i++) {
            int badCount = 0;
            long hash = 0;

            for (int j = i ; j < N ; j++) {
                // we are not even rolling, we can just expand
                hash = mod(hash * B + (input.charAt(j) - 'a'), P);

                if (bad[input.charAt(j) - 'a']) {
                    badCount++;
                }

                if (badCount <= limit) {
                    if (hashes.add(hash)) {
                        System.out.println(input.substring(i, j + 1));
                    }
                }
            }
        }

        return hashes.size();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();

        br.read(); // get rid of the newline

        boolean[] bad = new boolean[26];
        for (int i = 0 ; i < 26 ; i++) {
            bad[i] = ((char)br.read() == '0');
        }

        int limit = Integer.parseInt(br.readLine());

        System.out.println(solve(input, bad, limit));
    }
}
