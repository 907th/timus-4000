import java.util.Scanner;

public class RevolutionCup {

	public int g, t;
	private java.util.HashMap<String, Integer> parties = new java.util.HashMap<String, Integer>();
	private int partiesCount = 0;
	private int[] pCount = new int[100 * 100];

	private int[][] originalTable = new int[100][100];
	private String[][] originalNames = new String[100][100];
	
	public int getPartyId(String partyName) {
		if(parties.containsKey(partyName)) {
			return parties.get(partyName);
		} else {
			parties.put(partyName, partiesCount++);
			return partiesCount - 1;
		}
	}
	
	private int[] order;
	private int[] f;
	private int[] h;
	private int partiesLeft;
	private int[] match;
	
	private boolean DFS(int x, int[][] p, int fill) {
		f[x] = fill;
		for(int i = 0; i < t; i++) {
			if(p[i][x] <= 0) continue;
			if(match[i] == -1) {
				match[i] = x;
				return true;
			} else
			if(f[match[i]] != fill && DFS(match[i], p, fill)) {
				match[i] = x;
				return true;
			}
		}
		return false;
	}
	
	private int[] getMatching(int groupsLeft, int[][] p) {
		match = new int[t];
		java.util.Arrays.fill(match, -1);
		partiesLeft = 0;
		int count = 0;
		for(int i = 0; i < partiesCount; i++) {
			if(pCount[i] == groupsLeft) {
				order[partiesLeft++] = i;
			}
		}

		java.util.Arrays.fill(f, 0);
		for(int i = 0; i < partiesLeft; i++) {
			if(DFS(order[i], p, i + 1)) count++;
		}
		
		java.util.Arrays.fill(h, 0);
		for(int i = 0; i < partiesCount; i++) {
			if(0 < pCount[i] && pCount[i] < groupsLeft) {
				for(int j = 0; j < t; j++) {
					if(match[j] == -1 && p[j][i] > 0) {
						match[j] = i;
						h[i] = 1;
						count++;
						break;
					}
				}
			}
		}
		
		if(count < t) {
			java.util.Arrays.fill(f, 0);
			for(int i = 0; i < partiesCount; i++) {
				if(0 < pCount[i] && pCount[i] < groupsLeft && h[i] != 1) {
					if(DFS(i, p, i + 1)) count++;
					if(count == t) break;
				}
			}
		}
		return match;
	}
	
	public int[][] solve(int[][] p) {
		order = new int[2 * partiesCount];
		f = new int[2 * partiesCount];
		h = new int[2 * partiesCount];
		int[][] sol = new int[g][];
		for(int i = 0; i < g; i++) {
			int[] matching = getMatching(g - i, p);
			sol[i] = matching;
			for(int j = 0; j < t; j++) {
				p[j][matching[j]]--;
				pCount[matching[j]]--;
			}
		}
		return sol;
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		int g = s.nextInt();
		int t = s.nextInt();
		
		RevolutionCup rc = new RevolutionCup();
		rc.g = g;
		rc.t = t;
		for(int i = 0; i < t; i++) {
			for(int j = 0; j < g; j++) {
				String name = s.next();
				String party = s.next();
				int partyId = rc.getPartyId(party);
				rc.originalTable[i][j] = partyId;
				rc.originalNames[i][j] = name;
				rc.pCount[partyId]++;
				if(rc.pCount[partyId] > g) {
					System.out.println("No");
					s.close();
					return;
				}
				
			}
		}

		int[][] p = new int[t][rc.partiesCount];
		for(int i = 0; i < t; i++) {
			for(int j = 0; j < g; j++) {
				p[i][rc.originalTable[i][j]]++;
			}
		}
		int[][] solution = rc.solve(p);
		System.out.println("Yes");
		for(int i = 0; i < g; i++) {
			System.out.println();
			for(int j = 0; j < t; j++) {
				for(int k = 0; k < g; k++) {
					if(rc.originalTable[j][k] == solution[i][j]) {
						rc.originalTable[j][k] = -1;
						System.out.println(rc.originalNames[j][k]);
						break;
					}
				}
			}
		}
		s.close();
	}
	
}
