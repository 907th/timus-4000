#include <stdio.h>
#include <memory.h>
#include <algorithm>
#include <ctime>

using namespace std;

const int MAX_CONTAINERS = 16;
const int MAX_MOVES_COUNT = 40;
const int HASH_MAX_SIZE = 1024 * 1024 * 3;
const int HASH_TABLE_MAX_SIZE = 131072;
const int HASH_MODULE = 1070021;

const int dx[4] = { 1, -1, 0, 0 };
const int dy[4] = { 0, 0, 1, -1 };
const char mv[8] = { 'd', 'u', 'r', 'l' , 'D', 'U', 'R', 'L' };

struct HashNode {
	int ky1, ky2, mask, value;
	HashNode* next;
};

struct Container {
	int x, y, moveIndex, cId;
	int score;
};

struct Wall {
	int wallSize = 0;
	int coord[8][2];
	int targetsCount = 0;
};

bool operator < (Container a, Container b) {
	return a.score < b.score;
}

class LockDetector {

private:

	int field[16][16];
	int targetsMask[2];
	int n, m;

	Wall walls[64];
	int wallsCount = 0;

	bool isOn(int mask[2], int x, int y) {
		if (x < 0 || y < 0) return 0;
		int id = x * 6 + y;
		if (id > 30) {
			return (mask[1] & (1 << (id - 30))) != 0;
		}
		return (mask[0] & (1 << id)) != 0;
	}

	bool isWallHorizontal(int left, int right, int row, int offset) {
		if (row + offset < 0) return true;
		for (int i = left; i <= right; i++) {
			if (field[row + offset][i] == 0) return false;
		}
		return true;
	}

	bool isWallVertical(int top, int down, int row, int offset) {
		if (row + offset < 0) return true;
		for (int i = top; i <= down; i++) {
			if (field[i][row + offset] == 0) return false;
		}
		return true;
	}

public:

	void init(int a[16][16], int tM[2], int n, int m) {

		int fH[16][16];
		int fV[16][16];

		memset(fH, 0, sizeof(fH));
		memset(fV, 0, sizeof(fV));

		targetsMask[0] = tM[0];
		targetsMask[1] = tM[1];
		this->n = n;
		this->m = m;

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				field[i][j] = a[i][j];
			}
		}

		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < m - 1; j++) {
				if (a[i][j] == 0 && fH[i][j] == 0) {
					int left = j;
					int right = j;
					for (int k = j; k >= 0; k--) {
						if (a[i][k] == 1) break;
						left = k;
					}
					for (int k = j; k < m - 1; k++) {
						if (a[i][k] == 1) break;
						right = k;
					}
					if (isWallHorizontal(left, right, i, -1) || isWallHorizontal(left, right, i, 1)) {
						int index = 0;
						int targetsCount = 0;
						for (int k = left; k <= right; k++) {
							fH[i][k] = 1;
							walls[wallsCount].coord[index][0] = i;
							walls[wallsCount].coord[index][1] = k;
							index++;
							if (isOn(targetsMask, i, k)) targetsCount++;
						}
						walls[wallsCount].wallSize = index;
						walls[wallsCount].targetsCount = targetsCount;
						wallsCount++;
					}
				}
				if (a[i][j] == 0 && fV[i][j] == 0) {
					int top = i;
					int down = i;
					for (int k = i; k >= 0; k--) {
						if (a[k][j] == 1) break;
						top = k;
					}
					for (int k = j; k < n - 1; k++) {
						if (a[k][j] == 1) break;
						down = k;
					}
					if (isWallVertical(top, down, j, -1) || isWallVertical(top, down, j, 1)) {
						int index = 0;
						int targetsCount = 0;
						for (int k = top; k <= down; k++) {
							fV[k][j] = 1;
							walls[wallsCount].coord[index][0] = k;
							walls[wallsCount].coord[index][1] = j;
							index++;
							if (isOn(targetsMask, k, j)) targetsCount++;
						}
						walls[wallsCount].wallSize = index;
						walls[wallsCount].targetsCount = targetsCount;
						wallsCount++;
					}
				}
			}
		}

	}

	bool isFourTogether(int containersMask[2]) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {

				int containersCount = 0;
				int targetsCount = 0;
				int currentWallsCount = 0;
				for (int ki = -1; ki <= 0; ki++) {
					for (int kj = -1; kj <= 0; kj++) {
						if (i + ki < n - 2 && j + kj < m - 2) {
							containersCount += isOn(containersMask, i + ki, j + kj) ? 1 : 0;
							targetsCount += isOn(targetsMask, i + ki, j + kj) ? 1 : 0;
						}
						if (i + ki < 0 || j + kj < 0) currentWallsCount += 1; else currentWallsCount += field[i + ki][j + kj];
					}
				}
				if (containersCount + currentWallsCount == 4 && targetsCount < containersCount) {
					return true;
				}
			}
		}
		return false;
	}

	bool isInCorner(int containersMask[2]) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (isOn(containersMask, i, j) && !isOn(targetsMask, i, j)) {
					int uv = i == 0 ? 1 : field[i - 1][j];
					int dv = i >= 6 ? 1 : field[i + 1][j];

					int lv = j == 0 ? 1 : field[i][j - 1];
					int rv = j >= 6 ? 1 : field[i][j + 1];

					if (uv + lv == 2) return true;
					if (uv + rv == 2) return true;
					if (dv + lv == 2) return true;
					if (dv + rv == 2) return true;
				}
			}
		}
		return false;
	}

	bool isFailedOn9(int containersMask[2], int px, int py) {

		for (int i = 1; i < n - 1; i++) {
			for (int j = 1; j < m - 1; j++) {

				if (px == i - 1 && py == j - 1) continue;

				int targetsCount = 0;
				int containersCount = 0;
				int locksCount = 0;

				bool cornerFree = false;

				int centerField = field[i - 1][j - 1];
				int centerContainer = isOn(containersMask, i - 1, j - 1) ? 1 : 0;
				int centerTarget = isOn(targetsMask, i - 1, j - 1) ? 1 : 0;

				for (int ki = -2; ki <= 0; ki++) {
					for (int kj = -2; kj <= 0; kj++) {

						int containersCountCurrent = 0;
						int targetsCountCurrent = 0;
						int current = 1;
						if (i + ki >= 0 && j + kj >= 0) {
							current = field[i + ki][j + kj];
						}

						if (i + ki < n - 2 && j + kj < m - 2) {
							containersCountCurrent = isOn(containersMask, i + ki, j + kj) ? 1 : 0;
							targetsCountCurrent = isOn(targetsMask, i + ki, j + kj) ? 1 : 0;

							containersCount += containersCountCurrent;
							targetsCount += targetsCountCurrent;
						}

						if ((ki == -2 || ki == 0) && (kj == -2 || kj == 0) && current == 0 && containersCountCurrent == 0 && targetsCountCurrent == 0) {
							cornerFree = true;
						}

						locksCount += current;

					}
				}

				if (locksCount + containersCount == 8 && centerContainer == 0 && centerTarget == 0 && centerField == 0 && containersCount > targetsCount) {
					return true;
				}

				if (locksCount + containersCount == 8 && centerContainer == 0 && centerTarget == 1 && centerField == 0 && containersCount > targetsCount + 1) {
					return true;
				}

				if (locksCount + containersCount == 7 && centerContainer == 0 && centerTarget == 0 && centerField == 0 && containersCount > targetsCount && cornerFree) {
					return true;
				}

			}
		}

		for (int i = 0; i < n - 2; i++) {
			for (int j = 0; j < m - 2; j++) {
				//###
				//# $
				//#$
				if (field[i][j] == 1 && field[i][j + 1] == 1 && field[i][j + 2] == 1 && field[i + 1][j] == 1 && field[i + 2][j] == 1) {
					int targetSum = isOn(targetsMask, i + 1, j + 1) + isOn(targetsMask, i + 2, j + 1) + isOn(targetsMask, i + 1, j + 2);
					if (isOn(containersMask, i + 1, j + 2) && isOn(containersMask, i + 2, j + 1) && targetSum < 2) return true;
				}

				//#$
				//# $
				//###
				if (field[i + 2][j] == 1 && field[i + 2][j + 1] == 1 && field[i + 2][j + 2] == 1 && field[i + 1][j] == 1 && field[i][j] == 1) {
					int targetSum = isOn(targetsMask, i + 1, j + 1) + isOn(targetsMask, i, j + 1) + isOn(targetsMask, i + 1, j + 2);
					if (isOn(containersMask, i + 1, j + 2) && isOn(containersMask, i, j + 1) && targetSum < 2) return true;
				}

				// $#
				//$ #
				//###
				if (field[i][j + 2] == 1 && field[i + 1][j + 2] == 1 && field[i + 2][j + 2] == 1 && field[i + 2][j] == 1 && field[i + 2][j + 1] == 1) {
					int targetSum = isOn(targetsMask, i + 1, j + 1) + isOn(targetsMask, i, j + 1) + isOn(targetsMask, i + 1, j);
					if (isOn(containersMask, i, j + 1) && isOn(containersMask, i + 1, j) && targetSum < 2) return true;
				}

				//###
				//$ #
				// $#
				if (field[i][j] == 1 && field[i][j + 1] == 1 && field[i][j + 2] == 1 && field[i + 1][j + 2] == 1 && field[i + 2][j + 2] == 1) {
					int targetSum = isOn(targetsMask, i + 1, j + 1) + isOn(targetsMask, i + 1, j) + isOn(targetsMask, i + 2, j + 1);
					if (isOn(containersMask, i + 2, j + 1) && isOn(containersMask, i + 1, j) && targetSum < 2) return true;
				}
			}
		}

		return false;
	}

	bool isFailedOnWall(int containersMask[2]) {
		for (int i = 0; i < wallsCount; i++) {
			int containersOnWall = 0;
			for (int j = 0; j < walls[i].wallSize; j++) {
				if (isOn(containersMask, walls[i].coord[j][0], walls[i].coord[j][1])) containersOnWall++;
			}
			if (containersOnWall > walls[i].targetsCount) return true;
		}
		return false;
	}

	bool isLocked(int containersMask[2], int px, int py) {
		if (isFourTogether(containersMask)) return true;
		if (isInCorner(containersMask)) return true;
		if (isFailedOn9(containersMask, px, py)) return true;
		if (isFailedOnWall(containersMask)) return true;
		return false;
	}

};

struct ContainerWithScore {
	int containerId;
	int score;
};

bool operator < (ContainerWithScore a, ContainerWithScore b) {
	return a.score < b.score;
}

struct PlaceWithScore {
	int x, y;
	int playerX, playerY;
	int score;
	char movings[64];
	int movesCount = 0;
};

bool operator < (PlaceWithScore a, PlaceWithScore b) {
	return a.score < b.score;
}

class Algorithm {

private:

	int n, m;
	int a[16][16];

	int curPlayerX, curPlayerY;
	int targetMask[2];

	int containerMask[2];

	int containers[MAX_CONTAINERS][2];
	int containersCount = 0;

	int containerPlace[8][8];

	int targets[MAX_CONTAINERS][2];
	int targetsCount = 0;

	HashNode* hash[HASH_TABLE_MAX_SIZE];
	HashNode hashNodes[HASH_MAX_SIZE];
	int hashNodesCount = 0;

	LockDetector lockDetector;

	int hashFunc(unsigned int mask1, unsigned int mask2, unsigned int mask) {
		unsigned int hk = ((mask1 * HASH_MODULE + mask2) * HASH_MODULE + mask) % HASH_TABLE_MAX_SIZE;
		return hk;
	}

	void addToHash(int mask1, int mask2, int curX, int curY, int mask, int value) {
		mask2 |= (curX << 6) | (curY << 9);
		int hashKey = hashFunc(mask1, mask2, mask);
		for (HashNode* p = hash[hashKey]; p; p = p->next) {
			if (p->ky1 == mask1 && p->ky2 == mask2) {
				p->value = value;
				return;
			}
		}
		HashNode* p = hashNodes + hashNodesCount++;
		p->ky1 = mask1;
		p->ky2 = mask2;
		p->mask = mask;
		p->value = value;
		p->next = hash[hashKey];
		hash[hashKey] = p;
	}

	int getValue(int mask1, int mask2, int curX, int curY, int mask) {
		mask2 |= (curX << 6) | (curY << 9);
		int hashKey = hashFunc(mask1, mask2, mask);
		for (HashNode* p = hash[hashKey]; p; p = p->next) {
			if (p->ky1 == mask1 && p->ky2 == mask2 && p->mask == mask) {
				return p->value;
			}
		}
		return -1;
	}

	void updateTargetMask(int x, int y) {
		targets[targetsCount][0] = x;
		targets[targetsCount][1] = y;
		targetsCount++;
		int id = x * 6 + y;
		if (id > 30) targetMask[1] |= 1 << (id - 30); else targetMask[0] |= 1 << id;
	}

	void addContainer(int x, int y) {
		containers[containersCount][0] = x;
		containers[containersCount][1] = y;
		containerPlace[x][y] = containersCount;
		containersCount++;

		int id = x * 6 + y;
		if (id > 30) containerMask[1] |= 1 << (id - 30); else containerMask[0] |= 1 << id;
	}

	void updateContainerMask(int x, int y, int cId) {
		containers[cId][0] = x;
		containers[cId][1] = y;
		containerPlace[x][y] = cId;
		int id = x * 6 + y;
		if (id > 30) containerMask[1] |= 1 << (id - 30); else containerMask[0] |= 1 << id;
	}

	bool isOnTarget(int x, int y) {
		int id = x * 6 + y;
		if (id > 30) {
			return ((1 << (id - 30)) & targetMask[1]) != 0;
		}
		return ((1 << id) & targetMask[0]) != 0;
	}

	bool doesItHaveContainer(int x, int y) {
		int id = x * 6 + y;
		if (id > 30) {
			return ((1 << (id - 30)) & containerMask[1]) != 0;
		}
		return ((1 << id) & containerMask[0]) != 0;
	}

	void removeContainerMask(int x, int y) {
		int id = x * 6 + y;
		if (id > 30) {
			containerMask[1] = containerMask[1] ^ (1 << (id - 30));
		}
		else {
			containerMask[0] = containerMask[0] ^ (1 << id);
		}
	}

	int bfsPlayerF[8][8];
	int predBfsPlayer[8][8];
	int pQueue[2048];
	int pqf = 0, pqs = 0;

	void relaxPlayer(int x, int y, int outx, int outy, char move, int currentLength) {
		if (x < 0 || y < 0 || x >= n || y >= m || x == outx && y == outy) return;
		if (doesItHaveContainer(x, y) || a[x][y] == 1) return;
		if (bfsPlayerF[x][y] == 0) {
			bfsPlayerF[x][y] = currentLength + 1;
			pQueue[pqf++] = x;
			pQueue[pqf++] = y;
			predBfsPlayer[x][y] = move;
		}
	}

	void BFSPlayer(int x, int y, int outx, int outy) {
		pqs = pqf = 0;
		memset(bfsPlayerF, 0, sizeof(bfsPlayerF));
		pQueue[pqf++] = x;
		pQueue[pqf++] = y;
		bfsPlayerF[x][y] = 1;
		while (pqs != pqf) {
			x = pQueue[pqs++];
			y = pQueue[pqs++];
			for (int i = 0; i < 4; i++) {
				relaxPlayer(x + dx[i], y + dy[i], outx, outy, mv[i], bfsPlayerF[x][y]);
			}
		}
	}

	int q[2048 * 4];
	int bfsBoxF[8][8][8][8];
	int predBfsBox[8][8][8][8];
	int qf = 0, qs = 0;

	char subPlayerAnswer[1024];
	int subPlayerAnswerLength = 0;

	void relaxBox(int boxX, int boxY, int playerX, int playerY, int moveIndex, int currentLength) {
		if (playerX < 0 || playerY < 0 || playerX >= n || playerY >= m) return;
		if (playerX == boxX && playerY == boxY) {
			boxX = boxX + dx[moveIndex];
			boxY = boxY + dy[moveIndex];
			moveIndex += 4;
		}
		if (boxX < 0 || boxY < 0 || boxX >= n || boxY >= m) return;

		if (doesItHaveContainer(boxX, boxY) || a[boxX][boxY] == 1 || doesItHaveContainer(playerX, playerY) || a[playerX][playerY] == 1) return;

		if (bfsBoxF[boxX][boxY][playerX][playerY] == 0) {
			bfsBoxF[boxX][boxY][playerX][playerY] = currentLength + 1;
			q[qf++] = boxX;
			q[qf++] = boxY;
			q[qf++] = playerX;
			q[qf++] = playerY;
			predBfsBox[boxX][boxY][playerX][playerY] = mv[moveIndex];
		}
	}

	void getLocalBoxAnswer(int boxX, int boxY, int playerX, int playerY) {
		if (bfsBoxF[boxX][boxY][playerX][playerY] == 0) {
			return;
		}
		subAnswerLength = 0;
		int cnt = 0;
		int ibx = boxX;
		int iby = boxY;
		int ipx = playerX;
		int ipy = playerY;
		while (true) {
			subAnswer[subAnswerLength++] = predBfsBox[boxX][boxY][playerX][playerY];

			if (bfsBoxF[boxX][boxY][playerX][playerY] == 1) break;
			if (predBfsBox[boxX][boxY][playerX][playerY] == 'U') {
				boxX++; playerX++;
			}
			else
				if (predBfsBox[boxX][boxY][playerX][playerY] == 'D') {
					boxX--; playerX--;
				}
				else
					if (predBfsBox[boxX][boxY][playerX][playerY] == 'L') {
						boxY++; playerY++;
					}
					else
						if (predBfsBox[boxX][boxY][playerX][playerY] == 'R') {
							boxY--; playerY--;
						}
						else
							if (predBfsBox[boxX][boxY][playerX][playerY] == 'u') {
								playerX++;
							}
							else
								if (predBfsBox[boxX][boxY][playerX][playerY] == 'd') {
									playerX--;
								}
								else
									if (predBfsBox[boxX][boxY][playerX][playerY] == 'l') {
										playerY++;
									}
									else
										if (predBfsBox[boxX][boxY][playerX][playerY] == 'r') {
											playerY--;
										}
			cnt++;
			if (cnt > 1000) {
				int kor = 123;
			}
		}
		for (int i = 0; i < subAnswerLength / 2; i++) {
			char lc = subAnswer[i];
			subAnswer[i] = subAnswer[subAnswerLength - i - 1];
			subAnswer[subAnswerLength - i - 1] = lc;
		}
		answer[answerLength] = 0;
		subAnswer[subAnswerLength] = 0;
	}

	char subAnswer[1024];
	int subAnswerLength = 0;

	void BFSBox(int cx, int cy, int curX, int curY) {
		qs = qf = 0;
		memset(bfsBoxF, 0, sizeof(bfsBoxF));
		memset(predBfsBox, 0, sizeof(predBfsBox));
		q[qf++] = cx;
		q[qf++] = cy;
		q[qf++] = curX;
		q[qf++] = curY;
		while (qs != qf) {
			cx = q[qs++];
			cy = q[qs++];
			curX = q[qs++];
			curY = q[qs++];
			for (int i = 0; i < 4; i++) {
				relaxBox(cx, cy, curX + dx[i], curY + dy[i], i, bfsBoxF[cx][cy][curX][curY]);
			}
		}
	}

	void appendAnswer(char c[64], char cnt) {
		for (int i = 0; i < cnt; i++) {
			answer[answerLength++] = c[i];
		}
	}

	char answer[16384];
	int answerLength = 0;

	int movedOnTarget[MAX_CONTAINERS];

	void extractData(char field[8][9]) {
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				if (field[i][j] == '#') a[i - 1][j - 1] = 1;
				if (field[i][j] == '@' || field[i][j] == '+') {
					curPlayerX = i - 1; curPlayerY = j - 1;
				}
				if (field[i][j] == '+' || field[i][j] == '*' || field[i][j] == '.') {
					updateTargetMask(i - 1, j - 1);
				}
				if (field[i][j] == '$' || field[i][j] == '*') {
					addContainer(i - 1, j - 1);
				}
			}
		}
		n -= 2; m -= 2;
	}

	char* debugInformation() {
		char debugInfo[1024]; memset(debugInfo, 0, sizeof(debugInfo));
		int debugInfoSize = 0;
		for (int j = 0; j < 8; j++) {
			debugInfo[debugInfoSize++] = '#';
		}
		debugInfo[debugInfoSize++] = '\n';

		for (int i = 0; i < n; i++) {
			debugInfo[debugInfoSize++] = '#';
			for (int j = 0; j < m; j++) {
				char c = ' ';
				if (doesItHaveContainer(i, j)) c = '$';
				if (a[i][j] == 1) c = '#';
				debugInfo[debugInfoSize++] = c;
			}
			debugInfo[debugInfoSize++] = '\n';
		}
		for (int j = 0; j < 8; j++) {
			debugInfo[debugInfoSize++] = '#';
		}
		debugInfo[debugInfoSize++] = '\n';
		return debugInfo;
	}

	void out(char field[8][9]) {
		printf("%d %d\n", n + 2, m + 2);
		for (int i = 0; i < n + 2; i++) {
			for (int j = 0; j < m + 2; j++) {
				printf("%c", field[i][j]);
			}
			printf("\n");
		}
	}

	Container movements[16 * 4];
	int movementsCount;

	int findContainer(int x, int y) {
		return containerPlace[x][y];
	}

	int playerPos[8][8];

	bool hasSomeThing(int x, int y) {
		if (x < 0 || y < 0 || x >= n - 1 || y >= n - 1) return true;
		return a[x][y] == 1 || doesItHaveContainer(x, y);
	}

	int getContainerScore(int containerId, int lastContainer) {

		if (containerId == lastContainer) return 1000000;

		int containerX = containers[containerId][0];
		int containerY = containers[containerId][1];

		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (hasSomeThing(containerX + i, containerY + j)) {
					count++;
				}
			}
		}

		if (isOnTarget(containers[containerId][0], containers[containerId][1])) {
			return 10000 + count * 100;
		}
		return count * 100;
	}

	int customAbs(int a) {
		return a < 0 ? -a : a;
	}

	int getPlaceScore(int x, int y, int containerX, int containerY) {
		if (isOnTarget(x, y)) return customAbs(x - containerX) + customAbs(y - containerY);
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (hasSomeThing(containerX + i, containerY + j)) {
					count++;
				}
			}
		}
		return 1000 + customAbs(x - containerX) + customAbs(y - containerY) + 10 * (100 - count);
	}

	bool isAnswerFound() {
		return ((targetMask[0] & containerMask[0]) == containerMask[0]) && ((targetMask[1] & containerMask[1]) == containerMask[1]);
	}

public:

	bool rec(int movesCount, int lastContainerId) {

		if (answerLength > 10000) return false;

		if (isAnswerFound()) return true;

		if (getValue(containerMask[0], containerMask[1], curPlayerX, curPlayerY, 0) != -1) return false;
		addToHash(containerMask[0], containerMask[1], curPlayerX, curPlayerY, 0, 0);
		if (lockDetector.isLocked(containerMask, curPlayerX, curPlayerY)) return false;

		//printf("%d\n", movesCount);
		//printf("%s\n===============================\n", debugInformation());

		//Choose most suitable container
		ContainerWithScore cws[16];
		for (int i = 0; i < containersCount; i++) {
			cws[i].containerId = i;
			cws[i].score = getContainerScore(i, lastContainerId);
		}
		sort(cws, cws + containersCount);

		if (movesCount >= MAX_MOVES_COUNT) return false;

		for (int i = 0; i < containersCount; i++) {

			int containerId = cws[i].containerId;
			int containerX = containers[containerId][0];
			int containerY = containers[containerId][1];

			removeContainerMask(containerX, containerY);
			BFSBox(containerX, containerY, curPlayerX, curPlayerY);

			//Choose most suitable place for given container
			PlaceWithScore pws[36];
			int placesCount = 0;
			for (int jx = 0; jx < 6; jx++) {
				for (int jy = 0; jy < 6; jy++) {
					for (int jk = 0; jk < 4; jk++) {
						if (jx == containerX && jy == containerY) continue;
						if (bfsBoxF[jx][jy][jx + dx[jk]][jy + dy[jk]] != 0) {
							pws[placesCount].x = jx;
							pws[placesCount].y = jy;
							pws[placesCount].playerX = jx + dx[jk];
							pws[placesCount].playerY = jy + dy[jk];
							placesCount++; break;
						}
					}
				}
			}
			for (int j = 0; j < placesCount; j++) {
				int h = getPlaceScore(pws[j].x, pws[j].y, containerX, containerY);
				getLocalBoxAnswer(pws[j].x, pws[j].y, pws[j].playerX, pws[j].playerY);
				pws[j].score = h;
				pws[j].movesCount = 0;
				for (int k = 0; k < subAnswerLength; k++) {
					pws[j].movings[pws[j].movesCount++] = subAnswer[k];
				}
			}
			sort(pws, pws + placesCount);

			updateContainerMask(containerX, containerY, containerId);

			//Backtrack
			for (int j = 0; j < placesCount; j++) {
				int boxToMoveX = pws[j].x;
				int boxToMoveY = pws[j].y;

				int playerToMoveX = pws[j].playerX;
				int playerToMoveY = pws[j].playerY;

				//Save the state
				int lastAnswerLength = answerLength;
				int lastCurX = curPlayerX, lastCurY = curPlayerY;

				//Do a move
				curPlayerX = playerToMoveX; curPlayerY = playerToMoveY;
				removeContainerMask(containerX, containerY);
				updateContainerMask(boxToMoveX, boxToMoveY, containerId);
				appendAnswer(pws[j].movings, pws[j].movesCount);

				if (rec(movesCount + 1, containerId)) return true;

				//Restore
				curPlayerX = lastCurX; curPlayerY = lastCurY;
				answerLength = lastAnswerLength;
				removeContainerMask(boxToMoveX, boxToMoveY);
				updateContainerMask(containerX, containerY, containerId);
			}
		}

		return false;
	}

	bool solve(int n, int m, char field[8][9]) {

		this->n = n;
		this->m = m;

		extractData(field);
		//out(field);

		lockDetector.init(a, targetMask, n, m);

		if (rec(0, -1)) {
			for (int i = answerLength; i < 16384; i++) {
				answer[i] = 0;
			}
			printf("%s", answer);
		}
		else {
			printf("Answer is not found!\n"); int x = 0;
			int k = 25 / x;
		}

		return false;
	}

};

Algorithm algo;

void getData() {

	int n = 0, m = 0;
	char ca[8][9]; memset(ca, 0, sizeof(ca));
	char c;
	int k = 0; n = 0;
	while (scanf("%c", &c) != EOF) {
		if (c != 13 && c != 10) ca[n][k++] = c;
		if (c == 10) {
			n++;
			if (m < k) m = k;
			k = 0;
		}
	}
	n++;
	if (n > 8) n = 8;
	//n = 8;
	//m = 8;
	algo.solve(n, m, ca);
}

int main() {
	int n = 8, m = 8;
	clock_t begin = clock();
	getData();

	//char ca[8][9] = { "########", "#. . ..#", "#####  #", "#.@$  $#", "#$ $ $ #", "#  $ $ #", "#.$  ..#", "########" }; //+ 0.112
	//char ca[8][9] = { "########", "#..... #", "#$$$$$@#", "# .....#", "#$$$$$$#", "#    $.#", "#. $.$.#", "########" }; //+ 4.135
	//char ca[8][9] = { "########", "#. ....#", "#####  #", "#.@$  $#", "#$ $ $ #", "#  $ $ #", "#.$   .#", "########" }; //+ 5.601
	//char ca[8][9] = { "#######", "##  . #", "# * # #", "# .$  #", "#  #$##", "## @ ##", "#######", "" }; n = 7, m = 7; //+ 0.006
	//char ca[8][9] = { "########", "#.     #", "#$ $ $ #", "#@ $ $ #", "#  $ $ #", "#  ****#", "#......#", "########" }; //+ 0.107
	//char ca[8][9] = { " ######", "##   .#", "#@  ###", "#   * #", "#   $ #", "#     #", "#######" }; //+ 0.004

	//char ca[8][9] = { "########", "##.* $.#", "#  * . #", "#   *. #", "## $.$ #", "# $*$$$#", "#..   @#", "########" }; //-
	//char ca[8][9] = { "########", "#. .@  #", "#$$$*$*#", "#. $   #", "#.$.* *#", "# $.$  #", "#. .*  #", "########" }; //-
	//char ca[8][9] = { "########", "#. .*  #", "##$ *  #", "##  * *#", "## *  .#", "# $$**$#", "#@.    #", "########" }; //-
	//char ca[8][9] = { "########", "#......#", "#      #", "#***** #", "#      #", "#$$$$$$#", "#     @#", "########" }; //+ 0
	//char ca[8][9] = { "########", "#..... #", "#  ##  #", "# *    #", "#  # # #", "#$$$$$$#", "#     +#", "########" }; //+ 8
	//char ca[8][9] = { "########", "#......#", "#.   $@#", "#. $  $#", "#$ $ $ #", "# $$ $ #", "#.$   .#", "########" }; //-
	//char ca[8][9] = { "########", "#......#", "#.   $ #", "#.@$  $#", "#$ $ $ #", "# $$ $ #", "#.$   .#", "########" }; //+ 0

	//algo.solve(n, m, ca);
	clock_t end = clock();
	double elapsedSecs = double(end - begin) / CLOCKS_PER_SEC;
	//printf("\n\n%lf\n", elapsedSecs);
}