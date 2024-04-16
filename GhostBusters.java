public class GhostBusters {

	private final double eps = 1e-6;
	
	int n;
	int[] x = new int[128]; 
	int[] y = new int[128]; 
	int[] z = new int[128]; 
	int[] r = new int[128]; 
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			x[i] = scanner.nextInt();
			y[i] = scanner.nextInt();
			z[i] = scanner.nextInt();
			r[i] = scanner.nextInt();
		}
		scanner.close();
	}
	
	int count = 0;
	int[] temp = new int[128];
	
	double Q(double x) {
		return x * x;
	}
	
	double distanceToLine(double lx, double ly, double lz, double x, double y, double z) {
		double i = -y * lz - (-z) * ly;
		double j = -(-x * lz - (-z) * lx);
		double k = -x * ly - (-y) * lx;

		return (Q(i) + Q(j) + Q(k)) / (Q(lx) + Q(ly) + Q(lz));
	}
	
	int[] tryToHit(double lx, double ly, double lz) {
		
		count = 0;
		for(int i = 0; i < n; i++) {
			if(distanceToLine(lx, ly, lz, x[i], y[i], z[i]) <= Q(r[i]) + eps) {
				temp[count++] = i;
			}
		}
		
		int[] answer = new int[count];
		for(int i = 0; i < count; i++) {
			answer[i] = temp[i];
		}
		
		return answer;
	}
	
	double D2(double x1, double y1, double x2, double y2) {
		return Q(x1 - x2) + Q(y1 - y2);
	}	
	
	double[][] calc(double x2, double y2, double r2) {
		double d2 = Q(x2) + Q(y2);
		double s2 = d2 - r2;
		
		double cD = r2 - Q(x2) - Q(y2) - s2;
		double cA = -2 * x2;
		double cB = -2 * y2;
		//y = (D - cA * y) / cB
		
		if(Math.abs(cB) < eps) {
			double x = x2;
			double ay1 = Math.sqrt(s2 - Q(x));
			double ay2 = Math.sqrt(s2 - Q(x));
			return new double[][] {{x, ay1}, {x, ay2}};
		}
		
		double c1 = -cA / cB;
		double c2 =  cD / cB;
		
		double a = Q(c1) + 1;
		double b = 2 * c1 * c2;
		double c = Q(c2) - s2;
		
		double dsc = Q(b) - 4 * a * c;
		if(Math.abs(dsc) < eps) dsc = 0;
		if(dsc < 0) {
			return new double[][] {}; 
		}
		dsc = Math.sqrt(dsc);
		
		double ax1 = (-b + dsc) / 2.0 / a;
		double ay1 = c1 * ax1 + c2;
		double ax2 = (-b - dsc) / 2.0 / a;
		double ay2 = c1 * ax2 + c2;
		
		return new double[][] {{ax1, ay1}, {ax2, ay2}};
	}
	
	double distance2(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Q(x2 - x1) + Q(y2 - y1) + Q(z2 - z1);
	}
	
	double value(double[][] points, double x2, double y2, double z2, double r2) {
		double best = Double.MAX_VALUE;
		for(int i = 1; i < 2; i++) {
			double v = distanceToLine(points[i][2], points[i][1], points[i][2], x2, y2, z2);
			if( best > (Math.sqrt(v) - r2)) {
				best = (Math.sqrt(v) - r2);
			}
		}
		return best;
	}
	
	double[] getX(double x1, double y1, double z1, double r1, double curY) {
		
		double d2 = distance2(0, 0, 0, x1, y1, z1);
		double C0 = d2 - Q(r1);
		
		double c1 = -x1 / z1;
		double c2 = C0 / z1 - curY * y1 / z1;
		
		double a = Q(c1) + 1;
		double b = 2 * c1 * c2;
		double c = Q(curY) + Q(c2) - d2 + Q(r1);
		
		double dsc = Q(b) - 4 * a * c;
		if(Math.abs(dsc) < eps) dsc = 0;
		if(dsc < 0) {
			return new double[] {}; 
		}
		dsc = Math.sqrt(dsc);
		
		double ax1 = (-b + dsc) / 2.0 / a;
		double ax2 = (-b - dsc) / 2.0 / a;
		
		return new double[] {ax1, ax2};
	}
	
	double getValue(double x1, double y1, double z1, double r1,
			 double x2, double y2, double z2, double r2, double ym) {
		
		
		double d2 = distance2(0, 0, 0, x1, y1, z1);
		double C0 = d2 - Q(r1);
		double[] x = getX(x1, y1, z1, r1, ym);

		double bestDistance = Double.MAX_VALUE;
		
		for(int i = 0; i < x.length; i++) {
			double y = ym;
			double z = C0 / z1 - x[i] * x1 / z1 - y * y1 / z1;
			
			double v = distanceToLine(x[i], y, z, x2, y2, z2);
			double curValue = Math.abs(Math.sqrt(v) - r2);
			
			if( bestDistance > curValue) {
				bestDistance = curValue;
			}
		}
		
		return bestDistance;
	}

	double[][] getPoints(double x1, double y1, double z1, double r1,
						 double x2, double y2, double z2, double r2) {
		
		double yl = y1 - r1;
		double yr = y1 + r1;
		
		double d2 = distance2(0, 0, 0, x1, y1, z1);
		
		double C0 = d2 - Q(r1);
		
		while(yr - yl > eps) {
			
			double ym1 = yl + (yr - yl) / 3.0;
			double ym2 = yr - (yr - yl) / 3.0;
			
			double v1 = getValue(x1, y1, z1, r1, x2, y2, z2, r2, ym1);
			double v2 = getValue(x1, y1, z1, r1, x2, y2, z2, r2, ym2);
			
			if(v1 > v2) {
				yl = ym1;
			} else {
				yr = ym2;
			}
			
		}

		double[] x = getX(x1, y1, z1, r1, (yl + yr) / 2.0);
		
		double[][] answer = new double[x.length][3];
		
		for(int i = 0; i < x.length; i++) {
			double y = (yl + yr) / 2.0;
			double z = C0 / z1 - x[i] * x1 / z1 - y * y1 / z1;
			answer[i][0] = x[i];
			answer[i][1] = y;
			answer[i][2] = z;
		}
		
		return answer;
		
	}
	
	int[] bestIndexes = new int[] {};
	
	void solve() {
		init();
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				
				if(i == j) continue;
				
				double[][] points = getPoints(x[i], y[i], z[i], r[i], x[j], y[j], z[j], r[j]);
				
				for(int k = 0; k < points.length; k++) {
					int[] indexes = tryToHit(points[k][0], points[k][1], points[k][2]);
					if(bestIndexes.length < indexes.length) {
						bestIndexes = indexes;
					}
				}
				
			}
			
			int[] indexes = tryToHit(x[i], y[i], z[i]);
			if(bestIndexes.length < indexes.length) {
				bestIndexes = indexes;
			}
			
		}
		
		System.out.println(bestIndexes.length);
		for(int i = 0; i < bestIndexes.length; i++) {
			System.out.print((bestIndexes[i] + 1) + " ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		GhostBusters gb = new GhostBusters();
		gb.solve();
	}
	
}
