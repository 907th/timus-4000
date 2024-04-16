import java.io.PrintWriter;

public class VasyasDad2 {

	class ArrayComparator implements java.util.Comparator<double[]> {

		@Override
		public int compare(double[] o1, double[] o2) {
			return (o1[0] - o2[0]) < 0 ? -1 : 1;
		}
		
	}
	
	private final static double eps = 1e-6;
	private final static int N = 30004;
	
	int n;
	int[][] p = new int[2][N];

	int[] temp = new int[2 * N];
	int tempSize = 0;
	
	int[] xc = new int[N];
	int xcSize = 0;
	
	double[][] a = new double[8][8];
	double[] b = new double[8];
	double[] ans = new double[8];
	
	double[][] val = new double[2][N];
	
	double[][][] points = new double[2][2 * N][2];
	int[] pointsCount = new int[] {0, 0};
	
	double[][] answer = new double[2][N];
	int answerSize = 0;
	
	void setValue(int x, double v) {
		val[x < 0 ? 1 : 0][Math.abs(x)] = v;
	}
	
	double getValue(int x) {
		return val[x < 0 ? 1 : 0][Math.abs(x)];
	}
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			p[0][i] = scanner.nextInt();
			p[1][i] = scanner.nextInt();
			
			if(i > 0) {
				double diffX = p[0][i] - p[0][i - 1];
				double diffY = p[1][i] - p[1][i - 1];
				double diff = diffY / diffX;
				double c = p[1][i - 1];
				for(int j = p[0][i - 1]; j <= p[0][i]; j++) {
					setValue(j, c);
					c += diff;
				}
			}
			
			temp[tempSize++] = p[0][i]; 
			temp[tempSize++] = -p[0][i]; 
		}
		scanner.close();
		temp[tempSize++] = 0; 
		java.util.Arrays.sort(temp, 0, tempSize);
		xc[xcSize++] = temp[0];
		for(int i = 1; i < tempSize; i++) {
			if(temp[i] != temp[i - 1] && temp[i] <= 0) {
				xc[xcSize++] = temp[i];
			}
		}
	}
	
	boolean doGauss() {
		int m = 4;
		int[] ids = new int[m];
		for(int i = 0; i < m; i++) {
			ids[i] = i;
		}
		for(int i = 0; i < m; i++) {
			if(Math.abs(a[i][i]) < eps) {
				boolean found = false;
				int count = 0;
				while(!found) {
					count++;
					if(count > 100) {
						break;
					}
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
	
	void addPointEven(double x, double y) {
		points[0][pointsCount[0]][0] = x;
		points[0][pointsCount[0]][1] = y;
		pointsCount[0]++;
	}
	
	void addPointOdd(double x, double y) {
		points[1][pointsCount[1]][0] = x;
		points[1][pointsCount[1]][1] = y;
		pointsCount[1]++;
	}
	
	String format(double d) {
		return String.format("%.8f", d).replace(',', '.');
	}
	
	boolean onSameLine(double x1, double y1, double x2, double y2, double x3, double y3) {
		double v = (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
		return Math.abs(v) < eps;
	}
	
	void addAnswer(double x, double y) {
		if(answerSize == 0) {
			answer[0][answerSize] = x;
			answer[1][answerSize] = y;
			answerSize++;
			return;
		}
		
		if(Math.abs(answer[0][answerSize - 1] - x) < eps) return;
		
		if(answerSize > 1) {
			if(onSameLine(answer[0][answerSize - 2], answer[1][answerSize - 2], answer[0][answerSize - 1], answer[1][answerSize - 1], x, y)) {
				answer[0][answerSize - 1] = x;
				answer[1][answerSize - 1] = y;
				return;
			}
		}
		answer[0][answerSize] = x;
		answer[1][answerSize] = y;
		answerSize++;
	}
	
	void solve() {
		init();
		for(int i = 0; i < xcSize - 1; i++) {
			double v11 = getValue(xc[i]);
			double v12 = getValue(xc[i + 1]);
			
			double v21 = getValue(-xc[i]);
			double v22 = getValue(-xc[i + 1]);
			
			a[0][0] = xc[i];
			a[0][1] = 1;
			a[0][2] = xc[i];
			a[0][3] = 1;
			b[0] = v11;
			
			a[1][0] = xc[i + 1];
			a[1][1] = 1;
			a[1][2] = xc[i + 1];
			a[1][3] = 1;
			b[1] = v12;
			
			a[2][0] = xc[i];
			a[2][1] = 1;
			a[2][2] = -xc[i];
			a[2][3] = -1;
			b[2] = v21;
			
			a[3][0] = xc[i + 1];
			a[3][1] = 1;
			a[3][2] = -xc[i + 1];
			a[3][3] = -1;
			b[3] = v22;
			
			if(!doGauss()) {
				System.out.println("No");
				return;
			}
		
			double ea = ans[0];
			double eb = ans[1];
			double oa = ans[2];
			double ob = ans[3];
			
			addPointEven(xc[i], ea * xc[i] + eb);
			addPointEven(xc[i + 1], ea * xc[i + 1] + eb);
			addPointOdd(xc[i], oa * xc[i] + ob);
			addPointOdd(xc[i + 1], oa * xc[i + 1] + ob);
			
			addPointEven(-xc[i], ea * xc[i] + eb);
			addPointEven(-xc[i + 1], ea * xc[i + 1] + eb);
			addPointOdd(-xc[i], -(oa * xc[i] + ob));
			addPointOdd(-xc[i + 1], -(oa * xc[i + 1] + ob));
			
		}
		
		java.util.Arrays.sort(points[0], 0, pointsCount[0], new ArrayComparator());
		java.util.Arrays.sort(points[1], 0, pointsCount[1], new ArrayComparator());
		
		PrintWriter pw = new PrintWriter(System.out);
		
		pw.println("Yes");

		answerSize = 0;
		for(int i = 0; i < pointsCount[0]; i++) {
			addAnswer(points[0][i][0], points[0][i][1]);
		}
		for(int i = 0; i < answerSize; i++) {
			pw.println(format(answer[0][i]) + " " + format(answer[1][i]));
		}
		
		answerSize = 0;
		for(int i = 0; i < pointsCount[1]; i++) {
			addAnswer(points[1][i][0], points[1][i][1]);
		}
		for(int i = 0; i < answerSize; i++) {
			pw.println(format(answer[0][i]) + " " + format(answer[1][i]));
		}
		
		pw.flush();
	}
	
	public static void main(String[] args) {
		VasyasDad2 vd2 = new VasyasDad2();
		vd2.solve();
	}
	
}
