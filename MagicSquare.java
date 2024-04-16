public class MagicSquare {

	int[][] allowed = new int[1024][10];
	int[] allowedSize = new int[1024];
	int n;
	
	int[][] a = new int[16][16];
	
	int[] row = new int[16];
	int[] col = new int[16];
	int[] smallSquare = new int[16];
	int[][] relatedSquareRow = new int[16][16];
	int[][] relatedSquareCol = new int[16][16];
	int[][] id = new int[16][16];
	
	int[][] relatedRowSquares = new int[16][16];
	int[] relatedRowSquareSize = new int[16];
	int[][] relatedColSquares = new int[16][16];
	int[] relatedColSquareSize = new int[16];

	int[] idx = new int[16];
	int[] idy = new int[16];

	int getAllowedMask(int x, int y) {
		return row[x] | col[y] | smallSquare[id[x][y]] | relatedSquareRow[id[x][y]][x % n] | relatedSquareCol[id[x][y]][y % n];
	}
	
	boolean isOk(int x, int y, int v) {
		int all = row[x] | col[y] | smallSquare[id[x][y]] | relatedSquareRow[id[x][y]][x % n] | relatedSquareCol[id[x][y]][y % n];
		if((all & (1 << v)) == 0) return true;
		return false;
	}
	
	void set(int x, int y, int v) {
		row[x] |= (1 << v);
		col[y] |= (1 << v);
		smallSquare[id[x][y]] |= (1 << v);
		
		int sId = id[x][y];
		for(int i = 0; i < relatedRowSquareSize[sId]; i++) {
			relatedSquareRow[relatedRowSquares[sId][i]][x % n] |= (1 << v);
		}
		for(int i = 0; i < relatedColSquareSize[sId]; i++) {
			relatedSquareCol[relatedColSquares[sId][i]][y % n] |= (1 << v);
		}
		
		a[x][y] = v;
	}
	
	void unset(int x, int y, int v) {
		row[x] ^= (1 << v);
		col[y] ^= (1 << v);
		smallSquare[id[x][y]] ^= (1 << v);
		
		int sId = id[x][y];
		for(int i = 0; i < relatedRowSquareSize[sId]; i++) {
			relatedSquareRow[relatedRowSquares[sId][i]][x % n] ^= (1 << v);
		}
		for(int i = 0; i < relatedColSquareSize[sId]; i++) {
			relatedSquareCol[relatedColSquares[sId][i]][y % n] ^= (1 << v);
		}

		a[x][y] = -1;
	}
	
	boolean check() {
		int k = 0;
		for(int i = 0; i < n * n; i++) {
			int m = smallSquare[i];
			for(int j = 0; j < allowedSize[m]; j++) {
				int v = allowed[m][j];
				boolean found = false;
				for(int x = 0; x < n; x++) {
					for(int y = 0; y < n; y++) {
						if(a[idx[k] + x][idy[k] + y] == -1) {
							if(isOk(idx[k] + x, idy[k] + y, v)) {
								found = true; break;
							}
						}
					}
					if(found) break;
				}
				if(!found) {
					return false;
				}
			}
			k++;
		}
		return true;
	}
	
	boolean rec(int left) {
		//output();
		if(left == 0) return true;
		if(!check()) return false;
		int bx = -1, by = -1, b = -1;
		for(int i = 0; i < n * n; i++) {
			for(int j = 0; j < n * n; j++) {
				if(a[i][j] == -1) {
					int v = getAllowedMask(i, j);
					if(allowedSize[v] == 1) {
						b = v;
						bx = i;
						by = j;
						break;
					}
					if(b == -1 || allowedSize[b] > allowedSize[v]) {
						b = v;
						bx = i;
						by = j;
					}
				}
			}
			if(b != -1 && allowedSize[b] == 1) {
				break;
			}
		}
		
		for(int i = 0; i < allowedSize[b]; i++) {
			set(bx, by, allowed[b][i]);
			if(rec(left - 1)) return true;
			unset(bx, by, allowed[b][i]);
		}
		
		return false;
	}
	
	void initialize() {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[i].length; j++) {
				a[i][j] = -1;
			}
		}
		
		for(int i = 0; i < (1 << (n * n)); i++) {
			for(int j = 0; j < n * n; j++) {
				if((i & (1 << j)) == 0) {
					allowed[i][allowedSize[i]++] = j;
				}
			}
		}
		
		int k = 0;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				
				idx[k] = i * n;
				idy[k] = j * n;
				
				for(int x = 0; x < n; x++) {
					for(int y = 0; y < n; y++) {
						id[i * n + x][j * n + y] = k;
					}
				}
				k++;
			}
		}

		for(int i = 0; i < n * n; i++) {
			for(int j = 0; j < n * n; j++) {
				//System.out.print(id[i][j] + " ");
			}
			//System.out.println();
		}
		//System.out.println();

		if(n == 3) {
			relatedRowSquares[0][relatedRowSquareSize[0]++] = 4; 
			relatedRowSquares[0][relatedRowSquareSize[0]++] = 5;
			
			relatedRowSquares[1][relatedRowSquareSize[1]++] = 3; 
			relatedRowSquares[1][relatedRowSquareSize[1]++] = 5;
			
			relatedRowSquares[2][relatedRowSquareSize[2]++] = 3; 
			relatedRowSquares[2][relatedRowSquareSize[2]++] = 4;
			
			relatedRowSquares[3][relatedRowSquareSize[3]++] = 1; 
			relatedRowSquares[3][relatedRowSquareSize[3]++] = 2;
			relatedRowSquares[3][relatedRowSquareSize[3]++] = 7; 
			relatedRowSquares[3][relatedRowSquareSize[3]++] = 8;
			
			relatedRowSquares[4][relatedRowSquareSize[4]++] = 0; 
			relatedRowSquares[4][relatedRowSquareSize[4]++] = 2;
			relatedRowSquares[4][relatedRowSquareSize[4]++] = 6; 
			relatedRowSquares[4][relatedRowSquareSize[4]++] = 8;
			
			relatedRowSquares[5][relatedRowSquareSize[5]++] = 0; 
			relatedRowSquares[5][relatedRowSquareSize[5]++] = 1;
			relatedRowSquares[5][relatedRowSquareSize[5]++] = 6; 
			relatedRowSquares[5][relatedRowSquareSize[5]++] = 7;
			
			relatedRowSquares[6][relatedRowSquareSize[6]++] = 4; 
			relatedRowSquares[6][relatedRowSquareSize[6]++] = 5;
			
			relatedRowSquares[7][relatedRowSquareSize[7]++] = 3; 
			relatedRowSquares[7][relatedRowSquareSize[7]++] = 5;
			
			relatedRowSquares[8][relatedRowSquareSize[8]++] = 3; 
			relatedRowSquares[8][relatedRowSquareSize[8]++] = 4;
			
			
			relatedColSquares[0][relatedColSquareSize[0]++] = 4; 
			relatedColSquares[0][relatedColSquareSize[0]++] = 7;
			
			relatedColSquares[1][relatedColSquareSize[1]++] = 3; 
			relatedColSquares[1][relatedColSquareSize[1]++] = 6;
			relatedColSquares[1][relatedColSquareSize[1]++] = 5; 
			relatedColSquares[1][relatedColSquareSize[1]++] = 8;
			
			relatedColSquares[2][relatedColSquareSize[2]++] = 4; 
			relatedColSquares[2][relatedColSquareSize[2]++] = 7;
			
			relatedColSquares[3][relatedColSquareSize[3]++] = 1; 
			relatedColSquares[3][relatedColSquareSize[3]++] = 7;
			
			relatedColSquares[4][relatedColSquareSize[4]++] = 0; 
			relatedColSquares[4][relatedColSquareSize[4]++] = 6;
			relatedColSquares[4][relatedColSquareSize[4]++] = 2; 
			relatedColSquares[4][relatedColSquareSize[4]++] = 8;
			
			relatedColSquares[5][relatedColSquareSize[5]++] = 1; 
			relatedColSquares[5][relatedColSquareSize[5]++] = 7;
			
			relatedColSquares[6][relatedColSquareSize[6]++] = 1; 
			relatedColSquares[6][relatedColSquareSize[6]++] = 4;
			
			relatedColSquares[7][relatedColSquareSize[7]++] = 0; 
			relatedColSquares[7][relatedColSquareSize[7]++] = 3;
			relatedColSquares[7][relatedColSquareSize[7]++] = 2; 
			relatedColSquares[7][relatedColSquareSize[7]++] = 5;
			
			relatedColSquares[8][relatedColSquareSize[8]++] = 1; 
			relatedColSquares[8][relatedColSquareSize[8]++] = 4;
		}
		if(n == 2) {
			relatedRowSquares[0][relatedRowSquareSize[0]++] = 3; 
			relatedRowSquares[3][relatedRowSquareSize[3]++] = 0; 
			relatedRowSquares[1][relatedRowSquareSize[1]++] = 2; 
			relatedRowSquares[2][relatedRowSquareSize[2]++] = 1;
			
			relatedColSquares[0][relatedColSquareSize[0]++] = 3; 
			relatedColSquares[3][relatedColSquareSize[3]++] = 0; 
			relatedColSquares[1][relatedColSquareSize[1]++] = 2; 
			relatedColSquares[2][relatedColSquareSize[2]++] = 1;
		}
		
	}
	
	void output() {
		for(int i = 0; i < n * n; i++) {
			for(int j = 0; j < n * n; j++) {
				if(a[i][j] == -1) {
					System.out.print("- ");
				} else {
					System.out.print(a[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	boolean solve(int n, int[][] init) {
		if(n == 1) return true;
		this.n = n;
		initialize();
		
		for(int i = 0; i < init.length; i++) {
			if(isOk(init[i][0], init[i][1], init[i][2])) {
				set(init[i][0], init[i][1], init[i][2]);
			} else {
				return false;
			}
		}
		
		int c = 0;
		if(a[0][0] == -1) {
			if(isOk(0, 0, 0)) {
				c++;
				set(0, 0, 0);
			} else {
				return false;
			}
		} else {
			if(a[0][0] != 0) return false;
		}
		
		if(rec(n * n * n * n - init.length - c)) {
			//output();
			return true;
		}
		return false;
	}

	public static void fromConsole() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int k = scanner.nextInt();
		int[][] a = new int[k][3];
		for(int i = 0; i < k; i++) {
			a[i][1] = scanner.nextInt();
			a[i][0] = scanner.nextInt();
			a[i][2] = scanner.nextInt();
		}
		scanner.close();
		MagicSquare ms = new MagicSquare();
		boolean h = ms.solve(n, a);
		if(h) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
		}
	}
	
	public static void predefined() {
		MagicSquare ms = new MagicSquare();
		boolean h = ms.solve(3, new int[][] {});
		if(h) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
		}
	}
	
	public static void main(String[] args) {
		fromConsole();
		//predefined();
	}
	
}
