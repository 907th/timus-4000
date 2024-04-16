class AnotherShipsBF {

	public long maxWorkingTime = 400;
	public long startTime;
	
	public AnotherShipsBF(Set storage) {
		if(storage == null) this.storage = new Set(); else this.storage = storage;
	}
	
	class Set {

		private final int P = 65535;

		private int ky1 = 0;
		private int ky2 = 0;
		private int ky3 = 0;
		private int ky4 = 0;

		private final int HASH_SIZE = 65537;
		private final int SIZE = 1 << 16;

		int[] table = new int[HASH_SIZE];
		int[] nodeKey1 = new int[SIZE];
		int[] nodeKey2 = new int[SIZE];
		int[] nodeKey3 = new int[SIZE];
		int[] nodeKey4 = new int[SIZE];
		int[] nodeNext = new int[SIZE];
		byte[] nodeVal = new byte[SIZE];
		int pointer = 0;

		public Set() {
			for(int i = 0; i < table.length; i++) {
				table[i] = -1;
			}
		}

		public int get(byte[] unavailableShips) {
			int index = getKey(unavailableShips);

			for (int p = table[index]; p != -1; p = nodeNext[p]) {
				if (nodeKey1[p] == ky1 && nodeKey2[p] == ky2 && nodeKey3[p] == ky3 && nodeKey4[p] == ky4)
					return nodeVal[p];
			}
			return -1;
		}

		public void add(byte[] unavailableShips, int val) {

			if (pointer == SIZE)
				return;

			int index = getKey(unavailableShips);

			nodeKey1[pointer] = ky1;
			nodeKey2[pointer] = ky2;
			nodeKey3[pointer] = ky3;
			nodeKey4[pointer] = ky4;

			nodeNext[pointer] = table[index];
			nodeVal[pointer] = (byte) val;
			table[index] = pointer;
			pointer++;
		}

		private int getKey(byte[] unavailableShips) {
			ky1 = ky2 = ky3 = ky4;
			for (int i = 0; i < unavailableShips.length; i++) {
				if (unavailableShips[i] == 1) {
					if (i < 30)
						ky1 |= 1 << i;
					else if (i < 60)
						ky2 |= 1 << i;
					else if (i < 90)
						ky3 |= 1 << i;
					else if (i < 120)
						ky4 |= 1 << i;
				}
			}

			int commonKey = (((ky1 * P + ky2) * P + ky3) * P + ky4) % P;
			if (commonKey < 0)
				commonKey += P;
			return commonKey;
		}

	}

	class Row implements Comparable<Row> {
		int id, len;

		@Override
		public int compareTo(Row o) {
			return len - o.len;
		}

		@Override
		public String toString() {
			return len + "";
		}

	}

	class SolutionForRow implements Comparable<SolutionForRow> {
		int[] a;
		double score;

		@Override
		public int compareTo(SolutionForRow o) {
			if (score < o.score)
				return 1;
			if (score > o.score)
				return -1;
			return 0;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(a.length).append(": ");
			for (int i = 0; i < a.length; i++) {
				sb.append(a[i]).append(" ");
			}
			return sb.append("\n").toString();
		}

	}

	int[][] f = new int[2][0];
	int[][] cf = new int[2][0];

	SolutionForRow[][] rowsSolutions;

	private int maxRowLen;
	private Row[] rows;
	private int[] ships;
	private byte[] allowedShips = new byte[101];

	private int[] countOfShips = new int[128];
	private int differentShipsCount = 0;
	private int[] differentShips = new int[128];
	private int[] maxShipIndex = new int[128];

	private int rowSum = 0;
	private int shipSum = 0;
	
	private long recKnapsackTime = 0;
	
	private int[][] pred = new int[101][4096];
	int count = 0;

	private int[] solveKnapsack(byte[] allowedShips, int length, int from, int to, boolean onlyOne) {

		if (cf[0].length < length + 1) {
			cf = new int[2][length + 1];
		}

		java.util.Arrays.fill(cf[0], 0);
		java.util.Arrays.fill(cf[1], 0);

		int current = 0;
		cf[0][0] = 1;
		count = 0;

		for (int i = from; i <= to; i++) {
			int currentLength = differentShips[i];
			int currentShipCount = countOfShips[currentLength] - allowedShips[currentLength];

			if(onlyOne) currentShipCount = 1;
			for (int h = 0; h < currentShipCount; h++) {

				current = 1 - current;
				java.util.Arrays.fill(cf[current], 0);
				cf[current][0] = 1;
				cf[1 - current][0] = 1;
				for (int j = 0; j < length; j++) {
					if (cf[1 - current][j] != 0) {
						if (j + currentLength <= length) {
							pred[count][j + currentLength] = j;
							cf[current][j + currentLength] += cf[1 - current][j];
						}
					}
				}
				for (int j = 0; j <= length; j++) {
					if(cf[1 - current][j] != 0 && cf[current][j] == 0) {
						pred[count][j] = j;
					}
					cf[current][j] += cf[1 - current][j];
				}
				count++;
			}

		}

		return cf[current];
	}

	private int[][] solveKnapsack(int length) {

		if (f[0].length < length + 1) {
			f = new int[101][length + 1];
		}

		java.util.Arrays.fill(f[0], (byte) 0);
		java.util.Arrays.fill(f[1], (byte) 0);
		f[0][0] = 1;

		for (int i = 1; i <= ships.length; i++) {

			int currentLength = ships[i - 1];
			for (int j = 0; j < length; j++) {
				if (f[i - 1][j] != 0) {
					if (j + currentLength <= length) {
						if (f[i][j + currentLength] == 0 || f[i][j + currentLength] > f[i - 1][j] + 1)
							f[i][j + currentLength] = f[i - 1][j] + 1;
					}
				}
			}
			for (int j = 0; j <= length; j++) {
				if (f[i - 1][j] != 0) {
					if (f[i][j] == 0 || f[i][j] > f[i - 1][j])
						f[i][j] = f[i - 1][j];
				}
			}
		}

		return f;
	}

	int[] currentSolution = new int[101];
	int currentSolutionsLength;

	int totalSolutionsCount;
	int[][] solutions = new int[4096][100];
	int[] solutionLength = new int[4096];

	int[][][] allSolutionsForTheRow = new int[10][][];

	int[][] solution;

	private boolean addSolution() {
		if (totalSolutionsCount == solutionLength.length)
			return false;
		if(currentSolutionsLength > solutions[0].length) return true; 
		solutionLength[totalSolutionsCount] = currentSolutionsLength;
		for (int i = 0; i < currentSolutionsLength; i++) {
			solutions[totalSolutionsCount][i] = currentSolution[i];
		}
		totalSolutionsCount++;
		return true;
	}

	private int[][] getSolutionsForRow(int currentLength) {
		totalSolutionsCount = 0;
		java.util.Arrays.fill(solutionLength, 0);
		int[][] r = solveKnapsack(currentLength);
		
		recKnapsack(r, currentLength, differentShipsCount - 1, 0);
		int[][] localSolutions = new int[totalSolutionsCount][];
		for (int i = 0; i < localSolutions.length; i++) {
			localSolutions[i] = new int[solutionLength[i]];
			for (int j = 0; j < localSolutions[i].length; j++) {
				localSolutions[i][j] = solutions[i][j];
			}
		}
		return localSolutions;
	}

	private int[] answers = new int[16];
	private int answerDifference = 0;

	public Set storage;
	byte[] index = new byte[101];
	byte[] unavailableShips = new byte[101];

	private void addToStore() {
		if(System.currentTimeMillis() - startTime > maxWorkingTime) return;
		for (int i = 0; i < differentShipsCount; i++) {
			int cur = differentShips[i];
			for (int j = 0; j < countOfShips[cur] - allowedShips[cur]; j++) {
				unavailableShips[index[cur] + j] = 1;
			}
		}
		storage.add(unavailableShips, 0);
		for (int i = 0; i < differentShipsCount; i++) {
			int cur = differentShips[i];
			for (int j = 0; j < countOfShips[cur] - allowedShips[cur]; j++) {
				unavailableShips[index[cur] + j] = 0;
			}
		}
	}

	private boolean stored() {
		for (int i = 0; i < differentShipsCount; i++) {
			int cur = differentShips[i];
			for (int j = 0; j < countOfShips[cur] - allowedShips[cur]; j++) {
				unavailableShips[index[cur] + j] = 1;
			}
		}
		int h = storage.get(unavailableShips);
		for (int i = 0; i < differentShipsCount; i++) {
			int cur = differentShips[i];
			for (int j = 0; j < countOfShips[cur] - allowedShips[cur]; j++) {
				unavailableShips[index[cur] + j] = 0;
			}
		}
		return h != 0;
	}
	
	private boolean rec(int rowIndex, int howMuchSolutions) {

		if(System.currentTimeMillis() - startTime > maxWorkingTime) return false;

		if (!stored())
			return false;
		
		if (rows.length - rowIndex == 2) {
			int[] r = solveKnapsack(allowedShips, rows[rowIndex].len, 0, differentShipsCount - 1, false);
			for (int k = 0; k <= rowSum - shipSum; k++) {
				if (r[rows[rowIndex].len - k] != 0) {
					answerDifference = rows[rowIndex].len - k;
					return true;
				}
			}
			addToStore();
			return false;
		}

		int checked = 0;
		for (int i = 0; i < rowsSolutions[rowIndex].length; i++) {
			if (checked > howMuchSolutions)
				break;
			boolean failed = false;
			int failedOn = -1;
			for (int j = 0; j < rowsSolutions[rowIndex][i].a.length; j++) {
				allowedShips[rowsSolutions[rowIndex][i].a[j]]++;
				if (countOfShips[rowsSolutions[rowIndex][i].a[j]] < allowedShips[rowsSolutions[rowIndex][i].a[j]]) {
					failedOn = j;
					failed = true;
					break;
				}
			}
			if (!failed) {
				checked++;
				answers[rowIndex] = i;
				if (rec(rowIndex + 1, howMuchSolutions + 1))
					return true;
			}
			for (int j = 0; j < rowsSolutions[rowIndex][i].a.length; j++) {
				allowedShips[rowsSolutions[rowIndex][i].a[j]]--;
				if (j == failedOn)
					break;
			}
		}
		addToStore();
		return false;

	}

	private int[] currentOn = new int[64];
	private int[] stopSolutions = new int[] {100, 90, 80, 100, 100, 90, 80, 70, 60, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};

	private boolean recKnapsack(int[][] r, int currentLength, int l, int level) {
		if(System.currentTimeMillis() - recKnapsackTime > maxWorkingTime / 3) return false;
		if (currentLength == 0) {
			if(!addSolution()) return false;
			for(int lv = 0; lv < level; lv++) {
				if(totalSolutionsCount - currentOn[lv] >= stopSolutions[lv]) return true;
			}
			return true;
		}
		if (l < 0)
			return true;
		if (r[maxShipIndex[differentShips[l]]][currentLength] <= 0)
			return true;
		for (int i = l; i >= 0; i--) {
			int k = differentShips[i];
			
			int start = 1;
			int finish = countOfShips[k] + 1;
			int it = 1;

			for (int j = start; j != finish; j += it) {
				currentOn[level] = totalSolutionsCount;
				if (currentLength - j * k >= 0) {
					for (int h = 0; h < j; h++) {
						currentSolution[currentSolutionsLength++] = k;
					}
					boolean b = recKnapsack(r, currentLength - j * k, i - 1, level + 1);
					currentSolutionsLength -= j;
					if(!b) return false;

					for(int lv = 0; lv < level; lv++) {
						if(totalSolutionsCount - currentOn[lv] >= stopSolutions[lv]) return true;
					}
				}
			}
		}
		return true;
	}

	private void printSolution() {
		for (int i = 0; i < rows.length; i++) {
			System.out.println(solution[i].length);
			for (int j = 0; j < solution[i].length; j++) {
				System.out.print(solution[i][j] + " ");
			}
			System.out.println();
		}
	}

	public boolean solve(int[] s, int[] r, char order, char includeSorting, boolean outputNeeded, boolean extendedScore) {
		rows = new Row[r.length];
		rowsSolutions = new SolutionForRow[r.length][];
		ships = s;
		for (int i = 0; i < r.length; i++) {
			if (maxRowLen < r[i])
				maxRowLen = r[i];
			rows[i] = new Row();
			rows[i].id = i;
			rows[i].len = r[i];
			rowSum += r[i];
		}
		for (int i = 0; i < ships.length; i++) {
			shipSum += ships[i];
		}
		java.util.Arrays.sort(ships);

		for (int i = 0; i < ships.length; i++) {
			if (i == 0 || ships[i] != ships[i - 1]) {
				index[ships[i]] = (byte) i;
				differentShips[differentShipsCount++] = ships[i];
			}
			maxShipIndex[ships[i]] = i + 1;
			countOfShips[ships[i]]++;
		}

		java.util.Arrays.sort(rows);
		if (order == 'A') {
			int limit = rows.length / 2;
			for(int i = 0; i < limit / 2; i++) {
				Row rw = rows[i];
				rows[i] = rows[limit - i - 1];
				rows[limit - i - 1] = rw;
			}
		}

		recKnapsackTime = System.currentTimeMillis();
		for (int i = 0; i < rows.length - 2; i++) {
			allSolutionsForTheRow[i] = getSolutionsForRow(rows[i].len);
			rowsSolutions[i] = new SolutionForRow[allSolutionsForTheRow[i].length];
			for (int j = 0; j < allSolutionsForTheRow[i].length; j++) {
				rowsSolutions[i][j] = new SolutionForRow();
				rowsSolutions[i][j].a = allSolutionsForTheRow[i][j];
				rowsSolutions[i][j].score = 100 - allSolutionsForTheRow[i][j].length;
				rowsSolutions[i][j].score = rowsSolutions[i][j].score * 10000;

				if(allSolutionsForTheRow[i][j].length <= 3 && extendedScore) {
					rowsSolutions[i][j].score = rowsSolutions[i][j].score + (allSolutionsForTheRow[i][j][0] - allSolutionsForTheRow[i][j][1]);
				}
				
			}
			if (includeSorting == 'Y') {
				java.util.Arrays.sort(rowsSolutions[i]);
			}

		}

		solution = new int[rows.length][];

		boolean h = false;
		h = rec(0, 5);
		
		if (h) {

			java.util.ArrayList<Integer> localAnswerShips = new java.util.ArrayList<Integer>();
			java.util.ArrayList<Integer> lastRowShips = new java.util.ArrayList<Integer>();
			int current = answerDifference;
						
			solveKnapsack(allowedShips, current, 0, differentShipsCount - 1, false);
			count--;
			
			while (true) {
				int newCurrent = pred[count][current];
				int currentShip = current - newCurrent;
				if(currentShip > 0) {
					localAnswerShips.add(currentShip);
					allowedShips[currentShip]++;
				}
				current = newCurrent;
				count--;
				if (current == 0)
					break;
			}
			for (int i = 0; i < differentShipsCount; i++) {
				for (int j = 0; j < countOfShips[differentShips[i]] - allowedShips[differentShips[i]]; j++) {
					lastRowShips.add(differentShips[i]);
				}
			}
			
			for (int i = 0; i < rows.length - 2; i++) {
				if(solution[rows[i].id] == null) solution[rows[i].id] = rowsSolutions[i][answers[i]].a;
			}
			solution[rows[rows.length - 2].id] = new int[localAnswerShips.size()];
			for (int i = 0; i < localAnswerShips.size(); i++) {
				solution[rows[rows.length - 2].id][i] = localAnswerShips.get(i);
			}
			solution[rows[rows.length - 1].id] = new int[lastRowShips.size()];
			for (int i = 0; i < lastRowShips.size(); i++) {
				solution[rows[rows.length - 1].id][i] = lastRowShips.get(i);
			}
			if(outputNeeded) printSolution();
		} else {
			return false;
		}

		return true;
	}

}

public class ShipsBF {
	
	private long maxWorkingTime = 70;
	
	public ShipsBF() {
		startTime = System.currentTimeMillis();
	}
	
	class Set {
		
		private final int P = 65535;
		
		private int ky1 = 0;
		private int ky2 = 0;
		private int ky3 = 0;
		private int ky4 = 0;
		
		private final int HASH_SIZE = 65537;
		private final int SIZE = 1 << 16;
		
		int[] table = new int[HASH_SIZE];
		int[] nodeKey1 = new int[SIZE];
		int[] nodeKey2 = new int[SIZE];
		int[] nodeKey3 = new int[SIZE];
		int[] nodeKey4 = new int[SIZE];
		int[] nodeNext = new int[SIZE];
		byte[] nodeVal = new byte[SIZE];
		int pointer = 0;
		
		public Set() {
			for(int i = 0; i < table.length; i++) {
				table[i] = -1;
			}
		}
		
		public int get(byte[] unavailableShips) {
			int index = getKey(unavailableShips);

			for(int p = table[index]; p != -1; p = nodeNext[p]) {
				if(nodeKey1[p] == ky1 && nodeKey2[p] == ky2 && nodeKey3[p] == ky3 && nodeKey4[p] == ky4) return nodeVal[p];
			}
			return -1;
		}
		
		public void add(byte[] unavailableShips, int val) {
			
			if(pointer == SIZE) return;
			
			int index = getKey(unavailableShips);

			nodeKey1[pointer] = ky1;
			nodeKey2[pointer] = ky2;
			nodeKey3[pointer] = ky3;
			nodeKey4[pointer] = ky4;

			nodeNext[pointer] = table[index];
			nodeVal[pointer] = (byte)val;
			table[index] = pointer;
			pointer++;
		}
		
		
		private int getKey(byte[] unavailableShips) {
			ky1 = ky2 = ky3 = ky4;
			for(int i = 0; i < unavailableShips.length; i++) {
				if(unavailableShips[i] == 1) 
				{
					if(i < 30)  ky1 |= 1 << i; else
					if(i < 60)  ky2 |= 1 << i; else
					if(i < 90)  ky3 |= 1 << i; else
					if(i < 120) ky4 |= 1 << i;
				}
			}
			
			int commonKey = (((ky1 * P + ky2) * P + ky3) * P + ky4) % P;
			if(commonKey < 0) commonKey += P;
			return commonKey;
		}
		
	}

	class Row implements Comparable<Row> {
		int id, len;

		@Override
		public int compareTo(Row o) {
			return len - o.len;
		}
		
		@Override
		public String toString() {
			return len + "";
		}
		
	}

	class ShipOrder implements Comparable<ShipOrder> {
		byte ship;
		double value;
		
		@Override
		public int compareTo(ShipOrder o) {
			if(value - o.value < 0) return -1;
			if(value - o.value > 0) return  1;
			return 0;
		}
		
		@Override 
		public String toString() {
			return ships[ship] + ":" + value;
		}
		
	}
	
	Set storage = new Set();
	
	double[][] f = new double[2][0];
	byte[] p = new byte[0];

	private Row[] rows;
	private int[] ships;
	
	private int[] rowAnswerCount = new int[11];
	private int[][] rowAnswer = new int[11][101];
	
	private int[] countOfShips = new int[128];
	
	private long startTime;
	
	private double[] solveKnapsack(byte[] allowedShips, int length) {
		
		if(f[0].length < length + 1) {
			f = new double[2][length + 1];
			p = new byte[length + 1];
		}
		
		java.util.Arrays.fill(f[0], (byte)0);
		java.util.Arrays.fill(f[1], (byte)0);
		int current = 0;
		f[0][0] = 1;
		p[0] = -1;
		
		for(int i = 0; i < ships.length; i++) {
			
			if(allowedShips[i] == 1) continue;
			
			int currentLength = ships[i];
			current = 1 - current;
			java.util.Arrays.fill(f[current], (byte)0);
			f[current][0] = 1;
			f[1 - current][0] = 1;
			for(int j = 0; j < length; j++) {
				if( f[1 - current][j] != 0) {
					if(j + currentLength <= length) {
						if(f[current][j + currentLength] == 0 || f[current][j + currentLength] > f[1 - current][j] + 1) 
						f[current][j + currentLength] = f[1 - current][j] + 1;
					}
				}
			}
			
			for(int j = 0; j <= length; j++) {
				if(f[1 - current][j] != 0) {
					if(f[current][j] == 0 || f[current][j] > f[1 - current][j]) f[current][j] = f[1 - current][j];
				}
			}
			
		}
		
		double[] response = new double[length + 1];
		response = java.util.Arrays.copyOf(f[current], length + 1);
		
		return response;
	}
	
	private byte[] currentShips = new byte[101];
	
	private byte[] normalize() {
		byte[] currentUnavailableShips = new byte[101];
		int index = 0;
		for(int i = 0; i < ships.length; i++) {
			if(currentShips[i] == 1) {
				while(true) if(ships[index] == ships[i]) break; else index++;
				currentUnavailableShips[index] = 1;
				index++;
			}
		}
		return currentUnavailableShips;
	}
	
	private boolean recKnapsack(double[] r, int currentLength, int rowIndex) {
		
		if(System.currentTimeMillis() - startTime > maxWorkingTime) return false;

		byte[] cuv = normalize();
		int h = storage.get(cuv);
		if(h != -1) return h != 0;

		if(currentLength == 0) {
			boolean b = rec(rowIndex + 1);
			if(!b) {
				if(System.currentTimeMillis() - startTime <= maxWorkingTime) storage.add(cuv, 0);
			}
			return b;
		}
		
		ShipOrder[] shipOrders = new ShipOrder[ships.length];
		int availableShipsCount = 0;
		for(int i = 0; i < ships.length; i++) {
			if(currentShips[i] == 1) continue;
			shipOrders[availableShipsCount] = new ShipOrder();
			shipOrders[availableShipsCount].ship = (byte)i;
			if(currentLength - ships[i] < 0)
				shipOrders[availableShipsCount].value = Double.MAX_VALUE; else
				shipOrders[availableShipsCount].value = r[currentLength - ships[i]];
			availableShipsCount++;
		}

		java.util.Arrays.sort(shipOrders, 0, availableShipsCount);
		for(int k = 0; k < availableShipsCount; k++) {
			int i = shipOrders[k].ship;
			if(currentLength - ships[i] < 0) continue;
			
			if(r[currentLength - ships[i]] == 0) continue;
			currentShips[i] = 1;
			rowAnswer[rowIndex][rowAnswerCount[rowIndex]++] = i;
			if(recKnapsack(r, currentLength - ships[i], rowIndex)) {
				return true;
			}
			rowAnswerCount[rowIndex]--;
			currentShips[i] = 0;
		}
		if(System.currentTimeMillis() - startTime <= maxWorkingTime) storage.add(cuv, 0);
		return false;
	}
	
	private boolean rec(int rowIndex) {
		if(System.currentTimeMillis() - startTime > maxWorkingTime) return false;
		if(rowIndex >= rows.length) return true;
		double[] r = solveKnapsack(currentShips, rows[rowIndex].len);
		for(int i = rowIndex; i < rows.length; i++) {
			if(r[rows[rowIndex].len] == 0) {
				storage.add(currentShips, 0);
				return false;
			}
		}
		return recKnapsack(r, rows[rowIndex].len, rowIndex);
	}
	
	public boolean solve(int[] s, int[] r, boolean outputNeeded) {
		rows = new Row[r.length];
		ships = s;
		
		for(int i = 0; i < r.length; i++) {
			rows[i] = new Row(); rows[i].id = i; rows[i].len = r[i];
		}
		java.util.Arrays.sort(ships);
		for(int i = 0; i < ships.length; i++) {
			countOfShips[ships[i]]++;
		}
		
		for(int i = 0; i < ships.length / 2; i++) {
			int h = ships[i];
			ships[i] = ships[ships.length - i - 1];
			ships[ships.length - i - 1] = h;
		}

		java.util.Arrays.sort(rows);

		char first = 'D', second = 'A';
		
		if(!rec(0)) {
			AnotherShipsBF asbf = null;
			asbf = new AnotherShipsBF(null);
			asbf.startTime = System.currentTimeMillis();
			boolean extendedScore = false;
			
			if(!asbf.solve(s, r, first, 'Y', outputNeeded, extendedScore)) {
				AnotherShipsBF asbf1 = new AnotherShipsBF(asbf.storage);
				asbf1.maxWorkingTime = 450;
				asbf1.startTime = System.currentTimeMillis();
				
				if(!asbf1.solve(s, r, second, 'Y', outputNeeded, !extendedScore)) {
					return false;
				}
			};
			return false;
		}

		if(outputNeeded) {
			for(int i = 0; i < rows.length; i++) {
				for(int j = 0; j < rows.length; j++) {
					if(i == rows[j].id) {
						System.out.println(rowAnswerCount[j]);
						for(int k = 0; k < rowAnswerCount[j]; k++) {
							System.out.print(ships[rowAnswer[j][k]] + " ");
							countOfShips[ships[rowAnswer[j][k]]]--;
						}
						System.out.println();
						break;
					}
				}
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		java.util.Scanner s = new java.util.Scanner(System.in);
		int n = s.nextInt();
		int m = s.nextInt();
		int[] ships = new int[n];
		for(int i = 0; i < n; i++) {
			ships[i] = s.nextInt();
		}
		int[] rows = new int[m];
		for(int i = 0; i < m; i++) {
			rows[i] = s.nextInt();
		}
		s.close();
		ShipsBF sg = new ShipsBF();
		sg.solve(ships, rows, true);
	}
	
}