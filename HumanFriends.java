public class HumanFriends {

	private final static double eps = 1e-6;
	
	double checkCircle(double r, double x, double y) {
		
		double[][] p = GeometryUtils.findTouchingLineToCircle(0, 0, r, x, y);
		
		if(GeometryUtils.distance(0, 0, x, y) < r + eps) {
			
			double ya = -Math.sqrt(r * r - x * x);
			
			double h1 = GeometryUtils.getAngle(
					0, 0,
					-r, 0,
					x, ya);
			
			double vm = GeometryUtils.vectMultiplication(
					0, 0,
					-r, 0,
					x, ya);
			
			if(vm > 0) h1 = 2 * Math.PI - h1;
			h1 = h1 * r + (y - ya);
			
			double[][] possibleCenters = GeometryUtils.findPointsOnGivenDistancesFromTwoPoints(-2 * r, 0, 2 * r, x, y, r);
			int index = 0;
			if(possibleCenters[0][1] < possibleCenters[1][1]) index = 1;

			double[] pointsOfIntersection = new double[] {(possibleCenters[index][0] - 2 * r) / 2.0, possibleCenters[index][1] / 2.0};

			double h2 = GeometryUtils.getAngle(
				possibleCenters[index][0], possibleCenters[index][1],
				pointsOfIntersection[0], pointsOfIntersection[1],
				x, y);

			double mv = GeometryUtils.vectMultiplication(
				possibleCenters[index][0], possibleCenters[index][1],
				pointsOfIntersection[0], pointsOfIntersection[1],
				x, y);

			if(mv > 0) h2 = 2 * Math.PI - h2;
				
			h2 += GeometryUtils.getAngle(-2 * r, 0, -r, 0, possibleCenters[index][0], possibleCenters[index][1]);
	
			h2 = h2 * r;
				
			return Math.min(h1, h2);
		} else {
			int index = 0;
			
			double mv1 = GeometryUtils.vectMultiplication(p[0][0], p[0][1], x, y, -r, 0);
			
			if(mv1 < 0) index = 0; else index = 1;
			
			double l = GeometryUtils.distance(p[index][0], p[index][1], x, y);
			double h = GeometryUtils.getAngle(0, 0, -r, 0, p[index][0], p[index][1]);

			mv1 = GeometryUtils.vectMultiplication(0, 0, -r, 0, p[index][0], p[index][1]);
			if(mv1 > 0) h = 2 * Math.PI - h;

			return l + r * h; 
		}
	}
	
	double solve(double a, double r, double kx, double ky) {
		
		double[] rot = GeometryUtils.rotateVectorCounterClockwise(kx, ky, GeometryUtils.angleToRad(90 - a));
		kx = rot[0]; ky = rot[1];
		
		if(Math.abs(kx) < eps && ky > 0) {
			return Math.abs(ky);
		}
		
		double h1 = Math.abs(checkCircle(r,  kx - r, ky));
		double h2 = Math.abs(checkCircle(r, -kx - r, ky));
		
		return Math.min(h1, h2);
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		double x = scanner.nextDouble();
		double y = scanner.nextDouble();
		double a = scanner.nextDouble();
		double r = scanner.nextDouble();
		double kx = scanner.nextDouble();
		double ky = scanner.nextDouble();
		scanner.close();
		HumanFriends hf = new HumanFriends();
		double h = hf.solve(a, r, kx - x, ky - y);
		System.out.println(String.format("%f", h).replace(',', '.'));
	}
	
}

class GeometryUtils {

	private static final double eps = 1e-6;
	
	public static double Q(double x) {
		return x * x;
	}
	
	public static double vectMultiplication(double xs, double ys, double x1, double y1, double x2, double y2) {
		double vx1 = x1 - xs;
		double vy1 = y1 - ys;
		double vx2 = x2 - xs;
		double vy2 = y2 - ys;
		
		return vx1 * vy2 - vx2 * vy1;
	}
	
	public static double angleToRad(double x) {
		return x * Math.PI / 180.0;
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Q(x2 - x1) + Q(y2 - y1));
	}
	
	public static double[] rotateVectorCounterClockwise(double x, double y, double angle) {
		double nx = x * Math.cos(angle) - y * Math.sin(angle);
		double ny = x * Math.sin(angle) + y * Math.cos(angle);
		return new double[] {nx, ny};
	}
	
	public static double[][] findTouchingLineToCircle(double cx, double cy, double r, double px, double py) {
		double x1 = px - cx;
		double y1 = py - cy;
		
		double d1 = distance(cx, cy, px, py);
		
		if(Math.abs(y1) < eps) {
			double f = Q(x1) + 2 * Q(r) - Q(d1);
			double ax = f / 2 / x1;
			double ay1 =  Math.sqrt(Q(r) - Q(ax));
			double ay2 = -Math.sqrt(Q(r) - Q(ax));
			return new double[][] {{ax + cx, ay1 + cy}, {ax + cx, ay2 + cy}};
		}
		
		double c1 = Q(x1) + Q(y1) - Q(d1) + 2 * Q(r);
		double a1 = -x1 / y1;
		double b1 = c1 / 2 / y1;
		
		double a = 1 + Q(a1);
		double b = 2 * a1 * b1;
		double c = Q(b1) - Q(r);
		
		double disc = Q(b) - 4 * a * c;
		
		if(Math.abs(disc) < eps) disc = 0;
		if(disc >= 0) {
			disc = Math.sqrt(disc);
			double ax1 = (-b + disc) / 2 / a;
			double ay1 = (-2 * x1 * ax1 + c1) / 2 / y1;

			double ax2 = (-b - disc) / 2 / a;
			double ay2 = (-2 * x1 * ax2 + c1) / 2 / y1;
			
			return new double[][] {{ax1 + cx, ay1 + cy}, {ax2 + cx, ay2 + cy}};
		}
		
		return null;
	}
	
	public static double getAngle(double cx, double cy, double x1, double y1, double x2, double y2) {
		double d = distance(x1, y1, x2, y2);
		double d1 = distance(x1, y1, cx, cy);
		double d2 = distance(cx, cy, x2, y2);
				
		double cosa = (Q(d) - Q(d1) - Q(d2)) / (-2 * d1 * d2);
			
		return Math.acos(cosa);
	}
	
	public static double[][] findPointsOnGivenDistancesFromTwoPoints(double x1, double y1, double r1, double x2, double y2, double r2) {
		double c1 = 2 * x1 - 2 * x2;
		double c2 = 2 * y1 - 2 * y2;
		double c3 = Q(r2) - Q(x2) - Q(y2) - Q(r1) + Q(x1) + Q(y1);
		
		if(Math.abs(c2) < eps) {
			double ax = c3 / c1;
			double ay1 =  Math.sqrt(Q(r1) - Q(ax - x1)) + y1;
			double ay2 = -Math.sqrt(Q(r1) - Q(ax - x1)) + y1;
			return new double[][] {{ax, ay1}, {ax, ay2}};
		}
		
		double a = 1 + (Q(c1 / c2));
		double b = -2 * x1 - 2 * (c3 / c2 - y1) * c1 / c2;
		double c = Q(x1) + Q(c3 / c2 - y1) - Q(r1);
		
		double disc = Q(b) - 4 * a * c;
		if(Math.abs(disc) < eps) disc = 0;
		if(disc < 0) return null;
		double ax1 = (-b + Math.sqrt(disc)) / 2 / a;
		double ay1 = c3 / c2 - c1 / c2 * ax1;
		
		double ax2 = (-b - Math.sqrt(disc)) / 2 / a;
		double ay2 = c3 / c2 - c1 / c2 * ax2;
		
		return new double[][] {{ax1, ay1}, {ax2, ay2}};
	}
	
	public static void testTouchingLines(double cx, double cy, double cr, double px, double py) {
		
		double[][] points = findTouchingLineToCircle(cx, cy, cr, px, py);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"circles\":[");
		sb.append("{")
			.append("\"x\":").append(cx).append(",")
			.append("\"y\":").append(cy).append(",")
			.append("\"r\":").append(cr)
			.append("}").append("],");
		sb.append("\"lines\":[");
		for(int i = 0; i < points.length; i++) {
			if(i > 0) sb.append(",");
			sb.append("{")
			.append("\"x1\":").append(points[i][0]).append(",")
			.append("\"y1\":").append(points[i][1]).append(",")
			.append("\"x2\":").append(px).append(",")
			.append("\"y2\":").append(py)
			.append("}");
		}
		sb.append("]");
		sb.append("}");
		System.out.println(sb);
	}
	
	public static void testFindPointsOnGivenDistancesFromTwoPointsLines(double x1, double y1, double r1, double x2, double y2, double r2) {
		
		double[][] points = findPointsOnGivenDistancesFromTwoPoints(x1, y1, r1, x2, y2, r2);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"circles\":[");
		sb.append("{")
			.append("\"x\":").append(x1).append(",")
			.append("\"y\":").append(y1).append(",")
			.append("\"r\":").append(r1)
			.append("},");
		sb.append("{")
			.append("\"x\":").append(x2).append(",")
			.append("\"y\":").append(y2).append(",")
			.append("\"r\":").append(r2)
			.append("},");
		sb.append("{")
			.append("\"x\":").append(points[0][0]).append(",")
			.append("\"y\":").append(points[0][1]).append(",")
			.append("\"r\":").append(0.3)
			.append("},");
		sb.append("{")
			.append("\"x\":").append(points[1][0]).append(",")
			.append("\"y\":").append(points[1][1]).append(",")
			.append("\"r\":").append(0.3)
			.append("}");
		sb.append("],");
		sb.append("\"lines\":[]");
		sb.append("}");
		System.out.println(sb);
	}
	
}