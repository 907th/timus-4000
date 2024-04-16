import java.util.Locale;
import java.util.Scanner;

public class Mouse {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
		double mouseX = scanner.nextDouble();
		double mouseY = scanner.nextDouble();
		double cheeseX = scanner.nextDouble();
		double cheeseY = scanner.nextDouble();
		int n = scanner.nextInt();
		Kitchen kitchen = new Kitchen(mouseX, mouseY, cheeseX, cheeseY, n);
		for(int i = 0; i < kitchen.getCountOfFurniture(); i++) {
			int k = scanner.nextInt();
			double vertx[] = new double[16];
			double verty[] = new double[16];
			for(int j = 0; j < k; j++) {
				vertx[j] = scanner.nextDouble();
				verty[j] = scanner.nextDouble();
			}
			kitchen.addFurniture(i, k, vertx, verty);
		}
		scanner.close();
		kitchen.Solve();
		kitchen.Print();
	}
	
}

enum EdgeType {
	etPointPoint, etPointInterval, etIntervalPoint 
}

class Edge {
	public EdgeType type;
	public int pointFrom;
	public int pointTo;
	public double distance;
	public double intervalX, intervalY;
	
	public Edge(EdgeType type, int pointFrom, int pointTo) {
		this.type = type;
		this.pointFrom = pointFrom;
		this.pointTo = pointTo;
	}
}

class Kitchen {
	private double mouseX, mouseY, cheeseX, cheeseY;
	private int n;
	private int k[] = new int[128];
	private double[][][] furniture = new double[2][128][];
	private Edge edges[][] = new Edge[128][128];
	
	private double coordX[] = new double[16536];
	private double coordY[] = new double[16536];
	
	private double tempX = 0;
	private double tempY = 0;

	private double[][] a = new double [128][128];
	private double[] d = new double[128];
	private int[] f = new int[128];
	private int[] p = new int[128];
	private final double inf = 1e15;
	private final double eps = 1e-7;
	
	public Kitchen(double mouseX, double mouseY, double cheeseX, double cheeseY, int n) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.cheeseX = cheeseX;
		this.cheeseY = cheeseY;
		this.n = n;
		
		for(int i = 0; i < 128; i++) 
			for(int j = 0; j < 128; j++)
				a[i][j] = inf;
	}
	
	public int getCountOfFurniture() {
		return n;
	}
	
	public void addFurniture(int index, int count, double verticesX[], double verticesY[]) {
		k[index] = count;
		furniture[0][index] = verticesX;
		furniture[1][index] = verticesY;
	}
	
	public void Solve() {
		BuildGraph();
		FindShortestPath(n, n + 1);
	}
	
	private void BuildGraph() {
		//Add edges between furniture
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(i == j) continue;
				Edge h = findMinimumDistance(i, j);
				if(h.distance < 0) h.distance = 0;
				if( a[i][j] > h.distance) {
					a[i][j] = a[j][i] = h.distance;
					
					edges[i][j] = h;
					Edge reverse = new Edge(EdgeType.etPointPoint, h.pointTo, h.pointFrom);
					if(h.type == EdgeType.etIntervalPoint) reverse.type = EdgeType.etPointInterval;
					if(h.type == EdgeType.etPointInterval) reverse.type = EdgeType.etIntervalPoint;
					reverse.intervalX = h.intervalX;
					reverse.intervalY = h.intervalY;
					edges[j][i] = reverse;
				}
			}
		}
		//Add edges between mouse and furniture
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < k[i]; j++) {
				double h = pointPointDistance(mouseX, mouseY, furniture[0][i][j], furniture[1][i][j]);
				if( a[i][n] > h - 0.1) {
					h -= 0.1;
					if(h < 0) h = 0;
					a[i][n] = a[n][i] = h;
					edges[n][i] = new Edge(EdgeType.etPointPoint, j, j);
					edges[i][n] = new Edge(EdgeType.etPointPoint, j, j);
				}
				if(j == k[i] - 1) {
					h = pointIntervalDistance(mouseX, mouseY, furniture[0][i][j], furniture[1][i][j], furniture[0][i][0], furniture[1][i][0]);
				} else {
					h = pointIntervalDistance(mouseX, mouseY, furniture[0][i][j], furniture[1][i][j], furniture[0][i][j + 1], furniture[1][i][j + 1]);					
				}
				if(a[i][n] > h - 0.1) {
					h -= 0.1;
					if(h < 0) h = 0;
					a[i][n] = a[n][i] = h;
					edges[n][i] = new Edge(EdgeType.etPointInterval, j, j); 
					edges[i][n] = new Edge(EdgeType.etIntervalPoint, j, j);

					edges[i][n].intervalX = tempX;
					edges[i][n].intervalY = tempY;
					
					edges[n][i].intervalX = tempX;
					edges[n][i].intervalY = tempY;
				}
			}
		}
		//Add edges between furniture and cheese 
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < k[i]; j++) {
				double h = pointPointDistance(cheeseX, cheeseY, furniture[0][i][j], furniture[1][i][j]);
				if( a[i][n + 1] > h - 0.1) {
					h -= 0.1;
					if(h < 0) h = 0;
					a[i][n + 1] = a[n + 1][i] = h;
					edges[n + 1][i] = new Edge(EdgeType.etPointPoint, j, j);
					edges[i][n + 1] = new Edge(EdgeType.etPointPoint, j, j);
				}
				if(j == k[i] - 1) {
					h = pointIntervalDistance(cheeseX, cheeseY, furniture[0][i][j], furniture[1][i][j], furniture[0][i][0], furniture[1][i][0]);
				} else {
					h = pointIntervalDistance(cheeseX, cheeseY, furniture[0][i][j], furniture[1][i][j], furniture[0][i][j + 1], furniture[1][i][j + 1]);					
				}
				if(a[i][n + 1] > h - 0.1) {
					h -= 0.1;
					if(h < 0) h = 0;
					a[i][n + 1] = a[n + 1][i] = h;
					edges[n + 1][i] = new Edge(EdgeType.etPointInterval, j, j); 
					edges[i][n + 1] = new Edge(EdgeType.etIntervalPoint, j, j);
					edges[i][n + 1].intervalX = tempX;
					edges[i][n + 1].intervalY = tempY;
					edges[n + 1][i].intervalX = tempX;
					edges[n + 1][i].intervalY = tempY;
				}
			}
		}
		a[n][n + 1] = a[n + 1][n] = pointPointDistance(mouseX, mouseY, cheeseX, cheeseY);
	}
	
	public void Print() {
		int count = 0;
		int current = n + 1;
		int seq[] = new int[128];
		int m = 0;
		while(current != -1) {
			seq[m++] = current;
			current = p[current];
		}

		double fromX = 0.0;
		double fromY = 0.0;
		double toX = 0.0;
		double toY = 0.0;
		int fromPoint = -1;
		if(m == 2) {
			System.out.println("2");
			System.out.print(mouseX);
			System.out.print(" ");
			System.out.println(mouseY);
			System.out.print(cheeseX);
			System.out.print(" ");
			System.out.println(cheeseY);
			return;
		}
		for(int i = m - 1; i >= 1; i--) {
			int from = seq[i];
			int to = seq[i - 1];
			if(edges[from][to].type == EdgeType.etPointPoint) {
				if(from == n) {
					fromX = mouseX;
					fromY = mouseY;
				} else {
					fromX = furniture[0][from][edges[from][to].pointFrom];
					fromY = furniture[1][from][edges[from][to].pointFrom];

					if(fromPoint != -1) {
						int furnitureId = from;
						while(fromPoint != edges[from][to].pointFrom) {
							coordX[count] = furniture[0][furnitureId][fromPoint];
							coordY[count++] = furniture[1][furnitureId][fromPoint];
							fromPoint++;
							if(fromPoint >= k[furnitureId]) fromPoint = 0;
						}
						coordX[count] = furniture[0][furnitureId][fromPoint];
						coordY[count++] = furniture[1][furnitureId][fromPoint];
					}
				}
				coordX[count] = fromX;
				coordY[count++] = fromY;
				if(to == n + 1) {
					toX = cheeseX;
					toY = cheeseY;
				} else {
					toX = furniture[0][to][edges[from][to].pointTo];
					toY = furniture[1][to][edges[from][to].pointTo];
				}

				if(from != n && to != n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.04) {
						coordX[count] = getFirstX(fromX, fromY, toX, toY);
						coordY[count++] = getFirstY(fromX, fromY, toX, toY);
						coordX[count] = getSecondX(fromX, fromY, toX, toY);
						coordY[count++] = getSecondY(fromX, fromY, toX, toY);
					}
				}
				if(from == n && to != n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.01) {
						coordX[count] = getSecondX(fromX, fromY, toX, toY);
						coordY[count++] = getSecondY(fromX, fromY, toX, toY);
					}
				}
				if(from != n && to == n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.01) {
						coordX[count] = getFirstX(fromX, fromY, toX, toY);
						coordY[count++] = getFirstY(fromX, fromY, toX, toY);
					}
				}

				fromPoint = edges[from][to].pointTo;
			}
			if(edges[from][to].type == EdgeType.etPointInterval) {
				if(from == n) {
					fromX = mouseX;
					fromY = mouseY;
				} else {
					fromX = furniture[0][from][edges[from][to].pointFrom];
					fromY = furniture[1][from][edges[from][to].pointFrom];
					if(fromPoint != -1) {
						int furnitureId = from;
						while(fromPoint != edges[from][to].pointFrom) {
							coordX[count] = furniture[0][furnitureId][fromPoint];
							coordY[count++] = furniture[1][furnitureId][fromPoint];
							fromPoint++;
							if(fromPoint >= k[furnitureId]) fromPoint = 0;
						}
						coordX[count] = furniture[0][furnitureId][fromPoint];
						coordY[count++] = furniture[1][furnitureId][fromPoint];
					}
				}
				coordX[count] = fromX;
				coordY[count++] = fromY;

				toX = edges[from][to].intervalX;
				toY = edges[from][to].intervalY;

				if(from != n && to != n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.04) {
						coordX[count] = getFirstX(fromX, fromY, toX, toY);
						coordY[count++] = getFirstY(fromX, fromY, toX, toY);
						coordX[count] = getSecondX(fromX, fromY, toX, toY);
						coordY[count++] = getSecondY(fromX, fromY, toX, toY);
					}
				}
				if(from == n && to != n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.01) {
						coordX[count] = getSecondX(fromX, fromY, toX, toY);
						coordY[count++] = getSecondY(fromX, fromY, toX, toY);
					}
				}
				if(from != n && to == n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.01) {
						coordX[count] = getFirstX(fromX, fromY, toX, toY);
						coordY[count++] = getFirstY(fromX, fromY, toX, toY);
					}
				}

				fromPoint = edges[from][to].pointTo;
			}
			if(edges[from][to].type == EdgeType.etIntervalPoint) {
				fromX = edges[from][to].intervalX;
				fromY = edges[from][to].intervalY;
				if(fromPoint != -1) {
					int furnitureId = from;
					while(fromPoint != edges[from][to].pointFrom) {
						coordX[count] = furniture[0][furnitureId][fromPoint];
						coordY[count++] = furniture[1][furnitureId][fromPoint];
						fromPoint++;
						if(fromPoint >= k[furnitureId]) fromPoint = 0;
					}
					coordX[count] = furniture[0][furnitureId][fromPoint];
					coordY[count++] = furniture[1][furnitureId][fromPoint];
				}
				fromPoint = edges[from][to].pointTo;

				coordX[count] = fromX;
				coordY[count++] = fromY;
				if(to == n + 1) {
					toX = cheeseX;
					toY = cheeseY;
				} else {
					toX = furniture[0][to][edges[from][to].pointTo];
					toY = furniture[1][to][edges[from][to].pointTo];
				}

				if(from != n && to != n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.04) {
						coordX[count] = getFirstX(fromX, fromY, toX, toY);
						coordY[count++] = getFirstY(fromX, fromY, toX, toY);
						coordX[count] = getSecondX(fromX, fromY, toX, toY);
						coordY[count++] = getSecondY(fromX, fromY, toX, toY);
					}
				}
				if(from == n && to != n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.01) {
						coordX[count] = getSecondX(fromX, fromY, toX, toY);
						coordY[count++] = getSecondY(fromX, fromY, toX, toY);
					}
				}
				if(from != n && to == n + 1) {
					if(pointPointDistanceSquare(fromX, fromY, toX, toY) > 0.01) {
						coordX[count] = getFirstX(fromX, fromY, toX, toY);
						coordY[count++] = getFirstY(fromX, fromY, toX, toY);
					}
				}

			}

			coordX[count] = toX;
			coordY[count++] = toY;
		}
		int count1 = 1;
		for(int i = 1; i < count; i++) {
			if(coordX[i] == coordX[i - 1] && coordY[i] == coordY[i - 1]) {
				continue;
			}
			count1++;
		}
		System.out.println(count1);
		if(count1 > 1000) {
			while(true);
		}
		for(int i = 0; i < count; i++) {
			if(Math.abs(coordX[i]) < eps) coordX[i] = 0;
			if(Math.abs(coordY[i]) < eps) coordY[i] = 0;
			if(i > 0) if(coordX[i] == coordX[i - 1] && coordY[i] == coordY[i - 1]) {
				continue;
			}
			System.out.print(coordX[i]);
			System.out.print(" ");
			System.out.println(coordY[i]);
		}
	}
	
	private double pointPointDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private double pointPointDistanceSquare(double x1, double y1, double x2, double y2) {
		return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private double pointIntervalDistance(double px, double py, double x1, double y1, double x2, double y2) {
		double AQ = pointPointDistanceSquare(x1, y1, x2, y2);
		double BQ = pointPointDistanceSquare(px, py, x2, y2);
		double CQ = pointPointDistanceSquare(x1, y1, px, py);
		
		double A = pointPointDistance(x1, y1, x2, y2);
		double B = pointPointDistance(px, py, x2, y2);
		double C = pointPointDistance(x1, y1, px, py);
		
		double p = (A + B + C) / 2.0;
		double SQ = Math.sqrt(p * (p - A) * (p - B) * (p - C));
		double h = 2 * SQ / A;

		if(x2 == x1) {
			if(y1 <= py && py <= y2 || y2 <= py && py <= y1) {
				tempX = x1;
				tempY = py;
				return Math.abs(px - x1);
			} else {
				return inf;
			}
		}

		if(y2 == y1) {
			if(x1 <= px && px <= x2 || x2 <= px && px <= x1) {
				tempX = px;
				tempY = y1;
				return Math.abs(py - y1);
			} else {
				return inf;
			}
		}
		
		double k = (y2 - y1) / (x2 - x1);
		double k1 = -1 / k;
		
		double b = -x1 * k + y1;
		double b1 = py - k1 * px;
		
		tempX = (b - b1) / (k1 - k);
		if(Math.abs(tempX) < eps) tempX = 0;
		tempY = k1 * tempX + b1;
		if(Math.abs(tempY) < eps) tempY = 0;
		
		//double h = Math.abs(py - k * px - b) / (Math.sqrt(1 + k * k));
		
		if(BQ - h * h > AQ || CQ - h * h > AQ) return inf;
		
		double A1 = Math.sqrt(B * B - h * h);
		tempX = x2 - (x2 - x1) * A1 / A;
		tempY = y2 - (y2 - y1) * A1 / A;
		
		return h;
	}
	
	private double getFirstX(double x1, double y1, double x2, double y2) {
		double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); 
		return x1 + (x2 - x1) * 0.1 / length;
	}
	
	private double getFirstY(double x1, double y1, double x2, double y2) {
		double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); 
		return y1 + (y2 - y1) * 0.1 / length;
	}
	
	private double getSecondX(double x1, double y1, double x2, double y2) {
		double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); 
		return x2 - (x2 - x1) * 0.1 / length;
	}
	
	private double getSecondY(double x1, double y1, double x2, double y2) {
		double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); 
		return y2 - (y2 - y1) * 0.1 / length;
	}
	
	private Edge findMinimumDistance(int x, int y) {
		Edge ret = new Edge(EdgeType.etPointPoint, 0, 0);
		double minH = inf;
		for(int i = 0; i < k[x]; i++) {
			for(int j = 0; j < k[y]; j++) {
				double h = pointPointDistance(furniture[0][x][i], furniture[1][x][i], furniture[0][y][j], furniture[1][y][j]);
				if(minH > h - 0.2) {
					minH = h - 0.2;
					ret.type = EdgeType.etPointPoint;
					ret.pointFrom = i;
					ret.pointTo = j;
				}
				
				if(j == k[y] - 1) {
					h = pointIntervalDistance(furniture[0][x][i], furniture[1][x][i], furniture[0][y][j], furniture[1][y][j], furniture[0][y][0], furniture[1][y][0]);
				} else {
					h = pointIntervalDistance(furniture[0][x][i], furniture[1][x][i], furniture[0][y][j], furniture[1][y][j], furniture[0][y][j + 1], furniture[1][y][j + 1]);
				}
				
				if(minH > h - 0.2) {
					minH = h - 0.2;
					ret.type = EdgeType.etPointInterval;
					ret.pointFrom = i;
					ret.pointTo = j;
					ret.intervalX = tempX;
					ret.intervalY = tempY;
				}
				
			}
		}
		ret.distance = minH;
		return ret;
	}
	
	private double FindShortestPath(int s, int t) {
		for(int i = 0; i < 128; i++) {
			d[i] = inf;
			p[i] = -1;
			f[i] = 0;
		}
		d[s] = 0;
		while(true) {
			double mn = inf;
			int x = -1;
			for(int i = 0; i < n + 2; i++) {
				if(mn > d[i] && f[i] == 0) {
					mn = d[i];
					x = i;
				}
			}
			if(x == -1) break;
			f[x] = 1;
			for(int i = 0; i < n + 2; i++) {
				if( d[i] > d[x] + a[x][i] && f[i] == 0) {
					d[i] = d[x] + a[x][i];
					p[i] = x;
				}
			}
		}
		return d[t];
	}
}