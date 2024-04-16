#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <string.h>
#include <map>

using namespace std;

const int N = 128;
const int N1 = 256;
const int NN = 1024 * 1024;

struct ful
{
	char s[N];
};

bool operator < (ful a, ful b)
{
	return strcmp(a.s, b.s) < 0;
}

map<ful, int> M;

struct str
{
	ful sur;
	char que[N1];
	int c;
	char a[N1][N];
	char b[N1][N1];
};

char MS[N1];
char s[NN];
str a[N1];
int n, m;

int d[N][N];
int p1[N][N];
int f1[N][N];
int p2[N][N];
int f2[N][N];

int sum1[N];
int sum2[N];

char q[NN][N1];

void Print(int x, int y)
{
	printf("%s - %s\n", MS, s + 8);
	printf("%s %s\n", a[x].sur.s, a[x].que);
	for (int i = 0; i < a[x].c; i++)
	{
		printf(" %s%s\n", a[x].a[i], a[x].b[i]);
	}

	printf("%s %s\n", a[y].sur.s, a[y].que);
	for (int i = 0; i < a[y].c; i++)
	{
		printf(" %s%s\n", a[y].a[i], a[y].b[i]);
	}

	printf("\n       ");
	for (int i = 0; i < a[y].c; i++)
		printf("%s:%s ", a[y].sur.s, a[y].a[i]);

	printf("TOTAL\n");

	memset(sum1, 0, sizeof(sum1));
	memset(sum2, 0, sizeof(sum2));
	memset(d, 0, sizeof(d));
	memset(f1, 0, sizeof(f1));
	memset(f2, 0, sizeof(f2));
	memset(p1, 0, sizeof(p1));
	memset(p2, 0, sizeof(p2));
	int tot = 0;
	for (int i = 0; i < a[x].c; i++)
	{
		for (int j = 0; j < a[y].c; j++)
		{
			for (int k = 0; k < m; k++)
			{
				if (q[k][x] == a[x].a[i][0] && q[k][y] == a[y].a[j][0])
				{
					sum1[i]++;
					sum2[j]++;
					d[i][j]++;
					tot++;
					p1[i][j] = p2[i][j] = 0;
				}
			}
		}
	}

	for (int i = 0; i < a[x].c; i++)
	{
		for (int j = 0; j < a[y].c; j++)
		{
			if (sum1[i])
			{
				if ((d[i][j] * 100) % sum1[i] != 0)
				{
					f1[i][j] = 1;
				}

				p1[i][j] = (d[i][j] * 100) / sum1[i];
			}

			if (sum2[j])
			{
				if ((d[i][j] * 100) % sum2[j] != 0)
				{
					f2[i][j] = 1;
				}

				p2[i][j] = (d[i][j] * 100) / sum2[j];
			}
		}
		f2[i][a[y].c] = 0;
		d[i][a[y].c] = sum1[i];
		p1[i][a[y].c] = 100;
		if (!sum1[i]) p1[i][a[y].c] = 0;

		if ((sum1[i] * 100) % tot != 0) f2[i][a[y].c] = 1;

		p2[i][a[y].c] = (sum1[i] * 100) / tot;
	}

	for (int j = 0; j < a[y].c; j++)
	{
		f1[a[x].c][j] = 0;
		d[a[x].c][j] = sum2[j];
		p2[a[x].c][j] = 100;
		if (!sum2[j]) p2[a[x].c][j] = 0;

		if ((sum2[j] * 100) % tot != 0) f1[a[x].c][j] = 1;

		p1[a[x].c][j] = (sum2[j] * 100) / tot;
	}

	d[a[x].c][a[y].c] = tot;
	p1[a[x].c][a[y].c] = 100;
	p2[a[x].c][a[y].c] = 100;

	if (!tot) p1[a[x].c][a[y].c] = p2[a[x].c][a[y].c] = 0;

	for (int i = a[x].c; i >= 0; i--)
	{
		int sum = 0;
		for (int j = a[y].c - 1; j >= 0; j--)
			sum += p1[i][j];

		for (int j = a[y].c - 1; j >= 0 && sum < 100; j--)
		{
			if (f1[i][j])
			{
				sum++;
				p1[i][j]++;
				f1[i][j] = 0;
			}
		}
		if (sum < 100 && sum != 0 || sum > 100) while (1);
	}

	for (int i = a[y].c; i >= 0; i--)
	{
		int sum = 0;

		for (int j = a[x].c - 1; j >= 0; j--)
			sum += p2[j][i];

		for (int j = a[x].c - 1; j >= 0 && sum < 100; j--)
		{
			if (f2[j][i])
			{
				sum++;
				p2[j][i]++;
				f2[j][i] = 0;
			}
		}
		if (sum < 100 && sum != 0 || sum > 100) while (1);
	}

	for (int i = 0; i <= a[x].c; i++)
	{
		if (i > 0) printf("\n");
		if (i == a[x].c)
			printf(" TOTAL"); else
			printf(" %s:%s", a[x].sur.s, a[x].a[i]);

		for (int j = 0; j <= a[y].c; j++)
		{
			printf("%6d", d[i][j]);
		}
		printf("\n       ");
		for (int j = 0; j <= a[y].c; j++)
		{
			if (j > 0) printf(" ");
			if (sum1[i] == 0 && i < a[x].c)
				printf("%5c", '-'); else
				printf("%4d%c", p1[i][j], '%');
		}
		printf("\n       ");
		for (int j = 0; j <= a[y].c; j++)
		{
			if (j > 0) printf(" ");
			if (sum2[j] == 0 && j < a[y].c)
				printf("%5c", '-'); else
				printf("%4d%c", p2[i][j], '%');
		}
	}
}

int main()
{
	//freopen("out.txt", "w", stdout);
	gets_s(MS);

	bool B = false;

	while (true)
	{
		if (!B) gets_s(s);
		sscanf(s, "%s", a[n].sur.s);

		for (int i = 4; i < strlen(s); i++)
			a[n].que[i - 4] = s[i];

		if (a[n].sur.s[0] == '#') break;

		M[a[n].sur] = n;

		while (true)
		{
			gets_s(s);
			sscanf(s, "%s", a[n].a[a[n].c]);

			if (strlen(a[n].a[a[n].c]) != 1 || a[n].a[a[n].c][0] == '#') break;

			for (int i = 2; i < strlen(s); i++)
				a[n].b[a[n].c][i - 2] = s[i];

			a[n].c++;
		}
		n++;
		B = true;
	}

	while (true)
	{
		gets_s(q[m]);
		if (q[m][0] == '#') break;
		m++;
	}

	ful f1, f2;
	int bll = 0;

	while (true)
	{
		if (bll) printf("\n\n");
		gets_s(s);
		if (s[0] == '#') break;
		sscanf(s, "%s%s", f1.s, f2.s);
		Print(M[f1], M[f2]);
		bll = 1;
	}

	return 0;
}