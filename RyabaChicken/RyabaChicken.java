import java.math.BigDecimal;
import java.math.BigInteger;

public class RyabaChicken {

	public static BigInteger two = BigInteger.valueOf(2);
	public static BigInteger four = BigInteger.valueOf(4);
	
	BigDecimal Q(BigDecimal x) {
		return x.multiply(x);
	}
	
	BigInteger Q(BigInteger x) {
		return x.multiply(x);
	}
	
	String solveBS(BigInteger H, BigInteger l, BigInteger h) {
		BigInteger left = BigInteger.ZERO;
		BigInteger right = H.multiply(l).add(left);
		while(right.subtract(left).compareTo(BigInteger.ONE) > 0) {
			BigInteger m = left.add(right).divide(two);

			BigInteger leftPart = four
					.multiply(m)
					.multiply(m.add(BigInteger.ONE))
					.multiply(h)
					.multiply(H);
			
			BigInteger rightPart = Q(l).add(Q(H));
			if(leftPart.compareTo(rightPart) <= 0) {
				left = m;
			} else {
				right = m;
			}
		}
		return right.toString();
	}
	
	public static void main(String[] args) {
		RyabaChicken rc = new RyabaChicken();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			String H = scanner.next();
			String l = scanner.next();
			String h = scanner.next();
			String ans = rc.solveBS(new BigInteger(H), new BigInteger(l), new BigInteger(h));
			System.out.println(ans);
		}
		scanner.close();
	}
	
}
