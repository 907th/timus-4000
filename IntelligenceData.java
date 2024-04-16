import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class IntelligenceData {

	int d, n;
	String[] numbers = new String[128];
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		d = scanner.nextInt();
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			numbers[i] = scanner.next();
		}
		scanner.close();
	}
	
	boolean isOk(int x, String number) {
		BigDecimal h = BigDecimal.valueOf(x);
		BigDecimal b = new BigDecimal(number);
		BigDecimal a = b.multiply(BigDecimal.valueOf(x)).setScale(0, RoundingMode.HALF_UP);
		for(int i = 0; i <= 1; i++) {
			String y = a.add(BigDecimal.valueOf(i)).divide(h, MathContext.DECIMAL128).setScale(d, RoundingMode.HALF_UP).toString();
			if(y.equals(number)) return true;
		}
		return false;
	}
	
	void solve() {
		init();
		for(int i = 1; ; i++) {
			boolean found = true;
			for(int j = 0; j < n; j++) {
				if(!isOk(i, numbers[j])) {
					found = false;
					break;
				}
			}
			if(found) {
				System.out.println(i);
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		IntelligenceData id = new IntelligenceData();
		id.solve();
	}
	
}