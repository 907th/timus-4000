#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <memory.h>

using namespace std;

const int INF = 2000000014;
const int N = 16384;
const int M = 4 * N;

struct edge {
	int from, to, cap, rev, flow, id;
};

edge edges[4 * M];
int edgesCount = 0;
int n, m;

struct node {
	int v;
	node* next;
};

node* ed[N];
node* start[N];
node allNodes[4 * M];
int nodesCount = 0;

void addNode(int x, int to) {
	node* p = allNodes + nodesCount++;
	p->v = to;
	p->next = ed[x];
	ed[x] = p;
}

void addEdge(int from, int to, int cap, int id) {
	edge e;
	e.from = from; e.to = to; e.cap = cap; e.rev = edgesCount + 1;
	e.flow = 0;
	e.id = id;
	addNode(from, edgesCount);
	edges[edgesCount++] = e;

	edge e_rev;
	e_rev.from = to; e_rev.to = from; e_rev.cap = 0; e_rev.rev = edgesCount - 1;
	e_rev.flow = 0;
	e_rev.id = id;
	addNode(to, edgesCount);
	edges[edgesCount++] = e_rev;
}

int d[N];
int q[N];
int qs, qf;
int f[N];

int myMin(int x, int y) {
	return x < y ? x : y;
}

int BFS(int s, int t) {
	for (int i = 1; i <= n; i++) {
		start[i] = ed[i];
	}
	memset(d, 0, sizeof(d));
	qs = qf = 0;
	d[s] = 1;
	q[qf++] = s;
	while (qs != qf) {
		int x = q[qs++];
		for (node* p = ed[x]; p; p = p->next) {
			edge e = edges[p->v];
			if (!d[e.to] && e.cap - e.flow > 0) {
				d[e.to] = d[e.from] + 1;
				q[qf++] = e.to;
			}
		}
	}
	return d[t];
}

int DFS(int x, int t, int mn) {
	f[x] = 1;
	if (x == t) return mn;
	for (; start[x]; start[x] = start[x]->next) {
		int i = start[x]->v;
		edge e = edges[i];
		if (!f[e.to] && d[e.to] == d[e.from] + 1 && e.cap - e.flow > 0) {
			int h = DFS(e.to, t, myMin(mn, e.cap - e.flow));
			if (h) {
				edges[i].flow += h;
				edges[e.rev].flow -= h;
				return h;
			}
		}
	}
	return 0;
}

int MaxFlow(int s, int t) {
	int totalFlow = 0;
	while (BFS(s, t)) {
		memset(f, 0, sizeof(f));
		while (int h = DFS(s, t, INF)) {
			totalFlow += h;
			memset(f, 0, sizeof(f));
		}
	}
	return totalFlow;
}

int main() {
	scanf("%d", &n);
	for (int i = 1; i <= n; i++) {
		int x, y;
		scanf("%d%d", &x, &y);
	}
	scanf("%d", &m);
	for (int i = 1; i <= m; i++) {
		int x, y, z;
		scanf("%d%d%d", &x, &y, &z);
		addEdge(x, y, z, i);
		addEdge(y, x, z, i);
	}
	int h = MaxFlow(1, n);
	printf("%d\n", h);
	for (int i = 0; i < edgesCount; i += 4) {

		int forwardFlow = edges[i].flow + edges[i + 3].flow;
		int reverseFlow = edges[i + 1].flow + edges[i + 2].flow;

		if (forwardFlow > 0) {
			printf("%d %d %d\n", edges[i].from, edges[i].to, forwardFlow);
		}
		else
			if (forwardFlow < 0) {
				printf("%d %d %d\n", edges[i].to, edges[i].from, -forwardFlow);
			}
			else
			{
				printf("%d %d %d\n", edges[i].from, edges[i].to, 0);
			}
	}
	return 0;
}