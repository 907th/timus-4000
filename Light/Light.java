import java.util.Comparator;

public class Light {

    private static double eps = 1e-7;
    private static int N = 50004;
    private static int M = 300004;

    class PointsComparator implements Comparator<double[]> {
        @Override
        public int compare(double[] o1, double[] o2) {
            double v = vect(o1[0], o1[1], o2[0], o2[1]);
            if(Math.abs(v) < eps) return 0;
            return v < 0 ? -1 : 1;
        }
    }

    class PointsByYComparator implements Comparator<double[]> {
        @Override
        public int compare(double[] o1, double[] o2) {
            double v = o2[1] - o1[1];
            if(Math.abs(v) < eps) return 0;
            return v < 0 ? -1 : 1;
        }
    }

    int treeVerticesCount = 0;
    int[][] treeChildren = new int[M][2];
    int[] rayId = new int[M];
    int[] leftRayId = new int[M];
    int[] rightRayId = new int[M];
    int[] nearestWall = new int[M];

    int n;
    int edge = -1;

    double[][] points = new double[N][2];
    double[][] v = new double[N][2];
    double[][] ray = new double[N][3];
    int[] rayInTree = new int[N];

    static int sign(double x) {
        if(Math.abs(x) < eps) return 0;
        return x < 0 ? -1 : 1;
    }

    static double vect(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    static double Q(double x) {
        return x * x;
    }

    static double dist2(double x1, double y1, double x2, double y2) {
        return Q(x1 - x2) + Q(y1 - y2);
    }

    static double[] findIntersectionPoints(
            double x11, double y11, double x12, double y12,
            double x21, double y21, double x22, double y22) {

        double A1 = y12 - y11;
        double B1 = x11 - x12;
        double C1 = -A1 * x11 - B1 * y11;

        double A2 = y22 - y21;
        double B2 = x21 - x22;
        double C2 = -A2 * x21 - B2 * y21;

        if(Math.abs(A1) < eps) {
            if(Math.abs(A2) < eps) {
                return null;
            }
            double y = -C1 / B1;
            double x = (-C2 - B2 * y) / A2;
            return new double[] {x, y};
        }

        if(Math.abs(B2 - A2 * B1 / A1) < eps) {
            return null;
        }

        double y = (A2 * C1 / A1 - C2) / (B2 - A2 * B1 / A1);
        double x = -B1 / A1 * y - C1 / A1;

        return new double[] {x, y};
    }

    int addVertex(int rayId, int leftRayId, int rightRayId) {
        int vertex = treeVerticesCount++;

        if(rayId != -1) {
            rayInTree[rayId] = vertex;
        }

        this.rayId[vertex] = rayId;
        this.leftRayId[vertex] = leftRayId;
        this.rightRayId[vertex] = rightRayId - 1;
        this.nearestWall[vertex] = -1;
        return vertex;
    }

    void init() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        double sx = Double.parseDouble(scanner.next());
        double sy = Double.parseDouble(scanner.next());
        n = scanner.nextInt();
        for(int i = 0; i < n; i++) {
            points[i][0] = Double.parseDouble(scanner.next()) - sx;
            points[i][1] = Double.parseDouble(scanner.next()) - sy;

            v[i][0] = points[i][0];
            v[i][1] = points[i][1];
        }
        scanner.close();

        java.util.Arrays.sort(v, 0, n, new PointsComparator());

        for (int i = 0; i < n; i++) {
            ray[i][0] = (v[i][0] + v[(i + 1) % n][0]) / 2.0;
            ray[i][1] = (v[i][1] + v[(i + 1) % n][1]) / 2.0;
            ray[i][2] = i;
        }
        java.util.Arrays.sort(ray, 0, n, new PointsByYComparator());
        for (int i = 0; i < n; i++) {
            if (ray[i][1] < 0) {
                edge = i;
                break;
            }
        }
        if(edge < 0) {
            edge = n;
        }
        java.util.Arrays.sort(ray, 0, edge, new PointsComparator());
        java.util.Arrays.sort(ray, edge, n, new PointsComparator());
    }

    int buildTree(int l, int r) {
        if(l >= r) return -1;
        int x = addVertex(r == l + 1 ? l : -1, l, r);
        if(r == l + 1) {
            return x;
        }
        int left = buildTree(l, (l + r) / 2);
        int right = buildTree((l + r) / 2, r);
        treeChildren[x][0] = left;
        treeChildren[x][1] = right;
        return x;
    }

    void buildTree() {
        treeVerticesCount++;
        int left = buildTree(0, edge);
        int right = buildTree(edge, n);
        treeChildren[0][0] = left;
        treeChildren[0][1] = right;
    }

    boolean isWallBetweenRays(double wx1, double wy1, double wx2, double wy2, double rx1, double ry1, double rx2, double ry2) {
        int s = sign(vect(rx1, ry1, rx2, ry2));
        int s1 = sign(vect(rx1, ry1, wx1, wy1));
        int s2 = sign(vect(wx1, wy1, rx2, ry2));
        if(s1 == s && s2 == s) return true;

        s = sign(vect(rx1, ry1, rx2, ry2));
        s1 = sign(vect(rx1, ry1, wx2, wy2));
        s2 = sign(vect(wx2, wy2, rx2, ry2));
        if(s1 == s && s2 == s) return true;

        return false;
    }

    boolean isRayOnWall(double rx, double ry, double wx1, double wy1, double wx2, double wy2) {
        int s = sign(vect(wx1, wy1, wx2, wy2));
        int s1 = sign(vect(wx1, wy1, rx, ry));
        int s2 = sign(vect(rx, ry, wx2, wy2));

        return s1 == s && s2 == s;
    }

    void updateTree(int wallId, int vertex) {

        if(vertex == -1) return;

        if(rayId[vertex] != -1) {
            if(nearestWall[vertex] == -1) {
                if(isRayOnWall(
                        ray[rayId[vertex]][0], ray[rayId[vertex]][1],
                        points[wallId][0], points[wallId][1], points[(wallId + 1) % n][0], points[(wallId + 1) % n][1])) {
                    nearestWall[vertex] = wallId;
                }
                return;
            }
            if(!isRayOnWall(
                    ray[rayId[vertex]][0], ray[rayId[vertex]][1],
                    points[wallId][0], points[wallId][1], points[(wallId + 1) % n][0], points[(wallId + 1) % n][1])) {
                return;
            }
            double[] p1 = findIntersectionPoints(
                    points[wallId][0], points[wallId][1],
                    points[(wallId + 1) % n][0], points[(wallId + 1) % n][1],
                    0, 0,
                    ray[rayId[vertex]][0], ray[rayId[vertex]][1]);
            if(p1 == null) {
                return;
            }
            double[] p2 = findIntersectionPoints(
                    points[nearestWall[vertex]][0], points[nearestWall[vertex]][1],
                    points[(nearestWall[vertex] + 1) % n][0], points[(nearestWall[vertex] + 1) % n][1],
                    0, 0,
                    ray[rayId[vertex]][0], ray[rayId[vertex]][1]);

            if(dist2(0, 0, p1[0], p1[1]) < dist2(0, 0, p2[0], p2[1])) {
                nearestWall[vertex] = wallId;
                return;
            }

            return;
        }

        double[] ray1 = ray[leftRayId[vertex]];
        double[] ray2 = ray[rightRayId[vertex]];

        if(isWallBetweenRays(
                points[wallId][0], points[wallId][1], points[(wallId + 1) % n][0], points[(wallId + 1) % n][1],
                ray1[0], ray1[1], ray2[0], ray2[1]
                )) {
            updateTree(wallId, treeChildren[vertex][0]);
            updateTree(wallId, treeChildren[vertex][1]);
        }

        if(isRayOnWall(
                ray1[0], ray1[1],
                points[wallId][0], points[wallId][1], points[(wallId + 1) % n][0], points[(wallId + 1) % n][1])) {
            updateTree(wallId, treeChildren[vertex][0]);
            updateTree(wallId, treeChildren[vertex][1]);
        }

        if(isRayOnWall(
                ray2[0], ray2[1],
                points[wallId][0], points[wallId][1], points[(wallId + 1) % n][0], points[(wallId + 1) % n][1])) {
            updateTree(wallId, treeChildren[vertex][0]);
            updateTree(wallId, treeChildren[vertex][1]);
        }

    }

    void updateTree(int wallId) {
        if(points[wallId][1] >= 0 || points[(wallId + 1) % n][1] >= 0) {
            updateTree(wallId, treeChildren[0][0]);
        }
        if(points[wallId][1] < 0 || points[(wallId + 1) % n][1] < 0) {
            updateTree(wallId, treeChildren[0][1]);
        }
    }

    void solve() {
        init();
        buildTree();
        for (int i = 0; i < n; i++) {
            updateTree(i);
        }

        /*StringBuilder out = new StringBuilder();
        out.append("{\"lines\":[");
        for(int i = 0; i < n; i++) {
            if(i > 0) {
                out.append(",");
            }
            out.append("\n{");
            out.append("\"x1\": " + String.format("%f", points[i][0]).replace(',', '.') + ",");
            out.append("\"y1\": " + String.format("%f", points[i][1]).replace(',', '.') + ",");
            out.append("\"x2\": " + String.format("%f", points[(i + 1) % n][0]).replace(',', '.') + ",");
            out.append("\"y2\": " + String.format("%f", points[(i + 1) % n][1]).replace(',', '.'));
            out.append("}");
        }*/

        double totalSq = 0;
        for (int i = 0; i < n; i++) {
            int vertexId = rayInTree[i];
            int wallId = nearestWall[vertexId];
            int x = (int) ray[i][2];

            double[] p1 = findIntersectionPoints(
                    points[wallId][0], points[wallId][1],
                    points[(wallId + 1) % n][0], points[(wallId + 1) % n][1],
                    0, 0, v[x][0], v[x][1]);
            double[] p2 = findIntersectionPoints(
                    points[wallId][0], points[wallId][1],
                    points[(wallId + 1) % n][0], points[(wallId + 1) % n][1],
                    0, 0,
                    v[(x + 1) % n][0], v[(x + 1) % n][1]);

            double sq = vect(p1[0], p1[1], p2[0], p2[1]);
        /*out.append(",\n{");
        out.append("\"x1\": " + String.format("%f", 0.0).replace(',', '.') + ",");
        out.append("\"y1\": " + String.format("%f", 0.0).replace(',', '.') + ",");
        out.append("\"x2\": " + String.format("%f", p1[0]).replace(',', '.') + ",");
        out.append("\"y2\": " + String.format("%f", p1[1]).replace(',', '.'));
        out.append("}");
        out.append(",\n{");
        out.append("\"x1\": " + String.format("%f", 0.0).replace(',', '.') + ",");
        out.append("\"y1\": " + String.format("%f", 0.0).replace(',', '.') + ",");
        out.append("\"x2\": " + String.format("%f", p2[0]).replace(',', '.') + ",");
        out.append("\"y2\": " + String.format("%f", p2[1]).replace(',', '.'));
        out.append("}");*/
            totalSq += sq;
        }
        //out.append("]}");

        System.out.println(String.format("%f", -totalSq / 2.0).replace(',', '.'));

        //System.out.println(out);

    }

    public static void main(String[] args) {
        Light l = new Light();
        l.solve();
    }

}
/*
1.0 1.0
6
0 0
3 0
3 2
2 2
2 3
0 3

0 0
14
-3 5
-3 2
-2 2
-1 5
0 3
3 4
2 1
5 4
2 -3
-1 -3
-1 -6
-4 -6
-3 -2
-5 1
 */
