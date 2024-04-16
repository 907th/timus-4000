public class JapaneseCrossword {

    int n;
    int m;
    int[] a = new int[404];
    int[][] dp = new int[404][404];

    char[] c = new char[404];
    StringBuilder sb = new StringBuilder();

    void init() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        n = scanner.nextInt() + 1;
        m = scanner.nextInt();
        for(int i = 0; i < m; i++) {
            a[i] = scanner.nextInt();
        }
        String s = scanner.next() + ".";
        for(int i = 0; i < n; i++) {
            c[i] = s.charAt(i);
        }
        scanner.close();
    }

    boolean isOkDot(int l, int r) {
        for(int i = l; i < r; i++) {
            if(c[i] == '.') return false;
        }
        return true;
    }

    int DP() {
        for(int i = 0; i < dp.length; i++) {
            for(int j = 0; j < dp[i].length; j++) {
                dp[i][j] = 0;
            }
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j <= m; j++) {
                if(i - a[j] == 0 && j == 0) {
                    if(isOkDot(0, i) && c[i] != 'X') {
                        dp[i][j + 1] = 1;
                    }
                }
                if(i - a[j] > 0) {
                    if(dp[i - a[j] - 1][j] == 1) {
                        if(isOkDot(i - a[j], i) && c[i] != 'X') {
                            dp[i][j + 1] = 1;
                        }
                    }
                }
                if(c[i] != 'X') {
                    if(i == 0 && j == 0) {
                        dp[i][j] = 1;
                    }
                    if(i > 0 && dp[i - 1][j] == 1) {
                        dp[i][j] = 1;
                    }
                }
            }
        }

        return dp[n - 1][m];
    }

    void solve() {
        init();
        int h = DP();
        if(h == 0) {
            System.out.println("Impossible");
            return;
        }

        for(int i = 0; i < n - 1; i++) {
            if(c[i] == '?') {
                c[i] = 'X';
                int h1 = DP();
                c[i] = '.';
                int h2 = DP();
                if(h1 == 1 && h2 == 1) {
                    sb.append("?");
                }
                if(h1 == 1 && h2 == 0) {
                    sb.append("X");
                }
                if(h1 == 0 && h2 == 1) {
                    sb.append(".");
                }
                c[i] = '?';
            } else {
                sb.append(c[i]);
            }
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        JapaneseCrossword jc = new JapaneseCrossword();
        jc.solve();
    }

}
