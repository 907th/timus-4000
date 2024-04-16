public class Chapaev {

	private final double eps = 1e-9;
	private final double originalR = 0.4;
	
	double[][] draughts = new double[16][2];
	
	int[][] moves = new int[16][1 << 16];
	int[] movesCount = new int[16];
	
	int[][] allowedMoves = new int[16][1 << 16];

	int[][] dp = new int[1 << 16][16];
	
	double Q(double x) {
		return x * x;
	}
	
	double D2(double x1, double y1, double x2, double y2) {
		return Q(x1 - x2) + Q(y1 - y2);
	}
	
	double[][] calc(double x1, double y1, double x2, double y2, double r) {
		double xc = (x1 + x2) / 2.0;
		double yc = (y1 + y2) / 2.0;

		double d2 = D2(xc, yc, x1, y1) - Q(r);
		
		double xk = -2 * x1 + 2 * xc;
		double yk = -2 * y1 + 2 * yc;
		double fk = Q(r) - d2 + Q(xc) - Q(x1) + Q(yc) - Q(y1);
		
		if(Math.abs(yk) < eps) {
			double ax = fk / xk;
			double ay1 =  Math.sqrt(d2 - Q(ax - xc)) + yc;
			double ay2 = -Math.sqrt(d2 - Q(ax - xc)) + yc;
			return new double[][] {{ax, ay1}, {ax, ay2}};
		}
		
		double ak = fk / yk - yc;
		double bk = xk / yk;
		
		double a = Q(bk) + 1;
		double b = -2 * ak * bk - 2 * xc;
		double c = Q(xc) + Q(ak) - d2;
		
		double dsc = Q(b) - 4 * a * c;
		if(Math.abs(dsc) < eps) dsc = 0;
		if(dsc < 0) {
			return new double[][] {}; 
		}
		dsc = Math.sqrt(dsc);
		
		double ax1 = (-b + dsc) / 2.0 / a;
		double ay1 = fk / yk - xk / yk * ax1;
		double ax2 = (-b - dsc) / 2.0 / a;
		double ay2 = fk / yk - xk / yk * ax2;
		
		return new double[][] {{ax1, ay1}, {ax2, ay2}};
	}
	
	double distance(double a, double b, double c, double x, double y) {
		return Math.abs((a * x + b * y + c) / Math.sqrt(Q(a) + Q(b)));
	}
	
	int checkLine(double x1, double y1, double x2, double y2, double sx, double sy, double tx, double ty) {
		double a = y2 - y1;
		double b = -(x2 - x1);
		double c = -(a * sx + b * sy);
		
		double ap = -b;
		double bp = a;
		double cp = -(ap * sx + bp * sy);
		
		int mask = 0;
		
		for(int i = 0; i < 16; i++) {
			
			double v1 = ap * draughts[i][0] + bp * draughts[i][1] + cp;
			double v2 = ap * tx + bp * ty + cp;
			
			if(Math.abs(v1) < eps) v1 = eps;
			if(Math.abs(v2) < eps) v2 = eps;
			
			if(distance(a, b, c, draughts[i][0], draughts[i][1]) <= 2 * originalR + eps && v1 * v2 >= 0) {
				mask |= (1 << i);
			}

		}

		return mask;
	}
	
	void init() {
		
		for(int i = 0; i < dp.length; i++) {
			for(int j = 0; j < dp[i].length; j++) {
				dp[i][j] = -2;
			}
		}
		
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		for(int i = 0; i < 16; i++) {
			String d1 = scanner.next();
			String d2 = scanner.next();
			draughts[i][0] = Double.parseDouble(d1);
			draughts[i][1] = Double.parseDouble(d2);
		}
		
		scanner.close();
	}
	
	void add(int index, int mask) {
		allowedMoves[index][mask] = 1;
	}
	
	double[][] lines = new double[256][4];
	int linesCount = 0;
	
	void addLines(double x1, double y1, double x2, double y2) {
		lines[linesCount][0] = x1;
		lines[linesCount][1] = y1;
		lines[linesCount][2] = x2;
		lines[linesCount++][3] = y2;
	}

	void tryMove(int index1, int index2) {
		
		if(index1 == index2) return;
		
		double x1 = draughts[index2][0], y1 = draughts[index2][1];
		double x2 = draughts[index1][0], y2 = draughts[index1][1];
		
		double[][] finish = calc(x1, y1, x2, y2, originalR);
		double[][] start = calc(x2, y2, x1, y1, originalR);
		for(int i = 0; i < finish.length; i++) {
			for(int j = 0; j < start.length; j++) {
				int mask = checkLine(start[j][0], start[j][1], finish[i][0], finish[i][1], x2, y2, x1, y1);
				add(index1, mask | (1 << index1));
			}
		}

		int mask = checkLine(x1, y1, x2, y2, x2, y2, x1, y1);
		add(index1, mask | (1 << index1));
	}
	
	int DP(int mask, int move) {
		
		if(dp[mask][move] != -2) return dp[mask][move];
		
		int start = 0, end = 8;
		if(move == 1) {
			start = 8; end = 16;
		}
		
		dp[mask][move] = -1;
		for(int i = start; i < end; i++) {
			if(((1 << i) & mask) != 0) {
				for(int j = 0; j < movesCount[i]; j++) {
					int currentMask = moves[i][j] & mask;
					int h = DP((mask ^ currentMask), 1 - move);
					if(h == -1) {
						dp[mask][move] = 1;
						return 1;
					}
				}
				
			}
		}
		return dp[mask][move];
	}
	
	void solve() {
		init();
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				tryMove(i, j);
			}
		}
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < (1 << 16); j++) {
				if(allowedMoves[i][j] == 1) {
					moves[i][movesCount[i]++] = j;
				}
			}
		}

		int w = DP((1 << 16) - 1, 0);
		
		if(w == 1) {
			System.out.println("RED");
		} else {
			System.out.println("WHITE");
		}
		
	}
	
	public static void main(String[] args) {
		Chapaev s = new Chapaev();
		s.solve();
	}
	
}