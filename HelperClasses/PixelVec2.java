package HelperClasses;

public class PixelVec2 {
    public int x;
    public int y;
    public double z;
    public int rgb;

    public PixelVec2(int x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        rgb = -1;
    }
    
    public PixelVec2(int x, int y, double z, int rgb) {
        this(x,y,z);
        this.rgb = rgb;
    }

    public String toString() {
        return String.format("x: %d, y: %d, z: %.2f", x, y, z);
    }
}