public class DeansPyramid2 {
	
	class HashNode {
		State s;
		int value;
		int hashKey;
		HashNode next = null;
	}
	
	class HashStructure {
		
		public static final int HASH_SIZE = 1024 * 1024;
		
		HashNode[] hs = new HashNode[HASH_SIZE]; 
		
		public void put(State s, int x) {
			int ohc = s.hashCode();
			int ky = s.hashCode();
			if(ky < 0) ky += Integer.MAX_VALUE;
			ky = ky % HASH_SIZE;
			
			HashNode hn = new HashNode();
			hn.hashKey = ohc;
			hn.s = s;
			hn.value = x;
			hn.next = hs[ky];
			
			hs[ky] = hn;
		}
		
		public boolean containsKey(State s) {
			int ohc = s.hashCode();
			int ky = s.hashCode();
			if(ky < 0) ky += Integer.MAX_VALUE;
			ky = ky % HASH_SIZE;
			for(HashNode hn = hs[ky]; hn != null; hn = hn.next) {
				if(hn.hashKey == ohc) return true;
			}
			return false;
		}
		
		public int get(State s) {
			int ohc = s.hashCode();
			int ky = s.hashCode();
			if(ky < 0) ky += Integer.MAX_VALUE;
			ky = ky % HASH_SIZE;
			for(HashNode hn = hs[ky]; hn != null; hn = hn.next) {
				if(hn.hashKey == ohc) return hn.value;
			}
			return 1000000000;
		}
		
	}
	
	class State {
		
		public final static double eps = 1e-9;
		public final static int toInt = 10000;
		public final static int p1 = 107;
		public final static int p2 = 1709;
		public final static int p3 = 10771;
		public final static int p4 = 107101;
		
		double vx, vy;
		double x, y;
		
		int edge;
		
		State prev = null;
		
		@Override
		public int hashCode() {
			
			long ivx = (long)((vx + eps) * toInt);
			long ivy = (long)((vy + eps) * toInt);
			
			long ix = (long)((x + eps) * toInt);
			long iy = (long)((y + eps) * toInt);
			
			int hashCodeValue = (int)((ivx * p1 + ivy * p2 + ix * p3 + iy * p4 + edge) % Integer.MAX_VALUE);
			
			return hashCodeValue;
		}

		public State move(int toEdge, double angle, double len) {
			State newState = new State();
			newState.edge = toEdge;

			if(edge == 0) {
				angle = Math.PI / 2.0 * (toEdge - 1);
				
				newState.vx =  Math.cos(angle) * vx + Math.sin(angle) * vy;
				newState.vy = -Math.sin(angle) * vx + Math.cos(angle) * vy;

				newState.x = x + newState.vx * len;
				newState.y = y + newState.vy * len;
				
				double nvx =  Math.cos(Math.PI) * newState.vx + Math.sin(Math.PI) * newState.vy;
				double nvy = -Math.sin(Math.PI) * newState.vx + Math.cos(Math.PI) * newState.vy;
				
				newState.vx = nvx;
				newState.vy = nvy;
				
			} else
			if(toEdge == 0) {
				newState.x = x + vx * len;
				newState.y = y + vy * len;
				
				double nvx =  Math.cos(Math.PI) * vx + Math.sin(Math.PI) * vy;
				double nvy = -Math.sin(Math.PI) * vx + Math.cos(Math.PI) * vy;

				angle = -Math.PI / 2.0 * (edge - 1);
				
				newState.vx =  Math.cos(angle) * nvx + Math.sin(angle) * nvy;
				newState.vy = -Math.sin(angle) * nvx + Math.cos(angle) * nvy;
				
			} else {
				if(edge + 1 == toEdge || edge == 4 && toEdge == 1) angle = -angle;
				
				newState.vx =  Math.cos(angle) * vx + Math.sin(angle) * vy;
				newState.vy = -Math.sin(angle) * vx + Math.cos(angle) * vy;
				
				newState.x = x;
				newState.y = y;
			}
			
			newState.prev = this;

			return newState;
		}
		
		@Override
		public String toString() {
			return "{\"x\": \"" + x + "\", " + "\"y\": \"" + y + "\", " + "\"vx\": \"" + vx + "\", " + "\"vy\": \"" + vy + "\", " + "\"edge\": \"" + edge + "\"}";
		}
		
	}

	public static final int QUEUE_SIZE = 128 * 1024;
	
	State[] queue = new State[QUEUE_SIZE];
	int qs = 0;
	int qf = 0;
	int n;
	
	HashStructure d = new HashStructure();
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private void relax(State s, int currentDistance) {
		if(currentDistance == n) return;
		if(d.containsKey(s)) return;
		
		State currentNode = s;
		int count = 0;
		while(true) {
			if(currentNode.edge == 0 || currentNode == null) break;
			currentNode = currentNode.prev;
			count++;
		}
		
		if(count < 8) {
			d.put(s, currentDistance + 1);
			if(qf >= QUEUE_SIZE) qf = 0;
			queue[qf++] = s;
		}
	}
	
	public State solve(double a1, double a2, double x, double y, int n) {
		this.n = n;

		State initialState = new State();
		initialState.x = 0;
		initialState.y = 0;
		initialState.vx = 0;
		initialState.vy = 1;
		initialState.edge = 0;
		
		queue[qf++] = initialState;
		
		double angle = Math.acos((a1 * a1 - a2 * a2 - a2 * a2) / (- 2 * a2 * a2));
		double length = a2 * a2 * Math.sin(angle) / a1 + a1 / 2.0;
				
		d.put(initialState, 0);
		
		double bestValue = Double.MAX_VALUE;
		State bestAnswer = null;
		
		while(qs != qf) {
			State current = queue[qs++];
			if(qs >= QUEUE_SIZE) qs = 0;
			int stepsCount = d.get(current);
			
			if(current.edge == 0) {
				double h = distance(current.x, current.y, x, y);
				if(bestValue > h) {
					bestAnswer = current;
					bestValue = h;
				} else {
					if(h - bestValue > length) continue;
				}
			} else {
				double h = distance(current.x + current.vx * length, current.y + current.vy * length, x, y);
				if(h - bestValue > length - (a2 - a1 > 0.5 ? a1 : 0)) continue;
			}
			
			if(current.edge == 0) {
				relax(current.move(1, angle, length), stepsCount);
				relax(current.move(2, angle, length), stepsCount);
				relax(current.move(3, angle, length), stepsCount);
				relax(current.move(4, angle, length), stepsCount);
			} else {
				relax(current.move(current.edge == 1 ? 4 : (current.edge - 1), angle, length), stepsCount);
				relax(current.move(current.edge == 4 ? 1 : (current.edge + 1), angle, length), stepsCount);
				relax(current.move(0, angle, length), stepsCount);
			}
		}
		
		
		return bestAnswer;
	}
	
	public static void main(String[] args) {
		
		double a1, a2;
		double x, y;
		int n;
		
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		a1 = Double.parseDouble(scanner.next());
		a2 = Double.parseDouble(scanner.next());
		x = Double.parseDouble(scanner.next());
		y = Double.parseDouble(scanner.next());
		n = scanner.nextInt();
		scanner.close();
		//10.0 10.0 1000.0 0.0 5
		/*a1 = 10.0;
		a2 = 10.0;
		x = 200.0;
		y = 0.0;
		n = 32;*/
		
		long currentTime = System.currentTimeMillis();
		DeansPyramid2 dp2 = new DeansPyramid2();
		State h = dp2.solve(a1, a2, x, y, n);
		
		System.out.println(distance(x, y, h.x, h.y));
		int[] answer = new int[64];
		int answerSize = 0;
		State cur = h;
		while(true) {
			if(cur == null) break;
			answer[answerSize++] = cur.edge;
			cur = cur.prev;
		}
		System.out.println(answerSize - 1);
		for(int i = answerSize - 1; i >= 0; i--) {
			System.out.print(answer[i] + " ");
		}
		System.out.println();
		
		/*System.out.println("{");
		System.out.println("\"a1\": " + a1 + ",");
		System.out.println("\"a2\": " + a2 + ",");
		System.out.println("\"x\": " + x + ",");
		System.out.println("\"y\": " + y + ",");
		System.out.println("\"list\": [");
		while(true) {
			if(h == null) break;
			System.out.println(h);
			if(h.prev != null) System.out.print(",");
			h = h.prev;
		}
		System.out.println("]}");*/
		//System.out.println(System.currentTimeMillis() - currentTime);
	}
	
}