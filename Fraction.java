import java.math.BigInteger;

public class Fraction {

	int[] p = new int[32768];
	int[] digits = new int[32678];
	int[] values = new int[32678];
	int n = 0;
	
	char getC(int a, int k) {
		if(a < 10) return (char)(a + '0');
		return (char)(a - 10 + 'A');
	}
	
	String solve(int a, int b, int k) {
		java.util.Arrays.fill(p, 0);
		
		String intPart = BigInteger.valueOf(a / b).toString(k).toUpperCase();
		int digitsCount = 0;

		a = a % b;
		int edge = -1;
		while(a != 0) {
			p[a] = 1;
			
			digits[digitsCount] = a;
			values[digitsCount++] = a * k / b;

			a = a * k % b;
			if(p[a] != 0) {
				edge = a;
				break;
			}
		}
		
		StringBuilder fractionPart = new StringBuilder();
		for(int i = 0; i < digitsCount; i++) {
			if(digits[i] == edge) {
				fractionPart.append("(");
				for(int j = i; j < digitsCount; j++) {
					fractionPart.append(getC(values[j], k));
				}
				fractionPart.append(")");
				break;
			}
			fractionPart.append(getC(values[i], k));
		}
				
		return intPart + (fractionPart.length() == 0 ? "" : "." + fractionPart.toString());
	}
	
	public static void main(String[] args) {
		Fraction f = new Fraction();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		while(true) {
			int a = scanner.nextInt();
			int b = scanner.nextInt();
			if(a == 0 && b == 0) {
				break;
			}
			int k = scanner.nextInt();
			System.out.println(f.solve(a, b, k));
		}
		scanner.close();
	}
	
}
