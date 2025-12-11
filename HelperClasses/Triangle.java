package HelperClasses;

public class Triangle {
    public Vec3[] points;
    public Vec3 direction;

    public Triangle(Vec3[] points) {
        this.points = points;
    }

    public Triangle(Vec3[] points, Vec3 direction) {
        this.points = points;
    }
}