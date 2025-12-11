package HelperClasses;

public class PixelFace {
    public PixelVec2[] vertices;

    public PixelFace(PixelVec2[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        String output = "";
        for (PixelVec2 point : vertices) {
            output += point.toString() + "\n";
        }
        return output;
    }
}