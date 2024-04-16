public class DrunkKingValidator {

	private int m;
	
	private int[][] degree = new int[4][500 * 500 + 4];
	
	private boolean addEdge(int x1, int y1, int x2, int y2, StringBuilder[] inp, int type) {
		if(inp[x1].charAt(y1) != 'o' || inp[x2].charAt(y2) != 'o') return false;
		x1--; x2--;
		if(x1 % 2 != 0 || x2 % 2 != 0 || y1 % 2 != 0 || y2 % 2 != 0) return false;
		int v1 = (x1 / 2) * m + (y1 / 2);
		int v2 = (x2 / 2) * m + (y2 / 2);
		degree[type][v1]++;
		degree[type][v2]++;
		return true;
	}
	
	public boolean validate(StringBuilder[] inp, int x, int y) {
		m = y;
		if(inp[0].equals("No")) return true;
		
		for(int i = 1; i < 2 * x; i++) {
			for(int j = 0; j < 2 * y - 1; j++) {
				if(inp[i].charAt(j) == '-') {
					if(!addEdge(i, j - 1, i, j + 1, inp, 0)) return false;
				}
				if(inp[i].charAt(j) == '|') {
					if(!addEdge(i - 1, j, i + 1, j, inp, 1)) return false;
				}
				if(inp[i].charAt(j) == '/') {
					if(!addEdge(i + 1, j - 1, i - 1, j + 1, inp, 2)) return false;
				}
				if(inp[i].charAt(j) == '\\') {
					if(!addEdge(i - 1, j - 1, i + 1, j + 1, inp, 3)) return false;
				}
			}
		}
		for(int i = 0; i < x * y; i++) {
			int sum = 0;
			for(int j = 0; j < 4; j++) {
				sum += degree[j][i];
				if(degree[j][i] > 1) {
					System.out.println("The same direction in two consequent moves");
					return false;
				}
			}
			if(sum != 2) {
				System.out.println("Degree of some vertice is more than 2");
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		for(int i = 2; i <= 500; i++) {
			System.out.println(i);
			for(int j = 2; j <= 500; j++) {
				DrunkKingValidator dkv = new DrunkKingValidator();
				DrunkKing dk = new DrunkKing();
				long st = System.currentTimeMillis();
				try {
					if(!dkv.validate(dk.solve(i, j), i, j)) {
						System.out.println(i + " " + j); return;
					}
				} catch (Exception e) {
					System.out.println(i + " " + j); return;
				}
				if(System.currentTimeMillis() - st > 1000) System.out.println(i + " " + j);
			}
		}
	}
	
}
