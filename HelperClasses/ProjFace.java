package HelperClasses;
public class ProjFace {
    public ProjVec3[] vertices;
    public Vec3 normal;
    public int points;

    public ProjFace(ProjVec3[] vertices) {
        this.vertices = vertices;
        this.points = vertices.length;
    }

    public ProjFace(ProjVec3[] vertices, Vec3 normal) {
        this.vertices = vertices;
        this.normal = normal;
        this.points = vertices.length;
    }

    @Override
    public String toString() {
        String output = "";
        for (ProjVec3 vertex : vertices) {
            output += vertex.toString() + "\n";
        }
        return output;
    }
}