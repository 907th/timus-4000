class Point {
	double x, y;
}

class Line {
	double x1, y1, x2, y2;
}

public class GoatInTheGarden6 {
	
	private final double eps = 1e-6;

	int n;
	Point[] p;
	Line[] lines = new Line[256];
	int linesCount = 0;
	
	double vect(double x, double y, double x1, double y1, double x2, double y2) {
		return ((x1 - x) * (y2 - y) - (x2 - x) * (y1 - y));
	}
	
	double shortest(double x, double y, double x1, double y1, double x2, double y2) {
		double hlp = vect(x, y, x1, y1, x2, y2);
		double sq = (hlp > 0 ? hlp : -hlp);
		double h = sq / dist(x1, y1, x2, y2);

		double d1 = dist(x1, y1, x, y);
		double d2 = dist(x2, y2, x, y);

		double d21 = dist2(x1, y1, x, y);
		double d22 = dist2(x2, y2, x, y);
		double d23 = dist2(x2, y2, x1, y1);

		if (Q(h) + d23 < d21) return Math.min(d1, d2);
		if (Q(h) + d23 < d22) return Math.min(d1, d2);

		return h;
	}	
	
	double maxDist2(double x, double y) {
		double mx = Double.MIN_VALUE;
		for(int i = 0; i < n; i++) {
			double h2 = dist2(x, y, p[i].x, p[i].y);
			if(mx < h2) {
				mx = h2;
			}
		}
		return mx;
	}
	
	double minDist(double x, double y) {
		double mn = Double.MAX_VALUE;
		for(int i = 0; i < n; i++) {
			double h2 = dist2(x, y, p[i].x, p[i].y);
			if(mn > h2) {
				mn = h2;
			}
		}
		mn = Math.sqrt(mn);
		for(int i = 0; i < n; i++) {
			double h = shortest(x, y, p[i].x, p[i].y, p[(i + 1) % n].x, p[(i + 1) % n].y);
			if(mn > h) {
				mn = h;
			}
		}
		return mn;
	}
	
	double Q(double x) {
		return x * x;
	}
	
	double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Q(x2 - x1) + Q(y2 - y1));
	}
	
	double dist2(double x1, double y1, double x2, double y2) {
		return Q(x2 - x1) + Q(y2 - y1);
	}
	
	Point getPointAtDistance(double x1, double y1, double x2, double y2, double r) {
		Point a = new Point();
		double d = dist(x1, y1, x2, y2);
		a.x = x1 + r / d * (x1 - x2); 
		a.y = y1 + r / d * (y1 - y2); 
		return a;
	}
	
	Point[] getNewPoint(double x1, double y1, double x2, double y2, double r) {
		Point[] a = new Point[4];

		double C = 1000;
		
		double ay = y2 - y1;
		double ax = x2 - x1;
		double c = -ay * y1 - ax * x1;

		double nx1 = x1 + C;
		double nx2 = x1 - C;
		double ny1 = (-ax * nx1 - c) / ay;
		double ny2 = (-ax * nx2 - c) / ay;
		if(Math.abs(ay) < eps) {
			nx1 = x1;
			nx2 = x1;
			ny1 = y1 - C;
			ny2 = y1 + C;
		}
		a[0] = getPointAtDistance(x1, y1, nx1, ny1, r);
		a[1] = getPointAtDistance(x1, y1, nx2, ny2, r);
		
		c = -ay * y2 - ax * x2;

		nx1 = x2 + C;
		nx2 = x2 - C;
		ny1 = (-ax * nx1 - c) / ay;
		ny2 = (-ax * nx2 - c) / ay;
		if(Math.abs(ay) < eps) {
			nx1 = x2;
			nx2 = x2;
			ny1 = y2 - C;
			ny2 = y2 + C;
		}
		a[2] = getPointAtDistance(x2, y2, nx1, ny1, r);
		a[3] = getPointAtDistance(x2, y2, nx2, ny2, r);
		
		return a;
	}
	
	void addLine(Point a, Point b, boolean debug) {
		lines[linesCount] = new Line();
		lines[linesCount].x1 = a.x;
		lines[linesCount].y1 = a.y;
		lines[linesCount].x2 = b.x;
		lines[linesCount].y2 = b.y;
		linesCount++;
		if(debug) System.out.printf("{\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s}", 
				String.valueOf(a.x), 
				String.valueOf(a.y), 
				String.valueOf(b.x), 
				String.valueOf(b.y));
	}
	
	void buildLine(double x1, double y1, double x2, double y2, double x3, double y3, double r, boolean debug) {
		Point[] a = getNewPoint(x1, y1, x2, y2, r);
		Point[] b = new Point[2];
		int bc = 0;

		double v = (x3 - x1) * (y2 - y1) - (x2 - x1) * (y3 - y1);
		if(debug) System.out.printf("{\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s},", String.valueOf(x1), String.valueOf(y1), String.valueOf(x2), String.valueOf(y2));
		for(int i = 0; i < a.length; i++) {
			double vc = (a[i].x - x1) * (y2 - y1) - (x2 - x1) * (a[i].y - y1);
			if(v * vc > 0) continue;
			b[bc++] = a[i];
		}
		for(int i = 0; i < bc / 2; i++) {
			addLine(b[2 * i], b[2 * i + 1], debug);
		}
	}

	Point ternarySearchAngle(double x1, double y1, double x2, double y2, double rx, double ry, double r, boolean debug) {
		while(dist(x1, y1, x2, y2) > 1e-10) {

			//if(debug) System.out.printf("{\"x\": %s, \"y\": %s, \"r\": %s},\n", String.valueOf(x1), String.valueOf(y1), String.valueOf(r));

			double xx1 = x1 + (x2 - x1) / 3;
			double yy1 = y1 + (y2 - y1) / 3;

			double xx2 = x1 + 2 * (x2 - x1) / 3;
			double yy2 = y1 + 2 * (y2 - y1) / 3;

			Point cp1 = getPointAtDistance(rx, ry, xx1, yy1, r);
			Point cp2 = getPointAtDistance(rx, ry, xx2, yy2, r);
			
			cp1 = getPointAtDistance(rx, ry, cp1.x, cp1.y, r);
			cp2 = getPointAtDistance(rx, ry, cp2.x, cp2.y, r);
			
			double h1 = maxDist2(cp1.x, cp1.y);
			double h2 = maxDist2(cp2.x, cp2.y);

			if(h2 > h1) {
				x2 = cp2.x;
				y2 = cp2.y;
			} else {
				x1 = cp1.x;
				y1 = cp1.y;
			}
			
		}
		
		Point answer = new Point();
		answer.x = x1;
		answer.y = y1;
		
		return answer;
	}

	
	Point ternarySearchLine(double x1, double y1, double x2, double y2) {
		while(dist(x1, y1, x2, y2) > 1e-10) {
			double xx1 = x1 + (x2 - x1) / 3;
			double yy1 = y1 + (y2 - y1) / 3;

			double xx2 = x1 + 2 * (x2 - x1) / 3;
			double yy2 = y1 + 2 * (y2 - y1) / 3;
			
			double h1 = maxDist2(xx1, yy1);
			double h2 = maxDist2(xx2, yy2);

			if(h2 > h1) {
				x2 = xx2;
				y2 = yy2;
			} else {
				x1 = xx1;
				y1 = yy1;
			}
			
		}
		Point answer = new Point();
		answer.x = x1;
		answer.y = y1;
		return answer;
	}
	
	void solve(Point[] a, double r, double R, boolean debug) {
		n = a.length;
		this.p = a;
		
		if(debug) System.out.println("{\"lines\":[");
		for(int i = 0; i < n; i++) {
			int i1 = i;
			int i2 = (i + 1) % n;
			int i3 = (i - 1 + n) % n;
			buildLine(p[i1].x, p[i1].y, p[i2].x, p[i2].y, p[i3].x, p[i3].y, r, debug);
			if(i != n - 1) if(debug) System.out.println(",");
		}
		
		if(debug) System.out.println("],\"circles\":[");
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(i == j || i == (j + 1) % n) continue;
				Point cp1 = getPointAtDistance(p[i].x, p[i].y, p[j].x, p[j].y, r);
				Point cp2 = getPointAtDistance(p[i].x, p[i].y, p[(j + 1) % n].x, p[(j + 1) % n].y, r);
				
				Point bp = ternarySearchAngle(cp1.x, cp1.y, cp2.x, cp2.y, p[i].x, p[i].y, r, debug);
				
				double h = minDist(bp.x, bp.y);
				if(h < r - eps) {
					h = minDist(bp.x, bp.y);
					continue;
				}
				h = maxDist2(bp.x, bp.y);
				if(h < Q(R) + eps) {
					if(debug) System.out.printf("{\"x\": %s, \"y\": %s, \"r\": %s},\n", String.valueOf(bp.x), String.valueOf(bp.y), String.valueOf(r));
					if(debug) System.out.printf("{\"x\": %s, \"y\": %s, \"r\": %s},\n", String.valueOf(bp.x), String.valueOf(bp.y), String.valueOf(R));
					if(debug) System.out.println("]}");
					System.out.println(bp.x + " " + bp.y);
					return ;
				}
			}
		}
		
		for(int i = 0; i < linesCount; i++) {
			Point ans = ternarySearchLine(lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2);
			double h = maxDist2(ans.x, ans.y);
			if(h < Q(R) + eps) {
				if(debug) System.out.printf("{\"x\": %s, \"y\": %s, \"r\": %s},\n", String.valueOf(ans.x), String.valueOf(ans.y), String.valueOf(r));
				if(debug) System.out.printf("{\"x\": %s, \"y\": %s, \"r\": %s},\n", String.valueOf(ans.x), String.valueOf(ans.y), String.valueOf(R));
				if(debug) System.out.println("]}");
				System.out.println(ans.x + " " + ans.y);
				return ;
			}
		}
		
		if(debug) System.out.println("]}");
		System.out.println("No solution");
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		Point[] a = new Point[n];
		for(int i = 0; i < n; i++) {
			a[i] = new Point();
			a[i].x = scanner.nextDouble();
			a[i].y = scanner.nextDouble();
		}
		double r, R;
		r = scanner.nextDouble();
		R = scanner.nextDouble();
		scanner.close();
		
		GoatInTheGarden6 gig6 = new GoatInTheGarden6();
		gig6.solve(a, r, R, false);
		
	}
	
}
/*
5
2 2
6 2
10 4
8 9
3 7
2 9

3
0 0
3 3
6 0
1 5
*/