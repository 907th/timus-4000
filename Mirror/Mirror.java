import java.math.BigDecimal;
import java.math.BigInteger;

class Fraction {
	
	BigInteger num;
	BigInteger denom;
	
	public Fraction(BigInteger num, BigInteger denom) {
		this.num = num;
		this.denom = denom;
		this.normalize();
	}
	
	public Fraction(String s) {
		BigDecimal million = BigDecimal.TEN.pow(6);
		num = new BigDecimal(s).multiply(million).toBigInteger();
		denom = BigInteger.ONE;
	}
	
	static Fraction ZERO = new Fraction(BigInteger.ZERO, BigInteger.ONE);
	
	public Fraction subtract(Fraction f) {
		this.normalize(); f.normalize();
		if(f.num.compareTo(BigInteger.ZERO) == 0) return this;
		if(this.num.compareTo(BigInteger.ZERO) == 0) return new Fraction(BigInteger.ZERO.subtract(f.num), f.denom);
		BigInteger newDenom = this.denom.multiply(f.denom);
		BigInteger newNum = this.num.multiply(f.denom).subtract(f.num.multiply(this.denom));
		return new Fraction(newNum, newDenom);
	}
	
	public Fraction add(Fraction f) {
		this.normalize(); f.normalize();
		if(f.num.compareTo(BigInteger.ZERO) == 0) return this;
		if(this.num.compareTo(BigInteger.ZERO) == 0) return f;
		BigInteger newDenom = this.denom.multiply(f.denom);
		BigInteger newNum = this.num.multiply(f.denom).add(f.num.multiply(this.denom));
		return new Fraction(newNum, newDenom);
	}
	
	public Fraction multiply(Fraction f) {
		this.normalize(); f.normalize();
		if(f.num.compareTo(BigInteger.ZERO) == 0) return new Fraction(BigInteger.ZERO, BigInteger.ONE);
		if(this.num.compareTo(BigInteger.ZERO) == 0) return new Fraction(BigInteger.ZERO, BigInteger.ONE);
		BigInteger newDenom = this.denom.multiply(f.denom);
		BigInteger newNum = this.num.multiply(f.num);
		return new Fraction(newNum, newDenom);
	}
	
	public Fraction divide(Fraction f) {
		this.normalize(); f.normalize();
		if(f.num.compareTo(BigInteger.ZERO) == 0) {
			throw new RuntimeException("Division by zero");
		}
		if(this.num.compareTo(BigInteger.ZERO) == 0) return new Fraction(BigInteger.ZERO, BigInteger.ONE);
		BigInteger newDenom = this.denom.multiply(f.num);
		BigInteger newNum = this.num.multiply(f.denom);
		return new Fraction(newNum, newDenom);
	}

	public double doubleValue() {
		this.normalize();
		return (num.doubleValue() / denom.doubleValue()) / 1000000.0;
	}
	
	public void normalize() {
		if(num.compareTo(BigInteger.ZERO) == 0) {
			denom = BigInteger.ONE;
		}
		if(denom.compareTo(BigInteger.ZERO) < 0) {
			denom = denom.abs();
			num = BigInteger.ZERO.subtract(num);
		}
	}
	
	public int compareTo(Fraction f) {
		this.normalize(); f.normalize();
		BigInteger newNum1 = this.num.multiply(f.denom);
		BigInteger newNum2 = f.num.multiply(this.denom);
		return newNum1.compareTo(newNum2);
	}

	public Fraction abs() {
		return new Fraction(num.abs(), denom.abs());
	}
	
	@Override
	public String toString() {
		return doubleValue() + "";
	}
	
}

public class Mirror {

	Fraction x1, y1, x2, y2, xm1, ym1, xm2, ym2;
	
	Fraction A, B, C;
	Fraction A1, B1, C1;
	Fraction A2, B2, C2;

	Fraction xc1, yc1, xc2, yc2;

	void init(Fraction x1, Fraction y1,
			Fraction x2, Fraction y2,
			Fraction xm1, Fraction ym1,
			Fraction xm2, Fraction ym2
			) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		this.xm1 = xm1;
		this.ym1 = ym1;
		this.xm2 = xm2;
		this.ym2 = ym2;
	}
	
	boolean solve(boolean debug, Fraction x1, Fraction y1,
			Fraction x2, Fraction y2,
			Fraction xm1, Fraction ym1,
			Fraction xm2, Fraction ym2
			) {
		A = ym2.subtract(ym1);
		B = xm1.subtract(xm2);
		C = Fraction.ZERO.subtract(xm1).multiply(ym2.subtract(ym1))
				.add(ym1.multiply(xm2.subtract(xm1)));
		
		Fraction AA = y2.subtract(y1);
		Fraction BB = x1.subtract(x2);
		Fraction CC = Fraction.ZERO.subtract(x1).multiply(y2.subtract(y1))
				.add(ym1.multiply(x2.subtract(x1)));
		
		Fraction fv1 = A.multiply(x1).add(B.multiply(y1)).add(C);
		Fraction fv2 = A.multiply(x2).add(B.multiply(y2)).add(C);
		
		if(fv1.multiply(fv2).compareTo(Fraction.ZERO) < 0) {
			return false;
		}
		
		A1 = Fraction.ZERO.subtract(B);
		B1 = A;
		C1 = Fraction.ZERO.subtract(A1.multiply(x1).add(B1.multiply(y1)));
		
		A2 = Fraction.ZERO.subtract(B);
		B2 = A;
		C2 = Fraction.ZERO.subtract(A2.multiply(x2).add(B2.multiply(y2)));
		
		
		if(B.compareTo(Fraction.ZERO) == 0) {
			xc1 = xm1;
			yc1 = y1;
			xc2 = xm1;
			yc2 = y2;
		} else {
			
			xc1 = B1.multiply(C).subtract(C1.multiply(B)).divide(
					A1.multiply(B).subtract(A.multiply(B1)));
			
			yc1 = Fraction.ZERO.subtract(C).subtract(A.multiply(xc1)).divide(B);
		
			xc2 = B2.multiply(C).subtract(C2.multiply(B)).divide(
					A2.multiply(B).subtract(A.multiply(B2)));
		
			yc2 = Fraction.ZERO.subtract(C).subtract(A.multiply(xc2)).divide(B);
		}
		
		Fraction k1 = A.multiply(x1).add(B.multiply(y1)).add(C).abs();
		Fraction k2 = A.multiply(x2).add(B.multiply(y2)).add(C).abs();
		
		Fraction k = k2.add(k1);
		
		Fraction xv = xc1.add(xc2.subtract(xc1).multiply(k1).divide(k));
		Fraction yv = yc1.add(yc2.subtract(yc1).multiply(k1).divide(k));
		
		if(debug) {
			System.out.println("{\"lines\":[");
			System.out.println("{");
			System.out.println("\"x1\": " + xm1.doubleValue() + ",");
			System.out.println("\"y1\": " + ym1.doubleValue() + ",");
			System.out.println("\"x2\": " + xm2.doubleValue() + ",");
			System.out.println("\"y2\": " + ym2.doubleValue());
			System.out.println("},");
			System.out.println("{");
			System.out.println("\"x1\": " + x1.doubleValue() + ",");
			System.out.println("\"y1\": " + y1.doubleValue() + ",");
			System.out.println("\"x2\": " + xc1.doubleValue() + ",");
			System.out.println("\"y2\": " + yc1.doubleValue());
			System.out.println("},");
			System.out.println("{");
			System.out.println("\"x1\": " + x2.doubleValue() + ",");
			System.out.println("\"y1\": " + y2.doubleValue() + ",");
			System.out.println("\"x2\": " + xc2.doubleValue() + ",");
			System.out.println("\"y2\": " + yc2.doubleValue());
			System.out.println("},");
			System.out.println("{");
			System.out.println("\"x1\": " + x1.doubleValue() + ",");
			System.out.println("\"y1\": " + y1.doubleValue() + ",");
			System.out.println("\"x2\": " + xv.doubleValue() + ",");
			System.out.println("\"y2\": " + yv.doubleValue());
			System.out.println("},");
			System.out.println("{");
			System.out.println("\"x1\": " + x2.doubleValue() + ",");
			System.out.println("\"y1\": " + y2.doubleValue() + ",");
			System.out.println("\"x2\": " + xv.doubleValue() + ",");
			System.out.println("\"y2\": " + yv.doubleValue());
			System.out.println("}");
			System.out.println("]}");
		}
		
		if( (xm1.compareTo(xv) <= 0 && xv.compareTo(xm2) <= 0 || xm2.compareTo(xv) <= 0 && xv.compareTo(xm1) <= 0) && 
			(ym1.compareTo(yv) <= 0 && yv.compareTo(ym2) <= 0 || ym2.compareTo(yv) <= 0 && yv.compareTo(ym1) <= 0)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static void main(String[] args) {
		Mirror m = new Mirror();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		Fraction x1 = new Fraction(scanner.next());
		Fraction y1 = new Fraction(scanner.next());
		Fraction x2 = new Fraction(scanner.next());
		Fraction y2 = new Fraction(scanner.next());
		Fraction xm1 = new Fraction(scanner.next());
		Fraction ym1 = new Fraction(scanner.next());
		Fraction xm2 = new Fraction(scanner.next());
		Fraction ym2 = new Fraction(scanner.next());
		scanner.close();
		boolean h = 
				m.solve(false, x1, y1, x2, y2, xm1, ym1, xm2, ym2) || 
				m.solve(false, x1, y1, x2, y2, xm2, ym2, xm1, ym1)
				;
		if(h) {
			System.out.println("VISIBLE");
		} else {
			System.out.println("INVISIBLE");
		}
	}
	
}