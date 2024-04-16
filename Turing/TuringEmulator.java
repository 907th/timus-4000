public class TuringEmulator {

	char[] l = new char[1024];
	
	int currentState = 1;
	int currentIndex = 1;
	
	int n;

	int[] currentStateTable = new int[10024];
	char[] currentLSymbol = new char[10024];
	int[] newStateTable = new int[10024];
	char[] newLSymbol = new char[10024];
	int[] movement = new int[10024];
	
	int howManyMinuses(int left, int right) {
		int count = 0;
		for(int i = left; i <= right; i++) {
			if(l[i] == '-') count++;
		}
		return count;
	}
	
	void printL(int lf, int rg) {
		for(int i = lf; i < rg; i++) {
			System.out.print(l[i] + " ");
		}
		System.out.println();
		for(int i = lf; i < rg; i++) {
			char symbol = ' ';
			if(i == currentIndex) symbol = '^';
			System.out.print(symbol + " ");
		}
		System.out.println();
	}
	
	void init(int m) {
		for(int i = 0; i < l.length; i++) {
			l[i] = '-';
		}
		l[0] = l[m + 1] = '#';

		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = Integer.parseInt(scanner.nextLine().trim());
		for(int i = 0; i < n; i++) {
			String s = scanner.nextLine();
			String[] t = s.split(" ");
			currentStateTable[i] = Integer.parseInt(t[0]);
			currentLSymbol[i] = t[1].charAt(0);
			
			newStateTable[i] = Integer.parseInt(t[2]);
			newLSymbol[i] = t[3].charAt(0);
			
			if(t[4].charAt(0) == '>') movement[i] =  1;
			if(t[4].charAt(0) == '=') movement[i] =  0;
			if(t[4].charAt(0) == '<') movement[i] = -1;
		}
		scanner.close();
	}
	
	boolean applyTransition() {
		for(int i = 0; i < n; i++) {
			if(currentState == currentStateTable[i] && currentLSymbol[i] == l[currentIndex]) {
				l[currentIndex] = newLSymbol[i];
				currentState = newStateTable[i];
				currentIndex += movement[i];
				return true;
			}
		}
		return false;
	}
	
	void simulate(int m) {
		init(m);
		while(applyTransition()) {
			printL(0, m + 2);
		}
	}
	
	public static void main(String[] args) {
		TuringEmulator te = new TuringEmulator();
		te.simulate(10);
	}
	
}
