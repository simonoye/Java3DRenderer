
public class Camera {
    Point position;
    Rotation rotation;

    public Camera() {
        this.position = new Point(0, 0, 0);
        this.rotation = new Rotation(0, 0, 0);
    }

    public Camera(Point position) {
        this.position = position;
    }

    public Camera(Rotation rotation) {
        this.rotation = rotation;
    }

    public Camera(Point position, Rotation rotation) {
        this.position = position;
        this.rotation = rotation;
    }
}