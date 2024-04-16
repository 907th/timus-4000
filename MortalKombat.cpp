#include <stdio.h>
#include <memory.h>

int n, m;
int a[512][2048];
int b[512][2048];

int c[512][512];
int hasAnotherHuman[512];

int f[1504];
int f2[1504];
int mate[1504];
int twin[1504];

int good[1504];

int human = 0;
int monster = 0;

char s[2048];

void init() {
    scanf("%d%d", &n, &m);
	for(int i = 0; i < n; i++) {
	    scanf("%s", s);
		for(int j = 0; j < m; j++) {
			a[i][j] = s[j] - '0';
		}
	}
}

bool DFS(int x) {
	f[x] = 1;
	for(int i = 0; i < m; i++) {
		if(a[x][i] == 0 || f2[i] == 1) continue;
		if(mate[i] == -1) {
			mate[i] = x;
			twin[x] = i;
			return true;
		}
		if(f[mate[i]] == 1) continue;
		f2[i] = 1;
		if(DFS(mate[i])) {
			mate[i] = x;
			twin[x] = i;
			return true;
		}
	}
	return false;
}

void solve() {
	
	for(int i = 0; i < 512; i++) {
		for(int j = 0; j < 2048; j++) {
			b[i][j] = 1;
		}
	}

    for(int i = 0; i < 1504; i++) {
        twin[i] = mate[i] = -1;
    }

	init();
	
	for(int i = 0; i < n; i++) {
	    memset(f, 0, sizeof(f));
	    memset(f2, 0, sizeof(f2));

		if(twin[i] != -1) continue;
		if(!DFS(i)) {
			for(int k = 0; k < n; k++) {
				for(int j = 0; j < m; j++) {
					printf("1");
				}
				printf("\n");
			}
			return;
		}
	}

	for(int i = 0; i < n; i++) {
		for(int j = 0; j < n; j++) {
			if(a[j][twin[i]] == 1) {
				c[i][j] = 1;
			}
		}
	}
	
	for(int k = 0; k < n; k++) {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if( c[i][j] == 0 && c[i][k] == 1 && c[k][j] == 1) {
					c[i][j] = 1;
				}
			}
		}
	}
	
	for(int i = 0; i < n; i++) {
		b[i][twin[i]] = 0;
	}

	for(int i = 0; i < n; i++) {
		for(int j = 0; j < n; j++) {
			if(c[i][j] == 1 && a[i][twin[j]] == 1) {
				b[i][twin[j]] = 0;
			}
		}
	}
	
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			if(a[i][j] == 1 && mate[j] == -1) {
				b[i][j] = 0;
			}
		}
	}
	
	for(int j = 0; j < m; j++) {
		if(mate[j] == -1) {
			for(int i = 0; i < n; i++) {
				if(a[i][j] == 1) hasAnotherHuman[i] = 1;
			}
		}
	}
	
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < n; j++) {
			for(int k = 0; k < n; k++) {
				if(a[i][twin[j]] == 1 && c[k][j] == 1 && hasAnotherHuman[k] == 1) {
					b[i][twin[j]] = 0;
				}
			}
		}
	}
	
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < m; j++) {
			printf("%d",b[i][j]);
		}
		printf("\n");
	}
}

int main() {
    solve();
    return 0;
}