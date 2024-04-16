public class SpacePoker {

	int[][] a;
	int[] bitsCount = new int[1 << 20];
	int n;
	
	int[][] b = new int[32][2];
	int[] finished = new int[16];
	
	int[] mast = new int[32];
	int mastCount = 0;
	
	int[] mastTotal = new int[16];
	int[] mastCurrent = new int[16];
	int oddCount;
	int evenCount;
	
	String toBitString(int mask) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < n; i++) {
			if(((1 << i) & mask) == 0) {
				sb.append('0');
			} else {
				sb.append('1');
			}
		}
		return sb.toString();
	}
	
	boolean isOk(int mask) {
		int k = 0;
		int direction = 0;
		java.util.Arrays.fill(finished, 0);
		java.util.Arrays.fill(mastCurrent, 0);
		mastCount = 0;
		
		int currentOddCount = 0;
		int currentEvenCount = 0;
		
		for(int i = 0; i < n; i++) {
			if(((1 << i) & mask) == 0) {
				mastCurrent[a[i][1]]++;
				continue;
			}
			b[k][0] = a[i][0];
			b[k][1] = a[i][1];
			k++;
		}
		
		for(int i = 0; i < 16; i++) {
			if(mastTotal[i] == mastCurrent[i] && mastTotal[i] > 0) {
				if(i % 2 == 0) currentEvenCount++; else currentOddCount++;
			}
		}
		
		mast[mastCount++] = b[0][1];
		for(int i = 0; i < k; i++) {
			if(finished[b[i][1]] == 1) return false;
			if(i > 0) {
				if(b[i][1] != b[i - 1][1]) {
					finished[b[i - 1][1]] = 1;
					mast[mastCount++] = b[i][1];
				} else {
					if(direction == 0) {
						if(b[i][0] < b[i - 1][0]) {
							direction = -1;
						} else
						if(b[i][0] > b[i - 1][0]) {
							direction =  1;
						}
					} else {
						if(b[i][0] < b[i - 1][0] && direction ==  1) return false;
						if(b[i][0] > b[i - 1][0] && direction == -1) return false;
					}
				}
			}
		}
		
		if(oddCount > evenCount && mast[0] % 2 == 0) currentOddCount--;
		if(oddCount > evenCount && mast[mastCount - 1] % 2 == 0) currentOddCount--;
		if(oddCount < evenCount && mast[0] % 2 != 0) currentEvenCount--;
		if(oddCount < evenCount && mast[mastCount - 1] % 2 != 0) currentEvenCount--;
		
		for(int i = 1; i < mastCount; i++) {
			if(mast[i - 1] % 2 == mast[i] % 2) {
				if(mast[i] % 2 == 0) currentOddCount--; else currentEvenCount--;
			}
		}
		
		if(currentOddCount < 0 || currentEvenCount < 0) return false;
		
		return true;
	}
	
	public int solve(int[][] a) {
		n = a.length;
		this.a = a;
		
		for(int i = 0; i < n; i++) {
			mastTotal[a[i][1]]++;
		}

		for(int i = 0; i < 16; i++) {
			if(mastTotal[i] != 0) {
				if(i % 2 == 0) evenCount++; else oddCount++;
			}
		}
		
		bitsCount[1] = 1;
		for(int i = 2; i < (1 << n); i++) {
			if((i & 1) == 0) bitsCount[i] = bitsCount[i >> 1]; else bitsCount[i] = bitsCount[i >> 1] + 1;
		}
		
		int best = n;
		int bestMask = 0;
		for(int i = 1; i < (1 << n); i++) {
			if(isOk(i)) {
				if( best > n - bitsCount[i]) {
					best = n - bitsCount[i];
					bestMask = i;
				}
			}
		}
		return best;
	}
	
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int[][] a = new int[n][2];
		for(int i = 0; i < n; i++) {
			a[i][0] = scanner.nextInt();
			a[i][1] = scanner.nextInt();
		}
		scanner.close();
		SpacePoker sp = new SpacePoker();
		int h = sp.solve(a);
		System.out.println(h);
	}
	
}
