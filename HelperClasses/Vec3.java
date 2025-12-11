package HelperClasses;
public class Vec3 {
    public double x;
    public double y;
    public double z;
    public int rgb;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        rgb = -1;
    }
    
    public Vec3(double x, double y, double z, int rgb) {
        this(x,y,z);
        this.rgb = rgb;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 multiply(double scalar) {
        return new Vec3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vec3 divide(double scalar) {
        return new Vec3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public double dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        );
    }

    @Override
    public String toString() {
        return String.format("x: %.2f, y: %.2f, z: %.2f", x,y,z);
    }
}