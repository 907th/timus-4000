public class Benchmark {

	private static int[][] getTest(int n, int m) {
		java.util.Random r = new java.util.Random();
		int[] a = new int[n];
		int l = r.nextInt(70) + 5;
		int[] q = new int[l];
		for(int i = 0; i < q.length; i++) {
			q[i] = r.nextInt(92) + 9;
		}
		for(int i = 0; i < n; i++) {
			a[i] = q[r.nextInt(q.length)];
		}
		java.util.ArrayList<Integer> rw = new java.util.ArrayList<Integer>();
		int sum = 0;
		for(int i = 0; i < n; i++) {
			sum += a[i];
			if(r.nextInt(10) == 3 && sum > 0) {
				rw.add(sum); sum = 0;
				if(rw.size() == m - 1 && i < n - 1) {
					for(int j = i + 1; j < n; j++) {
						sum += a[j];
					}
					rw.add(sum); sum = 0;
					break;
				}
			}
		}
		if(sum != 0) {
			rw.add(sum);
		}
		int[] b = new int[rw.size()];
		java.util.Arrays.sort(a);
		for(int i = 0; i < rw.size(); i++) {
			b[i] = rw.get(i);
		}
		
		return new int[][] {a, b};
	}
	
	private static int[][] getTestWithDifferent(int n, int m, int diff) {
		
		int[] down = new int[] {91, 91, 81, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 60, 60, 60, 60, 60, 60, 60, 60, 50, 50, 50, 30, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0};
		int[] up = new int[] {101, 101, 91, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 70, 70, 70, 70, 70, 70, 70, 70, 60, 60, 60, 40, 30, 30, 30, 30, 30, 10, 10, 10, 10, 10, 10, 10};
		
		java.util.Random r = new java.util.Random();
		int[] a = new int[n];
		int[] f = new int[n];
		int[] q = new int[diff];
		for(int i = 0; i < q.length; i++) {
			while(true) {
				int h = r.nextInt(99) + 2;
				boolean failed = true;
				for(int j = 0; j < up.length; j++) {
					if(h >= down[j] && h < up[j] && f[j] == 0) {
						failed = false; break;
					}
				}
				if(failed) continue;
				for(int j = 0; j < i; j++) {
					if(q[j] == h) {
						failed = true; break;
					}
				}
				q[i] = h;
				if(!failed) break;
			}
		}
		
		for(int i = 0; i < down.length; i++) {
			boolean found = false;
			for(int j = 0; j < q.length; j++) {
				if(q[j] >= down[i] && q[j] < up[i]) {
					found = true; break;
				}
			}
			if(!found) return new int[0][0];
		}
		
		for(int i = 0; i < n; i++) {
			while(true) {
				int ind = r.nextInt(q.length);
				if(q[ind] >= down[i] && q[ind] < up[i]) {
					a[i] = q[ind]; break;
				}
			}
		}
		
		for(int i = 0; i < 1000; i++) {
			int x = r.nextInt(a.length);
			int y = r.nextInt(a.length);
			int h = a[x];
			a[x] = a[y];
			a[y] = h;
		}
		
		int[] b = new int[m];
		for(int i = 0; i < n; i++) {
			b[r.nextInt(m)] += a[i];
		}
		
		for(int i = 0; i < m; i++) {
			if(b[i] == 0) return new int[0][0];
		}

		return new int[][] {a, b};
	}
	
	private static void outTest(int[][] t) {
		int sumRows = 0;
		int sumShips = 0;
		System.out.println(t[0].length + " " + t[1].length);
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < t[i].length; j++) {
				if(i == 0) sumShips += t[i][j];
				if(i == 1) sumRows += t[i][j];
				System.out.print(t[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println(sumShips + " " + sumRows);
	}
	
	public static void main(String[] args) {
		for(int t = 0; t < 1000000; t++) {
			if(t % 4000 == 0) System.out.println(t);
			int[][] test;
			while(true) {
				test = getTestWithDifferent(40, 9, 23);
				if(test.length != 0) break;
			}
			long st = System.currentTimeMillis();
			ShipsBF sbf = new ShipsBF();
			try {
				sbf.solve(test[0], test[1], false);
			} catch (Exception e) {
				outTest(test);
			}
			if(System.currentTimeMillis() - st > 1000) {
				System.out.println("Time: " + (System.currentTimeMillis() - st));
				outTest(test);
			}
		}
	}
	
}
/*

Test 81:

39 9
99 99 86 79 74 74 72 72 71 71
71 71 70 70 70 69 69 68 68 67
67 67 67 59 57 56 31 24 22 21
21 12  8  6  5 3  3  3  1
128 172 181 183 186 188 189 324 472


39 9
99 99 86 79 74 74 72 72 71 71
71 71 70 70 70 69 69 68 68 67
67 67 67 59 57 56 31 24 22 21
21 12  8  6  5 3  3  3  1
128 172 181 183 186 188 189 324 472

2
69 59 
3
74 74 24 
3
79 71 31 
3
99 72 12 
4
72 71 22 21 
3
99 86 3 
9
71 71 21 8 6 5 3 3 1 
5
67 67 67 67 56 
7
57 68 68 69 70 70 70 


Test 82:
52 9
99 98 98 98 98 97 97 96 96 96 96 95 95 95 95 94 93 93 93 93 93 93 93 93 92 92 92 92 90 90 90 88 79 48 32 32 32 32 18 18 18 18  6  6  3  3  3  2  2  2  2  2
123 280 312 325 340 342 411 433 805


52 9
99 98 98 98 98 97 97 96 96 96 96 95 95 95 95 94 93 93 93 93 93 93 93 93 92 92 92 92 90 90 90 88 79 48 32 32 32 32 18 18 18 18  6  6  3  3  3  2  2  2  2  2
123 280 312 325 340 342 411 433 805

Answer:
3
99 18 6 
3
94 93 93 
4
98 98 98 18 
5
98 97 96 32 2 
5
97 96 93 48 6 
7
96 95 95 32 18 3 3 
5
96 95 95 93 32 
11
93 93 93 93 32 18 3 2 2 2 2 
9
79 88 90 90 90 92 92 92 92 

*/