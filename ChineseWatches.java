public class ChineseWatches {

	int n;
	int[] count = new int[65536];
	
	int bestTime = 0;
	long best = 1000000000L * 1000000000L;
	long last = 0;
	
	int timeToInt(String time) {
		String[] t = time.split(":");
		int hours = Integer.parseInt(t[0]);
		int minutes = Integer.parseInt(t[1]);
		int seconds = Integer.parseInt(t[2]);
		if(hours == 12) hours = 0;
		return hours * 60 * 60 + minutes * 60 + seconds;
	}
	
	void init() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		for(int i = 0; i < n; i++) {
			String t = scanner.next();
			int x = timeToInt(t);
			count[x]++;
			last = x == 0 ? 0 : 12 * 60 * 60 - x;
		}
		best = last;
		scanner.close();
	}
	
	void solve() {
		init();
		int ct = 0;
		for(int i = 0; i < 12; i++) {
			for(int j = 0; j < 60; j++) {
				for(int k = 0; k < 60; k++) {
					if(ct != 0) {
						long current = last + n;
						current -= count[ct] * 12 * 60 * 60;
						
						if(best > current) {
							best = current;
							bestTime = ct;
						}
						
						last = current;
					}
					ct++;
				}
			}
		}
		int h = bestTime / (60 * 60);
		int m = (bestTime - h * 60 * 60) / 60;
		int s = bestTime % 60;
		if(h == 0) h = 12;
		System.out.printf("%d:%02d:%02d\n", h, m, s);
	}
	
	public static void main(String[] args) {
		ChineseWatches cw = new ChineseWatches();
		cw.solve();
	}
	
}
