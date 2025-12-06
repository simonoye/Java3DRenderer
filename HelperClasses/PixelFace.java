package HelperClasses;

public class PixelFace {
    public PixelPoint[] vertices;

    public PixelFace(PixelPoint[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        String output = "";
        for (PixelPoint point : vertices) {
            output += point.toString() + "\n";
        }
        return output;
    }
}