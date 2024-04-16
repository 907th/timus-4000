#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <algorithm>

using namespace std;

const int N = 64;
const int M = 2402;

__int64 dp[2][72][M];

int h, m, n;

__int64 fdp[62][M];

int requestsCount = 0;
__int64 requests[N];
int answers[N][62];

int currentLength[N];
int currentCount[N];

int main()
{
	scanf("%d%d%d", &n, &h, &m);
	if (n > M - 2) 
		n = M - 2;
	while (true) {
		__int64 x = 0;
		scanf("%I64d", &x);
		if (x == -1) break;
		answers[requestsCount][0] = m;
		currentLength[requestsCount] = m;
		currentCount[requestsCount] = n;
		requests[requestsCount++] = x;
	}

	int current = 0;
	for (int i = 1; i <= 70; i++) {
		dp[current][i][i] = 1;
	}
	fdp[1][1] = dp[current][1][1];

	for (int i = 2; i <= h; i++) {
		current = 1 - current;
		memset(dp[current], 0, sizeof(dp[current]));
		for (int j = 1; j <= M - 2; j++) {
			for (int k = 1; k <= 70; k++) {
				if (j + k >= M) continue;
				dp[current][k][j + k] = dp[1 - current][k - 1][j] + dp[1 - current][k + 1][j];
			}
			fdp[i][j] = dp[current][1][j];
		}
	}

	__int64 answer = 0;
	for (int i = 1; i <= n; i++) {
		answer += dp[current][m][i];
	}
	printf("%I64d\n", answer);

	for (int digit = 1; digit < h; digit++) {
		for (int i = 0; i < requestsCount; i++) {
			__int64 localCount = 0;
			for (int j = 1; j <= currentCount[i] - currentLength[i]; j++) {
				localCount += dp[1 - current][currentLength[i] - 1][j];
			}
			if (localCount >= requests[i]) {
				currentCount[i] -= currentLength[i];
				currentLength[i]--;
			}
			else {
				requests[i] -= localCount;
				currentCount[i] -= currentLength[i];
				currentLength[i]++;
			}
			answers[i][digit] = currentLength[i];
		}
		for (int j = 1; j <= 2400; j++) {
			dp[current][1][j] = fdp[h - digit - 1][j];
			dp[current][2][j] = dp[1 - current][1][j + 1];

			for (int k = 3; k <= 70; k++) {
				dp[current][k][j] = dp[1 - current][k - 1][j + k - 1] - dp[current][k - 2][j];
			}
		}
		current = 1 - current;
	}
	
	for (int i = 0; i < requestsCount; i++) {
		for (int j = 0; j < h; j++) {
			printf("%d ", answers[i][j]);
		}
		printf("\n");
	}
	return 0;
}