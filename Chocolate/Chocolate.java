public class Chocolate {

	public final double eps = 1e-7;
	
	public static double vect(double sx, double sy, double x1, double y1, double x2, double y2) {
		return (x1 - sx) * (y2 - sy) - (x2 - sx) * (y1 - sy);
	}
	
	public static double Q(double x) {
		return x * x;
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Q(x2 - x1) + Q(y2 - y1));
	}
	
	int n;
	double[][] a;
	
	double sx, sy;
	double fx, fy;
	
	double s = 0;
	
	double getLength(double x, double y, int l) {
		double totalSquare = 0;
		
		double lx = 0, ly = 0;
		double rx = 0, ry = 0;
		
		int cur = l;
		while(true) {
			int next = (cur + 1) % n;

			double currentVect = vect(x, y, a[0][cur], a[1][cur], a[0][next], a[1][next]) / 2.0;
			if(totalSquare <= s / 2.0 && totalSquare + currentVect >= s / 2.0) {
				lx = a[0][cur]; ly = a[1][cur];
				rx = a[0][next]; ry = a[1][next]; break;
			}
			cur = next;
			totalSquare += currentVect;
		}
		
		while(getDistance(lx, ly, rx, ry) > eps) {
			double mx = (lx + rx) / 2.0;
			double my = (ly + ry) / 2.0;
			
			double currentSquare = vect(x, y, a[0][cur], a[1][cur], mx, my) / 2.0;
			
			if(totalSquare + currentSquare < s / 2.0) {
				lx = mx; ly = my;
			} else {
				rx = mx; ry = my;
			}
			
		}
		sx = x; sy = y;
		fx = lx; fy = ly;
		
		return getDistance(x, y, lx, ly);
	}
	
	double findBest(double x1, double y1, double x2, double y2, int l) {
		while(getDistance(x1, y1, x2, y2) > eps) {
			double mx1 = x1 + (x2 - x1) / 3.0;
			double my1 = y1 + (y2 - y1) / 3.0;

			double mx2 = x1 + 2.0 * (x2 - x1) / 3.0;
			double my2 = y1 + 2.0 * (y2 - y1) / 3.0;

			double l1 = getLength(mx1, my1, l);
			double l2 = getLength(mx2, my2, l);
			
			if(l1 > l2) {
				x1 = mx1; y1 = my1;
			} else {
				x2 = mx2; y2 = my2;
			}
		}
		return getLength(x1, y1, l);
	}
	
	public double solve(double[][] a) {
		this.a = a;
		this.n = a[0].length;
		
		double best = Double.MAX_VALUE;
		double bsx = 0, bsy = 0, bfx = 0, bfy = 0;
		
		for(int i = 0; i < n - 1; i++) {
			s += vect(a[0][0], a[1][0], a[0][i], a[1][i], a[0][i + 1], a[1][i + 1]);
		}
		s = s / 2.0;
		
		for(int i = 0; i < n; i++) {
			double current = findBest(a[0][i], a[1][i], a[0][(i + 1) % n], a[1][(i + 1) % n], i);
			if(best > current) {
				best = current;
				bsx = sx;
				bsy = sy;
				bfx = fx;
				bfy = fy;
			}
		}
		
		//printDebugInformation(bsx, bsy, bfx, bfy);
		
		return best;
	}
	
	void printDebugInformation(double sx, double sy, double fx, double fy) {
		System.out.println("{");
		System.out.println("\"points\":[");
		for(int i = 0; i < n; i++) {
			if(i > 0) System.out.println(",");
			System.out.println("{");
			System.out.println("\"x\":" + a[0][i] + ",\"y\":" + a[1][i]);
			System.out.println("}");
		}
		System.out.println("],");
		System.out.println("\"cut\":{");
		System.out.println("\"sx\":" + sx + ",\"sy\":" + sy + ",\"fx\":" + fx + ",\"fy\":" + fy);
		System.out.println("}");
		System.out.println("}");
	}
	
	public static void main(String[] args) {
		Chocolate c = new Chocolate();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		double[][] a = new double[2][n];
		for(int i = 0; i < n; i++) {
			a[0][i] = Double.parseDouble(scanner.next());
			a[1][i] = Double.parseDouble(scanner.next());
		}
		scanner.close();
		double h = c.solve(a);
		System.out.println(h);
	}
	
}