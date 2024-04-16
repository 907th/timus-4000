public class Kirhgoff {

	public static final int N = 1024;
	public static final double eps = 1e-6;

	double[][] a = new double[N][N];
	int n, m;
	
	double[] b = new double[N];
	double[] x = new double[N];
	
	int[] rStart = new int[N];
	int[] rEnd = new int[N];
	int[] rRes = new int[N];
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		m = scanner.nextInt();
		
		for(int i = 0; i < m; i++) {
			rStart[i] = scanner.nextInt();
			rEnd[i] = scanner.nextInt();
			rRes[i] = scanner.nextInt();
			
			a[i][n - 1 + i] = rRes[i];
			a[i][rEnd[i] - 2] = -1;
			
			if(rStart[i] != 1) {
				a[i][rStart[i] - 2] = 1;
			}
		}
		
		b[m] = 1;
		for(int i = 1; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(rStart[j] == i) {
					a[m + i - 1][n - 1 + j] = 1;
				}
				if(rEnd[j] == i) {
					a[m + i - 1][n - 1 + j] = -1;
				}
			}
		}
		
		scanner.close();
	}
	
	void solveGauss(int count) {
		for(int i = 0; i < count; i++) {
			if(Math.abs(a[i][i]) < eps) {
				int index = -1;
				for(int j = i + 1; j < count; j++) {
					if(Math.abs(a[j][i]) > eps) {
						index = j; break;
					}
				}
				double h = b[i];
				b[i] = b[index];
				b[index] = h;
				
				for(int k = 0; k < count; k++) {
					h = a[i][k];
					a[i][k] = a[index][k];
					a[index][k] = h;
				}
			}
			
			for(int j = i + 1; j < count; j++) {
			
				double koeff = -a[j][i] / a[i][i];
				
				if(Math.abs(koeff) < eps) continue;
				
				b[j] += b[i] * koeff;
				for(int k = 0; k < count; k++) {
					a[j][k] += a[i][k] * koeff;
				}
			
			}
			
		}
		
		for(int i = count - 1; i >= 0; i--) {
			double current = 0;
			for(int j = i + 1; j < count; j++) {
				current += a[i][j] * x[j];
			}
			x[i] = (b[i] - current) / a[i][i];
		}
	}
	
	void solve() {
		init();
		solveGauss(m + n - 1);
		System.out.println(x[n - 2]);
	}

	public static void main(String[] args) {
		Kirhgoff k = new Kirhgoff();
		k.solve();
	}

}