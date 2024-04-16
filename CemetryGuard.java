public class CemetryGuard {

	private final double eps = 1e-7;

	double Q(double x) {
		return x * x;
	}
	
	double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Q(x1 - x2) + Q(y1 - y2));
	}

	double[] rotate(double vx, double vy, double angle) {
		double nvx = vx * Math.cos(angle) - vy * Math.sin(angle);
		double nvy = vx * Math.sin(angle) + vy * Math.cos(angle);
		return new double[] {nvx, nvy};
	}
	
	double getReturnDistance(double sx, double sy, double x2, double y2, double hx, double hy, double rd) {
		if(getDistance(sx, sy, x2, y2) - rd < eps) return getDistance(sx, sy, hx, hy);

		double l = 0;
		double r = 2 * Math.PI;
		
		while(r - l > eps) {

			double h1 = l + (r - l) / 3.0;
			double h2 = l + (r - l) / 3.0 * 2.0;
			
			double[] nv1 = rotate(0, rd, h1); nv1[0] += x2; nv1[1] += y2;
			double[] nv2 = rotate(0, rd, h2); nv2[0] += x2; nv2[1] += y2;
			
			double v1 = getDistance(sx, sy, nv1[0], nv1[1]) + getDistance(nv1[0], nv1[1], hx, hy);
			double v2 = getDistance(sx, sy, nv2[0], nv2[1]) + getDistance(nv2[0], nv2[1], hx, hy);

			if(v1 > v2) {
				l = h1;
			} else {
				r = h2;
			}
			
		}

		double h = l + (r - l) / 3.0;
		double[] nv = rotate(0, rd, h); nv[0] += x2; nv[1] += y2;
		double v = getDistance(sx, sy, nv[0], nv[1]) + getDistance(nv[0], nv[1], hx, hy);
		return v;
	}
	
	double getShortestDistance(double x1, double y1, double x2, double y2, double x3, double y3, double rd) {
		
		if(getDistance(x1, y1, x3, y3) < rd) return Double.MAX_VALUE;
		
		double l = 0;
		double r = 2 * Math.PI;
		
		while(r - l > eps) {

			double vx = 0;
			double vy = rd;
			
			double h1 = l + (r - l) / 3.0;
			double h2 = l + (r - l) / 3.0 * 2.0;
			
			double[] nv1 = rotate(vx, vy, h1); nv1[0] += x1; nv1[1] += y1;
			double[] nv2 = rotate(vx, vy, h2); nv2[0] += x1; nv2[1] += y1;
			
			double addition1 = getDistance(x3, y3, nv1[0], nv1[1]);
			if(getDistance(x3, y3, x2, y2) > rd) {
				addition1 = getReturnDistance(nv1[0], nv1[1], x2, y2, x3, y3, rd);
			}
			double v1 = getDistance(x3, y3, nv1[0], nv1[1]) + addition1;
			
			double addition2 = getDistance(x3, y3, nv2[0], nv2[1]);
			if(getDistance(x3, y3, x2, y2) > rd) {
				addition2 = getReturnDistance(nv2[0], nv2[1], x2, y2, x3, y3, rd);
			}
			double v2 = getDistance(x3, y3, nv2[0], nv2[1]) + addition2;
			
			if(v1 > v2) {
				l = h1;
			} else {
				r = h2;
			}
			
		}

		double[] nv = rotate(0, rd, l); nv[0] += x1; nv[1] += y1;
		double addition = getDistance(x3, y3, nv[0], nv[1]);
		if(getDistance(x3, y3, x2, y2) > rd) {
			addition = getReturnDistance(nv[0], nv[1], x2, y2, x3, y3, rd);
		}
		return getDistance(x3, y3, nv[0], nv[1]) + addition;
	}
	
	public double solve(double r1, double r2, double r3, double r4) {

		double x = 0, y = 0;
		
		if(r3 < eps) {
			y = r2;
			x = 0;
		} else {
			y = (Q(r3) - Q(r2) + Q(r1)) / 2.0 / r3;
			x = Math.sqrt(Q(r1) - Q(y));
		}
		
		if(getDistance(0, 0, x, y) < r4 && getDistance(0, r3, x, y) < r4) return 0;
		
		double h1 = getShortestDistance(0, 0, 0, r3, x, y, r4);
		double h2 = getShortestDistance(0, r3, 0, 0, x, y, r4);
		
		return Math.min(h1, h2);
	}
	
	public static void main(String[] args) {
		CemetryGuard cg = new CemetryGuard();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int r1 = scanner.nextInt();
		int r2 = scanner.nextInt();
		int r3 = scanner.nextInt();
		int r4 = scanner.nextInt();
		scanner.close();
		/*int r1 = 1000;
		int r2 = 2000;
		int r3 = 1000;
		int r4 = 250;*/
		double h = cg.solve(r1, r2, r3, r4);
		String answer = String.format("%.3f", h).replace(',', '.');
		System.out.printf(answer);
	}
	
}
