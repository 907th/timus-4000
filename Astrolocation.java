public class Astrolocation {

	final double eps = 1e-9;
	
	int n;
	int d;
	
	int s1, e1, l1;
	int s2, e2, l2;
	
	double A;
	
	double[] a = new double[10024];
	
	int firstWall = -1;
	int lastWall = -1;
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		d = scanner.nextInt();
		s1 = scanner.nextInt();
		e1 = scanner.nextInt();
		l1 = scanner.nextInt();
		s2 = scanner.nextInt();
		e2 = scanner.nextInt();
		l2 = scanner.nextInt();
		A = Double.parseDouble(scanner.next());
		for(int i = 0; i < n; i++) {
			a[i] = Double.parseDouble(scanner.next());
		}
		scanner.close();
	}
	
	void solve() {
		init();
		for(int i = 0; i < n; i++) {
			if(a[i] <= 35) {
				for(int j = i + 1; j < n; j++) {
					if(a[j] >= 95) {
						firstWall = j;
						break;
					}
				}
				break;
			}
		}
		if(firstWall == -1) {
			System.out.println("No surface.");
			return;
		} else {
			System.out.println("Surface found at " + (firstWall + 1) + ".");
		}
		for(int i = firstWall + 1; i < n; i++) {
			a[i] = a[i] * (1 + ((i - firstWall) * A + eps));
		}
		
		for(int i = firstWall; i < n; i++) {
			if(a[i] <= 35) {
				for(int j = Math.max(i + 1, firstWall + d); j < n; j++) {
					if(a[j] >= 95) {
						lastWall = j;
						break;
					}
				}
				break;
			}
		}
		
		if(lastWall == -1) {
			System.out.println("No bottom.");
			lastWall = n;
		} else {
			System.out.println("Bottom found at " + (lastWall - firstWall) + ".");
		}
		
		boolean noSearch = true;
		
		double maxValue = -1;
		int maxIndex = -1;
		for(int i = firstWall + s1; i <= firstWall + e1 && i < lastWall; i++) {
			noSearch = false;
			if(maxValue < a[i]) {
				maxValue = a[i];
				maxIndex = i;
			}
		}
		
		if(noSearch) {
			System.out.println("Channel 1: No search.");
		} else
		if(maxValue >= l1) {
			System.out.println("Channel 1: Object at " + (maxIndex - firstWall) + ".");
		} else {
			System.out.println("Channel 1: No object.");
		}
		
		noSearch = true;
		maxValue = -1;
		maxIndex = -1;
		for(int i = firstWall + s2; i <= firstWall + e2 && i < lastWall; i++) {
			noSearch = false;
			if(maxValue < a[i]) {
				maxValue = a[i];
				maxIndex = i;
			}
		}

		if(noSearch) {
			System.out.println("Channel 2: No search.");
		} else
		if(maxValue >= l2) {
			System.out.println("Channel 2: Object at " + (maxIndex - firstWall) + ".");
		} else {
			System.out.println("Channel 2: No object.");
		}
		
	}
	
	public static void main(String[] args) {
		Astrolocation a = new Astrolocation();
		a.solve();
	}
	
}
