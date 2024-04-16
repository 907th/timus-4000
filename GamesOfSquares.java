public class GamesOfSquares {

	int N = 4096;
	int GN = 2048;
	int n;
	int[][][][][][][][] dp;

	int[] a;
	int[] lastMove;
	int[][] g = new int[GN][GN];

	int gcd(int x, int y) {
		if (x < GN && y < GN) {
			if (g[x][y] != 0)
				return g[x][y];
		}
		if (x == 0)
			return y;
		int h = gcd(y % x, x);
		if (x < GN && y < GN) {
			g[x][y] = h;
		}
		return h;
	}

	void rec(int[] a, int index, int[] c, int[] f, int isStart) {

		if (index == n) {

			boolean goodToGo = false;
			int v = 0;
			for (int i = 0; i < (1 << n); i++) {
				int[] newA = new int[n];

				boolean skip = false;
				boolean isGcdOk = true;
				for (int j = 0; j < n; j++) {
					if (((1 << j) & i) == 0) {
						newA[j] = c[j] - 1;
					} else {
						newA[j] = a[j] - c[j];
					}
					if (newA[j] == 0) {
						skip = true;
						break;
					}
					if (gcd(newA[j], a[j]) != 1) {
						isGcdOk = false;
					}
				}
				if (skip)
					continue;
				if (isGcdOk) {
					goodToGo = true;
				}
				java.util.Arrays.sort(newA);
				v = v ^ SG(newA, 0);
			}
			if (goodToGo) {
				if (v == 0 && isStart == 1 && lastMove[0] == 0) {
					for (int i = 0; i < n; i++) {
						lastMove[i] = c[i];
					}
				}
				f[v] = 1;
			}

			return;
		}

		for (int i1 = 1; i1 <= a[0]; i1++) {
			if (n == 1) {
				rec(a, n, new int[] { i1 }, f, isStart);
				continue;
			}
			for (int i2 = 1; i2 <= a[1]; i2++) {
				if (n == 2) {
					rec(a, n, new int[] { i1, i2 }, f, isStart);
					continue;
				}
				for (int i3 = 1; i3 <= a[2]; i3++) {
					if (n == 3) {
						rec(a, n, new int[] { i1, i2, i3 }, f, isStart);
						continue;
					}
					for (int i4 = 1; i4 <= a[3]; i4++) {
						if (n == 4) {
							rec(a, n, new int[] { i1, i2, i3, i4 }, f, isStart);
							continue;
						}
						for (int i5 = 1; i5 <= a[4]; i5++) {
							if (n == 5) {
								rec(a, n, new int[] { i1, i2, i3, i4, i5 }, f, isStart);
								continue;
							}
							for (int i6 = 1; i6 <= a[5]; i6++) {
								if (n == 6) {
									rec(a, n, new int[] { i1, i2, i3, i4, i5, i6 }, f, isStart);
									continue;
								}
								for (int i7 = 1; i7 <= a[6]; i7++) {
									if (n == 7) {
										rec(a, n, new int[] { i1, i2, i3, i4, i5, i6, i7 }, f, isStart);
										continue;
									}
									for (int i8 = 1; i8 <= a[7]; i8++) {
										if (n == 8)
											rec(a, n, new int[] { i1, i2, i3, i4, i5, i6, i7, i8 }, f, isStart);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	int SG(int[] a, int isStart) {
		int[] ky = new int[8];
		int size = 0;
		for (int i = n; i < 8; i++) {
			ky[size++] = 0;
		}
		for (int i = 0; i < n; i++) {
			ky[size++] = a[i] - 1;
		}

		if (isStart == 0) {
			if (dp[ky[0]][ky[1]][ky[2]][ky[3]][ky[4]][ky[5]][ky[6]][ky[7]] != -1) {
				return dp[ky[0]][ky[1]][ky[2]][ky[3]][ky[4]][ky[5]][ky[6]][ky[7]];
			}
		}

		for (int i = 0; i < n; i++) {
			if (a[i] == 1) {
				return 1;
			}
		}

		int[] f = new int[N];
		rec(a, 0, new int[n], f, isStart);

		int h = -1;
		for (int i = 0; i < N; i++) {
			if (f[i] == 0) {
				h = i;
				break;
			}
		}
		if (isStart == 0) {
			dp[ky[0]][ky[1]][ky[2]][ky[3]][ky[4]][ky[5]][ky[6]][ky[7]] = h;
		}
		return h;
	}

	void solve(int[] a) {
		n = a.length;
		if (n == 1) {
			if (a[0] % 2 == 1) {
				System.out.println("1\n1\n");
				return;
			}
		}
		lastMove = new int[n];
		this.a = a;
		int[] ky = new int[8];
		for (int i = 0; i < n; i++) {
			ky[i] = a[i];
		}
		for (int i = n; i < 8; i++) {
			ky[i] = 1;
		}
		java.util.Arrays.sort(ky);
		dp = new int[ky[0]][ky[1]][ky[2]][ky[3]][ky[4]][ky[5]][ky[6]][ky[7]];
		for (int i1 = 0; i1 < ky[0]; i1++) {
			for (int i2 = 0; i2 < ky[1]; i2++) {
				for (int i3 = 0; i3 < ky[2]; i3++) {
					for (int i4 = 0; i4 < ky[3]; i4++) {
						for (int i5 = 0; i5 < ky[4]; i5++) {
							for (int i6 = 0; i6 < ky[5]; i6++) {
								for (int i7 = 0; i7 < ky[6]; i7++) {
									for (int i8 = 0; i8 < ky[7]; i8++) {
										dp[i1][i2][i3][i4][i5][i6][i7][i8] = -1;
									}
								}
							}
						}
					}
				}
			}
		}

		java.util.Arrays.fill(lastMove, 0);
		int h = SG(a, 1);
		if (h == 0) {
			System.out.println(2);
		} else {
			System.out.println(1);
			if (lastMove[0] == 0) {
				for (int i = 0; i < n; i++) {
					lastMove[i] = 1;
				}
			}
			for (int i = 0; i < n; i++) {
				System.out.print(lastMove[i] + " ");
			}
		}
	}

	public static void fromConsole() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = scanner.nextInt();
		}
		scanner.close();
		GamesOfSquares gos = new GamesOfSquares();
		gos.solve(a);
	}

	public static void main(String[] args) {
		fromConsole();
	}

}