class Block {
	int[] side = new int[2];
	int center;
	int type;
}

class Edge {
	int b1, b2;
	boolean isCenter1;
	boolean isCenter2;
}

public class Blocks {

	int edgesCount = 0;
	Edge[] edges = new Edge[1024];
	
	int n;
	Block[] blocks = new Block[1024];
	
	int answerIndex = -1;
	
	boolean isClash = false;
	
	int[] passed = new int[1024];
	double[] f = new double[1024];
	
	int getEdge(int from, int to, boolean isCenter) {
		for(int i = 0; i < edgesCount; i++) {
			if(edges[i].b1 == from && edges[i].b2 == to) {
				edges[i].isCenter1 = isCenter;
				return i;
			}
			if(edges[i].b2 == from && edges[i].b1 == to) {
				edges[i].isCenter2 = isCenter;
				return i;
			}
		}
		edges[edgesCount] = new Edge();
		edges[edgesCount].b1 = from;
		edges[edgesCount].b2 = to;
		edges[edgesCount].isCenter1 = isCenter;
		edgesCount++;
		return edgesCount - 1;
	}
	
	boolean isDifferent(double x, double y) {
		return Math.abs(x - y) > 1e-9;
	}
	
	void DFS(int x, int edgeIndex, double force) {
		if(f[edgeIndex] >= 0) {
			if(isDifferent(f[edgeIndex], force)) {
				isClash = true;
			}
		}
		f[edgeIndex] = force;
		if(passed[x] == 1 || x == 0) return;
		passed[x] = 1;
		if(x == n + 1 || x == n + 2) return;

		boolean isCenter = false;
		if(blocks[x].center == edgeIndex) {
			isCenter = true;
		}
		
		DFS(getBlock(blocks[x].center, x), blocks[x].center, isCenter ? force : force * 2);
		DFS(getBlock(blocks[x].side[0], x), blocks[x].side[0], isCenter ? force / 2.0 : force);
		DFS(getBlock(blocks[x].side[1], x), blocks[x].side[1], isCenter ? force / 2.0 : force);
	}
	
	int getBlock(int edgeIndex, int oppositeBlock) {
		if(edgeIndex == -1) {
			while(true);
		}
		Edge edge = edges[edgeIndex];
		if(edge.b1 == oppositeBlock) return edge.b2;
		return edge.b1;
	}
	
	void read() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		n = scanner.nextInt();
		
		for(int i = 0; i < f.length; i++) {
			f[i] = -1;
		}
		
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block();
			blocks[i].center = -1;
			blocks[i].side[0] = -1;
			blocks[i].side[1] = -1;
		}
		
		for(int i = 1; i <= n; i++) {
			int t, b1, b2, c;
			t = scanner.nextInt();
			b1 = scanner.nextInt();
			b2 = scanner.nextInt();
			c = scanner.nextInt();
			
			if(b1 == -1) b1 = n + 1;
			if(b1 == -2) b1 = n + 2;
			if(b2 == -1) b2 = n + 1;
			if(b2 == -2) b2 = n + 2;
			if( c == -1)  c = n + 1;
			if( c == -2)  c = n + 2;
			
			int s1 = getEdge(i, b1, false);
			int s2 = getEdge(i, b2, false);
			int ce = getEdge(i, c, true);
			
			if( c == n + 2) {
				answerIndex = ce;
				blocks[n + 2].center = ce;
			}
			if(b1 == n + 2) {
				answerIndex = s1;
				blocks[n + 2].center = s1;
			}
			if(b2 == n + 2) {
				answerIndex = s2;
				blocks[n + 2].center = s2;
			}
			
			if( c == n + 1) {
				blocks[n + 1].center = ce;
			}
			if(b1 == n + 1) {
				blocks[n + 1].center = s1;
			}
			if(b2 == n + 1) {
				blocks[n + 1].center = s2;
			}
			
			blocks[i].type = t;
			blocks[i].side[0] = s1;
			blocks[i].side[1] = s2;
			blocks[i].center = ce;
			
		}
		scanner.close();
	}
	
	void solve() {
		read();
		DFS(getBlock(blocks[n + 1].center, n + 1), blocks[n + 1].center, 1);
	
		boolean isAny = false;
		if(passed[n + 2] == 0) {
			isAny = true;
			DFS(getBlock(blocks[n + 2].center, n + 2), blocks[n + 2].center, 1);
		}
			
		if(isClash) {
			System.out.println("No solution");
			return;
		}
			
		if(isAny) {
			System.out.println("Any");
			return;
		}
			
		if(answerIndex == -1) {
			while(true);
		}
			
		System.out.println(f[answerIndex]);
	}
	
	public static void main(String[] args) {
		Blocks b = new Blocks();
		b.solve();
	}
	
}
