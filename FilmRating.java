import java.math.BigDecimal;
import java.math.RoundingMode;

public class FilmRating {
	
	static BigDecimal getCurrentRating(long i, BigDecimal total, BigDecimal n, int scale) {
		BigDecimal bi = BigDecimal.valueOf(i);
		return total.add(bi).divide(n.add(bi), scale, RoundingMode.HALF_UP);
	}

	public static void main(String[] args) {
		BigDecimal n;
		BigDecimal x;
		BigDecimal y;
		
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		String xs = scanner.next();
		x = new BigDecimal(xs);

		y = new BigDecimal(scanner.next());
		n = new BigDecimal(scanner.next());
		scanner.close();
		
		BigDecimal total = x.multiply(n);
		
		for(long tl = n.longValue() * 10; tl >= 1; tl--) {
			BigDecimal tlb = BigDecimal.valueOf(tl);
			if(tlb.divide(n, 1, RoundingMode.HALF_UP).equals(x)) {
				total = tlb; break;
			}
		}
		
		long l = 0;
		long r = 1000000000;
		
		while(r - l > 1) {
			long m = (l + r) / 2;
			BigDecimal currentRating = getCurrentRating(m, total, n, 1);
			if(currentRating.compareTo(y) <= 0)
			{
				r = m;
			} else {
				l = m;
			}
		}
		
		for(long i = Math.max(l - 10, 0); ; i++)
		{
			BigDecimal currentRating = getCurrentRating(i, total, n, 1);
			if(currentRating.compareTo(y) <= 0)
			{
				System.out.println(i); break;
			}
		}
		
	}
	
}