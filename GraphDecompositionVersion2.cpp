#define _CRT_SECURE_NO_WARNINGS
#pragma comment(linker, "/STACK:16777216")

#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <algorithm>

using namespace std;

struct node {
	int x, id;
	node* next;
};

node nodes[100004 * 4];
int nodesSize = 0;

node* a[100004];
int f[100004];

int checked[100004];

int answer[3][100004];
int answerSize = 0;

void addToAnswer(int x, int y, int z) {
	answer[0][answerSize] = x;
	answer[1][answerSize] = y;
	answer[2][answerSize] = z;
	answerSize++;
}

void addEdge(int from, int to, int id) {
	node* p = nodes + nodesSize++;
	p->x = to;
	p->next = a[from];
	p->id = id;
	a[from] = p;
}

int DFS(int x, int last) {
	f[x] = 1;
	int lastVertice = -1;

	for (node* p = a[x]; p; p = p->next) {
		int y = p->x;
		if (y == last || checked[p->id] == 1) continue;
		checked[p->id] = 1;

		if (f[y] == 0) {
			int h = DFS(y, x);
			if (h == -1) {
				if (lastVertice == -1) {
					lastVertice = y;
				}
				else {
					addToAnswer(lastVertice, x, y);
					lastVertice = -1;
				}
			}
			else {
				addToAnswer(h, y, x);
			}
		} else {
			if (lastVertice == -1) {
				lastVertice = y;
			}
			else {
				addToAnswer(lastVertice, x, y);
				lastVertice = -1;
			}
		}

	}
	return lastVertice;
}

int main() {
	int x, y, m = 0;
	while (scanf("%d%d", &x, &y) != EOF)
	{
		addEdge(x, y, m);
		addEdge(y, x, m);
		m++;
	}

	for (int i = 1; i <= 100000; i++) {
		if (f[i] == 0) {
			int h = DFS(i, -1);
			if (h != -1) {
				printf("-1\n");
				return 0;
			}
		}
	}

	printf("%d\n", answerSize);
	for (int i = 0; i < answerSize; i++) {
		printf("%d %d %d\n", answer[0][i], answer[1][i], answer[2][i]);
	}

	return 0;
}