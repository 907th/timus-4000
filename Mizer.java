import java.util.Random;

public class Mizer {

	private final char[] suits = new char[] {'C', 'D', 'H', 'S'};
	private final int maxMask = (1 << 10) - 1;
	
	private int[] suitsMap = new int[256];
	
	private byte[] minBit = new byte[1 << 10];
	private byte[] maxBit = new byte[1 << 10];
	private byte[] bitsCount = new byte[1 << 10];
	
	public Mizer() {
		for(int i = 0; i < playersCards.length; i++) {
			playersCards[i] = new PlayerCard();
		}
	}
	
	class PlayerCard {
		char suit;
		int val;
		int player;
		int index;
	}
	
	class HashByArray {
		
		private final int SIZE = 1 << 18;
		private final int HASH_SIZE = 65537;
		
		int[] table = new int[HASH_SIZE];
		int[] nodeKey = new int[SIZE];
		int[] nodeNext = new int[SIZE];
		byte[] nodeVal = new byte[SIZE];
		int pointer = 0;
		
		public HashByArray() {
			for(int i = 0; i < table.length; i++) {
				table[i] = -1;
			}
		}
		
		public int get(int key) {
			int index = key % HASH_SIZE;
			if(index < 0) index += HASH_SIZE;
			for(int p = table[index]; p != -1; p = nodeNext[p]) {
				if(nodeKey[p] == key) return nodeVal[p];
			}
			return -1;
		}
		
		public void add(int key, int val) {
			int index = key % HASH_SIZE;
			if(index < 0) index += HASH_SIZE;
			nodeKey[pointer] = key;
			nodeNext[pointer] = table[index];
			nodeVal[pointer] = (byte)val;
			table[index] = pointer;
			pointer++;
		}
		
	}
		
	class Card implements Comparable<Card> {

		char suit;
		int val;
		
		@Override
		public int compareTo(Card o) {
			if(suit == o.suit) return val - o.val;
			return suit - o.suit;
		}
		
		public boolean equals(Card o) {
			return o.suit == suit && o.val == val;
		}
		
	}
	
	class Player {
		
		Card[] cards = new Card[10];
		
		int[] cardsMasks = new int[4];
		
		int[] ctr = new int[10];

		public Player() {
			for(int i = 0; i < 10; i++) {
				cards[i] = new Card();
			}
		}
		
		public void sortCards() {
			java.util.Arrays.sort(cards);
			for(int j = 0; j < 4; j++) {
				for(int i = 0; i < 10; i++) {
					if(cards[i].suit == suits[j]) cardsMasks[j] |= 1 << i;
				}
			}
		}
		
		public int[] getCardsToMove(Card c, int mask) {
			int count = getCardsCount(c.suit, mask);
			int[] toReturn = null;
			if(count == 0) {
				int ctrCount = 0;
				for(int i = 9; i >= 0; i--) {
					if(((1 << i) & mask) != 0) continue;
					ctr[ctrCount++] = i;
				}
				toReturn = new int[ctrCount];
				for(int i = 0; i < ctrCount; i++) {
					toReturn[i] = ctr[i];
				}
			} else {
				int[] cardsBySuit = getAllCardsBySuit(c.suit, mask);
				return cardsBySuit;
			}
			return toReturn;
		}
	
		public int[] getCardsByMizer(Card c, int mask) {
			
			int maskOfCards = maxMask ^ mask;
			
			int countOfCurrentSuits = 
					((maskOfCards & cardsMasks[0]) != 0 ? 1 : 0) + 
					((maskOfCards & cardsMasks[1]) != 0 ? 1 : 0) + 
					((maskOfCards & cardsMasks[2]) != 0 ? 1 : 0) + 
					((maskOfCards & cardsMasks[3]) != 0 ? 1 : 0);
			
			int[] cardsOfSameSuit = getAllCardsBySuit(c.suit, mask);
			if(cardsOfSameSuit.length != 0) return cardsOfSameSuit;
			
			int[] toReturn = new int[countOfCurrentSuits];
			int suitesCount = 0;
			for(int i = 9; i >= 0; i--) {
				if(((1 << i) & mask) != 0) continue;
				boolean found = false;
				for(int j = 0; j < suitesCount; j++) {
					if(cards[toReturn[j]].suit == cards[i].suit) {
						found = true; break;
					}
				}
				if(!found) {
					toReturn[suitesCount++] = i;
				}
			}
			return toReturn;
		}
		
		public int[] getCardsByMizerBest(Card c, int mask) {
			int maskOfCards = maxMask ^ mask;
			
			int countOfCurrentSuits = 
					((maskOfCards & cardsMasks[0]) != 0 ? 1 : 0) + 
					((maskOfCards & cardsMasks[1]) != 0 ? 1 : 0) + 
					((maskOfCards & cardsMasks[2]) != 0 ? 1 : 0) + 
					((maskOfCards & cardsMasks[3]) != 0 ? 1 : 0);
			
			int[] toReturn = new int[countOfCurrentSuits];
			int suitesCount = 0;
			for(int i = 9; i >= 0; i--) {
				if(((1 << i) & mask) != 0) continue;
				if(c.suit == cards[i].suit) {
					if(cards[i].val < c.val) return new int[] {i};
				} else {
					boolean found = false;
					for(int j = 0; j < suitesCount; j++) {
						if(cards[toReturn[j]].suit == cards[i].suit) {
							found = true; break;
						}
					}
					if(!found) {
						toReturn[suitesCount++] = i;
					}
				}
			}
			return toReturn;
		}
		
		public int getMinCardBySuit(char s, int mask) {
			mask = (maxMask ^ mask);
			if((cardsMasks[suitsMap[s]] & mask) == 0) return 0;
			return cards[minBit[cardsMasks[suitsMap[s]] & mask]].val;
		}
		
		public int getMaxCardBySuit(char s, int mask) {
			mask = (maxMask ^ mask);
			if((cardsMasks[suitsMap[s]] & mask) == 0) return 0;
			return cards[maxBit[cardsMasks[suitsMap[s]] & mask]].val;
		}
		
		public int getMaxCardIndexBySuit(char s, int mask) {
			mask = (maxMask ^ mask);
			if((cardsMasks[suitsMap[s]] & mask) == 0) return -1;
			return maxBit[cardsMasks[suitsMap[s]] & mask];
		}
		
		public int getMinCardIndexBySuit(char s, int mask) {
			mask = (maxMask ^ mask);
			if((cardsMasks[suitsMap[s]] & mask) == 0) return -1;
			return minBit[cardsMasks[suitsMap[s]] & mask];
		}
		
		public int[] getAllCardsBySuit(char s, int mask) {
			int count = 0;
			for(int i = 0; i < 10; i++) {
				if(((1 << i) & mask) != 0) continue;
				if(cards[i].suit == s) count++;
			}
			int[] toReturn = new int[count];
			count = 0;
			for(int i = 0; i < 10; i++) {
				if(((1 << i) & mask) != 0) continue;
				if(cards[i].suit == s) toReturn[count++] = i;
			}
			return toReturn;
		}
		
		public int getCardsCount(char s, int mask) {
			mask = (maxMask ^ mask);
			return bitsCount[cardsMasks[suitsMap[s]] & mask];
		}
		
		public int getCardsCount(int mask) {
			mask = (maxMask ^ mask);
			return bitsCount[mask];
		}
		
		public int getCardsMask(char st, int mask) {
			int answer = 0;
			for(int i = 0; i < 10; i++) {
				if(((1 << i) & mask) != 0) continue;
				if(cards[i].suit != st) continue;
				answer |= (1 << (cards[i].val - 7));
			}
			return answer;
		}
		
		public void printCards(int mask) {
			for(int i = 0; i < 10; i++) {
				if(((1 << i) & mask) != 0) continue;
				System.out.print(cards[i].toString() + " ");
			}
			System.out.println();
		}
		
	}

	HashByArray st = new HashByArray();
	
	Player[] players = new Player[3];
	
	private void parseCards(String cards, int index) {
		players[index] = new Player();
		String[] temp = cards.split(" ");
		for(int i = 0; i < temp.length; i++) {
			players[index].cards[i].suit = temp[i].charAt(temp[i].length() - 1);
			if(temp[i].charAt(0) == '1') players[index].cards[i].val = 10;
			if(temp[i].charAt(0) == '7') players[index].cards[i].val = 7;
			if(temp[i].charAt(0) == '8') players[index].cards[i].val = 8;
			if(temp[i].charAt(0) == '9') players[index].cards[i].val = 9;
			if(temp[i].charAt(0) == 'J') players[index].cards[i].val = 11;
			if(temp[i].charAt(0) == 'Q') players[index].cards[i].val = 12;
			if(temp[i].charAt(0) == 'K') players[index].cards[i].val = 13;
			if(temp[i].charAt(0) == 'A') players[index].cards[i].val = 14;
		}
		players[index].sortCards();
	}

	int playersCardsCount = 0;
	PlayerCard[] playersCards = new PlayerCard[30];
	int currentCardsCount = 0;
	
	private Card getCardMin(Card a, Card b) {
		if(a == null) return b; if(b == null) return a;
		return a.compareTo(b) < 0 ? a : b;
	}
	
	private void getAllCardsFromPlayers(int[] mask) {
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		playersCardsCount = 0;
		int p = 0, ind = 0;
		while(p1 < 10 || p2 < 10 || p3 < 10) {
			Card minCard = null;
			
			while(((mask[0] & (1 << p1)) != 0) && p1 < 10) p1++;
			while(((mask[1] & (1 << p2)) != 0) && p2 < 10) p2++;
			while(((mask[2] & (1 << p3)) != 0) && p3 < 10) p3++;
			
			if(p1 < 10) minCard = getCardMin(minCard, players[0].cards[p1]);
			if(p2 < 10) minCard = getCardMin(minCard, players[1].cards[p2]);
			if(p3 < 10) minCard = getCardMin(minCard, players[2].cards[p3]);
			
			if(minCard == null) {
				break;
			}
			
			if(p1 < 10 && minCard.equals(players[0].cards[p1])) {
				ind = p1;
				p = 0;
				p1++;
			}
			if(p2 < 10 && minCard.equals(players[1].cards[p2])) {
				ind = p2;
				p = 1;
				p2++;
			}
			if(p3 < 10 && minCard.equals(players[2].cards[p3])) {
				ind = p3;
				p = 2;
				p3++;
			}
			
			playersCards[playersCardsCount].suit = minCard.suit;
			playersCards[playersCardsCount].val = minCard.val;
			playersCards[playersCardsCount].player = p;
			playersCards[playersCardsCount].index = ind;
			playersCardsCount++;
		}
	}
	
	PlayerCard[] allPlayersCards = new PlayerCard[30];
	public void preGenerate() {
		getAllCardsFromPlayers(new int[] {0, 0, 0});
		for(int i = 0; i < playersCardsCount; i++) {
			allPlayersCards[i] = new PlayerCard();
			allPlayersCards[i].index = playersCards[i].index;
			allPlayersCards[i].player = playersCards[i].player;
			allPlayersCards[i].suit = playersCards[i].suit;
			allPlayersCards[i].val = playersCards[i].val;
		}
	}
	
	private void init(String cardsFirst, String cardsSecond, String cardsThird) {
		parseCards(cardsFirst, 0);
		parseCards(cardsSecond, 1);
		parseCards(cardsThird, 2);
		preGenerate();
	}

	private boolean alwaysMizer(int[] mask, int who) {
		for(int i = 0; i < 4; i++) {
			char st = suits[i];
			int v0 = players[0].getMaxCardBySuit(st, mask[0]);
			int v1 = players[1].getMinCardBySuit(st, mask[1]);
			int v2 = players[2].getMinCardBySuit(st, mask[2]);

			if(v0 == 0) continue;
			
			if(v1 == 0) v1 = 20;
			if(v2 == 0) v2 = 20;
			
			if(v0 > v1 || v0 > v2) return false;
		}		
		
		return true;
	}

	private boolean cantPlayMizer(int[] mask, int who) {
		for(int i = 0; i < 4; i++) {
			char st = suits[i];
			int v = players[who].getMinCardBySuit(st, mask[who]);
			if(v == 0) continue;
			
			int v0 = players[0].getMinCardBySuit(st, mask[0]);
			if(v0 == 0) continue;
			
			int va = players[3 - who].getMinCardBySuit(st, mask[3 - who]);
						
			if(v0 > v && v0 > va) return true;
		}
		return false;
	}

	private int[] normalizeMasks(int[] mask) {
		getAllCardsFromPlayers(mask);
		mask[0] = 0;
		mask[1] = 0;
		mask[2] = 0;
		int j = 0;
		for(int i = 0; i < playersCardsCount; i++) {
			for(; j < 30; j++) {
				if(
					playersCards[i].player == allPlayersCards[j].player && 
					playersCards[i].suit == allPlayersCards[j].suit) {
					mask[playersCards[i].player] |= (1 << allPlayersCards[j].index);
					j++;
					break;
				}
			}
		}
		mask[0] = maxMask ^ mask[0];
		mask[1] = maxMask ^ mask[1];
		mask[2] = maxMask ^ mask[2];
		
		getAllCardsFromPlayers(mask);
		return mask;
	}
	
	private int rec(int[] mask, int who) {

		mask = normalizeMasks(mask);
		
		int ky = mask[0] | (mask[1] << 10) | (mask[2] << 20) | ((who - 1) << 30);

		int hV = st.get(ky); 
		if(hV != -1) return hV;
		
		if(cantPlayMizer(mask, who)) {
			st.add(ky, 1);
			return 1;
		}
		
		if(alwaysMizer(mask, who)) {
			st.add(ky, 0);
			return 0;
		}
		
		for(int i = 0; i < 10; i++) {
			if(((1 << i) & mask[who]) != 0) continue;
			
			Card c = players[who].cards[i];

			if(who == 1) {
				int[] cardsToMove = players[3 - who].getCardsToMove(c, mask[3 - who]);
				for(int j = 0; j < cardsToMove.length; j++) {
					int[] cardsToMoveByMizer = null;
					int newWho = who;
					if(players[3 - who].cards[cardsToMove[j]].suit == c.suit) {
						if(c.compareTo(players[3 - who].cards[cardsToMove[j]]) < 0) {
							newWho = 3 - who;
							cardsToMoveByMizer = players[0].getCardsByMizerBest(players[3 - who].cards[cardsToMove[j]], mask[0]);
						} else 
							cardsToMoveByMizer = players[0].getCardsByMizerBest(c, mask[0]);
					} else {
						cardsToMoveByMizer = players[0].getCardsByMizerBest(c, mask[0]);
					}
					
					boolean canWin = false;
					for(int k = 0; k < cardsToMoveByMizer.length; k++) {
						int[] newMask = new int[] {mask[0], mask[1], mask[2]};
						
						newMask[0] = newMask[0] ^ (1 << cardsToMoveByMizer[k]);
						newMask[who] = newMask[who] ^ (1 << i);
						newMask[3 - who] = newMask[3 - who] ^ (1 << cardsToMove[j]);
						if(rec(newMask, newWho) == 0) {
							canWin = true; break;
						}
					}
					if(!canWin) {
						st.add(ky, 1);
						return 1;
					}
				}
			} else {
				int[] cardsToMoveByMizer = players[0].getCardsByMizer(c, mask[0]);
				int[] cardsToMove = players[3 - who].getCardsToMove(c, mask[3 - who]);

				boolean canWin = false;
				for(int j = 0; j < cardsToMoveByMizer.length; j++) {
					Card d = players[0].cards[cardsToMoveByMizer[j]];
					if(d.suit == c.suit) {
						if(d.val > c.val) {
							if(players[3 - who].cards[cardsToMove[0]].suit != c.suit) continue; else {
								int minCardValue = players[3 - who].getMinCardBySuit(c.suit, mask[3 - who]); 
								if(d.val > minCardValue) continue;
							}
						}
					}
					
					boolean canPlayMizer = true;
					int newWho = who;
					for(int k = 0; k < cardsToMove.length; k++) {
						Card q = players[3 - who].cards[cardsToMove[k]];
						
						if(c.suit == q.suit && c.val < q.val) newWho = 3 - who;
						
						int[] newMask = new int[] {mask[0], mask[1], mask[2]};
						newMask[0] = newMask[0] ^ (1 << cardsToMoveByMizer[j]);
						newMask[who] = newMask[who] ^ (1 << i);
						newMask[3 - who] = newMask[3 - who] ^ (1 << cardsToMove[k]);
						if(rec(newMask, newWho) == 1) {
							canPlayMizer = false; break;
						}
					}
					if(canPlayMizer) {
						canWin = true; break;
					}
				}
				if(!canWin) {
					st.add(ky, 1);
					return 1;
				}
				
			}
			
		}
		st.add(ky, 0);
		return 0;
	}
	
	private String solve(String cardsFirst, String cardsSecond, String cardsThird, int who) {
		for(int i = 0; i < 4; i++) {
			suitsMap[suits[i]] = i;
		}
		
		for(int i = 0; i < (1 << 10); i++) {
			for(int j = 0; j < 10; j++) {
				if((i & (1 << j)) != 0) {
					minBit[i] = (byte)j; break;
				}
			}
			for(int j = 9; j >= 0; j--) {
				if((i & (1 << j)) != 0) {
					maxBit[i] = (byte)j; break;
				}
			}
			bitsCount[i] = (byte)(bitsCount[i >> 1] + (i & 1));
		}
		init(cardsFirst, cardsSecond, cardsThird);
		
		if(who == 1) {
			for(int i = 0; i < 10; i++) {
				int[] fst = players[1].getAllCardsBySuit(players[0].cards[i].suit, 0);
				int[] snd = players[2].getAllCardsBySuit(players[0].cards[i].suit, 0);
				
				if(fst.length == 0 && snd.length == 0) continue;
				
				if(fst.length == 0 && snd.length != 0) {
					int minCard = players[2].getMinCardBySuit(players[0].cards[i].suit, 0);
					if(minCard < players[0].cards[i].val) continue;
				}
				
				if(fst.length != 0 && snd.length == 0) {
					int minCard = players[1].getMinCardBySuit(players[0].cards[i].suit, 0);
					if(minCard < players[0].cards[i].val) continue;
				}

				if(fst.length != 0 && snd.length != 0) {
					int minCard2 = players[2].getMinCardBySuit(players[0].cards[i].suit, 0);
					int minCard1 = players[1].getMinCardBySuit(players[0].cards[i].suit, 0);
					if(minCard2 < players[0].cards[i].val && minCard1 < players[0].cards[i].val) continue;
				}

				boolean fstEmpty = fst.length == 0;
				boolean sndEmpty = snd.length == 0;
				if(fst.length == 0) {
					fst = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
				}
				if(snd.length == 0) {
					snd = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
				}
				
				boolean failed = false;
				for(int j = 0; j < fst.length; j++) {
					for(int k = 0; k < snd.length; k++) {
						if(fstEmpty) {
							if(players[2].cards[snd[k]].val < players[0].cards[i].val) {
								failed = true; break;
							}
							who = 2;
						}
						if(sndEmpty) {
							if(players[1].cards[fst[j]].val < players[0].cards[i].val) {
								failed = true; break;
							}
							who = 1;
						}
						if(!fstEmpty && !sndEmpty) {
							if( players[2].cards[snd[k]].val > players[0].cards[i].val &&
								players[2].cards[snd[k]].val > players[1].cards[fst[j]].val
								) who = 2; else
							if( players[1].cards[fst[j]].val > players[0].cards[i].val &&
								players[1].cards[fst[j]].val > players[2].cards[snd[k]].val
								) who = 1; else {
									failed = true; break;
								}
						}
						
						int h = rec(new int[] {1 << i, 1 << fst[j], 1 << snd[k]}, who);
						if(h == 1) {
							failed = true;
							break;
						}
					}
					if(failed) break;
				}
				if(failed) continue;
				return ";)";
			}
		} else {
			int h = rec(new int[] {0, 0, 0}, who - 1);
			if(h == 0) {
				return ";)";
			}
		}
		return ";(";
	}
	
	public static void main(String[] args) {
		Mizer m = new Mizer();
		java.util.Scanner s = new java.util.Scanner(System.in);
		String c1 = s.nextLine(); String c2 = s.nextLine(); String c3 = s.nextLine(); int w = s.nextInt();
		s.close();		
		String h = m.solve(c1, c2, c3, w);
		System.out.println(h);
	}
	
}