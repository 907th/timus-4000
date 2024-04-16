#define _CRT_SECURE_NO_WARNINGS
#pragma comment(linker, "/STACK:16777216")

#include <stdio.h>
#include <algorithm>

using namespace std;

const int N = 1024;

struct rect {
    int x1, y1, x2, y2;
    int color;
};

int mn(int a, int b) {
    return a < b ? a : b;
}

int mx(int a, int b) {
    return a > b ? a : b;
}

int w, h;
int n;

rect r[N];

int total[2504];

inline bool isInside(int x11, int y11, int x12, int y12, int x21, int y21, int x22, int y22) {
    return x21 <= x11 && y21 <= y11 && x12 <= x22 && y12 <= y22;
}

inline bool isIntersect(int x11, int y11, int x12, int y12, int x21, int y21, int x22, int y22) {
    bool isNotIntersect = x11 >= x22 || y11 >= y22 || x12 <= x21 || y12 <= y21;
    return !isNotIntersect;
}

inline bool isStrictlyBetween(int l, int r, int x) {
    return l < x&& x < r;
}

int intersectionSquare(int cx1, int cy1, int cx2, int cy2, int x1, int y1, int x2, int y2) {
    if (isInside(cx1, cy1, cx2, cy2, x1, y1, x2, y2)) return 0;

    if (!isIntersect(cx1, cy1, cx2, cy2, x1, y1, x2, y2)) {
        return 0;
    }
    else {

        if (isStrictlyBetween(cx1, cx2, x1) && isStrictlyBetween(cx1, cx2, x2)) {
            intersectionSquare(cx1, cy1, x1, cy2, x1, y1, x2, y2);
            intersectionSquare(x1, cy1, x2, cy2, x1, y1, x2, y2);
            intersectionSquare(x2, cy1, cx2, cy2, x1, y1, x2, y2);
        }
        else
            if (isStrictlyBetween(cx1, cx2, x1)) {
                intersectionSquare(cx1, cy1, x1, cy2, x1, y1, x2, y2);
                intersectionSquare(x1, cy1, cx2, cy2, x1, y1, x2, y2);
            }
            else
                if (isStrictlyBetween(cx1, cx2, x2)) {
                    intersectionSquare(cx1, cy1, x2, cy2, x1, y1, x2, y2);
                    intersectionSquare(x2, cy1, cx2, cy2, x1, y1, x2, y2);
                }
                else {
                    if (isStrictlyBetween(cy1, cy2, y1) && isStrictlyBetween(cy1, cy2, y2)) {
                        intersectionSquare(cx1, cy1, cx2, y1, x1, y1, x2, y2);
                        intersectionSquare(cx1, y1, cx2, y2, x1, y1, x2, y2);
                        intersectionSquare(cx1, y2, cx2, cy2, x1, y1, x2, y2);
                    }
                    else
                        if (isStrictlyBetween(cy1, cy2, y1)) {
                            intersectionSquare(cx1, cy1, cx2, y1, x1, y1, x2, y2);
                            intersectionSquare(cx1, y1, cx2, cy2, x1, y1, x2, y2);
                        }
                        else {
                            intersectionSquare(cx1, cy1, cx2, y2, x1, y1, x2, y2);
                            intersectionSquare(cx1, y2, cx2, cy2, x1, y1, x2, y2);
                        }
                }
    }

}

int currentIndex;

bool operator < (rect a, rect b) {
    int sq1 = intersectionSquare(r[currentIndex].x1, r[currentIndex].y1, r[currentIndex].x2, r[currentIndex].y2, a.x1, a.y1, a.x2, a.y2);
    int sq2 = intersectionSquare(r[currentIndex].x1, r[currentIndex].y1, r[currentIndex].x2, r[currentIndex].y2, b.x1, b.y1, b.x2, b.y2);
    if (sq1 == sq2) {
        if (a.x1 != b.x1) return a.x1 < b.x1;
        return a.y1 < b.y1;
    }
    return sq1 > sq2;
}

void applyRect(int cx1, int cy1, int cx2, int cy2, int color, int index) {
    if (index >= n) {
        total[color] += (cx2 - cx1) * (cy2 - cy1);
        return;
    }

    if (isInside(cx1, cy1, cx2, cy2, r[index].x1, r[index].y1, r[index].x2, r[index].y2)) return;

    if (!isIntersect(cx1, cy1, cx2, cy2, r[index].x1, r[index].y1, r[index].x2, r[index].y2)) {
        applyRect(cx1, cy1, cx2, cy2, color, index + 1);
    }
    else {

        if (isStrictlyBetween(cx1, cx2, r[index].x1) && isStrictlyBetween(cx1, cx2, r[index].x2)) {
            applyRect(cx1, cy1, r[index].x1, cy2, color, index);
            applyRect(r[index].x1, cy1, r[index].x2, cy2, color, index);
            applyRect(r[index].x2, cy1, cx2, cy2, color, index);
        }
        else
            if (isStrictlyBetween(cx1, cx2, r[index].x1)) {
                applyRect(cx1, cy1, r[index].x1, cy2, color, index);
                applyRect(r[index].x1, cy1, cx2, cy2, color, index);
            }
            else
                if (isStrictlyBetween(cx1, cx2, r[index].x2)) {
                    applyRect(cx1, cy1, r[index].x2, cy2, color, index);
                    applyRect(r[index].x2, cy1, cx2, cy2, color, index);
                }
                else {
                    if (isStrictlyBetween(cy1, cy2, r[index].y1) && isStrictlyBetween(cy1, cy2, r[index].y2)) {
                        applyRect(cx1, cy1, cx2, r[index].y1, color, index);
                        applyRect(cx1, r[index].y1, cx2, r[index].y2, color, index);
                        applyRect(cx1, r[index].y2, cx2, cy2, color, index);
                    }
                    else
                        if (isStrictlyBetween(cy1, cy2, r[index].y1)) {
                            applyRect(cx1, cy1, cx2, r[index].y1, color, index);
                            applyRect(cx1, r[index].y1, cx2, cy2, color, index);
                        }
                        else {
                            applyRect(cx1, cy1, cx2, r[index].y2, color, index);
                            applyRect(cx1, r[index].y2, cx2, cy2, color, index);
                        }
                }
    }

}

int main() {
    scanf("%d%d%d", &w, &h, &n);
    for (int i = 0; i < n; i++) {
        scanf("%d%d%d%d%d", &r[i].x1, &r[i].y1, &r[i].x2, &r[i].y2, &r[i].color);
    }

    for (int i = n - 1; i >= 0; i--) {
        currentIndex = i;
        sort(r + i + 1, r + n);
        applyRect(r[i].x1, r[i].y1, r[i].x2, r[i].y2, r[i].color, i + 1);
    }

    int whiteTotal = w * h;

    for (int i = 2; i <= 2500; i++) {
        whiteTotal -= total[i];
    }
    if (whiteTotal > 0) {
        printf("1 %d\n", whiteTotal);
    }
    for (int i = 2; i <= 2500; i++) {
        if (total[i] > 0) printf("%d %d\n", i, total[i]);
    }

    return 0;
}