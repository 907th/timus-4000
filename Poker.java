class Utils {
	
	private double[][] a = new double[16][16];
	public double A(int k, int n) {
		if(a[k][n] > 0) return a[k][n];
		a[k][n] = 1;
		for(int i = 0; i < k; i++) {
			a[k][n] = a[k][n] * (n - i);
		}
		return a[k][n];
	}
	
	private double[] f = new double[16];
	public double F(int x) {
		return f[x];
	}
	
	public static int getCardValue(char c) {
		if(c == 'T') return 10;
		if(c == 'J') return 11;
		if(c == 'Q') return 12;
		if(c == 'K') return 13;
		if(c == 'A') return 14;
		return c - '0';
	}
	
	public Utils() {
		f[1] = 1;
		for(int i = 2; i <= 15; i++) {
			f[i] = f[i - 1] * i;
		}
		
		for(int i = 1; i <= 15; i++) {
			for(int j = 1; j <= i; j++) {
				A(j, i);
			}
		}
	}
	
}

public class Poker {

	private Utils u = new Utils();
	
	private int[][] g = new int[256][];
	private int[][] h = new int[16][];
	private int gCount = 0;
	private int hCount = 0;

	private int[] maxHand = new int[256];

	private int[][] distributionsCount = new int[16][128];
	
	private int[] a = new int[16];
	private int count = 0;
	
	private int[][] hands;
	
	private int m;
	private int[] cards;
	private int[] commonCards;
	
	private double[][] d = new double[8][128];

	private int[] o;
	
	private void getSplitting(int left, int max, int index, int[][] rs) {
		if(left == 0) {
			if(index <= 13) {
				count++;
				rs[count - 1] = new int[index];
				for(int i = 0; i < index; i++) {
					rs[count - 1][i] = a[i];
				}
			}
			return;
		}
		for(int i = max; i >= 1; i--) {
			a[index] = i;
			getSplitting(left - i, Math.min(left - i, i), index + 1, rs);
		}
	}
	
	private double match(int x, int y, int iY, int maskX, int cnt, double ans) {
		if(maskX == (1 << h[x].length) - 1) {
			for(int i = 0; i < g[y].length; i++) {
				if(o[i] > 1) ans = ans / u.F(o[i]);
			}
			for(int i = 1; i <= 15; i++) {
				if(f[i] > 1) ans = ans / u.F(f[i]);
			}
			return ans;
		}
		if(iY == g[y].length) return 0;
		double response = 0;
		for(int iX = 0; iX < h[x].length; iX++) {
			if(g[y][iY] >= h[x][iX] && ((maskX & (1 << iX)) == 0)) {
				o[iY] -= h[x][iX];
				double localResponse = match(x, y, iY + 1, maskX | (1 << iX), cnt + 1, ans);
				response = response + localResponse;
				o[iY] += h[x][iX];
			}
		}
		response = response + match(x, y, iY + 1, maskX, cnt, ans);
		return response;
	}

	private int getMaxHand(int x) {
		for(int i = hands.length - 1; i >= 0; i--) {
			int index = hands[i].length - 1;
			for(int j = 0; j < g[x].length; j++) {
				if(g[x][j] >= hands[i][index]) {
					index--;
					if(index < 0) return i + 1;
				}
			}
		}
		return 0;
	}
	
	private int getMaxHand(int[] a, int aCount) {
		for(int i = hands.length - 1; i >= 0; i--) {
			int index = hands[i].length - 1;
			for(int j = aCount - 1; j >= 0; j--) {
				if(a[j] >= hands[i][index]) {
					index--;
					if(index < 0) return i + 1;
				}
			}
		}
		return 0;
	}

	private int getDistribution(int[] a, int aCount) {
		for(int i = 0; i < hCount; i++) {
			if(h[i].length == aCount) {
				boolean foundInconsistence = false;
				for(int j = 0; j < aCount; j++) {
					if(a[aCount - j - 1] != h[i][j]) {
						foundInconsistence = true; break;
					}
				}
				if(!foundInconsistence) return i;
			}
		}
		return -1;
	}
	
	private void calculateCounts() {
		for(int i = 0; i < hCount; i++) {
			for(int j = 0; j < gCount; j++) {
				o = new int[g[j].length];
				for(int l = 0; l < o.length; l++) {
					o[l] = g[j][l];
				}
				if(g[j].length - h[i].length >= 0) {
					java.util.Arrays.fill(f, 0);
					for(int l = 0; l < g[j].length; l++) {
						f[g[j][l]]++;
					}

					double help = match(i, j, 0, 0, 0, u.A(g[j].length - h[i].length, 13 - h[i].length) * u.F(m));
					d[i][maxHand[j]] += help;
				}
			}
		}
	}
	
	private int[] f = new int[16];
	private int[] f1 = new int[16];
	private int[] b = new int[16];
	private int bCount = 0;
	private int totalBFCount = 0;
	
	private void bruteForce(int index, int length) {
		if(index == length) {
			totalBFCount++;
			java.util.Arrays.fill(f, 0);
			java.util.Arrays.fill(f1, 0);
			for(int i = 0; i < cards.length; i++) {
				f[cards[i]]++;
			}
			
			for(int i = 0; i < length; i++) {
				f[commonCards[i]]++;
				f1[commonCards[i]]++;
			}
			
			bCount = 0;
			for(int i = 2; i <= 14; i++) {
				if(f[i] > 0) b[bCount++] = f[i];
			}
			java.util.Arrays.sort(b, 0, bCount);
			
			int maxHand = getMaxHand(b, bCount);

			bCount = 0;
			for(int i = 2; i <= 14; i++) {
				if(f1[i] > 0) b[bCount++] = f1[i];
			}
			java.util.Arrays.sort(b, 0, bCount);
			int currentDistribution = getDistribution(b, bCount);
			
			distributionsCount[currentDistribution][maxHand]++;
			
			return;
		}
		for(int i = 2; i <= 14; i++) {
			commonCards[index] = i;
			bruteForce(index + 1, length);
		}
		
	}
	
	public double solve(int n, int m, int k, String myCards, String common, int[][] hands) {
		this.m = m;
		
		this.hands = hands;
		for(int i = 0; i < hands.length; i++) {
			java.util.Arrays.sort(hands[i]);
		}
		
		count = 0;
		getSplitting(m + k, m + k, 0, g);
		gCount = count;
		
		count = 0;
		getSplitting(k, k, 0, h);
		hCount = count;
		
		for(int i = 0; i < gCount; i++) {
			maxHand[i] = getMaxHand(i);
		}
		
		calculateCounts();
		
		cards = new int[myCards.length()];
		for(int i = 0; i < cards.length; i++) {
			cards[i] = Utils.getCardValue(myCards.charAt(i));
		}
		
		commonCards = new int[k];
		for(int i = 0; i < common.length(); i++) {
			commonCards[i] = Utils.getCardValue(common.charAt(i));
		}
		
		bruteForce(common.length(), k);
		
		double response = 0;

		double totalCount = 1;
		for(int i = 0; i < m; i++) {
			totalCount *= 13;
		}
		
		for(int i = 0; i < hCount; i++) {
			for(int j = 1; j <= hands.length; j++) {
				double P = 0;
				for(int l = 0; l < j; l++) {
					P = P + d[i][l] / totalCount;
				}
				double pTotal = 0;
				pTotal = Math.pow(P, n - 1) * (distributionsCount[i][j] + 0.0) / (totalBFCount + 0.0);
				response = response + pTotal;
			}
		}
		return response;
	}
	
	public static void main(String[] args) {
		Poker a = new Poker();

		//Read input
		java.util.Scanner s = new java.util.Scanner(System.in);
		String tmp = s.nextLine();
		String tmpArray[] = tmp.split(" ");
		int n = Integer.parseInt(tmpArray[0]);
		int m = Integer.parseInt(tmpArray[1]);
		int k = Integer.parseInt(tmpArray[2]);
		
		String myCards = s.nextLine();
		String commonCards = s.nextLine();
		
		int c = Integer.parseInt(s.next());
		int[][] arr = new int[c][];
		for(int i = 0; i < c; i++) {
			int arrSize = Integer.parseInt(s.next());
			arr[i] = new int[arrSize];
			for(int j = 0; j < arrSize; j++) {
				arr[i][j] = Integer.parseInt(s.next());
			}
		}
		//Read input finish
		
		double h = a.solve(n, m, k, myCards, commonCards, arr);
		System.out.println(String.format("%.12f", h).replace(',', '.'));
		s.close();
	}
	
}