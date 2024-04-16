import java.math.BigDecimal;

public class Polynomial {

    double F(BigDecimal[] a, double x) {
        BigDecimal v = BigDecimal.ZERO;
        for(int i = 0; i < a.length; i++) {
        	v = v.add(BigDecimal.valueOf(x).pow(a.length - i - 1).multiply(a[i]));
        }
        return v.doubleValue();
    }

    Double binSearch(double l, double r, double[] a) {
    	BigDecimal[] abd = new BigDecimal[a.length];
    	for(int i = 0; i < a.length; i++) {
    		abd[i] = BigDecimal.valueOf(a[i]);
    	}
        double vl = F(abd, l);
        double vr = F(abd, r);
        if(vl > 1e-6 && vr > 1e-6) return null;
        if(vl < -1e-6 && vr < -1e-6) return null;
        boolean isDecrease = false;
        if(vl > vr) {
            isDecrease = true;
        }
        while(r - l > 1e-9) {
            double m = (r + l) / 2.0;
            double v = F(abd, m);
            if(isDecrease) {
                if(v > 0) {
                    l = m;
                } else {
                    r = m;
                }
            } else {
                if(v > 0) {
                    r = m;
                } else {
                    l = m;
                }
            }
        }
        return l;
    }

    double[] filter(double[] a) {
    	double[] t = new double[16];
    	int tSize = 0;
    	for(int i = 0; i < a.length; i++) {
    		boolean found = false;
    		for(int j = 0; j < i; j++) {
    			if(Math.abs(a[i] - a[j]) < 1e-9) {
    				found = true;
    				break;
    			}
    		}
    		if(!found) {
    			t[tSize++] = a[i];
    		}
    	}
    	double[] ans = new double[tSize];
    	for(int i = 0; i < tSize; i++) {
    		ans[i] = t[i];
    	}
    	return ans;
    }
    
    double[] solve(double[] a, int isStart) {
        if(a.length == 2) {
            return new double[] {-a[1] / (a[0] + 0.0)};
        } else {
            double[] b = new double[a.length - 1];
            for(int i = 0; i < b.length; i++) {
                b[i] = a[i] * (a.length - i - 1);
            }
            double[] points = solve(b, 0);

            int rootsCount = 0;
            double[] roots = new double[32];

            for(int i = 0; i <= points.length; i++) {
                double l = -1e6;
                if(i > 0) {
                    l = points[i - 1];
                }
                double r = 1e6;
                if(i < points.length) {
                    r = points[i];
                }
                Double x = binSearch(l, r, a);
                if(x != null) {
                    roots[rootsCount++] = x;
                }
            }
            double[] answer = new double[rootsCount];
            for(int i = 0; i < rootsCount; i++) {
                answer[i] = roots[i];
            }
            return answer;
        }
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int n = scanner.nextInt();
        double[] a = new double[n + 1];
        for(int i = 0; i <= n; i++) {
            a[i] = scanner.nextInt();
        }
        scanner.close();
        Polynomial p = new Polynomial();
        double[] d = p.solve(a, 1);
        for(int i = 0; i < d.length; i++) {
        	if(Math.abs(d[i]) < 1e-9) d[i] = 0;
            System.out.println(d[i]);
        }
    }

}
