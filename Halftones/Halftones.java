import java.io.PrintWriter;

class Cell implements Comparable<Cell> {
	int x, y, value;

	@Override
	public int compareTo(Cell o) {
		return o.value - value;
	}
	
	@Override
	public String toString() {
		return x + " " + y + ": " + value;
	}
}

public class Halftones {

	int n, m;
	int[][] a;
	int[][] b;
	
	public int[][] init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		m = scanner.nextInt();
		int[][] c = new int[n][m];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				c[i][j] = scanner.nextInt();
			}
		}
		
		scanner.close();
		return c;
	}
	
	public void generateBaseSolution() {
		b = new int[n][m];
		sum = new int[n][m];
		count = new int[n][m];
		java.util.Random r = new java.util.Random();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				int k = r.nextInt(256);
				if(k < a[i][j]) b[i][j] = 255; else b[i][j] = 0;
			}
		}
	}
	
	int[][] sum;
	int[][] count;
	
	int cellsCount = 0;
	Cell[] cells = new Cell[512];
	
	private final int WORST_LIMIT = 256;
	int worstCount = 0;
	int[][] worstIndex = new int[WORST_LIMIT][2];
	int[][] worstValue = new int[WORST_LIMIT][2];
	
	void addWorst(int x, int y, int value) {
		int mnIndex = 0;
		int mn = 1000000000;
		if(worstCount < WORST_LIMIT - 1) {
			worstIndex[worstCount][0] = x;
			worstIndex[worstCount][1] = y;
			worstValue[worstCount][0] = value;
			worstValue[worstCount][1] = sum[x][y];
			worstCount++;
			return;
		}
		for(int i = 0; i < worstCount; i++) {
			if(mn > worstValue[i][0]) {
				mn = worstValue[i][0];
				mnIndex = i;
			}
		}
		if(mn < value) {
			worstIndex[mnIndex][0] = x;
			worstIndex[mnIndex][1] = y;
			worstValue[mnIndex][0] = value;
			worstValue[mnIndex][1] = sum[x][y];
		}
	}
	
	void calculateSumsAndCounts() {
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				count[i][j] = sum[i][j] = 0;
			}
		}
		
		for(int i = 0; i <= 10; i++) {
			for(int j = 0; j <= 10; j++) {
				count[0][0]++;
				sum[0][0] += (b[i][j] - a[i][j]);
			}
		}
		
		for(int i = 0; i < n; i++) {

			if(i > 0) {
				count[i][0] = count[i - 1][0];
				sum[i][0] = sum[i - 1][0];
				if(i >= 11) {
					for(int l = 0; l <= 10; l++) {
						count[i][0]--;
						sum[i][0] -= (b[i - 11][l] - a[i - 11][l]);
					}
				}
				if(i + 10 < n) {
					for(int l = 0; l <= 10; l++) {
						count[i][0]++; 
						sum[i][0] += (b[i + 10][l] - a[i + 10][l]);
					}
				}
			}
			
			for(int j = 1; j < m; j++) {
				count[i][j] = count[i][j - 1];
				sum[i][j] = sum[i][j - 1];
				
				if(j >= 11) {
					for(int k = i - 10; k <= i + 10; k++) {
						if(k < 0 || k >= n) continue;
						count[i][j]--;
						sum[i][j] -= (b[k][j - 11] - a[k][j - 11]);
					}
				}
				if(j + 10 < m) {
					for(int k = i - 10; k <= i + 10; k++) {
						if(k < 0 || k >= n) continue;
						count[i][j]++;
						sum[i][j] += (b[k][j + 10] - a[k][j + 10]);
					}
				}
			}
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(Math.abs(sum[i][j]) > count[i][j] * 20) {
					addWorst(i, j, Math.abs(sum[i][j]) - count[i][j] * 20);
				}
			}
		}
		
	}
	
	public int[][] getSolution(int[][] a) {
		this.a = a;
		n = a.length;
		m = a[0].length;
		
		for(int i = 0; i < cells.length; i++) {
			cells[i] = new Cell();
		}
		
		generateBaseSolution();
		
		for(int i = 0; i < 100; i++) {
			worstCount = 0;
			calculateSumsAndCounts();
			if(worstCount == 0) break;
			int mx = 0;
			
			int index = -1;
			for(int j = 0; j < worstCount; j++) {
				if(mx < worstValue[j][0]) {
					mx = worstValue[j][0];
					index = j;
				}
			}

			for(index = 0; index < worstCount && index < 3; index++) {
				int x = worstIndex[index][0];
				int y = worstIndex[index][1];
				cellsCount = 0;
				for(int k = x - 10; k <= x + 10; k++) {
					if(k < 0 || k >= n) continue;
					for(int l = y - 10; l <= y + 10; l++) {
						if(l < 0 || l >= m) continue;
						cells[cellsCount].x = k;
						cells[cellsCount].y = l;
						cells[cellsCount].value = Math.abs(x - k) + Math.abs(y - l);
						cellsCount++;
					}
				}
				java.util.Arrays.sort(cells, 0, cellsCount);
				
				int currentSum = Math.abs(worstValue[index][1]); 
				int currentCount = count[worstIndex[index][0]][worstIndex[index][1]]; 
				
				if(worstValue[index][1] > 0) {
					for(int h = 0; h < cellsCount; h++) {
						if(b[cells[h].x][cells[h].y] == 255) {
							b[cells[h].x][cells[h].y] = 0;
							currentSum -= 255;
						}
						if(currentSum <= currentCount * 20) {
							break;
						}
					}
				} else {
					for(int h = 0; h < cellsCount; h++) {
						if(b[cells[h].x][cells[h].y] == 0) {
							b[cells[h].x][cells[h].y] = 255;
							currentSum -= 255;
						}
						if(currentSum <= currentCount * 20) {
							break;
						}
					}
				}
			}
		}
		
		return b;
	}
	
	public void print(int[][] c) {
		PrintWriter pw = new PrintWriter(System.out);
		for(int i = 0; i < c.length; i++) {
			for(int j = 0; j < c[i].length; j++) {
				pw.print(c[i][j] + " ");
			}
			pw.println();
		}
		pw.println();
		pw.flush();
		pw.close();
	}
	
	public static void main(String[] args) {
		Halftones h = new Halftones();
		int[][] answer = h.getSolution(h.init());
		h.print(answer);
	}
	
}
