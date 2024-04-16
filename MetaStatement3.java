public class MetaStatement3 {

	private final int P = 1000000007;
	private final int N = 4096;
	
	int[] a = new int[N];

	int[] sum = new int[N];
	int[][] dp = new int[N][N];
	int n, m;
	
	int[][] c = new int[N][N];
	
	int C(int k, int n) {
		if(c[k][n] >= 0) return c[k][n];
		if(k > n) return 0;
		if(k == 0) return 1;
		if(k == n) return 1;
		if(k == 1) return n;
		c[k][n] = (C(k - 1, n - 1) + C(k, n - 1)) % P;
		return c[k][n];
	}
	
	void init() {
		
		for(int i = 0; i < c.length; i++) {
			for(int j = 0; j < c[i].length; j++) {
				c[i][j] = -1;
			}
		}
		
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		m = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			a[i] = scanner.nextInt();
		}
		scanner.close();
	}
	
	int M(long a, long b) {
		return (int)((a * b) % P);
	}
	
	void solve() {
		init();
		
		sum[0] = 0;
		for(int i = 1; i <= n; i++) {
			sum[i] = sum[i - 1] + a[i - 1];
		}
		
		dp[0][0] = 1;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j <= m; j++) {
				
				if(dp[i][j] == 0) continue;
				
				int onesCount = Math.min(a[i], j);
				
				for(int k = 0; k <= onesCount; k++) {
					
					int diff = (a[i] - k) + (j - k);
					
					int tasksWithZero = m - ((sum[i] - j) / 2 + j);
					
					dp[i + 1][diff] = (dp[i + 1][diff] + M(dp[i][j], M(C(k, j), C(a[i] - k, tasksWithZero)))) % P;  
				}
				
			}
		}
		
		int answer = 0;
		for(int i = 0; i <= m; i++) {
			answer = (answer + dp[n][i]) % P;
		}
		
		System.out.println(answer);
		
	}
	
	public static void main(String[] args) {
		MetaStatement3 ms3 = new MetaStatement3();
		ms3.solve();
	}
	
}
