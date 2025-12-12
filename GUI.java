import java.util.Arrays;
import javax.swing.JFrame;

import HelperClasses.ProjFace;
import HelperClasses.ProjVec3;
import HelperClasses.Vec3;
import HelperClasses.PixelVec2;
import HelperClasses.PixelFace;

public class GUI {
    int width, height;
    RenderPanel panel;
    JFrame frame;
    private double[][] zBuffer;
    private int[][] colorBuffer;

    public GUI(int width, int height) {
        this.width = width;
        this.height = height;

        zBuffer = new double[height][width];
        colorBuffer = new int[height][width];

        panel = new RenderPanel(width, height);
        frame = new JFrame("PIECE OF SHIT");
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void drawFace(ProjFace f, boolean smooth) {
        PixelFace face = convertFace(f);

        PixelVec2 A = face.vertices[0];
        PixelVec2 B = face.vertices[1];
        PixelVec2 C = face.vertices[2];
        
        int area = edgeFunction(A, B, C);
        if (area <= 0) { return; }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (PixelVec2 p : face.vertices) {
            if (p.x < minX) { minX = p.x; }
            if (p.x > maxX) { maxX = p.x; }
            if (p.y < minY) { minY = p.y; }
            if (p.y > maxY) { maxY = p.y; }
        }

        int Argb = -1;
        int Brgb = -1;
        int Crgb = -1;
        if (smooth) {
            Argb = normalToColor(f.vertices[0].normal);
            Brgb = normalToColor(f.vertices[1].normal);
            Crgb = normalToColor(f.vertices[2].normal);
        }
        
        boolean inside;
        for (int y = Math.max(minY, 0); y < Math.min(maxY + 1, height); ++y) { //clamp y bounds
            inside = false;
            for (int x = Math.max(minX, 0); x < Math.min(maxX + 1, width); ++x) { //clamp x bounds
                int Ax = (A.x - x);
                int Bx = (B.x - x);
                int Cx = (C.x - x);

                int Ay = (A.y - y);
                int By = (B.y - y);
                int Cy = (C.y - y);
                
                double w0 = Cy * Bx - Cx * By;
                double w1 = Ay * Cx - Ax * Cy;
                double w2 = By * Ax - Bx * Ay;

                if (w0 < 0 || w1 < 0 || w2 < 0) { // if not in triangle
                    if (inside == true) { break; } //small optimisation at end of triangle
                    else { continue; }
                }
                inside = true;

                w0 /= area; // get weights 
                w1 /= area;
                w2 /= area;

                double w0p = w0 / A.z;
                double w1p = w1 / B.z;
                double w2p = w2 / C.z;

                double z = 1f / (w0p + w1p + w2p);
                if (z < zBuffer[y][x]) { 
                    zBuffer[y][x] = z;

                    int color = (smooth) ? 
                        interpolateColor(Argb, Brgb, Crgb, w0p, w1p, w2p)
                        : normalToColor(f.normal);

                    colorBuffer[y][x] = color;
                }
            }
        }
    }

    private int normalToColor(Vec3 normal) {
        double angle = Math.acos(normal.z / normal.magnitude()); // dot product but look dir is (0, 0, -1)
        // 0° = white, 90° or pi/2 = black 
        int color = clamp((int)((1 - angle / (Math.PI / 2)) * 255));

        return (color << 16) | (color << 8) | color;
    }

    private int interpolateColor(
        int rgbA, int rgbB, int rgbC,
        double w0p, double w1p, double w2p
    ) {
        double sum = w0p + w1p + w2p;

        w0p /= sum;
        w1p /= sum;
        w2p /= sum;

        int rA = (rgbA >> 16) & 0xFF; // extract channels
        int gA = (rgbA >> 8) & 0xFF;
        int bA = rgbA & 0xFF;

        int rB = (rgbB >> 16) & 0xFF;
        int gB = (rgbB >> 8) & 0xFF;
        int bB = rgbB & 0xFF;

        int rC = (rgbC >> 16) & 0xFF;
        int gC = (rgbC >> 8) & 0xFF;
        int bC = rgbC & 0xFF;

        int r = clamp((int)(rA * w0p + rB * w1p + rC * w2p));
        int g = clamp((int)(gA * w0p + gB * w1p + gC * w2p));
        int b = clamp((int)(bA * w0p + bB * w1p + bC * w2p));

        return (r << 16) | (g << 8) | b;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private int edgeFunction(PixelVec2 p1, PixelVec2 p2, PixelVec2 p3) {
        return (p3.y - p1.y) * (p2.x - p1.x) - (p3.x - p1.x) * (p2.y - p1.y);
    }

    private PixelFace convertFace(ProjFace face) {
        PixelVec2[] pixelCoordinateVertices = new PixelVec2[face.points];

        for (int i = 0; i < face.points; ++i) {
            pixelCoordinateVertices[i] = new PixelVec2(
                getScreenCoordinatesX(face.vertices[i].x), 
                getScreenCoordinatesY(face.vertices[i].y), 
                face.vertices[i].z, 
                face.vertices[i].rgb
            );
        }
        
        return new PixelFace(pixelCoordinateVertices);
    }

    public void drawBuffer() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (zBuffer[y][x] < Double.MAX_VALUE) {
                    panel.setPixel(x, y, colorBuffer[y][x]);
                } else {
                    panel.setPixel(x, y, 0xFF0D1519);
                }
            }
        }
    }
    
    public void clearBuffer() { // we dont need to clear color buffer as it gets overidden
        for (int y = 0; y < height; y++) { 
            Arrays.fill(zBuffer[y], Double.MAX_VALUE);
        }
    }

    static final int INSIDE = 0; // 0000
    static final int LEFT   = 1; // 0001
    static final int RIGHT  = 2; // 0010
    static final int BOTTOM = 4; // 0100
    static final int TOP    = 8; // 1000
    
    public void drawLine(ProjVec3 p1in, ProjVec3 p2in) {        
        PixelVec2 p1 = new PixelVec2(getScreenCoordinatesX(p1in.x), getScreenCoordinatesY(p1in.y), p1in.z, p1in.rgb);
        PixelVec2 p2 = new PixelVec2(getScreenCoordinatesX(p2in.x), getScreenCoordinatesY(p2in.y), p2in.z, p2in.rgb);
    
        int p1Code = getRegionCode(p1.x, p1.y);
        int p2Code = getRegionCode(p2.x, p2.y);
    
        if ((p1Code & p2Code) != INSIDE) { return; } // if on same side of screen

        else if (p1Code != INSIDE && p2Code != INSIDE) { // if both arent inside screen
            if (!lineIntersectsScreen(p1.x, p1.y, p2.x, p2.y)) { return; }
        } 

        PixelVec2 p1c = p1;
        PixelVec2 p2c = p2;
        if ((p1Code | p2Code) != INSIDE) { // if at least one is outside screen
            float gradient;
            if (p1.x == p2.x) { gradient = Float.MAX_VALUE; }
            else { gradient = (float)(p2.y - p1.y) / (float)(p2.x - p1.x); }

            p1c = clipPoint(p1, gradient);
            p2c = clipPoint(p2, gradient);
        }
    
        if (p1 != null && p2 != null && p1c != null && p2c != null) {
            bresenham(p1c, p2c, p1, p2);
        }
    }

    private boolean ccw(int Ax, int Ay, int Bx, int By, int Cx, int Cy) {
        long crossProduct = (long)(Cy - Ay) * (Bx - Ax) - (long)(By - Ay) * (Cx - Ax);
        return crossProduct > 0;
    }

    private boolean lineIntersectsScreen(int x1, int y1, int x2, int y2) {
        boolean intersectsTop = intersectSegments(x1, y1, x2, y2, 0, 0, width, 0);
        boolean intersectsBottom = intersectSegments(x1, y1, x2, y2, 0, height, width, height);
        boolean intersectsLeft = intersectSegments(x1, y1, x2, y2, 0, 0, 0, height);
        boolean intersectsRight = intersectSegments(x1, y1, x2, y2, width, 0, width, height);

        if (intersectsTop || intersectsBottom || intersectsLeft || intersectsRight) {
            return true;
        }
        return false;
    }

    private boolean intersectSegments(int Ax, int Ay, int Bx, int By, int Cx, int Cy, int Dx, int Dy) {        
        return ccw(Ax, Ay, Bx, By, Cx, Cy) != ccw(Ax, Ay, Bx, By, Dx, Dy) && 
            ccw(Cx, Cy, Dx, Dy, Ax, Ay) != ccw(Cx, Cy, Dx, Dy, Bx, By);
    }

    private PixelVec2 clipPoint(PixelVec2 p, float gradient) {
        int nx = p.x;
        int ny = p.y;
        
        final double nearZero = 1e-6;
        
        int code;
        int count = 0;
        while ((code = getRegionCode(nx, ny)) != INSIDE) {
            if (++count > 2) { return null; } // doesnt intersect screen

            if ((code & TOP) != 0) { 
                if (Math.abs(gradient) > nearZero) { // if gradient=0 dont change x
                    nx = Math.round(nx + (0 - ny) / gradient); 
                }
                ny = 0;
            }
            else if ((code & BOTTOM) != 0) {
                if (Math.abs(gradient) > nearZero) {
                     nx = Math.round(nx + (height - 1 - ny) / gradient); 
                    }
                ny = height - 1;
            }
            else if ((code & LEFT) != 0) {
                if (Math.abs(gradient) > nearZero) { 
                    ny = Math.round(ny + (0 - nx) * gradient); 
                }
                nx = 0; 
            }
            else if ((code & RIGHT) != 0) {
                if (Math.abs(gradient) > nearZero) { 
                    ny = Math.round(ny + (width - 1 - nx) * gradient); 
                }
                nx = width - 1; 
            }
        }
        return new PixelVec2(nx, ny, p.z, p.rgb);
    }

    private int interpolateColor2(
        int rgbA, int rgbB,
        PixelVec2 p1, PixelVec2 p2, 
        int x, int y
    ) {
        if (rgbA == -1) { rgbA = 0xA9A9A9; }
        if (rgbB == -1) { rgbB = 0xA9A9A9; }

        int rA = (rgbA >> 16) & 0xFF;
        int gA = (rgbA >> 8) & 0xFF;
        int bA = rgbA & 0xFF;

        int rB = (rgbB >> 16) & 0xFF;
        int gB = (rgbB >> 8) & 0xFF;
        int bB = rgbB & 0xFF;

        int p12x = (p2.x - p1.x);
        int p12y = (p2.y - p1.y);
        int p1Px = (x - p1.x);
        int p1Py = (y - p1.y);

        double t = (double)(p1Px * p12x + p1Py * p12y) / (p12x*p12x + p12y*p12y);

        double wP1 = (1 - t) * (1f / p1.z);
        double wP2 = t * (1f / p2.z);

        double sum = wP1 + wP2;

        wP1 /= sum;
        wP2 /= sum;

        int r = (int)(rA * wP1 + rB * wP2);
        int g = (int)(gA * wP1 + gB * wP2);
        int b = (int)(bA * wP1 + bB * wP2);

        return (r << 16) | (g << 8) | b;
    }

    private void bresenham(PixelVec2 p1c, PixelVec2 p2c, PixelVec2 p1, PixelVec2 p2) {
        int dx = Math.abs(p2c.x - p1c.x);
        int dy = Math.abs(p2c.y - p1c.y);
        
        int sx = p1c.x < p2c.x ? 1 : -1; // get direction to move
        int sy = p1c.y < p2c.y ? 1 : -1;
        
        int err = dx - dy;

        int x = p1c.x;
        int y = p1c.y;
        
        while (x != p2c.x || y != p2c.y) {    
            if (x > width - 1) { break; }
            if (x < 0) { break; }
            if (y > height - 1) { break; }
            if (y < 0) { break; }

            if (zBuffer[y][x] >= Integer.MAX_VALUE - 1) { 
                zBuffer[y][x] = Integer.MAX_VALUE - 1; 
                int color = interpolateColor2(p1.rgb, p2.rgb, p1, p2, x, y);
                colorBuffer[y][x] = color;
            }
            // int color = interpolateColor2(p1.rgb, p2.rgb, p1, p2, x, y);
            // colorBuffer[y][x] = color;
            
            int e2 = 2 * err;
            
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }

    private int getRegionCode(double x, double y) {
        int out = INSIDE;
        if (x >= width) { out |= RIGHT; }
        else if (x < 0) { out |= LEFT; }

        if (y < 0) { out |= TOP; }
        else if (y >= height) { out |= BOTTOM; }
        
        return out;
    }

    private int getScreenCoordinatesX(double x) {
        return (int)Math.round(x * width + width / 2);
    }

    private int getScreenCoordinatesY(double y) {
        return (int)Math.round(-y * height + height / 2);
    }
}