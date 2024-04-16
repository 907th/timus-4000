public class MobileLife {

	public static double Q(double x) {
		return x * x;
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Q(x1 - x2) + Q(y1 - y2);
	}
	
	public static boolean isBetween(double m, double l, double r) {
		return l <= m && m <= r || r <= m && m <= l;
	}
	
	public static double[][] getIntersectionPoints(double x1, double y1, double x2, double y2, double x, double y, double r) {
		
		x1 -= x;
		y1 -= y;
		x2 -= x;
		y2 -= y;
		
		double[][] answer = new double[2][2];
		int size = 0;
		
		if(Math.abs(y2 - y1) < 1e-6) {
			if(r * r - y1 < 0) return new double[][] {};
			double ax1 = -Math.sqrt(r * r - y1 * y1);
			double ax2 = Math.sqrt(r * r - y1 * y1);
			answer = new double[][] {{ax1, y1}, {ax2, y2}};
			
			if(isBetween(ax1, x1, x2)) {
				answer[size][0] = ax1 + x;
				answer[size][1] = y1 + y;
				size++;
			}
			if(isBetween(ax2, x1, x2)) {
				answer[size][0] = ax2 + x;
				answer[size][1] = y1 + y;
				size++;
			}
			
		} else {
			double A = (x2 - x1) / (y2 - y1);
			double C = (-y1 * (x2 - x1) + x1 * (y2 - y1)) / (y2 - y1);
			
			double a = A * A + 1;
			double b = 2 * A * C;
			double c = C * C - r * r;
			
			double D = b * b - 4 * a * c;
			
			if(D < 0) return new double[][] {};
			
			double ay1 = (-b + Math.sqrt(D)) / 2 / a;
			double ay2 = (-b - Math.sqrt(D)) / 2 / a;
			
			double ax1 = ay1 * A + C;
			double ax2 = ay2 * A + C;
			
			answer = new double[][] {{ax1 + x, ay1 + y}, {ax2 + x, ay2 + y}};
			
			if(isBetween(ax1, x1, x2) && isBetween(ay1, y1, y2)) {
				answer[size][0] = ax1 + x;
				answer[size][1] = ay1 + y;
				size++;
			}
			if(isBetween(ax2, x1, x2) && isBetween(ay2, y1, y2)) {
				answer[size][0] = ax2 + x;
				answer[size][1] = ay2 + y;
				size++;
			}
		}
		
		double[][] toReturn = new double[size][2];
		for(int i = 0; i < size; i++) {
			toReturn[i][0] = answer[i][0];
			toReturn[i][1] = answer[i][1];
		}
		
		return toReturn;
	}
	
	class Cell {
		double x, y, r;
		String name;
		
		double[] rs = new double[8];
		
		public Cell() {
			
		}
		
		public Cell(double x, double y, double r, String name) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.name = name;
			init();
		}
		
		public void init() {
			rs[0] = r;
			for(int i = 1; i <= 5; i++) {
				rs[i] = Math.pow(10, 0.2 * i) * r;
			}
		}
		
		public int getLevel(double x, double y) {
			double distance = Math.sqrt(getDistance(this.x, this.y, x, y));
			for(int i = 0; i < 6; i++) {
				if(distance <= rs[i]) return i;
			}
			return 6;
		}
		
		@Override
		public String toString() {
			String radiuses = "";
			for(int i = 0; i < 6; i++) {
				if(i > 0) radiuses += ",";
				radiuses += rs[i];
			}
			return "{\"name\": \"" + name + "\",\nx:" + x + ",\ny:" + y + ",\nr:[" + radiuses + "]}";
		}
		
	}
	
	class PathPoint implements Comparable<PathPoint> {
		
		double x, y;
		int cellId;
		int level;

		@Override
		public int compareTo(PathPoint o) {
			double d1 = getDistance(startX, startY, x, y);
			double d2 = getDistance(startX, startY, o.x, o.y);
			if(d1 < d2) return -1;
			if(d1 > d2) return 1;
			return 0;
		}
		
		@Override 
		public String toString() {
			return "{\"x\": " + x + ", \"y\": " + y + "}";
		}
		
	}
	
	public static final String[] levels = new String[] {
		"SIGNAL_LEVEL:VIOLET",
		"SIGNAL_LEVEL:INDIGO",
		"SIGNAL_LEVEL:BLUE",
		"SIGNAL_LEVEL:GREEN",
		"SIGNAL_LEVEL:YELLOW",
		"SIGNAL_LEVEL:ORANGE",
		"SIGNAL_LEVEL:RED"
	};
	
	int n, m;
	double startX;
	double startY;
	
	PathPoint[] points = new PathPoint[16 * 128];
	int pathPointsCount = 0;
	
	double[][] p = new double[4][1024];
	Cell[] cells = new Cell[128];
	
	double[][] intersections = new double[2][1024];
	int intersectionsSize = 0;
	
	public void solve() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			double x = scanner.nextInt();
			double y = scanner.nextInt();
			double r = scanner.nextInt();
			String name = scanner.next();
			cells[i] = new Cell(x, y, r, name);
		}
		m = scanner.nextInt();
		for(int i = 0; i < m; i++) {
			p[0][i] = scanner.nextInt();
			p[1][i] = scanner.nextInt();
			p[2][i] = scanner.nextInt();
			p[3][i] = scanner.nextInt();
		}
		scanner.close();
		
		int id = -1;
		int bestLevel = 100;
		
		for(int i = 0; i < n; i++) {
			int level = cells[i].getLevel(p[0][0], p[1][0]);
			if(bestLevel == level) {
				if(cells[i].name.compareTo(cells[id].name) < 0) {
					id = i;
				}
			}
			if(bestLevel > level) {
				bestLevel = level;
				id = i;
			}
		}
		
		System.out.println("Power on. CELL_ID:" + cells[id].name + ", " + levels[cells[id].getLevel(p[0][0], p[1][0])]);
		
		for(int i = 0; i < m; i++) {
			pathPointsCount = 0;
			for(int j = 0; j < n; j++) {
				for(int k = 0; k < 6; k++) {
					double[][] rr = getIntersectionPoints(p[0][i], p[1][i], p[2][i], p[3][i], cells[j].x, cells[j].y, cells[j].rs[k]);
					for(int l = 0; l < rr.length; l++) {
						
						PathPoint pp = new PathPoint();
						pp.x = rr[l][0];
						pp.y = rr[l][1];
						pp.cellId = j;
						pp.level = k;
						
						points[pathPointsCount++] = pp;
						
						intersections[0][intersectionsSize] = rr[l][0];
						intersections[1][intersectionsSize] = rr[l][1];
						intersectionsSize++;
					}
				}
			}

			startX = p[0][i];
			startY = p[1][i];
			java.util.Arrays.sort(points, 0, pathPointsCount);
			
			for(int j = 0; j < pathPointsCount; j++) {
				
				if(bestLevel == 6) {
					if(id == points[j].cellId) { // Crossing back the edge of the current cell
						bestLevel--;
						System.out.println("Signal changed. " + levels[bestLevel]);
					} else { //Crossing the edge of the another 
						bestLevel = points[j].level - 1;
						id = points[j].cellId;
						System.out.println("Cell changed. CELL_ID:" + cells[id].name + ", " + levels[bestLevel]);
					}
				} else {
					if(id == points[j].cellId) {
						if(bestLevel == 5 && points[j].level == 5) { //Walking to red zone
							for(int k = 0; k < n; k++) {
								if(k == id) continue;
								int level = cells[k].getLevel(points[j].x, points[j].y);
								if(bestLevel == level) {
									if(cells[k].name.compareTo(cells[id].name) < 0) {
										id = i;
									}
								}
								if(bestLevel > level) {
									bestLevel = level;
									id = i;
								}
							}
							if(bestLevel == 5 && id == points[j].cellId) { //Nothing found with signal more than RED
								bestLevel = 6;
							} else {
								System.out.println("Signal changed. " + levels[6]);
								System.out.println("Cell changed. CELL_ID:" + cells[id].name + ", " + levels[bestLevel]);
							}
						} else {
							if(bestLevel == points[j].level) bestLevel = points[j].level + 1; else bestLevel = points[j].level;
							System.out.println("Signal changed. " + levels[bestLevel]);
						}
					}
				}
			}
		}
		
		//printDebugData();
		
	}
	
	public void printDebugData() {
		System.out.println("{");
		System.out.println("\"cells\":[");
		for(int i = 0; i < n; i++) {
			if(i > 0) System.out.print(",");
			System.out.println(cells[i]);
		}
		System.out.println("],");
		System.out.println("\"lines\":[");
		for(int i = 0; i < m; i++) {
			if(i > 0) System.out.print(",");
			System.out.println("{");
			System.out.println("\"x1\":" + p[0][i] + ",");
			System.out.println("\"y1\":" + p[1][i] + ",");
			System.out.println("\"x2\":" + p[2][i] + ",");
			System.out.println("\"y2\":" + p[3][i] + "");
			System.out.println("}");
		}
		System.out.println("],");
		System.out.println("\"intersections\":[");
		for(int i = 0; i < intersectionsSize; i++) {
			if(i > 0) System.out.print(",");
			System.out.println("{");
			System.out.println("\"x\":" + intersections[0][i] + ",");
			System.out.println("\"y\":" + intersections[1][i] + "");
			System.out.println("}");
		}
		System.out.println("]");
		System.out.println("}");
	}
	
	public static void main(String[] args) {
		MobileLife ml = new MobileLife();
		ml.solve();
	}
	
}
