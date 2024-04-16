class DrunkKingPrebuilder {

	private int[][] d = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
	private char[] path = new char[] {'-', '|', '-', '|', '\\', '/', '/', '\\'};
	
	private int gn, gm;
	
	private int[][] f = new int[16][16];
	private char[][] c = new char[32][32];
	private int[] found = new int[8];
	private int startPipe = 0;
	
	private void clear() {
		
		for(int i = 0; i < found.length; i++)
			found[i] = 0;
		
		for(int i = 0; i < f.length; i++) {
			for(int j = 0; j < f[0].length; j++) {
				f[i][j] = 0;
			}
		}

		for(int i = 0; i < c.length; i++) {
			for(int j = 0; j < c[0].length; j++) {
				c[i][j] = ' ';
			}
		}
	}
	
	private boolean isOk(int x, int y) {
		if(x < 0 || y < 0 || x >= gn || y >= gm) return false;
		if(f[x][y] == 1) return false;
		return true;
	}
	
	private boolean print(int x, int y, int start, int finish, int pipeStart, int pipeFinish) {
		if((pipeFinish == 0 || pipeFinish == 2) && finish == 4) return false;
		System.out.print("\npb[" + x + "][" + y + "][" + start + "][" + finish + "][" + pipeStart + "][" + pipeFinish + "] = new String[] { \n");
		for(int i = 0; i < 2 * gn - 1; i++) {
			if(i == 0) System.out.print("\""); else System.out.print(",\n\"");
			for(int j = 0; j < 2 * gm - 1; j++) {
				System.out.print(c[i][j]);
			}
			System.out.print("\"");
		}
		System.out.println("};\n");
		return true;
	}
	
	private boolean rec(int x, int y, int last, int left, int fnx, int fny, int st, int fn) {
		f[x][y] = 1;
		c[2 * x][2 * y] = 'o';
		
		if(left == 1 && x == fnx && y == fny) {
			if(found[last] == 1) {
				c[2 * x][2 * y] = ' ';
				f[x][y] = 0;
				return false;
			}
			if(print(gn, gm, st, fn, startPipe, last) && fn == 4) return true;;
			found[last] = 1;
			int total = 0;
			for(int i = 0; i < found.length; i++)
				total += found[i];
			if(total > 1) {
				return true;				
			}
			c[2 * x][2 * y] = ' ';
			f[x][y] = 0;
			return false;
		}
		
		for(int i = 0; i < d.length; i++) {
			if(i == last) continue;
			if(isOk(x + d[i][0], y + d[i][1])) {
				if(c[2 * x + d[i][0]][2 * y + d[i][1]] == ' ') {
					c[2 * x + d[i][0]][2 * y + d[i][1]] = path[i];
					if(left == gn * gm) startPipe = i;
					if(rec(x + d[i][0], y + d[i][1], i, left - 1, fnx, fny, st, fn)) return true;
					c[2 * x + d[i][0]][2 * y + d[i][1]] = ' ';
				}
			}
		}
		c[2 * x][2 * y] = ' ';
		f[x][y] = 0;
		return false;
	}
	
	public void solve() {
		for(int i = 5; i <= 5; i++) {
			for(int j = 5; j <= 5; j++) {
				gn = i; gm = j;
				clear();
				rec(0, 0, 1, gn * gm, gn - 2, 0, 0, 3);
			}
		}

	}
	
	public static void main(String[] args) {
		DrunkKingPrebuilder dkp = new DrunkKingPrebuilder();
		dkp.solve();
	}
}