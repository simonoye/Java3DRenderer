package HelperClasses;

public class Mesh {
    public Point[] points;
    public int[] numVertices, verticesIndex;
    public int[] colorRGB;

    public Mesh(int[] numVertices, int[] verticesIndex, Point[] points) {
        this.numVertices = numVertices;
        this.verticesIndex = verticesIndex;
        this.points = points;
    }
    public Mesh(int[] numVertices, int[] colorRGB, int[] verticesIndex, Point[] points) {
        this.numVertices = numVertices;
        this.colorRGB = colorRGB;
        this.verticesIndex = verticesIndex;
        this.points = points;
    }
}