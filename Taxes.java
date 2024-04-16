public class Taxes {

	private static final double eps = 1e-2;
	
	int L;
	
	int m = 0;
	double[] n = new double[128];
	int[] s = new int[128];
	
	int count = 0;
	double[] salary = new double[128];
	
	double Rd(double x) {
		return ((long)(x * 100 + 0.5)) / 100.0;
	}
	
	double C(double x) {
		return Rd(x * L / 100.0);
	}
	
	double T(double x) {
		double sumTaxes = 0;
		if(x <= n[0]) {
			sumTaxes = sumTaxes + (x * s[0]) / 100.0;
		} else {
			sumTaxes = sumTaxes + (n[0] * s[0]) / 100.0;
		}
		for(int i = 1; i < m; i++) {
			if(n[i - 1] > x) break;
			if(n[i - 1] <= x && x <= n[i]) {
				sumTaxes = sumTaxes + (x - n[i - 1]) * s[i] / 100.0; break;
			} else {
				sumTaxes = sumTaxes + (n[i] - n[i - 1]) * s[i] / 100.0;
			}
		}
		if(m > 0 && x > n[m - 1]) {
			sumTaxes = sumTaxes + (x - n[m - 1]) * s[m] / 100.0;
		}
		return Rd(sumTaxes);
	}
	
	double binSearch(double l, double r, double val) {
		while(r - l > 1e-4) {
			double mid = (r + l) / 2.0;
			double sum = (mid + C(mid));
			double taxes = T(mid) + T(C(mid));
			if(sum - taxes <= val - 1e-6) l = mid; else r = mid;
		}
		return r;
	}
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		L = scanner.nextInt();
		while(true) {
			double pn = scanner.nextDouble();
			int ps = scanner.nextInt();
			if(pn < eps) {
				s[m] = ps;
				break;
			} else {
				s[m] = ps;
				n[m] = pn;
				m++;
			}
		}
		while(true) {
			double pv = scanner.nextDouble();
			if(pv < 0) break;
			salary[count++] = pv;
		}
		scanner.close();
	}
	
	void solve() {
		double alreadyPaidTaxes = 0;
		double totalSum = 0;
		double taxesFromTotalSum = 0;
		for(int i = 0; i < count; i++) {
			double beforeTaxes = 0;
			double candidate = 0;
				
			for(int j = 0; j <= m; j++) {
				candidate = binSearch(j == 0 ? 0 : n[j - 1], j == m ? 250 * salary[i] : n[j], salary[i]);
				double clearSum = candidate + C(candidate) - T(candidate) - T(C(candidate));
				if(Math.abs(clearSum - salary[i]) < 0.01) {
					beforeTaxes = candidate;
				}
			}
			totalSum = totalSum + beforeTaxes;
			alreadyPaidTaxes = alreadyPaidTaxes + T(beforeTaxes) + T(C(beforeTaxes));
		}
		taxesFromTotalSum = T(totalSum) + T(C(totalSum));

		System.out.println(String.format("%.2f", (taxesFromTotalSum - alreadyPaidTaxes)).replace(',', '.'));
		
	}
	
	public static void main(String[] args) {
		Taxes t = new Taxes();
		t.init();
		t.solve();
	}
	
}
