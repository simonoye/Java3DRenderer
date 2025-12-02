package HelperClasses;
public class Face2D {
    public Point2D[] vertices;

    public Face2D(Point2D[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        String output = "";
        for (Point2D vertex : vertices) {
            output += vertex.toString() + "\n";
        }
        return output;
    }

}