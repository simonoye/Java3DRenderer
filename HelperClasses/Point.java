package HelperClasses;
public class Point {
    public double x;
    public double y;
    public double z;
    public int rgb;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point(double x, double y, double z, int rgb) {
        this(x,y,z);
        this.rgb = rgb;
    }

    public Point add(Point other) {
        return new Point(x + other.x, y + other.y, z + other.z);
    }

    public Point subtract(Point other) {
        return new Point(x - other.x, y - other.y, z - other.z);
    }

    public Point multiply(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Point divide(double scalar) {
        return new Point(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public double dotProduct(Point other) {
        return x * other.x + y * other.y + z * other.z;
    }

    @Override
    public String toString() {
        return String.format("x: %.2f, y: %.2f, z: %.2f", x,y,z);
    }
}