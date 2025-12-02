package HelperClasses;
public class Face2D {
    public ProjPoint[] vertices;

    public Face2D(ProjPoint[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        String output = "";
        for (ProjPoint vertex : vertices) {
            output += vertex.toString() + "\n";
        }
        return output;
    }

}