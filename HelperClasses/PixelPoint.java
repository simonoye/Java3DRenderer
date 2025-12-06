package HelperClasses;

public class PixelPoint {
    public int x;
    public int y;
    public double z;
    public int rgb;

    public PixelPoint(int x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public PixelPoint(int x, int y, double z, int rgb) {
        this(x,y,z);
        this.rgb = rgb;
    }

    public String toString() {
        return String.format("x: %d, y: %d, z: %.2f", x, y, z);
    }
}