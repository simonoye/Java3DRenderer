import HelperClasses.Camera;
import HelperClasses.ProjFace;
import HelperClasses.OBJ;
import HelperClasses.Vec3;
import HelperClasses.ProjVec3;
import HelperClasses.Triangle;

public class Renderer {
    Camera cam;
    GUI out;
    double aspect;
    boolean smoothShading;

    public Renderer(Camera cam, GUI out, boolean smoothShading) {
        this.cam = cam;
        this.out = out;
        aspect = (double) out.width / out.height;
        this.smoothShading = smoothShading;
    }

    public void drawOBJ(OBJ obj) {
        for (Triangle tri : obj.triangles) {
            drawTriangle(tri);
        }
    }

    public void drawWireOBJ(OBJ obj) {
        for (Triangle tri : obj.triangles) {
            drawWireFace(tri);
        }
    }

    private void drawTriangle(Triangle tri) {
        ProjVec3[] projTri = new ProjVec3[tri.points.length];

        for (int i = 0; i < projTri.length; ++i) {
            projTri[i] = projectTo2D(tri.points[i]);
            if (projTri[i] == null) { return; }
        }

        if (smoothShading) {
            out.drawFace(new ProjFace(projTri), true);
        }

        else { 
            Vec3 normal = normalToCamera(tri.normal);
            if (normal.z < 0) { // if facing away
                return;
            }
            out.drawFace(new ProjFace(projTri, normal), false);
        }
    }

    private Vec3 normalToCamera(Vec3 p) {
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

    private void drawWireFace(Triangle tri) {
        for (int i = 0; i < tri.points.length - 1; i++) {
            drawLine(tri.points[i], tri.points[i + 1]);
        }
        drawLine(tri.points[0], tri.points[tri.points.length - 1]);
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
        if (point.normal == null) { point.normal = new Vec3(0, 0, 0); }
        Vec3 relativePoint = toCameraSpace(point, cam);

        if (relativePoint.z > -1e-6) { return null; }
        // if (Math.abs(relativePoint.z) < 1e-6) { return null; }

        return new ProjVec3( //convert from 3d to 2d through similiar triangles
            (relativePoint.x / relativePoint.z) / aspect, 
            -(relativePoint.y / relativePoint.z),
            -relativePoint.z,
            normalToCamera(point.normal),
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