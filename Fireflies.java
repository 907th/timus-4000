import java.util.HashMap;

public class Fireflies {

	int n;
	int[][] p = new int[3][2048];
	int best = 0;
	
	HashMap<String, Integer> hs = new HashMap<String, Integer>();
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			p[0][i] = scanner.nextInt();
			p[1][i] = scanner.nextInt();
			p[2][i] = scanner.nextInt();
		}
		scanner.close();
	}
	
	int gcd(int x, int y) {
		if(x == 0) return y;
		return gcd(y % x, x);
	}
	
	int[] h = new int[3];
	
	void shot(int x1, int y1, int z1, int x2, int y2, int z2) {
		
		int dx = x2 - x1;
		int dy = y2 - y1;
		int dz = z2 - z1;
		

		if(dx != 0 && dy != 0 && dz != 0) {
			int g = gcd(Math.abs(dx), gcd(Math.abs(dy), Math.abs(dz)));
			dx /= g;
			dy /= g;
			dz /= g;
		}
		
		if(dx == 0) {
			int g = gcd(Math.abs(dy), Math.abs(dz));
			dy /= g;
			dz /= g;
		}
		if(dy == 0) {
			int g = gcd(Math.abs(dx), Math.abs(dz));
			dx /= g;
			dz /= g;
		}
		if(dz == 0) {
			int g = gcd(Math.abs(dx), Math.abs(dy));
			dx /= g;
			dy /= g;
		}
		
		if(dx == 0 && dy == 0) {
			dz = 1;
		}
		if(dz == 0 && dx == 0) {
			dy = 1;
		}
		if(dy == 0 && dz == 0) {
			dx = 1;
		}

		h[0] = dx;
		h[1] = dy;
		h[2] = dz;
	}
	
	void solve() {
		init();
		int best = 1;
		for(int i = 0; i < n; i++) {
			hs.clear();
			for(int j = 0; j < n; j++) {
				if(i == j) continue;
				shot(p[0][i], p[1][i], p[2][i], 
					p[0][j], p[1][j], p[2][j]);
				
				String key = java.util.Arrays.toString(h);
				int cur = hs.getOrDefault(key, 0) + 1;
				if( best < cur + 1) {
					best = cur + 1;
				}
				hs.put(key, cur);
			}
		}
		System.out.println(best);
	}
	
	public static void main(String[] args) {
		Fireflies ff = new Fireflies();
		ff.solve();
	}
	
}