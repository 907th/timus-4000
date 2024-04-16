#include <stdio.h>
#include <memory.h>

const unsigned P = 433494437;
const unsigned hs = 1 << 15;
const unsigned b1 = 19;
const unsigned b2 = 31;

unsigned h1[500001];
unsigned h2[500001];

unsigned s1[500001];
unsigned s2[500001];

char s[500001];
char q[500001];

int n;

unsigned M(__int64 a, __int64 b) {
	return (unsigned)((a * b) % P);
}

unsigned A(__int64 a, __int64 b) {
	return (unsigned)((a + b) % P);
}

unsigned S(__int64 a, __int64 b) {
	__int64 h = a - b;
	while(h < 0) {
		h += P;
	}
	return (unsigned)(h % P);
}

int main()
{
	scanf("%d", &n);
	scanf("%s", s);

	unsigned H1 = 0, H2 = 0;
	scanf("%s", q);

	for(int i = 1; i <= n; i++)
	{
		H1 = A(M(H1, b1), unsigned(q[i - 1]));
		H2 = A(M(H2, b2), unsigned(q[i - 1]));
	}

	for(int i = n; i < 2 * n; i++)
		s[i] = s[i - n];

	s1[0] = 1;
	s2[0] = 1;
	for(int i = 1; i < 2 * n; i++)
	{
		s1[i] = M(s1[i - 1], b1);
		s2[i] = M(s2[i - 1], b2);
	}

	for(int i = 1; i <= 2 * n; i++)
	{
		h1[i] = A(M(h1[i - 1], b1), unsigned(s[i - 1]));
		h2[i] = A(M(h2[i - 1], b2), unsigned(s[i - 1]));
	}

	if(h1[n] == H1 && h2[n] == H2)
	{
		printf("0\n"); return 0;
	}

	for(int i = n + 1; i <= 2 * n; i++)
	{
		unsigned hash1 = S(h1[i], M(h1[i - n], s1[n]));
		unsigned hash2 = S(h2[i], M(h2[i - n], s2[n]));
		if(H1 == hash1 && H2 == hash2)
		{
			if(memcmp(q, s + i - n, n) == 0)
			{
				if(i == 2 * n) i = n;
				printf("%d\n", n - (i - n));
				return 0;
			}
		}
	}
	printf("-1\n");
	return 0;
}