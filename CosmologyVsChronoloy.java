public class CosmologyVsChronoloy {

	private final int MX = 100004;
	private final long INF = 1000000000L * 1000000000L;
	
	int n;
	int[] leftMost = new int[MX];
	int[] rightMost = new int[MX];
	
	int startCosmo;
	int startChrono;
	
	int[] cosmoInitialTime = new int[MX];
	int[] chronoInitialTime = new int[MX];
	
	int[] cosmoAccessible = new int[MX];
	int[] chronoAccessible = new int[MX];
	
	int[] cosmoInCycle = new int[MX];
	int[] chronoInCycle = new int[MX];
	
	int noFurtherCrono;
	int noFurtherCosmo;
	
	int chronoCylceLength = 0;
	int cosmoCylceLength = 0;
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 1; i <= n; i++) {
			int current = scanner.nextInt();
			leftMost[i] = current;
			while(current != 0) {
				rightMost[i] = current;
				current = scanner.nextInt();
			}
		}
		startCosmo = scanner.nextInt();
		startChrono = scanner.nextInt();
		scanner.close();
	}
	
	void calcChrono() {
		int currentTime = 1;
		int currentChrono = startChrono;
		while(true) {
			chronoAccessible[currentChrono] = 1;
			chronoInitialTime[currentChrono] = currentTime++;
			if(rightMost[currentChrono] != 0) {
				if(chronoAccessible[rightMost[currentChrono]] == 1) {
					chronoCylceLength = chronoInitialTime[currentChrono] - chronoInitialTime[rightMost[currentChrono]] + 1;
					break;
				}
				currentChrono = rightMost[currentChrono];
			} else {
				noFurtherCrono = 1;
				break;
			}
		}
		int stopVertice = currentChrono;
		while(true) {
			currentChrono = rightMost[currentChrono];
			chronoInCycle[currentChrono] = 1;
			if(currentChrono == 0 || currentChrono == stopVertice) break;
		}
	}
	
	void calcCosmo() {
		int currentTime = 1;
		int currentCosmo = startCosmo;
		while(true) {
			cosmoAccessible[currentCosmo] = 1;
			cosmoInitialTime[currentCosmo] = currentTime++;
			if(leftMost[currentCosmo] != 0) {
				if(cosmoAccessible[leftMost[currentCosmo]] == 1) {
					cosmoCylceLength = cosmoInitialTime[currentCosmo] - cosmoInitialTime[leftMost[currentCosmo]] + 1;
					break;
				}
				currentCosmo = leftMost[currentCosmo];
			} else {
				noFurtherCosmo = 1;
				break;
			}
		}
		int stopVertice = currentCosmo;
		while(true) {
			currentCosmo = leftMost[currentCosmo];
			cosmoInCycle[currentCosmo] = 1;
			if(currentCosmo == 0 || currentCosmo == stopVertice) break;
		}
	}
	
	long GCD(long a, long b) {
		if(a == 0) return b;
		return GCD(b % a, a);
	}
	
	long[] gcd(long a, long b) {
		if(a == 0) {
			return new long[] {b, 0, 1};
		}
		long[] r = gcd(b % a, a);
		return new long[] {r[0], r[2] - b / a * r[1], r[1]};
	}
	
	long gcdab = 0;
	long best = INF;
	long[] ugcd;
	
	long[] firstSolution = new long[2];

	long calcDiophantine(long a, long b, long an, long bm, long u1, long u2) {
		
		if(((b - a) % gcdab) != 0) return INF;
		
		long mn = (b - a) / gcdab;
		
		long c1 = u1;
		long c2 = -u2;
		
		bm = -bm;
		
		firstSolution[0] = c1 * mn;
		firstSolution[1] = c2 * mn;
		
		long localBest = INF;
		
		long fs0 = firstSolution[0];
		long fs1 = firstSolution[1];
		
		while(fs0 >= 0 && fs1 >= 0) {
			fs0 += bm / gcdab;
			fs1 -= an / gcdab;
		}

		while(fs0 < 0 || fs1 < 0) {
			fs0 -= bm / gcdab;
			fs1 += an / gcdab;
		}
		
		localBest = a + an * fs0;
		
		return localBest;
	}
	
	long solve() {
		init();
		
		calcChrono();
		calcCosmo();

		if(cosmoCylceLength > 0 && chronoCylceLength > 0) {
			gcdab = GCD(cosmoCylceLength, chronoCylceLength);
			ugcd = gcd(cosmoCylceLength, chronoCylceLength);
		}
		
		for(int i = 1; i <= n; i++) {
			if(chronoAccessible[i] == 0) continue;
			if(cosmoAccessible[i] == 0) continue;
			
			if(chronoInitialTime[i] == cosmoInitialTime[i]) {
				if( best > chronoInitialTime[i]) 
					best = chronoInitialTime[i];
			}
			
			if(chronoInitialTime[i] - cosmoInitialTime[i] >= 0 && cosmoInCycle[i] == 1) {
				if((chronoInitialTime[i] - cosmoInitialTime[i]) % cosmoCylceLength == 0) {
					if( best > chronoInitialTime[i]) 
						best = chronoInitialTime[i];
				}
			}
			
			if(cosmoInitialTime[i] - chronoInitialTime[i] >= 0 && chronoInCycle[i] == 1) {
				if((cosmoInitialTime[i] - chronoInitialTime[i]) % chronoCylceLength == 0) {
					if( best > cosmoInitialTime[i]) 
						best = cosmoInitialTime[i];
				}
			}
			
			if(cosmoInCycle[i] == 1 && chronoInCycle[i] == 1) {
				if(chronoInitialTime[i] > cosmoInitialTime[i]) {
					long h = calcDiophantine(cosmoInitialTime[i], chronoInitialTime[i], cosmoCylceLength, chronoCylceLength, ugcd[1], ugcd[2]);
					if( best > h)
						best = h;
				} else {
					long h = calcDiophantine(chronoInitialTime[i], cosmoInitialTime[i], chronoCylceLength, cosmoCylceLength, ugcd[2], ugcd[1]);
					if( best > h)
						best = h;
				}
			}
		}
		
		return best == INF ? -1 : best - 1;
	}
	
	public static void main(String[] args) {
		CosmologyVsChronoloy cvc = new CosmologyVsChronoloy();
		long h = cvc.solve();
		System.out.println(h);
	}
	
}