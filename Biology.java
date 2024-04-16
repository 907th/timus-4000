public class Biology {

	private int n;
	private int[][] a = new int[16][16];
	
	private long[][] dp = new long[1 << 16][16];
	
	private long[] totalCount = new long[1 << 16];
	
	private int[] firstBit = new int[1 << 16];
	private int[] bitsCount = new int[1 << 16];
	
	public long getScore(String[] adj) {
		
		for(int i = 1; i < (1 << 16); i++) {
			if((i & 1) == 0) bitsCount[i] = bitsCount[i >> 1]; else bitsCount[i] = bitsCount[i >> 1] + 1;
		}
		
		for(int i = 1; i < (1 << 16); i++) {
			for(int j = 0; j < 16; j++) {
				if((i & (1 << j)) != 0) {
					firstBit[i] = j; break;
				}
			}
		}
		
		n = adj.length;
		
		for(int i = 0; i < n; i++) {
			dp[1 << i][i] = 1;
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				a[i][j] = adj[i].charAt(j) == '1' ? 1 : 0;
			}
		}

		for(int i = 1; i < (1 << n); i++) {
			for(int j = firstBit[i]; j < n; j++) {
				for(int k = firstBit[i]; k < n; k++) {
					if(((1 << k) & i) != 0) continue;
					if(((1 << j) & i) == 0) continue;
					if(a[j][k] == 1) {
						dp[i | (1 << k)][k] += dp[i][j];
					}
				}
			}
		}
		
		for(int i = 0; i < (1 << 16); i++) {
			if(bitsCount[i] < 3) continue;
			for(int j = 0; j < 16; j++) {
				
				if(a[j][firstBit[i]] == 1) {
					totalCount[i] += dp[i][j];
				}
			}
		}
		
		long response = 0;
		for(int i = 0; i < (1 << 16); i++) {
			totalCount[i] = totalCount[i] / 2;
			response += totalCount[i];
		}
		
		return response;
	}
	
	public static void getGraph() {
		
		
		int[][] coordinates = new int[][] {{12, 13},
			{15, 6},
			{22, 0},
			{0, 0},
			{7, 6},
			{11, 8},
			{14, 6},
			{13, 2},
			{9, 2},
			{8, 6},
			{11, 7},
			{13, 6},
			{12, 3},
			{10, 3},
			{9, 6},
			{11, 5}};
			
		for(int i = 0; i < coordinates.length; i++) {
			System.out.println(coordinates[i][0] + " " + coordinates[i][1]);
		}
		
		String[] s = new String[] {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
		char[][] b = new char[16][16];
		
		for(int i = 0; i < 5; i++) {
			int x = i;
			int y = (i + 1) % 5;
			
			b[x][y] = b[y][x] = 1;
			
			for(int j = 1; j <= 2; j++) {
				b[x + 5 * (j - 1)][x + 5 * j] = b[x + 5 * j][x + 5 * (j - 1)] = 1;
				
				b[x + 5 * (j - 1)][y + 5 * j] = b[y + 5 * j][x + 5 * (j - 1)] = 1;
				
				b[x + 5 * j][y + 5 * j] = b[y + 5 * j][x + 5 * j] = 1;
			}
			
			b[x + 10][15] = b[15][x + 10] = 1;
			
		}
		b[0][2] = b[2][0] = 1;
		b[0][3] = b[3][0] = 1;
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				s[i] = s[i] + (b[i][j] == 1 ? "1" : "0");
			}
		}
		
		for(int i = 0; i < 16; i++) {
			System.out.println(s[i]);
		}
		
		/*for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				if(b[i][j] == 1) {
					System.out.println("ctx.moveTo(" + coordinates[i][0] * 30 + ", " + coordinates[i][1] * 30 + ");");
					System.out.println("ctx.lineTo(" + coordinates[j][0] * 30 + ", " + coordinates[j][1] * 30 + ");");
				}
			}
		}*/
		
	}
	
	public static void main(String[] args) {
		//Biology b = new Biology();
		//long h = b.getScore(getGraph(edges));
		//System.out.println(h);
		getGraph();
	}
	
}
