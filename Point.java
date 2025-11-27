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

    @Override
    public String toString() {
        return super.toString() + String.format(", z: %.2f", z);
    }
}