package HelperClasses;

public class ProjPoint {
    public double x;
    public double y;
    public double z;

    public ProjPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ProjPoint subtract(ProjPoint other) {
        return new ProjPoint(x - other.x, y - other.y, z - other.z);
    }

    public double dot(ProjPoint other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public String toString() {
        return String.format("x: %.2f, y: %.2f, z: %.2f", x, y, z);
    }
}