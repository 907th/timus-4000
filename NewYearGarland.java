public class NewYearGarland {

	int n;
	int[] a = new int[100004];
	
	boolean isOk(int y, int left) {
		return y <= left;
	}
	
	String getString(int y, int left) {
		StringBuilder sb = new StringBuilder();
		int currentY = y;
		for(int i = 0; i < left; i++) {
			if(currentY > 0) {
				if(left - i == 2 && currentY == 1) {
					sb.append('h');
				} else {
					sb.append('d');
					currentY--;
				}
			} else {
				sb.append('u');
				currentY++;
			}
		}
		return sb.toString();
	}
	
	String solve(String in) {
		n = in.length();
		int currentY = 0;
		for(int i = 0; i < n; i++) {
			if(in.charAt(i) == 'u') currentY++;
			if(in.charAt(i) == 'd') currentY--;
			a[i] = currentY;
		}
		for(int i = n - 1; i >= 0; i--) {
			if(in.charAt(i) == 'd') {
				if(a[i - 1] != 0 && isOk(a[i - 1], n - 1 - i)) { //try to do h
					return in.substring(0, i) + "h" + getString(a[i - 1], n - 1 - i);
				}
				if(isOk(a[i - 1] + 1, n - 1 - i)) { //try to do u
					return in.substring(0, i) + "u" + getString(a[i - 1] + 1, n - 1 - i);
				}
			}
			if(in.charAt(i) == 'h') {
				if(isOk(a[i - 1] + 1, n - 1 - i)) { //try to do u
					return in.substring(0, i) + "u" + getString(a[i - 1] + 1, n - 1 - i);
				}
			}
		}
		return "No solution";
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		scanner.nextInt();
		String h = scanner.next();
		scanner.close();
		NewYearGarland nyg = new NewYearGarland();
		h = nyg.solve(h);
		System.out.println(h);
	}
	
}
