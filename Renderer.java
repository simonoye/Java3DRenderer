public class Renderer {
    Camera camera;
    GUI out;

    public Renderer(Camera camera, GUI out) {
        this.camera = camera;
        this.out = out;
    }

    public void renderMesh(Mesh mesh) {
        for (Point p : mesh.points) {
            Point2D point = projectTo2D(p);
            out.drawPoint(point.x, point.y);
        }
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