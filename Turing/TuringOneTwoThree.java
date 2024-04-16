public class TuringOneTwoThree {

	int n = 0;
	String[] commands = new String[10024];
	
	void solveForOne() {
		System.out.println("4");
		System.out.println("1 - 2 - >");
		System.out.println("2 # 4 # <");
		System.out.println("2 - 3 - <");
		System.out.println("3 - 1 + >");
	}
	
	void solveForMoreThanOne(int k) {
		for(int i = 1; i < k; i++) {
			commands[n++] = i + " - " + (i + 1) + " - " + "> ";
			commands[n++] = i + " + " + i + " + " + "> ";
			commands[n++] = i + " # " + (1000 + i) + " # " + "< ";
			
			commands[n++] = (1000 + i) + " # " + i + " # " + "> ";
			commands[n++] = (1000 + i) + " - " + (2000 + i) + " - " + "< ";
			commands[n++] = (1000 + i) + " + " + (1000 + i) + " + " + "< ";

			commands[n++] = (2000 + i) + " - " + (3000 + i) + " - " + "< ";
			commands[n++] = (2000 + i) + " + " + (2000 + i) + " + " + "< ";

			commands[n++] = (2000 + i) + " # " + 10000 + " # " + "> ";

			commands[n++] = (3000 + i) + " - " + (3000 + i) + " - " + "< ";
			commands[n++] = (3000 + i) + " + " + (3000 + i) + " + " + "< ";
			commands[n++] = (3000 + i) + " # " + i + " # " + "> ";

		}

		commands[n++] = k + " - " + 1 + " + " + "> ";
		commands[n++] = k + " + " + k + " + " + "> ";
		commands[n++] = k + " # " + (1000 + k) + " # " + "< ";
		
		commands[n++] = (1000 + k) + " # " + k + " # " + "> ";
		commands[n++] = (1000 + k) + " - " + (2000 + k) + " - " + "< ";
		commands[n++] = (1000 + k) + " + " + (1000 + k) + " + " + "< ";
		
		commands[n++] = (2000 + k) + " - " + (3000 + k) + " - " + "< ";
		commands[n++] = (2000 + k) + " + " + (2000 + k) + " + " + "< ";

		commands[n++] = (3000 + k) + " - " + (3000 + k) + " - " + "< ";
		commands[n++] = (3000 + k) + " + " + (3000 + k) + " + " + "< ";
		commands[n++] = (3000 + k) + " # " + k + " # " + "> ";

		commands[n++] = (2000 + k) + " # " + 10000 + " # " + "> ";

		commands[n++] = 10000 + " - " + 10001 + " - " + "= ";
		commands[n++] = 10000 + " + " + 10000 + " + " + "> ";

		System.out.println(n);
		for(int i = 0; i < n; i++) {
			System.out.println(commands[i]);
		}
	}
	
	void solve(int k) {
		if(k == 1) {
			solveForOne();
		} else {
			solveForMoreThanOne(k);
		}
	}
	
	public static void main(String[] args) {
		TuringOneTwoThree tott = new TuringOneTwoThree();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int k = scanner.nextInt();
		scanner.close();
		tott.solve(k);
	}
	
}
