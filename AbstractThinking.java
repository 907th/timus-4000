import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

public class AbstractThinking {

    class IntArrayComparator implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            int v1 = o1[0] * 10000 + o1[1];
            int v2 = o2[0] * 10000 + o2[1];
            return v1 - v2;
        }

    }

    IntArrayComparator c = new IntArrayComparator();

    HashSet<String> hs = new HashSet<String>();

    int n;
    int[] f = new int[64];

    int N(int x) {
        x++;
        if(x > n) return 1;
        return x;
    }

    boolean isIntersected(int[] a, int[] b) {
        if(a[0] == b[0] || a[0] == b[1] || a[1] == b[0] || a[1] == b[1]) return false;
        int c = a[0];
        int count = 0;
        while(c != a[1]) {
            if(c == b[0]) count++;
            if(c == b[1]) count++;
            if(N(c) == a[1] && N(c) != b[1]) {
                if(count == 1) return true;
            }
            c = N(c);
        }
        return false;
    }

    void addHorde(int s1, int e1, int s2, int e2, int s3, int e3) {
        int[][] a = new int[][] {{s1, e1}, {s2, e2}, {s3, e3}};
        for(int i = 0; i < 3; i++) {
            java.util.Arrays.sort(a[i]);
        }
        java.util.Arrays.sort(a, c);
        java.util.Arrays.fill(f, 0);

        for(int i = 0; i < 3; i++) {
            if(a[i][0] == a[i][1]) return;
            f[a[i][0]]++;
            f[a[i][1]]++;
            for(int j = 0; j < 3; j++) {
                if(i == j) continue;
                if(a[i][0] == a[j][0] && a[i][1] == a[j][1]) return;
            }
        }

        int strongIntersectionCount = 0;
        int lightIntersectionCount = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(i == j) continue;
                if(isIntersected(a[i], a[j])) {
                	strongIntersectionCount++;
                }
                if(a[i][0] == a[j][0] || a[i][0] == a[j][1] || a[i][1] == a[j][0] || a[i][1] == a[j][1]) {
                	lightIntersectionCount++;
                }
            }
        }
        
        strongIntersectionCount = strongIntersectionCount / 2;
        lightIntersectionCount = lightIntersectionCount / 2;

        String[] s = new String[a.length];
        for(int i = 0; i < a.length; i++) {
            s[i] = java.util.Arrays.toString(a[i]);
        }
        
        if(strongIntersectionCount + lightIntersectionCount == 3 && strongIntersectionCount > 0) {
        	
            hs.add(java.util.Arrays.toString(s));
        }

    }

    int getAnswer(int n) {

        this.n = n;

        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                for(int i1 = 1; i1 <= n; i1++) {
                    for(int j1 = 1; j1 <= n; j1++) {
                        for(int i2 = 1; i2 <= n; i2++) {
                            for(int j2 = 1; j2 <= n; j2++) {
                                addHorde(i, j, i1, j1, i2, j2);
                            }
                        }
                    }
                }
            }
        }

        return hs.size();
    }
    
    BigInteger[][] b = new BigInteger[6][2048];
    
    String solve(int n) {
    	b[5][8] = new BigInteger("8");
    	for(int i = 9; i <= 2000; i++) {
    		b[5][i] = b[5][i - 1].add(BigInteger.ONE);
    	}
    	b[4][7] = new BigInteger("22");
    	for(int i = 8; i <= 2000; i++) {
    		b[4][i] = b[4][i - 1].add(b[5][i]);
    	}
    	b[3][6] = new BigInteger("28");
    	for(int i = 7; i <= 2000; i++) {
    		b[3][i] = b[3][i - 1].add(b[4][i]);
    	}
    	b[2][5] = new BigInteger("17");
    	for(int i = 6; i <= 2000; i++) {
    		b[2][i] = b[2][i - 1].add(b[3][i]);
    	}
    	b[1][4] = new BigInteger("4");
    	for(int i = 5; i <= 2000; i++) {
    		b[1][i] = b[1][i - 1].add(b[2][i]);
    	}
    	b[0][3] = new BigInteger("0");
    	for(int i = 4; i <= 2000; i++) {
    		b[0][i] = b[0][i - 1].add(b[1][i]);
    	}
    	return b[0][n].toString();
    }

    public static void prebuild() {
        for(int i = 3; i <= 20; i++) {
            AbstractThinking at = new AbstractThinking();
            int h = at.getAnswer(i);
            System.out.println(i + "\t" + h);
        }
    }
    
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	int n = scanner.nextInt();
    	scanner.close();
    	AbstractThinking at = new AbstractThinking();
    	String h = at.solve(n);
    	System.out.println(h);
    }

}
