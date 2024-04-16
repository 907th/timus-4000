public class MetaStatement {

	private final int N = 100004;
	private final int P = 1000000007;
	private final int INF = 1000000000;
	
	int n;
	int[] a = new int[N];
	int[] pos = new int[N];
	
	int M(long a, long b) {
		return (int)((a * b) % P);
	}
	
	int pow(int x, int y) {
		if(y == 1) return x;
		int h = pow(x, y / 2);
		if(y % 2 == 1) {
			return M(M(h, h), x);
		}
		return M(h, h);
	}
	
	int[] fact = new int[N];
	int[] factR = new int[N];
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			a[i] = scanner.nextInt();
			pos[a[i]] = i;
		}
		scanner.close();
		
		root = buildTree(0, n);
		
		fact[1] = 1;
		factR[1] = pow(fact[1], P - 2);
		for(int i = 2; i <= n; i++) {
			fact[i] = M(fact[i - 1], i);
			factR[i] = pow(fact[i], P - 2);
		}
		
	}
	
	int[] anc1 = new int[2 * N];
	int[] anc2 = new int[2 * N];
	int[] left = new int[2 * N];
	int[] right = new int[2 * N];
	int[] value = new int[2 * N];
	int nodesCount = 0;
	int root = 0;
	
	int addNode(int a1, int a2, int val, int l, int r) {
		anc1[nodesCount] = a1;
		anc2[nodesCount] = a2;
		value[nodesCount] = val;
		left[nodesCount] = l;
		right[nodesCount] = r;
		
		nodesCount++;
		return nodesCount - 1;
	}
	
	int getValue(int nodeIndex) {
		return value[nodeIndex];
	}
	
	int buildTree(int l, int r) {
		if(r - l == 1) {
			return addNode(-1, -1, a[l], l, r);
		}
		int m = (r + l) / 2;
		int a1 = buildTree(l, m);
		int a2 = buildTree(m, r);
		return addNode(a1, a2, Math.min(getValue(a1), getValue(a2)), l, r);
	}
	
	int getMin(int nodeIndex, int l, int r) {
		if(l <= left[nodeIndex] && right[nodeIndex] <= r) return value[nodeIndex];
		if(right[nodeIndex] <= l || r <= left[nodeIndex]) return INF;
		return Math.min(getMin(anc1[nodeIndex], l, r), getMin(anc2[nodeIndex], l, r));
	}
	
	int C(int k, int n) {
		if(k == n) return 1;
		if(k == 0) return 1;
		return M(M(fact[n], factR[k]), factR[n - k]);
	}

	int findMin(int l, int r) {
		return getMin(root, l, r);
	}
	
	int rec(int l, int r) {
		if(r - l <= 1) return 1;
		int index = pos[findMin(l, r)];
		int total = r - l - 1;
		int count = index - l;
		return M(M(rec(l, index), rec(index + 1, r)), C(count, total));
	}
	
	void solve() {
		init();
		int h = rec(0, n);
		System.out.println(h);
	}
	
	public static void main(String[] args) {
		MetaStatement ms = new MetaStatement();
		ms.solve();
	}
	
}
