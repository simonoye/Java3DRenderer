package HelperClasses;
public class Face {
    public Point[] vertices;

    public Face(Point[] veritices) {
        this.vertices = veritices;
    }

    @Override
    public String toString() {
        String output = "";
        for (Point vertex : vertices) {
            output += vertex.toString() + "\n";
        }
        return output;
    }
}