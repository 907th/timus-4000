import java.util.Random;

public class HalftonesTestGenerator {

	public int[][] getRandomTest(int n, int m) {
		int[][] a = new int[n][m];
		Random r = new Random();
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				a[i][j] = r.nextInt(256);
			}
		}
		return a;
	}
	
}
