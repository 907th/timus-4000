class Printer {
	
	int[][] f1 = new int[32][32];
	int[][] f2 = new int[32][32];
	int[][] a = new int[32][32];
	int[] letter = new int[32];
	int n;
	
	char[][] board = new char[32][32];
	
	private void relaxLetter(int x, int y, int val) {
		if(x < 0 || y < 0 || x >= n || y >= n) return;
		if(f1[x][y] == 0 && a[x][y] == val) {
			findLetter(x, y, val);
		}
		if(f1[x][y] == 1) {
			if(a[x][y] != val) {
				letter[board[x][y] - 'a'] = 1;
			}
		}
	}
	
	private void findLetter(int x, int y, int val) {
		f1[x][y] = 1;
		relaxLetter(x + 1, y, val);
		relaxLetter(x - 1, y, val);
		relaxLetter(x, y + 1, val);
		relaxLetter(x, y - 1, val);
	}
	
	private void relax(int x, int y, int val, char c) {
		if(x < 0 || y < 0 || x >= n || y >= n) return;
		if(f2[x][y] == 0 && a[x][y] == val) {
			find(x, y, val, c);
		}
	}
	
	private void find(int x, int y, int val, char c) {
		f2[x][y] = 1;
		board[x][y] = c;
		relax(x + 1, y, val, c);
		relax(x - 1, y, val, c);
		relax(x, y + 1, val, c);
		relax(x, y - 1, val, c);
	}
	
	public String getSolution(int size, int[][] a, int totalCount) {
		this.a = a;
		n = size;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(f1[i][j] == 0) {
					java.util.Arrays.fill(letter, 0);
					findLetter(i, j, a[i][j]);
					for(int k = 0; k < 26; k++) {
						if(letter[k] == 0) {
							find(i, j, a[i][j], (char)(k + 'a'));
							break;
						}
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(totalCount).append("\\n");
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				sb.append(board[i][j]);
			}
			sb.append("\\n");
		}
		return sb.toString();
	}
	
}

public class PolyominoValidator {

	int n, m;
	int[][] a = new int[32][32];
	int[][] f = new int[32][32];
	int[][] b = new int[32][32];
	
	int pCount = 0;
	Polyomino[] p = new Polyomino[2048];
	
	private void relax(int x, int y, int v) {
		if(x < 0 || y < 0 || x >= n || y >= m) return;
		if(f[x][y] == 1 || a[x][y] != v) return;
		rec(x, y, v);
	}
	
	private void rec(int x, int y, int v) {
		f[x][y] = 1;
		b[x][y] = 1;
		relax(x + 1, y, v);
		relax(x - 1, y, v);
		relax(x, y + 1, v);
		relax(x, y - 1, v);
	}
	
	private void getPolyomino(int x, int y) {
		for(int i = 0; i < b.length; i++) {
			java.util.Arrays.fill(b[i], 0);
		}
		rec(x, y, a[x][y]);
		
		int mnX = 100, mnY = 100, mxX = -1, mxY = -1;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(b[i][j] == 1) {
					if(mnX > i) mnX = i;
					if(mxX < i) mxX = i;
					if(mnY > j) mnY = j;
					if(mxY < j) mxY = j;
				}
			}
		}
	
		Polyomino cur = new Polyomino();
		cur.n = mxX - mnX + 1;
		cur.m = mxY - mnY + 1;
		for(int i = mnX; i <= mxX; i++) {
			for(int j = mnY; j <= mxY; j++) {
				cur.a[i - mnX][j - mnY] = b[i][j];
				cur.c[i - mnX][j - mnY] = (i + j) % 2;
				if(b[i][j] == 1) cur.count++;
			}
		}
		p[pCount++] = cur;
	}

	public boolean validate(int n, int m, String[] brd, int totalCount) {
		int[][] abrd = new int[brd.length][brd[0].length()];
		for(int i = 0; i < brd.length; i++) {
			for(int j = 0; j < brd[0].length(); j++) {
				abrd[i][j] = brd[i].charAt(j) - 'a' + 1;
			}
		}
		return validate(n, m, abrd, totalCount);
	}

	
	public boolean validate(int n, int m, int[][] a, int totalCount) {
		for(int i = 0; i < b.length; i++) {
			java.util.Arrays.fill(this.b[i], 0);
			java.util.Arrays.fill(this.f[i], 0);
			java.util.Arrays.fill(this.a[i], 0);
		}
		pCount = 0;
		this.n = n;
		this.m = m;
		this.a = a;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(f[i][j] == 0) {
					getPolyomino(i, j);
				}
			}
		}
		
		for(int i = 0; i < pCount; i++) {
			for(int j = 0; j < pCount; j++) {
				if(i == j) continue;
				if(p[i].equals(p[j])) {
					System.out.println("Failed!");
					System.out.println(p[i]);
					System.out.println(p[j]);
					return false;
				}
			}
		}
		
		return pCount == totalCount;
	}
	
	public static void main(String[] args) {
		PolyominoValidator pv = new PolyominoValidator();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int[][] a = new int[32][32];
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		int tc = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			String tmp = scanner.next();
			for(int j = 0; j < m; j++) {
				a[i][j] = tmp.charAt(j) - 'a' +1 ;
				//a[i][j] = scanner.nextInt();
			}
		}
		scanner.close();

		pv.validate(n, m, a, tc);
	}
	
}
