package HelperClasses;
public class Face {
    public Vec3[] vertices;

    public Face(Vec3[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        String output = "";
        for (Vec3 vertex : vertices) {
            output += vertex.toString() + "\n";
        }
        return output;
    }
}