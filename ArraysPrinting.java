public class ArraysPrinting {

	private final int N = 514;
	private final int M = 10007;
	
	int[] a = new int[128];
	int n = 0;
	
	int[][] dpc = new int[N][N];
	
	int[][] dp = new int[N][N];
	
	int DP(int level, int count) {
		if(dp[level][count] != 0) return dp[level][count];
		if(level >= n) return 1;
		int h = 0;
		
		for(int i = 1; i <= count; i++) {
			int local = DP(level + 1, a[level + 1] * i);
			h = (h + (local * dpc[count][i]) % M) % M;
		}
		
		dp[level][count] = h;
		return h;
	}
	
	void init(String s) {
		int balance = 0;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '[') {
				balance++;
				if(n < balance)
					n = balance;
			}
			if(s.charAt(i) == ']') {
				balance--;
				a[balance]++;
			}
		}
		for(int i = n - 1; i >= 1; i--) {
			a[i] /= a[i - 1];
		}
		
	}
	
	int solve(String s) {
		init(s);
		dpc[1][1] = 1;
		for(int i = 1; i < N - 1; i++) {
			for(int j = 1; j < N - 1; j++) {
				if(dpc[i][j] == 0) continue;
				dpc[i + 1][j + 1] = (dpc[i + 1][j + 1] + dpc[i][j]) % M;
				dpc[i + 1][j] = (dpc[i + 1][j] + (dpc[i][j] * j) % M) % M;
			}
		}
		
		return DP(0, a[0]);
	}
	
	public static void main(String[] args) {
		ArraysPrinting ap = new ArraysPrinting();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		String s = scanner.nextLine();
		scanner.close();
		int h = ap.solve(s);
		System.out.println(h);
	}
	
}
