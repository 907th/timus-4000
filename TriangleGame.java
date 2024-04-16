public class TriangleGame {

	private final double eps = 1e-9;
	
	int Q(int x) {
		return x * x;
	}
	
	double Qd(double x) {
		return x * x;
	}
	
	int dist2(int x1, int y1, int x2, int y2) {
		return Q(x2 - x1) + Q(y2 - y1);
	}
	
	double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Qd(x1 - x2) + Qd(y1 - y2));
	}
	
	double distance2(double x1, double y1, double x2, double y2) {
		return Math.abs(Qd(x1 - x2) + Qd(y1 - y2));
	}
	
	double getAngle(double xc, double yc, double x1, double y1, double x2, double y2) {
		double x = distance2(x1, y1, x2, y2);
		double y = distance2(x1, y1, xc, yc);
		double cosa = 1 - x / y / 2;
		return Math.acos(cosa);
	}
	
	double normalizeAngle(double angle) {
		if(angle < 0) angle += 2 * Math.PI;
		return 180 / Math.PI * angle;
	}
	
	boolean isRotateOk(double x1, double y1, double x2, double y2, double xc, double yc, double cosa, double sina) {
		
		double vx = x1 - xc;
		double vy = y1 - yc;
		
		double nx = vx * cosa - vy * sina + xc; 
		double ny = vx * sina + vy * cosa + yc;
		
		return dist(x2, y2, nx, ny) < eps;
		
	}
	
	boolean isMoveOk(int x1, int y1, int x2, int y2, int dx, int dy) {
		return x1 + dx == x2 && y1 + dy == y2;
	}
	
	String format(double d) {
		return String.format("%.10f", d).replace(',', '.');
	}
	
	boolean isOk(int x1, int y1, int x2, int y2, int x3, int y3, int X1, int Y1, int X2, int Y2, int X3, int Y3) {
		
		if(	dist2(x1, y1, x2, y2) != dist2(X1, Y1, X2, Y2) || 
			dist2(x1, y1, x3, y3) != dist2(X1, Y1, X3, Y3) ||
			dist2(x3, y3, x2, y2) != dist2(X3, Y3, X2, Y2)) return false;
		
		int dx = x1 - X1;
		int dy = y1 - Y1;
		
		if( isMoveOk(X1, Y1, x1, y1, dx, dy) &&
			isMoveOk(X2, Y2, x2, y2, dx, dy) &&
			isMoveOk(X3, Y3, x3, y3, dx, dy)) {
			System.out.println(1 + " " + format(dx) + " " + format(dy));
			return true;
		}
		
		//Find center
		double xc = 0;
		double yc = 0;
		
		double a1 = 2 * x1 - 2 * X1;
		double b1 = 2 * Y1 - 2 * y1;
		double c1 = Q(x1) + Q(y1) - Q(X1) - Q(Y1);
		
		if(Math.abs(a1) < eps && Math.abs(b1) < eps) {
			a1 = 2 * x3 - 2 * X3;
			b1 = 2 * Y3 - 2 * y3;
			c1 = Q(x3) + Q(y3) - Q(X3) - Q(Y3);
		}
		
		double a2 = 2 * x2 - 2 * X2;
		double b2 = 2 * Y2 - 2 * y2;
		double c2 = Q(x2) + Q(y2) - Q(X2) - Q(Y2);
		
		if(Math.abs(a1) < eps) {
			yc = -c1 / b1;
			xc = (b2 * yc + c2) / a2;
		} else {
			yc = (c2 - c1 / a1 * a2) / (a2 * b1 / a1 - b2);
			xc = b1 / a1 * yc + c1 / a1;
		}
		
		//Find angle
		double angle = getAngle(xc, yc, x1, y1, X1, Y1);
		if(Math.abs(x1 - X1) < eps && Math.abs(y1 - Y1) < eps) {
			angle = getAngle(xc, yc, x2, y2, X2, Y2);
		}
		
		//Check
		if( isRotateOk(X1, Y1, x1, y1, xc, yc, Math.cos(angle), Math.sin(angle)) && 
				isRotateOk(X2, Y2, x2, y2, xc, yc, Math.cos(angle), Math.sin(angle)) &&
				isRotateOk(X3, Y3, x3, y3, xc, yc, Math.cos(angle), Math.sin(angle))) {
			
			System.out.println(2 + " " + format(xc) + " " + format(yc) + " " + format(normalizeAngle(angle)));
			return true;
		}
		angle = -angle; 
		if( isRotateOk(X1, Y1, x1, y1, xc, yc, Math.cos(angle), Math.sin(angle)) && 
				isRotateOk(X2, Y2, x2, y2, xc, yc, Math.cos(angle), Math.sin(angle)) &&
				isRotateOk(X3, Y3, x3, y3, xc, yc, Math.cos(angle), Math.sin(angle))) {
			System.out.println(2 + " " + format(xc) + " " + format(yc) + " " + format(normalizeAngle(angle)));
			return true;
		}
		
		return false;
	}

	
	void solve(int x1, int y1, int x2, int y2, int x3, int y3, int X1, int Y1, int X2, int Y2, int X3, int Y3) {
		
		if(isOk(x1, y1, x2, y2, x3, y3, X1, Y1, X2, Y2, X3, Y3)) return;
		if(isOk(x1, y1, x2, y2, x3, y3, X1, Y1, X3, Y3, X2, Y2)) return;
		if(isOk(x1, y1, x2, y2, x3, y3, X2, Y2, X1, Y1, X3, Y3)) return;
		if(isOk(x1, y1, x2, y2, x3, y3, X2, Y2, X3, Y3, X2, Y2)) return;
		if(isOk(x1, y1, x2, y2, x3, y3, X3, Y3, X1, Y1, X2, Y2)) return;
		if(isOk(x1, y1, x2, y2, x3, y3, X3, Y3, X2, Y2, X1, Y1)) return;

		if(isOk(x1, y1, x3, y3, x2, y2, X1, Y1, X2, Y2, X3, Y3)) return;
		if(isOk(x1, y1, x3, y3, x2, y2, X1, Y1, X3, Y3, X2, Y2)) return;
		if(isOk(x1, y1, x3, y3, x2, y2, X2, Y2, X1, Y1, X3, Y3)) return;
		if(isOk(x1, y1, x3, y3, x2, y2, X2, Y2, X3, Y3, X2, Y2)) return;
		if(isOk(x1, y1, x3, y3, x2, y2, X3, Y3, X1, Y1, X2, Y2)) return;
		if(isOk(x1, y1, x3, y3, x2, y2, X3, Y3, X2, Y2, X1, Y1)) return;

		if(isOk(x2, y2, x1, y1, x3, y3, X1, Y1, X2, Y2, X3, Y3)) return;
		if(isOk(x2, y2, x1, y1, x3, y3, X1, Y1, X3, Y3, X2, Y2)) return;
		if(isOk(x2, y2, x1, y1, x3, y3, X2, Y2, X1, Y1, X3, Y3)) return;
		if(isOk(x2, y2, x1, y1, x3, y3, X2, Y2, X3, Y3, X2, Y2)) return;
		if(isOk(x2, y2, x1, y1, x3, y3, X3, Y3, X1, Y1, X2, Y2)) return;
		if(isOk(x2, y2, x1, y1, x3, y3, X3, Y3, X2, Y2, X1, Y1)) return;

		if(isOk(x2, y2, x3, y3, x1, y1, X1, Y1, X2, Y2, X3, Y3)) return;
		if(isOk(x2, y2, x3, y3, x1, y1, X1, Y1, X3, Y3, X2, Y2)) return;
		if(isOk(x2, y2, x3, y3, x1, y1, X2, Y2, X1, Y1, X3, Y3)) return;
		if(isOk(x2, y2, x3, y3, x1, y1, X2, Y2, X3, Y3, X2, Y2)) return;
		if(isOk(x2, y2, x3, y3, x1, y1, X3, Y3, X1, Y1, X2, Y2)) return;
		if(isOk(x2, y2, x3, y3, x1, y1, X3, Y3, X2, Y2, X1, Y1)) return;

		if(isOk(x3, y3, x1, y1, x2, y2, X1, Y1, X2, Y2, X3, Y3)) return;
		if(isOk(x3, y3, x1, y1, x2, y2, X1, Y1, X3, Y3, X2, Y2)) return;
		if(isOk(x3, y3, x1, y1, x2, y2, X2, Y2, X1, Y1, X3, Y3)) return;
		if(isOk(x3, y3, x1, y1, x2, y2, X2, Y2, X3, Y3, X2, Y2)) return;
		if(isOk(x3, y3, x1, y1, x2, y2, X3, Y3, X1, Y1, X2, Y2)) return;
		if(isOk(x3, y3, x1, y1, x2, y2, X3, Y3, X2, Y2, X1, Y1)) return;

		if(isOk(x3, y3, x2, y2, x1, y1, X1, Y1, X2, Y2, X3, Y3)) return;
		if(isOk(x3, y3, x2, y2, x1, y1, X1, Y1, X3, Y3, X2, Y2)) return;
		if(isOk(x3, y3, x2, y2, x1, y1, X2, Y2, X1, Y1, X3, Y3)) return;
		if(isOk(x3, y3, x2, y2, x1, y1, X2, Y2, X3, Y3, X2, Y2)) return;
		if(isOk(x3, y3, x2, y2, x1, y1, X3, Y3, X1, Y1, X2, Y2)) return;
		if(isOk(x3, y3, x2, y2, x1, y1, X3, Y3, X2, Y2, X1, Y1)) return;

		System.out.println(0);
	}
	
	public static void main(String[] args) {
		
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int x1 = scanner.nextInt();
		int y1 = scanner.nextInt();
		int x2 = scanner.nextInt();
		int y2 = scanner.nextInt();
		int x3 = scanner.nextInt();
		int y3 = scanner.nextInt();

		int X1 = scanner.nextInt();
		int Y1 = scanner.nextInt();
		int X2 = scanner.nextInt();
		int Y2 = scanner.nextInt();
		int X3 = scanner.nextInt();
		int Y3 = scanner.nextInt();
		scanner.close();
		
		TriangleGame tg = new TriangleGame();
		tg.solve(x1, y1, x2, y2, x3, y3, X1, Y1, X2, Y2, X3, Y3);
	}
	
}
