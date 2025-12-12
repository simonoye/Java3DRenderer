package HelperClasses;

public class ProjVec3 {
    public double x;
    public double y;
    public double z;
    public Vec3 normal;
    public int rgb;

    public ProjVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        rgb = -1;
    }
    
    public ProjVec3(double x, double y, double z, int rgb) {
        this(x,y,z);
        this.rgb = rgb;
    }

    public ProjVec3(double x, double y, double z, Vec3 normal, int rgb) {
        this(x,y,z,rgb);
        this.normal = normal;
    }

    public ProjVec3 subtract(ProjVec3 other) {
        return new ProjVec3(x - other.x, y - other.y, z - other.z);
    }

    public double dot(ProjVec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public ProjVec3 cross(ProjVec3 other) {
        return new ProjVec3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }
    public String toString() {
        return String.format("x: %.2f, y: %.2f, z: %.2f", x, y, z);
    }
}