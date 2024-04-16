public class DrunkKing {

	private StringBuilder[] print(char[][] a) {
		StringBuilder[] toReturn = new StringBuilder[a.length + 1];
		toReturn[0] = new StringBuilder("Yes");
		for(int i = 0; i < a.length; i++) {
			toReturn[i + 1] = new StringBuilder();
			for(int j = 0; j < a[0].length; j++) {
				toReturn[i + 1].append(a[i][j]);
			}
		}
		return toReturn;
	}

	public char[][] verticalMirror(char[][] a) {
		char[][] r = new char[a.length][a[0].length];
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				r[i][j] = a[a.length - i - 1][j];
				if(r[i][j] == '\\') r[i][j] = '/'; else
				if(r[i][j] == '/') r[i][j] = '\\';
			}
		}
		return r;
	}
	
	public char[][] transpose(char[][] a) {
		char[][] r = new char[a[0].length][a.length];
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[0].length; j++) {
				r[r.length - j - 1][i] = a[i][j];
			}
		}
		for(int i = 0; i < r.length; i++) {
			for(int j = 0; j < r[0].length; j++) {
				if(r[i][j] == '|') r[i][j] = '-'; else
				if(r[i][j] == '-') r[i][j] = '|'; else
				if(r[i][j] == '\\') r[i][j] = '/'; else
				if(r[i][j] == '/') r[i][j] = '\\';
			}
		}
		return r;
	}
	
	public char[][] getVertical2OnX(int x) { //starting from 6
		char[][] toRet = new char[2 * x - 1][3];
		for(int i = 0; i < toRet.length; i++) {
			for(int j = 0; j < toRet[0].length; j++) {
				toRet[i][j] = ' ';
			}
		}
		for(int i = 0; i < x; i++) {
			toRet[2 * i][0] = 'o';
			toRet[2 * i][2] = 'o';
		}
		
		toRet[1][0] = toRet[1][2] = toRet[2 * x - 3][0] = toRet[2 * x - 3][2] = '|';
		toRet[0][1] = toRet[2 * x - 2][1] = '-';
		
		toRet[3][1] = '\\';
		
		toRet[4][1] = toRet[6][1] = '-'; 
		toRet[5][0] = '|';
		
		for(int i = 4; i < x - 2; i++) {
			toRet[2 * i - 1][1] = '/';
			toRet[2 * i][1] = '-';
		}
		toRet[2 * x - 5][1] = '/';
		
		return toRet;
	}

	public char[][] getHorizontal5OnX(int x) {
		String[] temp = new String[] {};
		String[] toAdd = new String[] {
			" o-o o-o",
			" | | | |",
			" o o-o o",
			"  \\   / ",
			" o-o o-o",
			"  \\   / ",
			" o o o-o",
			"/| |  / ",
			" o-o o-o"
		};
		if(x % 4 == 0) {
			temp = new String[] {
				"o-o o-o",
				"  | | |",
				"o-o o o",
				"|  / / ",
				"o o o-o",
				" \\ \\  |",
				"o-o o o",
				"|  / / ",
				"o-o o-o"
			};
		}
		if(x % 4 == 1) {
			temp = new String[] {
				"o-o o-o o",
				"  | | |/|",
				"o-o o o o",
				"|  /   / ",
				"o o-o o-o",
				" \\   \\  |",
				"o-o o o o",
				"|  /|/ / ",
				"o-o o o-o"
			};
		}
		if(x % 4 == 2) {
			temp = new String[] {
				"o-o o-o o-o",
				"  |  \\ \\| |",
				"o o-o o o o",
				"|\\  | |  / ",
				"o o-o o o-o",
				" \\     \\  |",
				"o-o o-o o o",
				"|  / / / / ",
				"o-o o-o o-o"
			};
		}
		if(x % 4 == 3) {
			temp = new String[] {
				"o-o o-o o-o o",
				"  |  \\ \\| |/|",
				"o o-o o o o o",
				"|\\  | |    / ",
				"o o-o o-o o-o",
				" \\       \\  |",
				"o-o o-o o o o",
				"|  / / /|/ / ",
				"o-o o-o o o-o"
			};
		}
		
		int n = (x - (temp[0].length() + 1) / 2) / 4;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < temp.length; j++) {
				temp[j] = temp[j] + toAdd[j];
			}
		}
		
		char[][] toRet = new char[temp.length][temp[0].length()];
		for(int i = 0; i < toRet.length; i++) {
			for(int j = 0; j < toRet[0].length; j++) {
				toRet[i][j] = temp[i].charAt(j);
			}
		}
		
		return toRet;
	}

	public char[][] getHorizontal4OnX(int x) {
		String[] temp = new String[] {};
		String[] toAdd = new String[] {
			" o-o o-o",
			" | | | |",
			" o o-o o",
			"  \\   / ",
			" o o o-o",
			"/| |  / ",
			" o-o o-o"
		};
		if(x % 4 == 0) {
			temp = new String[] {
				"o-o o-o",
				" /  | |",
				"o-o o o",
				" / / / ",
				"o o o-o",
				"| |  / ",
				"o-o o-o"
			};
		}
		if(x % 4 == 1) {
			temp = new String[] {
				"o-o o-o o-o o o-o",
				"  | |  \\| |/|/  |",
				"o-o o-o o o o o-o",
				"|      \\     /   ",
				"o-o o o o-o o o-o",
				" / /| |\\  | |/ / ",
				"o-o o-o o-o o o-o"
			};
		}
		if(x % 4 == 2) {
			temp = new String[] {
				"o-o o-o o-o",
				"  |  \\ \\| |",
				"o-o o-o o o",
				"|    \\   / ",
				"o-o o o o-o",
				" / /| |  / ",
				"o-o o-o o-o"
			};
		}
		if(x % 4 == 3) {
			temp = new String[] {
				"o-o o-o o-o o",
				"  | |  \\| |/|",
				"o-o o-o o o o",
				"|      \\   / ",
				"o-o o o o o-o",
				" / /| |\\|  / ",
				"o-o o-o o o-o"
			};
		}
		
		int n = (x - (temp[0].length() + 1) / 2) / 4;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < temp.length; j++) {
				temp[j] = temp[j] + toAdd[j];
			}
		}
		
		char[][] toRet = new char[temp.length][temp[0].length()];
		for(int i = 0; i < toRet.length; i++) {
			for(int j = 0; j < toRet[0].length; j++) {
				toRet[i][j] = temp[i].charAt(j);
			}
		}

		return toRet;
	}

	public char[][] getHorizontal2OnX(int x, boolean isTopDown) {
		char[][] toRet = new char[3][2 * x - 1];
		for(int i = 0; i < toRet.length; i++) {
			for(int j = 0; j < toRet[0].length; j++) {
				toRet[i][j] = ' ';
			}
		}
		for(int i = 0; i < x; i++) {
			toRet[0][2 * i] = 'o';
			toRet[2][2 * i] = 'o';
		}
		if(x % 2 == 0) {
			for(int i = 0; i < x - 1; i++) {
				if(i % 2 == 0) toRet[0][2 * i + 1] = '-';
				if(i % 2 == 0) toRet[2][2 * i + 1] = '-';
				toRet[1][2 * i + 1] = isTopDown ? '/' : '\\';
			}
		} else {
			for(int i = 1; i < x - 2; i++) {
				toRet[1][2 * i + 1] = isTopDown ? '/' : '\\';
				if(i > 1) toRet[1][2 * i] = '|'; 
			}
			toRet[0][1] = toRet[2][1] = toRet[0][2 * x - 3] = toRet[2][2 * x - 3] = '-';
			toRet[1][1] = isTopDown ? '/' : '\\'; toRet[1][2 * x - 2] = '|';
		}
		return toRet;
	}
	
	private void fill(char[][] source, int x, int y, char[][] target) {
		for(int i = 0; i < target.length; i++) {
			for(int j = 0; j < target[0].length; j++) {
				source[x + i][y + j] = target[i][j];
			}
		}
	}
	
	private void fillOdd(char[][] answer, int x, int y) {
		char[][] horz5 = getHorizontal5OnX(y - 2);
		fill(answer, 0, 4, horz5); answer[1][3] = '/';  answer[2 * x - 3][3] = '\\';

		char[][] horz2TopDown = getHorizontal2OnX(y - 2, true);
		char[][] horz2DownTop = getHorizontal2OnX(y - 2, false);

		if(((x - 5) / 2) % 2 == 0) {
			char[][] horz4 = getHorizontal4OnX(y - 2);
			fill(answer, 10, 4, verticalMirror(horz4)); answer[9][answer[0].length - 1] = '|';

			int cnt = (x - 5 - 4) / 4;
			int from = 18;
			for(int i = 0; i < cnt; i++) {
				answer[from - 1][4] = '|';
				fill(answer, from, 4, horz2TopDown);
				from += 4;
				if(y % 2 == 1) 
					answer[from - 1][answer[0].length - 3] = '|'; else
					answer[from - 1][answer[0].length - 1] = '|';
				fill(answer, from, 4, horz2DownTop);
				from += 4;
			}
			
		} else {
			int cnt = (x - 5) / 4;
			int from = 10;
			for(int i = 0; i < cnt; i++) {
				if(y % 2 == 1) 
					answer[from - 1][answer[0].length - 3] = '|'; else
					answer[from - 1][answer[0].length - 1] = '|';
				fill(answer, from, 4, horz2DownTop);
				from += 4;
				answer[from - 1][4] = '|';
				fill(answer, from, 4, horz2TopDown);
				from += 4;
			}
			if(y % 2 == 1) 
				answer[from - 1][answer[0].length - 3] = '|'; else
				answer[from - 1][answer[0].length - 1] = '|';
			fill(answer, from, 4, horz2DownTop);
		}
		if(x % 4 == 3 && y % 2 == 1) {
			answer[9][answer[0].length - 2] = '/';
			answer[9][answer[0].length - 3] = ' ';
		}
	}
	
	private void fillEven(char[][] answer, int x, int y) {
		answer[1][3] = '/';  answer[2 * x - 3][3] = '\\';
		char[][] horz2TopDown = getHorizontal2OnX(y - 2, true);
		char[][] horz2DownTop = getHorizontal2OnX(y - 2, false);
		if((x / 2) % 2 == 1) {
			char[][] horz4 = getHorizontal4OnX(y - 2);
			fill(answer, 0, 4, horz4);
			int cnt = (x - 4) / 4;
			int from = 8;
			for(int i = 0; i < cnt; i++) {
				if(y % 2 == 1) 
					answer[from - 1][answer[0].length - 3] = '|'; else
					answer[from - 1][answer[0].length - 1] = '|';
				fill(answer, from, 4, horz2DownTop);
				from += 4;
				answer[from - 1][4] = '|';
				fill(answer, from, 4, horz2TopDown);
				from += 4;
			}
			if(y % 2 == 1) 
				answer[from - 1][answer[0].length - 3] = '|'; else
				answer[from - 1][answer[0].length - 1] = '|';
			fill(answer, from, 4, horz2DownTop);
			from += 4;
		} else {
			int cnt = x / 4 - 1;
			fill(answer, 0, 4, horz2TopDown);
			int from = 4;
			for(int i = 0; i < cnt; i++) {
				if(y % 2 == 1) 
					answer[from - 1][answer[0].length - 3] = '|'; else
					answer[from - 1][answer[0].length - 1] = '|';

				fill(answer, from, 4, horz2DownTop);
				from += 4;
				answer[from - 1][4] = '|';
				fill(answer, from, 4, horz2TopDown);
				from += 4;
			}
			if(y % 2 == 1) 
				answer[from - 1][answer[0].length - 3] = '|'; else
				answer[from - 1][answer[0].length - 1] = '|';
			fill(answer, from, 4, horz2DownTop);
			from += 4;
		}
		if(x % 4 == 2 && y % 2 == 1) {
			answer[7][answer[0].length - 2] = '/';
			answer[7][answer[0].length - 3] = ' ';
		}
	}
	
	private char[][] solve4(int x) {
		char[][] toRet = new char[7][2 * x - 1];
		for(int i = 0; i < toRet.length; i++) {
			for(int j = 0; j < toRet[0].length; j++) {
				toRet[i][j] = ' ';
			}
		}
		for(int i = 0; i < toRet.length; i += 2) {
			for(int j = 0; j < toRet[0].length; j += 2) {
				toRet[i][j] = 'o';
			}
		}
		
		String[] solution = new String[7];
		
		solution[0] = "o-o";
		solution[1] = "|  ";
		solution[2] = "o-o";
		solution[3] = " / ";
		solution[4] = "o o";
		solution[5] = "| |";
		solution[6] = "o-o";
		
		for(int i = 0; i < x - 4; i++) {
			solution[0] = solution[0] + " o";
			solution[1] = solution[1] + "\\|";
			solution[2] = solution[2] + " o";
			solution[3] = solution[3] + "  ";
			solution[4] = solution[4] + " o";
			solution[5] = solution[5] + "\\|";
			solution[6] = solution[6] + " o";
		}

		solution[0] = solution[0] + " o-o";
		solution[1] = solution[1] + "\\| |";
		solution[2] = solution[2] + " o o";
		solution[3] = solution[3] + "  / ";
		solution[4] = solution[4] + " o-o";
		solution[5] = solution[5] + "\\  |";
		solution[6] = solution[6] + " o-o";
		
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < solution[i].length(); j++) {
				toRet[i][j] = solution[i].charAt(j);
			}
		}
		
		return toRet;
	}
	
	private char[][] solve5(int x) {
		char[][] toRet = new char[9][2 * x - 1];
		for(int i = 0; i < toRet.length; i++) {
			for(int j = 0; j < toRet[0].length; j++) {
				toRet[i][j] = ' ';
			}
		}
		for(int i = 0; i < toRet.length; i += 2) {
			for(int j = 0; j < toRet[0].length; j += 2) {
				toRet[i][j] = 'o';
			}
		}
		String[] solution = new String[] {"", "", "", "", "", "", "", "", ""};
		if(x % 2 == 1) {
			String[][] initialSolution = new String[][] {
				{
					"o-o ",
					"| |/",
					"o o ",
					" \\ ",
					"o-o ",
					"|   ",
					"o-o ",
					" / /",
					"o-o "},
				{
					"o-o o o-o",
					"  |/|/  |",
					"o o o o-o",
					" |\\     \\",
					"o o-o o-o",
					" \\  |  \\",
					"o o o o o",
					"|/ / /| |",
					"o o-o o-o"
				}};
			
			String[] toAdd = new String[] {
				"o o ",
				"|/|/",
				"o o ",
				"    ",
				"o-o ",
				" \\ \\",    
				"o o ",
				"| | ", 
				"o-o "};
			
			for(int i = 0; i < 9; i++) {
				solution[i] = initialSolution[0][i];
				for(int j = 0; j < (x - 7) / 2; j++) {
					solution[i] = solution[i] + toAdd[i];
				}
				solution[i] = solution[i] + initialSolution[1][i];
			}
		} else {
			String[] initialSolution = new String[] {
					"o-o o-o o-o",
					" \\ \\| | | |",
					"o o o o o o",
					"|\\|  / / / ",
					"o o o-o o-o",
					" \\       / ",
					"o-o o o o-o",
					"|  /| |\\  |",
					"o-o o-o o-o"};
				
				String[] toAdd = new String[] {
					" o-o",
					" | |",
					" o o",
					"/ / ",
					" o-o",
					"    ",    
					"-o o",
					" | |", 
					" o-o"};
				
				int endIndex = 7;
				for(int i = 0; i < 9; i++) {
					solution[i] = initialSolution[i].substring(0, endIndex);
					for(int j = 0; j < (x - 6) / 2; j++) {
						solution[i] = solution[i] + toAdd[i];
					}
					solution[i] = solution[i] + initialSolution[i].substring(endIndex);
				}
		}
		
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < solution[i].length(); j++) {
				toRet[i][j] = solution[i].charAt(j);
			}
		}
		return toRet;
	}
	
	public StringBuilder[] solve(int x, int y) {
		
		if(x == 2 || y == 2) {
			if(x == 2 && y == 2) {
				return new StringBuilder[] {new StringBuilder("Yes"), new StringBuilder("o-o"), new StringBuilder("| |"), new StringBuilder("o-o")};
			}
			return new StringBuilder[] {new StringBuilder("No")};
		}
		if(x == 3 || y == 3) {
			return new StringBuilder[] {new StringBuilder("No")};
		}
		
		if(x == 4 || y == 4) {
			char[][] a = new char[0][0];
			if(x == 4) a = solve4(y);
			if(y == 4) {
				a = solve4(x);
				a = transpose(a);
			}
			return print(a);
		}
		
		if(x == 5 || y == 5) {
			if(x == 5 && y == 5) return new StringBuilder[] {new StringBuilder("No")};
			char[][] a = new char[0][0];
			if(x == 5) a = solve5(y);
			if(y == 5) {
				a = solve5(x);
				a = transpose(a);
			}
			return print(a);
		}
		
		if(x >= 6 && y >= 6) {
			boolean shouldTrans = false;
			if(x == 6 && y == 7 || (x > 7) && y == 7) {
				int h = x;
				x = y;
				y = h;
				shouldTrans = true;
			}
			char[][] answer = new char[2 * x - 1][2 * y - 1];
			for(int i = 0; i < answer.length; i++) {
				for(int j = 0; j < answer[0].length; j++) {
					answer[i][j] = ' ';
				}
			}
			char[][] vert = getVertical2OnX(x);
			fill(answer, 0, 0, vert);
			if(x % 2 == 1) {
				fillOdd(answer, x, y);
			} else {
				fillEven(answer, x, y);
			}
			if(shouldTrans) answer = transpose(answer);
			return print(answer);
		} else if(x > y) {
			int h = x;
			x = y;
			y = h;

			char[][] answer = new char[2 * x - 1][2 * y - 1];
			for(int i = 0; i < answer.length; i++) {
				for(int j = 0; j < answer[0].length; j++) {
					answer[i][j] = ' ';
				}
			}

			char[][] vert = getVertical2OnX(x);
			fill(answer, 0, 0, vert);
			if(x % 2 == 1) {
				fillOdd(answer, x, y);
			} else {
				fillEven(answer, x, y);
			}
			answer = transpose(answer);
			return print(answer);
		} else {
			char[][] answer = new char[2 * x - 1][2 * y - 1];
			for(int i = 0; i < answer.length; i++) {
				for(int j = 0; j < answer[0].length; j++) {
					answer[i][j] = ' ';
				}
			}
			char[][] vert = getVertical2OnX(x);
			fill(answer, 0, 0, vert);
			if(x % 2 == 1) {
				fillOdd(answer, x, y);
			} else {
				fillEven(answer, x, y);
			}
			return print(answer);
		}
	}
	
	public static void main(String[] args) {
		DrunkKing dk = new DrunkKing();
		java.util.Scanner s = new java.util.Scanner(System.in);
		int n = s.nextInt(); int m = s.nextInt();
		s.close();
		StringBuilder[] t = dk.solve(n, m);
		StringBuilder answer = new StringBuilder();
		for(int i = 0; i < t.length; i++) {
			answer.append(t[i]).append("\n");
		}
		System.out.println(answer.toString());
	}
	
}