import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Archiver {

	private final int LINE_LENGTH = 400;
	
	int n = 0;
	int codedLength = 0;
	char[] input = new char[200004];

	int linesCount = 0;
	
	PrintWriter pw;
	InputStreamReader isr;
	
	void init(boolean debug) {
		try {
			if(debug) {
				pw = new PrintWriter(new File("D:/out.txt"));
				isr = new InputStreamReader(new FileInputStream("D:/in.txt"));
			} else {
				pw = new PrintWriter(System.out);
				isr = new InputStreamReader(System.in);
			}
			
			int x = 0;
			while((x = isr.read()) != -1) {
				char c = (char)x;
				input[n++] = c;
				if(c == '^' || c == '$' || c == '%' || c == '@' || c == '\\' || c == '/') {
					while(true);
				}
			}
			isr.close();
		} catch (IOException e) {
			
		}
	}
	
	int printHead(int linesCount) {
		String out = String.format("//CPP\n"
				+ "#include<stdio.h>\n"
				+ "#include<string.h>\n"
				+ "char s[%s][%s] = {", linesCount, LINE_LENGTH + 2);
		pw.println(out);
		return out.length() + 1;
	}
	
	int printBody() {
		
		linesCount = n / LINE_LENGTH + 1;
		int headLength = printHead(linesCount);
		int totalLength = 0;

		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < n; i++) {
			if( input[i] == ' ' && 
				input[i + 1] == 't' &&
				input[i + 2] == 'h' &&
				input[i + 3] == 'e' &&
				input[i + 4] == ' '
				) {
				sb.append('^');
				i += 4;
			} else
			if( input[i] == ' ' && 
				input[i + 1] == 'a' &&
				input[i + 2] == ' '
				) {
				sb.append('$');
				i += 2;
			} else
			if( input[i] == ' ' && 
				input[i + 1] == 'a' &&
				input[i + 2] == 'n' &&
				input[i + 3] == 'd' &&
				input[i + 4] == ' '
				) {
				sb.append('%');
				i += 4;
			} else
			if( input[i] == '\n') {
				sb.append("\\n");
			} else
			if( input[i] == '"') {
				sb.append('\\');
				sb.append('"');
			} else
			if( input[i] < 32) {
				if(input[i] != 13 && input[i] != 10) while(true);
			} else
			if( input[i] == ' ' && 
				input[i + 1] == 't' &&
				input[i + 2] == 'o' &&
				input[i + 3] == ' '
				) {
				i += 3;
				sb.append('@');
			} else {
				sb.append(input[i]);
			}
			if(sb.length() >= LINE_LENGTH) {
				pw.print('"');
				for(int j = 0; j < sb.length(); j++) {
					pw.print(sb.charAt(j));
				}
				pw.print('"');
				pw.print(',');
				pw.println();
				totalLength += sb.length() + 1;
				sb.setLength(0);
			}
		}
		
		pw.print('"');
		for(int j = 0; j < sb.length(); j++) {
			pw.print(sb.charAt(j));
		}
		pw.print('"');
		pw.print('}');
		pw.print(';');
		totalLength += sb.length() + 1;
		
		return headLength + totalLength;
	}
	
	int printTail(int linesCount) {
		String out = String.format("\n"
				+ "int main()\n"
				+ "{for(int j=0;j<%d;j++) for(int i=0;i<strlen(s[j]);i++){\nif(s[j][i]=='^')printf(\" the \");else\nif(s[j][i]=='$')printf(\" a \");else\nif(s[j][i]=='%c')printf(\" and \");else\nif(s[j][i]=='@')printf(\" to \");else\nprintf(\"%cc\",s[j][i]);} return 0;}", linesCount, '%', '%');
		pw.print(out);
		return out.length();
	}
	
	void output(boolean debug) {
		int archiveLength = 0;
		archiveLength += printBody();
		archiveLength += printTail(linesCount);
		pw.flush();
		pw.close();
		if(archiveLength >= n && !debug) {
			while(true);
		}
	}
	
	void solve(boolean debug) {
		init(debug);
		output(debug);
	}
	
	public static void main(String[] args) {
		Archiver a = new Archiver();
		a.solve(false);
	}
	
}