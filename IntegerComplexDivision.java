public class IntegerComplexDivision {

	private final double eps = 1e-9;
	
	long nextInteger(double d) {
		if(d <= 0) {
			return (long)d;
		} else {
			double part = d - ((long)d);
			if(Math.abs(part) < eps) return (long)d;
			return (long)d + 1;
		}
	}
	
	long prevInteger(double d) {
		if(d >= 0) {
			return (long)d;
		} else {
			double part = d - ((long)d);
			if(Math.abs(part) < eps) return (long)d;
			return (long)d - 1;
		}
	}
	
	double Q(double x) {
		return x * x;
	}
	
	long getCount(long q1, long a1, long a2, long b1, long b2) {
		double c1 = a1 - b1 * q1;
		double c2 = a2 - b2 * q1;
		
		double a = b1 * b1 + b2 * b2;
		double b = 2 * c1 * b2 - 2 * c2 * b1;
		double c = c1 * c1 + c2 * c2 - b1 * b1 - b2 * b2;
		
		double d = b * b - 4 * a * c;
		if(Math.abs(d) < eps) return 0;
		if(d <= 0) return 0;
		d = Math.sqrt(d);
		
		double x1 = (-b - d) / 2.0 / a; 
		double x2 = (-b + d) / 2.0 / a;
		
		if(x1 > x2) {
			double h = x1;
			x1 = x2;
			x2 = h;
		}
		
		long n1 = nextInteger(x1);
		long n2 = prevInteger(x2);

		if(n2 < n1) {
			long count = 0;
			for(long i = n2 - 10; i <= n1 + 10; i++) {
				double v = a * n1 * n1 + b * n1 + c;
				if(v < -eps) count++;
			}
			return count;
		}
		
		double v1 = a * n1 * n1 + b * n1 + c;
		double v2 = a * n2 * n2 + b * n2 + c;
		
		
		if(n2 == n1) {
			double v3 = a * n2 * n2 + b * n2 + c;
			if(v3 >= -eps) return 0;
			return 1;
		}
		
		long count = n2 - n1 + 1;
		
		if(v1 >= -eps) count--;
		if(v2 >= -eps) count--;
		
		return count;
	}
	
	long solve(int a1, int a2, int b1, int b2) {
		long answer = 0;
		int limit = 1000000;
		for(int q1 = -limit; q1 <= limit; q1++) {
			answer += getCount(q1, a1, a2, b1, b2);
		}
		return answer;
	}
	
	public static void main(String[] args) {
		int a1, a2;
		int b1, b2;
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		a1 = scanner.nextInt();
		a2 = scanner.nextInt();
		b1 = scanner.nextInt();
		b2 = scanner.nextInt();
		scanner.close();
		IntegerComplexDivision icd = new IntegerComplexDivision();
		long h = icd.solve(a1, a2, b1, b2);
		System.out.println(h);
	}
	
}