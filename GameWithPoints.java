public class GameWithPoints {

	private final static int N = 1024;
	
	int n;
	
	double[][] dist = new double[N][N];
	double[] sum = new double[N];
	double[][] p;
	
	double Q(double x) {
		return x * x;
	}
	
	double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Q(x2 - x1) + Q(y2 - y1));
	}
	
	int[][] list = new int[2][N / 2];
	int[] f = new int[N];
	
	double getSum(int index, int listIndex, int m) {
		double h = 0;
		for(int i = 0; i < m; i++) {
			h += dist[index][list[listIndex][i]];
		}
		return h;
	}
	
	double solveGreedy(double[][] points, int ix1, int ix2) {

		list[0][0] = ix1;
		list[1][0] = ix2;
		
		java.util.Arrays.fill(f, 0);
		
		f[ix1] = f[ix2] = 1;
		
		int bestIndex = 0;
		double best = Double.MIN_VALUE;
		
		for(int i = 1; i < n / 2; i++) {
			best = Double.MIN_VALUE;
			bestIndex = 0;
			
			for(int j = 0; j < n; j++) {
				if(f[j] == 1) continue;
				list[0][i] = j;
				if( best < sum[j]) {
					best = sum[j];
					bestIndex = j;
				}
			}
			
			list[0][i] = bestIndex;
			f[bestIndex] = 1;
			
			best = Double.MIN_VALUE;
			bestIndex = 0;
			for(int j = 0; j < n; j++) {
				if(f[j] == 1) continue;
				if( best < sum[j]) {
					best = sum[j];
					bestIndex = j;
				}
			}

			list[1][i] = bestIndex;
			f[bestIndex] = 1;

		}
		
		double sum1 = 0;
		for(int i = 0; i < n / 2; i++) {
			for(int j = i + 1; j < n / 2; j++) {
				sum1 += dist[list[0][i]][list[0][j]];
			}
		}
		
		double sum2 = 0;
		for(int i = 0; i < n / 2; i++) {
			for(int j = i + 1; j < n / 2; j++) {
				sum2 += dist[list[1][i]][list[1][j]];
			}
		}
		
		return sum1 - sum2;
	}
	
	double solve(double[][] points) {
		this.n = points.length;
		this.p = points;
		
		for(int i = 0; i < n; i++) {
			sum[i] = 0;
			f[i] = 0;
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				dist[i][j] = getDistance(points[i][0], points[i][1], points[j][0], points[j][1]);
				sum[i] = sum[i] + dist[i][j];
			}
		}
		
		int bestIndex = 0;
		double best = -1;
		
		for(int i = 0; i < n; i++) {
			if( best < sum[i]) {
				best = sum[i];
				bestIndex = i;
			}
		}
		
		int ix1 =  bestIndex;

		bestIndex = 0;
		best = -1;
		
		for(int i = 0; i < n; i++) {
			if(i == ix1) continue;

			double h = sum[i];

			if( best < h) {
				best = h;
				bestIndex = i;
			}
		}
		
		int ix2 = bestIndex;

		return solveGreedy(points, ix1, ix2);
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		GameWithPoints gwp = new GameWithPoints();
		while(scanner.hasNextInt()) {
			int n = scanner.nextInt();
			double[][] d = new double[2 * n][2];
			for(int i = 0; i < 2 * n; i++) {
				double x = Double.parseDouble(scanner.next());
				double y = Double.parseDouble(scanner.next());
				d[i][0] = x;
				d[i][1] = y;
			}
			double h = gwp.solve(d);
			System.out.printf("%s\n", String.format("%.3f", h).replace(',', '.'));
		}
		scanner.close();
	}
	
}
