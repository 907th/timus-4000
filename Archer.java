public class Archer {

	private final int P = 1000000000;
	
	class Matrix {
		int[][] a = new int[32][32];
		int n = 0;
		
		Matrix multiply(Matrix b) {
			Matrix c = new Matrix();
			c.n = n;
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					for(int k = 0; k < n; k++) {
						c.a[i][j] = (int)((c.a[i][j] + ((long)a[i][k] * (long)b.a[k][j]) % P) % P);
					}
				}
			}
			return c;
		}
		
	}
	
	void print(int mask, int len) {
		for(int i = 0; i < len; i++) {
			System.out.print((bit(mask, i) ? 1 : 0));
		}
		System.out.println(" " + (bit(mask, len) ? 2 : 1));
	}
	
	Matrix pow(Matrix a, int degree) {
		if(degree == 1) return a; else {
			Matrix c = pow(a, degree / 2);
			if(degree % 2 == 0) return c.multiply(c); else return c.multiply(c).multiply(a);
		}
	}
	
	int solve2(int n) {
		Matrix a = new Matrix();
		a.n = 1 << 2;
		a.a[2][3] = 1;
		a.a[1][3] = 1;
		a.a[3][1] = 1;
		a.a[3][2] = 1;
		a = pow(a, n - 1);
		return (2 * a.a[3][3]) % P;
	}
	
	boolean bit(int x, int bit) {
		return (x & (1 << bit)) != 0;
	}
	
	int[] hasHole(int mask, int len) {
		for(int i = 0; i < len - 1; i++) {
			for(int j = i + 1; j < len; j++) {
				if(bit(mask, i) && bit(mask, j)) {
					boolean noHole = false;
					int count = 0;
					for(int k = i + 1; k < j; k++) {
						count++;
						if(bit(mask, k)) {
							noHole = true; break;
						}
					}
					if(!noHole && count > 0) return new int[] {i, j};
				}
			}
		}
		return new int[] {};
	}
	
	int[][] field = new int[8][8];
	
	void rec(int x, int y) {
		if(!(x >= 0 && x < 8 && y >= 0 && y < 8)) return;
		if(field[x][y] != 1) return;
		field[x][y] = 2;
		rec(x + 1, y);
		rec(x - 1, y);
		rec(x, y + 1);
		rec(x, y - 1);
	}
	
	int calcComponents(int mask1, int mask2, int len) {
		
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				field[i][j] = 0;
			}
		}
		
		for(int i = 0; i < len; i++) {
			if(bit(mask1, i)) field[0][i] = 1;
			if(bit(mask2, i)) field[1][i] = 1;
		}
		
		int count = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(field[i][j] == 1) {
					rec(i, j);
					count++;
				}
			}
		}
		
		return count;
	}
	
	boolean isOk(int mask1, int mask2, int len) {
		int componentsCount1 = bit(mask1, len) ? 2 : 1;
		int componentsCount2 = bit(mask2, len) ? 2 : 1;
		
		if(componentsCount1 > calcComponents(0, mask1, len)) return false;
		if(componentsCount2 > calcComponents(0, mask2, len)) return false;
		
		if(componentsCount1 == 1 && componentsCount2 == 1) {
			if(calcComponents(0, mask1, len) == 1 && calcComponents(mask1, mask2, len) == 2) return false;
			if(calcComponents(0, mask1, len) == 2 && calcComponents(mask1, mask2, len) == 1) return false;
		}
		
		if(componentsCount1 == 1 && componentsCount2 == 2) {
			if(calcComponents(mask1, mask2, len) != 2) return false;
			if(calcComponents(0, mask1, len) == 2 && calcComponents(0, mask2, len) == 2) return false;
		}
		
		if(componentsCount1 == 2 && componentsCount2 == 1 && calcComponents(mask1, mask2, len) != 1) return false; 
		
		if(componentsCount1 == 2 && componentsCount2 == 2 && calcComponents(mask1, mask2, len) != 2) return false;
		
		if(!bit(mask1, 0) && !bit(mask2, 0)) return false;
		if(!bit(mask1, len - 1) && !bit(mask2, len - 1)) return false;

		for(int i = 1; i < len; i++) {
			boolean b11 = bit(mask1, i - 1);
			boolean b12 = bit(mask1, i);
			boolean b21 = bit(mask2, i - 1);
			boolean b22 = bit(mask2, i);
			if( b11 &&  b12 &&  b21 &&  b22) return false;
			if(!b11 && !b12 && !b21 && !b22) return false;
			if(!b11 &&  b12 &&  b21 && !b22) return false;
			if( b11 && !b12 && !b21 &&  b22) return false;
		}
		
		return true;
	}
	
	boolean canBeStart(int mask, int len) {
		boolean ok = ((mask & 1) != 0) && ((mask & (1 << (len - 1))) != 0);
		for(int i = 1; i < len; i++) {
			if((mask & (1 << i)) == 0 && (mask & (1 << (i - 1))) == 0) return false;
		}
		int componentsCount = bit(mask, len) ? 2 : 1;
		return ok && componentsCount == calcComponents(0, mask, len);
	}
	
	boolean canBeFinish(int mask, int len) {
		boolean ok = ((mask & 1) != 0) && ((mask & (1 << (len - 1))) != 0);
		for(int i = 1; i < len; i++) {
			if((mask & (1 << i)) == 0 && (mask & (1 << (i - 1))) == 0) return false;
		}
		boolean oneComponentsFlag = !bit(mask, len);
		return ok && oneComponentsFlag;
	}
	
	int solve3(int n) {
		Matrix a = new Matrix();
		
		a.n = 1 << 4;
		for(int i = 1; i < 1 << 4; i++) {
			for(int j = 1; j < 1 << 4; j++) {
				if(isOk(i, j, 3)) {
					//print(i, 3);
					//print(j, 3);
					//System.out.println();
					a.a[i][j] = 1;
				}
			}
		}
		
		a = pow(a, n - 1);
		
		int answer = 0;
		for(int i = 1; i < 1 << 4; i++) {
			if(canBeStart(i, 3)) {
				for(int j = 1; j < 1 << 4; j++) {
					if(canBeFinish(j, 3)) {
						//System.out.print("Start at: "); print(i, 3);
						//System.out.print("Finish at: "); print(j, 3);
						//System.out.println();
						answer = (answer + a.a[i][j]) % P;
					}
				}
			}
		}
		return (2 * answer) % P;
	}
	
	int solve4(int n) {
		Matrix a = new Matrix();
		a.n = 1 << 5;
		for(int i = 1; i < 1 << 5; i++) {
			for(int j = 1; j < 1 << 5; j++) {
				if(isOk(i, j, 4)) {
					//print(i, 4);
					//print(j, 4);
					//System.out.println();
					a.a[i][j] = 1;
				}
			}
		}
		a = pow(a, n - 1);
		int answer = 0;
		for(int i = 1; i < 1 << 5; i++) {
			if(canBeStart(i, 4)) {
				for(int j = 1; j < 1 << 5; j++) {
					if(canBeFinish(j, 4)) {
						//System.out.print("Start at: "); print(i, 4);
						//System.out.print("Finish at: "); print(j, 4);
						//System.out.println(a.a[i][j]);
						//System.out.println();
						answer = (answer + a.a[i][j]) % P;
					}
				}
			}
		}
		return (2 * answer) % P;
	}
	
	int solve(int n, int m) {
		if(n == 2) return 2;
		if(n == 3) {
			return solve2(m - 1);
		}
		if(n == 4) {
			return solve3(m - 1);
		}
		if(n == 5) {
			return solve4(m - 1);
		}
		return 0;
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		//int n = 5, m = 6;
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		if(n > m) {
			int h = n;
			n = m;
			m = h;
		}
		scanner.close();
		Archer a = new Archer();
		int h = a.solve(n, m);
		System.out.println(h);
	}
	
}
