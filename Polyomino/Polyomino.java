class PolyominoFactory {
	
	int totalCount = 0;
	Polyomino[] a = new Polyomino[2048];
	
	int[][] b = new int[16][16];
	
	private void tryToAdd(Polyomino p) {
		boolean found = false;
		for(int i = 0; i < totalCount; i++) {
			if(a[i].equals(p)) {
				found = true; break;
			}
		}
		if(!found) {
			a[totalCount++] = p;
		}
	}
	
	private void add() {
		int mnX = 1000, mnY = 1000, mxX = -1, mxY = -1;
		int count = 0;
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b[i].length; j++) {
				if(b[i][j] == 0) continue;
				count++;
				if(mnX > i) mnX = i;
				if(mxX < i) mxX = i;
				if(mnY > j) mnY = j;
				if(mxY < j) mxY = j;
			}
		}
		Polyomino p = new Polyomino();
		p.n = mxX - mnX + 1;
		p.m = mxY - mnY + 1;
		p.count = count;
		for(int i = mnX; i <= mxX; i++) {
			for(int j = mnY; j <= mxY; j++) {
				p.a[i - mnX][j - mnY] = b[i][j];
			}
		}
		for(int i = 0; i < p.n; i++) {
			for(int j = 0; j < p.m; j++) {
				p.c[i][j] = (i + j) % 2;
			}
		}
		
		tryToAdd(p);
		
		p = new Polyomino();
		p.n = mxX - mnX + 1;
		p.m = mxY - mnY + 1;
		p.count = count;
		for(int i = mnX; i <= mxX; i++) {
			for(int j = mnY; j <= mxY; j++) {
				p.a[i - mnX][j - mnY] = b[i][j];
			}
		}
		for(int i = 0; i < p.n; i++) {
			for(int j = 0; j < p.m; j++) {
				p.c[i][j] = (i + j + 1) % 2;
			}
		}
		
		tryToAdd(p);
	}
	
	private int V(int x, int y) {
		if(x < 0 || y < 0) return 0;
		return b[x][y];
	}
	
	private void generate(Polyomino p) {
		for(int i = 0; i < b.length; i++) {
			java.util.Arrays.fill(b[i], 0);
		}
		
		for(int i = 1; i <= p.n; i++) {
			for(int j = 1; j <= p.m; j++) {
				b[i][j] = p.a[i - 1][j - 1];
			}
		}
		
		for(int i = 0; i < p.n + 2; i++) {
			for(int j = 0; j < p.m + 2; j++) {
				int h = V(i - 1, j) + V(i + 1, j) + V(i, j - 1) + V(i, j + 1);
				if(b[i][j] == 0 && h > 0) {
					b[i][j] = 1;
					add();
					b[i][j] = 0;
				}
			}
		}
		
	}
	
	public Polyomino[] generatePolyominos(int maxSize) {
		int current = 0;
		Polyomino one = new Polyomino();
		one.count = 1;
		one.n = 1; one.m = 1;
		one.a[0][0] = 1; one.c[0][0] = 0;
		a[totalCount++] = one;
		
		Polyomino two = new Polyomino();
		two.count = 1;
		two.n = 1; two.m = 1;
		two.a[0][0] = 1; two.c[0][0] = 1;
		a[totalCount++] = two;
				
		while(true) {
			if(a[current].count == maxSize) break;
			generate(a[current]);
			current++;
			if(current >= totalCount) break;
		}
		
		Polyomino[] answer = new Polyomino[totalCount];
		for(int i = 0; i < totalCount; i++) {
			answer[i] = a[i];
			calculateScore(answer[i]);
		}
		
		return answer;
	}
	
	private int scoreValue(int x, int y) {
		if(x < 0 || y < 0) return 0;
		return scoreField[x][y] == -1 ? 1 : 0;
	}
	
	int[][] scoreField = new int[16][16];
	private void calculateScore(Polyomino p) {
		
		for(int i = 0; i < scoreField.length; i++)
			java.util.Arrays.fill(scoreField[i], 0);
		
		for(int i = 1; i <= p.n; i++) {
			for(int j = 1; j <= p.m; j++) {
				scoreField[i][j] = -p.a[i - 1][j - 1];
			}
		}
		for(int i = 0; i < p.n + 2; i++) {
			for(int j = 0; j < p.m + 2; j++) {
				if( scoreField[i][j] == 0) {
					scoreField[i][j] = scoreValue(i + 1, j) + scoreValue(i - 1, j) + scoreValue(i, j + 1) + scoreValue(i, j - 1);
				}
			}
		}

		int count4 = 0;
		int count3 = 0;
		int count2 = 0;
		int count1 = 0;
		for(int i = 0; i < scoreField.length; i++) {
			for(int j = 0; j < scoreField[i].length; j++) {
				count4 += scoreField[i][j] == 4 ? 1 : 0;
				count3 += scoreField[i][j] == 3 ? 1 : 0;
				count2 += scoreField[i][j] == 2 ? 1 : 0;
				count1 += scoreField[i][j] == 1 ? 1 : 0;
			}
		}
		
		p.score = count4 * 1000 + count3 * 100 + count2 * 10 + count1;
		
	}
	
}

public class Polyomino implements Comparable<Polyomino> {

	int n, m;
	int[][] a = new int[8][8];
	int[][] c = new int[8][8];
	
	int count;
	int score;
	
	public boolean isTheSame(Polyomino p) {
		if(p.n == n && p.m == m) {
			if(p.count != count) return false; 
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < m; j++) {
					if(a[i][j] != p.a[i][j] || c[i][j] != p.c[i][j]) return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		Polyomino p = (Polyomino)o;
		for(int i = 0; i < 4; i++) {
			if(isTheSame(p)) return true;
			p = p.rotate();
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(a[i][j] == 1) {
					if(c[i][j] == 1) {
						sb.append('O');
					} else {
						sb.append('o');
					}
				} else {
					sb.append(' ');
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public Polyomino rotate() {
		Polyomino r = new Polyomino();
		r.n = m;
		r.m = n;
		r.count = count;
		for(int i = 0; i < r.n; i++) {
			for(int j = 0; j < r.m; j++) {
				r.a[i][j] = a[j][r.n - i - 1];
				r.c[i][j] = c[j][r.n - i - 1];
			}
		}
		return r;
	}
	
	public static void main(String[] args) {
		
		int[] c = new int[8];
		java.util.Arrays.fill(c, 0);
		
		PolyominoFactory pf = new PolyominoFactory();
		Polyomino[] p = pf.generatePolyominos(6);
		for(int i = 0; i < p.length; i++) {
			c[p[i].count]++;
		}
		
		for(int i = 0; i < p.length; i++) {
			if(p[i].count <= 4) {
				System.out.println(p[i]);
			}
		}
		
		int total = 0;
		for(int i = 0; i < 8; i++) {
			total += i * c[i];
			System.out.println(i + ": " + c[i]);
		}
		System.out.println(total);
	}

	@Override
	public int compareTo(Polyomino o) {
		if(count != o.count) return count - o.count;
		return o.score - score;
	}
	
}
