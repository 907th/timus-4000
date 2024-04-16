public class PhilosophicDispute {

	private double eps = 1e-6;
	private double MX = 1000000.0 * 1000000.0;
	
	double Q(double x) {
		return x * x;
	}
	
	double distance(double x1, double y1, double x2, double y2) {
		return Q(x1 - x2) + Q(y1 - y2);
	}
	
	double getAnswer(double x0, double y0, double t, double ep, double l, double r) {
		double c = Math.sqrt(distance(Math.sin(t), Math.cos(t * t), x0, y0));
		if(c > 0.5 && eps < 1e-3) return -1;
		while(r - l > eps) {
			double m1 = l + (r - l) / 3.0;
			double m2 = r - (r - l) / 3.0;
			
			double x1 = Math.sin(m1);
			double y1 = Math.cos(m1 * m1);
			double v1 = distance(x1, y1, x0, y0);
			
			double x2 = Math.sin(m2);
			double y2 = Math.cos(m2 * m2);
			double v2 = distance(x2, y2, x0, y0);
			
			if(v1 > v2) {
				l = m1;
			} else {
				r = m2;
			}
			
		}
		double m = (l + r) / 2.0;
		double x = Math.sin(m);
		double y = Math.cos(m * m);
		double d = Math.sqrt(distance(x, y, x0, y0));
		if(d > ep - eps) {
			return -1;
		}
		return m;
	}
	
	double solve(double x0, double y0, double ep) {
		double zstart = Math.asin(x0);

		for(int n = 0; ; n++) {

			double z = Math.pow(-1, n) * zstart + Math.PI * n;
			
			if(z * z > MX) break;
			
			double h = getAnswer(x0, y0, z, ep, z - 0.1, z + 0.1);
			if(h > -1) {
				return h * h;
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		double xs = Double.parseDouble(scanner.next());
		double ys = Double.parseDouble(scanner.next());
		double eps = Double.parseDouble(scanner.next());
		scanner.close();
		PhilosophicDispute pd = new PhilosophicDispute();
		double h = pd.solve(xs, ys, eps);
		if(h < 0) {
			System.out.println("FAIL");
		} else {
			System.out.println(h);
		}
	}
	
}
