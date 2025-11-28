import java.util.ArrayList;

public class Shapes {
    static double phi = (1 + Math.sqrt(5)) / 2;
    
    static Mesh icosahedron() {
        Point[] vertices = new Point[]{
            new Point(-1,  phi, 0),
            new Point( 1,  phi, 0),
            new Point(-1, -phi, 0),
            new Point( 1, -phi, 0),
            
            new Point(0, -1,  phi),
            new Point(0,  1,  phi),
            new Point(0, -1, -phi),
            new Point(0,  1, -phi),
            
            new Point( phi, 0, -1),
            new Point( phi, 0,  1),
            new Point(-phi, 0, -1),
            new Point(-phi, 0,  1)
        };
    
        // Each face is a triangle (3 vertex indices)
        int[] faces = new int[]{
            0,11,5,
            0,5,1,
            0,1,7,
            0,7,10,
            0,10,11,
            
            1,5,9,
            5,11,4,
            11,10,2,
            10,7,6,
            7,1,8,
            
            3,9,4,
            3,4,2,
            3,2,6,
            3,6,8,
            3,8,9,
            
            4,9,5,
            2,4,11,
            6,2,10,
            8,6,7,
            9,8,1
        };

        int[] arr = new int[20];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 3;
        }
        return new Mesh(
            arr,
            faces,
            vertices
        );
    }

    static Mesh prism() {
        return new Mesh(
            new int[]{3,3,4,4,4,4}, // 2 triangles, 4 quads
            new int[]{
                0,1,2,  // front triangle
                3,4,5,  // back triangle

                0,1,4,3,  // side
                1,2,5,4,  // side
                2,0,3,5,  // side
                3,4,5,3   // back bottom (optional)
            },
            new Point[]{
                new Point(-1, -1,  1), // 0
                new Point( 1, -1,  1), // 1
                new Point( 0,  1,  1), // 2

                new Point(-1, -1, -1), // 3
                new Point( 1, -1, -1), // 4
                new Point( 0,  1, -1), // 5
            }
        );
    }

    static Mesh octohedron() {
        return new Mesh(
            new int[]{3,3,3,3,3,3,3,3}, // 8 triangular faces
            new int[]{
                0,1,4,
                1,3,4,
                3,2,4,
                2,0,4,

                1,0,5,
                3,1,5,
                2,3,5,
                0,2,5
            },
            new Point[]{
                new Point(-1, 0,  1), // 0
                new Point( 1, 0,  1), // 1
                new Point(-1, 0, -1), // 2
                new Point( 1, 0, -1), // 3
                new Point( 0, 1,  0), // 4 top
                new Point( 0, -1, 0), // 5 bottom
            }
        );
    }

    static Mesh cube() {
        return new Mesh(
            new int[]{4,4,4,4,4,4},
            new int[]{
                2,3,7,6,
                0,4,5,1,
                0,2,6,4,
                1,3,7,5,
                0,2,3,1,
                4,6,7,5
            },
            new Point[]{
            new Point(-1,-1, 1),
            new Point( 1,-1, 1),
            new Point(-1, 1, 1),
            new Point( 1, 1, 1),
            new Point(-1,-1,-1),
            new Point( 1,-1,-1),
            new Point(-1, 1,-1),
            new Point( 1, 1,-1),
        });
    }

    static Mesh torus() {
        int radial = 32;    // slices around the circle
        int tubular = 32;   // slices around the tube
        Point[] points = new Point[radial * tubular];
        int[] numVertices = new int[radial * tubular];
        int[] verticesIndex = new int[radial * tubular * 4]; // quads

        double R = 2; // major radius
        double r = 0.5; // minor radius

        int idx = 0;
        for (int i = 0; i < radial; i++) {
            double theta = i * 2 * Math.PI / radial;
            for (int j = 0; j < tubular; j++) {
                double phi = j * 2 * Math.PI / tubular;
                double x = r * Math.sin(phi);
                double y = (R + r * Math.cos(phi)) * Math.cos(theta);
                double z = (R + r * Math.cos(phi)) * Math.sin(theta);
                points[i * tubular + j] = new Point(x, y, z);

                // define quads (four vertices each)
                int nextI = (i + 1) % radial;
                int nextJ = (j + 1) % tubular;
                verticesIndex[idx++] = i * tubular + j;
                verticesIndex[idx++] = nextI * tubular + j;
                verticesIndex[idx++] = nextI * tubular + nextJ;
                verticesIndex[idx++] = i * tubular + nextJ;
                numVertices[i * tubular + j] = 4;
            }
        }
        return new Mesh(numVertices, verticesIndex, points);
    }

    static Mesh starIcosahedron() {
        Point[] basePoints = new Point[]{
            new Point(-1,  phi,  0),
            new Point( 1,  phi,  0),
            new Point(-1, -phi,  0),
            new Point( 1, -phi,  0),
            new Point( 0, -1,  phi),
            new Point( 0,  1,  phi),
            new Point( 0, -1, -phi),
            new Point( 0,  1, -phi),
            new Point( phi,  0, -1),
            new Point( phi,  0,  1),
            new Point(-phi,  0, -1),
            new Point(-phi,  0,  1),
        };

        Point[] points = new Point[basePoints.length * 2];
        for (int i = 0; i < basePoints.length; i++) {
            points[i] = basePoints[i];                     // base vertex
            points[i + basePoints.length] = basePoints[i].multiply(1.5); // spike
        }

        // Triangles for icosahedron (20 faces, indices to basePoints)
        int[][] faces = new int[][]{
            {0, 11, 5}, {0, 5, 1}, {0, 1, 7}, {0, 7, 10}, {0, 10, 11},
            {1, 5, 9}, {5, 11, 4}, {11, 10, 2}, {10, 7, 6}, {7, 1, 8},
            {3, 9, 4}, {3, 4, 2}, {3, 2, 6}, {3, 6, 8}, {3, 8, 9},
            {4, 9, 5}, {2, 4, 11}, {6, 2, 10}, {8, 6, 7}, {9, 8, 1}
        };

        // Now create spikes: each face will have its "tip" vertex
        int[] numVertices = new int[faces.length * 2];
        int[] verticesIndex = new int[faces.length * 2 * 3];

        for (int i = 0; i < faces.length; i++) {
            // base triangle
            numVertices[i * 2] = 3;
            verticesIndex[i * 6 + 0] = faces[i][0];
            verticesIndex[i * 6 + 1] = faces[i][1];
            verticesIndex[i * 6 + 2] = faces[i][2];

            // spike triangle (connect to "extruded" vertex)
            numVertices[i * 2 + 1] = 3;
            verticesIndex[i * 6 + 3] = faces[i][0] + basePoints.length;
            verticesIndex[i * 6 + 4] = faces[i][1] + basePoints.length;
            verticesIndex[i * 6 + 5] = faces[i][2] + basePoints.length;
        }

        return new Mesh(numVertices, verticesIndex, points);
    }

    static Mesh torusKnot(int steps, double R, double r, int p, int q) {
        Point[] points = new Point[steps];
        for (int i = 0; i < steps; i++) {
            double t = 2 * Math.PI * i / steps;
            double x = (R + r * Math.cos(q * t)) * Math.cos(p * t);
            double y = (R + r * Math.cos(q * t)) * Math.sin(p * t);
            double z = r * Math.sin(q * t);
            points[i] = new Point(x, y, z);
        }

        // Triangles connecting sequential points to form "tube" faces
        int numFaces = steps;
        int[] numVertices = new int[numFaces * 2]; // 2 triangles per quad
        int[] verticesIndex = new int[numFaces * 6];

        for (int i = 0; i < steps; i++) {
            int next = (i + 1) % steps;
            // triangle 1
            numVertices[i * 2] = 3;
            verticesIndex[i * 6 + 0] = i;
            verticesIndex[i * 6 + 1] = next;
            verticesIndex[i * 6 + 2] = (i + steps / 10) % steps; // offset for width
            // triangle 2
            numVertices[i * 2 + 1] = 3;
            verticesIndex[i * 6 + 3] = next;
            verticesIndex[i * 6 + 4] = (next + steps / 10) % steps;
            verticesIndex[i * 6 + 5] = (i + steps / 10) % steps;
        }

        return new Mesh(numVertices, verticesIndex, points);
    }

    static Mesh sierpinskiTetrahedron(int level) {
        Point[] basePoints = new Point[]{
            new Point(1, 1, 1),
            new Point(-1, -1, 1),
            new Point(-1, 1, -1),
            new Point(1, -1, -1)
        };

        ArrayList<Point> pointsList = new ArrayList<>();
        ArrayList<int[]> facesList = new ArrayList<>();

        subdivideTetra(basePoints[0], basePoints[1], basePoints[2], basePoints[3], level, pointsList, facesList);

        int[] numVertices = new int[facesList.size()];
        int[] verticesIndex = new int[facesList.size() * 3];

        for (int i = 0; i < facesList.size(); i++) {
            numVertices[i] = 3;
            int[] f = facesList.get(i);
            verticesIndex[i * 3] = f[0];
            verticesIndex[i * 3 + 1] = f[1];
            verticesIndex[i * 3 + 2] = f[2];
        }

        Point[] points = pointsList.toArray(new Point[0]);
        return new Mesh(numVertices, verticesIndex, points);
    }

    static void subdivideTetra(Point a, Point b, Point c, Point d, int level, ArrayList<Point> pointsList, ArrayList<int[]> facesList) {
        if (level == 0) {
            int baseIndex = pointsList.size();
            pointsList.add(a);
            pointsList.add(b);
            pointsList.add(c);
            pointsList.add(d);
            // 4 triangular faces of tetrahedron
            facesList.add(new int[]{baseIndex, baseIndex+1, baseIndex+2});
            facesList.add(new int[]{baseIndex, baseIndex+1, baseIndex+3});
            facesList.add(new int[]{baseIndex, baseIndex+2, baseIndex+3});
            facesList.add(new int[]{baseIndex+1, baseIndex+2, baseIndex+3});
            return;
        }
        // midpoints
        Point ab = a.add(b).multiply(0.5);
        Point ac = a.add(c).multiply(0.5);
        Point ad = a.add(d).multiply(0.5);
        Point bc = b.add(c).multiply(0.5);
        Point bd = b.add(d).multiply(0.5);
        Point cd = c.add(d).multiply(0.5);

        subdivideTetra(a, ab, ac, ad, level-1, pointsList, facesList);
        subdivideTetra(ab, b, bc, bd, level-1, pointsList, facesList);
        subdivideTetra(ac, bc, c, cd, level-1, pointsList, facesList);
        subdivideTetra(ad, bd, cd, d, level-1, pointsList, facesList);
    }
}