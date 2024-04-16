public class FractalLabyrinth {

    private final int INF = 1000000000;

    int[][][][] a = new int[20][6][20][6];

    int n, k;

    int from, to;

    void addEdge(int x1, int y1, int x2, int y2) {
        a[x1][y1][x2][y2] = 1;
        a[x2][y2][x1][y1] = 1;

        if(y1 == 0 && y2 == 0) {
            for(int i = 1; i <= k; i++) {
                a[x1][i][x2][i] = 1;
                a[x2][i][x1][i] = 1;
            }
        }

    }

    void init() {

        for(int i1 = 0; i1 < a.length; i1++) {
            for(int i2 = 0; i2 < a[i1].length; i2++) {
                for(int i3 = 0; i3 < a[i1][i2].length; i3++) {
                    for(int i4 = 0; i4 < a[i1][i2][i3].length; i4++) {
                        a[i1][i2][i3][i4] = INF;
                    }
                }
            }
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        n = scanner.nextInt();
        k = scanner.nextInt();
        int m = scanner.nextInt();
        for(int i = 0; i < m; i++) {
            String s1 = scanner.next();
            String t = scanner.next();
            String s2 = scanner.next();

            String[] v1 = s1.split("\\.");
            String[] v2 = s2.split("\\.");
            addEdge(Integer.parseInt(v1[1]), Integer.parseInt(v1[0]), Integer.parseInt(v2[1]), Integer.parseInt(v2[0]));
        }
        from = scanner.nextInt();
        to = scanner.nextInt();
        scanner.close();
    }

    void solve() {
        init();

        for(int k1 = 0; k1 < n; k1++) {
            for (int k2 = 0; k2 <= k; k2++) {
                a[k1][k2][k1][k2] = 0;
            }
        }

        for(int st = 0; st < n; st++) {

            for (int k1 = 0; k1 < n; k1++) {
                for (int k2 = 0; k2 <= k; k2++) {

                    for (int i1 = 0; i1 < n; i1++) {
                        for (int i2 = 0; i2 <= k; i2++) {

                            for (int j1 = 0; j1 < n; j1++) {
                                for (int j2 = 0; j2 <= k; j2++) {
                                    if (a[i1][i2][j1][j2] > a[i1][i2][k1][k2] + a[k1][k2][j1][j2]) {
                                        a[i1][i2][j1][j2] = a[i1][i2][k1][k2] + a[k1][k2][j1][j2];

                                        if (i2 == 0 && j2 == 0) {
                                            for (int i = 1; i <= k; i++) {
                                                if (a[i1][i][j1][i] > a[i1][i2][j1][j2])
                                                    a[i1][i][j1][i] = a[i1][i2][j1][j2];
                                            }
                                        }

                                    }
                                }
                            }

                        }
                    }

                }
            }

        }

        if(a[from][0][to][0] >= INF) {
            System.out.println("no solution");
        } else {
            System.out.println(a[from][0][to][0]);
        }
    }

    public static void main(String[] args) {
        FractalLabyrinth fl = new FractalLabyrinth();
        fl.solve();
    }

}
