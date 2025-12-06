package HelperClasses;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Shapes {
    static double phi = (1 + Math.sqrt(5)) / 2;

    public static Mesh triangle() {
        return new Mesh(
            new int[]{3}, 
            new int[]{
                0,1,2
            }, 
            new Point[]{
                new Point(-3,-1,-2),
                new Point(-3,-1,0),
                new Point(0,-1,0)
            });
    }

    public static Mesh triangularPyramid() {

        Point[] pts = new Point[]{
            new Point(-1, -0.5, 0, Color.GREEN.getRGB()),
            new Point( 1, -0.5, 0, Color.RED.getRGB()),
            new Point( 0, 2.5 - Math.sqrt(2), 0, Color.BLUE.getRGB()),
            new Point(0, 0.5, 1, Color.PINK.getRGB())
        };

        int[] numVertices = new int[]{
            3, 3, 3, 3   // four triangular faces
        };

        int[] verticesIndex = new int[]{
            0, 2, 1,
            0, 1, 3,
            1, 2, 3,
            2, 0, 3
        };

        return new Mesh(numVertices, verticesIndex, pts);
    }



    public static Mesh bottom_cube() {
        return new Mesh(
            new int[]{4,4},
            new int[]{
                2,3,7,6,  // Back face - OK
                0,2,6,4
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

    public static Mesh cube() {
        return new Mesh(
            new int[]{4,4,4,4,4,4},
            new int[]{
                2,3,1,0,  // Front face (z=1): counter-clockwise from outside
                4,5,7,6,  // Back face (z=-1): counter-clockwise from outside
                6,7,3,2,  // Top face (y=1): counter-clockwise from above
                0,1,5,4,  // Bottom face (y=-1): counter-clockwise from below
                0,4,6,2,  // Left face (x=-1): counter-clockwise from left
                1,3,7,5   // Right face (x=1): counter-clockwise from right
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

    public static Mesh cube2() {
        return new Mesh(
            new int[]{4,4,4,4,4,4},
            new int[]{
                2,3,7,6,  // Back face - OK
                0,1,5,4,  // Front face - FIXED (was 0,4,5,1)
                0,2,6,4,  // Left face - OK
                1,3,7,5,  // Right face - OK
                0,1,3,2,  // Bottom face - FIXED (was 0,2,3,1)
                4,5,7,6   // Top face - FIXED (was 4,6,7,5)
            },
            new Point[]{
                new Point(-1,-1, 1),  // 0
                new Point( 1,-1, 1),  // 1
                new Point(-1, 1, 1),  // 2
                new Point( 1, 1, 1),  // 3
                new Point(-1,-1,-1),  // 4
                new Point( 1,-1,-1),  // 5
                new Point(-1, 1,-1),  // 6
                new Point( 1, 1,-1),  // 7
        });
    }

    
    public static Mesh icosahedron() {
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

    public static Mesh prism() {
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

    public static Mesh octohedron() {
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


    public static Mesh torus() {
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

    public static Mesh starIcosahedron() {
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

    public static Mesh torusKnot(int steps, double R, double r, int p, int q, int tubeSegments) {
        // Generate the curve points
        Point[] curvePoints = new Point[steps];
        for (int i = 0; i < steps; i++) {
            double t = 2 * Math.PI * i / steps;
            double x = (R + r * Math.cos(q * t)) * Math.cos(p * t);
            double y = (R + r * Math.cos(q * t)) * Math.sin(p * t);
            double z = r * Math.sin(q * t);
            curvePoints[i] = new Point(x, y, z);
        }
        
        // Create tube around the curve
        double tubeRadius = 0.2; // Adjust thickness
        int totalPoints = steps * tubeSegments;
        Point[] points = new Point[totalPoints];
        
        for (int i = 0; i < steps; i++) {
            Point current = curvePoints[i];
            Point next = curvePoints[(i + 1) % steps];
            
            // Simple normal approximation (perpendicular to curve direction)
            double dx = next.x - current.x;
            double dy = next.y - current.y;
            double dz = next.z - current.z;
            double len = Math.sqrt(dx*dx + dy*dy + dz*dz);
            dx /= len; dy /= len; dz /= len;
            
            // Create circle of points around this position
            for (int j = 0; j < tubeSegments; j++) {
                double angle = 2 * Math.PI * j / tubeSegments;
                // Perpendicular vectors (simplified - better to use Frenet frame)
                double nx = Math.cos(angle) * tubeRadius;
                double ny = Math.sin(angle) * tubeRadius;
                
                points[i * tubeSegments + j] = new Point(
                    current.x + nx,
                    current.y + ny,
                    current.z
                );
            }
        }
        
        // Create quad faces connecting the tube segments
        int numFaces = steps * tubeSegments;
        int[] numVertices = new int[numFaces];
        int[] verticesIndex = new int[numFaces * 4];
        
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < tubeSegments; j++) {
                int idx = i * tubeSegments + j;
                int nextI = ((i + 1) % steps) * tubeSegments + j;
                int nextJ = i * tubeSegments + ((j + 1) % tubeSegments);
                int nextBoth = ((i + 1) % steps) * tubeSegments + ((j + 1) % tubeSegments);
                
                int faceIdx = idx;
                numVertices[faceIdx] = 4;
                verticesIndex[faceIdx * 4 + 0] = idx;
                verticesIndex[faceIdx * 4 + 1] = nextI;
                verticesIndex[faceIdx * 4 + 2] = nextBoth;
                verticesIndex[faceIdx * 4 + 3] = nextJ;
            }
        }
        
        return new Mesh(numVertices, verticesIndex, points);
    }
    public static Mesh sierpinskiTetrahedron(int level) {
        Point[] basePoints = new Point[]{
            new Point(1, 1, 1),
            new Point(-1, -1, 1),
            new Point(-1, 1, -1),
            new Point(1, -1, -1)
        };
        ArrayList<Point> pointsList = new ArrayList<>();
        ArrayList<int[]> facesList = new ArrayList<>();
        Random r = new Random();
        
        subdivideTetra(basePoints[0], basePoints[1], basePoints[2], basePoints[3], level, pointsList, facesList, r);
        
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
        
    private static void subdivideTetra(Point a, Point b, Point c, Point d, int level, ArrayList<Point> pointsList, ArrayList<int[]> facesList, Random r) {
        if (level == 0) {
            int baseIndex = pointsList.size();
            
            // Add points with random colors
            pointsList.add(new Point(a.x, a.y, a.z, r.nextInt(0x1000000)));
            pointsList.add(new Point(b.x, b.y, b.z, r.nextInt(0x1000000)));
            pointsList.add(new Point(c.x, c.y, c.z, r.nextInt(0x1000000)));
            pointsList.add(new Point(d.x, d.y, d.z, r.nextInt(0x1000000)));
            
            // 4 triangular faces with consistent outward-facing winding
            facesList.add(new int[]{baseIndex, baseIndex+2, baseIndex+1});
            facesList.add(new int[]{baseIndex, baseIndex+1, baseIndex+3});
            facesList.add(new int[]{baseIndex, baseIndex+3, baseIndex+2});
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
        
        subdivideTetra(a, ab, ac, ad, level-1, pointsList, facesList, r);
        subdivideTetra(ab, b, bc, bd, level-1, pointsList, facesList, r);
        subdivideTetra(ac, bc, c, cd, level-1, pointsList, facesList, r);
        subdivideTetra(ad, bd, cd, d, level-1, pointsList, facesList, r);
    }
}