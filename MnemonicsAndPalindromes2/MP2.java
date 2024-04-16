import java.io.PrintWriter;

public class MP2 {

	int[] dp = new int[32];
	int[] p = new int[32];
	
	int[] dp2 = new int[1024];
	int[] p2 = new int[1024];
	
	boolean isPal(String s, int l, int r) {
		int len = r - l + 1;
		for(int i = 0; i < len / 2; i++) {
			if(s.charAt(l + i) != s.charAt(r - i)) return false;
		}
		return true;
	}
	
	boolean isBitPal(int count, int mask) {
		for(int i = 0; i < count / 2; i++) {
			int x = (1 << i) & mask;
			int y = (1 << (count - i - 1)) & mask;
			if(x == 0 && y != 0 || x != 0 && x == 0) return false;
		}
		return true;
	}
	
	String[] getMinimal(String s) {
		java.util.Arrays.fill(dp, 100000);
		java.util.Arrays.fill(p, -1);
		
		int n = s.length();
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j <= i; j++) {
				if(isPal(s, j, i)) {
					if(j == 0) {
						dp[i] = 1;
						p[i] = -1;
					} else
					if( dp[i] >= dp[j - 1] + 1) {
						dp[i] = dp[j - 1] + 1;
						p[i] = j - 1;
					}
				}
			}
		}
		
		String[] answer = new String[dp[n - 1]];
		int size = answer.length - 1;
		int end = n - 1;
		while(end != -1) {
			answer[size--] = s.substring(p[end] + 1, end + 1);
			end = p[end];
		}
		return answer;
	}
	
	String[] getMinimal2(String s) {
		
		java.util.Arrays.fill(dp2, 100000);
		java.util.Arrays.fill(p2, -1);
		
		int n = s.length();
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j <= i; j++) {
				if(isPal(s, j, i)) {
					if(j == 0) {
						dp2[i] = 1;
						p2[i] = -1;
					} else
					if( dp2[i] > dp2[j - 1] + 1) {
						dp2[i] = dp2[j - 1] + 1;
						p2[i] = j - 1;
					}
				}
			}
		}
		
		String[] answer = new String[dp2[n - 1]];
		int size = answer.length - 1;
		int end = n - 1;
		while(end != -1) {
			answer[size--] = s.substring(p2[end] + 1, end + 1);
			end = p2[end];
		}
		return answer;
	}
	
	String getBits(int size, int mask) {
		StringBuilder sb = new StringBuilder();
		for(int i = size - 1; i >= 0; i--) {
			if((mask & (1 << i)) == 0) sb.append('a'); else sb.append('b');
		}
		return sb.toString();
	}
	
	static String[] answersle19 = new String[] {"a","ba","baa","baaa","baaaa","bbabaa","bbabaaa","aabbabaa","aabbabaaa","aabbabaaaa","babaabbabaa","babaabbabaaa","babaabbabaaaa","bbabaaabbbabaa","bbabaaabbbabaaa","bbabaaabbbabaaaa","bbabaaabbbabaaaaa","bbabaaabbabaabbbaba", "bbabaabbababaabbabaa", "bbabaaabbabaabbbabaa"};
	
	int maxPalindrom(String s) {
		int best = 0;
		for(int i = 0; i < s.length(); i++) {
			for(int j = i; j < s.length(); j++) {
				if(isPal(s, i, j)) {
					int h = j - i + 1;
					if(best < h) {
						best = h;
					}
				}
			}
		}
		return best;
	}
	
	public final int N = 8;
	public final int MN = 5;
	
	int[][][] cdp = new int[1024][N + 1][1 << N];
	String[][][] ans = new String[1024][N + 1][1 << N];
	
	String DP(int n) {
		
		for(int i = MN; i <= N; i++) {
			for(int j = 0; j < (1 << i); j++) {
				String s = getBits(i, j);
				if(maxPalindrom(s) < 6) {
					cdp[i][i][j] = getMinimal(s).length;
					ans[i][i][j] = s;
				}
			}
		}
		
		for(int i = 1; i <= n; i++) {
			System.out.println(i);
			for(int j = MN; j <= N; j++) {
				for(int k = 0; k < (1 << j); k++) {
					
					if(cdp[i][j][k] == 0) continue;
					
					for(int h1 = MN; h1 <= N; h1++) {
						for(int h2 = 0; h2 < (1 << N); h2++) {
							
							String s1 = getBits(j, k);
							String s2 = getBits(h1, h2);
							
							int h = maxPalindrom(ans[i][j][k] + s2);
							int hp = getMinimal2(ans[i][j][k] + s2).length;
							if(h < 6) {
								if(cdp[i + h1][h1][h2] == 0) {
									cdp[i + h1][h1][h2] = hp;
									ans[i + h1][h1][h2] = ans[i][j][k] + s2;
								} else {
									if(cdp[i + h1][h1][h2] < hp) {
										cdp[i + h1][h1][h2] = hp;
										ans[i + h1][h1][h2] = ans[i][j][k] + s2;
									}
								}
							}
							
						}
					}
					
				}
			}
		}
		
		StringBuilder answer = new StringBuilder();
		for(int len = 20; len <= 1000; len++) {
			String bestString = "";
			for(int i = 1; i <= N; i++) {
				for(int j = 0; j < (1 << N); j++) {
					if(cdp[len][i][j] != 0) {
						bestString = ans[len][i][j];
					}
				}
			}
			answer.append("\"" + bestString + "\",\n");
		}
		
		try {
			PrintWriter pw = new PrintWriter("D:/out.txt");
			pw.append(answer);
			pw.flush();
			pw.close();
		} catch(Exception e) {
			
		}

		int best = 0;
		String bestString = "";
		for(int i = 1; i <= N; i++) {
			for(int j = 0; j < (1 << N); j++) {
				if(cdp[n][i][j] != 0) {
					if(best < getMinimal2(ans[n][i][j]).length) {
						best = getMinimal2(ans[n][i][j]).length;
						bestString = ans[n][i][j];
					}
				}
			}
		}

		System.out.println(best);
		
		return bestString;
	}
	
	public static void main(String[] args) {
		MP2 mp2 = new MP2();
		String s = mp2.DP(29);
		System.out.println(s);
		String[] h = mp2.getMinimal2(s);
		System.out.println(h.length);
	}
	
}
