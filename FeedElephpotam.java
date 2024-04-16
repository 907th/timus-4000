public class FeedElephpotam {

	int n;
	int sx = 1000000000, sy = 1000000000;
	int flag;
	
	int vect(int x, int y, int x1, int y1, int x2, int y2) {
		int vx1 = x1 - x;
		int vy1 = y1 - y;

		int vx2 = x2 - x;
		int vy2 = y2 - y;
		
		return (vx1 * vy2) - (vx2 * vy1);
	}
	
	int Q(int x) {
		return x * x;
	}
	
	int dist(int x1, int y1, int x2, int y2) {
		return Q(x2 - x1) + Q(y2 - y1);
	}
	
	class Pumpkin implements Comparable<Pumpkin> {
		int x, y, id;

		@Override
		public int compareTo(FeedElephpotam.Pumpkin o) {
			int v = vect(sx, sy, x, y, o.x, o.y);
			if(v == 0) {
				int d1 = dist(sx, sy, x, y);
				int d2 = dist(sx, sy, o.x, o.y);
				return d2 - d1;
			}
			return v;
		}
		
		@Override
		public String toString() {
			return id + "";
		}
		
	}
	
	Pumpkin[] p = new Pumpkin[32768];
	int pSize = 0;
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			p[pSize] = new Pumpkin();
			p[pSize].x = x;
			p[pSize].y = y;
			p[pSize].id = i + 1;
			pSize++;
			if(sy == y) {
				if(sx > x) {
					sx = x;
					sy = y;
				}
			}
			if(sy > y) {
				sx = x;
				sy = y;
			}
		}
		scanner.close();
	}
	
	void reverse(int l, int r) {
		while(l < r) {
			Pumpkin h = p[l];
			p[l] = p[r];
			p[r] = h;
			l++;
			r--;
		}
	}
	
	void solve() {
		init();
		java.util.Arrays.sort(p, 0, pSize);
		
		int k = 1;
		while(true) {
			if(vect(sx, sy, p[0].x, p[0].y, p[k].x, p[k].y) != 0) break;
			k++;
		}
		reverse(0, k - 1);
		
		System.out.println(n);
		System.out.println(1);
		for(int i = 0; i < n; i++) {
			if(p[i].id == 1) {
				for(int j = i + 1; j < n; j++) {
					System.out.println(p[j].id);
				}
				break;
			}
		}
		for(int i = 0; i < n; i++) {
			if(p[i].id == 1) {
				break;
			}
			System.out.println(p[i].id);
		}
	}
	
	public static void main(String[] args) {
		FeedElephpotam fe = new FeedElephpotam();
		fe.solve();
	}
	
}