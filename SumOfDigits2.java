public class SumOfDigits2 {

	java.util.HashSet<String> prefixesSet = new java.util.HashSet<String>();
	java.util.HashSet<String> patternsSet = new java.util.HashSet<String>();

    String[] prefixes = new String[65536];
    String[] patterns = new String[2 * 65536];
    
    int[] sumOfD = new int[65536];
    int[] sumOfD2 = new int[65536];
    
    String[] for35 = new String[] {"1111111111111Y","111111111111Y","111111111112X","11111111111X","11111111111XY","11111111111Y","11111111112X","1111111111R","1111111111V","1111111111X","1111111111XX","1111111111XXY","1111111111XY","1111111111Y","1111111112X","111111111Q","111111111R","111111111S","111111111V","111111111W","111111112Q","111111112U","111111112W","111111112WW","111111112X","11111111WW","1111111222V","111111122P","111111122V","111111122W","11111112WW","111111222V","11111122WX","1111112WXX","111111SYYY","111111SYYYY","111111WWXX","111111WXXX","111111WXXXX","1111122WXX","11111PXYYY","11111WXXXX","11112WXXXX","1111MYYYYY","1111MYYYYYY","1111MYYYYYYY","1111MYYYYYYYY","1111WWWXXX","1111WWXXXX","111222XXXX","111MXYYYYY","111MXYYYYYY","111MXYYYYYYY","111MXYYYYYYYY","111MYYYYYY","111MYYYYYYY","111MYYYYYYYY","111WWWXXXX","112WWXXXXX","112WWXXXXXX","112WXXXXXX","11MXXXYYYY","11MXXXYYYYY","11MXXXYYYYYY","11MXXXYYYYYYY","11MXXXYYYYYYYY","11MXXYYYYY","11MXXYYYYYY","11MXXYYYYYYY","11MXXYYYYYYYY","11MXYYYYYY","11MXYYYYYYY","11MXYYYYYYYY","11MYYYYYYY","11MYYYYYYYY","11QUXXYYYY","11QUXXYYYYY","11QUXXYYYYYY","11QUXXYYYYYYY","11QUXYYYYY","11QUXYYYYYY","11QUXYYYYYYY","11QUYYYYYY","11QUYYYYYYY","11WWWXXXXX","11WWXXXXXX","11WXXXXXXX","12MYYYYYYY","12VWWXXXXX","12VWWXXXXXX","12VWXXXXXX","12WWWWWXXX","12WWXXXXXX","12WWXXXXXXY","12WWXXXXXY","13VWXXXXXY","14QWWYYYYY","14QWWYYYYYY","14QWWYYYYYYY","15QVXYYYYY","15VWWXXYYY","16UXXXXXXY","16UXXXXXXYY","16UXXXXXYY","17UWWXXYYY","17UWXXXYYY","18TXXXXXXY","1EHIJJKKKL","1FIJKKLLLLM","1FIJKKLLLM","1FIJKLLLLM","1IMNOOPPPQ","1JNOPPQQQQR","1JNOPPQQQR","1JNOPQQQQR","1JNPPQQQQR","1KOPQQRRRRRS","1KOPQQRRRRS","1KOPQQRRRS","1KOPQRRRRRS","1KOPQRRRRS","1KOPRRRRRS","1NSTUUVVVW","1NTUUVVVVW","1OTUVVWWWWX","1OTUVVWWWX","1OTUVWWWWX","1OTVVWWWWX","1PUVWWXXXXXY","1PUVWWXXXXY","1PUVWWXXXY","1PUVWXXXXXY","1PUVWXXXXY","1PUVXXXXXY","1PUWWXXXXXY","1PUWWXXXXY","1PUWXXXXXY","222MYYYYYY","22WXXXXXXX","22WXXXXXXXX","2FIJKKLLLM","2GJKLLMMMMN","2GJKLLMMMN","2GJKLMMMMN","2JNOPPQQQR","2KOPQQRRRRS","2KOPQQRRRS","2KOPQRRRRS","2KOQQRRRRS","2LPQRRSSSSST","2LPQRRSSSST","2LPQRRSSST","2LPQRSSSSST","2LPQRSSSST","2LPQSSSSST","2MWYYYYYYY","2OTUVVWWWX","2OUVVWWWWX","2PUVWWXXXXY","2PUVWWXXXY","2PUVWXXXXY","2PUWWXXXXY","3GJKLLMMMN","3HKLMMNNNNO","3HKLMMNNNO","3HKLMNNNNO","3KOPQQRRRS","3LPQRRSSSST","3LPQRRSSST","3LPQRSSSST","3LPRRSSSST","3MQRSSTTTTTU","3MQRSSTTTTU","3MQRSSTTTU","3MQRSTTTTTU","3MQRSTTTTU","3MQRTTTTTU","3PUVWWXXXY","3PVWWXXXXY","4HKLMMNNNO","4ILMNNOOOOP","4ILMNNOOOP","4ILMNOOOOP","4LPQRRSSST","4MQRSSTTTTU","4MQRSSTTTU","4MQRSTTTTU","4MQSSTTTTU","4NRSTTUUUUUV","4NRSTTUUUUV","4NRSTTUUUV","4NRSTUUUUUV","4NRSTUUUUV","4NRSUUUUUV","5ILMNNOOOP","5JMNOOPPPPQ","5JMNOOPPPQ","5JMNOPPPPQ","5KWYYYYYYY","5KWYYYYYYYY","5KYYYYYYYY","5MQRSSTTTU","5NRSTTUUUUV","5NRSTTUUUV","5NRSTUUUUV","5NRTTUUUUV","5OSTUUVVVVVW","5OSTUUVVVVW","5OSTUUVVVW","5OSTUVVVVVW","5OSTUVVVVW","5OSTVVVVVW","6IYYYYYYYY","6JMNOOPPPQ","6KNOPPQQQQR","6KNOPPQQQR","6KNOPQQQQR","6NRSTTUUUV","6OSTUUVVVVW","6OSTUUVVVW","6OSTUVVVVW","6OSUUVVVVW","6PTUVVWWWWWX","6PTUVVWWWWX","6PTUVVWWWX","6PTUVWWWWWX","6PTUVWWWWX","6PTUWWWWWX","7KNOPPQQQR","7LOPQQRRRRS","7LOPQQRRRS","7LOPQRRRRS","7OSTUUVVVW","7PTUVVWWWWX","7PTUVVWWWX","7PTUVWWWWX","7PTVVWWWWX","7QUVWWXXXXXY","7QUVWWXXXXY","7QUVWWXXXY","7QUVWXXXXXY","7QUVWXXXXY","7QUVXXXXXY","8LOPQQRRRS","8MPQRRSSSST","8MPQRRSSST","8MPQRSSSST","8PTUVVWWWX","8QUVWWXXXXY","8QUVWWXXXY","8QUVWXXXXY","8QUWWXXXXY","8STWYYYYYY","8STWYYYYYYY","8STWYYYYYYYY","9MPQRRSSST","9NQRSSTTTTU","9NQRSSTTTU","9NQRSTTTTU","9QUVWWXXXY","9STWYYYYYY","9STWYYYYYYY","9STYYYYYYY","ANQRSSTTTU","AORSTTUUUUV","AORSTTUUUV","AORSTUUUUV","ASTWYYYYYY","BORSTTUUUV","BPSTUUVVVVW","BPSTUUVVVW","BPSTUVVVVW","CIYYYYYYYY","CIYYYYYYYYY","CPSTUUVVVW","CQTUVVWWWWX","CQTUVVWWWX","CQTUVWWWWX","DQTUVVWWWX","DRUVWWXXXXY","DRUVWWXXXY","DRUVWXXXXY","EHYYYYYYYY","ERUVWWXXXY","ETTXXYYYYY","ETTXXYYYYYY","ETTXYYYYYY","FTTXXYYYYY"};
    
    String[] for36 = new String[] {"11111111111X","11111111111Y","11111111112X","1111111111R","1111111111V","1111111111X","1111111112X","111111111Q","111111111R","111111111S","111111111V","111111111W","111111111X","111111112Q","111111112U","111111112W","111111112X","11111111WW","11111111XX","11111111XY","11111111XYY","1111111222V","111111122P","111111122V","111111122W","111111122Y","111111122YY","11111112XX","11111112XY","1111111XYY","111111222V","11111122YY","1111112XYY","111111TZZZ","111111TZZZZ","111111XYYY","111111XYYYY","1111122XYY","1111122YYY","11111QZZZZ","11111XYYYY","111122YYYY","1111QYZZZZ","1111XXXYYY","1111XXYYYY","1111XYYYYY","1111XYYYYYY","111QYYYZZZ","111QYYYZZZZ","111QYYZZZZ","111XXYYYYY","111XYYYYYY","112XXYYYYY","112XYYYYYY","11QYYYZZZZ","11RVYYZZZZ","11RVYYZZZZZ","11RVYYZZZZZZ","11RVYZZZZZ","11RVYZZZZZZ","11RVYZZZZZZZ","11RVZZZZZZ","11RVZZZZZZZ","11RVZZZZZZZZ","11XXYYYYYY","11XXYYYYYYY","11XXYYYYYYYY","11XYYYYYYY","11XYYYYYYYY","11XYYYYYYYYY","123XXYYYYZ","12RVXZZZZZ","12RVXZZZZZZ","12RVXZZZZZZZ","12RVZZZZZZ","12RVZZZZZZZ","12WXYYYYYY","12WYYYYYYY","12XXYYYYYZ","13NXZZZZZZ","13NXZZZZZZZ","13NZZZZZZZ","13WWYYYYYY","13WWYYYYYYY","13WXXXYYYY","13WXXYYYYZ","13WYYYYYYY","13WYYYYYYYZ","13WYYYYYYZ","14WXXYYYZZ","15RWYZZZZZ","15RWYZZZZZZ","15RWZZZZZZ","15XXXYYYYY","16RXZZZZZZ","16VYYYYYZZ","17VXYYYYZZ","17VXYYYYZZZ","17VXYYYZZZ","18VWYYYZZZ","18VXXYZZZZ","19UXYYYYZZ","1EHIJJKKKL","1FIJKKLLLLM","1FIJKKLLLM","1FIJKLLLLM","1IMNOOPPPQ","1JNOPPQQQQR","1JNOPPQQQR","1JNOPQQQQR","1JNPPQQQQR","1KOPQQRRRRRS","1KOPQQRRRRS","1KOPQQRRRS","1KOPQRRRRRS","1KOPQRRRRS","1KOPRRRRRS","1NSTUUVVVW","1NTUUVVVVW","1OTUVVWWWWX","1OTUVVWWWX","1OTUVWWWWX","1OTVVWWWWX","1PUVWWXXXXXY","1PUVWWXXXXY","1PUVWWXXXY","1PUVWXXXXXY","1PUVWXXXXY","1PUVXXXXXY","1PUWWXXXXXY","1PUWWXXXXY","1PUWXXXXXY","1QUXXYYYYZ","1QVWXXYYYYYZ","1QVWXXYYYYZ","1QVWXXYYYZ","1QVWXYYYYYYZ","1QVWXYYYYYZ","1QVWXYYYYZ","1QVWYYYYYYZ","1QVWYYYYYZ","1QVXXYYYZZ","22XYYYYYYY","23NZZZZZZZ","23XXYYYYYZ","26WXXYYZZZ","2FIJKKLLLM","2GJKLLMMMMN","2GJKLLMMMN","2GJKLMMMMN","2JNOPPQQQR","2KOPQQRRRRS","2KOPQQRRRS","2KOPQRRRRS","2KOQQRRRRS","2LPQRRSSSSST","2LPQRRSSSST","2LPQRRSSST","2LPQRSSSSST","2LPQRSSSST","2LPQSSSSST","2OTUVVWWWX","2OUVVWWWWX","2PUVWWXXXXY","2PUVWWXXXY","2PUVWXXXXY","2PUWWXXXXY","2QVWXXYYYYYZ","2QVWXXYYYYZ","2QVWXXYYYZ","2QVWXYYYYYZ","2QVWXYYYYZ","2QVWYYYYYZ","2QVXXYYYYYZ","2QVXXYYYYZ","2QVXYYYYYZ","3GJKLLMMMN","3HKLMMNNNNO","3HKLMMNNNO","3HKLMNNNNO","3KOPQQRRRS","3LPQRRSSSST","3LPQRRSSST","3LPQRSSSST","3LPRRSSSST","3MQRSSTTTTTU","3MQRSSTTTTU","3MQRSSTTTU","3MQRSTTTTTU","3MQRSTTTTU","3MQRTTTTTU","3NXZZZZZZZ","3PUVWWXXXY","3PVWWXXXXY","3QVWXXYYYYZ","3QVWXXYYYZ","3QVWXYYYYZ","3QVXXYYYYZ","4HKLMMNNNO","4ILMNNOOOOP","4ILMNNOOOP","4ILMNOOOOP","4LPQRRSSST","4MQRSSTTTTU","4MQRSSTTTU","4MQRSTTTTU","4MQSSTTTTU","4NRSTTUUUUUV","4NRSTTUUUUV","4NRSTTUUUV","4NRSTUUUUUV","4NRSTUUUUV","4NRSUUUUUV","4QVWXXYYYZ","4QWXXYYYYZ","5ILMNNOOOP","5JMNOOPPPPQ","5JMNOOPPPQ","5JMNOPPPPQ","5MQRSSTTTU","5NRSTTUUUUV","5NRSTTUUUV","5NRSTUUUUV","5NRTTUUUUV","5OSTUUVVVVVW","5OSTUUVVVVW","5OSTUUVVVW","5OSTUVVVVVW","5OSTUVVVVW","5OSTVVVVVW","6JMNOOPPPQ","6KNOPPQQQQR","6KNOPPQQQR","6KNOPQQQQR","6LXZZZZZZZ","6LXZZZZZZZZ","6LZZZZZZZZ","6NRSTTUUUV","6OSTUUVVVVW","6OSTUUVVVW","6OSTUVVVVW","6OSUUVVVVW","6PTUVVWWWWWX","6PTUVVWWWWX","6PTUVVWWWX","6PTUVWWWWWX","6PTUVWWWWX","6PTUWWWWWX","7JZZZZZZZZ","7KNOPPQQQR","7LOPQQRRRRS","7LOPQQRRRS","7LOPQRRRRS","7OSTUUVVVW","7PTUVVWWWWX","7PTUVVWWWX","7PTUVWWWWX","7PTVVWWWWX","7QUVWWXXXXXY","7QUVWWXXXXY","7QUVWWXXXY","7QUVWXXXXXY","7QUVWXXXXY","7QUVXXXXXY","8LOPQQRRRS","8MPQRRSSSST","8MPQRRSSST","8MPQRSSSST","8PTUVVWWWX","8QUVWWXXXXY","8QUVWWXXXY","8QUVWXXXXY","8QUWWXXXXY","8RVWXXYYYYYZ","8RVWXXYYYYZ","8RVWXXYYYZ","8RVWXYYYYYZ","8RVWXYYYYZ","8RVWYYYYYZ","9MPQRRSSST","9NQRSSTTTTU","9NQRSSTTTU","9NQRSTTTTU","9QUVWWXXXY","9RVWXXYYYYZ","9RVWXXYYYZ","9RVWXYYYYZ","9RVXXYYYYZ","9TUXZZZZZZ","9TUXZZZZZZZ","9TUXZZZZZZZZ","ANQRSSTTTU","AORSTTUUUUV","AORSTTUUUV","AORSTUUUUV","ARVWXXYYYZ","ATUXZZZZZZ","ATUXZZZZZZZ","ATUZZZZZZZ","BORSTTUUUV","BPSTUUVVVVW","BPSTUUVVVW","BPSTUVVVVW","BTUXZZZZZZ","CPSTUUVVVW","CQTUVVWWWWX","CQTUVVWWWX","CQTUVWWWWX","DJZZZZZZZZ","DJZZZZZZZZZ","DQTUVVWWWX","DRUVWWXXXXY","DRUVWWXXXY","DRUVWXXXXY","ERUVWWXXXY","ESVWXXYYYYZ","ESVWXXYYYZ","ESVWXYYYYZ","FIZZZZZZZZ","FSVWXXYYYZ","FUUYYZZZZZ","FUUYYZZZZZZ","FUUYZZZZZZ","GUUYYZZZZZ","1111111111111Y","111111111111Y","111111111112X","11RVYYZZZZZZZ","11RVYYZZZZZZZZ","11RVYZZZZZZZZ","11XXYYYYYYYYY","1QVWXXYYYYYYZ"};
    
    int prefixesCount = 0;
    int patternsCount = 0;

	SumOfDigits sod = null;
	
	int pattsCount = 0;
	
	public static int getInt(char c) {
		if(c >= '0' && c <= '9') return c - '0';
		return c - 'A' + 10;
	}
	
	private int getSumOfDigits(String prefix) {
		int answer = 0;
		for(int i = 0; i < prefix.length(); i++) {
			int currentDigit = prefix.charAt(i);
			if(currentDigit >= 'A') currentDigit = currentDigit - 'A' + 10; else currentDigit = currentDigit - '0';
			answer += currentDigit;
		}
		return answer;
	}

	private int getSumOfDigitsSquares(String prefix) {
		int answer = 0;
		for(int i = 0; i < prefix.length(); i++) {
			int currentDigit = prefix.charAt(i);
			if(currentDigit >= 'A') currentDigit = currentDigit - 'A' + 10; else currentDigit = currentDigit - '0';
			answer += currentDigit * currentDigit;
		}
		return answer;
	}
	
	private boolean isOk(String prefix, int symbol1, int c1, int symbol2, int c2, int s1, int s2) {
		StringBuilder answer = sod.findMinimumString(s1 + symbol1 * c1 + symbol2 * c2, s2 + symbol1 * symbol1 * c1 + symbol2 * symbol2 * c2);
		if(answer == null) return false;
		if(answer.length() == prefix.length() + c1 + c2 && (sod.countOfDigits[symbol1] == c1 && sod.countOfDigits[symbol2] == c2)) return true;
		return false;
	}

	private String getString(char c, int n) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < n; i++) {
			sb.append(c);
		}
		return sb.toString();
	}
		
	private void solve(int system) {
		if(system == 2) {
			System.out.println("1*"); return;
		}
		sod = new SumOfDigits(system);
		
		for(int i = 1; i <= 300; i++) {
			for(int j = i; j <= i * i && j <= 7000; j++) {
				StringBuilder answer = sod.findMinimumString(i, j);
				if(answer == null) continue;
				char lastSymbol = answer.charAt(answer.length() - 1);
				char predLastSymbol = (char)(lastSymbol - 1);
				if(lastSymbol == 'A') predLastSymbol = '9';
				int to = answer.indexOf(predLastSymbol + "");
				if(to == -1) to = answer.indexOf(lastSymbol + "");
				String prefix = answer.substring(0, to);
				prefixesSet.add(prefix);
			}
		}

		boolean[] oneStarFound = new boolean[64];

		for(String prefix: prefixesSet) {
			sumOfD[prefixesCount] = getSumOfDigits(prefix);
			sumOfD2[prefixesCount] = getSumOfDigitsSquares(prefix);
			prefixes[prefixesCount++] = prefix;
		}
		
		for(int k = 0; k < prefixesCount; k++) {
			String prefix = prefixes[k];
			int lastDigit = 0;
			if(prefix.length() > 0) {
				lastDigit = prefix.charAt(prefix.length() - 1);
				if(lastDigit >= 'A') lastDigit = lastDigit - 'A' + 10; else lastDigit = lastDigit - '0';
			}
			
			for(int i = lastDigit + 2; i < system; i++) {
				char symbol1 = SumOfDigits.digits[i];
				char symbol2 = SumOfDigits.digits[i - 1];

				int mxConst = 12;
				int limit = 15;
				if(system == 34) {
					mxConst = 11;
					limit = 13;
				}
				if(system == 35) {
					limit = 10;
				}
				if(system == 36) {
					mxConst = 10;
					limit = 10;
				}
				boolean twoStarsFound = false;
				java.util.Arrays.fill(oneStarFound, false);
				for(int c1 = mxConst; c1 >= 1; c1--) {
					for(int c2 = mxConst; c2 >= 0; c2--) {
						if(c1 + c2 == 0) continue;
						if(c1 != mxConst && c2 != mxConst && prefix.length() + c1 + c2 >= limit) continue;
						if(oneStarFound[c2]) continue;
						if(isOk(prefix, i - 1, c2, i, c1, sumOfD[k], sumOfD2[k])) {
							if(c1 == mxConst && c2 == mxConst) {
								String currentPattern = prefix + symbol2 + "*" + symbol1 + "*";
								patternsSet.add(currentPattern);
								twoStarsFound = true; break;
							}
							if(c1 == mxConst) {
								for(int cc = c2; cc >= 0; cc--) {
									String currentPattern = prefix + getString(symbol2, cc) + symbol1 + "*";
									if(patternsSet.contains(prefix + getString(symbol2, cc))) {
										patternsSet.remove(prefix + getString(symbol2, cc));
									}
									patternsSet.add(currentPattern);
								}
								oneStarFound[c2] = true;
								twoStarsFound = true;
								break;
							}
							if(c2 == mxConst) {
								for(int cc = c1; cc >= 1; cc--) {
									String currentPattern = prefix + symbol2 + "*" + getString(symbol1, cc);
									if(patternsSet.contains(prefix + getString(symbol1, cc))) {
										patternsSet.remove(prefix + getString(symbol1, cc));
									}
									patternsSet.add(currentPattern);
								}
								twoStarsFound = true;
								break;
							}
							if(c1 < mxConst && c2 < mxConst) {
								String currentPattern = prefix + getString(symbol2, c2) + getString(symbol1, c1);
								boolean found = false;
								for(int h = i - 1; h < system; h++) {
									if(patternsSet.contains(currentPattern + SumOfDigits.digits[h] + "*")) {
										found = true; break;
									}
								}
								if(!found) patternsSet.add(currentPattern);
							}
						}
					}
					if(twoStarsFound) break;
				}
				
			}
		}

		if(system == 35) {
			for(int i = 0; i < for35.length; i++) {
				patternsSet.add(for35[i]);
			}
		}
		if(system == 36) {
			for(int i = 0; i < for36.length; i++) {
				patternsSet.add(for36[i]);
			}
		}
		
		for(String pattern: patternsSet) {
			patterns[patternsCount++] = pattern;
		}
		
		java.util.Arrays.sort(patterns, 0, patternsCount);
		
		int mx = 0;
		for(int i = 0; i < patternsCount; i++) {
			System.out.println(patterns[i]);
			if(patterns[i].indexOf("*") == -1) {
				if(mx < patterns[i].length()) mx = patterns[i].length();
			}
		}
	}
	
	public static void main(String[] args) {
		SumOfDigits2 sod2 = new SumOfDigits2();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int system = scanner.nextInt();
		scanner.close();
		long currentTime = System.currentTimeMillis();
		sod2.solve(system);
	}
	
}

class SumOfDigits {

    public static char[] digits = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final int MX = 30050;
    
	private byte[] dp = new byte[1050 * MX];
	
	public final byte INF = Byte.MAX_VALUE;
	private int system = 10;
	
	public SumOfDigits(int system) {
		this.system = system;
	}
	
	private byte DP(int x, int y) {
		if(x > y || x < 0) return INF;
		
		if(x == 0) {
			if(y == 0) return 0;
			return INF;
		}
		
		if(dp[x * MX + y] != 0) return dp[x * MX + y];
		
		byte best = INF;
		for(int i = 1; i < system; i++) {
			byte h = DP(x - i, y - i * i);
			if(best > h + 1) {
				best = (byte)(h + 1);
			}
		}
		dp[x * MX + y] = best;
		return best;
	}
	
	StringBuilder sb = new StringBuilder();
	int[] countOfDigits = new int[64];
	private StringBuilder getAnswer(int x, int y) {
		java.util.Arrays.fill(countOfDigits, 0);
		sb.setLength(0);
		int p = 1;
		while(true) {
			for(int i = p; i < system; i++) {
				int nx = x - i;
				int ny = y - i * i;
				if(dp[x * MX + y] == dp[nx * MX + ny] + 1) {
					p = i; break;
				}
			}
			countOfDigits[p]++;
			sb.append(digits[p]);
			x = x - p;
			y = y - p * p;
			if(x == 0 && y == 0) break;
		}
		return sb;
	}
	
	public StringBuilder findMinimumString(int x, int y) {
		if(x > (system - 1) * 50 || x % 2 != y % 2) return null;
		int h = DP(x, y);
		if(h > 50) return null;
		return getAnswer(x, y);
	}

}