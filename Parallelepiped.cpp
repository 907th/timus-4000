#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <memory.h>
#include <algorithm>
#include <vector>
#include <string>
#include <map>
#include <set>

using namespace std;

double u, v, w;

double x[64];
double y[64];
double z[64];
int n = 0;

double l3d[64];
double l2d[64];
double l[64];

double best = 0;

void solve(double lx, double rx, double ly, double ry) {
	int m = 0;
	for (int i = 0; i < n; i++) {
		if (x[i] <= lx || x[i] >= rx) continue;
		if (y[i] <= ly || y[i] >= ry) continue;
		l[m] = z[i];
		m++;
	}
	l[m++] = 0;
	l[m++] = w;
	sort(l, l + m);
	for (int i = 1; i < m; i++) {
		double dz = l[i] - l[i - 1];
		if (best < dz * (rx - lx) * (ry - ly)) {
			best = dz* (rx - lx)* (ry - ly);
		}
	}
}

void solve2d(double lx, double rx) {

	int m = 0;
	for (int i = 0; i < n; i++) {
		if (x[i] <= lx || x[i] >= rx) continue;
		l2d[m] = y[i];
		m++;
	}
	l2d[m++] = 0;
	l2d[m++] = v;
	sort(l2d, l2d + m);

	for (int i = 0; i < m; i++) {
		for (int j = i + 1; j < m; j++) {
			solve(lx, rx, l2d[i], l2d[j]);
		}
	}

}

void solve3d() {

	int m = 0;
	for (int i = 0; i < n; i++) {
		l3d[m++] = x[i];
	}
	l3d[m++] = 0;
	l3d[m++] = u;
	sort(l3d, l3d + m);

	for (int i = 0; i < m; i++) {
		for (int j = i + 1; j < m; j++) {
			solve2d(l3d[i], l3d[j]);
		}
	}

}

int main() {
	int localn = 0;
	scanf("%lf%lf%lf", &u, &v, &w);
	scanf("%d", &localn);
	for (int i = 0; i < localn; i++) {
		double lx, ly, lz;
		scanf("%lf%lf%lf", &lx, &ly, &lz);
		if (lx <= 0 || lx >= u) continue;
		if (ly <= 0 || ly >= v) continue;
		if (lz <= 0 || lz >= w) continue;

		x[n] = lx;
		y[n] = ly;
		z[n] = lz;
		n++;
	}

	solve3d();
	printf("%.2lf", best);

	return 0;
}