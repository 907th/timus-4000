import java.math.BigInteger;

public class Calculator {

	private boolean isDigit(char c) {
		return Character.isDigit(c);
	}
	
	private boolean isOperation(char c) {
		return c == '+' || c == '/' || c == '*';
	}
	
	String calculate(String input, int from, int to) {
		
		String[] tokens = new String[64];
		int tokensCount = 0;
		
		StringBuilder sb = new StringBuilder();
		for(int i = from; i < to; i++) {
			if(input.charAt(i) == '(') {
				int l = i + 1, r = 0;
				int bal = 0;
				while(true) {
					if(i >= to) {
						r = to; break;
					}
					if(input.charAt(i) == '(') bal++;
					if(input.charAt(i) == ')') bal--;
					if(bal == 0) {
						r = i;
						break;
					}
					i++;
				}
				sb.append(calculate(input, l, r));
			} else {
				sb.append(input.charAt(i));
			}
		}

		String currentToken = "";
		for(int i = 0; i < sb.length(); i++) {
			if(isOperation(sb.charAt(i))) {
				if(currentToken.length() > 0) {
					tokens[tokensCount++] = currentToken;
					currentToken = "";
				}
				if(i > 0 && isDigit(sb.charAt(i - 1))) {
					tokens[tokensCount++] = "C";
				}
				tokens[tokensCount++] = sb.charAt(i) + "";
			} else
			if(sb.charAt(i) == ';') {
				tokens[tokensCount++] = currentToken;
				currentToken = "";
			} else {
				currentToken = currentToken + sb.charAt(i);
			}
		}
		tokens[tokensCount++] = currentToken;		
		
		for(int i = tokensCount - 1; i >= 0; i--) {
			if(tokens[i] == null || tokens[i].length() == 0) tokens[i] = "1";
			if(tokens[i].charAt(0) == 'C') {
				if(i == 0) {
					tokens[i] = tokens[i + 1];
				} else
				if(tokensCount > i + 1) {
					tokens[i - 1] = tokens[i - 1] + tokens[i + 1];
					tokensCount = i;
				}
			}
			if(isOperation(tokens[i].charAt(0))) {
				BigInteger res = BigInteger.ZERO;
				if(tokens[i].charAt(0) == '+') {
					for(int j = i + 1; j < tokensCount; j++) {
						res = res.add(new BigInteger(tokens[j]));
					}
				}
				if(tokens[i].charAt(0) == '/') {
					if(tokensCount > i + 1) {
						res = new BigInteger(tokens[i + 1]);
					} else {
						res = new BigInteger("0");
					}
					for(int j = i + 2; j < tokensCount; j++) {
						BigInteger divider = new BigInteger(tokens[j]);
						if(divider.equals(BigInteger.ZERO)) {
							res = BigInteger.ZERO;
							break;
						} else {
							res = res.divide(divider);
						}
					}
				}
				if(tokens[i].charAt(0) == '*') {
					res = BigInteger.ONE;
					for(int j = i + 1; j < tokensCount; j++) {
						res = res.multiply(new BigInteger(tokens[j]));
					}
				}
				tokensCount = i + 1;
				tokens[i] = (new BigInteger(res.toString())).toString();
			}
		}
		
		String value = "";
		for(int i = 0; i < tokensCount; i++) {
			value = value + (new BigInteger(tokens[i]));
		}
		
		return (new BigInteger(value)).toString();
	}
	
	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		int ln = 1;
		while(scanner.hasNextLine()) {
			Calculator clc = new Calculator();
			String line = scanner.nextLine();
			String h = "0";
			h = clc.calculate(line, 0, line.length());
			h = (new BigInteger(h)).toString();
			System.out.printf("Expression %d evaluates to: %s\n", ln, h);
			ln++;
		}
		scanner.close();
		//4/2*3+ WA 7
	}
	
}
