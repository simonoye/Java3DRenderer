package HelperClasses;
public class ProjFace {
    public ProjPoint[] vertices;
    public int points;

    public ProjFace(ProjPoint[] vertices) {
        this.vertices = vertices;
        this.points = vertices.length;
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