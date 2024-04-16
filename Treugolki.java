import java.util.ArrayList;

public class Treugolki {

	int n, m;
	int[] deg = new int[50004];
	int[][] a = new int[50004][];
	int[] aSize = new int[50004];
	
	int find(int x, int y) {
		int l = 0;
		int r = aSize[x];
		while(r - l > 1) {
			int m = (r + l) / 2;
			if(a[x][m] > y) {
				r = m;
			} else {
				l = m;
			}
		}
		return l;
	}
	
	void rec(int x, int y) {
		int origX = x;
		if(x + 1 == y) return;
		
		ArrayList<Integer> currentList = new ArrayList<>();
		currentList.add(x);
		while(x != y) {
			if(x == origX) {
				if(aSize[x] == 0) {
					x++;
				} else 
				if(a[x][0] == y) {
					x++;
				} else {
					if(a[x][aSize[x] - 1] >= y) {
						int h = find(x, y);
						if(a[x][h] == y) {
							rec(x, a[x][h - 1]);
							x = a[x][h - 1];
						}
					} else {
						rec(x, a[x][aSize[x] - 1]);
						x = a[x][aSize[x] - 1];
					}
				}
			} else
			if(aSize[x] == 0) {
				x++;
			} else
			if(a[x][0] > y) {
				x++;
			} else {
				if(a[x][aSize[x] - 1] == y) {
					rec(x, y);
					x = y;
				} else {
					rec(x, a[x][aSize[x] - 1]);
					x = a[x][aSize[x] - 1];
				}
			}
			currentList.add(x);
		}
		
		for(int i = 2; i < currentList.size() - 1; i++) {
			System.out.println(currentList.get(0) + " " + currentList.get(i));
		}
		
	}
	
	void solve(int[][] in, int n) {
		m = in[0].length;
		
		for(int i = 0; i < m; i++) {
			deg[in[0][i]]++;
		}
		
		for(int i = 1; i <= n; i++) {
			a[i] = new int[deg[i]];
		}

		for(int i = 0; i < m; i++) {
			a[in[0][i]][aSize[in[0][i]]++] = in[1][i];
		}
		for(int i = 1; i <= n; i++) {
			java.util.Arrays.sort(a[i]);
		}
		
		System.out.println(n - 3 - m);
		rec(1, n);
		
	}
	
	public static void main(String[] args) {
		Treugolki t = new Treugolki();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int k = scanner.nextInt();
		int[][] a = new int[2][k];
		for(int i = 0; i < k; i++) {
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			if(x > y) {
				int h = x;
				x = y;
				y = h;
			}
			a[0][i] = x;
			a[1][i] = y;
		}
		scanner.close();
		t.solve(a, n);
	}
	
}