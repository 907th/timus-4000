public class Cheese {

    int limit = 1000000;
    double weightLimit = 500;
    private final double eps = 1e-6;

    int n;
    double[] y;
    double[] r;
    int[] a = new int[32];
    int size = 0;

    double Q(double x) {
        return x * x;
    }

    double T(double x) {
        return x * x * x;
    }

    double V(int index, double yv) {
        return Math.PI * Q(r[index]) * yv + Math.PI * T(y[index] - yv) / 3.0;
    }

    double getVolume(double lastEdge, double currentEdge, int index) {
        double left = (y[index] - r[index]);
        double right = (y[index] + r[index]);
        if((y[index] + r[index]) < lastEdge) {
            return 0;
        } else {
            left = Math.max(left, lastEdge);
        }
        if((y[index] - r[index]) > currentEdge) {
            return 0;
        } else {
            right = Math.min(right, currentEdge);
        }

        return (V(index, right) - V(index, left));
    }

    double getWeight(double lastEdge, double currentEdge) {
        double w = (currentEdge - lastEdge) * 10 * 10;
        for(int i = 0; i < n; i++) {
            double h = getVolume(lastEdge, currentEdge, i);
            w -= h;
        }
        return w;
    }

    int getEdge(int lastEdge) {
        double l = lastEdge + 1;
        double r = limit;
        double w = getWeight(lastEdge / 10000.0, limit / 10000.0);
        if(w < weightLimit) return -1;
        while(r - l > eps) {
            double m = (r + l) / 2;
            w = getWeight(lastEdge / 10000.0, m / 10000.0);
            if(w < weightLimit) {
                l = m;
            } else {
                r = m;
            }
        }
        int h = (int)(l + 0.5);
        if(h >= limit) {
            return -1;
        }
        return h;
    }

    void solve(double[][] in) {
        n = in[0].length;
        y = in[1];
        r = in[3];

        int lastEdge = 0;
        while(lastEdge >= 0) {
            int edge = getEdge(lastEdge);
            lastEdge = edge;
            a[size++] = edge;
        }

        System.out.println(size - 1);
        for(int i = 0; i < size - 1; i++) {
            System.out.println(a[i]);
        }
    }

    public static void main(String[] args) {
        Cheese c = new Cheese();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int n = scanner.nextInt();
        double[][] circles = new double[4][n];
        for(int i = 0; i < n; i++) {
            String x = scanner.next();
            String y = scanner.next();
            String z = scanner.next();
            String r = scanner.next();
            circles[0][i] = Double.parseDouble(x);
            circles[1][i] = Double.parseDouble(y);
            circles[2][i] = Double.parseDouble(z);
            circles[3][i] = Double.parseDouble(r);
        }
        scanner.close();
        c.solve(circles);
    }

}
