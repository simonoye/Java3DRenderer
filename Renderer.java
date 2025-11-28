public class Renderer {
    Camera cam;
    GUI out;

    public Renderer(Camera cam, GUI out) {
        this.cam = cam;
        this.out = out;
    }

    public void drawWireframe(Mesh mesh) {
        int offset = 0;
        Point[] verticies;
        for (int numVertices : mesh.numVertices) {
            verticies = new Point[numVertices];
            for (int i = 0; i < numVertices; ++i) {
                verticies[i] = mesh.points[mesh.verticesIndex[i + offset]];
            }
            offset += numVertices;
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
        if (p1_2D == null) { return; }
        Point2D p2_2D = projectTo2D(p2);
        if (p2_2D == null) { return; }
        
        // System.out.println("a" + p1_2D + " : " + p2_2D);

        out.drawLine(p1_2D.x, p1_2D.y, p2_2D.x, p2_2D.y);
    }

    public Point2D projectTo2D(Point point) {
        Point relativePoint = toCameraSpace(point, cam);
        // System.out.println(relativePoint);
        if (Math.abs(relativePoint.z) < 1e-6) { return null; }
        if (relativePoint.z < 0) { return null; }

        double aspect = out.width / out.height;
        return new Point2D( //convert from 3d to 2d through similiar triangles
            (relativePoint.x / relativePoint.z) / aspect, 
            (relativePoint.y / relativePoint.z)
        );
    }

    public Point toCameraSpace(Point p, Camera cam) {
        // Translate point relative to cam
        p = p.subtract(cam.position);
        // p = cam.position.subtract(p);

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