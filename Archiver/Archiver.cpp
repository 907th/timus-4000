#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <string>

using namespace std;

const int LINE_LENGTH = 400;

int n = 0;
int codedLength = 0;
char input[200004];

int linesCount = 0;

void init(bool debug) {
	char c;
	while (scanf("%c", &c) != EOF) {
		input[n++] = c;
		if (c == '^' || c == '$' || c == '%' || c == '@' || c == '\\' || c == '/') {
			while (true);
		}
	}
}

int printHead(int linesCount) {
	string out = "//CPP\n#include<stdio.h>\n#include<string.h>\nchar s[%d][%d] = {";
	printf("//CPP\n#include<stdio.h>\n#include<string.h>\nchar s[%d][%d] = {", linesCount, LINE_LENGTH + 2);
	return out.length() + 1;
}

int sblength = 0;
char sb[LINE_LENGTH + 100];

int printBody() {

	linesCount = n / LINE_LENGTH + 1;
	int headLength = printHead(linesCount);
	int totalLength = 0;

	for (int i = 0; i < n; i++) {
		if (input[i] == ' ' &&
			input[i + 1] == 't' &&
			input[i + 2] == 'h' &&
			input[i + 3] == 'e' &&
			input[i + 4] == ' '
			) {
			sb[sblength++] = '^';
			i += 4;
		}
		else
			if (input[i] == ' ' &&
				input[i + 1] == 'a' &&
				input[i + 2] == ' '
				) {
				sb[sblength++] = '$';
				i += 2;
			}
			else
				if (input[i] == ' ' &&
					input[i + 1] == 'a' &&
					input[i + 2] == 'n' &&
					input[i + 3] == 'd' &&
					input[i + 4] == ' '
					) {
					sb[sblength++] = '%';
					i += 4;
				}
				else
					if (input[i] == '\n') {
						sb[sblength++] = '\\';
						sb[sblength++] = 'n';
					}
					else
						if (input[i] == '"') {
							sb[sblength++] = '\\';
							sb[sblength++] = '"';
						}
						else
							if (input[i] < 32) {
								if (input[i] != 13 && input[i] != 10) while (true);
							}
							else
								if (input[i] == ' ' &&
									input[i + 1] == 't' &&
									input[i + 2] == 'o' &&
									input[i + 3] == ' '
									) {
									i += 3;
									sb[sblength++] = '@';
								}
								else {
									sb[sblength++] = input[i];
								}
		if (sblength >= LINE_LENGTH) {
			printf("%c", '"');
			for (int j = 0; j < sblength; j++) {
				printf("%c", sb[j]);
			}
			printf("%c", '"');
			printf("%c\n", ',');
			totalLength += sblength + 1;
			sblength = 0;
		}
	}

	printf("%c", '"');
	for (int j = 0; j < sblength; j++) {
		printf("%c", sb[j]);
	}
	printf("%c", '"');
	printf("%c", '}');
	printf("%c", ';');
	totalLength += sblength + 1;

	return headLength + totalLength;
}

int printTail(int linesCount) {
	string out = "\nint main()\n{for(int j=0;j<%d;j++) for(int i=0;i<strlen(s[j]);i++){\nif(s[j][i]=='^')printf(\" the \");else\nif(s[j][i]=='$')printf(\" a \");else\nif(s[j][i]=='%c')printf(\" and \");else\nif(s[j][i]=='@')printf(\" to \");else\nprintf(\"%cc\",s[j][i]);} return 0;}";
	printf("\nint main()\n{for (int j = 0; j < %d; j++) for (int i = 0; i < strlen(s[j]); i++) {\nif(s[j][i] == '^')printf(\" the \");else\nif(s[j][i]=='$')printf(\" a \");else\nif(s[j][i]=='%c')printf(\" and \");else\nif(s[j][i]=='@')printf(\" to \");else\nprintf(\"%cc\",s[j][i]);} return 0;}", linesCount, '%', '%');
	return out.length();
}

void output(bool debug) {
	int archiveLength = 0;
	archiveLength += printBody();
	archiveLength += printTail(linesCount);
}

void solve(bool debug) {
	init(debug);
	output(debug);
}

int main() {
	solve(false);
	return 0;
}