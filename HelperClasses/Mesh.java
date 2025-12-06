package HelperClasses;

public class Mesh {
    public Point[] points;
    public int[] numVertices, verticesIndex;

    public Mesh(int[] numVertices, int[] verticesIndex, Point[] points) {
        this.numVertices = numVertices;
        this.verticesIndex = verticesIndex;
        this.points = points;
    }
}