import java.util.*;
import java.io.*;

public class Autocomplete {
    // Instance Variables

    static class Trie {
        int[] wordCount;
        int[] prefixCount;
        int[] level; // how many levels down a node is from the top
        ArrayList<Map<Character, Integer>> edges;
        char[] content; // debug purposes
        int size;
        int maxSize;

        public Trie(int _maxSize) {
            // note here maxSize is the total number of nodes, not just how many
            // strings can be stored in the trie
            size = 1; // root is ""
            maxSize = _maxSize;
            wordCount = new int[maxSize];
            prefixCount = new int[maxSize];
            level = new int[maxSize];
            content = new char[maxSize];
            edges = new ArrayList<Map<Character, Integer>>();
            for (int i = 0 ; i < maxSize ; i++) {
                edges.add(new HashMap<Character, Integer>());
            }
            level[0] = 0;
            content[0] = ' ';
        }

        int insert(String s) { // we are going to make insert return the amount of strokes necessary
            // there are 2 ways to for a prefix to become a key for autocomplete:
            // 1. when prefixCount at the current node is equal to 1
            // 2. when you reach a word
 
            int currentNode = 0; // start from empty
            boolean prefixFound = false;
            int strokes = 0;

            for (char c : s.toCharArray()) {
                prefixCount[currentNode]++;
                if (!prefixFound) { // passively takes care of condition 2
                    strokes++;
                }

                if (edges.get(currentNode).get(c) == null) { // condition 1
                    edges.get(currentNode).put(c, size);
                    level[size] = level[currentNode] + 1;
                    content[size] = c;
                    prefixFound = true;
                    size++;
                }

                currentNode = edges.get(currentNode).get(c);
            }

            prefixCount[currentNode]++;
            wordCount[currentNode]++;
            return strokes;
        }
    }

    // Functions

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int i = 0 ; i < T ; i++) {
            int N = Integer.parseInt(br.readLine());
            Trie t = new Trie(1000000);
            int count = 0;
            for (int j = 0 ; j < N ; j++) {
                String s = br.readLine();
                count += t.insert(s);
            }
            System.out.println("Case #" + (i + 1) + ": " + count);
        }
    }
}
