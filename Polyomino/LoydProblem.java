public class LoydProblem {

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
	
	public static void main(String[] args) {
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
