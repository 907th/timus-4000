class MST {
	
	int n;
	int[] f = new int[8];
	int[] pred = new int[8];
	double[] d = new double[8];
	
	double[][] p;
	
	public double mstLength(double[][] p) {
		
		this.p = p;
		
		java.util.Arrays.fill(f, 0);
		java.util.Arrays.fill(pred, -1);
		java.util.Arrays.fill(d, Double.MAX_VALUE);
		
		n = p.length;
		
		d[0] = 0;
		
		while(true) {
			double best = Double.MAX_VALUE;
			int minIndex = -1;
			for(int i = 0; i < n; i++) {
				if( best > d[i] && f[i] == 0) {
					best = d[i];
					minIndex = i;
				}
			}
			if(minIndex == -1) break;
			f[minIndex] = 1;
			for(int i = 0; i < n; i++) {
				double len = ShortestTree.len(p[minIndex][0], p[minIndex][1], p[i][0], p[i][1]);
				if(f[i] == 0 && d[i] > len) {
					d[i] = len;
					pred[i] = minIndex;
				}
			}
		}
		
		double answer = 0;
		for(int i = 0; i < n; i++) {
			if(pred[i] != -1) {
				double len = ShortestTree.len(p[pred[i]][0], p[pred[i]][1], p[i][0], p[i][1]);
				answer += len;
			}
		}
		
		return answer;
	}
	
	public double[][] getLines() {
		double[][] answer = new double[n - 1][4];
		int answerSize = 0;
		for(int i = 0; i < n; i++) {
			if(pred[i] != -1) {
				answer[answerSize][0] = p[i][0];
				answer[answerSize][1] = p[i][1];
				answer[answerSize][2] = p[pred[i]][0];
				answer[answerSize][3] = p[pred[i]][1];
				answerSize++;
			}
		}
		
		return answer;
	}
	
}

public class ShortestTree {
	
	class Point implements Comparable<Point> {
		
		public double x, y;
		
		public Point(double x, double y) {
			this.x = x; this.y = y;
		}

		@Override
		public int compareTo(ShortestTree.Point o) {
			double h = 
					vect(
							originalPoints[mostLeftNottomIndex][0], originalPoints[mostLeftNottomIndex][1],
							x, y,
							o.x, o.y
						);
			
			double len1 = len(
						originalPoints[mostLeftNottomIndex][0], originalPoints[mostLeftNottomIndex][1],
						x, y
					);

			double len2 = len(
					originalPoints[mostLeftNottomIndex][0], originalPoints[mostLeftNottomIndex][1],
					o.x, o.y
				);

			if(Math.abs(h) < eps) {
				return len1 < len2 ? -1 : 1;
			}
			
			return h < 0 ? -1 : 1;
		}
		
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
		
	}
	
	private final double eps = 1e-5;

	int mostLeftNottomIndex = 0;
	MST mst = new MST();
	Point[] pointsToSort = new Point[8];
	
	double[][] originalPoints;
	
	double bestLocalPointX;
	double bestLocalPointY;
	double[] bestPointFerma = new double[2];
	
	String format(double d) {
		return String.valueOf(d).replace(',', '.');
	}

	void outputPoints(double[][] p) {
		System.out.println("\"circles\":[");
		for(int i = 0; i < p.length; i++) {
			System.out.printf("{\"x\": %s, \"y\": %s, \"r\": %s}", format(p[i][0]), format(p[i][1]), format(0.1));
			if(i != p.length - 1) System.out.println(",");
		}
		System.out.println("]");
	}
	
	void outputLines(double[][] lines) {
		System.out.println("\"lines\":[");
		for(int i = 0; i < lines.length; i++) {
			System.out.printf("{\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s}", format(lines[i][0]), format(lines[i][1]), format(lines[i][2]), format(lines[i][3]));
			if(i != lines.length - 1) System.out.println(",");
		}
		System.out.println("]");
	}
	
	void output(boolean needOutput, double[][] points, double[][] lines) {
		if(needOutput) {
			System.out.println("{");
			outputPoints(points); System.out.println(",");
			outputLines(lines);
			System.out.println("}");			
		}
	}
	
	static double Q(double x) {
		return x * x;
	}
	
	double vect(double x1, double y1, double x2, double y2, double x3, double y3) {
		return (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
	}
	
	static double len(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Q(x2 - x1) + Q(y2 - y1));
	}
	
	double[] rotate(double angle, double x1, double y1, double x2, double y2) {
		double vx = x2 - x1;
		double vy = y2 - y1;
		
		double p1 =  Math.cos(angle) * vx + Math.sin(angle) * vy + x1;
		double p2 = -Math.sin(angle) * vx + Math.cos(angle) * vy + y1;
		
		return new double[] {p1, p2};
	}
	
	double[] getEquation(double x1, double y1, double x2, double y2) {
		double a = y1 - y2;
		double b = x2 - x1;
		double c = x2 * (y2 - y1) - (x2 - x1) * y2;
		return new double[] {a, b, c};
	}
	
	double[] getPoint(double[] p1, double[] p2, double[] opposite) {
		double[] r1 = rotate(Math.PI / 3.0, p1[0], p1[1], p2[0], p2[1]);
		double[] r2 = rotate(-Math.PI / 3.0, p1[0], p1[1], p2[0], p2[1]);

		double[] eq = getEquation(p1[0], p1[1], p2[0], p2[1]);
		
		double v1 = eq[0] * r1[0] + eq[1] * r1[1] + eq[2];
		double v2 = eq[0] * opposite[0] + eq[1] * opposite[1] + eq[2];
		
		if(v1 * v2 < 0) return r1; else return r2;
	}
	
	double[] getIntersectionPoint(double[] eq1, double[] eq2) {
		double y = (eq2[0] / eq1[0] * eq1[2] - eq2[2]) / (eq2[1] - eq1[1] * eq2[0] / eq1[0]);
		double x = (-eq1[1] * y - eq1[2]) / eq1[0];
		if(Math.abs(eq1[0]) < eps) {
			if(Math.abs(eq2[0]) < eps) {
				return null;
			}
			return getIntersectionPoint(eq2, eq1);
		}
		return new double[] {x, y};
	}
	
	double minFor3Points(double[][] p) {
		
		double[] p1 = getPoint(p[0], p[1], p[2]);
		double[] p2 = getPoint(p[0], p[2], p[1]);
		
		double[] eq1 = getEquation(p1[0], p1[1], p[2][0], p[2][1]);
		double[] eq2 = getEquation(p2[0], p2[1], p[1][0], p[1][1]);
		
		double[] pointFerma = getIntersectionPoint(eq1, eq2);
		
		double v11 = len(p[0][0], p[0][1], p[1][0], p[1][1]) + len(p[0][0], p[0][1], p[2][0], p[2][1]);
		double v12 = len(p[1][0], p[1][1], p[2][0], p[2][1]) + len(p[1][0], p[1][1], p[0][0], p[0][1]);
		double v13 = len(p[2][0], p[2][1], p[1][0], p[1][1]) + len(p[2][0], p[2][1], p[0][0], p[0][1]);
		double v1 = Math.min(v11, Math.min(v12, v13));

		if(pointFerma == null) {
			return v1;
		}
		
		bestPointFerma[0] = pointFerma[0];
		bestPointFerma[1] = pointFerma[1];
		
		double v2 = 
				len(pointFerma[0], pointFerma[1], p[0][0], p[0][1]) +
				len(pointFerma[0], pointFerma[1], p[1][0], p[1][1]) +
				len(pointFerma[0], pointFerma[1], p[2][0], p[2][1])
			;
		
		if(v1 < v2) {
			bestPointFerma[0] = p[0][0];
			bestPointFerma[1] = p[0][1];
			return v1;
		}
		
		return v2;
	}
	
	double best(double sx, double sy, double px, double py) {
		double lx = sx, ly = sy;
		double rx = px, ry = py;
		
		double phi = (1 + Math.sqrt(5)) / 2.0;
		double resphi = 2 - phi;
		
		double m1x = lx + (rx - lx) * resphi;
		double m1y = ly + (ry - ly) * resphi;
		
		double m2x = rx - (rx - lx) * resphi;
		double m2y = ry - (ry - ly) * resphi;

		double v1 = 
				minFor3Points(
						new double[][] {
							{m1x, m1y}, 
							{pointsToSort[2].x, pointsToSort[2].y}, 
							{pointsToSort[3].x, pointsToSort[3].y}
							}) 
							+ len(pointsToSort[0].x, pointsToSort[0].y, m1x, m1y)
							+ len(pointsToSort[1].x, pointsToSort[1].y, m1x, m1y);
		
		double v2 = minFor3Points(
						new double[][] {
							{m2x, m2y}, 
							{pointsToSort[2].x, pointsToSort[2].y}, 
							{pointsToSort[3].x, pointsToSort[3].y}
							})
							+ len(pointsToSort[0].x, pointsToSort[0].y, m2x, m2y)
							+ len(pointsToSort[1].x, pointsToSort[1].y, m2x, m2y);
		
		while(len(lx, ly, rx, ry) > eps) {
			if(v1 < v2) {
				rx = m2x; ry = m2y;

				m2x = m1x; m2y = m1y;
				v2 = v1;
				
				m1x = lx + (rx - lx) * resphi;
				m1y = ly + (ry - ly) * resphi;

				v1 = minFor3Points(
						new double[][] {
							{m1x, m1y}, 
							{pointsToSort[2].x, pointsToSort[2].y}, 
							{pointsToSort[3].x, pointsToSort[3].y}
							}) 
							+ len(pointsToSort[0].x, pointsToSort[0].y, m1x, m1y)
							+ len(pointsToSort[1].x, pointsToSort[1].y, m1x, m1y);
			} else {
				lx = m1x; ly = m1y;

				m1x = m2x; m1y = m2y;
				v1 = v2;
				
				m2x = rx - (rx - lx) * resphi;
				m2y = ry - (ry - ly) * resphi;

				v2 = minFor3Points(
						new double[][] {
							{m2x, m2y}, 
							{pointsToSort[2].x, pointsToSort[2].y}, 
							{pointsToSort[3].x, pointsToSort[3].y}
							})
							+ len(pointsToSort[0].x, pointsToSort[0].y, m2x, m2y)
							+ len(pointsToSort[1].x, pointsToSort[1].y, m2x, m2y);
			}
		}
		
		bestLocalPointX = lx;
		bestLocalPointY = ly;
		return minFor3Points(new double[][] {
					{lx, ly}, 
					{pointsToSort[2].x, pointsToSort[2].y}, 
					{pointsToSort[3].x, pointsToSort[3].y}
				})
				+ len(pointsToSort[0].x, pointsToSort[0].y, lx, ly)
				+ len(pointsToSort[1].x, pointsToSort[1].y, lx, ly);
	}
	
	double getBest(double[][] p, boolean needOutput) {
		double lx = pointsToSort[1].x, ly = pointsToSort[1].y;
		double rx = pointsToSort[2].x, ry = pointsToSort[2].y;

		double phi = (1 + Math.sqrt(5)) / 2.0;
		double resphi = 2 - phi;
		
		double m1x = lx + (rx - lx) * resphi;
		double m1y = ly + (ry - ly) * resphi;
		
		double m2x = rx - (rx - lx) * resphi;
		double m2y = ry - (ry - ly) * resphi;
		
		double v1 = best(m1x, m1y, pointsToSort[0].x, pointsToSort[0].y);
		double v2 = best(m2x, m2y, pointsToSort[0].x, pointsToSort[0].y);

		while(len(lx, ly, rx, ry) > eps) {
			if(v1 < v2) {
				rx = m2x; ry = m2y;

				m2x = m1x; m2y = m1y;
				v2 = v1;
				
				m1x = lx + (rx - lx) * resphi;
				m1y = ly + (ry - ly) * resphi;

				v1 = best(m1x, m1y, pointsToSort[0].x, pointsToSort[0].y);
			} else {
				lx = m1x; ly = m1y;

				m1x = m2x; m1y = m2y;
				v1 = v2;
				
				m2x = rx - (rx - lx) * resphi;
				m2y = ry - (ry - ly) * resphi;

				v2 = best(m2x, m2y, pointsToSort[0].x, pointsToSort[0].y);
			}
		}
		
		double with2Points = best(lx, ly, pointsToSort[0].x, pointsToSort[0].y);
		double withNoPoints = mst.mstLength(p);
		
		if(with2Points < withNoPoints) {
			
			double[][] pts = new double[][] {
				{bestLocalPointX, bestLocalPointY},
				{bestPointFerma[0], bestPointFerma[1]},
				originalPoints[0],
				originalPoints[1],
				originalPoints[2],
				originalPoints[3]
				};
			
			mst.mstLength(pts);
			
			output(needOutput, pts, mst.getLines());
			return with2Points;
		} else {
			mst.mstLength(p);
			output(needOutput, originalPoints, mst.getLines());
		}
		
		return withNoPoints;
	}
	
	int[][] a = new int[32][4];
	int[] f = new int[32];
	int[] st = new int[32];
	int permCount = 0;
	
	void genPerm(int cur, int limit) {
		if(cur == limit) {
			for(int i = 0; i < limit; i++) {
				a[permCount][i] = st[i];
			}
			permCount++;
			return;
		}
		for(int i = 0; i < limit; i++) {
			if(f[i] == 0) {
				f[i] = 1;
				st[cur] = i;
				genPerm(cur + 1, limit);
				f[i] = 0;
			}
		}
	}
	
	double minFor4Points(double[][] p, boolean needOutput) {
		
		originalPoints = p;
		
		mostLeftNottomIndex = 0;
		for(int i = 0; i < p.length; i++) {
			if(p[mostLeftNottomIndex][1] == p[i][1] && p[mostLeftNottomIndex][0] > p[i][0] || p[mostLeftNottomIndex][1] > p[i][1]) {
				mostLeftNottomIndex = i;
			}
			pointsToSort[i] = new Point(p[i][0], p[i][1]);
		}
		java.util.Arrays.sort(pointsToSort, 0, p.length);
		
		Point[] sortedPoints = new Point[4];
		sortedPoints[0] = new Point(pointsToSort[0].x, pointsToSort[0].y);
		sortedPoints[1] = new Point(pointsToSort[1].x, pointsToSort[1].y);
		sortedPoints[2] = new Point(pointsToSort[2].x, pointsToSort[2].y);
		sortedPoints[3] = new Point(pointsToSort[3].x, pointsToSort[3].y);
		
		double best = Double.MAX_VALUE;

		double[] gba = new double[24];
	
		for(int i = 0; i < permCount; i += 4) {
			pointsToSort = new Point[] {sortedPoints[a[i][0]], sortedPoints[a[i][1]], sortedPoints[a[i][2]], sortedPoints[a[i][3]]};
			gba[i] = getBest(p, false);
			if(best > gba[i]) {
				best = gba[i];
			}
		}
		
		return best;
	}
	
	public static void main(String[] args) {
		ShortestTree st = new ShortestTree();
		st.genPerm(0, 4);
		int testCount = 0;
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		testCount = scanner.nextInt();
		for(int i = 0; i < testCount; i++) {
			int x1, y1, x2, y2, x3, y3, x4, y4;
			x1 = scanner.nextInt(); y1 = scanner.nextInt();
			x2 = scanner.nextInt(); y2 = scanner.nextInt();
			x3 = scanner.nextInt(); y3 = scanner.nextInt();
			x4 = scanner.nextInt(); y4 = scanner.nextInt();
			double h = st.minFor4Points(new double[][] {{x1, y1}, {x2, y2}, {x3, y3}, {x4, y4}}, false);
			System.out.println(h);
		}
		scanner.close();
	}
	
}
