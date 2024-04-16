#define _CRT_SECURE_NO_WARNINGS
#pragma comment(linker, "/STACK:16777216")

#include <stdio.h>
#include <algorithm>

using namespace std;

int n, k, f;

int a[1024];

int flag[1024];
int list[1024];
int listSize = 0;

int answer[1024][3];
int answerSize = 0;

int current[1024];

bool solveForList(bool print) {
    if (print) {
        for (int i = 0; i < listSize; i++) {
            printf("%d ", list[i]);
        }
        printf("\n");
    }

    current[0] = a[list[0]];

    for (int i = 1; i < listSize; i++) {
        current[i] = a[list[i]] - current[i - 1];
    }

    if (listSize % 2 == 0) {
        if (current[listSize - 1] != 0) {
            return false;
        }

        int best = 1000000000;
        int bestValue = 0;

        int currentSum = 0;
        for (int i = 0; i < listSize - 1; i++) {
            int currentSumValue = 0;
            if (i % 2 == 0) {
                currentSumValue = current[i] - current[listSize - 1];
            }
            else {
                currentSumValue = current[i] + current[listSize - 1];
            }
            currentSum += currentSumValue > 0 ? currentSumValue : -currentSumValue;
        }

        best = currentSum;

        int prevSum = 1000000000;
        for (int cv = -1; ; cv--) {
            int currentSum = -cv;
            current[listSize - 1] = cv;
            for (int i = 0; i < listSize - 1; i++) {
                int currentSumValue = 0;
                if (i % 2 == 0) {
                    currentSumValue = current[i] - current[listSize - 1];
                }
                else {
                    currentSumValue = current[i] + current[listSize - 1];
                }
                currentSum += currentSumValue > 0 ? currentSumValue : -currentSumValue;
            }
            if (prevSum < currentSum) break;
            prevSum = currentSum;
            if (best > currentSum) {
                best = currentSum;
                bestValue = cv;
            }
        }

        prevSum = 1000000000;
        for (int cv = 1; ; cv++) {
            int currentSum = cv;
            current[listSize - 1] = cv;
            for (int i = 0; i < listSize - 1; i++) {
                int currentSumValue = 0;
                if (i % 2 == 0) {
                    currentSumValue = current[i] - current[listSize - 1];
                }
                else {
                    currentSumValue = current[i] + current[listSize - 1];
                }
                currentSum += currentSumValue > 0 ? currentSumValue : -currentSumValue;
            }
            if (prevSum < currentSum) break;
            prevSum = currentSum;
            if (best > currentSum) {
                best = currentSum;
                bestValue = cv;
            }
        }

        current[listSize - 1] = bestValue;

        answer[answerSize][0] = list[0];
        answer[answerSize][1] = list[listSize - 1];
        answer[answerSize][2] = current[listSize - 1];
        answerSize++;

        for (int i = 0; i < listSize - 1; i++) {
            answer[answerSize][0] = list[i];
            answer[answerSize][1] = list[(i + 1) % listSize];
            if (i % 2 == 0) {
                answer[answerSize][2] = current[i] - current[listSize - 1];
            }
            else {
                answer[answerSize][2] = current[i] + current[listSize - 1];
            }
            answerSize++;
        }

    }
    else {
        if (current[listSize - 1] % 2 != 0) {
            return false;
        }

        current[listSize - 1] /= 2;

        answer[answerSize][0] = list[0];
        answer[answerSize][1] = list[listSize - 1];
        answer[answerSize][2] = current[listSize - 1];
        answerSize++;

        for (int i = 0; i < listSize - 1; i++) {
            answer[answerSize][0] = list[i];
            answer[answerSize][1] = list[(i + 1) % listSize];
            if (i % 2 == 0) {
                answer[answerSize][2] = current[i] - current[listSize - 1];
            }
            else {
                answer[answerSize][2] = current[i] + current[listSize - 1];
            }
            answerSize++;
        }

    }

    return true;
}

void solve() {
    for (int i = 0; i < n; i++) {
        if (flag[i] == 0) {
            listSize = 0;
            while (true) {
                if (flag[i] == 1) break;
                flag[i] = 1;
                list[listSize++] = i;
                i = (i + k) % n;
            }
            if (!solveForList(false)) {
                printf("-1\n");
                return;
            }
        }
    }
    
    int total = 0;
    for (int i = 0; i < answerSize; i++) {
        total += answer[i][2] > 0 ? answer[i][2] : -answer[i][2];
    }

    printf("%d\n", total);
    for (int i = 0; i < answerSize; i++) {
        if (answer[i][2] > 0) {
            for (int j = 0; j < answer[i][2]; j++) {
                printf("%d %d +\n", answer[i][0] + 1, answer[i][1] + 1);
            }
        }
    }
    for (int i = 0; i < answerSize; i++) {
        if (answer[i][2] < 0) {
            for (int j = 0; j < -answer[i][2]; j++) {
                printf("%d %d -\n", answer[i][0] + 1, answer[i][1] + 1);
            }
        }
    }
}

int main() {
    scanf("%d%d%d", &n, &k, &f);
    for (int i = 0; i < n; i++) {
        scanf("%d", &a[i]);
        a[i] = f - a[i];
    }
    solve();
    return 0;
}

/*
4 1 7
8 4 5 9

4 1 7
8 4 5 3

3 1 4
4 2 4

2 1 3
2 2

2 1 3
4 4
*/