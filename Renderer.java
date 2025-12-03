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
            int color = (mesh.colorRGB == null) ? 0x00000 : mesh.colorRGB[faceNum];
            drawFace(new Face(vertices), color); 
        }
    }

    private void drawFace(Face face, int rgb) {
        ProjPoint[] face2DVertices = new ProjPoint[face.vertices.length];

        for (int i = 0; i < face2DVertices.length; ++i) {
            face2DVertices[i] = projectTo2D(face.vertices[i]);
            if (face2DVertices[i] == null) { return; }
        }
        out.drawFace(new ProjFace(face2DVertices), rgb);
    }  

    private void drawWireFace(Face face) {
        for (int i = 0; i < face.vertices.length - 1; i++) {
            drawLine(face.vertices[i], face.vertices[i + 1]);
        }
        drawLine(face.vertices[0], face.vertices[face.vertices.length - 1]);
    }

    private void drawLine(Point p1, Point p2) {
        ProjPoint p1_2D = projectTo2D(p1);
        if (p1_2D == null) { return; }
        ProjPoint p2_2D = projectTo2D(p2);
        if (p2_2D == null) { return; }
        
        out.drawLine(p1_2D.x, p1_2D.y, p2_2D.x, p2_2D.y);
    }

    private ProjPoint projectTo2D(Point point) {
        Point relativePoint = toCameraSpace(point, cam);

        if (Math.abs(relativePoint.z) < 1e-6) { return null; }
        if (relativePoint.z > 0) { return null; }

        return new ProjPoint( //convert from 3d to 2d through similiar triangles
            (relativePoint.x / relativePoint.z) / aspect, 
            (relativePoint.y / relativePoint.z),
            -relativePoint.z
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