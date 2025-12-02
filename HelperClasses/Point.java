package HelperClasses;
public class Point extends Point2D{
    public double z;

    public Point(double x, double y, double z) {
        super(x, y);
        this.z = z;
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
        return super.toString() + String.format(", z: %.2f", z);
    }
}