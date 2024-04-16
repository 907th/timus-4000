public class Hyphenation {

	int n;
	
	String[][] r = new String[1024][2];
	int m = 0;
	
	char[] buffer = new char[1024];
	int bufferSize = 0;
	
	int wordSize = 0;
	char[] word = new char[1024];
	
	java.io.PrintWriter pw;
	
	boolean isAlphabet(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}
	
	void addRule(String in) {
		r[m] = in.toLowerCase().split("-");
		m++;
	}
	
	void flushBuffer(boolean isLastLine) {
		for(int i = 0; i < bufferSize; i++) {
			pw.print(buffer[i]);
		}
		if(!isLastLine) pw.println();
		bufferSize = 0;
	}
	
	void addWordToBuffer() {
		for(int i = 0; i < wordSize; i++) {
			buffer[bufferSize++] = word[i];
		}
	}
	
	boolean couldApplyRule(int rule) {
		if(r[rule][0].length() + r[rule][1].length() > wordSize) return false;
		int k = 0;
		for(int i = r[rule][1].length() - 1; i >= 0; i--) {
			k++;
			if(Character.toLowerCase(word[wordSize - k]) != r[rule][1].charAt(i)) return false;
		}
		for(int i = r[rule][0].length() - 1; i >= 0; i--) {
			k++;
			if(Character.toLowerCase(word[wordSize - k]) != r[rule][0].charAt(i)) return false;
		}
		return true;
	}
	
	void processLine(String line, boolean isLastLine) {
		if(line.length() <= 40) {
			if(isLastLine) 
				pw.print(line); else
				pw.println(line);
		} else {
			for(int i = 0; i < line.length(); i++) {
				if(bufferSize == 40) flushBuffer(false);
				if(!isAlphabet(line.charAt(i))) {
					buffer[bufferSize++] = line.charAt(i);
					if(bufferSize == 40) flushBuffer(isLastLine && i == line.length() - 1);
				} else {
					wordSize = 0;
					while(i < line.length() && isAlphabet(line.charAt(i))) {
						word[wordSize++] = line.charAt(i);
						i++;
					}
					i--;
					if(bufferSize + wordSize < 40) {
						addWordToBuffer();
					} else
					if(bufferSize + wordSize == 40) {
						addWordToBuffer();
						flushBuffer(isLastLine && i == line.length() - 1);
					} else {
						
						int bestApplicableRule = -1;
						int minimalPart = 1000;
						for(int j = 0; j < m; j++) {
							if(couldApplyRule(j) && bufferSize + (wordSize - r[j][1].length()) + 1 <= 40) {
								if(minimalPart > r[j][1].length()) {
									minimalPart = r[j][1].length();
									bestApplicableRule = j;
								}
							}
						}
						if(bestApplicableRule == -1) {
							flushBuffer(false);
							for(int j = 0; j < wordSize; j++) {
								buffer[bufferSize++] = word[j];
							}
						} else {
							for(int j = 0; j < wordSize - r[bestApplicableRule][1].length(); j++) {
								buffer[bufferSize++] = word[j];
							}
							buffer[bufferSize++] = '-';
							flushBuffer(false);
							for(int j = 0; j < r[bestApplicableRule][1].length(); j++) {
								buffer[bufferSize++] = word[wordSize - r[bestApplicableRule][1].length() + j];
							}
						}
						
					}
				}
			}
		}
		if(bufferSize > 0) flushBuffer(isLastLine);
	}
	
	void solve() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);;
		pw = new java.io.PrintWriter(System.out);
		n = Integer.parseInt(scanner.nextLine().trim());
		for(int i = 0; i < n; i++) {
			String t = scanner.nextLine();
			addRule(t);
		}
		boolean hasNextLine = scanner.hasNextLine();
		while(hasNextLine) {
			String line = scanner.nextLine();
			hasNextLine = scanner.hasNextLine();
			processLine(line, !hasNextLine);
		}
		pw.flush();
		pw.close();
		scanner.close();
	}
	
	public static void main(String[] args) {
		Hyphenation h = new Hyphenation();
		h.solve();
	}
	
}
