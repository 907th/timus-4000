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

class PolyominoValidator {

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
	
	public static void main(String args) {
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
			}
		}
		scanner.close();

		pv.validate(n, m, a, tc);
	}
	
}

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

class Polyomino implements Comparable<Polyomino> {

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
	
	public static void main(String args) {
		
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

class LoydProblem {

	Polyomino[] p;
	int[] searchDirection = new int[32];
	int[] f = new int[2048];
	int totalCount = 0;
	
	int sizeN, sizeM;
	int[][] a = new int[32][32];
	int leftCount = 0;
	
	int[][] b = new int[32][32];
	int islandSize = 0;
	
	int[] rotationsCount = new int[2048];

	public String getString(int[][] v) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sizeN; i++) {
			for(int j = 0; j < sizeM; j++) {
				sb.append(v[i][j] == 0 ? '0' : '1');
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private void relax(int x, int y) {
		if(x < 0 || y < 0 || x >= sizeN || y >= sizeM) return;
		if(b[x][y] == 1 || a[x][y] != 0) return;
		recIsland(x, y);
	}
	
	private void recIsland(int x, int y) {
		islandSize++;
		b[x][y] = 1;
		relax(x - 1, y);
		relax(x + 1, y);
		relax(x, y - 1);
		relax(x, y + 1);
	}
	
	private boolean failedSolution() {
		for(int i = 0; i < b.length; i++) {
			java.util.Arrays.fill(b[i], 0);
		}
		
		for(int i = 0; i < sizeN; i++) {
			for(int j = 0; j < sizeM; j++) {
				if(a[i][j] == 0 && b[i][j] == 0) {
					islandSize = 0;
					recIsland(i, j);
					if(islandSize != leftCount) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private int A(int x, int y) {
		if(x < 0 || y < 0 || x >= sizeN || y >= sizeM) return 1;
		return a[x][y] != 0 ? 1 : 0;
	}
	
	private boolean okToSet(int x, int y, int fromX, int fromY, Polyomino p) {
		if(fromX < 0 || fromY < 0) return false;
		if((fromX + fromY) % 2 != p.c[0][0]) return false;
		if(p.a[x - fromX][y - fromY] != 1) return false;
		if(fromX + p.n - 1 >= sizeN || fromY + p.m - 1 >= sizeM) return false;
		for(int i = 0; i < p.n; i++) {
			for(int j = 0; j < p.m; j++) {
				if(p.a[i][j] != 0 && a[fromX + i][fromY + j] != 0) return false;
			}
		}
		return true;
	}
	
	private void set(int fromX, int fromY, Polyomino p, int id) {
		for(int i = 0; i < p.n; i++) {
			for(int j = 0; j < p.m; j++) {
				if(p.a[i][j] == 1) {
					a[fromX + i][fromY + j] = id;
				}
			}
		}
	}
	
	private void unset(int fromX, int fromY, Polyomino p) {
		for(int i = 0; i < p.n; i++) {
			for(int j = 0; j < p.m; j++) {
				if(p.a[i][j] == 1) {
					a[fromX + i][fromY + j] = 0;
				}
			}
		}
	}
	
	private boolean rec(int minSize, int maxSize, int leftToSet) {
		
		int x = -1, y = -1, mx = -1;
		for(int i = 0; i < sizeN; i++) {
			for(int j = 0; j < sizeM; j++) {
				if(a[i][j] != 0) continue;
				
				int h = 0;
				int searchSize = 2;
				for(int kx = -searchSize; kx <= searchSize; kx++) {
					for(int ky = -searchSize; ky <= searchSize; ky++) {
						h = h + A(i + kx, j + ky);
					}
				}

				h = h * 1000 + (1000 - (i + 2 * j) * searchDirection[sizeN]);
				
				if(mx < h) {
					mx = h;
					x = i;
					y = j;
				}
				
			}
		}
		
		if(failedSolution()) {
			return false;
		}
		if(mx < 0) return true;
		if(leftToSet == 0) return true;
		
		boolean tried = false;
		for(int k = 0; k < p.length; k++) {
			if(!(minSize <= p[k].count && p[k].count <= maxSize)) continue;
			if(f[k] != 0) continue;
			tried = true;
			Polyomino cur = p[k];
			for(int i = 0; i < cur.n; i++) {
				for(int j = 0; j < cur.m; j++) {
					
					for(int r = 0; r < rotationsCount[k]; r++) {
						if(okToSet(x, y, x - i, y - j, cur)) {
							set(x - i, y - j, cur, k + 1);
							leftCount -= cur.count;
							f[k]++;
							if(rec(minSize, maxSize, leftToSet - 1)) return true;
							f[k]--;
							leftCount += cur.count;
							unset(x - i, y - j, cur);
						}
						cur = cur.rotate();
					}
					
				}
			}
		}
		if(!tried) return true;
		return false;
	}
	
	public int[][] solve(int sizeN, int sizeM) {
		p = (new PolyominoFactory()).generatePolyominos(6);
		this.sizeN = sizeN;
		this.sizeM = sizeM;
		
		java.util.Arrays.sort(p, 0, p.length);
		
		for(int i = 0; i < p.length; i++) {
			if(p[i].isTheSame(p[i].rotate())) rotationsCount[i] = 1; else
			if(p[i].isTheSame(p[i].rotate().rotate())) rotationsCount[i] = 2; else rotationsCount[i] = 4;
		}
		searchDirection[30] = 100;
		searchDirection[29] = 100;
		searchDirection[25] = 100;
		searchDirection[24] = 100;
		searchDirection[23] = 100;
		searchDirection[22] = 100;
		searchDirection[20] = 100;
		searchDirection[17] = 100;
		searchDirection[16] = 100;
		searchDirection[14] = 100;
		searchDirection[13] = 100;
		searchDirection[12] = 100;
		searchDirection[11] = 100;
		searchDirection[10] = 100;
		searchDirection[9] = 100;
		searchDirection[8] = 100;
		searchDirection[28] = 50;
		searchDirection[27] = 50;
		searchDirection[26] = 50;
		searchDirection[21] = 50;
		searchDirection[19] = 50;
		searchDirection[18] = 50;

		leftCount = sizeN * sizeM;
		int leftToSet = (leftCount - 236) / 6;
		if(leftToSet > 0 && (leftCount - 236) % 6 != 0) {
			leftToSet--;
			totalCount++;
		}
		if(leftToSet < 0) leftToSet = 0;
		totalCount += leftToSet;
		if(rec(6, 6, leftToSet)) {
			if(leftToSet == 0) {
				leftToSet = (leftCount - 56) / 5;
				if(leftToSet > 0 && (leftCount - 56) % 5 != 0) {
					leftToSet--;
					totalCount++;
				}
				if(sizeN < 12) {
					for(int i = 0; i < 8; i++) {
						for(int j = 0; j < 7; j++) {
							a[i][j] = 1;
						}
					}
					leftCount -= 56;
				}
			} else {
				leftToSet = 36;
			}
			totalCount += leftToSet;
			if(rec(5, 5, leftToSet)) {
				leftToSet = 17;
				if(sizeN < 12) {
					for(int i = 0; i < sizeN; i++) {
						for(int j = 0; j < sizeM; j++) {
							if(a[i][j] == 0) a[i][j] = 321;
						}
					}
					for(int i = 0; i < 8; i++) {
						for(int j = 0; j < 7; j++) {
							a[i][j] = 0;
						}
					}
					leftCount = 56;
					leftToSet = 17;
				}
				totalCount += leftToSet;
				if(rec(1, 4, leftToSet)) {
					/*for(int i = 0; i < sizeN; i++) {
						for(int j = 0; j < sizeM; j++) {
							System.out.printf("%4d", a[i][j]);
						}
						System.out.println();
					}*/
				}
			}
		}
		return a;
	}
	
	public static void main(String args) {
		PolyominoValidator pv = new PolyominoValidator();
		for(int i = 9; i <= 30; i++) {
			LoydProblem lp = new LoydProblem();
			int[][] a = lp.solve(i, i);
			if(pv.validate(i, i, a, lp.totalCount)) {
				Printer printer = new Printer();
				String s = printer.getSolution(i, a, lp.totalCount);
				System.out.println("answer[" + i + "] = \"" + s + "\";");
			}
		}
	}
	
}

public class LoydProblemSolver {

	public static void main(String[] args) {

		String[] answer = new String[32];

		answer[1] = "1\na";
		answer[2] = "3\nab\ncb";
		answer[3] = "4\nacd\nbcd\nddd";
		answer[4] = "7\nadfa\naefa\nabca\nbbcc";
		answer[5] = "9\nadfab\naefab\nabcab\nbbccb\naaaab";
		answer[6] = "12\naaaccd\nbaxydd\nbbhhde\nbqqhee\nqqzxxx\nzzzyyy";
		answer[7] = "15\naaacdee\nbaccdde\nbbkkkfe\nbggkkfl\ngglllfl\nhhhxiin\nhjjjjii";
		answer[8] = "18\naaaacaaa\nbbbcccba\nbaddabbb\naacdacba\naccdacaa\nbabdccbb\nbaaadbba\nbabbaaaa";

		answer[9] = "22\nabbbaabaa\nbabcadbaa\nbadccddab\nbadcaadbb\nccddaacba\naccbcccba\naaabbbdaa\nbccccddba\naaaaabbbb\n";
		answer[10] = "25\nabbbaabaaa\nbabcadbaaa\nbadccddaaa\nbadcaadbbb\nccddaacbab\naccbcccaaa\naaabbbabba\nbccccaacbb\naaaabcccab\nabbbbcaaaa\n";
		answer[11] = "30\nabbbaabaabb\nbabcadbcaab\nbadccddcadb\nbadcaadccdb\nccddaabcadd\naccebbbdadb\naaaeeeadaab\nbccccaadbab\naaaeeeddbcb\nabaeaebbbcb\nbbbbaaaaccc\n";
		answer[12] = "34\naaabbaaaabba\nabacbbcaddcc\nbbbcbacccdca\nabccaaabcaba\naaacbbabbaba\nbabbbccbdabb\nbbaacbccdccc\ncbbaabbbdabc\nccccaccedabb\naaaabcceeaab\ncacbbbaecccc\ncccbaaaacccc\n";
		answer[13] = "39\naaabbbabbbbab\nababcbaabcacc\nbbbccccaacacb\nabdddaabbcabb\naaaddbabbdddb\nbabbbbaacccda\nbbaaaddddcbba\nabbabbbbdbbca\naccaddbaacaca\naaccdaaaccacc\nabcddbddcaabb\nbbbaabbdeeebb\nbaaabbaaebbbb\n";
		answer[14] = "45\naaabbbaaabbaaa\nabababccaabbba\nbbbcaaacbbccca\nabcccbaccbaacb\naaacbbbbabbacb\nbaddddaabaacab\nbbaaadabbaccbb\nabbabaacbacaac\naccabbbccbddac\naaccbaaacbddac\nabcdaabcbbeeec\nbbbddbbcaaeaba\nbaddbbddabaaba\naaaadddbbbbaba\n";
		answer[15] = "50\naaabbbaaababbba\nabacbdabbbabaaa\nbbdcbdacbaabcab\nbbdcddccdacccbb\naadccdcabbcabba\naadabbcabaaacaa\nbadbbdddcabccca\nbbbaaabdcbbbcba\naabccbbbcaaabbb\naaacbaaacbbbaab\ncbbbbabbaabbbaa\nccdddddbabbbaba\ncabbaaabaccdabb\ncaabaaceeccdaab\naabbcccceedddab\n";
		answer[16] = "56\nabbaabbbaabbbbab\naabbababcaacbaab\nabbaacaaccaccabb\naadccccaacbccabc\nbbdddbbbdcabbccc\nabbdabbdddbbaaac\nabaaacccdcccabba\naabbacaabaaacbba\nacbbbcdabbcccaca\ncccddddaadddaaca\nacaaaabbccdbaccb\naabbcabbbcbbdddb\nabbaccccdaabddbb\nacbaaadddaccddda\ncccbbadcaabbbbba\ncbbbccccdddddaaa\n";
		answer[17] = "61\nabbaabbbbaaaabbba\naabbacbcbdaccbaaa\nabbaacccdddcabacb\naaccabcaadccacccb\nbbcddbbbabbbacabb\nbcccddbcacbaababa\nbbaaadccacbdddbcc\ncbcabaccbcccddbbc\ncccabaaabaaaaadbc\naaabbccabcccccddd\nacabaccdbbaaabaab\nbcccaacddddbbbaab\nbbcdaabbbaabbbbcb\nbdddcbbaccabbbccb\nbadcccaacdddaddca\naaabcaaddbaaacdda\nabbbbdddbbbbcccaa\n";
		answer[18] = "67\nabbaabbbbabbbaaabb\naabbacbcbaaabbaccb\nabbaacccddaccdacbb\naaccabcbbddccdccaa\nbbcddbbbabdcbdddab\nbcccdccaabaabbbaab\nbbadddccabcabddcbb\ncbaabbcdabcaadcccb\ncccaabddcbcccdacac\ncacabbddccaabdadbc\naabccaaaacaabbddcc\nabbbccbbacbacaabca\naabacbbbcabccaabba\nbcaaaacccabcaccbba\nbcccbbcdaaeeacbbbb\nbbcbbddddeebacdabc\nababccccabbbaddacc\naaaacaaaacccccdaac\n";
		answer[19] = "73\nabbaabbbbaaabbaabbb\naabbacbcbadaabbaaab\nabbaacccddddccbbcbb\naaccabcbaadacccdaac\nbbcddbbbabbaaaddaab\nbcccdbccaabacbdbccb\nbbdddaccbdbbcbbbcab\nababaaacbdddcbbbcab\naaabbaddbbbdccbbbaa\nacbbdddaaaaadddddbb\nccccabccccbcccaaaba\ncabaabbbbcbbbccacba\naabbacaaadddbaabcca\nabbdaccaadaaababcbb\naadddcbbcdaabbabaab\nbbdaacbccccbaabaacb\nbcccaabbabbbcbbdbcb\nbbcdaddaabdcccddbcc\nbccdddaaddddcddbbbc\n";
		answer[20] = "80\nabbaabbbbaaabbaaaabb\naabbacbcbacacbbacbba\nabbaacccddcccabccbaa\naaccabcbbddbaaabcdab\nbbcddbbbadbbbacbcdab\nbcccdcccaaabcccbbdbb\nbbadddcbbacaaacabddb\ncbaaeeccbbccabbaaacc\ncccaaeeebacbacbbddcc\ncacabbeaaacbccacddac\naadbbbbacbbbacacccaa\naddddaddcccaabadaaaa\naabdaaadddcaabbddabc\nbbbbcabbbeeeeabdbbbc\nbccccacbbaaaecccccac\naaacbcccdaababbbbbac\nacabbbbcdddbaaaacdaa\naccccabcbadbbbcccddb\nbcbaaaabbaadddcabbab\nbbbbabbbaaddaaaacaab\n";
		answer[21] = "87\nabbaabbbbaaabbaaabbaa\naabbacbcbacaabbbaabba\nabbaacccddccccbdacbda\naaccabcbdaaacdddccbda\nbbcddbbbdddaabdccddda\nbcccdbaacccbabdacbdcc\nbbdddccabcabbbaaabbbc\nababbcdabcacccbacbccc\naaabccdabaacbbbacbaab\nacbbdddbbabcabdccaabb\ncccaaaccddbaadddcabba\nccbaacccddbaabdaaccca\nbbbbcaaadbbccabbabcaa\nbaccccbaacccaabcabcda\naaddbbbbcaabdabcebddb\naddaaadcccabdddceedbb\naadabaddacaabaaceeacb\nbbdbbddaaabbbaaeebacc\nbaaabbccadbcccccabaac\nbbadbddcdddbbaaaabcbb\nbaadddccdbbbcccccaccb\n";
		answer[22] = "94\nabbaabbbbaaabbaaabbbaa\naabbacbcbadaabbbabcbab\nabbaacccddddccbaaccaab\naaccabcbaadbccabbbccbb\nbbcddbbbbaabbcaaabbaab\nbcccdaaacbaabbcacccaba\nbbadddaccbbddcccaacaba\ncbaabbaaccbbddcbbacaba\ncccaabbbcaabcdbbbaabba\ncacaccbdddaacaaaccbcca\naabbcccdadabccbadcaacc\naccbbcdaaabbcbbadccabb\naacbddddacbbabbdddbccb\ncccbdaabcccaaaabbbbcca\naaacaabbbbcbbcccadddba\naccccabaaaabccbbbbbaba\naabbcacaddbbaddaacaabb\ncccbbbcddbaaaadddccacc\ncacbcccdbbbbcccabcddca\ncaaaacbdabdccababbdaca\ndaddbbbaadddaabaabdaaa\ndddbbaaaddaaabbbccccaa\n";
		answer[23] = "101\nabbaabbbbaaaaabbaaababb\naabbacbcbadbbbbcccabaab\nabbaacccddddaaaccdababb\naaccabcbdbbbbbabbdbbaca\nbbcddbbbbabccaabdddccca\nbcccdcccaaaaccbbabbcdaa\nbbadddcbbabddccaabbbdab\ncbaabbccbbbddddbaaccdbb\ncccaabbbacccccacbbcadbb\ncacaccbaabbbcbacbccbdaa\naabccccabbaabbbcaddbbaa\nabbbbdaababaaabdaaddbca\naacbddddaabbacdddbbbacb\nccccadaaababcccdabccacb\ncaaaacbbbbabbcaaadcbaab\nbbbacccabcaaabadddcbbcb\nbabccaaaaccadbbbbdabccc\nbaaaabbdacddddaccaaaabb\ncaccbbadccbbaaaacaaaaba\ncccbbaadddabbddccaaccba\naaacddaadaaabcdddbbccda\naccccdbbbbdaccabbbeeeda\naacdddbddddccaaaaeeddda\n";
		answer[24] = "109\nabbaabbbbaaabbaaababbbbb\naabbacbcbadaabbbabaaaaba\nabbaacccddddccbaabbaccca\naaccabcbaadbcccdccbbcaaa\nbbcddbbbbaabbcddccaacbac\nbcccdaaacbaabbbddcaacbcc\nbbadddaccbbcccaabbabbbca\ncbaabbaaccbbaccaabcbacca\ncccaabbbcaabacbadbcaabaa\ncacaccbabaccaabbdbcabbba\naabccccabaacacbaddcadbca\nabbbbdaabbccdcbadbcddacc\naabddddacbdddcaaabedaaac\ncccaabdcccdabccbbbeaaaab\ncaaacbbbbcaabbaacceeacab\nccaccccbdaaccbbacccbbcbb\nbbbcadddddbacccaababbcaa\nbaaaaccbbbbaabdddbaadcba\nbbacccaccbaacbbbdbadddba\ndddcbaaacccbccabdccaabbc\ndadbbbbacabbbcaaacbaccac\ndaaaabcabaabdcaddcbdcaac\nbabbcccbbaddddbddcbccbdd\nbbbccbbbaadbbbbbdbbaaaaa\n";
		answer[25] = "117\nabbaabbbbaabbbbbaabbbaaab\naabbacbcbaccbcdddaaabbabb\nabbaacccdaacccdacccabdacb\naaccabcbddabbddaabcccdacb\nbbcddbbbbddabbbabbbaddcca\nbcccdaaacadacbaadabadbbca\nbbadddaccaaacccddabaacbaa\ncbaabbaaccbccdddbaabacbba\ncccaabbbcabbbbabbbabcccdd\ncacaccbaaaabaaaaabcbbadda\naabddccabbcccccbbccbaadaa\nabbbdcbbbabbbcabddccabaac\naabadcbcaaaabbabbdeeabccc\nccbaddccccacbdaaadeeebacb\naccaaacbdddccdddbdaabbaab\naaccabbbbaddcccdbbabacaab\nbadddddbaaadaaabbaabdccbb\nbaadcccccaabccacccbddaacc\nbbbbcdddddbbbcaccabbdbaca\naaaeeeaadbabaccbbabccbada\naeeebbabbbaaababbaccbbdda\naabbbaacbacccbabcddddadbb\ncccbdabcbaacdbacccaacabbb\ncddddbbccadcdbaacbaacaabb\nccdbbbccaadddbdddddccbbbb\n";
		answer[26] = "126\nabbaabbbbaaabbaaabbbabbbaa\naabbacbcbadaabbbabcbaacbba\nabbaacccddddccbaadcbacccaa\naaccabcbbadaaccdddccabcbbb\nbbcddbbbaaabaaccadcbbaaaba\nbcccdaaababbabbaaaddbccdba\nbbadddabbacbacbbabdaaacdda\ncbaaccaabbcbbccbdcbbabcdaa\ncccaacccbacccaccdccccbbccc\ncacabbcddaacbaaadaaaaabaac\naadbbaddaabbbbbaddbcccccaa\nadddbadbbaccccccbbbabbaabc\naaddbadcbdddddadbdaabcaabc\nbcccdaacbbaadaadddabbcccbc\nbccddaccbaabbbaaacaddddabd\nbbcddbdccabaabbccccdbaaadd\nabbadbdddabbabddaaaabbdddd\naaaabbbdbbbcaaadddacbbadaa\nbbbcccbdaddccccdabcccaabaa\nbddcccaaadabdcaaabbacaabca\nbbdddaadddabdddabbaaabbbcc\naaabdcccccabddccccabbaaabc\nacabbbbbcaabbacdbbbbaabbbc\naccccaaddacbdacddccccdbaaa\nbcbaaaddcccddaadccadddccca\nbbbbaddccdddaaddaaaaaddcca\n";
		answer[27] = "135\nabbaabbbbaaabbaabbaabaabbaa\naabbacbcbacaabbaabbabbaabaa\nabbaacccddccccbbabcaabbabba\naaccabcbbdaacbadabcadbcabda\nbbcddbbbdddaabaddcccdcccddd\nbcccdaaccceabbaadacddccaddb\nbbdddbaaaceaebacdaddaabaaab\nabccbbbacceeebacdabbaabaccb\naaacccbcaaaaacccaacbcabbcbb\nabadcabccabbdddcbacbccabccb\nbbddbaaaccabbbdbbbcbccaddda\nbdddbbbaacabccddbacdddadaaa\nbbabbcccbbaaacaaaacdbaadbac\ncaaaabbccbbbaccceaedbcbbbcc\ncccdabbcabddddaaeeebbcbacca\ncaadddbbaabbbddaaaabdccabaa\ncaddabaaabbbcccccbdddcbacaa\naabaabbccccacdddbbbbdabaccb\nabbbaabcabcaaaadddceeecadbb\nbbccadbbabbbbdaccccaeecbdda\ncccddddaabacaddddbaaaacbdaa\ncabbbdaacdacaaabbbccbddbbca\naaaabccccdaccadbaacabdaddcc\nbbabbcadddaacdddaccabdabddc\nbccdaaaadbbaedbaadaaccabbba\nbbcddddacbbeeebbddabccadaaa\nbcccdcccccbbebbddbbbbdddaaa\n";
		answer[28] = "144\nabbaabbbbaaabbaaabbbaaaaabba\naabbacbcbadaabbbabcbdddacbaa\nabbaacccddddccbaaccbdddccbac\naaccabcbbadaaccbbbcccabccbac\nbbcddbbbaaabaaccabbaaabbcbac\nbcccdaaababbabaaabcaacbbaccc\nbbadddabbacbabbbaaccccbaaabb\ncbaaccaabbcbbabdddddaaccabba\ncccaacccbacccabcdaaaacccbcba\ncacabbcddaacaaaccccdddbbbcaa\naadbbaddaabdabbbbacbddacbcab\nadddbadccabdbbaaaabbaaabacbb\naaddbadbcbbdddacccbbabbaacba\nbcceeaabccbadcccbdddddadddba\nbccceabbcabaaabbbccccaaadaaa\nbbcaeecbbaabcaabbaaacbbbcccc\ncbbaaecccacbcccccabbaaabaaaa\nccccaaacbacbbbdddabcadddaaaa\naacddddcbacbaaaadbbcccaabbcc\naaaabbddbbcccbabddaabbcacbbc\nbbbcbbbabdddbbabbbaabbcaccac\nbaacccbabddbbccccbcaddcbacac\nbbaaacaaaadaaabcacccdabbabaa\ncccbacbccbbbabbaaaacdaccabba\ncacbbbbcccbbacbdbbeedaacaabb\ncaaaaccaacdbecbddbaeebaccbaa\nbabcccaadddeeccdbbaebbbabbba\nbbbbcaaddeeeccddaaaabaaaabaa\n";
		answer[29] = "153\nabbaabbbbaabbbbbaaaaabbaaabbb\naabbacbcbaccbcacccadddbcaaabb\nabbaacccdaacccabbccddabccccca\naaccabcbddabbaaabcedaabbddaaa\nbbcddbbbbddabbbabbeeaaacdbbac\nbcccdaaacadacbddbeeebcccddbcc\nbbadddaccaaacccdddaabbccabbdc\ncbaabbaaccbccbbdaaaabbabadddc\ncccaabbbcbbbbabbbcdddbabaadaa\ncacababdddccbaaabcccdaabaccab\naadbbaaaadaccccaecceddabbcaab\nadddbbcaddaabbbaeeeeaeeeccdbb\naadadbccaaacccbbbdddaabedddba\nbbdadddcccbbaccdddabbabedcaaa\nabbaaaddaabbacbbbaaababbcccba\naabbacaaabbcaaabbbcabbabacabb\nbaccccabccccaddddcccaaacababb\nbaadcbbbbaacbbbbddcabbababbaa\nbbdddddbcaaddbbaaaaabbcdaacca\nabbeeeeeccaaddcccbccbacdbbbcc\naaaaebaaccbeddeacbbccacdabaaa\ncabbbbaadcbeeeeaccbbcaabaabba\nccccbeaadbbbcaaabbddddbbbabcc\naaaceeeedbcccccadbbbdcccccbcc\nabbbaaeddaaabddddaeeeeaaaddda\naabbaaabdacbbbccdaaabeaacdbba\ncccbdacbaacbbccccabdbbbbcbbba\ncddddccbbccaaaaddabdddcccbbba\nccdcccbbccaaddddbbbbdaaaaabbb\n";
		answer[30] = "163\nabbaabbbbaabbbbbabbbaabbbbbaab\naabbacbcbaccbcaaaacbbaaacbdaab\nabbaacccdaacccbbbaccbabbccdabb\naaccabcbddabbaaabbdcccbbccdabb\nbbcddbbbbddabbbabddaaddbbcdccc\nbcccdaaacadacbaadddaaadaabdcac\nbbadddaccaaacddbbbbbacdacbdaaa\ncbaabbaaccbcccdddbacccdacbbbac\ncccaabbbcbbbbcdbaaaaacdaccabcc\ncacaccbaaaccbabbbbbddcbaccaccb\naabddccabacccaaaccddddbbbdaabb\nabbbdcddbacddddaacccaabdddabab\naabadcbdbbbdabdeeeecaabcdbbbab\nccbaddbddbaaabbbbceeacccabcaaa\naccaaabdcaaddbaaaccccbbcacccbb\naaccadbbccccdcccaaabcbbbabacbc\nbaddddbaabcadddcdbbbaccaababbc\nbaacdaaabbbabbbcdcbaaacbbbaaac\nbbccccdabbcaaabcdcccabccacbbcc\nabbdcadddacaddbbdddcdbbddaaabb\naaddaadaaaccdddaaaaddbbadddccb\nbaadaadbaadcabdaccadaaaabbccab\nbacddaebbbdcabbbccbdcccccbbdaa\nbbcccceecbddabbccbbbbaadddadab\nabbcbeeecbddadddddaccbaccdaddb\naaaabbaaccccaabbbdaacbbccaaccb\nacccbbaabbbdddbbbaaccaaabccccb\nbbbccbdaabbbdddaddddbbcbbbccaa\nbaacddddccccbbaadebbbccaaaccaa\nbbaaaadccbbbbaaaeeeeccaabbbbba\n";

		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int h = scanner.nextInt();

		scanner.close();
		System.out.println(answer[h]);
	}

}