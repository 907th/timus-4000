#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <memory.h>

const int N = 512;
const int M = 250004;

const int P = 1000000007;

int dp[2][N][2][2][2][2];

int cnt[M][2][2];

int power(__int64 v, int d) {
	if (d == 0) return 1;
	if (d == 1) return (int)v;
	__int64 h = power(v, d / 2);
	if (d % 2 == 0) {
		return (int)((h * h) % P);
	}
	else {
		return (int)((__int64)(((h * h) % P) * v) % P);
	}
}

int initialC(int n, int k) {
	int answer = 1;
	for (int i = n - k + 1; i <= n; i++) {
		answer = (int)(((__int64)answer * (__int64)i) % P);
	}
	for (int i = 1; i <= k; i++) {
		answer = (int)(((__int64)answer * (__int64)power(i, P - 2)) % P);
	}
	return answer;
}

int minus(int x, int y) {
	return ((x - y) < 0) ? (x - y + P) : (x - y);
}
int plus(int x, int y) {
	return (x + y) % P;
}

int mult(__int64 x, __int64 y) {
	return (int)((x * y) % P);
}

int solve(int n, int k) {
	dp[1][0][0][0][0][0] = 1;

	int count = n / 2;
	int cur = 1;
	for (int i = 1; i <= count; i++) {
		cur = 1 - cur;

		for (int j = 0; j <= count; j++)
			for (int p1 = 0; p1 < 2; p1++)
				for (int p2 = 0; p2 < 2; p2++)
					for (int p3 = 0; p3 < 2; p3++)
						for (int p4 = 0; p4 < 2; p4++)
							dp[cur][j][p1][p2][p3][p4] = 0;

		for (int j = 0; j <= count; j++) {
			for (int p1 = 0; p1 < 2; p1++) {
				for (int p2 = 0; p2 < 2; p2++) {
					for (int p3 = 0; p3 < 2; p3++) {
						for (int p4 = 0; p4 < 2; p4++) {

							dp[cur][j][p1][p2][p3][p4] = (dp[cur][j][p1][p2][p3][p4] + dp[1 - cur][j][p1][p2][p3][p4]) % P;

							if (j > 0) {
								dp[cur][j][1][p2][p3][p4] =
									(dp[cur][j][1][p2][p3][p4] + dp[1 - cur][j - 1][p1][p2][p3][p4]) % P;

								dp[cur][j][p1][1][p3][p4] =
									(dp[cur][j][p1][1][p3][p4] + dp[1 - cur][j - 1][p1][p2][p3][p4]) % P;

								dp[cur][j][p1][p2][1][p4] =
									(dp[cur][j][p1][p2][1][p4] + dp[1 - cur][j - 1][p1][p2][p3][p4]) % P;

								dp[cur][j][p1][p2][p3][1] =
									(dp[cur][j][p1][p2][p3][1] + dp[1 - cur][j - 1][p1][p2][p3][p4]) % P;
							}
						}
					}
				}
			}

		}
	}


	for (int i = 0; i <= n / 2; i++) {
		for (int j = 0; j <= n / 2; j++) {
			if (i + j <= k) {

				for (int m1 = 0; m1 < 16; m1++) {
					int p11 = (((1 << 0) & m1) == 0) ? 0 : 1;
					int p12 = (((1 << 1) & m1) == 0) ? 0 : 1;
					int p13 = (((1 << 2) & m1) == 0) ? 0 : 1;
					int p14 = (((1 << 3) & m1) == 0) ? 0 : 1;

					if (p13 == 1) continue;

					for (int m2 = 0; m2 < 16; m2++) {
						int p21 = (((1 << 0) & m2) == 0) ? 0 : 1;
						int p22 = (((1 << 1) & m2) == 0) ? 0 : 1;
						int p23 = (((1 << 2) & m2) == 0) ? 0 : 1;
						int p24 = (((1 << 3) & m2) == 0) ? 0 : 1;

						if (p21 == 1) continue;

						int hasWhite = ((p11 + p12 + p21 + p22) > 0) ? 1 : 0;
						int hasBlack = ((p13 + p14 + p23 + p24) > 0) ? 1 : 0;

						cnt[i + j][hasWhite][hasBlack] =
							plus(
								cnt[i + j][hasWhite][hasBlack],
								mult(dp[cur][i][p11][p12][p13][p14], dp[cur][j][p21][p22][p23][p24])
							);

					}
				}

			}
		}
	}

	int currentN = n / 2 * (n - 2);
	int currentC = initialC(currentN, k);
	int answer = mult(currentC, minus(minus(power(4, k), power(2, k)), power(2, k)));

	int cK = k;
	for (int i = 1; i <= k; i++) {

		currentC = mult(currentC, mult(cK, power(currentN - cK + 1, P - 2)));
		cK--;

		answer = plus(answer,
			plus(
				plus(
					mult(currentC, mult(power(4, k - i), cnt[i][1][1])),
					mult(currentC, mult(minus(power(4, k - i), power(2, k - i)), cnt[i][0][1]))
				),
				mult(currentC, mult(minus(power(4, k - i), power(2, k - i)), cnt[i][1][0]))
			)
		);

	}

	return answer;
}


int main() {
	int n, k;
	scanf("%d%d", &n, &k);
	printf("%d", solve(n, k));
	return 0;
}