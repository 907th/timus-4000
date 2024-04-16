import java.math.BigInteger;

public class VasyasFather {

	BigInteger[] dp = new BigInteger[64];
	
	long[][] sumsCount = new long[64][64];
	int[] a = new int[64];
	int totalCount = 0;
	BigInteger currentAnswer = BigInteger.ZERO;
	
	int[] f = new int[64];
	
	BigInteger C(BigInteger n, long k) {
		if(BigInteger.valueOf(k).compareTo(n) > 0) return BigInteger.ZERO;
		BigInteger answer = BigInteger.ONE;
		BigInteger fact = BigInteger.ONE;
		for(int i = 0; i < k; i++) {
			answer = answer.multiply(n.subtract(BigInteger.valueOf(i)));
			fact = fact.multiply(BigInteger.valueOf(i + 1));
		}
		answer = answer.divide(fact);
		
		return answer;
	}
	
	void process(int[] a, int n) {
		java.util.Arrays.fill(f, 0);
		for(int i = 0; i < n; i++) {
			f[a[i]]++;
		}
		
		BigInteger localAnswer = BigInteger.ONE;
		for(int i = 1; i <= 50; i++) {
			if(f[i] > 0) {
				BigInteger cycleAnswer = BigInteger.ZERO;
				for(int j = 1; j <= f[i]; j++) {
					BigInteger h = C(dp[i], j);
					if(!h.equals(BigInteger.ZERO)) {
						cycleAnswer = cycleAnswer.add(h.multiply(BigInteger.valueOf(sumsCount[f[i]][j])).abs());
					}
				}
				localAnswer = localAnswer.multiply(cycleAnswer);
			}
		}
		currentAnswer = currentAnswer.add(localAnswer);
	}
	
	void generateSum(int sum, int last, int current) {
		if(sum < last && sum != 0) return;
		
		if(sum == 0) {
			process(a, current);
			return;
		}

		for(int i = last; i <= sum; i++) {
			a[current] = i;
			generateSum(sum - i, i, current + 1);
		}
	}
	
	long fact(int x) {
		long ans = 1;
		for(int i = 1; i <= x; i++) {
			ans = ans * i;
		}
		return ans;
	}
	
	long calc(int[] a, int current) {
		java.util.Arrays.fill(f, 0);
		for(int i = 0; i < current; i++) {
			f[a[i]]++;
		}
		long divisor = 1;
		for(int i = 1; i <= 50; i++) {
			if(f[i] > 0) {
				divisor = divisor * fact(f[i]);
			}
		}
		return fact(current) / divisor;
	}
	
	void generateAnotherSum(int sum, int last, int current, int originalSum) {
		if(sum < last && sum != 0) return;
		
		if(sum == 0) {
			sumsCount[originalSum][current] += calc(a, current);
			return;
		}

		for(int i = last; i <= sum; i++) {
			a[current] = i;
			generateAnotherSum(sum - i, i, current + 1, originalSum);
		}
	}
	
	void generate(int count) {

		for(int i = 1; i <= 50; i++) {
			generateAnotherSum(i, 1, 0, i);
		}
		
		dp[1] = BigInteger.ONE;
		System.out.println("ans[" + 1 + "] = \"" + dp[1] + "\";");
		for(int i = 2; i <= count; i++) {
			currentAnswer = BigInteger.ZERO;
			generateSum(i - 1, 1, 0);
			dp[i] = currentAnswer;
			System.out.println("ans[" + i + "] = \"" + dp[i] + "\";");
		}
	}
	
	String[] ans = new String[64];
	
	void prebuild(int x) {
		ans[1] = "1";
		ans[2] = "1";
		ans[3] = "2";
		ans[4] = "4";
		ans[5] = "9";
		ans[6] = "20";
		ans[7] = "48";
		ans[8] = "115";
		ans[9] = "286";
		ans[10] = "719";
		ans[11] = "1842";
		ans[12] = "4766";
		ans[13] = "12486";
		ans[14] = "32973";
		ans[15] = "87811";
		ans[16] = "235381";
		ans[17] = "634847";
		ans[18] = "1721159";
		ans[19] = "4688676";
		ans[20] = "12826228";
		ans[21] = "35221832";
		ans[22] = "97055181";
		ans[23] = "268282855";
		ans[24] = "743724984";
		ans[25] = "2067174645";
		ans[26] = "5759636510";
		ans[27] = "16083734329";
		ans[28] = "45007066269";
		ans[29] = "126186554308";
		ans[30] = "354426847597";
		ans[31] = "997171512998";
		ans[32] = "2809934352700";
		ans[33] = "7929819784355";
		ans[34] = "22409533673568";
		ans[35] = "63411730258053";
		ans[36] = "179655930440464";
		ans[37] = "509588049810620";
		ans[38] = "1447023384581029";
		ans[39] = "4113254119923150";
		ans[40] = "11703780079612453";
		ans[41] = "33333125878283632";
		ans[42] = "95020085893954917";
		ans[43] = "271097737169671824";
		ans[44] = "774088023431472074";
		ans[45] = "2212039245722726118";
		ans[46] = "6325843306177425928";
		ans[47] = "18103111141539779470";
		ans[48] = "51842285219378800562";
		ans[49] = "148558992149369434381";
		ans[50] = "425976989835141038353";
		System.out.println(ans[x]);
	}
	
	public static void main(String[] args) {
		VasyasFather vf = new VasyasFather();
		//vf.generate(50);
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int x = scanner.nextInt();
		scanner.close();
		vf.prebuild(x);
	}
	
}