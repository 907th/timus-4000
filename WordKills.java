import java.math.BigInteger;

public class WordKills {

	public final int N = 1024;

	int[] f = new int[16];
	int totalCount = 0;

	int[][] potentialWords = new int[N][12];
	int[] potentialWordLength = new int[N];
	int[] differentCount = new int[N];

	int[][][] vc = new int[32][8][16];

	void generate(int n, int current, int count) {
		if (current == n) {
			for (int i = 0; i < n; i++) {
				potentialWords[totalCount][i] = f[i];
			}
			differentCount[totalCount] = count;
			potentialWordLength[totalCount] = n;
			totalCount++;
			return;
		}
		for (int i = 0; i < count; i++) {
			f[current] = i;
			generate(n, current + 1, count);
		}
		f[current] = count;
		generate(n, current + 1, count + 1);
	}

	boolean check(int[] origWord, int length, int totalLength, int k) {

		int[] word = new int[totalLength];
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < length; j++) {
				word[length * i + j] = origWord[j];
			}
		}

		for (int i = 0; i < totalLength; i++) {
			for (int j = i + 1; j <= totalLength; j++) {

				if (i == 0 && j == totalLength)
					continue;

				if ((j - i) % k == 0) {
					boolean failed = false;
					int len = (j - i) / k;
					for (int h = 0; h < len; h++) {
						for (int t = 0; t < k; t++) {
							if (word[i + len * t + h] != word[i + h]) {
								failed = true;
								break;
							}
						}
						if (failed)
							break;
					}
					if (!failed) {
						return false;
					}
				}
			}
		}
		return true;
	}

	void init(int n, int totalLen, int k) {
		generate(n, 0, 0);
		for (int i = 0; i < totalCount; i++) {
			if (check(potentialWords[i], n, totalLen, k)) {
				vc[n][k][differentCount[i]]++;
			}
		}

		for (int i = 0; i < 16; i++) {
			if (vc[n][k][i] != 0) {
				System.out.println("a[" + n + "][" + k + "][" + i + "] = " + vc[n][k][i] + ";");
			}
		}

	}

	int[][][] a = new int[32][8][32];

	void prebuildInit() {
		a[1][2][1] = 1;
		a[1][3][1] = 1;
		a[1][4][1] = 1;
		a[1][5][1] = 1;
		a[2][2][2] = 1;
		a[2][3][2] = 1;
		a[2][4][2] = 1;
		a[2][5][2] = 1;
		a[3][2][3] = 1;
		a[3][3][2] = 3;
		a[3][3][3] = 1;
		a[3][4][2] = 3;
		a[3][4][3] = 1;
		a[3][5][2] = 3;
		a[3][5][3] = 1;
		a[4][2][3] = 2;
		a[4][2][4] = 1;
		a[4][3][2] = 2;
		a[4][3][3] = 6;
		a[4][3][4] = 1;
		a[4][4][2] = 6;
		a[4][4][3] = 6;
		a[4][4][4] = 1;
		a[4][5][2] = 6;
		a[4][5][3] = 6;
		a[4][5][4] = 1;
		a[5][2][4] = 5;
		a[5][2][5] = 1;
		a[5][3][2] = 5;
		a[5][3][3] = 20;
		a[5][3][4] = 10;
		a[5][3][5] = 1;
		a[5][4][2] = 10;
		a[5][4][3] = 25;
		a[5][4][4] = 10;
		a[5][4][5] = 1;
		a[6][2][3] = 3;
		a[6][2][4] = 14;
		a[6][2][5] = 9;
		a[6][2][6] = 1;
		a[6][3][2] = 6;
		a[6][3][3] = 59;
		a[6][3][4] = 59;
		a[6][3][5] = 15;
		a[6][3][6] = 1;
		a[7][2][4] = 35;
		a[7][2][5] = 49;
		a[7][2][6] = 14;
		a[7][2][7] = 1;
		a[7][3][2] = 7;
		a[7][3][3] = 168;
		a[7][3][4] = 287;
		a[7][3][5] = 133;
		a[7][3][6] = 21;
		a[7][3][7] = 1;
		a[8][2][3] = 4;
		a[8][2][4] = 98;
		a[8][2][5] = 222;
		a[8][2][6] = 118;
		a[8][2][7] = 20;
		a[8][2][8] = 1;
		a[9][2][4] = 240;
		a[9][2][5] = 930;
		a[9][2][6] = 798;
		a[9][2][7] = 237;
		a[9][2][8] = 27;
		a[9][2][9] = 1;
		a[10][2][4] = 665;
		a[10][2][5] = 3747;
		a[10][2][6] = 4802;
		a[10][2][7] = 2200;
		a[10][2][8] = 425;
		a[10][2][9] = 35;
		a[10][2][10] = 1;
		a[11][2][3] = 11;
		a[11][2][4] = 1650;
		a[11][2][5] = 14652;
		a[11][2][6] = 26950;
		a[11][2][7] = 17765;
		a[11][2][8] = 5148;
		a[11][2][9] = 704;
		a[11][2][10] = 44;
		a[11][2][11] = 1;

	}

	BigInteger A(int k, int n) {
		BigInteger answer = BigInteger.ONE;
		for (int i = 1; i <= k; i++) {
			answer = answer.multiply(BigInteger.valueOf(n));
			n--;
		}
		return answer;
	}

	void prebuildSolve(int n, int k, int lettersCount) {
		prebuildInit();
		BigInteger answer = BigInteger.ZERO;

		for (int ln = 1; ln <= n; ln++) {
			if (ln % k != 0)
				continue;
			for (int i = 1; i <= lettersCount; i++) {
				BigInteger local = BigInteger.valueOf(a[ln / k][k][i]);
				local = local.multiply(A(i, lettersCount));
				answer = answer.add(local);
			}
		}

		System.out.println(answer);
	}

	public static void main1(String[] args) {
		for (int i = 1; i <= 11; i++) {
			for (int k = 2; k <= 5; k++) {
				if (i * k > 22)
					continue;
				WordKills wk = new WordKills();
				wk.init(i, i * k, k);
			}
		}
	}

	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int m = scanner.nextInt();
		int k = scanner.nextInt();
		int n = scanner.nextInt();
		WordKills wk = new WordKills();
		wk.prebuildSolve(n, k, m);
		scanner.close();
	}

}