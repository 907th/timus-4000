#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <map>

using namespace std;

typedef __int64 longType;

const int P = 433494437;
const int H = 107;

struct pr {
	int x, y;
};

int n, m;
char a[512][512];
int cnt[256];

int ax1, ay1, ax2, ay2;

int highestBit[512];

int v[256][2];

longType hsh[9][512][512];

longType pw[10];

longType M(longType a, longType b) {
	return (a * b) % P;
}

longType A(longType a, longType b) {
	return (a + b) % P;
}

longType generateHash(longType q1, longType q2, longType q3, longType q4, int size) {
	longType q = A(M(A(M(A(M(q1, pw[size]), q2), pw[size]), q3), pw[size]), q4);
	return q;
}

void initHashes() {

	for (int i = 0; i < 256; i++) {
		v[i][0] = -1;
		v[i][1] = -1;
	}

	for (int i = 1; i < 512; i++) {
		for (int j = 10; j >= 0; j--) {
			if (((1 << j) & i) != 0) {
				highestBit[i] = j;
				break;
			}
		}
	}

	for (int i = 0; i < 10; i++) {
		int s = 1 << i;
		pw[i] = 1;
		for (int j = 0; j < s * s; j++) {
			pw[i] = M(pw[i], H);
		}
	}

	for (int i = 0; i < n; i++) {
		for (int j = 0; j < m; j++) {
			hsh[0][i][j] = a[i][j] - 'a';
		}
	}
	for (int size = 1; size < 9; size++) {
		if ((1 << size) > n || (1 << size) > m) break;

		int s = 1 << size;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (i + s - 1 >= n || j + s - 1 >= m) continue;
				hsh[size][i][j] = generateHash(
					hsh[size - 1][i][j],
					hsh[size - 1][i + s / 2][j],
					hsh[size - 1][i][j + s / 2],
					hsh[size - 1][i + s / 2][j + s / 2], size);
			}
		}
	}
}

map<longType, pr> mapHash;

void store(int x, int y, longType q) {
	ax1 = -1;
	ay1 = -1;
	pr p; p.x = x; p.y = y;
	if (mapHash.find(q) != mapHash.end()) {
		ax1 = mapHash[q].x;
		ay1 = mapHash[q].y;
		return;
	}
	mapHash[q] = p;
}

bool isOk(int size) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			if (a[ax1 + i][ay1 + j] != a[ax2 + i][ay2 + j]) return false;
		}
	}
}

bool findAnswer(int s) {
	int hb = highestBit[s];
	int sz = 1 << hb;
	if (sz == s) {
		hb--;
		sz = 1 << hb;
	}
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < m; j++) {
			if (i + s - 1 >= n || j + s - 1 >= m) continue;
			longType q1 = hsh[hb][i][j];
			longType q2 = hsh[hb][i + s - sz][j];
			longType q3 = hsh[hb][i][j + s - sz];
			longType q4 = hsh[hb][i + s - sz][j + s - sz];
			longType hashValue = generateHash(q1, q2, q3, q4, hb - 1);

			store(i, j, hashValue);
			if (ax1 != -1 && ay1 != -1) {
				ax2 = i;
				ay2 = j;
				if (isOk(s)) return true;
			}

		}
	}
	return false;
}

void solve() {
	int l = 1;
	int r = n;
	while (r - l > 1) {
		mapHash.clear();
		int m = (r + l) / 2;
		bool answer = findAnswer(m);
		if (!answer) {
			r = m;
		}
		else {
			l = m;
		}
	}
	mapHash.clear();
	if (l == 1) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (v[a[i][j]][0] != -1) {
					printf("%d\n%d %d\n%d %d\n", 1, v[a[i][j]][0] + 1, v[a[i][j]][1] + 1, i + 1, j + 1);
					return;
				}
				else {
					v[a[i][j]][0] = i;
					v[a[i][j]][1] = j;
				}
			}
		}
	}
	else {
		findAnswer(l);
	}
	printf("%d\n%d %d\n%d %d\n", l, ax1 + 1, ay1 + 1, ax2 + 1, ay2 + 1);
}

int main() {
	scanf("%d%d", &n, &m);
	bool hasAnswer = false;
	for (int i = 0; i < n; i++) {
		scanf("%s", a[i]);
		for (int j = 0; j < m; j++) {
			if (cnt[a[i][j]] > 0) hasAnswer = true;
			cnt[a[i][j]]++;
		}
	}
	if (!hasAnswer) {
		printf("0\n");
		return 0;
	}

	initHashes();

	solve();

	return 0;
}