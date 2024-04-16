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
	
	int getMinimal3(String s) {
		
		java.util.Arrays.fill(dp2, 100000);
		
		int n = s.length();
		
		for(int i = 0; i < n; i++) {
			for(int j = Math.max(0, i - 10); j <= i; j++) {
				if(isPal(s, j, i)) {
					if(j == 0) {
						dp2[i] = 1;
					} else
					if( dp2[i] > dp2[j - 1] + 1) {
						dp2[i] = dp2[j - 1] + 1;
					}
				}
			}
		}
		
		return dp2[n - 1];
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
	
	public final int N = 7;
	public final int MN = 6;
	
	int[][] cdp = new int[1024][1 << N];
	String[][] ans = new String[1024][1 << N];
	
	String DP(int n) {
		
		for(int i = MN; i <= N; i++) {
			for(int j = 0; j < (1 << i); j++) {
				String s = getBits(i, j);
				if(maxPalindrom(s) < 6) {
					cdp[i][j] = getMinimal(s).length;
					ans[i][j] = s;
				}
			}
		}
		
		long cur = System.currentTimeMillis();
		
		for(int i = 1; i <= n; i++) {
			if(i % 10 == 0) {
				System.out.println((System.currentTimeMillis() - cur) + ": " + i);
			}
			//for(int j = MN; j <= N; j++) {
				for(int k = 0; k < (1 << N); k++) {
					
					if(cdp[i][k] == 0) continue;
					
					for(int h1 = MN; h1 <= N; h1++) {
						for(int h2 = 0; h2 < (1 << N); h2++) {
							
							//String s1 = getBits(j, k);
							String s2 = getBits(h1, h2);
							
							String tmp = ans[i][k] + s2;
							
							int h = maxPalindrom(tmp.substring(Math.max(tmp.length() - 20, 0), tmp.length()));
							int hp = getMinimal3(tmp);
							if(h < 6) {
								if(cdp[i + h1][h2] == 0) {
									cdp[i + h1][h2] = hp;
									ans[i + h1][h2] = ans[i][k] + s2;
								} else {
									if(cdp[i + h1][h2] < hp) {
										cdp[i + h1][h2] = hp;
										ans[i + h1][h2] = ans[i][k] + s2;
									}
								}
							}
							
						}
					//}
					
				}
			}
		}
		
		StringBuilder answer = new StringBuilder();
		for(int len = 20; len <= 1000; len++) {
			if(len % 10 == 0) System.out.println(len);
			String bestString = "";
			int bst = 0;
			for(int j = 0; j < (1 << N); j++) {
				if(ans[len][j] == null || ans[len][j].length() == 0) continue;
				if(bst < getMinimal2(ans[len][j]).length) {
					bst = getMinimal2(ans[len][j]).length;
					bestString = ans[len][j];
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
		for(int j = 0; j < (1 << N); j++) {
			if(cdp[n][j] != 0) {
				if(best < getMinimal2(ans[n][j]).length) {
					best = getMinimal2(ans[n][j]).length;
					bestString = ans[n][j];
				}
			}
		}

		System.out.println(best);
		System.out.println(System.currentTimeMillis() - cur);
		
		return bestString;
	}
	
	public static void main(String[] args) {
		MP2 mp2 = new MP2();
		String s = mp2.DP(1000);

		MP2 mp3 = new MP2();
		String[] h = mp3.getMinimal2(s);
		System.out.println(s.length() + " " + h.length);
	}
	
}
