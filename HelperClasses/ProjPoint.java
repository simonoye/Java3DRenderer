package HelperClasses;

public class ProjPoint {
    public double x;
    public double y;
    public double z;
    public int rgb;

    public ProjPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        rgb = -1;
    }
    
    public ProjPoint(double x, double y, double z, int rgb) {
        this(x,y,z);
        this.rgb = rgb;
    }

    public ProjPoint subtract(ProjPoint other) {
        return new ProjPoint(x - other.x, y - other.y, z - other.z);
    }

    public double dot(ProjPoint other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public ProjPoint cross(ProjPoint other) {
        return new ProjPoint(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }
    public String toString() {
        return String.format("x: %.2f, y: %.2f, z: %.2f", x, y, z);
    }
}