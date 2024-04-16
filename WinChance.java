public class WinChance {

	int[] a;
	String s;
	int n;
	
	int hsLimit = 30000;
	
	int[][] b = new int[2][64];
	int[] bSize = new int[2];
	
	int[][] f = new int[2][64];
	
	int[][] answer = new int[2][64];

	java.util.HashSet<String> hs = new java.util.HashSet<>();
	
	long getKey(int x) {
		long answer = 0;
		for(int i = 0; i < bSize[x]; i++) {
			answer = (answer << 1) | f[x][i];
		}
		return answer;
	}
	
	int rec(int s1, int s2, int cur, int last) {
		if(hs.size() > hsLimit) return 0;

		long key0 = getKey(0);
		long key1 = getKey(1);
		
		String totalKey = key0 + " "+ key1;
		if(hs.contains(totalKey)) return 0;

		if(cur == n) {
			for(int i = 0; i < n; i++) {
				System.out.println(answer[1][i] + " " + (answer[0][i] == 0 ? 'L' : 'R'));
			}
			return 1;
		}

		if(last == 1) {
			for(int i = 0; i < bSize[0]; i++) {
				if(f[0][i] == 0) {
					if(s.charAt(cur) == 'L' && s1 + b[0][i] > s2) {
						f[0][i] = 1;
						answer[0][cur] = 0; answer[1][cur] = b[0][i];
						int h = rec(s1 + b[0][i], s2, cur + 1, 0);
						f[0][i] = 0;
						if(h == 1) return 1;
					} else
					if(s.charAt(cur) == 'R' && s1 + b[0][i] < s2) {
						f[0][i] = 1;
						answer[0][cur] = 0; answer[1][cur] = b[0][i];
						int h = rec(s1 + b[0][i], s2, cur + 1, 0);
						f[0][i] = 0;
						if(h == 1) return 1;
					}
				}
			}

			for(int i = 0; i < bSize[1]; i++) {
				if(f[1][i] == 0) {
					if(s.charAt(cur) == 'R' && s2 + b[1][i] > s1) {
						f[1][i] = 1;
						answer[0][cur] = 1; answer[1][cur] = b[1][i];
						int h = rec(s1, s2 + b[1][i], cur + 1, 1);
						f[1][i] = 0;
						if(h == 1) return 1;
					} else
					if(s.charAt(cur) == 'L' && s2 + b[1][i] < s1) {
						f[1][i] = 1;
						answer[0][cur] = 1; answer[1][cur] = b[1][i];
						int h = rec(s1, s2 + b[1][i], cur + 1, 1);
						f[1][i] = 0;
						if(h == 1) return 1;
					}
				}
			}
		} else {
			for(int i = 0; i < bSize[1]; i++) {
				if(f[1][i] == 0) {
					if(s.charAt(cur) == 'R' && s2 + b[1][i] > s1) {
						f[1][i] = 1;
						answer[0][cur] = 1; answer[1][cur] = b[1][i];
						int h = rec(s1, s2 + b[1][i], cur + 1, 1);
						f[1][i] = 0;
						if(h == 1) return 1;
					} else
					if(s.charAt(cur) == 'L' && s2 + b[1][i] < s1) {
						f[1][i] = 1;
						answer[0][cur] = 1; answer[1][cur] = b[1][i];
						int h = rec(s1, s2 + b[1][i], cur + 1, 1);
						f[1][i] = 0;
						if(h == 1) return 1;
					}
				}
			}
			for(int i = 0; i < bSize[0]; i++) {
				if(f[0][i] == 0) {
					if(s.charAt(cur) == 'L' && s1 + b[0][i] > s2) {
						f[0][i] = 1;
						answer[0][cur] = 0; answer[1][cur] = b[0][i];
						int h = rec(s1 + b[0][i], s2, cur + 1, 0);
						f[0][i] = 0;
						if(h == 1) return 1;
					} else
					if(s.charAt(cur) == 'R' && s1 + b[0][i] < s2) {
						f[0][i] = 1;
						answer[0][cur] = 0; answer[1][cur] = b[0][i];
						int h = rec(s1 + b[0][i], s2, cur + 1, 0);
						f[0][i] = 0;
						if(h == 1) return 1;
					}
				}
			}
		}
		hs.add(totalKey);
		return 0;
	}
	
	void solve(String s, int[] w) {
		this.s = s;
		a = w;
		java.util.Arrays.sort(a);
		n = a.length;
		int cur = s.charAt(n - 1) == 'R' ? 1 : 0;
		for(int i = n - 1; i >= 0; i--) {
			b[cur][bSize[cur]++] = a[i];
			cur = 1 - cur;
		}
		
		int h = rec(0, 0, 0, -1);
		if(h == 0) {
			for(int k = 0; k < 2; k++) {
				java.util.Arrays.sort(b[k], 0, bSize[k]);
			}

			hs.clear();
			hsLimit = 1000000;
			h = rec(0, 0, 0, -1);
		}
	}
	
	public static void main(String[] args) {
		WinChance wc = new WinChance();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int[] w = new int[n];
		for(int i = 0; i < n; i++) {
			w[i] = scanner.nextInt();
		}
		String s = scanner.next();
		scanner.close();
		wc.solve(s, w);
	}
	
}