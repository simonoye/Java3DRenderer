import HelperClasses.Camera;
import HelperClasses.Face;
import HelperClasses.Face2D;
import HelperClasses.Mesh;
import HelperClasses.Point;
import HelperClasses.ProjPoint;

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
            drawWireFace(new Face(verticies));
        }
    }

    public void drawMesh(Mesh mesh) {
        int offset = 0;
        Point[] verticies;
        for (int faceNum = 0; faceNum < mesh.numVertices.length; faceNum++) {
            verticies = new Point[mesh.numVertices[faceNum]];
            for (int i = 0; i < mesh.numVertices[faceNum]; ++i) {
                verticies[i] = mesh.points[mesh.verticesIndex[i + offset]];
            }
            offset += mesh.numVertices[faceNum];
            if (mesh.colors == null) { drawFace(new Face(verticies), 0x00000); }
            else { drawFace(new Face(verticies), mesh.colors[faceNum].getRGB()); }
        }
    }

    public void drawFace(Face face, int rgba) {
        ProjPoint[] face2DVertices = new ProjPoint[face.vertices.length];

        for (int i = 0; i < face2DVertices.length; ++i) {
            face2DVertices[i] = projectTo2D(face.vertices[i]);
            if (face2DVertices[i] == null) { return; }
        }
        out.drawFace(new Face2D(face2DVertices), rgba);
    }  


    public void drawWireFace(Face face) {
        for (int i = 0; i < face.vertices.length - 1; i++) {
            drawLine(face.vertices[i], face.vertices[i + 1]);
        }
        drawLine(face.vertices[0], face.vertices[face.vertices.length - 1]);
    }

    public void drawLine(Point p1, Point p2) {
        ProjPoint p1_2D = projectTo2D(p1);
        if (p1_2D == null) { return; }
        ProjPoint p2_2D = projectTo2D(p2);
        if (p2_2D == null) { return; }
        
        out.drawLine(p1_2D.x, p1_2D.y, p2_2D.x, p2_2D.y);
    }

    public ProjPoint projectTo2D(Point point) {
        Point relativePoint = toCameraSpace(point, cam);

        if (Math.abs(relativePoint.z) < 1e-6) { return null; }
        if (relativePoint.z < 0) { return null; }

        double aspect = out.width / out.height;
        return new ProjPoint( //convert from 3d to 2d through similiar triangles
            (relativePoint.x / relativePoint.z) / aspect, 
            (relativePoint.y / relativePoint.z),
            relativePoint.z
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