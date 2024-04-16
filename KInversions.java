public class KInversions {

	private final int Q = 40001;
	private final int D = 11;

	int[][] c = new int[D][2 * Q];

	void add(int index, int x, int z)
	{
		for(int i = x + Q; i != 0; i >>= 1)
			c[index][i] = A(c[index][i], z);
	}

	int getSum(int index, int L, int R)
	{
		L += Q; R += Q;
		int sum = 0;
		while(L < R)
		{
			if((L & 1) != 0) sum = A(sum, c[index][L++]);
			if((R & 1) != 0) sum = A(sum, c[index][--R]);
			L >>= 1; R >>= 1;
		}
		return sum;
	}
	private final int P = 1000000000;
	
	int n;
	int k;
	int[] a = new int[32768];
	int[] id = new int[32768];
	
	int[][] d = new int[11][32768];
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		k = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			a[i] = scanner.nextInt();
			id[a[i]] = i;
		}
		scanner.close();
	}
	
	int A(int x, int y) {
		int h = x + y;
		return h >= P ? h - P : h;
	}
	
	void solve() {
		init();
		
		for(int i = n; i >= 1; i--) {
			d[1][id[i]] = 1;
			add(1, id[i], 1);
			for(int j = 2; j <= k; j++) {
				int h = getSum(j - 1, 0, id[i]);
				d[j][id[i]] = h;
				add(j, id[i], h);
			}
		}
		
		int ans = 0;
		for(int i = 1; i <= n; i++) {
			ans = A(ans, d[k][i - 1]);
		}
		
		System.out.println(ans);
	}
	
	public static void main(String[] args) {
		KInversions ki = new KInversions();
		ki.solve();
	}
	
}
