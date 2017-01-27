import java.util.*;
import java.io.*;

public class Fedor {

    static class Coupon {
        public int l, r;

        public Coupon(int _l, int _r) {
            l = _l; r = _r;
        }

        public boolean coversRange(int lBount, int rBound) {
            return (l <= lBount && r >= rBound);
        }
    }

    static class Event implements Comparable {
        public boolean left;
        public int loc, couponID;

        public Event(boolean _l, int _loc, int _cID) {
            left = _l; loc = _loc; couponID = _cID;
        }

        public int compareTo(Object o) {
            if (this.loc == ((Event) o).loc) {
                if (this.left && !((Event) o).left) { // this is left and that is right
                    return -1;
                }
            }
            return (this.loc - ((Event) o).loc);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String inputs[] = reader.readLine().split(" ");

        int n = Integer.parseInt(inputs[0]);
        int k = Integer.parseInt(inputs[1]);

        List<Coupon> coupons = new ArrayList<Coupon>();

        for (int i = 0 ; i < n ; i++) {
            String coupon[] = reader.readLine().split(" ");
            int l = Integer.parseInt(coupon[0]);
            int r = Integer.parseInt(coupon[1]);

            coupons.add(new Coupon(l, r));
        }

        // unlike previous problems, b/c this problem requires multiple print
        // statements, we'll put output in solve to simplify code
        // (alternatively you can have solve return one thing and set some
        // global variable, but this is easier to understand)
        solve(coupons, k);
    }

    public static void solve(List<Coupon> coupons, int k) {
        Set<Integer> active = new TreeSet<Integer>();
        List<Event> endpoints = new ArrayList<Event>();

        for (int i = 0 ; i < coupons.size() ; i++) {
            Coupon c = coupons.get(i);
            endpoints.add(new Event(true, c.l, i));
            endpoints.add(new Event(false, c.r, i));
        }

        Collections.sort(endpoints);
        
        int counter = 0;
        int maxCouponsLength = 0;
        int maxCouponsLeft = 0, maxCouponsRight = 0;

        List<Event> leftEndPts = new ArrayList<Event>();

        for (int i = 0 ; i < endpoints.size() ; i++) {
            Event e = endpoints.get(i);

            //System.out.println("Counter: " + counter);

            if (e.left) {
                // this is the start of a coupon
                counter++; // increment the counter
                active.add(e.couponID);
                leftEndPts.add(e);
            }
            else {
                // this is the end of a coupon
                if (counter >= k) {
                    Event begin = leftEndPts.get(leftEndPts.size() - (counter - k + 1));
                    int extraSteps = 0;

                    while (!active.contains(begin.couponID)) {
                        extraSteps++;
                        begin = leftEndPts.get(leftEndPts.size() - (counter - k + 1) - extraSteps);
                    }

                    int KStackLength = e.loc - begin.loc + 1;

                    //if (KStackLength > 0) {
                    //    KStackLength++; // count left and right bound are inclusive
                    //}

                    //System.out.println(KStackLength + " (" + begin.loc + ", " + e.loc + ")");

                    if (KStackLength > maxCouponsLength) {
                        maxCouponsLength = KStackLength;
                        maxCouponsLeft = begin.loc;
                        maxCouponsRight = e.loc;
                        //System.out.println(maxCouponsLength + " (" + maxCouponsLeft + ", " + maxCouponsRight + ")");
                    }
                }

                active.remove(e.couponID);
                counter--;
            }
        }

        System.out.println(maxCouponsLength);

        int cUsed = 0;

        for (int i = 0 ; i < coupons.size() ; i++) {
            if (cUsed == k) {
                System.out.print("\n");
                break;
            }
            else {
                if (maxCouponsLength == 0 || coupons.get(i).coversRange(maxCouponsLeft, maxCouponsRight)) {
                    System.out.print((i+1) + " ");
                    cUsed++;
                }
            }
        }
    }
}
