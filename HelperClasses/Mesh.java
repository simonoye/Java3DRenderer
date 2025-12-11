package HelperClasses;

public class Mesh {
    public Vec3[] points;
    public int[] numVertices, verticesIndex;

    public Mesh(int[] numVertices, int[] verticesIndex, Vec3[] points) {
        this.numVertices = numVertices;
        this.verticesIndex = verticesIndex;
        this.points = points;
    }
}