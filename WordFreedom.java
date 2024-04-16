public class WordFreedom {

	private final static int INF = 1000000000;
	
	int n;
	String[] s;
	
	int[][] d = new int[128][128];
	int[][] f = new int[128][128];
	int[][][] p = new int[3][128][128];
	boolean[][][] applicable = new boolean[101][101][101];
	
	String[] out = new String[10024];
	int outCount = 0;
	
	String getWord(int index, int length) {
		StringBuilder sb = new StringBuilder();
		out[outCount++] = s[index];
		while(true) {
			int newIndex = p[0][index][length];
			int newLength = p[1][index][length];
			out[outCount++] = s[p[2][index][length]];
			if(newIndex == -1) {
				break;
			}
			index = newIndex;
			length = newLength;
		}
		int len = out[outCount - 1].length();
		sb.append(out[outCount - 1]);
		for(int i = outCount - 2; i >= 0; i--) {
			if(len < 0) {
				sb.append(out[i]);
				len += out[i].length();
			} else {
				len -= out[i].length();
			}
		}
		return sb.toString();
	}
	
	String doDijkstra() {
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[i].length; j++) {
				d[i][j] = INF;
				f[i][j] = 0;
			}
		}
		
		for(int i = 0; i < n; i++) {
			d[i][s[i].length()] = s[i].length();
			p[0][i][s[i].length()] = -1;
			p[1][i][s[i].length()] = 0;
			p[2][i][s[i].length()] = i;
		}
		
		while(true) {
			int index = -1;
			int length = -1;
			int best = INF;
			for(int i = 0; i < n; i++) {
				for(int j = 0; j <= s[i].length(); j++) {
					if(best > d[i][j] && f[i][j] == 0) {
						best = d[i][j];
						index = i;
						length = j;
					}
				}
			}
			if(index < 0 && length < 0) break;
			f[index][length] = 1;
			
			if(length == 0) {
				return getWord(index, length);
			}
			
			for(int i = 0; i < n; i++) {
				if(applicable[index][length][i]) {
					int newLength = length - s[i].length();
					int newWord = index;
					int w = 0;
					if(s[i].length() >= length) {
						newLength = s[i].length() - length;
						newWord = i;
						w = newLength;
					}
					if(d[newWord][newLength] > d[index][length] + w && f[newWord][newLength] == 0) {
						d[newWord][newLength] = d[index][length] + w;
						p[0][newWord][newLength] = index;
						p[1][newWord][newLength] = length;
						p[2][newWord][newLength] = i;
					}
				}
			}
			
		}
		return "";
	}
	
	boolean isOk(int index, int length, int k) {
		int start = s[index].length() - length;
		for(int i = 0; i < length && i < s[k].length(); i++) {
			if(s[index].charAt(start + i) != s[k].charAt(i)) return false;
		}
		return true;
	}
	
	String solve(String[] in) {
		s = in;
		n = in.length;
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j <= s[i].length(); j++) {
				for(int k = 0; k < n; k++) {
					if(j == s[i].length() && i == k) {
						continue;
					}
					applicable[i][j][k] = isOk(i, j, k);
				}
			}
		}
		
		return doDijkstra();
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		String[] s = new String[n];
		for(int i = 0; i < n; i++) {
			s[i] = scanner.next();
		}
		scanner.close();
		WordFreedom wf = new WordFreedom();
		String h = wf.solve(s);
		if(h.length() == 0) {
			System.out.println("NO");
		} else {
			System.out.println("YES");
		}
		System.out.print(h);
	}
}
