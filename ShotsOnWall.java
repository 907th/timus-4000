import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Utils {
	
	private static final double eps = 1e-9;
	
	static double Q(double x) {
		return x * x;
	}
	
	static double getPointToPointDistance(double x1, double y1, double x2, double y2) {
		return Q(x2 - x1) + Q(y2 - y1);
	}
	
	static long angle(int sx, int sy, int x1, int y1, int x2, int y2) {
		long vx1 = x1 - sx;
		long vy1 = y1 - sy;
		long vx2 = x2 - sx;
		long vy2 = y2 - sy;
		return vx1 * vy2 - vy1 * vx2;
	}
	
	static boolean between(double v, double l, double r) {
		return  l <= v + eps && v <= r + eps || 
				r <= v + eps && v <= l + eps;
	}
	
	static int sign(double x) {
		if(x < -eps) return -1;
		if(x > eps) return 1;
		return 0;
	}
	
	static double[] getCrossPoint(
			int x11, int y11, int x12, int y12,
			int x21, int y21, int x22, int y22) {

		double A1 = y12 - y11;
		double B1 = x11 - x12;
		double C1 = -A1 * x11 - B1 * y11;
		
		double A2 = y22 - y21;
		double B2 = x21 - x22;
		double C2 = -A2 * x21 - B2 * y21;

		if(Math.abs(A1) < eps) {
			if(Math.abs(A2) < eps) {
				return null;
			}
			double y = -C1 / B1;
			double x = (-C2 - B2 * y) / A2;
			if( between(x, x11, x12) && between(y, y11, y12) &&
				sign(x22) == sign(x) && sign(y22) == sign(y)) {
				return new double[] {x, y};
			}
			return null;
		}
		
		if(Math.abs(B2 - A2 * B1 / A1) < eps) {
			return null;
		}
		
		double y = (A2 * C1 / A1 - C2) / (B2 - A2 * B1 / A1);
		double x = -B1 / A1 * y - C1 / A1;
		
		if( between(x, x11, x12) && between(y, y11, y12) &&
			sign(x22) == sign(x) && sign(y22) == sign(y)) {
			return new double[] {x, y};
		}
		return null;
	}
	
	static double getDistance(int lx, int ly, int rx, int ry, int sx, int sy) {
		double[] crossPoint = Utils.getCrossPoint(lx, ly, rx, ry, 0, 0, sx, sy);
		if(crossPoint == null) return -1;
		return Utils.getPointToPointDistance(0, 0, crossPoint[0], crossPoint[1]);
	}
	
	static int sign(double x1, double y1, double x2, double y2, double px, double py) {
		double s = (px - x1) * (y2 - y1) - (py - y1) * (x2 - x1);
		if(s < -eps) return -1;
		if(s > eps) return 1;
		return 0;
	}
	
}

class ShotsAngleComparator implements Comparator<int[]> {
	
	int vx, vy;
	
	public ShotsAngleComparator(int x, int y) {
		vx = x; vy = y;
	}
	
	@Override
	public int compare(int[] o1, int[] o2) {
		return Utils.angle(vx, vx, o1[0], o1[1], o2[0], o2[1]) < 0 ? -1 : 1;
	}

}

class Tree {

	private static final double eps = 1e-9;

	private final int N = 60004;
	
	int totalShotsCount = 0;
	int[][] shots = new int[N][2];
	int[][] walls = new int[4][N];
	
	int root;
	
	int comparatorX, comparatorY;
	
	int currentNode;
	int[] nodes = new int[2 * N];
	int[] pL = new int[2 * N];
	int[] pR = new int[2 * N];
	int[] pWallIndex = new int[2 * N];
	int[] predcessor = new int[2 * N];
	int[] leftChild = new int[2 * N];
	int[] rightChild = new int[2 * N];
	
	Map<Long, Integer> map = new HashMap<>();

	public Tree(int x, int y) {
		comparatorX = x;
		comparatorY = y;
		for(int i = 0; i < 2 * N; i++) {
			nodes[i] = i;
		}
	}
	
	void addShot(int sx, int sy) {
		shots[totalShotsCount][0] = sx;
		shots[totalShotsCount][1] = sy;
		totalShotsCount++;
	}
	
	void putShot(int sx, int sy, int index) {
		map.put(sx * 1000000L + sy, index);
	}
	
	int getIndex(int sx, int sy) {
		return map.get(sx * 1000000L + sy);
	}
	
	int addNode(int pred, int l, int r) {
		
		int index = nodes[currentNode++];
		
		predcessor[index] = pred;
		pL[index] = l;
		pR[index] = r - 1;
		pWallIndex[index] = -1;

		if(r - l > 1) {
			int m = (r + l) / 2;
			leftChild[index] = addNode(index, l, m);
			rightChild[index] = addNode(index, m, r);
		} else {
			putShot(shots[l][0], shots[l][1], index);
		}
		
		return index;
	}
	
	void build() {
		if(totalShotsCount == 0) return;
		Arrays.sort(shots, 0, totalShotsCount, new ShotsAngleComparator(comparatorX, comparatorY));
		root = addNode(-1, 0, totalShotsCount);
	}

	int addWall(int lx, int ly, int rx, int ry, int wallIndex, int nodeIndex) {

		double[] leftCrossPoint = null;
		double[] rightCrossPoint = null;
		boolean isInside = false;

		if( Utils.angle(0, 0, shots[pL[nodeIndex]][0], shots[pL[nodeIndex]][1], lx, ly) <= 0 && 
			Utils.angle(0, 0, lx, ly, shots[pR[nodeIndex]][0], shots[pR[nodeIndex]][1]) <= 0) {
			isInside = true;
		}
				
		if( Utils.angle(0, 0, shots[pL[nodeIndex]][0], shots[pL[nodeIndex]][1], rx, ry) <= 0 && 
			Utils.angle(0, 0, rx, ry, shots[pR[nodeIndex]][0], shots[pR[nodeIndex]][1]) <= 0) {
			isInside = true;
		}
		
		leftCrossPoint = Utils.getCrossPoint(lx, ly, rx, ry, 0, 0, shots[pL[nodeIndex]][0], shots[pL[nodeIndex]][1]);
		rightCrossPoint = Utils.getCrossPoint(lx, ly, rx, ry, 0, 0, shots[pR[nodeIndex]][0], shots[pR[nodeIndex]][1]);

		if(leftCrossPoint != null && rightCrossPoint != null && pWallIndex[nodeIndex] != -1) {
			int olx = walls[0][pWallIndex[nodeIndex]];
			int oly = walls[1][pWallIndex[nodeIndex]];
			int orx = walls[2][pWallIndex[nodeIndex]];
			int ory = walls[3][pWallIndex[nodeIndex]];

			double[] leftOldCrossPoint = Utils.getCrossPoint(olx, oly, orx, ory, 0, 0, shots[pL[nodeIndex]][0], shots[pL[nodeIndex]][1]);
			double[] rightOldCrossPoint = Utils.getCrossPoint(olx, oly, orx, ory, 0, 0, shots[pR[nodeIndex]][0], shots[pR[nodeIndex]][1]);
			
			int sign1 = 0, sign2 = 0, sign3 = 0;

			try {
				sign1 = Utils.sign(lx, ly, rx, ry, leftOldCrossPoint[0], leftOldCrossPoint[1]);
				sign2 = Utils.sign(lx, ly, rx, ry, rightOldCrossPoint[0], rightOldCrossPoint[1]);
				sign3 = Utils.sign(lx, ly, rx, ry, 0, 0);
			} catch (Exception e) {
				
			}
			
			if(sign1 == sign2 && sign1 * sign3 <= 0) {
				pWallIndex[nodeIndex] = wallIndex;
				return wallIndex;
			}

			if(sign1 == sign2 && sign1 * sign3 >= 0) {
				return pWallIndex[nodeIndex];
			}
			
		}
		
		if(leftCrossPoint != null || rightCrossPoint != null) {
			isInside = true;
		}

		if(!isInside) return -1;
		
		if(pWallIndex[nodeIndex] == -1 && pL[nodeIndex] == pR[nodeIndex]) {
			pWallIndex[nodeIndex] = wallIndex;
			return wallIndex;
		}

		if(leftChild[nodeIndex] > 0) {
			if(pWallIndex[nodeIndex] != -1) {
				pWallIndex[leftChild[nodeIndex]] = pWallIndex[nodeIndex];
				pWallIndex[rightChild[nodeIndex]] = pWallIndex[nodeIndex];
			}
			
			pWallIndex[nodeIndex] = -1;
			
			int leftWall = addWall(lx, ly, rx, ry, wallIndex, leftChild[nodeIndex]);
			int rightWall = addWall(lx, ly, rx, ry, wallIndex, rightChild[nodeIndex]);
			
			if(leftWall == rightWall) {
				pWallIndex[nodeIndex] = leftWall;
				return pWallIndex[nodeIndex];
			}
		}
	
		pWallIndex[nodeIndex] = -1;
		return pWallIndex[nodeIndex];
	}
	
	void addWall(int lx, int ly, int rx, int ry, int wallIndex) {
		walls[0][wallIndex] = lx;
		walls[1][wallIndex] = ly;
		walls[2][wallIndex] = rx;
		walls[3][wallIndex] = ry;
		
		addWall(lx, ly, rx, ry, wallIndex, 0);
	}
	
	double getShot(int sx, int sy) {
		int nodeIndex = getIndex(sx, sy);
		double best = -1;
		while(nodeIndex != -1) {
			if(pWallIndex[nodeIndex] != -1) {
				double d = Utils.getDistance(
						walls[0][pWallIndex[nodeIndex]], 
						walls[1][pWallIndex[nodeIndex]], 
						walls[2][pWallIndex[nodeIndex]], 
						walls[3][pWallIndex[nodeIndex]],
						sx, sy);
				if(best < -eps || best > d) {
					best = d;
				}
			}
			nodeIndex = predcessor[nodeIndex];
		}
		
		return best;
	}
	
}

public class ShotsOnWall {

	private final int N = 60004;
	private final double eps = 1e-9;
	
	int[] historyId = new int[N];
	char[] historyCommand = new char[N];
	int historyCount = 0;
	
	int[][] wall = new int[4][N];
	int wallsCount = 0;
	int[][] shot = new int[2][N];
	int shotsCount = 0;

	int totalShots = 0;
	
	Tree[] tree = new Tree[2];
	
	void addWall(int lx, int ly, int rx, int ry) {
		historyCommand[historyCount] = 'w';
		historyId[historyCount] = wallsCount;
		historyCount++;
		wall[0][wallsCount] = lx; wall[1][wallsCount] = ly;
		wall[2][wallsCount] = rx; wall[3][wallsCount] = ry;
		wallsCount++;
	}
	
	void addShot(int sx, int sy) {
		historyCommand[historyCount] = 's';
		historyId[historyCount] = shotsCount;
		historyCount++;
		shot[0][shotsCount] = sx; shot[1][shotsCount] = sy;
		shotsCount++;
		
		if(sx >= 0) {
			tree[0].addShot(sx, sy);
		} else {
			tree[1].addShot(sx, sy);
		}
	}
	
	void init() {
		
		tree[0] = new Tree(0, 1);
		tree[1] = new Tree(0, -1);
		
		Scanner scanner = new Scanner(System.in);
		while(true) {
			String command = scanner.next();
			if(command.startsWith("end")) break;
			if(command.startsWith("wall")) {
				int lx = scanner.nextInt();
				int ly = scanner.nextInt();
				int rx = scanner.nextInt();
				int ry = scanner.nextInt();
				addWall(lx, ly, rx, ry);
			}
			if(command.startsWith("shot")) {
				int sx = scanner.nextInt();
				int sy = scanner.nextInt();
				addShot(sx, sy);
			}
		}
		scanner.close();
		tree[0].build();
		tree[1].build();
	}
	
	void executeCommands() {
		for(int i = 0; i < historyCount; i++) {
			int index = historyId[i];
			if(historyCommand[i] == 'w') {
				if(wall[0][index] >= 0 || wall[2][index] >= 0) {
					tree[0].addWall(
							wall[0][index], 
							wall[1][index], 
							wall[2][index], 
							wall[3][index],
							index);
				}
				if(wall[0][index] < 0 || wall[2][index] < 0) {
					tree[1].addWall(
							wall[0][index], 
							wall[1][index], 
							wall[2][index], 
							wall[3][index],
							index);
				}
			}
			if(historyCommand[i] == 's') {
				if(shot[0][index] >= 0) {
					double result = tree[0].getShot(shot[0][index], shot[1][index]);
					System.out.println(result < -eps ? "Infinite" : String.format("%.9f", Math.sqrt(result / Utils.getPointToPointDistance(0, 0, shot[0][index], shot[1][index]))).replace(',', '.'));
				} else {
					double result = tree[1].getShot(shot[0][index], shot[1][index]);
					System.out.println(result < -eps ? "Infinite" : String.format("%.9f", Math.sqrt(result / Utils.getPointToPointDistance(0, 0, shot[0][index], shot[1][index]))).replace(',', '.'));
				}
			}
		}
	}
	
	void solve() {
		init();
		executeCommands();
	}
	
	public static void main(String[] args) {
		ShotsOnWall sow = new ShotsOnWall();
		sow.solve();
	}
	
}