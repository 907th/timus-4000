public class HalftonesChecker {

	int[][] count;
	int[][] sum;
	
	void calculateSumsAndCounts(int[][] a, int[][] b) {
		int n = a.length;
		int m = a[0].length;
		
		count = new int[n][m];
		sum = new int[n][m];
		
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
				sum[i][j] = Math.abs(sum[i][j]);
			}		
		}		
	}

	public boolean check(int[][] a, int[][] b) {
		
		int failsCount = 0;
		
		calculateSumsAndCounts(a, b);
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[i].length; j++) {
				
				int currentCount = 0;
				int currentSum = 0;
				
				for(int k = i - 10; k <= i + 10; k++) {
					if(k < 0 || k >= a.length) continue;
					for(int l = j - 10; l <= j + 10; l++) {
						if(l < 0 || l >= a.length) continue;
						currentCount++;
						currentSum += (b[k][l] - a[k][l]);
					}
				}
				
				currentSum = Math.abs(currentSum);
				if(currentSum > currentCount * 20) {
					if(currentCount != count[i][j] || currentSum != sum[i][j]) {
						throw new RuntimeException(currentSum + " " + sum[i][j]);
					}
					//System.out.println(i + " " + j + ": " + currentCount * 20 + " " + currentSum);
					failsCount++;
				}
				
			}
		}
		//System.out.println(failsCount);
		return failsCount == 0;
	}
	
	public void print(int[][] c) {
		for(int i = 0; i < c.length; i++) {
			for(int j = 0; j < c[i].length; j++) {
				System.out.print(c[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		
		int n = 150, m = 150;
		
		HalftonesChecker hc = new HalftonesChecker();
		HalftonesTestGenerator htg = new HalftonesTestGenerator();
		Halftones h = new Halftones();

		int[][] a = htg.getRandomTest(n, m);
		int[][] b = h.getSolution(a);
		
		System.out.println(n + " " + m);
		hc.print(a);
		//hc.print(b);
		
		hc.check(a, b);
		
	}
	
}
