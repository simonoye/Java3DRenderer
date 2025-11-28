public class Renderer {
    Camera camera;
    GUI out;

    public Renderer(Camera camera, GUI out) {
        this.camera = camera;
        this.out = out;
    }

    public void renderMeshPoints(Mesh mesh) {
        for (Point p : mesh.points) {
            Point2D point = projectTo2D(p);
            out.drawPixel(point.x, point.y);
        }
    }

    public void drawMesh(Mesh mesh) {
        int index = 0;
        Point[] verticies;
        for (int numVertices : mesh.numVertices) {
            verticies = new Point[numVertices];
            for (int i = 0; i < numVertices; ++i) {
                verticies[i] = mesh.points[mesh.verticesIndex[i + index]];
            }
            index += numVertices;
            drawFace(new Face(verticies));
        }
    }

    public void drawFace(Face face) {
        for (int i = 0; i < face.vertices.length - 1; i++) {
            drawLine(face.vertices[i], face.vertices[i + 1]);
        }
        drawLine(face.vertices[0], face.vertices[face.vertices.length - 1]);
    }

    public void drawLine(Point p1, Point p2) {
        Point2D p1_2D = projectTo2D(p1);
        Point2D p2_2D = projectTo2D(p2);
        out.drawLine(p1_2D.x, p1_2D.y, p2_2D.x, p2_2D.y);
    }


    public Point2D projectTo2D(Point point) {
        Point relativePoint = toCameraSpace(point, camera);
        return new Point2D( //convert from 3d to 2d through similiar triangles
            relativePoint.x / relativePoint.z, 
            relativePoint.y / relativePoint.z
        );
    }

    public Point toCameraSpace(Point p, Camera cam) {
        // Translate point relative to camera
        p = p.subtract(cam.position);

        double cosY = Math.cos(cam.rotation.y);
        double sinY = Math.sin(cam.rotation.y);
        double cosX = Math.cos(cam.rotation.x);
        double sinX = Math.sin(cam.rotation.x);

        // Yaw rotation (around Y axis)
        double x1 = p.x * cosY + p.z * sinY;
        double z1 = - p.x * sinY + p.z * cosY;

        // Pitch rotation (around X axis)
        double y2 = p.y * cosX - z1 * sinX;
        double z2 = p.y * sinX + z1 * cosX;

        return new Point(x1, y2, z2);
    }
}