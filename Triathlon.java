public class Triathlon {

	class Participant {
		
		int bike;
		int swim;
		int run;
		int id;
		
		double getTime(Solution s) {
			return s.bikeDistance / bike + s.swimDistance / swim + s.runDistance / run;
		}
		
	}
	
	double getBest(Solution s) {
		double best = Double.MAX_VALUE;
		for(int i = 0; i < n; i++) {
			if(i == currentCheck) continue;
			double t = p[i].getTime(s);
			if(best > t) best = t;
		}
		return best;
	}
	
	class Solution implements Comparable<Solution> {
		
		double bikeDistance;
		double swimDistance;
		double runDistance;

		@Override
		public int compareTo(Solution o) {
			double v1 = -(getBest(this) - p[currentCheck].getTime(this));
			double v2 = -(getBest(o) - p[currentCheck].getTime(o));
			if(v1 < v2) return -1;
			if(v1 > v2) return 1;
			return 0;
		}
		
		@Override
		public String toString() {
			return getBest(this) + " " + p[currentCheck].getTime(this) + " " + (-getBest(this) + p[currentCheck].getTime(this)) + " (" + bikeDistance + ", " + swimDistance + ", " + runDistance + ")";
		}
		
	}

	int currentCheck = 0;
	int n;
	Participant[] p = new Participant[128];
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			p[i] = new Participant();
			p[i].bike = scanner.nextInt();
			p[i].swim = scanner.nextInt();
			p[i].run = scanner.nextInt();
		}
		scanner.close();
	}
	
	final double eps = 1e-7;
	final int generationsCount = 100;
	final int solutionsCount = 10;
	Solution[] sols = new Solution[128];

	Solution[] currentGeneration = new Solution[4096];

	java.util.Random random = new java.util.Random();
	
	void generateSolutions() {
		for(int i = 0; i < solutionsCount; i++) {
			sols[i] = new Solution();
			sols[i].bikeDistance = random.nextDouble() * 100;
			sols[i].swimDistance = random.nextDouble() * 100;
			sols[i].runDistance = random.nextDouble() * 100;
		}
	}
	
	Solution mergeSolutions(Solution a, Solution b) {
		Solution newSolution = new Solution();
		if(random.nextBoolean()) {
			newSolution.bikeDistance = a.bikeDistance;
		} else {
			newSolution.bikeDistance = b.bikeDistance;
		}
		if(random.nextBoolean()) {
			newSolution.swimDistance = a.swimDistance;
		} else {
			newSolution.swimDistance = b.swimDistance;
		}
		if(random.nextBoolean()) {
			newSolution.runDistance = a.runDistance;
		} else {
			newSolution.runDistance = b.runDistance;
		}
		
		if(random.nextBoolean()) {
			newSolution.bikeDistance += newSolution.bikeDistance * (random.nextDouble() - 0.5) / 2.0;
			if(newSolution.bikeDistance < 1e-3) newSolution.bikeDistance = 1e-3; 
		}
		if(random.nextBoolean()) {
			newSolution.swimDistance += newSolution.swimDistance * (random.nextDouble() - 0.5) / 2.0;
			if(newSolution.swimDistance < 1e-3) newSolution.swimDistance = 1e-3; 
		}
		if(random.nextBoolean()) {
			newSolution.runDistance += newSolution.runDistance * (random.nextDouble() - 0.5) / 2.0;
			if(newSolution.runDistance < 1e-3) newSolution.runDistance = 1e-3; 
		}
		
		return newSolution;
	}
	
	boolean winOn(Solution s) {
		double current = p[currentCheck].getTime(s);
		for(int i = 0; i < n; i++) {
			if(i == currentCheck) continue;
			if(p[i].getTime(s) <= current) {
				return false;
			}
		}
		return true;
	}
	
	boolean canWin() {
		
		for(int i = 0; i < n; i++) {
			if(i == currentCheck) continue;
			if(	p[currentCheck].swim <= p[i].swim &&
				p[currentCheck].bike <= p[i].bike &&
				p[currentCheck].run <= p[i].run) return false;
		}
		
		generateSolutions();
		java.util.Arrays.sort(sols, 0, solutionsCount);
		for(int i = 0; i < generationsCount; i++) {

			if(winOn(sols[0])) {
				return true;
			}
			
			int currentCount = 0;
			for(int j = 0; j < solutionsCount; j++) {
				currentGeneration[currentCount++] = sols[j];
			}
			for(int j = 0; j < solutionsCount; j++) {
				for(int k = 0; k < solutionsCount; k++) {
					if(i == j) continue;
					currentGeneration[currentCount++] = mergeSolutions(sols[j], sols[k]);
				}
			}
			java.util.Arrays.sort(currentGeneration, 0, currentCount);
			for(int j = 0; j < solutionsCount; j++) {
				sols[j] = currentGeneration[j];
			}
		}
		if(winOn(sols[0])) return true;
		return false;
	}
	
	void solve() {
		init();
		for(int i = 0; i < n; i++) {
			currentCheck = i;
			if(canWin()) {
				System.out.println("Yes");
			} else {
				System.out.println("No");
			}
		}
	}
	
	public static void main(String[] args) {
		Triathlon t = new Triathlon();
		t.solve();
	}
	
}