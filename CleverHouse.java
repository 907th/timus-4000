import java.math.BigInteger;

public class CleverHouse {

	void solve(int n, int m, int k) {
		if(k == 0) {
			System.out.printf("%d/1\n", m);
		} else
		if(n == 1) {
			if(k % 2 == 0) 
				System.out.printf("%d/1\n", m); else
				System.out.printf("%d/1\n", 1 - m);
		} else
		if(n == 2) {
			System.out.printf("%d/1\n", 1);
		} else {
			BigInteger nom0 = BigInteger.valueOf(n - 2).pow(k);
			BigInteger den0 = BigInteger.valueOf(n).pow(k);

			BigInteger nom1 = BigInteger.valueOf(2 * m - n);
			BigInteger den1 = BigInteger.valueOf(2);
			
			nom0 = nom0.multiply(nom1);
			den0 = den0.multiply(den1);

			BigInteger nom2 = BigInteger.valueOf(n).multiply(den0);
			BigInteger den2 = BigInteger.valueOf(2).multiply(den0);

			nom0 = nom0.multiply(BigInteger.valueOf(2));
			den0 = den0.multiply(BigInteger.valueOf(2));
			
			nom0 = nom0.add(nom2);
			
			BigInteger gcd = nom0.gcd(den0);
			
			nom0 = nom0.divide(gcd);
			den0 = den0.divide(gcd);
			
			System.out.println(nom0.toString() + "/" + den0.toString());
		}
	}
	
	public static void main(String[] args) {
		CleverHouse ch = new CleverHouse();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		int k = scanner.nextInt();
		scanner.close();
		ch.solve(n, m, k);
	}
	
}
