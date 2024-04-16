import java.util.HashMap;

public class Chasing {

    long[] solution = new long[64];
    int size = 0;

    HashMap<String, Integer> hm = new HashMap<>();

    static long gcd(long x, long y) {
        if(x == 0) return y;
        return gcd(y % x, x);
    }

    int rec(long m, long n, long last, int dep) {
        long h = gcd(m, n);
        m = m / h;
        n = n / h;
        if(dep > 19) return 0;
        if(m == 1) {
            if(n > last && n <= 100000) {
                solution[size++] = n;
                return 1;
            }
        }
        String key = m + " " + n;
        if(hm.containsKey(key)) {
            return hm.get(key);
        }
        long end = 50;
        long start = last + 1;
        if(dep == 0) end = 100;
        if(m == 2) {
            start = Math.max(n / 2, last + 1);
            end = start + 5;
        }
        for(long i = start; i <= end; i++) {
            long newNom = (m * i - n);
            if(newNom < 0) continue;
            long newDenom = (n * i);
            h = rec(newNom, newDenom, i, dep + 1);
            if(h == 1) {
                solution[size++] = i;
                return 1;
            }
        }
        hm.put(key, 0);
        return 0;
    }

    void solve(int m, int n) {
        size = 0;
        if(m == 1) {
            System.out.println(1 + "\n" + n);
            return;
        }
        if(m > n) {
            m -= n;
            solution[size++] = 1;
            long h = gcd(m, n);
            m /= h;
            n /= h;
        }
        int h = rec(m, n, 1, 0);
        if(h == 0) {
            System.out.println(-1);
            return;
        }
        java.util.Arrays.sort(solution, 0, size);
        System.out.println(size);
        for(int i = size - 1; i >= 0; i--) {
            System.out.print(solution[i] + " ");
        }
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        scanner.close();
        Chasing c = new Chasing();
        c.solve(m, n);
    }

}