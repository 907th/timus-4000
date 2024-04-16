public class WeirdDuel {

	int[][] perm = new int[][] {
		{0, 1, 2},
		{0, 2, 1},
		{1, 0, 2},
		{1, 2, 0},
		{2, 0, 1},
		{2, 1, 0}
	};
	
	double[] p = new double[3];
	
	double[] getAnswerForTwo(int i1, int i2) {
		double[] answer = new double[] {0, 0, 0};
		if(i1 == 0) return new double[] {1, 0, 0};
		if(i2 == 0) {
			answer[i1] = p[i1];
			answer[i2] = 1 - p[i1];
		} else {
			double p1 = p[i1];
			double q1 = 1 - p[i1];
			double q2 = 1 - p[i2];
			answer[i1] = p1 / (1 - q1 * q2);
			answer[i2] = 1 - answer[i1];
		}
		return answer;
	}
	
	double[] DP(int mask, int[] order, int index) {

		if(mask != 7) {
			int currentIndex = index;
			int i1 = -1, i2 = -1;
			while(true) {
				if((mask & (1 << order[currentIndex])) != 0) {
					if(i1 == -1) {
						i1 = order[currentIndex];
					} else {
						i2 = order[currentIndex];
						break;
					}
				}
				currentIndex = (currentIndex + 1) % 3;
			}
			return getAnswerForTwo(i1, i2);
		}
		
		if(order[index] == 0) {
			if(p[1] > p[2]) {
				return DP(mask ^ 2, order, (index + 1) % 3);
			} else {
				return DP(mask ^ 4, order, (index + 1) % 3);
			}
		} else {
			double[] best = new double[] {-1, -1, -1};
			
			double[] h = new double[3];
			double[] h1 = DP(mask ^ 1, order, (index + 1) % 3); //Shot in first
			double[] h2 = DP(mask, order, (index + 1) % 3); //Missed in first
			
			for(int i = 0; i < 3; i++) {
				h1[i] = h1[i] * p[order[index]];
				h2[i] = h2[i] * (1 - p[order[index]]);
				h[i] = h1[i] + h2[i];
			}
			
			if(h[order[index]] > best[order[index]]) {
				for(int i = 0; i < 3; i++) {
					best[i] = h[i];
				}
			}
			
			if(order[index] != 1) {
				h1 = DP(mask ^ 2, order, (index + 1) % 3); //Shot in second
				h2 = DP(mask, order, (index + 1) % 3); //Missed in second
				for(int i = 0; i < 3; i++) {
					h1[i] = h1[i] * p[order[index]];
					h2[i] = h2[i] * (1 - p[order[index]]);
					h[i] = h1[i] + h2[i];
				}
				
				if(h[order[index]] > best[order[index]]) {
					for(int i = 0; i < 3; i++) {
						best[i] = h[i];
					}
				}
			}
			
			if(order[index] != 2) {
				h1 = DP(mask ^ 4, order, (index + 1) % 3); //Shot in third
				h2 = DP(mask, order, (index + 1) % 3); //Missed in third
				for(int i = 0; i < 3; i++) {
					h1[i] = h1[i] * p[order[index]];
					h2[i] = h2[i] * (1 - p[order[index]]);
					h[i] = h1[i] + h2[i];
				}
				
				if(h[order[index]] > best[order[index]]) {
					for(int i = 0; i < 3; i++) {
						best[i] = h[i];
					}
				}
			}
			
			h = DP(mask, order, (index + 1) % 3); //Shot in sky
			if(h[order[index]] > best[order[index]]) {
				for(int i = 0; i < 3; i++) {
					best[i] = h[i];
				}
			}
			
			return best;
		}
	}
	
	double[] solve(double p1, double p2, double p3) {
		p[0] = p1;
		p[1] = p2;
		p[2] = p3;
		double[] acc = new double[3];
		for(int i = 0; i < perm.length; i++) {
			double[] h = DP(7, perm[i], 0);
			for(int j = 0; j < 3; j++) {
				acc[j] += h[j];
			}
		}
		for(int j = 0; j < 3; j++) {
			acc[j] = acc[j] / 6.0;
		}
		return acc;
	}
	
	public static void main(String[] args) {
		WeirdDuel wd = new WeirdDuel();
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		double a = Double.parseDouble(scanner.next());
		double b = Double.parseDouble(scanner.next());
		double c = Double.parseDouble(scanner.next());
		scanner.close();
		double[] h = wd.solve(a, b, c);
		//double[] h = wd.solve(1, 0.8, 0.5);
		System.out.print(String.format("%.9f ", h[0]).replace(',', '.'));
		System.out.print(String.format("%.9f ", h[1]).replace(',', '.'));
		System.out.print(String.format("%.9f\n", h[2]).replace(',', '.'));
	}
	
}
