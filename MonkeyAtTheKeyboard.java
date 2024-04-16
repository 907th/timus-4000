import java.math.BigInteger;

public class MonkeyAtTheKeyboard {
	
	//M(x) = expected count of steps left to reach the goal
	//M(i) = (M(j) + 1) * P(j) + (M(i + 1) + 1) * P(i + 1), where j < i, SUM(P(j)) + P(i + 1) = 1
	//M(i) - 1 = SUM(M(j) * P(j)) + M(i + 1) * P(i + 1)
	//M(i + 1) = (M(i) - 1 - SUM(M(j) * P(j))) / P(i + 1)
	
	int n;
	String s;
	
	BigInteger[][] m = new BigInteger[2][30001];
	
	int[] f = new int[30001];
	
	int[][] step = new int[27][30001];
	int[][] inds = new int[26][];
	int[] indSize = new int[26];
	int[] lettersCount = new int[26];
	
	boolean isOk(int prefixLength, int index) {
		int c = 0;
		for(int i = index - 1; i >= 0; i--) {
			if(s.charAt(i) != s.charAt(prefixLength - c - 1)) return false;
			c++;
		}
		return true;
	}
	
	int getLength(int prefixLength, char newLetter) {
		
		if(s.charAt(prefixLength) == newLetter) return prefixLength + 1;

		int g = newLetter - 'a';
		
		for(int i = indSize[g] - 1; i >= 0; i--) {
			if(inds[g][i] >= prefixLength) continue;
			
			if(isOk(prefixLength, inds[g][i])) return inds[g][i] + 1;
			
		}
		
		return 0;
	}
	
	public void solve(int n, String goal) {

		this.s = goal;
		this.n = n;
		
		for(int i = 0; i < goal.length(); i++) {
			int x = goal.charAt(i) - 'a';
			lettersCount[x]++;
		}
		
		for(int i = 0; i < n; i++) {
			inds[i] = new int[lettersCount[i]];
		}
		
		for(int i = 0; i < goal.length(); i++) {
			int x = goal.charAt(i) - 'a';
			inds[x][indSize[x]++] = i;
		}
		
		BigInteger ts = BigInteger.valueOf(n);
		
		m[0][0] = BigInteger.ONE;
		m[1][0] = BigInteger.ZERO;

		for(int i = 1; i <= goal.length(); i++) {
			for(int j = 1; j <= n; j++) {
				int x = getLength(i - 1, (char)('a' + j - 1));
				step[j][i] = x;
				f[x] = i;
			}
		}
		
		
		for(int i = 1; i <= goal.length(); i++) {
			m[0][i] = m[0][i - 1].multiply(ts);
			m[1][i] = m[1][i - 1].subtract(BigInteger.ONE).multiply(ts);
			
			int zerosCount = 0;
			
			for(int j = 1; j <= n; j++) {
				int x = step[j][i];
				
				if(x == 0) {
					zerosCount++;
				} else if(x < i) {
					m[0][i] = m[0][i].subtract( m[0][x] );
					m[1][i] = m[1][i].subtract( m[1][x] );
				}
				
				if(f[x] == i && x != 0 && x < i - 1) {
					m[0][x] = m[1][x] = null;
				}
			}

			m[0][i] = m[0][i].subtract( m[0][0].multiply(BigInteger.valueOf(zerosCount)) );
			m[1][i] = m[1][i].subtract( m[1][0].multiply(BigInteger.valueOf(zerosCount)) );

			if(f[i - 1] < i && i != 1) {
				m[0][i - 1] = m[1][i - 1] = null;
			}
			
		}
		
		int g = goal.length();
		
		BigInteger ans = m[1][g].multiply(BigInteger.valueOf(-1)).divide(m[0][g]);
		
		System.out.println(ans);
	}
	
	public static void main(String[] args) {
		MonkeyAtTheKeyboard rm = new MonkeyAtTheKeyboard();
		java.util.Scanner scanner = new java.util.Scanner(System.in);

		int n = scanner.nextInt();
		String s = scanner.next();
		scanner.close();

		rm.solve(n, s);
	}
	
}