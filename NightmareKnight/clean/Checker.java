public class Checker {
	int[][] f = new int[1024][1024];

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

	boolean check(int[][] a, int x, int y, int left, int n, int m, int current) {
		while(true) {
			f[x][y] = 1;
			if(left <= 0) {
				for(int i = 0; i < n; i++) {
					for(int j = 0; j < m; j++) {
						if(f[i][j] == 0) {
							return false;
						}
					}
				}
				return true;
			}
			
			int nx = -1;
			int ny = -1;
			for(int i = 0; i < moves.length; i++) {
				nx = x + moves[i][0];
				ny = y + moves[i][1];
				if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
				if(a[nx][ny] == current + 1) break;
				nx = -1;
				ny = -1;
			}
			if(nx < 0 || ny < 0) {
				System.out.println("!!" + current);
				return false;
			}
			
			x = nx; y = ny;
			left--;
			current++;
		}
	}
	
	public boolean checkSolution(int n, int m, int[][] answer) {
		for(int i = 0; i < f.length; i++) {
			for(int j = 0; j < f.length; j++) {
				f[i][j] = 0;
			}
		}
		int ox = 0, oy = 0;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(answer[i][j] == 1) {
					ox = i; oy = j;
				}
			}
		}
		boolean result = check(answer, ox, oy, n * m - 1, n, m, 1);
		//System.out.println("Checking result for " + n + " " + m + " is " + result);
		return result;
	}
}
