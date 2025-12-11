import HelperClasses.Camera;
import HelperClasses.Face;
import HelperClasses.ProjFace;
import HelperClasses.Mesh;
import HelperClasses.OBJ;
import HelperClasses.Vec3;
import HelperClasses.ProjVec3;
import HelperClasses.Triangle;

public class Renderer {
    Camera cam;
    GUI out;
    double aspect;

    public Renderer(Camera cam, GUI out) {
        this.cam = cam;
        this.out = out;
        aspect = (double) out.width / out.height;
    }

    public void drawOBJ(OBJ obj) {
        for (Triangle tri : obj.triangles) {
            drawFace(tri);
        }
    }

    public void drawWireOBJ(OBJ obj) {
        for (Triangle tri : obj.triangles) {
            drawWireFace(tri);
        }
    }

    public void drawWireframe(Mesh mesh) {
        int offset = 0;
        Vec3[] vertices;
        for (int numVertices : mesh.numVertices) {
            vertices = new Vec3[numVertices];
            for (int i = 0; i < numVertices; ++i) {
                vertices[i] = mesh.points[mesh.verticesIndex[i + offset]];
            }
            offset += numVertices;
            drawWireFace(new Face(vertices));
        }
    }

    public void drawMesh(Mesh mesh) {
        int offset = 0;
        Vec3[] vertices;
        for (int faceNum = 0; faceNum < mesh.numVertices.length; faceNum++) {
            vertices = new Vec3[mesh.numVertices[faceNum]];
            for (int i = 0; i < mesh.numVertices[faceNum]; ++i) {
                vertices[i] = mesh.points[mesh.verticesIndex[i + offset]];
            }
            offset += mesh.numVertices[faceNum];
            drawFace(new Face(vertices)); 
        }
    }

    private void drawFace(Triangle tri) {
        ProjVec3[] face2DVertices = new ProjVec3[tri.points.length];

        for (int i = 0; i < face2DVertices.length; ++i) {
            face2DVertices[i] = projectTo2D(tri.points[i]);
            if (face2DVertices[i] == null) { return; }
        }
        out.drawFace(new ProjFace(face2DVertices));
    }

    private void drawFace(Face face) {
        ProjVec3[] face2DVertices = new ProjVec3[face.vertices.length];

        for (int i = 0; i < face2DVertices.length; ++i) {
            face2DVertices[i] = projectTo2D(face.vertices[i]);
            if (face2DVertices[i] == null) { return; }
        }
        out.drawFace(new ProjFace(face2DVertices));
    }  

    private void drawWireFace(Triangle tri) {
        for (int i = 0; i < tri.points.length - 1; i++) {
            drawLine(tri.points[i], tri.points[i + 1]);
        }
        drawLine(tri.points[0], tri.points[tri.points.length - 1]);
    }

    private void drawWireFace(Face face) {
        for (int i = 0; i < face.vertices.length - 1; i++) {
            drawLine(face.vertices[i], face.vertices[i + 1]);
        }
        drawLine(face.vertices[0], face.vertices[face.vertices.length - 1]);
    }

    private void drawLine(Vec3 p1in, Vec3 p2in) {
        ProjVec3 p1 = projectTo2D(p1in);
        if (p1 == null) { return; }
        ProjVec3 p2 = projectTo2D(p2in);
        if (p2 == null) { return; }
        
        out.drawLine(p1, p2);
    }

    public void drawAxis(double length, int steps) {
        drawLine(new Vec3(0,length,0), new Vec3(0,-length,0));
        drawLine(new Vec3(length,0,0), new Vec3(-length,0,0));
        drawLine(new Vec3(0,0,length), new Vec3(0,0,-length));

        for (int i = -steps; i <= steps; ++i) {
            if (i == 0) { continue; }
            drawLine( // y
                new Vec3( 0.75f / steps,  i * length / steps, 0),
                new Vec3(-0.75f / steps,  i * length / steps, 0)
            );
            drawLine( // x
                new Vec3(i * length / steps, 0,  0.75f / steps),
                new Vec3(i * length / steps, 0, -0.75f / steps)
            );
            drawLine( // z
                new Vec3( 0.75f / steps, 0, i * length / steps),
                new Vec3(-0.75f / steps, 0, i * length / steps)
            );
        }
    }

    private ProjVec3 projectTo2D(Vec3 point) {
        Vec3 relativePoint = toCameraSpace(point, cam);

        if (Math.abs(relativePoint.z) < 1e-6) { return null; }
        if (relativePoint.z > 0) { return null; }

        return new ProjVec3( //convert from 3d to 2d through similiar triangles
            (relativePoint.x / relativePoint.z) / aspect, 
            -(relativePoint.y / relativePoint.z),
            -relativePoint.z,
            point.rgb
        );
    }

    private Vec3 toCameraSpace(Vec3 p, Camera cam) {
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

        return new Vec3(x1, y2, z2);
    }
}