public class AsteroidLanding {

	private static double eps = 1e-7;
	
	double currentX, currentY, currentZ;
	double d, h;
	
	double Q(double x) {
		return x * x;
	}
	
	double D2(double x1, double z1, double x2, double z2) {
		return Q(x1 - x2) + Q(z1 - z2);
	}
	
	double D(double x1, double z1, double x2, double z2) {
		return Math.sqrt(D2(x1, z1, x2, z2));
	}
	
	double surfaceBinarySearch(double l, double r) {
		while(r - l > eps) {
			double m1 = l + (r - l) / 3.0; 
			double m2 = r - (r - l) / 3.0;

			double v1 = Math.abs(D2(currentX, currentZ, m1, 0) - Q(d));
			double v2 = Math.abs(D2(currentX, currentZ, m2, 0) - Q(d));
			
			if(v1 > v2) l = m1; else r = m2;
		}
		return (r + l) / 2.0;
	}
	
	double[] binarySearch(double lx, double lz, double rx, double rz) {
		
		while(D2(lx, lz, rx, rz) > eps) {
			double m1x = lx + (rx - lx) / 3.0;
			double m2x = rx - (rx - lx) / 3.0;

			double m1z = lz + (rz - lz) / 3.0;
			double m2z = rz - (rz - lz) / 3.0;
			
			double v1 = Math.abs(D2(m1x, m1z, currentX, currentZ) - Q(d));
			double v2 = Math.abs(D2(m2x, m2z, currentX, currentZ) - Q(d));
			
			if(v1 > v2) {
				lx = m1x;
				lz = m1z;
			} else {
				rx = m2x;
				rz = m2z;
			}
			
		}
		
		return new double[] {(lx + rx) / 2.0, (lz + rz) / 2.0};
	}
	
	double[][] solve(double h, double d, double alpha) {
		
		this.h = h; this.d = d;
		
		double t = (h / Math.cos(alpha / 2.0));
		double halfBase = t * Math.sin(alpha / 2.0);
		
		int intH = (int)(h * 10000 + 0.5);
		int intD = (int)(d * 10000 + 0.5);
		int count = intH / intD;
		if(intH % intD != 0) count++;
		double[][] response = new double[count][3];
		
		currentX = 0;
		currentY = 0;
		currentZ = h;
		
		for(int i = 0; i < count; i++) {
			if(i == count - 1) {
				double x = surfaceBinarySearch(-halfBase, halfBase);
				if(Math.abs(D(currentX, currentZ, x, 0) - d) > 1e-4) {
					//if(alpha == 1 && h == 0.85 && d == 1) while(true);
					return null;
				}
				response[i][0] = x; response[i][1] = 0; response[i][2] = 0;
			} else {
				double stepsLeft = count - i;
				if(Math.abs(stepsLeft * d - currentZ) < eps) {
					currentZ = (stepsLeft - 1) * d;
				} else {
					double potentialX = currentX;
					double potentialZ = currentZ;
					double currentT = (h / Math.cos(alpha / 2.0));
					double currentBase = 2 * currentT * Math.sin(alpha / 2.0);
					if(currentBase + eps > d) {
						if(currentX > 0) {
							double[] c = binarySearch(currentX, currentZ, halfBase, 0);
							potentialX = c[0];
							potentialZ = c[1];
						} else {
							double[] c = binarySearch(currentX, currentZ, -halfBase, 0);
							potentialX = c[0];
							potentialZ = c[1];
						}
					} else {
						if(currentX > 0) {
							double[] c = binarySearch(-currentX, currentZ, -halfBase, 0);
							potentialX = c[0];
							potentialZ = c[1];
						} else {
							double[] c = binarySearch(-currentX, currentZ,  halfBase, 0);
							potentialX = c[0];
							potentialZ = c[1];
						}
					}
					if(potentialZ + eps > (count - i - 1) * d) {
						potentialZ = (count - i - 1) * d;
						double differenceZ = currentZ - potentialZ;
						double differenceX = Math.sqrt(Q(d) - Q(differenceZ));
						if(currentX > 0) 
							potentialX = currentX + differenceX; else 
							potentialX = currentX - differenceX;
					}
					currentX = potentialX;
					currentZ = potentialZ;
				}
				response[i][0] = currentX;
				response[i][2] = currentZ;
			}
		}
		
		return response;
	}
	
	public static String toDouble(double d) {
		return Double.toString(d).replace(',', '.');
	}
	
	public static void main(String[] args) {
		boolean needOutJson = false;
		AsteroidLanding al = new AsteroidLanding();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		double h = Double.parseDouble(scanner.next());
		double d = Double.parseDouble(scanner.next());
		double alpha = Double.parseDouble(scanner.next());
		scanner.close();
		double[][] response = al.solve(h, d, alpha);
		if(response == null) {
			System.out.print(-1);
		} else {
			
			double halfBase = (h * Math.tan(alpha / 2.0));
			
			if(needOutJson) {
				System.out.println("{\"lines\": [");
				System.out.println("{");
				System.out.printf("\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s, \"color\": \"#FF0000\"", toDouble(-halfBase), toDouble(0), toDouble(0), toDouble(h));
				System.out.println("}");
				System.out.println(",{");
				System.out.printf("\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s, \"color\": \"#FF0000\"", toDouble(0), toDouble(h), toDouble(halfBase), toDouble(0));
				System.out.println("}");
				System.out.println(",{");
				System.out.printf("\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s, \"color\": \"#FF0000\"", toDouble(-halfBase), toDouble(0), toDouble(halfBase), toDouble(0));
				System.out.println("}");
				System.out.println(",{");
				System.out.printf("\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s, \"color\": \"#0000FF\"", toDouble(0), toDouble(h), toDouble(response[0][0]), toDouble(response[0][2]));
				System.out.println("}");
				for(int i = 1; i < response.length; i++) {
					System.out.println(",{");
					System.out.printf("\"x1\": %s, \"y1\": %s, \"x2\": %s, \"y2\": %s, \"color\": \"#0000FF\"", toDouble(response[i - 1][0]), toDouble(response[i - 1][2]), toDouble(response[i][0]), toDouble(response[i][2]));
					System.out.println("}");
				}
				System.out.println("]}");
			}
			
			System.out.println(response.length);
			for(int i = 0; i < response.length; i++) {
				if(i > 0) {
					if(response[i][2] >= response[i - 1][2]) while(true);
				}
				System.out.println(response[i][0] + " " + response[i][1] + " " + response[i][2]);
			}
		}
	}
	
}


//11 5 2
//11 5 1
//21 5 1
//21 5 0.5
//21 6 0.5
//120 16 0.5
//5 6 1.2
//5 4 1.2
//5 4.5 1.2
//99.9999 5.0000 0.1000
//19 18 2
//19 28 2
//19 20 1
//19 4 0.2
//21.5 3.8 0.1

//5 4.9 1.0472
//4 5 1.287	
//0.85 1 1