#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <math.h>

const double eps = 1e-9;

struct point
{
	double x, y;
};

point p[64];
double XX, YY;
int n;

inline double Q(double x) { return x * x; }

double Min(double x, double y) { return x < y ? x : y; }

double Max(double x, double y) { return x > y ? x : y; }

double vect(double X, double Y, double X1, double Y1, double X2, double Y2)
{
	return ((X1 - X) * (Y2 - Y) - (X2 - X) * (Y1 - Y));
}

double R(double X1, double Y1, double X2, double Y2)
{
	return sqrt(Q(X1 - X2) + Q(Y1 - Y2));
}

double R2(double X1, double Y1, double X2, double Y2)
{
	return Q(X1 - X2) + Q(Y1 - Y2);
}

bool Inside(double x, double y)
{
	double sum = 0;
	for (int i = 0; i < n; i++)
	{
		double d1 = R(p[i].x, p[i].y, x, y);
		double d2 = R(p[i + 1].x, p[i + 1].y, x, y);
		double d3 = R(p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);

		double d21 = R2(p[i].x, p[i].y, x, y);
		double d22 = R2(p[i + 1].x, p[i + 1].y, x, y);
		double d23 = R2(p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);

		double cor = acos((d23 - d21 - d22) / d1 / d2 / -2.0);

		if (fabs(vect(x, y, p[i].x, p[i].y, p[i + 1].x, p[i + 1].y)) < eps) continue;

		if (vect(x, y, p[i].x, p[i].y, p[i + 1].x, p[i + 1].y) < 0) cor = -cor;
		sum += cor;
	}
	return (fabs(sum) > 1);
}

double Short(double x, double y, double X1, double Y1, double X2, double Y2)
{
	double HELP = vect(x, y, X1, Y1, X2, Y2);
	double SQ = (HELP > 0 ? HELP : -HELP);
	double H = SQ / R(X1, Y1, X2, Y2);

	double d1 = R(X1, Y1, x, y);
	double d2 = R(X2, Y2, x, y);
	double d3 = R(X2, Y2, X1, Y1);

	if (Q(H) + Q(d3) < Q(d1)) return Min(d1, d2);
	if (Q(H) + Q(d3) < Q(d2)) return Min(d1, d2);

	return H;
}

bool YES(double RR)
{
	for (int i = 0; i < n; i++)
	{
		double H = Short(XX, YY, p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);
		if (R(XX, YY, p[i].x, p[i].y) + eps < RR) return 0;
		if (H + eps < RR) return 0;
	}
	return 1;
}

bool check2(double k1, double b1, double x, double R) {
	XX = x;
	YY = k1 * XX + b1;
	if (YES(R) && Inside(XX, YY)) return 1;

	return 0;
}

bool check(double k1, double b1, double k2, double b2, double R) {
	if (fabs(k1 - k2) < eps) return 0;

	XX = (b2 - b1) / (k1 - k2);
	YY = k1 * XX + b1;

	if (YES(R) && Inside(XX, YY)) return 1;

	return 0;
}

bool isOkBetweenTwoLines(double X1, double Y1, double X2, double Y2, double X3, double Y3, double X4, double Y4, double R)
{
	//distance between two parallel lines is |b2 - b1| / (SQRT(k * k + 1))
	double k1 = (Y2 - Y1) / (X2 - X1);
	double b1 = Y1 - X1 * (Y2 - Y1) / (X2 - X1);

	double b11 = b1 - R * (sqrt(k1 * k1 + 1));
	double b12 = R * (sqrt(k1 * k1 + 1)) + b1;

	double k2 = (Y4 - Y3) / (X4 - X3);
	double b2 = Y3 - X3 * (Y4 - Y3) / (X4 - X3);

	double b21 = b2 - R * (sqrt(k2 * k2 + 1));
	double b22 = R * (sqrt(k2 * k2 + 1)) + b2;

	if (fabs(X2 - X1) < eps) {
		if (fabs(X4 - X3) < eps) {
			XX = (X1 + X3) / 2.0;
			YY = (Y1 + Y2) / 2.0;
			if (YES(R) && Inside(XX, YY)) return 1;
			return 0;
		}
		else {
			if (check2(k2, b21, X1 + R, R)) return 1;
			if (check2(k2, b22, X1 + R, R)) return 1;
			if (check2(k2, b21, X1 - R, R)) return 1;
			if (check2(k2, b22, X1 - R, R)) return 1;
		}
	}

	if (fabs(X4 - X3) < eps) {
		if (check2(k1, b11, X3 + R, R)) return 1;
		if (check2(k1, b12, X3 + R, R)) return 1;
		if (check2(k1, b11, X3 - R, R)) return 1;
		if (check2(k1, b12, X3 - R, R)) return 1;
	}

	if (check(k1, b11, k2, b21, R)) return 1;
	if (check(k1, b11, k2, b22, R)) return 1;
	if (check(k1, b12, k2, b21, R)) return 1;
	if (check(k1, b12, k2, b22, R)) return 1;

	return 0;
}

bool isOkBetweenPointAndPoint(double X1, double Y1, double X2, double Y2, double m) {

	if (fabs(X1 - X2) < eps)
	{
		YY = (Y1 + Y2) / 2.0;

		if (Q(m) - Q(fabs(Y1 - YY)) < -eps) {
			XX = X1;
			if (YES(m) && Inside(XX, YY)) return 1;
			return 0;
		}

		double diff = sqrt(Q(m) - Q(fabs(Y1 - YY)));

		XX = X1 + diff;
		if (YES(m) && Inside(XX, YY)) return 1;

		XX = X1 - diff;
		if (YES(m) && Inside(XX, YY)) return 1;
		return 0;
	}

	double K1 = (Q(X1) + Q(Y1) - Q(X2) - Q(Y2)) / (2.0 * X1 - 2.0 * X2);
	double K2 = (2.0 * Y1 - 2.0 * Y2) / (2.0 * X1 - 2.0 * X2);

	double a = Q(K2) + 1;
	double b = -(2.0 * Y1 + 2.0 * K2 * (K1 - X1));
	double c = -(Q(m) - Q(X1 - K1) - Q(Y1));

	if (fabs(a) < eps)
	{
		YY = -c / b;
		XX = K1 - K2 * YY;
		if (YES(m) && Inside(XX, YY)) return 1;
		return 0;
	}

	double D = Q(b) - 4.0 * a * c;

	if (D < -eps) {
		XX = (X1 + X2) / 2.0;
		YY = (Y1 + Y2) / 2.0;
		if (YES(m) && Inside(XX, YY)) return 1;
		return 0;
	}

	if (fabs(D) < eps) D = 0.0;
	D = sqrt(D);

	YY = (-b - D) / (2.0 * a);
	XX = K1 - K2 * YY;
	if (YES(m) && Inside(XX, YY)) return 1;

	YY = (-b + D) / (2.0 * a);
	XX = K1 - K2 * YY;
	if (YES(m) && Inside(XX, YY)) return 1;
	return 0;
}

bool solveQEquation(double a, double b, double c, double& x1, double& x2) {
	double D = Q(b) - 4 * a * c;
	if (D < 0) {
		return false;
	}

	x1 = (-b + sqrt(D)) / 2 / a;
	x2 = (-b - sqrt(D)) / 2 / a;

	return 1;
}

bool isOkBetweenPointAndLine(double PX, double PY, double X1, double Y1, double X2, double Y2, double m) {

	double A = Y2 - Y1;
	double B = -(X2 - X1);
	double P = m * sqrt(Q(A) + Q(B));

	double C = -P - X1 * (Y2 - Y1) + Y2 * (X2 - X1);

	if (fabs(A) < eps) {
		YY = -C / B;
		XX = sqrt(Q(m) - Q(YY - PY)) + PX;
		if (YES(m) && Inside(XX, YY)) return 1;

		XX = -sqrt(Q(m) - Q(YY - PY)) + PX;
		if (YES(m) && Inside(XX, YY)) return 1;
	}
	else
		if (fabs(B) < eps) {
			XX = -C / A;
			YY = sqrt(Q(m) - Q(XX - PX)) + PY;
			if (YES(m) && Inside(XX, YY)) return 1;

			YY = -sqrt(Q(m) - Q(XX - PX)) + PY;
			if (YES(m) && Inside(XX, YY)) return 1;
		}
		else {

			double x1 = 0, x2 = 0;
			bool hasResult = false;
			if (solveQEquation(1 + Q(A / B), -2 * PX + 2 * A / B * (C / B + PY), Q(PX) + Q(C / B + PY) - Q(m), x1, x2)) {

				XX = x1;
				YY = (-C - A * XX) / B;
				if (YES(m) && Inside(XX, YY)) return 1;

				XX = x2;
				YY = (-C - A * XX) / B;

				if (YES(m) && Inside(XX, YY)) return 1;
			}
		}

	return 0;
}

bool Ok(double m)
{
	if(m <= 0) return 1;
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++)
		{
			if (i != j)
			{
				if (isOkBetweenTwoLines(p[i].x, p[i].y, p[i + 1].x, p[i + 1].y, p[j].x, p[j].y, p[j + 1].x, p[j + 1].y, m)) {
					isOkBetweenTwoLines(p[i].x, p[i].y, p[i + 1].x, p[i + 1].y, p[j].x, p[j].y, p[j + 1].x, p[j + 1].y, m);
					return 1;
				}
			}
		}

	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < n; j++)
		{
			if (i == j) continue;
			if (isOkBetweenPointAndPoint(p[i].x, p[i].y, p[j].x, p[j].y, m)) {
				return 1;
			}
		}
	}
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < n; j++)
		{
			//if (i == j || i == (j + 1) % n) continue;
			if (isOkBetweenPointAndLine(p[i].x, p[i].y, p[j].x, p[j].y, p[j + 1].x, p[j + 1].y, m)) {
				return 1;
			}
		}
	}
	return 0;
}

double dix(double l, double r)
{
	while (r - l > eps)
	{
		double m = (l + r) / 2.0;
		if (Ok(m)) l = m; else r = m;
	}
	return l;
}

int main()
{
	scanf("%d", &n);
	for (int i = 0; i < n; i++)
		scanf("%lf%lf", &p[i].x, &p[i].y);

	p[n] = p[0];
	double ar = dix(0, 1000);
	if (ar < eps) ar = 0;
	printf("%.2lf\n", ar);

	Ok(ar);

	/*printf("{\"lines\":[");
	for (int i = 0; i < n; i++) {
		printf("{\"x1\": %lf, \"y1\": %lf, \"x2\": %lf, \"y2\": %lf}", p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);
		if (i != n - 1) printf(",\n");
	}
	printf("],\n\"circles\":[");
	printf("{\"x\": %lf, \"y\": %lf, \"r\": %lf}", XX, YY, ar);
	printf("]}\n");*/
	return 0;
}