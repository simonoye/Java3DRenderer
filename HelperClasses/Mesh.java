package HelperClasses;
import java.awt.Color;

public class Mesh {
    public Point[] points;
    public int[] numVertices, verticesIndex;
    public Color[] colors;

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