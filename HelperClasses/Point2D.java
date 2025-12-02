package HelperClasses;
public class Point2D {
    public double x;
    public double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("x: %.2f, y: %.2f", x, y);
    }

    public Point2D add(Point2D other) {
        return new Point2D(x + other.x, y + other.y);
    }

    public Point2D subtract(Point2D other) {
        return new Point2D(x - other.x, y - other.y);
    }
}