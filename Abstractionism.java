class Input {
	int n, m;
	int[][] a = new int[32][32];
	
	String output() {
		StringBuilder sb = new StringBuilder();
		sb.append(n).append(" ").append(m).append("\n");
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				sb.append(a[i][j]).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}

class Solution {

	int movesCount = 0;
	int[][] moves = new int[32 * 32][2];
	
	void output() {
		System.out.println("Yes");
		for(int i = 0; i < movesCount; i++) {
			System.out.println(moves[i][0] + " " + moves[i][1]);
		}
	}
	
	void addMove(int x, int y) {
		moves[movesCount][0] = x;
		moves[movesCount][1] = y;
		movesCount++;
	}
	
	public static Solution predfinedSolution() {
		Solution solution = new Solution();
		solution.addMove(2, 2);
		solution.addMove(2, 1);
		solution.addMove(1, 1);
		solution.addMove(1, 2);
		solution.addMove(2, 3);
		solution.addMove(1, 3);
		solution.addMove(3, 3);
		solution.addMove(3, 2);
		solution.addMove(3, 1);
		return solution;
	}
}

interface TaskInput {

	public Input getInput();

}

class ConsoleTaskInput implements TaskInput {

	Input input = null;
	
	public Input getInput() {
		if(input != null) return input;
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		input = new Input();
		input.n = scanner.nextInt();
		input.m = scanner.nextInt();
		for(int i = 0; i < input.n; i++) {
			for(int j = 0; j < input.m; j++) {
				input.a[i][j] = scanner.nextInt();
			}
		}
		scanner.close();
		return input;
	}
	
}

class RandomTaskInput implements TaskInput {
	
	Input toReturn = null;
	
	int freeCellsCount = 0;
	int[][] freeCells = new int[32 * 32][2];
	
	int[][] a = new int[32][32];
	
	void mark(int x, int y) {
		if(x < 0 || y < 0 || x >= toReturn.n || y >= toReturn.m) return;
		if(a[x][y] != 0) a[x][y]++;
	}
	
	public Input getInput() {
		if(toReturn != null) return toReturn;
		toReturn = new Input();
		toReturn.n = 5;
		toReturn.m = 5;
		for(int i = 0; i < toReturn.n; i++) {
			for(int j = 0; j < toReturn.m; j++) {
				freeCells[freeCellsCount][0] = i;
				freeCells[freeCellsCount][1] = j;
				freeCellsCount++;
			}
		}
		
		java.util.Random random = new java.util.Random();
		while(freeCellsCount > 0) {
			int index = random.nextInt(freeCellsCount);
			int x = freeCells[index][0];
			int y = freeCells[index][1];

			a[x][y]++;

			mark(x + 1, y);
			mark(x - 1, y);
			mark(x, y + 1);
			mark(x, y - 1);

			freeCellsCount--;
			
			int h = freeCells[index][0];
			freeCells[index][0] = freeCells[freeCellsCount][0];
			freeCells[freeCellsCount][0] = h;
			
			h = freeCells[index][1];
			freeCells[index][1] = freeCells[freeCellsCount][1];
			freeCells[freeCellsCount][1] = h;
		}
		
		for(int i = 0; i < toReturn.n; i++) {
			for(int j = 0; j < toReturn.m; j++) {
				if(a[i][j] > 4) a[i][j] -= 4;
				toReturn.a[i][j] = a[i][j];
			}
		}
		
		return toReturn;
	}
	
}

class TaskChecker {
	
	int[][] a = new int[32][32];
	int n, m;
	
	void mark(int x, int y) {
		x--; y--;
		if(x < 0 || y < 0 || x >= n || y >= m) return;
		if(a[x][y] != 0) a[x][y]++;
	}
	
	boolean isOk(TaskInput input, Solution solution) {
		
		this.n = input.getInput().n;
		this.m = input.getInput().m;
		
		for(int i = 0; i < solution.movesCount; i++) {
			if(a[solution.moves[i][0] - 1][solution.moves[i][1] - 1] != 0) {
				return false;
			}
			
			a[solution.moves[i][0] - 1][solution.moves[i][1] - 1]++;

			mark(solution.moves[i][0] + 1, solution.moves[i][1]);
			mark(solution.moves[i][0] - 1, solution.moves[i][1]);
			mark(solution.moves[i][0], solution.moves[i][1] + 1);
			mark(solution.moves[i][0], solution.moves[i][1] - 1);
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(a[i][j] > 4) a[i][j] -= 4;
				if(a[i][j] != input.getInput().a[i][j]) {
					for(int i1 = 0; i1 < n; i1++) {
						for(int j1 = 0; j1 < m; j1++) {
							System.out.print(a[i1][j1] + " ");
						}
						System.out.println();
					}
					return false;
				}
			}
		}
		
		return true;
	}
	
}

public class Abstractionism {

	long startTime = 0;
	boolean answerNotFound = false;
	
	int n, m;
	int[][] a = new int[32][32];
	int[][] edgeCase = new int[32][32];
	int[][] aTemp = new int[32][32];
	int[][] fTemp = new int[32][32];
	
	boolean[] clicked = new boolean[32 * 32];
	boolean[] five = new boolean[32 * 32];
	
	boolean[][] nonDetermined = new boolean[32][32];
	int[][] nonDeterminedIndex = new int[32][32];
	
	int choiceSize = 0;
	int[][] choice = new int[32 * 32][2];
	
	Solution solution = new Solution();
	
	int[][] f = new int[32][32];
	
	boolean mark(int[][] a, int[][] aOrig, int x, int y, int currentIndex) {
		if(x < 0 || y < 0 || x >= n || y >= m) return true;
		if(a[x][y] > 0) {
			a[x][y]--;
			if(nonDetermined[x][y]) {
				if(a[x][y] == 0 && !clicked[nonDeterminedIndex[x][y]]) a[x][y] = 4;
			} else {
				if(a[x][y] == 0 && fTemp[x][y] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	boolean process(int[][] a, int[][] aOrig, int x, int y, int currentIndex) {
		if(x < 0 || y < 0 || x >= n || y >= m) return true;
		if(a[x][y] != 1) return true;
		fTemp[x][y] = 1;
		solution.addMove(x, y);
		a[x][y]--;
		if(!mark(a, aOrig, x - 1, y, currentIndex)) return false;
		if(!mark(a, aOrig, x + 1, y, currentIndex)) return false;
		if(!mark(a, aOrig, x, y - 1, currentIndex)) return false;
		if(!mark(a, aOrig, x, y + 1, currentIndex)) return false;
		
		if(!process(a, aOrig, x - 1, y, currentIndex)) return false;
		if(!process(a, aOrig, x + 1, y, currentIndex)) return false;
		if(!process(a, aOrig, x, y - 1, currentIndex)) return false;
		if(!process(a, aOrig, x, y + 1, currentIndex)) return false;
		
		return true;
	}
	
	boolean tryDFS(int x, int y, boolean hasOne) {
		if(x < 0 || y < 0 || x >= n || y >= m) return false;
		if(a[x][y] == 0) return false;
		if(f[x][y] == 0) return DFS(x, y, hasOne);
		return false;
	}
	
	boolean DFS(int x, int y, boolean hasOne) {
		f[x][y] = 1;
		if(a[x][y] == 1) hasOne = true;
		boolean hasOne1 = tryDFS(x + 1, y, hasOne);
		boolean hasOne2 = tryDFS(x - 1, y, hasOne);
		boolean hasOne3 = tryDFS(x, y + 1, hasOne);
		boolean hasOne4 = tryDFS(x, y - 1, hasOne);
		return hasOne || hasOne1 || hasOne2 || hasOne3 || hasOne4;
	}
	
	int getVal(int x, int y, int v) {
		if(x < 0 || y < 0 || x >= n || y >= m) return 0;
		return a[x][y] != 0 ? 1 : 0;
	}
	
	boolean checkEdgeCase(int x, int y) {
		if(x < 0 || y < 0 || x >= n || y >= m) return false;
		return edgeCase[x][y] == 1;
	}
	
	boolean check() {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				f[i][j] = 0;
				edgeCase[i][j] = 0;
			}
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				int sum = 
						getVal(i + 1, j, a[i][j]) + 
						getVal(i - 1, j, a[i][j]) + 
						getVal(i, j + 1, a[i][j]) + 
						getVal(i, j - 1, a[i][j]);
				if(a[i][j] > sum + 1) {
					return false;
				}
				if(a[i][j] == sum + 1) {
					edgeCase[i][j] = 1;
				}
				if(a[i][j] < 0) return false;
			}
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {

				if(a[i][j] <= 1) continue;
				
				int checkValSum = 
						getVal(i + 1, j, a[i][j]) + 
						getVal(i - 1, j, a[i][j]) + 
						getVal(i, j + 1, a[i][j]) + 
						getVal(i, j - 1, a[i][j]);
				
				int checkEdgeSum = 
						(checkEdgeCase(i + 1, j) ? 1 : 0) + 
						(checkEdgeCase(i - 1, j) ? 1 : 0) + 
						(checkEdgeCase(i, j + 1) ? 1 : 0) + 
						(checkEdgeCase(i, j - 1) ? 1 : 0);
				
				if(checkValSum - checkEdgeSum <= 0) {
					return false;
				}
				
				if(checkEdgeCase(i, j)) {
					if(checkEdgeCase(i + 1, j) || checkEdgeCase(i - 1, j) || checkEdgeCase(i, j + 1) || checkEdgeCase(i, j - 1)) {
						return false;
					}
				}
			}
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(f[i][j] == 0 && a[i][j] != 0) {
					if(!DFS(i, j, false)) {
						return false;
					}
				}
			}
		}

		return true;
	}
	
	void increaseMark(int x, int y) {
		if(x < 0 || y < 0 || x >= n || y >= m) return;
		if(a[x][y] > 0) {
			a[x][y]++;
			if(a[x][y] == 5 && !five[nonDeterminedIndex[x][y]]) a[x][y] = 1;
		}
	}
	
	void increase(int x, int y) {
		if(x < 0 || y < 0 || x >= n || y >= m) return;
		a[x][y]++;
		if(a[x][y] == 5 && !five[nonDeterminedIndex[x][y]]) a[x][y] = 1;
		increaseMark(x - 1, y);
		increaseMark(x + 1, y);
		increaseMark(x, y - 1);
		increaseMark(x, y + 1);
	}
	
	void rollback(int currentSolutionSize) {
		for(int i = solution.movesCount - 1; i >= currentSolutionSize; i--) {
			increase(solution.moves[i][0], solution.moves[i][1]);
		}
	}
	
	void copyToTemp() {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				fTemp[i][j] = 0;
				aTemp[i][j] = a[i][j];
			}
		}
	}
	
	void copyFromTemp() {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				a[i][j] = aTemp[i][j];
			}
		}
	}
	
	boolean rec(int index) {
		
		if(System.currentTimeMillis() - startTime > 500) {
			answerNotFound = true;
			return true;
		}
		
		String beforeProcess = output();
		
		if(index == choiceSize) {
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < m; j++) {
					if(a[i][j] != 0) return false;
				}
			}
			return true;
		}
		
		if(!check()) {
			return false;
		}

		int x = choice[index][0];
		int y = choice[index][1];
		
		boolean tried = false;
		
		if(a[x][y] == 1) {
			tried = true;
			int currentSolutionSize = solution.movesCount;
			copyToTemp();
			if(process(aTemp, a, x, y, index)) {
				copyFromTemp();
				clicked[index] = true;
				if(!rec(index + 1)) {
					rollback(currentSolutionSize);
					solution.movesCount = currentSolutionSize;
					clicked[index] = false;
					String afterRollback = output();
					if(!beforeProcess.equals(afterRollback)) {
						throw new RuntimeException("Wrong answer");
					}
				} else {
					return true;
				}
			} else {
				solution.movesCount = currentSolutionSize;
			}
		}
		
		if(tried) {
			a[x][y] = 5;
		}
		five[index] = true;
		if(rec(index + 1)) {
			return true;
		}
		if(tried) {
			a[x][y] = 1;
		}
		five[index] = false;

		String afterRollback = output();
		if(!beforeProcess.equals(afterRollback)) {
			throw new RuntimeException("Wrong answer");
		}
		
		return false;
	}
	
	String output() {
		StringBuilder sb = new StringBuilder();
		sb.append(n).append(" ").append(m).append("\n");
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				sb.append(a[i][j]).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	String outputTemp() {
		StringBuilder sb = new StringBuilder();
		sb.append(n).append(" ").append(m).append("\n");
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				sb.append(aTemp[i][j]).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	Solution solve(Input in) {
		n = in.n;
		m = in.m;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				a[i][j] = in.a[i][j];
			}
		}

		for(int i = 1; i < n; i++) {
			for(int j = 1; j < m; j++) {
				if(a[i][j] == 1) {
					nonDetermined[i][j] = true;
				}
			}
		}
		
		copyToTemp();
		for(int i = 0; i < m; i++) {
			if(a[0][i] == 1) {
				if(!process(aTemp, a, 0, i, -1)) {
					return null;
				}
			}
			if(a[n - 1][i] == 1) {
				if(!process(aTemp, a, n - 1, i, -1)) {
					return null;
				}
			}
		}
		
		for(int i = 0; i < n; i++) {
			if(a[i][0] == 1) {
				if(!process(aTemp, a, i, 0, -1)) {
					return null;
				}
			}
			if(a[i][m - 1] == 1) {
				if(!process(aTemp, a, i, m - 1, -1)) {
					return null;
				}
			}
		}
		copyFromTemp();
		
		for(int i = 1; i < n; i++) {
			for(int j = 1; j < m; j++) {
				nonDetermined[i][j] = false;
			}
		}
		
		for(int i = 1; i < n; i++) {
			for(int j = 1; j < m; j++) {
				if(a[i][j] < 0) {
					a[i][j] += 4;
				}
				if(a[i][j] == 1) {
					choice[choiceSize][0] = i;
					choice[choiceSize][1] = j;
					nonDetermined[i][j] = true;
					nonDeterminedIndex[i][j] = choiceSize;
					choiceSize++;
				}
			}
		}
		
		if(rec(0)) {
			Solution toReturn = new Solution();
			for(int i = solution.movesCount - 1; i >= 0; i--) {
				toReturn.addMove(solution.moves[i][0] + 1, solution.moves[i][1] + 1);
			}
			return toReturn;
		}
		
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		
		for(int i = 1; i <= 1; i++) {
			
			Abstractionism a = new Abstractionism();
			TaskInput taskInput = new ConsoleTaskInput();
			//TaskInput taskInput = new RandomTaskInput();
			TaskChecker taskChecker = new TaskChecker();
			
			//System.out.println(taskInput.getInput().output());
			
			a.startTime = System.currentTimeMillis();
			Solution s = a.solve(taskInput.getInput());
			//System.out.println(System.currentTimeMillis() - a.startTime);
			
			if(a.answerNotFound) {
				System.out.println("No");
				break;
			}
			
			if(s != null) {
				s.output();
				boolean isOk = taskChecker.isOk(taskInput, s);
				if(!isOk) {
					System.out.println(taskInput.getInput().output());
					throw new RuntimeException("Wrong answer");
				}
			} else {
				//System.out.println(taskInput.getInput().output());
				//throw new RuntimeException("Wrong answer");
				System.out.println("No");
			}
		}
		
	}
	
}
