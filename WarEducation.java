public class WarEducation {

	int n;
	int[] row = new int[1024];
	int[] col = new int[1024];
	
	static char opp(char c) {
		return c == 'B' ? 'W' : 'B';
	}
	
	static char[][] reverse(char[][] c) {
		for(int i = 0; i < c.length; i++) {
			for(int j = 0; j < c[i].length; j++) {
				c[i][j] = opp(c[i][j]);
			}
		}
		return c;
	}
	
	int[][] solve(char[][] in) { //Try to move all cells to B
		n = in.length;
		for(int i = 0; i < 1024; i++) {
			row[i] = 0;
			col[i] = 0;
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(in[i][j] == 'W') {
					row[i] = 1 - row[i];
					col[j] = 1 - col[j];
				}
			}
		}
		
		int count = 0;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				int k = 0;
				if(in[i][j] == 'W') k = 1;
				if((row[i] + col[j] + k) % 2 == 1) {
					count++;
				}
			}
		}
		
		int[][] answer = new int[count][2];
		count = 0;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				int k = 0;
				if(in[i][j] == 'W') k = 1;
				if((row[i] + col[j] + k) % 2 == 1) {
					answer[count][0] = i + 1;
					answer[count][1] = j + 1;
					count++;
				}
			}
		}
		
		return answer;
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int n = scanner.nextInt();
		char[][] c = new char[n][n];
		for(int i = 0; i < n; i++) {
			String s = scanner.next();
			for(int j = 0; j < n; j++) {
				c[i][j] = s.charAt(j);
			}
		}
		scanner.close();
		WarEducation we = new WarEducation();
		int[][] ans = null;
		int[][] a1 = we.solve(c);
		int[][] a2 = we.solve(reverse(c));
		if(a1.length < a2.length) ans = a1; else ans = a2;
		
		java.io.PrintWriter pw = new java.io.PrintWriter(System.out);
		
		pw.println(ans.length);
		for(int i = 0; i < ans.length; i++) {
			pw.println(ans[i][0] + " " + ans[i][1]);
		}
		
		pw.flush();
	}
	
}