#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <memory.h>
#include <algorithm>
#include <vector>

using namespace std;

const int N = 256 * 1024;

const int P = 174440041;

unsigned int HP1 = 19997;
unsigned int HP2 = 1069;
unsigned int HP3 = 10079;

struct CyclicString {
	int c1, c2, p;
};

bool operator < (CyclicString a, CyclicString b) {
	if (a.c1 == b.c1) {
		if (a.c2 == b.c2) {
			return a.p < b.p;
		}
		return a.c2 < b.c2;
	}
	return a.c1 < b.c1;
}

CyclicString a[N];
int cl[N];
int n;
char s[N];
double sum = 0;

void print(int start, int length) {
	printf("%d ", cl[start]);
	for (int i = 0; i < length; i++) {
		printf("%c", s[start]);
		start++;
		if (start >= n) start -= n;
	}
	printf("\n");
}

int hv1[N];
int hv2[N];
int hv3[N];
int pw1[N];
int pw2[N];
int pw3[N];

inline int minusP(int a, int b) {
	int x = a - b;
	if (x < 0) x += P;
	return x;
}

inline int plusP(int a, int b) {
	int x = a + b;
	if (x >= P) x -= P;
	return x;
}

inline int multP(__int64 a, __int64 b) {
	__int64 x = a * b;
	return (int)(x % P);
}

int hf1(int index, int len) {
	if (index + len > n) {
		int len2 = index + len - n;
		int h1 = minusP(hv1[n - 1], multP(hv1[index - 1], pw1[n - index]));
		return plusP(multP(h1, pw1[len2]), hv1[len2 - 1]);
	}
	else {
		if(index == 0) {
			return hv1[index + len - 1];
		}
		return minusP(hv1[index + len - 1], multP(hv1[index - 1], pw1[len]));
	}
}

int hf2(int index, int len) {
	if (index + len > n) {
		int len2 = index + len - n;
		int h1 = minusP(hv2[n - 1], multP(hv2[index - 1], pw2[n - index]));
		return plusP(multP(h1, pw2[len2]), hv2[len2 - 1]);
	}
	else {
		if (index == 0) {
			return hv2[index + len - 1];
		}
		return minusP(hv2[index + len - 1], multP(hv2[index - 1], pw2[len]));
	}
}

int hf3(int index, int len) {
	if (index + len > n) {
		int len2 = index + len - n;
		int h1 = minusP(hv3[n - 1], multP(hv3[index - 1], pw3[n - index]));
		return plusP(multP(h1, pw3[len2]), hv3[len2 - 1]);
	}
	else {
		if (index == 0) {
			return hv3[index + len - 1];
		}
		return minusP(hv3[index + len - 1], multP(hv3[index - 1], pw3[len]));
	}
}

bool isOk(int i1, int i2, int len) {
	bool isOk = hf1(i1, len) == hf1(i2, len) && hf2(i1, len) == hf2(i2, len);
	return isOk;
}

int gcpLength(int index1, int index2) {
	int l = 0;
	int r = n + 1;
	while (r - l > 1) {
		int m = (r + l) / 2;
		if (isOk(index1, index2, m)) {
			l = m;
		}
		else {
			r = m;
		}
	}
	return l;
}

void init() {
	pw1[0] = 1;
	pw2[0] = 1;
	pw3[0] = 1;
	hv1[0] = s[0] - 'A' + 1;
	hv2[0] = s[0] - 'A' + 1;
	hv3[0] = s[0] - 'A' + 1;
	for (int i = 1; i < n; i++) {
		pw1[i] = multP(pw1[i - 1], HP1);
		pw2[i] = multP(pw2[i - 1], HP2);
		pw3[i] = multP(pw3[i - 1], HP3);

		hv1[i] = plusP(multP(hv1[i - 1], HP1), (unsigned int)(s[i] - 'A' + 1));
		hv2[i] = plusP(multP(hv2[i - 1], HP2), (unsigned int)(s[i] - 'A' + 1));
		hv3[i] = plusP(multP(hv3[i - 1], HP3), (unsigned int)(s[i] - 'A' + 1));
	}
}

void solve() {
	for (int i = 0; i < n; i++) {
		cl[i] = s[i] - 'A';
	}

	for (int k = 1; (1 << (k - 1)) <= n; k++) {

		for (int i = 0; i < n; i++) {
			a[i].c1 = cl[i];
			int nx = (i + (1 << (k - 1)));
			while (nx >= n) nx -= n;
			a[i].c2 = cl[nx];
			a[i].p = i;
		}

		sort(a, a + n);

		int classNo = 0;
		for (int i = 0; i < n; i++) {
			if (i > 0) {
				if (a[i - 1].c1 != a[i].c1 || a[i - 1].c2 != a[i].c2) {
					classNo++;
				}
			}
			cl[a[i].p] = classNo;
		}

	}

	for (int i = 1; i < n; i++) {
		int h = gcpLength(a[i - 1].p, a[i].p);
		sum += h;
	}

	printf("%.9lf\n", sum / (n - 1.0));

}

int main() {
	scanf("%d", &n);
	scanf("%s", s);
	init();
	solve();
	return 0;
}