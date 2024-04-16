public class LastSeason {

	int n;
	
	private final int N = 6004;
	
	int[] flag = new int[N];
	
	int[][] ls = new int[N][8];
	int[] lsSize = new int[N];
	
	int[] f = new int[N];
	
	int size = 0;
	int onEdge = 0;
	int partsCount = 0;
	int[] partSize = new int[N];
	int[][] part = new int[N][8];
	
	int[][][] onEdgeParts = new int[8][3][N];
	int[][] onEdgePartsSize = new int[8][3];
	
	int[][] answer = new int[N][8];
	int[] answerSize = new int[N];
	int answerCount = 0;
	
	void addEdge(int x, int y) {
		ls[x][lsSize[x]++] = y;
	}
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt() * 6;
		String s = scanner.next();
		
		for(int i = 0; i < s.length(); i++) {
			flag[i + 1] = s.charAt(i) == '0' ? 1 : 0;
		}
		
		int m = scanner.nextInt();
		for(int i = 0; i < m; i++) {
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			addEdge(x, y);
			addEdge(y, x);
		}
		scanner.close();
	}
	
	boolean hasElement(int size, int count) {
		return onEdgePartsSize[size][count] > 0;
	}
	
	int pop(int size, int count) {
		int x = onEdgeParts[size][count][onEdgePartsSize[size][count] - 1]; 
		onEdgePartsSize[size][count]--;
		return x;
	}
	
	void DFS(int x) {
		f[x] = 1;
		part[partsCount][partSize[partsCount]++] = x;
		onEdge += flag[x];
		
		for(int i = 0; i < lsSize[x]; i++) {
			int y = ls[x][i];
			if(f[y] == 0) DFS(y);
		}
	}
	
	void solve6() {
		while(onEdgePartsSize[6][2] > 0) {
			answer[answerCount][answerSize[answerCount]++] = pop(6, 2);
			answerCount++;
		}
	}
	
	void solve5() {
		for(int k = 1; k <= 2; k++) {
			while(onEdgePartsSize[5][k] > 0) {
				answer[answerCount][answerSize[answerCount]++] = pop(5, k);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 2 - k);
				answerCount++;
			}
		}
	}
	
	void solve4() {
		for(int k = 0; k <= 2; k++) {
			while(onEdgePartsSize[4][k] > 0) {
				answer[answerCount][answerSize[answerCount]++] = pop(4, k);
				if(hasElement(2, 2 - k)) {
					answer[answerCount][answerSize[answerCount]++] = pop(2, 2 - k);
				} else {
					if(k == 0) { //need to add two 1s
						answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
						answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
					}
					if(k == 1) { //need to add 0 and 1
						answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
						answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
					}
					if(k == 2) { //need to add two 0s
						answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
						answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
					}
				}
				answerCount++;
			}
		}
	}
	
	void solve3() {
		//Solve for two 1s 
		while(onEdgePartsSize[3][2] > 0) {
			answer[answerCount][answerSize[answerCount]++] = pop(3, 2);
			if(hasElement(3, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(3, 0);
				answerCount++;
				continue;
			}
			if(hasElement(2, 0) && hasElement(1, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
				answerCount++;
				continue;
			}
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}
		
		//Solve for two 0s
		while(onEdgePartsSize[3][0] > 0) {
			answer[answerCount][answerSize[answerCount]++] = pop(3, 0);
			if(hasElement(2, 2) && hasElement(1, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 2);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
				answerCount++;
				continue;
			}
			if(hasElement(2, 1) && hasElement(1, 1)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 1);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
				answerCount++;
				continue;
			}
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}
		
		//Solve for 0 and 1
		while(onEdgePartsSize[3][1] > 0) {
			answer[answerCount][answerSize[answerCount]++] = pop(3, 1);
			if(hasElement(3, 1)) {
				answer[answerCount][answerSize[answerCount]++] = pop(3, 1);
				answerCount++;
				continue;
			}
			if(hasElement(2, 1) && hasElement(1, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 1);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
				answerCount++;
				continue;
			}
			if(hasElement(2, 0) && hasElement(1, 1)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
				answerCount++;
				continue;
			}
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}
		
	}
	
	void solve2() {
		//Solve for two 1s 
		while(onEdgePartsSize[2][2] > 0) {
			answer[answerCount][answerSize[answerCount]++] = pop(2, 2);
			if(hasElement(2, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				if(hasElement(2, 0)) {
					answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				} else {
					answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
					answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
				}
				answerCount++;
				continue;
			}
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}
		//Solve for 0 and 1 
		while(onEdgePartsSize[2][1] > 0) {
			answer[answerCount][answerSize[answerCount]++] = pop(2, 1);
			if(hasElement(2, 1)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 1);
				if(hasElement(2, 0)) {
					answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				} else {
					answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
					answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
				}
				answerCount++;
				continue;
			}
			if(hasElement(2, 0) && hasElement(1, 1) && hasElement(1, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
				answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
				answerCount++;
				continue;
			}
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}

		//Solve for two 0s
		while(hasElement(2, 0)) {
			answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			if(hasElement(2, 0)) {
				answer[answerCount][answerSize[answerCount]++] = pop(2, 0);
				answerCount++;
				continue;
			}
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}
	}
	
	void solve1() {
		while(hasElement(1, 1)) {
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 1);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answer[answerCount][answerSize[answerCount]++] = pop(1, 0);
			answerCount++;
		}
	}
	
	void output() {
		for(int i = 0; i < answerCount; i++) {
			for(int j = 0; j < answerSize[i]; j++) {
				int x = answer[i][j];
				for(int k = 0; k < partSize[x]; k++) {
					System.out.print(part[x][k] + " ");
				}
			}
			System.out.println();
		}
	}
	
	void solve() {
		init();
		for(int i = 1; i <= n; i++) {
			if(f[i] == 0) {
				onEdge = 0;
				DFS(i);
				onEdgeParts[partSize[partsCount]][onEdge][ onEdgePartsSize[partSize[partsCount]][onEdge]++ ] = partsCount;
				partsCount++;
			}
		}

		solve6();
		solve5();
		solve4();
		solve3();
		solve2();
		solve1();
		
		output();
	}
	
	public static void main(String[] args) {
		LastSeason ls = new LastSeason();
		ls.solve();
	}
	
}
