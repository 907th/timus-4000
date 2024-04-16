import java.math.BigInteger;

public class ZonesOnAPlane {

	BigInteger[] a0 = new BigInteger[2048];
	BigInteger[] a90 = new BigInteger[2048];
	
	public String getCount(int n) {
		if(n == 1) return "1";
		if(n == 2) return "4";
		if(n == 3) return "8";
		if(n == 4) return "12";
		BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
		BigInteger four = two.add(two);
		a0[4] = BigInteger.ONE;
		a90[4] = BigInteger.ONE.add(BigInteger.ONE);
		
		for(int i = 5; i <= n; i++) {
			a0[i] = a90[i - 1].subtract(BigInteger.ONE);
			a90[i] = a0[i - 1].multiply(two).add(two);
		}
		return a0[n].add(a90[n]).multiply(four).toString();
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int h = scanner.nextInt();
		scanner.close();
		ZonesOnAPlane zoap = new ZonesOnAPlane();
		String answer = zoap.getCount(h);
		System.out.println(answer);
	}
	
}
