package HelperClasses;

public class Camera {
    public Point position;
    public Rotation rotation;

    public Camera() {
        this.position = new Point(0, 0, 0);
        this.rotation = new Rotation(0, 0, 0);
    }

    public Camera(Point position) {
        this.position = position;
        this.rotation = new Rotation(0, 0, 0);
    }

    public Camera(Rotation rotation) {
        this.position = new Point(0, 0, 0);
        this.rotation = rotation;
    }

    public Camera(Point position, Rotation rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void rotateAroundOrigin(double t, double radius) {
        position.x = radius * Math.cos(t);
        position.z = radius * Math.sin(t);
        rotation.y = Math.atan2(-position.x, position.z); 
    }
}