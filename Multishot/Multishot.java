public class Multishot {

	public static double vect(double xs, double ys, double x1, double y1, double x2, double y2) {
		return (x1 - xs) * (y2 - ys) - (y1 - ys) * (x2 - xs);
	}

	public static double[] getK(double x, double y, double r) {
		double a = r * r - x * x;
		double b = 2 * x * y;
		double c = r * r - y * y;
		
		double d = b * b - 4 * a * c;
		
		if(d < 0) return new double[] {};
		
		double k1 = (-b + Math.sqrt(d)) / 2.0 / a;
		double k2 = (-b - Math.sqrt(d)) / 2.0 / a;
		
		return new double[] {k1, k2};
	}
	
	public static double[] rotateAnother(double x, double y, int i, int k, int p) {
		double angle = (i / (k - 1.0) - 0.5) * p;
		double sinx = angle;
		double cosx = 1.0;
		double nx = x *  cosx + y * sinx;
		double ny = x * -sinx + y * cosx;
		double distanceLocal = Math.sqrt(1 + angle * angle);
		distanceLocal = 1;
		return new double[] {nx / distanceLocal, ny / distanceLocal};
	}
	
	class Cow implements Comparable<Cow> {
	
		double x, y, r;
		
		public Cow() {
			
		}
		
		public Cow(double x, double y, double r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}

		@Override
		public int compareTo(Cow o) {
			double v = vect(sx, sy, x, y, o.x, o.y);
			if(v < 0) return -1;
			if(v > 0) return 1;
			return 0;
		}

	}
	
	double sx = 150000, sy = 150000;
	int n, k, p;
	Cow[] cows = new Cow[50004];

	int m;
	double[] vx = new double[50004 * 32];
	double[] vy = new double[50004 * 32];
	
	private void addDirection(double x, double y) {
		vx[m] = x;
		vy[m] = y;
		m++;
	}
	
	private double Q(double x) {
		return x * x;
	}
	
	private double getAngle(double x1, double y1, double x2, double y2) {
		return (x1 * x2 + y1 * y2) / Math.sqrt(x1 * x1 + y1 * y1) / Math.sqrt(x2 * x2 + y2 * y2);
	}
	
	private double getDistance(double a, double b, double x, double y) {
		return Q(Math.abs(a * x + b * y)) / (a * a + b * b);
	}
	
	private int checkCountFor(double x, double y) {
		int count = 0;
		double a = -y;
		double b = x;
		if(n < 20) {
			for(int i = 0; i < n; i++) {
				if(getDistance(a, b, cows[i].x, cows[i].y) < Q(cows[i].r) + 1e-5 && getAngle(x, y, cows[i].x, cows[i].y) < Math.PI && getAngle(x, y, cows[i].x, cows[i].y) > 0) {
					count++;
					if(count >= 5) return 5;
				}
			}
		} else {
			
			int lim = 10;
			
			int l = 0;
			int r = n;
			while(r - l > 1) {
				int m = (r + l) / 2;
				if(vect(sx, sy, x, y, cows[m].x, cows[m].y) < 0) {
					l = m;
				} else {
					r = m;
				}
			}

			boolean flag = true;
			for(int i = Math.max(l - lim, lim); i < Math.min(n - lim, l + lim); i++) {
				if(getDistance(a, b, cows[i].x, cows[i].y) < Q(cows[i].r) + 1e-5 && getAngle(x, y, cows[i].x, cows[i].y) < Math.PI && getAngle(x, y, cows[i].x, cows[i].y) > 0) {
					count++;
					if(count >= 5) return 5;
					flag = false;
				} else {
					if(!flag) break;
				}
			}
			if(count >= 5) return 5;
			
			flag = true;
			for(int i = 0; i < lim; i++) {
				if(getDistance(a, b, cows[i].x, cows[i].y) < Q(cows[i].r) + 1e-5 && getAngle(x, y, cows[i].x, cows[i].y) < Math.PI && getAngle(x, y, cows[i].x, cows[i].y) > 0) {
					count++;
					if(count >= 5) return 5;
					flag = false;
				} else {
					if(!flag) break;
				}
			}
			if(count >= 5) return 5;

			flag = true;
			for(int i = n - lim; i < n; i++) {
				if(getDistance(a, b, cows[i].x, cows[i].y) < Q(cows[i].r) + 1e-5 && getAngle(x, y, cows[i].x, cows[i].y) < Math.PI && getAngle(x, y, cows[i].x, cows[i].y) > 0) {
					count++;
					if(count >= 5) return 5;
					flag = false;
				} else {
					if(!flag) break;
				}
			}
			if(count >= 5) return 5;
		}
		return count;
	}
	
	public void solve() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		k = scanner.nextInt();
		p = scanner.nextInt();
		double r = Double.parseDouble(scanner.next());
		double x = Double.parseDouble(scanner.next());
		double y = Double.parseDouble(scanner.next());
		for(int i = 0; i < n; i++) {
			double xc = Double.parseDouble(scanner.next()) - x;
			double yc = Double.parseDouble(scanner.next()) - y;
			if(sy == yc) {
				if(sx > xc) {
					sx = xc;
				}
			}
			if(sy > yc) {
				sx = xc;
				sy = yc;
			}
			cows[i] = new Cow(xc, yc, r);
		}
		scanner.close();
		java.util.Arrays.sort(cows, 0, n);
		
		for(int i = 0; i < n; i++) {
			double[] resp = getK(cows[i].x, cows[i].y, cows[i].r);
			for(int j = 0; j < k; j++) {

				double vectX = 1;
				double vectY = resp[0];
				if(cows[i].x < 0) vectX = -vectX;
				if(cows[i].x < 0) vectY = -vectY;
				
				double[] nv = rotateAnother(vectX, vectY, j, k, 1);
				addDirection(nv[0], nv[1]);

				vectX = 1;
				vectY = resp[1];
				if(cows[i].x < 0) vectX = -vectX;
				if(cows[i].x < 0) vectY = -vectY;

				nv = rotateAnother(vectX, vectY, j, k, 1);
				//addDirection(nv[0], nv[1]);
			}
		}

		double xBest = 0;
		double yBest = 0;
		double probBest = 0.0;
		for(int i = 0; i < m; i++) {
			
			double currentProb = 0;
			for(int j = 0; j < k; j++) {
				double[] nv = rotateAnother(vx[i], vy[i], j, k, 1);
				int h = checkCountFor(nv[0], nv[1]);
				if(h >= 1) currentProb += 1; 
				if(h >= 2) currentProb += p / 100.0; 
				if(h >= 3) currentProb += Math.pow(p / 100.0, 2); 
				if(h >= 4) currentProb += Math.pow(p / 100.0, 3); 
				if(h >= 5) currentProb += Math.pow(p / 100.0, 4);
			}
			if( probBest < currentProb) {
				probBest = currentProb;
				xBest = vx[i];
				yBest = vy[i];
			}
		}
		
		double dist = Math.sqrt(xBest * xBest + yBest * yBest);
		xBest = xBest / dist;
		yBest = yBest / dist;
	
		System.out.println("The best shot at (" + xBest + ";" + yBest + ") gives Vasya " + probBest + " average hits.");
	
		//printDebugInformation(xBest, yBest);
		
	}
	
	public void printDebugInformation(double tx, double ty) {
		System.out.println("{");
		System.out.println("\"circles\":[");
		for(int i = 0; i < n; i++) {
			if(i > 0) System.out.println(",");
			System.out.println("{");
			System.out.println("\"x\":" + cows[i].x + ",\"y\":" + cows[i].y + ",\"r\":" + cows[i].r);
			System.out.println("}");
		}
		System.out.println("],");
		System.out.println("\"vectors\":[");

		System.out.println("{");
		System.out.println("\"x\":" + tx + ",\"y\":" + ty);
		System.out.println("}");
		for(int j = 0; j < k; j++) {
			double[] nv = rotateAnother(tx, ty, j, k, 1);
			System.out.println(",{");
			System.out.println("\"x\":" + nv[0] + ",\"y\":" + nv[1]);
			System.out.println("}");
		}

		System.out.println("]");
		System.out.println("}");
	}
	
	public static void main(String[] args) {
		Multishot m = new Multishot();
		m.solve();
	}
	
}

/*
4 2 40 1.0
0 0
0 20
0 -20
-20 0
20 0
*/