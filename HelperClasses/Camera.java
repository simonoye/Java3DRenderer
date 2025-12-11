package HelperClasses;

public class Camera {
    public Vec3 position;
    public Rotation rotation;
    double tx;
    double ty;

    public Camera() {
        this.position = new Vec3(0, 0, 0);
        this.rotation = new Rotation(0, 0, 0);
        tx = 0;
        ty = 0;
    }

    public Camera(Vec3 position) {
        this.position = position;
        this.rotation = new Rotation(0, 0, 0);
    }

    public Camera(Rotation rotation) {
        this.position = new Vec3(0, 0, 0);
        this.rotation = rotation;
    }

    public Camera(Vec3 position, Rotation rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void rotateAroundOrigin(double tx, double ty, double radius) {
        tx += this.tx;
        ty += this.ty;
        this.tx = tx;
        this.ty = ty;
        
        this.ty = Math.max(-Math.PI / 2 + 0.01, Math.min(Math.PI / 2 - 0.01, this.ty));

        // Convert spherical coordinates to Cartesian
        position.x = radius * Math.cos(ty) * Math.sin(tx);
        position.y = radius * Math.sin(ty);
        position.z = radius * Math.cos(ty) * Math.cos(tx);

        // Look at origin
        rotation.y = Math.atan2(-position.x, position.z);
        rotation.x = Math.atan2(position.y, Math.sqrt(position.x * position.x + position.z * position.z));
    }
}