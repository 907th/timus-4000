import java.math.BigInteger;

public class SumOfDegrees {

	int n;
	
	Matrix a = new Matrix();
	
	Fraction pow(Fraction b, int d) {
		return b.pow(d);
	}
	
	void fillRow(int rowIndex) {
		Fraction v = new Fraction(BigInteger.ZERO, BigInteger.ONE);
		for(int i = 1; i <= rowIndex + 1; i++) {
			v = v.add(pow(new Fraction(BigInteger.valueOf(i), BigInteger.ONE), n - 2));
		}
		for(int i = 0; i < n; i++) {
			a.m[rowIndex][i] = pow(new Fraction(BigInteger.valueOf(rowIndex + 1), BigInteger.ONE), i);
		}
		a.a[rowIndex] = v;
	}
	
	void initMatrix() {
		for(int i = 0; i < n; i++) {
			fillRow(i);
		}
	}
	
	void check(int x, int d, Fraction[] koeff) {
		Fraction xb = new Fraction(BigInteger.ZERO, BigInteger.ONE);
		for(int i = 0; i < n; i++) {
			xb = xb.add(pow(new Fraction(BigInteger.valueOf(x), BigInteger.ONE), i).mult(koeff[i]));
		}
		System.out.println(xb);
		
		BigInteger bf = BigInteger.ZERO;
		for(int i = 1; i <= x; i++) {
			bf = bf.add(BigInteger.valueOf(i).pow(d));
		}
		System.out.println(bf);
	}
	
	void solve(int n) {
		this.n = n + 2;
		a.n = this.n;
		initMatrix();
		//a.out();
		GaussSolver gs = new GaussSolver();
		Fraction[] h = gs.solve(a);
		//a.out();
		//check(63, this.n - 2, h);
		for(int i = h.length - 1; i >= 0; i--) {
			System.out.print(h[i]);
			if(i != 0) System.out.print(" ");
		}
	}

	void generatePrebuild() {
		for(int i = 0; i < 31; i++) {
			SumOfDegrees sod = new SumOfDegrees();
			System.out.print("preb[" + i + "] = \"");
			sod.solve(i);
			System.out.println("\";");
		}
	}
	
	String[] preb = new String[32];
	
	void init() {
		preb[0] = "1/1 0/1";
		preb[1] = "1/2 1/2 0/1";
		preb[2] = "1/3 1/2 1/6 0/1";
		preb[3] = "1/4 1/2 1/4 0/1 0/1";
		preb[4] = "1/5 1/2 1/3 0/1 -1/30 0/1";
		preb[5] = "1/6 1/2 5/12 0/1 -1/12 0/1 0/1";
		preb[6] = "1/7 1/2 1/2 0/1 -1/6 0/1 1/42 0/1";
		preb[7] = "1/8 1/2 7/12 0/1 -7/24 0/1 1/12 0/1 0/1";
		preb[8] = "1/9 1/2 2/3 0/1 -7/15 0/1 2/9 0/1 -1/30 0/1";
		preb[9] = "1/10 1/2 3/4 0/1 -7/10 0/1 1/2 0/1 -3/20 0/1 0/1";
		preb[10] = "1/11 1/2 5/6 0/1 -1/1 0/1 1/1 0/1 -1/2 0/1 5/66 0/1";
		preb[11] = "1/12 1/2 11/12 0/1 -11/8 0/1 11/6 0/1 -11/8 0/1 5/12 0/1 0/1";
		preb[12] = "1/13 1/2 1/1 0/1 -11/6 0/1 22/7 0/1 -33/10 0/1 5/3 0/1 -691/2730 0/1";
		preb[13] = "1/14 1/2 13/12 0/1 -143/60 0/1 143/28 0/1 -143/20 0/1 65/12 0/1 -691/420 0/1 0/1";
		preb[14] = "1/15 1/2 7/6 0/1 -91/30 0/1 143/18 0/1 -143/10 0/1 91/6 0/1 -691/90 0/1 7/6 0/1";
		preb[15] = "1/16 1/2 5/4 0/1 -91/24 0/1 143/12 0/1 -429/16 0/1 455/12 0/1 -691/24 0/1 35/4 0/1 0/1";
		preb[16] = "1/17 1/2 4/3 0/1 -14/3 0/1 52/3 0/1 -143/3 0/1 260/3 0/1 -1382/15 0/1 140/3 0/1 -3617/510 0/1";
		preb[17] = "1/18 1/2 17/12 0/1 -17/3 0/1 221/9 0/1 -2431/30 0/1 1105/6 0/1 -11747/45 0/1 595/3 0/1 -3617/60 0/1 0/1";
		preb[18] = "1/19 1/2 3/2 0/1 -34/5 0/1 34/1 0/1 -663/5 0/1 1105/3 0/1 -23494/35 0/1 714/1 0/1 -3617/10 0/1 43867/798 0/1";
		preb[19] = "1/20 1/2 19/12 0/1 -323/40 0/1 323/7 0/1 -4199/20 0/1 4199/6 0/1 -223193/140 0/1 2261/1 0/1 -68723/40 0/1 43867/84 0/1 0/1";
		preb[20] = "1/21 1/2 5/3 0/1 -19/2 0/1 1292/21 0/1 -323/1 0/1 41990/33 0/1 -223193/63 0/1 6460/1 0/1 -68723/10 0/1 219335/63 0/1 -174611/330 0/1";
		preb[21] = "1/22 1/2 7/4 0/1 -133/12 0/1 323/4 0/1 -969/2 0/1 146965/66 0/1 -223193/30 0/1 33915/2 0/1 -481061/20 0/1 219335/12 0/1 -1222277/220 0/1 0/1";
		preb[22] = "1/23 1/2 11/6 0/1 -77/6 0/1 209/2 0/1 -3553/5 0/1 11305/3 0/1 -223193/15 0/1 124355/3 0/1 -755953/10 0/1 482537/6 0/1 -1222277/30 0/1 854513/138 0/1";
		preb[23] = "1/24 1/2 23/12 0/1 -1771/120 0/1 4807/36 0/1 -81719/80 0/1 37145/6 0/1 -5133439/180 0/1 572033/6 0/1 -17386919/80 0/1 11098351/36 0/1 -28112371/120 0/1 854513/12 0/1 0/1";
		preb[24] = "1/25 1/2 2/1 0/1 -253/15 0/1 506/3 0/1 -14421/10 0/1 29716/3 0/1 -10266878/195 0/1 208012/1 0/1 -17386919/30 0/1 22196702/21 0/1 -28112371/25 0/1 1709026/3 0/1 -236364091/2730 0/1";
		preb[25] = "1/26 1/2 25/12 0/1 -115/6 0/1 1265/6 0/1 -24035/12 0/1 185725/12 0/1 -25667195/273 0/1 1300075/3 0/1 -17386919/12 0/1 277458775/84 0/1 -28112371/6 0/1 21362825/6 0/1 -1181820455/1092 0/1 0/1";
		preb[26] = "1/27 1/2 13/6 0/1 -65/3 0/1 16445/63 0/1 -16445/6 0/1 142025/6 0/1 -10266878/63 0/1 2600150/3 0/1 -20548177/6 0/1 3606964075/378 0/1 -52208689/3 0/1 55543345/3 0/1 -1181820455/126 0/1 8553103/6 0/1";
		preb[27] = "1/28 1/2 9/4 0/1 -195/8 0/1 4485/14 0/1 -29601/8 0/1 142025/4 0/1 -15400317/56 0/1 1671525/1 0/1 -61644531/8 0/1 721392815/28 0/1 -469878201/8 0/1 166630035/2 0/1 -3545461365/56 0/1 76977927/4 0/1 0/1";
		preb[28] = "1/29 1/2 7/3 0/1 -273/10 0/1 390/1 0/1 -9867/2 0/1 52325/1 0/1 -905901/2 0/1 3120180/1 0/1 -33193209/2 0/1 65581165/1 0/1 -365460823/2 0/1 333260070/1 0/1 -709092273/2 0/1 179615163/1 0/1 -23749461029/870 0/1";
		preb[29] = "1/30 1/2 29/12 0/1 -609/20 0/1 1885/4 0/1 -26013/4 0/1 303485/4 0/1 -8757043/12 0/1 22621305/4 0/1 -137514723/4 0/1 1901853785/12 0/1 -10598363867/20 0/1 4832271015/4 0/1 -6854558639/4 0/1 5208839727/4 0/1 -23749461029/60 0/1 0/1";
		preb[30] = "1/31 1/2 5/2 0/1 -203/6 0/1 1131/2 0/1 -16965/2 0/1 216775/2 0/1 -2304485/2 0/1 19959975/2 0/1 -137514723/2 0/1 731482225/2 0/1 -31795091601/22 0/1 8053785025/2 0/1 -102818379585/14 0/1 15626519181/2 0/1 -23749461029/6 0/1 8615841276005/14322 0/1";
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		SumOfDegrees sod = new SumOfDegrees();
		sod.init();
		System.out.println(sod.preb[n]);
	}
	
}

class Fraction {
	BigInteger nom = null;
	BigInteger den = null;

	public Fraction(BigInteger n, BigInteger d) {
		nom = n; den = d;
		normalize();
	}
	
	public Fraction add(Fraction a) {
		BigInteger nden = den.multiply(a.den);
		BigInteger nnom = nom.multiply(a.den).add(a.nom.multiply(den));
		BigInteger td = nden.gcd(nnom);
		return new Fraction(nnom.divide(td), nden.divide(td));
	}

	public Fraction mult(Fraction a) {
		return new Fraction(nom.multiply(a.nom), den.multiply(a.den));
	}

	public Fraction div(Fraction a) {
		return new Fraction(nom.multiply(a.den), den.multiply(a.nom));
	}

	public Fraction minus(Fraction a) {
		BigInteger nden = den.multiply(a.den);
		BigInteger nnom = nom.multiply(a.den).subtract(a.nom.multiply(den));
		BigInteger td = nden.gcd(nnom);
		return new Fraction(nnom.divide(td), nden.divide(td));
	}

	public Fraction pow(int d) {
		return new Fraction(nom.pow(d), den.pow(d));
	}

	@Override
	public String toString() {
		return nom.toString() + "/" + den.toString();
	}
	
	void normalize() {
		BigInteger td = nom.gcd(den);
		nom = nom.divide(td);
		den = den.divide(td);
	}
	
}

class Matrix {
	
	int n;
	
	Fraction m[][] = new Fraction[64][64];
	Fraction a[] = new Fraction[64];
	
	void out() {
		System.out.println();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println(a[i]);
		}
	}
	
}

class GaussSolver {
	
	boolean isZero(Fraction a) {
		return a.den.equals(BigInteger.ZERO);
	}
	
	Fraction[] solve(Matrix a) {
		
		int[] ids = new int[a.n];
		for(int i = 0; i < a.n; i++) {
			ids[i] = i;
		}
		
		for(int i = 0; i < a.n; i++) {
			if(isZero(a.m[i][i])) {
				boolean found = false;
				for(int j = i + 1; j < a.n; j++) {
					if(isZero(a.m[j][i])) {
						
						Fraction h = a.a[i];
						a.a[i] = a.a[j];
						a.a[j] = h;
						for(int k = 0; k < a.n; k++) {
							h = a.m[j][k];
							a.m[j][k] = a.m[i][k];
							a.m[i][k] = h;
						}
						found = true;
						break;
					}
				}
				if(!found) {
					for(int j = i + 1; j < a.n; j++) {
						if(isZero(a.m[i][j])) {
							int ih = ids[i];
							ids[i] = ids[j];
							ids[j] = ih;
							for(int k = 0; k < a.n; k++) {
								Fraction h = a.m[k][i];
								a.m[k][i] = a.m[k][j];
								a.m[k][j] = h;
							}
							break;
						}
					}
				}
			}
			
			if(isZero(a.m[i][i])) {
				System.out.println(i + "!!!!!!");
			}

			for(int j = i + 1; j < a.n; j++) {
				Fraction c = a.m[j][i].div(a.m[i][i]);
				for(int k = i; k < a.n; k++) {
					a.m[j][k] = a.m[j][k].minus(a.m[i][k].mult(c));
				}
				a.a[j] = a.a[j].minus(a.a[i].mult(c));
			}
		}
		
		Fraction[] ans = new Fraction[a.n];
		
		for(int i = a.n - 1; i >= 0; i--) {

			Fraction c = new Fraction(BigInteger.ZERO, BigInteger.ONE);
			for(int j = i + 1; j < a.n; j++) {
				c = c.add(a.m[i][j].mult(ans[j]));
			}
			
			ans[i] = a.a[i].minus(c).div(a.m[i][i]);
		}
		
		Fraction[] result = new Fraction[a.n];
		for(int i = 0; i < a.n; i++) {
			result[ids[i]] = ans[i];
		}
		
		return ans;
	}
	
}
