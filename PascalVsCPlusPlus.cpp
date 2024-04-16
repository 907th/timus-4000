#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <algorithm>

using namespace std;

int EDGE = 10000;
const int N = 10048;
const int M = 8;
const int CHECKS_COUNT_LIMIT = 70;

const int hashSize = 100007;
const int totalSize = N * 40;
const int totalSizeA = 18496;
const unsigned int P1 = 41, P2 = 107;

const int BC = 8;

int bestCount = 0;
int best[BC];
int bestStart[BC];
int bestDiff[BC];

struct str
{
	int a;
	short int id;
};

int first[hashSize];
int firstA[totalSizeA];
int freeIndexes[totalSize];
int startIndexes = 0;

int bestTotal = 1;
int bestFinalStart = 0;
int bestFinalDiff = 0;

short int hashNodesX[totalSize];
short int hashNodesY[totalSize];
short int hashNodesValue[totalSize];
int hashNodesNext[totalSize];

int startIndexesA = 0;
int hashNodesA[totalSizeA];
short int hashNodesAValue[totalSizeA];
short int hashNodesANext[totalSizeA];
short int freeIndexesA[totalSizeA];

str a[N];
str ok[N];
int n;

bool operator < (str a, str b)
{
	return a.a < b.a;
}

int same[N];
int b[N];

inline int maxx(int x, int y) {
	return x > y ? x : y;
}

void storeA(int x, int v) {
	int currentIndex = freeIndexesA[startIndexesA++];
	if (startIndexesA >= totalSizeA) startIndexesA = 0;
	unsigned int key = x * P2;
	int chainIndex = key % totalSizeA;
	hashNodesA[currentIndex] = x;
	hashNodesAValue[currentIndex] = v;
	hashNodesANext[currentIndex] = firstA[chainIndex];
	firstA[chainIndex] = currentIndex;
}

int findA(int x) {
	unsigned int key = x * P2;
	int chainIndex = key % totalSizeA;
	for (int index = firstA[chainIndex]; index != -1; index = hashNodesANext[index]) {
		if (hashNodesA[index] == x) return hashNodesAValue[index];
	}
	return -1;
}

inline unsigned int getKey(unsigned int x, unsigned int y) {
	return x * P1 + y * P2;
}

void store(int x, int y, int v) {
	int currentIndex = freeIndexes[startIndexes++];
	if (startIndexes >= totalSize) startIndexes = 0;
	unsigned int key = getKey(x, y);
	int chainIndex = key % hashSize;
	hashNodesX[currentIndex] = x;
	hashNodesY[currentIndex] = y;
	hashNodesValue[currentIndex] = v;
	hashNodesNext[currentIndex] = first[chainIndex];
	first[chainIndex] = currentIndex;
}

int remove(int x, int y) {
	unsigned int key = getKey(x, y);
	int chainIndex = key % hashSize;

	int prevIndex = -1;
	for (int index = first[chainIndex]; index != -1; index = hashNodesNext[index]) {

		if (hashNodesX[index] == x && hashNodesY[index] == y) {
			if (index == first[chainIndex]) {
				first[chainIndex] = hashNodesNext[index];
			}
			else {
				hashNodesNext[prevIndex] = hashNodesNext[index];
			}
			--startIndexes;
			if (startIndexes < 0) startIndexes = totalSize - 1;
			freeIndexes[startIndexes] = index;
			return hashNodesValue[index];
		}
		prevIndex = index;
	}
	return 1;
}

void removeEquals() {
	for (int i = 2; i <= n; i++) {
		if (a[i].a == a[i - 1].a) {
			same[i] = 1;
		}
	}

	int m = 0;
	for (int i = 1; i <= n; i++) {
		if (same[i] == 0) ok[m++] = a[i];
	}

	n = m;
	for (int i = 0; i < m; i++) {
		a[i + 1] = ok[i];
	}
}

int toCheck[N][M];
int countToCheck[N];

void addToCheck(int index, int prev, int value) {
	if (countToCheck[index] < M - 1) {
		toCheck[index][countToCheck[index]] = (prev << 16) | value;
		countToCheck[index]++;
		return;
	}
	else {
		int mn = 1000000000;
		int mnIndex = -1;
		for (int i = 0; i < countToCheck[index]; i++) {

			int value = toCheck[index][i] & ((1 << 16) - 1);

			if (mn > value) {
				mn = value;
				mnIndex = i;
			}
		}
		if (mn < value) {
			toCheck[index][mnIndex] = (prev << 16) | value;
		}
	}

}

void checkBest(int value, int start, int diff) {

	int toCheck = start - value * diff;
	while (findA(toCheck) >= 0) {
		toCheck -= diff;
		value++;
	}

	for (int i = 0; i < bestCount; i++) {
		if (bestStart[i] == start - diff && diff == bestDiff[i]) {
			best[i] = value;
			bestStart[i] = start;
			bestDiff[i] = diff;
			return;
		}
	}
	if (bestCount < BC - 1) {
		best[bestCount] = value;
		bestStart[bestCount] = start;
		bestDiff[bestCount] = diff;
		bestCount++;
	}
	else {
		int mn = 0;
		int mnIndex = -1;
		for (int i = 0; i < bestCount; i++) {
			if (bestStart[i] == start - diff && diff == bestDiff[i]) {
				mnIndex = i;
				mn = 0;
				break;
			}
			if (mn < best[i]) {
				mn = best[i];
				mnIndex = i;
			}
		}
		if (mn < value) {
			best[mnIndex] = value;
			bestStart[mnIndex] = start;
			bestDiff[mnIndex] = diff;
		}
	}
}

int getCount(int start, int diff) {
	int count = 1;
	int current = start - diff;
	while (true) {
		int x = findA(current);
		if (x == -1) break;
		current -= diff;
		count++;
	}
	current = start + diff;
	while (true) {
		int x = findA(current);
		if (x == -1) break;
		current += diff;
		count++;
	}
	return count;
}

void print(int start, int diff) {
	printf("%d ", a[findA(start)].id);
	int current = start - diff;
	while (true) {
		int x = findA(current);
		if (x == -1) break;
		current -= diff;
		printf("%d ", a[x].id);
	}
	current = start + diff;
	while (true) {
		int x = findA(current);
		if (x == -1) break;
		current += diff;
		printf("%d ", a[x].id);
	}
	printf("\n");
}

int main()
{
	for (int i = 0; i < totalSizeA; i++) {
		freeIndexesA[i] = i;
		firstA[i] = -1;
	}

	for (int i = 0; i < totalSize; i++) {
		freeIndexes[i] = i;
	}

	for (int i = 0; i < hashSize; i++) {
		first[i] = -1;
	}

	scanf("%d", &n);
	for (int i = 1; i <= n; i++)
	{
		scanf("%d", &a[i].a);
		a[i].id = i;
	}

	sort(a + 1, a + n + 1);

	removeEquals();

	for (int i = 1; i <= n; i++) {
		storeA(a[i].a, i);
	}

	for (int i = 1; i <= n; i++)
	{
		int checksCount = 0;

		for (int j = 0; j < countToCheck[i]; j++) {
			int x = toCheck[i][j] >> 16;
			int t = toCheck[i][j] & ((1 << 16) - 1);

			int nextH = findA(a[i].a + (a[i].a - a[x].a));
			if (nextH < 0) {
				checkBest(t + 1, a[i].a, a[i].a - a[x].a);
				continue;
			}
			if (nextH - i > EDGE) {
				addToCheck(nextH, i, t + 1);
			}
		}

		for (int j = i - 1; j >= maxx(1, i - EDGE); j--)
		{
			if (a[i].a - a[j].a == 0) continue;
			if (a[j].a - (a[i].a - a[j].a) < 0) break;
			int h = findA(a[j].a - (a[i].a - a[j].a));
			if (h > 0)
			{
				checksCount++;
				int t = remove(j, h);
				if (t == 1) {
					int hh = findA(a[h].a - (a[i].a - a[j].a));
					t++;
					if (hh != -1) {
						while (true) {
							int newHh = findA(a[hh].a - (a[i].a - a[j].a));
							if (newHh == -1) break;
							hh = newHh;
							t++;
						}
					}
				}

				checkBest(t + 1, a[i].a, a[i].a - a[j].a);
				int nextH = findA(a[i].a + (a[i].a - a[j].a));
				if (nextH < 0) {
					continue;
				}
				if (nextH - i > EDGE) {
					addToCheck(nextH, i, t + 2);
				}
				store(i, j, t + 1);

				if (checksCount > CHECKS_COUNT_LIMIT && i > 1000) break;
			}
		}
	}

	for (int i = 0; i < bestCount; i++) {
		int currentCount = getCount(bestStart[i], bestDiff[i]);
		if (bestTotal < currentCount) {
			bestTotal = currentCount;
			bestFinalStart = bestStart[i];
			bestFinalDiff = bestDiff[i];
		}
	}

	if (bestTotal == 1) {
		if (a[1].a == a[n].a) {
			printf("1\n%d\n", a[1].id);
		}
		else {
			printf("2\n%d %d\n", a[1].id, a[n].id);
		}
		return 0;
	}

	printf("%d\n", bestTotal);
	print(bestFinalStart, bestFinalDiff);
	return 0;
}