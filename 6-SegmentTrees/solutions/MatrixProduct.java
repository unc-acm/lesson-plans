import java.util.*;
import java.io.*;

public class MatrixProduct {
    static class Matrix3x3 {
        int[][] matrix;

        public Matrix3x3() {
            matrix = new int[3][3];
            for (int i = 0 ; i < 3 ; i++) {
                for (int j = 0 ; j < 3 ; j++) {
                    if (i == j) {
                        matrix[i][j] = 1;
                    }
                    else {
                        matrix[i][j] = 0;
                    }
                }
            }
        }

        public void getUserInput(Scanner sin) {
            for (int i = 0 ; i < 3 ; i++) {
                for (int j = 0 ; j < 3 ; j++) {
                    matrix[i][j] = sin.nextInt();
                }
            }
        }

        public String toString() {
            String result = "";
            for (int i = 0 ; i < 3 ; i++) {
                for (int j = 0 ; j < 3 ; j++) {
                    result += matrix[i][j] + " ";
                }
            }
            return result;
        }

        public static Matrix3x3 multiply(Matrix3x3 a, Matrix3x3 b) {
            Matrix3x3 result = new Matrix3x3();
            for (int i = 0 ; i < 3 ; i++) {
                for (int j = 0 ; j < 3 ; j++) {
                    int tmp = 0;
                    for (int k = 0 ; k < 3 ; k++) {
                        tmp += a.matrix[i][k] * b.matrix[k][j];
                    }
                    result.matrix[i][j] = tmp;
                }
            }
            return result;
        }
    }
    // Instance Variables
    List<Matrix3x3> segmentTree;
    int sz;
    Matrix3x3 init;

    // Constructors
    public MatrixProduct(List<Matrix3x3> elements, Matrix3x3 _i) {
        sz = elements.size();
        init = _i;
        segmentTree = new ArrayList<Matrix3x3>(2 * sz);
        for (int i = 0 ; i < 2 * sz ; i++) {
            segmentTree.add(new Matrix3x3());
        }
        for (int i = sz ; i < 2 * sz ; i++) {
            segmentTree.set(i, elements.get(i - sz));
        }
        for (int i = sz - 1 ; i > 0 ; i--) {
            segmentTree.set(i, Matrix3x3.multiply(segmentTree.get(2 * i),
                                                  segmentTree.get(2 * i + 1)));
        }
    }

    // Functions
    public Matrix3x3 query(int lbound, int rbound) { // query on range [lbound, rbound]
        Matrix3x3 resl = init;
        Matrix3x3 resr = init;
        for (int l = lbound + sz, r = rbound + sz ; l <= r ; l /= 2, r /= 2) {
            if (l % 2 == 1) {
                resl = Matrix3x3.multiply(resl, segmentTree.get(l));
                l++;
            }
            if (r % 2 == 0) {
                resr = Matrix3x3.multiply(segmentTree.get(r), resr);
                r--;
            }
        }
        return Matrix3x3.multiply(resl, resr);
    }

    public static void main(String[] args) {
        Scanner sin = new Scanner(System.in);
        int n = sin.nextInt();
        List<Matrix3x3> matrices = new ArrayList<Matrix3x3>();
        for (int i = 0 ; i < n ; i++) {
            Matrix3x3 m = new Matrix3x3();
            m.getUserInput(sin);
            matrices.add(m);
        }

        MatrixProduct segTree = new MatrixProduct(matrices, new Matrix3x3());

        int m = sin.nextInt();

        for (int i = 0 ; i < m ; i++) {
            int l = sin.nextInt();
            int r = sin.nextInt();
            System.out.println(segTree.query(l, r));
        }
    }
}
