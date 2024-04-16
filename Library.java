class Shelf {
	int x, y, l;
	int p1x, p2x;
}

class Measure implements Comparable<Measure> {
	
	int pegsToMove, shelfToCut;

	@Override
	public int compareTo(Measure o) {
		if(pegsToMove != o.pegsToMove) return pegsToMove - o.pegsToMove;
		return shelfToCut - o.shelfToCut;
	}
	
	Measure sum(Measure m) {
		Measure toReturn = new Measure();
		toReturn.pegsToMove = pegsToMove + m.pegsToMove;
		toReturn.shelfToCut = shelfToCut + m.shelfToCut;
		return toReturn;
	}
	
	void plus(int pegs, int size) {
		pegsToMove += pegs;
		shelfToCut += size;
	}
	
}

public class Library {

	int w, h, wt, ht, n;
	Shelf[] shelves;
	
	int isInside(int l, int r, int x) {
		return (l <= x && x <= r) ? 1 : 0;
	}
	
	int isStrictlyInside(double l, double r, double x) {
		return (l < x && x < r) ? 1 : 0;
	}
	
	Measure countOfPegsToMove(int left, int right, int p1x, int p2x, boolean canCutLeft, boolean canCutRight, int minWidth) {
		int len = right - left;
		int countOfPegsForLeftSide = 
				isInside(left, left + len / 2, p1x) + 
				isInside(left, left + len / 2, p2x);
		
		int countOfPegsForRightSide = 
				isInside(right - len / 2, right, p1x) + 
				isInside(right - len / 2, right, p2x);
		if( countOfPegsForLeftSide == 1 && 
				countOfPegsForRightSide == 1 && 
				len % 2 == 0 && 
				(left + len / 2 == p1x) || (left + len / 2 == p2x)) {
				countOfPegsForLeftSide--;
			}
	 

		int answer = 0;
		if(countOfPegsForLeftSide == 0) answer++;
		if(countOfPegsForRightSide == 0) answer++;
		
		int countOfPegsInside = isInside(left, right, p1x) + 
				isInside(left, right, p2x);
		
		if(answer != 0 && countOfPegsInside == 2) {
			if(canCutLeft) {
				int nLeft = left;
				int nRight = right;
				int toCutCount = 0;
				while(true) {
					toCutCount++;
					nLeft++;
					countOfPegsInside = isInside(nLeft, nRight, p1x) + 
							isInside(nLeft, nRight, p2x);
					if(countOfPegsInside != 2 || nRight - nLeft < minWidth) {
						break;
					}
					int nLen = nRight - nLeft;
					countOfPegsForLeftSide = 
							isInside(nLeft, nLeft + nLen / 2, p1x) + 
							isInside(nLeft, nLeft + nLen / 2, p2x);
					
					countOfPegsForRightSide = 
							isInside(nRight - nLen / 2, nRight, p1x) + 
							isInside(nRight - nLen / 2, nRight, p2x);
					if( countOfPegsForLeftSide == 1 && 
						countOfPegsForRightSide == 1 && 
						len % 2 == 0 && 
						(left + len / 2 == p1x) || (left + len / 2 == p2x)) {
							countOfPegsForLeftSide--;
					}
	
					answer = 0;
					if(countOfPegsForLeftSide == 0) answer++;
					if(countOfPegsForRightSide == 0) answer++;
					if(answer == 0) {
						Measure measure = new Measure();
						measure.pegsToMove = 0;
						measure.shelfToCut = toCutCount;
						return measure;
					}
				}
			}
			if(canCutRight) {
				int nLeft = left;
				int nRight = right;
				int toCutCount = 0;
				while(true) {
					toCutCount++;
					nRight--;
					countOfPegsInside = isInside(nLeft, nRight, p1x) + 
							isInside(nLeft, nRight, p2x);
					if(countOfPegsInside != 2 || nRight - nLeft < minWidth) {
						break;
					}
					int nLen = nRight - nLeft;
					countOfPegsForLeftSide = 
							isInside(nLeft, nLeft + nLen / 2, p1x) + 
							isInside(nLeft, nLeft + nLen / 2, p2x);
					
					countOfPegsForRightSide = 
							isInside(nRight - nLen / 2, nRight, p1x) + 
							isInside(nRight - nLen / 2, nRight, p2x);
					if( countOfPegsForLeftSide == 1 && 
						countOfPegsForRightSide == 1 && 
						len % 2 == 0 && 
						(left + len / 2 == p1x) || (left + len / 2 == p2x)) {
						countOfPegsForLeftSide--;
					}

					answer = 0;
					if(countOfPegsForLeftSide == 0) answer++;
					if(countOfPegsForRightSide == 0) answer++;
					if(answer == 0) {
						Measure measure = new Measure();
						measure.pegsToMove = 0;
						measure.shelfToCut = toCutCount;
						return measure;
					}
				}
			}
		}
		
		Measure measure = new Measure();
		measure.pegsToMove = answer;
		measure.shelfToCut = 0;
		
		return measure;
	}
	
	Measure calcMeasure(int l, int r, int y) {
		Measure maxMeasure = new Measure(); maxMeasure.pegsToMove = 1000; maxMeasure.shelfToCut = 1000 * 1000;
		
		Measure toReturn = new Measure();
		
		for(int i = 0; i < n; i++) {
			if(shelves[i].y >= y + ht) continue;
			if(shelves[i].y <= y) continue;
			if(shelves[i].x >= r || shelves[i].x + shelves[i].l <= l) continue;

			if( isStrictlyInside(l, r, shelves[i].x) == 1 || 
				isStrictlyInside(l, r, shelves[i].x + shelves[i].l) == 1 ||
				isStrictlyInside(shelves[i].x, shelves[i].x + shelves[i].l, (l + r) / 2.0) == 1) {
				
				Measure bestToDo = new Measure();
				bestToDo.pegsToMove = 2; bestToDo.shelfToCut = shelves[i].l;
				
				//Try move to left
				if(l > 0) {
					Measure currentMeasure = new Measure();
					int toCut = 0;
					int newX = l - shelves[i].l;
					if(newX < 0) {
						toCut = -newX;
						newX = 0;
					}
					Measure count = countOfPegsToMove(newX, l, shelves[i].p1x, shelves[i].p2x, true, true, 1);
					if(count.pegsToMove == 2) {
						currentMeasure.pegsToMove = 2;
						currentMeasure.shelfToCut = shelves[i].l;
					} else {
						currentMeasure.pegsToMove = count.pegsToMove;
						currentMeasure.shelfToCut = toCut + count.shelfToCut;
					}
					if(bestToDo.compareTo(currentMeasure) > 0) {
						bestToDo = currentMeasure;
					}
				}
				
				//Try move to right
				if(r < w) {
					Measure currentMeasure = new Measure();
					int toCut = 0;
					int newX = r + shelves[i].l;
					if(newX > w) {
						toCut = newX - w;
						newX = w;
					}
					Measure count = countOfPegsToMove(r, newX, shelves[i].p1x, shelves[i].p2x, true, true, 1);
					if(count.pegsToMove == 2) {
						currentMeasure.pegsToMove = 2;
						currentMeasure.shelfToCut = shelves[i].l;
					} else {
						currentMeasure.pegsToMove = count.pegsToMove;
						currentMeasure.shelfToCut = toCut + count.shelfToCut;
					}
					if(bestToDo.compareTo(currentMeasure) > 0) {
						bestToDo = currentMeasure;
					}
				}
				
				toReturn = toReturn.sum(bestToDo);
			}
			
		}
		
		return toReturn;
	}
	
	void solve(int w, int h, int wt, int ht, int n, Shelf[] shelves) {
		this.w = w; this.h = h;
		this.wt = wt; this.ht = ht;
		this.n = n;
		this.shelves = shelves;
		
		Measure best = new Measure();
		best.pegsToMove = 1000; best.shelfToCut = 1000000;
		
		for(int i = 0; i < n; i++) {
			if(shelves[i].y + ht <= h && shelves[i].l >= wt) {
				for(int nx = 0; nx <= w - wt; nx++) {
					if(shelves[i].x > nx) {
						//Move shelf to left
						int newLeftEdge = nx;
						Measure current = new Measure();

						Measure count = countOfPegsToMove(
								newLeftEdge, newLeftEdge + shelves[i].l,
								shelves[i].p1x, shelves[i].p2x,
								false, true, wt);
						if(count.pegsToMove == 2) continue;
						current.pegsToMove = count.pegsToMove;
						
						Measure m = calcMeasure(nx, nx + wt, shelves[i].y);
						m = m.sum(current);
						
						if(best.compareTo(m) > 0) {
							best = m;
						}
						
					} else
					if(shelves[i].x <= nx && nx + wt <= shelves[i].x + shelves[i].l) {
						//Do not move the shelf
						Measure m = calcMeasure(nx, nx + wt, shelves[i].y);
						if(best.compareTo(m) > 0) {
							best = m;
						}
					} else {
						//Move shelf to the right
						int newLeftEdge = nx + wt - shelves[i].l;
						Measure current = new Measure();
						
						Measure count = countOfPegsToMove(
								newLeftEdge, newLeftEdge + shelves[i].l, 
								shelves[i].p1x, shelves[i].p2x,
								true, false, wt);
						if(count.pegsToMove == 2) continue;
						current.pegsToMove = count.pegsToMove;

						Measure m = calcMeasure(nx, nx + wt, shelves[i].y);
						m = m.sum(current);
						if(best.compareTo(m) > 0) {
							best = m;
						}
					}
					
				}
			}
		}
		System.out.println(best.pegsToMove + " " + best.shelfToCut);
	}
	
	public static void main(String[] args) {
		Library l = new Library();
		
		int w, h;
		int wt, ht;
		int n;
		
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		w = scanner.nextInt();
		h = scanner.nextInt();
		
		wt = scanner.nextInt();
		ht = scanner.nextInt();
		
		n = scanner.nextInt();
		
		Shelf[] shelves = new Shelf[n];
		for(int i = 0; i < n; i++) {
			shelves[i] = new Shelf();
			shelves[i].y = scanner.nextInt();
			shelves[i].x = scanner.nextInt();
			shelves[i].l = scanner.nextInt();
			shelves[i].p1x = scanner.nextInt() + shelves[i].x;
			shelves[i].p2x = scanner.nextInt() + shelves[i].x;
		}
		
		scanner.close();
		
		l.solve(w, h, wt, ht, n, shelves);
		
	}
	
}
