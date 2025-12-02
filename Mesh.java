import java.awt.Color;

public class Mesh {
    Point[] points;
    int[] numVertices, verticesIndex;
    Color[] colors;

    public Mesh(int[] numVertices, int[] verticesIndex, Point[] points) {
        this.numVertices = numVertices;
        this.verticesIndex = verticesIndex;
        this.points = points;
    }
    public Mesh(int[] numVertices, Color[] colors, int[] verticesIndex, Point[] points) {
        this.numVertices = numVertices;
        this.colors = colors;
        this.verticesIndex = verticesIndex;
        this.points = points;
    }
}