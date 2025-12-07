import HelperClasses.Camera;
import HelperClasses.Face;
import HelperClasses.ProjFace;
import HelperClasses.Mesh;
import HelperClasses.Point;
import HelperClasses.ProjPoint;

public class Renderer {
    Camera cam;
    GUI out;
    double aspect;

    public Renderer(Camera cam, GUI out) {
        this.cam = cam;
        this.out = out;
        aspect = (double) out.width / out.height;
    }

    public void drawWireframe(Mesh mesh) {
        int offset = 0;
        Point[] vertices;
        for (int numVertices : mesh.numVertices) {
            vertices = new Point[numVertices];
            for (int i = 0; i < numVertices; ++i) {
                vertices[i] = mesh.points[mesh.verticesIndex[i + offset]];
            }
            offset += numVertices;
            drawWireFace(new Face(vertices));
        }
    }

    public void drawMesh(Mesh mesh) {
        int offset = 0;
        Point[] vertices;
        for (int faceNum = 0; faceNum < mesh.numVertices.length; faceNum++) {
            vertices = new Point[mesh.numVertices[faceNum]];
            for (int i = 0; i < mesh.numVertices[faceNum]; ++i) {
                vertices[i] = mesh.points[mesh.verticesIndex[i + offset]];
            }
            offset += mesh.numVertices[faceNum];
            drawFace(new Face(vertices)); 
        }
    }

    private void drawFace(Face face) {
        ProjPoint[] face2DVertices = new ProjPoint[face.vertices.length];

        for (int i = 0; i < face2DVertices.length; ++i) {
            face2DVertices[i] = projectTo2D(face.vertices[i]);
            if (face2DVertices[i] == null) { return; }
        }
        out.drawFace(new ProjFace(face2DVertices));
    }  

    private void drawWireFace(Face face) {
        for (int i = 0; i < face.vertices.length - 1; i++) {
            drawLine(face.vertices[i], face.vertices[i + 1]);
        }
        drawLine(face.vertices[0], face.vertices[face.vertices.length - 1]);
    }

    private void drawLine(Point p1in, Point p2in) {
        ProjPoint p1 = projectTo2D(p1in);
        if (p1 == null) { return; }
        ProjPoint p2 = projectTo2D(p2in);
        if (p2 == null) { return; }
        
        out.drawLine(p1, p2);
    }

    public void drawAxis(double length, int steps) {
        drawLine(new Point(0,length,0), new Point(0,-length,0));
        drawLine(new Point(length,0,0), new Point(-length,0,0));
        drawLine(new Point(0,0,length), new Point(0,0,-length));

        for (int i = -steps; i <= steps; ++i) {
            if (i == 0) { continue; }
            drawLine( // y
                new Point( 0.75f / steps,  i * length / steps, 0),
                new Point(-0.75f / steps,  i * length / steps, 0)
            );
            drawLine(
                new Point(i * length / steps, 0,  0.75f / steps),
                new Point(i * length / steps, 0, -0.75f / steps)
            );
            drawLine(
                new Point( 0.75f / steps, 0, i * length / steps),
                new Point(-0.75f / steps, 0, i * length / steps)
            );
        }
    }

    private ProjPoint projectTo2D(Point point) {
        Point relativePoint = toCameraSpace(point, cam);

        if (Math.abs(relativePoint.z) < 1e-6) { return null; }
        if (relativePoint.z > 0) { return null; }

        return new ProjPoint( //convert from 3d to 2d through similiar triangles
            (relativePoint.x / relativePoint.z) / aspect, 
            -(relativePoint.y / relativePoint.z),
            -relativePoint.z,
            point.rgb
        );
    }

    private Point toCameraSpace(Point p, Camera cam) {
        p = p.subtract(cam.position);

        double cosY = Math.cos(cam.rotation.y);
        double sinY = Math.sin(cam.rotation.y);
        double cosX = Math.cos(cam.rotation.x);
        double sinX = Math.sin(cam.rotation.x);

        // y axis rotation
        double x1 = p.x * cosY + p.z * sinY;
        double z1 = - p.x * sinY + p.z * cosY;

        // x axis rotation
        double y2 = p.y * cosX - z1 * sinX;
        double z2 = p.y * sinX + z1 * cosX;

        return new Point(x1, y2, z2);
    }
}