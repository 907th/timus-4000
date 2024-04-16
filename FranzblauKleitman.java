//Franzblau & Kleitman Algorithm. https://core.ac.uk/download/pdf/82333912.pdf - 1984.

class Interval implements Comparable<Interval> {

	protected int l, r, id;

	public Interval(int l, int r, int id) {
		this.l = l; this.r = r; this.id = id;
	}
	
	@Override
	public int compareTo(Interval o) {
		if(l == o.l) return r - o.r;
		return l - o.l;
	}
	
	@Override
	public String toString() {
		return l + " " + r;
	}
	
}

public class FranzblauKleitman {
	
	private Interval[] intervals = new Interval[64 * 64];
	private int intervalsCount = 0;
	private boolean[][] checkedIntervals = new boolean[64][64];
	private int[][] a = new int[64][64];
	private int n, m;

	private Interval[] newIntervals = new Interval[64 * 64];
	private int newIntervalCount = 0;
	
	private void addInterval(int l, int r) {
		if(checkedIntervals[l][r]) return;
		checkedIntervals[l][r] = true;
		intervals[intervalsCount++] = new Interval(l, r, intervalsCount + 1);
	}
	
	private void updateIntervals() {
		newIntervalCount = 0;
		for(int i = 0; i < intervalsCount; i++) {
			if(removeFlag[i] == 0) {
				newIntervals[newIntervalCount++] = intervals[i];
			}
		}
		
		intervalsCount = 0;
		for(int i = 0; i < newIntervalCount; i++) {
			intervals[intervalsCount++] = newIntervals[i];
		}
		java.util.Arrays.sort(intervals, 0, intervalsCount);
	}
	
	private void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		m = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			String temp = scanner.next();
			for(int j = 0; j < m; j++) {
				if(temp.charAt(j) == '1') a[i][j] = 1;
			}
		}
		scanner.close();
	}
	
	private void initIntervals() {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(a[i][j] == 1) {
					int mostLeft = j;
					int mostRight = j;
					while(true) {
						if(mostLeft - 1 < 0 || a[i][mostLeft - 1] == 0) break;
						mostLeft--;
					}
					while(true) {
						if(mostRight + 1 >= m || a[i][mostRight + 1] == 0) break;
						mostRight++;
					}
					addInterval(mostLeft, mostRight);
				}
			}
		}
		java.util.Arrays.sort(intervals, 0, intervalsCount);
	}
	
	private void print() {
		System.out.println(intervalsCount);
		for(int i = 0; i < intervalsCount; i++) {
			int startLine = 0;
			for(int k = 0; k < n; k++) {
				boolean failed = false;
				for(int j = intervals[i].l; j <= intervals[i].r; j++) {
					if(a[k][j] == 0) {
						failed = true;
						break;
					}
				}
				if(!failed) {
					startLine = k; break;
				}
			}

			int endLine = startLine;
			for(int k = startLine + 1; k < n; k++) {
				boolean failed = false;
				for(int j = intervals[i].l; j <= intervals[i].r; j++) {
					if(a[k][j] == 0) {
						failed = true;
						break;
					}
				}
				if(failed) break;
				endLine = k;
			}
			System.out.printf("%d %d %d %d\n", startLine + 1, intervals[i].l + 1, endLine + 1, intervals[i].r + 1);
		}
	}
	
	private int[] brackets = new int[128];
	private int[] removeFlag = new int[128];
	private int[] f = new int[128];
	private int bracketsSize;
	private boolean[] checkedInterval = new boolean[128];
	
	private boolean getBrackets() {
		if(intervalsCount == 1) return false;
		for(int i = intervalsCount - 1; i >= 1; i--) {
			if(intervals[i - 1].l == intervals[i].l) {

				java.util.Arrays.fill(checkedInterval, false);

				int start = intervals[i - 1].l;
				int[] right = new int[2];
				right[0] = intervals[i - 1].r;
				right[1] = intervals[i].r;
				checkedInterval[i - 1] = checkedInterval[i] = true;
				
				while(true) {
					if(right[0] == right[1]) {
						bracketsSize = 0;
						for(int j = 0; j < intervalsCount; j++) {
							if(isInside(intervals[j].l, intervals[j].r, start, right[0])) {
								brackets[bracketsSize++] = j;
							}
						}
						return true;
					}
					int mn = 1000000000;
					
					int index = -1;
					for(int j = 0; j < intervalsCount; j++) {
						if(checkedInterval[j]) continue;
						if(right[0] < right[1]) {
							if(intervals[j].l >= start && intervals[j].l <= right[0] + 1 && intervals[j].r > right[0]) {
								if( mn > intervals[j].r) {
									mn = intervals[j].r;
									index = j;
								}
							}
						} else {
							if(intervals[j].l >= start && intervals[j].l <= right[1] + 1 && intervals[j].r > right[1]) {
								if( mn > intervals[j].r) {
									mn = intervals[j].r;
									index = j;
								}
							}
						}
					}
					if(mn == 1000000000) break;
					if(right[0] < right[1]) {
						right[0] = mn;
					} else {
						right[1] = mn;
					}
					checkedInterval[index] = true;
				}
				
			}
		}
		return false;
	}
	
	private boolean isInside(int l1, int r1, int l2, int r2) {
		return l2 <= l1 && r1 <= r2;
	}

	private void reduce() {
		java.util.Arrays.fill(f, 0);
		java.util.Arrays.fill(removeFlag, 0);
		
		for(int i = 0; i < bracketsSize; i++) {
			for(int j = 0; j < bracketsSize; j++) {
				if(i == j) continue;
				if(isInside(
						intervals[brackets[i]].l,
						intervals[brackets[i]].r, 
						intervals[brackets[j]].l, 
						intervals[brackets[j]].r)) {
					f[brackets[i]] = 1;
					break;
				}
			}
		}
		int[] rSet = new int[64];
		int rSetSize = 0;
		for(int i = 0; i < bracketsSize; i++) {
			if(f[brackets[i]] == 0) {
				rSet[rSetSize++] = brackets[i];
			}
		}

		for(int i = 0; i < rSetSize; i++) {
			removeFlag[rSet[i]] = 1;
		}
		
		for(int i = 1; i < rSetSize; i++) {
			addInterval(intervals[rSet[i]].l, intervals[rSet[i - 1]].r);
		}
		
		updateIntervals();
		
	}
	
	private void reduceSet() {
		while(true) {
			if(!getBrackets()) return;
			reduce();
		}
	}
	
	public void solve() {
		init();
		initIntervals();
		reduceSet();
		print();
	}
	
	public static void main(String[] args) {
		FranzblauKleitman fk = new FranzblauKleitman();
		fk.solve();
	}
	
}