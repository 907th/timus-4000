public class SpacePoker2 {

	private final static double eps = 1e-6;

	int n;
	double[][] a = new double[128][128];
	double[] ans = new double[128];
	double[] b = new double[128];
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				a[i][j] = scanner.nextInt();
			}
			a[i][n] = -1;
		}
		scanner.close();
		for(int i = 0; i < n; i++) {
			a[n][i] = 1;
		}
		b[n] = 1;
		n++;
	}
	
	boolean doGauss() {
		int m = n;
		int[] ids = new int[m];
		for(int i = 0; i < m; i++) {
			ids[i] = i;
		}
		for(int i = 0; i < m; i++) {
			if(Math.abs(a[i][i]) < eps) {
				boolean found = false;
				while(!found) {
					for(int j = i + 1; j < m; j++) {
						if(Math.abs(a[i][j]) < eps) {
							//Swap columns i and j

							for(int k = 0; k < m; k++) {
								double d = a[k][i];
								a[k][i] = a[k][j];
								a[k][j] = d;
							}
							
							int h = ids[i];
							ids[i] = ids[j];
							ids[j] = h;
							
							found = true; break;
						}
					}
					if(!found) {
						//Find line
						for(int j = i + 1; j < m; j++) {
							if(Math.abs(a[j][i]) > eps) {
								//Swap line j and i
								for(int k = 0; k < m; k++) {
									double d = a[i][k];
									a[i][k] = a[j][k];
									a[j][k] = d;
								}
								found = true;
								break;
							}
						}
						if(!found) {
							int row = -1;
							for(int j = i + 1; j < m; j++) {
								for(int k = i + 1; k < m; k++) {
									if(Math.abs(a[j][k]) > eps) {
										row = j; break;
									}
								}
								if(row >= 0) break;
							}
							if(row == -1) break;
							//Swap row and i
							for(int k = 0; k < m; k++) {
								double d = a[i][k];
								a[i][k] = a[row][k];
								a[row][k] = d;
							}
						}
					}
				}
				if(!found) break;
			}
			
			for(int j = i + 1; j < m; j++) {
				double koeff = a[j][i] / a[i][i];
				for(int k = i; k < m; k++) {
					a[j][k] -= a[i][k] * koeff;
				}
				b[j] -= b[i] * koeff;
			}
		}
		
		for(int i = m - 1; i >= 0; i--) {
			for(int j = i + 1; j < m; j++) {
				b[i] -= a[i][j] * ans[ids[j]];
			}
			if(Math.abs(a[i][i]) < eps) {
				if(Math.abs(b[i]) < eps) {
					ans[ids[i]] = 0;
				} else {
					return false;
				}
			} else {
				ans[ids[i]] = b[i] / a[i][i];
			}
		}
		return true;
	}

	void solve() {
		init();
		doGauss();
		for(int i = 0; i < n - 1; i++) {
			System.out.println(String.format("%.8f", ans[i]).replace(',', '.'));
		}
	}
	
	public static void main(String[] args) {
		SpacePoker2 sp2 = new SpacePoker2();
		sp2.solve();
	}
	
}
