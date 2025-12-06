import java.awt.Color;
import java.util.Arrays;
import javax.swing.JFrame;

import HelperClasses.ProjFace;
import HelperClasses.ProjPoint;
import HelperClasses.PixelPoint;
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
        for (int y = 0; y < height; y++) {
            Arrays.fill(zBuffer[y], Double.MAX_VALUE);
        }
        colorBuffer = new int[height][width];

        panel = new RenderPanel(width, height);
        frame = new JFrame("PIECE OF SHIT");
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void drawFace(ProjFace f) {
        PixelFace face = convertFace(f);

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (PixelPoint p : face.vertices) {
            if (p.x < minX) { minX = p.x; }
            if (p.x > maxX) { maxX = p.x; }
            if (p.y < minY) { minY = p.y; }
            if (p.y > maxY) { maxY = p.y; }
        }
        
        PixelPoint A = face.vertices[0];
        PixelPoint B = face.vertices[1];
        PixelPoint C = face.vertices[2];
        
        double invA = 1f / face.vertices[0].z;
        double invB = 1f / face.vertices[1].z;
        double invC = 1f / face.vertices[2].z;

        int area = edgeFunction(A, B, C);
        // if (area <= 0) { return; }

        // A.rgb = Color.RED.getRGB();
        // B.rgb = Color.BLUE.getRGB();
        // C.rgb = Color.GREEN.getRGB();

        boolean inside;
        for (int y = Math.max(minY, 0); y < Math.min(maxY + 1, height); ++y) { //clamp y bounds
            inside = false;
            for (int x = Math.max(minX, 0); x < Math.min(maxX + 1, width); ++x) { //clamp x bounds
                PixelPoint currPixel = new PixelPoint(x, y, 0);

                double w0 = edgeFunction(currPixel, B, C);
                double w1 = edgeFunction(currPixel, C, A);
                double w2 = edgeFunction(currPixel, A, B);

                if (w0 > 0 || w1 > 0 || w2 > 0) { // if not in triangle
                    if (inside == true) { break; } //small optimisation at end of triangle
                    else { continue; }
                }
                inside = true;

                w0 /= area; // get weight proportions
                w1 /= area;
                w2 /= area;

                double z = 1f / (w0 * invA + w1 * invB + w2 * invC);
                if (z <= zBuffer[y][x]) { 
                    zBuffer[y][x] = z;
                    
                    // int M = 8;

                    // // 2. checkerboard boolean
                    // boolean checker = ((int)(w1 * M) % 2 == 0) ^ ((int)(w2 * M) % 2 == 0);

                    // // 3. assign color
                    // int colorA = Color.WHITE.getRGB();
                    // int colorB = Color.BLACK.getRGB();
                    // int color = checker ? colorA : colorB;


                    int color = interpolateColor(
                        A.rgb,B.rgb, C.rgb,
                        w0, w1, w2, 
                        invA, invB, invC
                    );

                    colorBuffer[y][x] = color;
                }
            }
        }
    }

    private int interpolateColor(
        int rgbA, int rgbB, int rgbC,
        double w0, double w1, double w2,
        double invA, double invB, double invC
    ) {
        double w0p = w0 * invA; // perspective-correct weights
        double w1p = w1 * invB;
        double w2p = w2 * invC;
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

        int r = (int)(rA * w0p + rB * w1p + rC * w2p);
        int g = (int)(gA * w0p + gB * w1p + gC * w2p);
        int b = (int)(bA * w0p + bB * w1p + bC * w2p);

        return (r << 16) | (g << 8) | b;
    }


    private int edgeFunction(PixelPoint p1, PixelPoint p2, PixelPoint p3) {
        return (p3.x - p1.x) * (p2.y - p1.y) - (p3.y - p1.y) * (p2.x - p1.x);
    }

    private PixelFace convertFace(ProjFace face) {
        PixelPoint[] pixelCoordinateVertices = new PixelPoint[face.points];

        for (int i = 0; i < face.points; ++i) {
            pixelCoordinateVertices[i] = new PixelPoint(
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
    
    public void drawLine(ProjPoint p1in, ProjPoint p2in) {        
        PixelPoint p1 = new PixelPoint(getScreenCoordinatesX(p1in.x), getScreenCoordinatesY(p1in.y), p1in.z);
        PixelPoint p2 = new PixelPoint(getScreenCoordinatesX(p2in.x), getScreenCoordinatesY(p2in.y), p2in.z);
    
        int p1Code = getRegionCode(p1.x, p1.y);
        int p2Code = getRegionCode(p2.x, p2.y);
    
        if ((p1Code & p2Code) != INSIDE) { return; } // if on same side of screen

        else if (p1Code != INSIDE && p2Code != INSIDE) { // if both arent inside screen
            if (!lineIntersectsScreen(p1.x, p1.y, p2.x, p2.y)) { return; }
        } 

        if ((p1Code | p2Code) != INSIDE) { // if at least one is outside screen
            float gradient;
            if (p1.x == p2.x) { gradient = Float.MAX_VALUE; }
            else { gradient = (float)(p2.y - p1.y) / (float)(p2.x - p1.x); }

            p1 = clipPoint(p1, gradient);
            p2 = clipPoint(p2, gradient);
        }
    
        if (p1 != null && p2 != null) {
            bresenham(p1, p2);
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

    private PixelPoint clipPoint(PixelPoint p, float gradient) {
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
        return new PixelPoint(nx, ny, p.z);
    }

    private void bresenham(PixelPoint p1, PixelPoint p2) {
        // System.err.println("p1: " + p1);
        // System.err.println("p2: " + p2);
        int dx = Math.abs(p2.x - p1.x);
        int dy = Math.abs(p2.y - p1.y);
        
        int sx = p1.x < p2.x ? 1 : -1; // get direction to move
        int sy = p1.y < p2.y ? 1 : -1;
        
        int err = dx - dy;

        int x = p1.x;
        int y = p1.y;
        
        while (x != p2.x || y != p2.y) {    
            // System.err.println("x: " + x);
            // System.err.println("y: " + y);
            if (x > width - 1) { break; }
            if (x < 0) { break; }
            if (y > height - 1) { break; }
            if (y < 0) { break; }
            
            zBuffer[y][x] = 1;
            colorBuffer[y][x] = Color.RED.getRGB();
            // panel.setPixel(x, y, Color.RED.getRGB());
            
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
        return (int) (-y * height + height / 2);
    }
}