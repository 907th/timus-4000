class PossibleMove implements Comparable<PossibleMove> {
	
	int x, y;
	int score;
	int move;
	
	@Override
	public int compareTo(PossibleMove o) {
		if(o.score == score) {
			return 0;
		}
		return score < o.score ? -1 : 1;
	}
	
}

public class NightmareKnight {
	
	private static final int N = 16;
	
	private static long TIME_LIMIT = 5000;
	
	public static int[][] moves = new int[][] {
		{2, 1},
		{2, -1},
		{-2, 1},
		{-2, -1},
		{1, 2},
		{1, -2},
		{-1, 2},
		{-1, -2}
	};
	
	public static int[][][] neededParts = new int[][][] {
		{{0, 0}, {-2, -2}, {1, -2}, {-2, 1}, {1, 1}},
		{{-1, -1}, {1, 1}, {1, -2}, {-2, 1}, {-2, -2}},
		{{0, -1}, {1, 1}, {-2, -2}, {-2, 1}, {1, -2}},
		{{-1, 0}, {1, 1}, {-2, -2}, {1, -2}, {-2, 1}},
	};
	
	int[][] b = new int[N][N];
	int[][] f = new int[N][N];
	int[][] field = new int[N][N];
	
	long start = 0;
	
	public String output(int n, int m, int x, int y, int[][] t) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(i == x && y == j) {
					sb.append("X ");
				} else {
					sb.append(t[i][j]).append(" ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private int getScore(int n, int m, int x, int y) {
		int score = 0;
		for(int i = 0; i < moves.length; i++) {
			if(x + moves[i][0] < 0 || y + moves[i][1] < 0 || x + moves[i][0] >= n || y + moves[i][1] >= m) continue;
			score++;
		}
		return score;
	}
	
	private void DFS(int n, int m, int x, int y) {
		field[x][y] = 1;
		for(int i = 0; i < moves.length; i++) {
			int nx = x + moves[i][0];
			int ny = y + moves[i][1];
			if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
			if(field[nx][ny] == 1 || f[nx][ny] == 1) continue;
			DFS(n, m, nx, ny);
		}
	}
	
	private boolean checkState(int x, int y, int n, int m, int fR, int fC) {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				field[i][j] = 0;
			}
		}
		DFS(n, m, x, y);
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(field[i][j] == 0 && f[i][j] == 0) return false;
			}
		}
		
		if(fC >= 0) {
			int leftCount = 0;
			for(int i = 0; i < n; i++) {
				if(f[i][fC] == 0) leftCount++;
			}
			if(leftCount == 0) return false;
		}
		
		if(fR >= 0) {
			int leftCount = 0;
			for(int i = 0; i < m; i++) {
				if(f[fR][i] == 0) leftCount++;
			}
			if(leftCount == 0) return false;
		}
		
		if(fR >= 0 && fC >= 0) {
			if(f[fR][fC] != 0) return false;
		}
		
		return true;
	}
	
	private boolean rec(int n, int m, int x, int y, int fR, int fC, int left) {
		
		if(System.currentTimeMillis() - start > TIME_LIMIT) return false;
		
		f[x][y] = 1;
		if(left == 0) {
			b[x][y] = n * m;
			return true;
		}
		
		if(!checkState(x, y, n, m, fR, fC)) {
			f[x][y] = 0;
			return false;
		}
		
		PossibleMove[] possibleMoves = new PossibleMove[8];
		int movesCount = 0;
		for(int i = 0; i < moves.length; i++) {
			if(x + moves[i][0] < 0 || y + moves[i][1] < 0 || x + moves[i][0] >= n || y + moves[i][1] >= m) continue;
			if(f[x + moves[i][0]][y + moves[i][1]] != 0) continue;
			possibleMoves[movesCount] = new PossibleMove();
			possibleMoves[movesCount].x = x + moves[i][0];
			possibleMoves[movesCount].y = y + moves[i][1];
			possibleMoves[movesCount].move = i + 1;
			possibleMoves[movesCount].score = getScore(n, m, x + moves[i][0], y + moves[i][1]);
			movesCount++;
		}
		java.util.Arrays.sort(possibleMoves, 0, movesCount);
		
		for(int i = 0; i < movesCount; i++) {
			b[x][y] = n * m - left;
			boolean h = rec(n, m, possibleMoves[i].x, possibleMoves[i].y, fR, fC, left - 1);
			if(h) return true;
			b[x][y] = 0;
		}
		
		f[x][y] = 0;
		return false;
	}
	
	private boolean rec2(int n, int m, int x, int y, int x1, int y1, int fx1, int fy1, int x2, int y2, int fx2, int fy2, int current, int left, int toSubtract, int toMultiply) {
		
		if(System.currentTimeMillis() - start > TIME_LIMIT) return false;
		
		f[x][y] = 1;
		if(left == 0) {
			if(current == 0) {
				f[x][y] = 0;
				return false;
			}
			b[x][y] = (n * m - toSubtract) * toMultiply;
			return true;
		}
		
		if((f[fx1][fy1] == 1 || f[x2][y2] == 1) && current == 0 || f[fx2][fy2] == 1) {
			f[x][y] = 0;
			return false;
		}
		
		PossibleMove[] possibleMoves = new PossibleMove[8];
		int movesCount = 0;
		for(int i = 0; i < moves.length; i++) {
			if(x + moves[i][0] < 0 || y + moves[i][1] < 0 || x + moves[i][0] >= n || y + moves[i][1] >= m) continue;
			if(f[x + moves[i][0]][y + moves[i][1]] != 0) continue;
			possibleMoves[movesCount] = new PossibleMove();
			possibleMoves[movesCount].x = x + moves[i][0];
			possibleMoves[movesCount].y = y + moves[i][1];
			possibleMoves[movesCount].move = i + 1;
			possibleMoves[movesCount].score = getScore(n, m, x + moves[i][0], y + moves[i][1]);
			movesCount++;
		}
		java.util.Arrays.sort(possibleMoves, 0, movesCount);
		
		for(int i = 0; i < movesCount; i++) {
			b[x][y] = (n * m - left - toSubtract) * toMultiply;
			
			if(possibleMoves[i].x == fx1 && possibleMoves[i].y == fy1 && current == 0) {
				f[fx1][fy1] = 1;
				b[fx1][fy1] = (n * m - left + 1 - toSubtract) * toMultiply;
				boolean h = rec2(n, m, x2, y2, x1, y1, fx1, fy1, x2, y2, fx2, fy2, 1, left - 2, n * m - left + 1, -1);
				if(h) return true;
				f[fx1][fy1] = 1;
				b[fx1][fy1] = 0;
			} else {
				boolean h = rec2(n, m, possibleMoves[i].x, possibleMoves[i].y, x1, y1, fx1, fy1, x2, y2, fx2, fy2, current, left - 1, toSubtract, toMultiply);
				if(h) return true;
			}
			
			b[x][y] = 0;
		}
		
		f[x][y] = 0;
		return false;
	}
	
	public int[][] bruteForce2(
			int n, int m, 
			int startX1, int startY1, 
			int finishX1, int finishY1,
			int startX2, int startY2, 
			int finishX2, int finishY2
			) {
		start = System.currentTimeMillis();
		
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b[i].length; j++) {
				b[i][j] = 0;
				f[i][j] = 0;
			}
		}
		
		if(rec2(n, m, startX1, startY1, startX1, startY1, finishX1, finishY1, startX2, startY2, finishX2, finishY2,
				0,
				n * m - 1,
				0, 1)) {
			int[][] answer = new int[n][m];
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < m; j++) {
					answer[i][j] = b[i][j];
				}
			}
			return answer;
		}
		
		return null;
	}
	
	public int[][] bruteForce(int n, int m, int startX, int startY, int finishRow, int finishColumn) {
		
		start = System.currentTimeMillis();
		
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b[i].length; j++) {
				b[i][j] = 0;
				f[i][j] = 0;
			}
		}
		
		if(rec(n, m, startX, startY, finishRow, finishColumn, n * m - 1)) {
			int[][] answer = new int[n][m];
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < m; j++) {
					answer[i][j] = b[i][j];
				}
			}
			return answer;
		}
		
		return null;
	}
	
	public static int[] getData() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		scanner.close();
		return new int[] {n, m};
	}
	
	public static void output(int w, int h, int[][] ans, int sr, int sc, int fr, int fc) {
		System.out.print("storePreb(" + w + ", " + h + ", " + sr + ", " + sc + ", " + fr + ", " + fc + ",  new int[] {");
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				System.out.printf("%d", ans[i][j]);
				if(!(i == w - 1 && j == h - 1)) System.out.print(", ");
			}
		}
		System.out.println("});");
	}
	
	public static void BF() {

		for(int w = 5; w <= 8; w++) {
			for(int h = 5; h <= 8; h++) {
				
				NightmareKnight nk = new NightmareKnight();

				for(int i = 0; i < neededParts.length; i++) {
					for(int j = 1; j < neededParts[i].length; j++) {
						
						int sx = neededParts[i][0][0];
						int sy = neededParts[i][0][1];
						if(sx < 0) sx += w;
						if(sy < 0) sy += h;
						
						int fx = neededParts[i][j][0];
						int fy = neededParts[i][j][1];
						if(fx < 0) fx += w;
						if(fy < 0) fy += h;
						
						int[][] answer = nk.bruteForce(w, h, 
								sx, sy, 
								fx, fy);
						if(answer != null) {
							output(w, h, answer, sx, sy, fx, fy);
						}
					}
				}
								
			}
		}
		
	}

	public static void solveSingle(int n, int m) {
		NightmareKnight nk = new NightmareKnight();
		int w = n, h = n;
		int[][] answer = nk.bruteForce(w, h, 
				0, 0, 
				-1, -1);
		if(answer != null) {
			for(int i = 0; i < w; i++) {
				for(int j = 0; j < h; j++) {
					System.out.printf(" %d", answer[i][j]);
				}
				System.out.print("\\n");
			}
			System.out.println();
		}
	}
	
	public static void prebuilds() {

		for(int w = 6; w <= 9; w++) {
			for(int h = 6; h <= 9; h++) {
				if(w % 2 == 1 && h % 2 == 1) continue;
				NightmareKnight nk = new NightmareKnight();
				int[][] answer = nk.bruteForce(w, h, 
						0, 0, 
						1, 2);
				if(answer != null) {
					output(w, h, answer, 0, 0, 0, 0);
				}
			}
		}
	}

	public static void prebuildPass(int w, int h) {
		NightmareKnight nk = new NightmareKnight();

		int[][] answer = nk.bruteForce2(w, h, 
				0, 0, 
				w - 2, h - 2,
				0, h - 1,
				w - 2, 1
				);

		if(w % 2 == 0) {
			answer = nk.bruteForce2(w, h, 
					1, 1, 
					0, h - 1,
					w - 1, 0,
					w - 2, h - 2
					);
		}

		if(answer != null) {
			System.out.println("pass[" + w + "][" + h + "][0] = new int[][] {");
			for(int i = 0; i < w; i++) {
				System.out.print("{");
				for(int j = 0; j < h; j++) {
					System.out.printf("%d", answer[i][j]);
					if(j != h - 1) System.out.printf(", "); 
				}
				System.out.print("}");
				if(i != w - 1) System.out.print(",");
				System.out.println();
			}
			System.out.println("};\n");
		}
		
		if(w % 2 == 0) return;
		
		answer = nk.bruteForce2(w, h, 
				1, 1, 
				w - 1, h - 1,
				w - 1, 0,
				1, h - 2
				);
		
		if(answer != null) {
			System.out.println("pass[" + w + "][" + h + "][1] = new int[][] {");
			for(int i = 0; i < w; i++) {
				System.out.print("{");
				for(int j = 0; j < h; j++) {
					System.out.printf("%d", answer[i][j]);
					if(j != h - 1) System.out.printf(", "); 
				}
				System.out.print("}");
				if(i != w - 1) System.out.print(",");
				System.out.println();
			}
			System.out.println("};");
		}
	}
	
	public static void prebuildLeftEdge(int w, int h) {
		NightmareKnight nk = new NightmareKnight();

		int[][] answer = nk.bruteForce(w, h, 
				0, h - 1, 
				w - 2, h - 2);

		if(answer != null) {
			System.out.println("leftEdge[" + w + "][" + h + "][0] = new int[][] {");
			for(int i = 0; i < w; i++) {
				System.out.print("{");
				for(int j = 0; j < h; j++) {
					System.out.printf("%d", answer[i][j]);
					if(j != h - 1) System.out.printf(", "); 
				}
				System.out.print("}");
				if(i != w - 1) System.out.print(",");
				System.out.println();
			}
			System.out.println("};\n");
		}
		
		answer = nk.bruteForce(w, h, 
				1, h - 2, 
				w - 1, h - 1);
		
		if(answer != null) {
			System.out.println("leftEdge[" + w + "][" + h + "][1] = new int[][] {");
			for(int i = 0; i < w; i++) {
				System.out.print("{");
				for(int j = 0; j < h; j++) {
					System.out.printf("%d", answer[i][j]);
					if(j != h - 1) System.out.printf(", "); 
				}
				System.out.print("}");
				if(i != w - 1) System.out.print(",");
				System.out.println();
			}
			System.out.println("};");
		}
	}

	public static void prebuildRightEdge(int w, int h) {
		NightmareKnight nk = new NightmareKnight();

		int[][] answer = nk.bruteForce(w, h, 
				w - 2, 1,
				0, 0 
				);

		if(answer != null) {
			System.out.println("rightEdge[" + w + "][" + h + "][0] = new int[][] {");
			for(int i = 0; i < w; i++) {
				System.out.print("{");
				for(int j = 0; j < h; j++) {
					System.out.printf("%d", answer[i][j]);
					if(j != h - 1) System.out.printf(", "); 
				}
				System.out.print("}");
				if(i != w - 1) System.out.print(",");
				System.out.println();
			}
			System.out.println("};\n");
		}
		
		answer = nk.bruteForce(w, h, 
				w - 1, 0,
				1, 1 
				);
		
		if(answer != null) {
			System.out.println("rightEdge[" + w + "][" + h + "][1] = new int[][] {");
			for(int i = 0; i < w; i++) {
				System.out.print("{");
				for(int j = 0; j < h; j++) {
					System.out.printf("%d", answer[i][j]);
					if(j != h - 1) System.out.printf(", "); 
				}
				System.out.print("}");
				if(i != w - 1) System.out.print(",");
				System.out.println();
			}
			System.out.println("};");
		}
	}

	public static void buildPrebuild() {
		int w = 6;
		prebuildPass(w, 6);
		prebuildLeftEdge(w, 5);
		prebuildRightEdge(w, 5);
		prebuildLeftEdge(w, 6);
		prebuildRightEdge(w, 6);
		prebuildLeftEdge(w, 7);
		prebuildRightEdge(w, 7);
		prebuildLeftEdge(w, 8);
		prebuildRightEdge(w, 8);
	}
	
	public static void main(String[] args) {
		//BF();
		prebuilds();
	}
	
}