import java.awt.Color;
import java.util.Arrays;
import javax.swing.JFrame;

import HelperClasses.ProjFace;
import HelperClasses.ProjPoint;

public class GUI {
    int width, height;
    RenderPanel panel;
    JFrame frame;
    double[][] zBuffer;
    int[][] colorBuffer;

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

    public void drawFace(ProjFace face, int rgb) {
        ProjPoint[] pixelCoordinateVertices = new ProjPoint[face.points];

        for (int i = 0; i < face.points; ++i) {
            int x = convertToScreenCoordinatesX(face.vertices[i].x);
            int y = convertToScreenCoordinatesY(face.vertices[i].y);
            
            pixelCoordinateVertices[i] = new ProjPoint(x, y, face.vertices[i].z);
        }
        
        ProjFace newFace = new ProjFace(pixelCoordinateVertices);

        // boolean useClockwise = isClockwise(newFace);

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (ProjPoint p : pixelCoordinateVertices) {
            if (p.x < minX) { minX = (int)p.x; }
            if (p.x > maxX) { maxX = (int)p.x; }
            if (p.y < minY) { minY = (int)p.y; }
            if (p.y > maxY) { maxY = (int)p.y; }
        }

        for (int y = Math.max(minY, 0); y < Math.min(maxY + 1, height); ++y) { //clamp y bounds
            for (int x = Math.max(minX, 0); x < Math.min(maxX + 1, width); ++x) { //clamp x bounds
                if (pixelInsideFace(newFace, x, y, true)) {
                    if (newFace.points == 3) {
                        double z = calculateZOnTriangle(newFace, x, y);
                        if (z <= zBuffer[y][x]) { 
                            zBuffer[y][x] = z;
                            colorBuffer[y][x] = rgb;
                        }
                    }
                }
            }
        }
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
    
    public void clearBuffer() { // we dont need to clear color buffer
        for (int y = 0; y < height; y++) { 
            Arrays.fill(zBuffer[y], Double.MAX_VALUE);
        }
    }
    public double calculateZOnTriangle(ProjFace face, double px, double py) {
        ProjPoint A = face.vertices[0];
        ProjPoint B = face.vertices[1];
        ProjPoint C = face.vertices[2];

        ProjPoint v0 = new ProjPoint(A.x - C.x, A.y - C.y, 0);
        ProjPoint v1 = new ProjPoint(B.x - C.x, B.y - C.y, 0);
        ProjPoint v2 = new ProjPoint(px - C.x, py - C.y, 0);

        double denom = v0.cross(v1).z;
    
        double w1 = v2.cross(v1).z / denom;
        double w2 = v0.cross(v2).z / denom;
        double w0 = 1 - w1 - w2;

        return w0 * A.z + w1 * B.z + w2 * C.z;
    }

    public boolean pixelInsideFace(ProjFace face, int x, int y, boolean clockwise) {
        for (int i = 0; i < face.points; ++i) {
            int next = (i + 1) % face.points;
            if (!edgeFunction(face.vertices[i], face.vertices[next], x, y, clockwise)) {
                return false;
            } 
        }       
        return true;
}

    public boolean edgeFunction(ProjPoint p1, ProjPoint p2, int x, int y, boolean clockwise) {
        double result = (x - p1.x) * (p2.y - p1.y) - (y - p1.y) * (p2.x - p1.x);
        return clockwise ? result >= 0 : result <= 0;
    }

    static final int INSIDE = 0; // 0000
    static final int LEFT   = 1; // 0001
    static final int RIGHT  = 2; // 0010
    static final int BOTTOM = 4; // 0100
    static final int TOP    = 8; // 1000
    
    public void drawLine(double x1, double y1, double x2, double y2) {        
        int pixelX1 = convertToScreenCoordinatesX(x1);
        int pixelY1 = convertToScreenCoordinatesY(y1);
        int pixelX2 = convertToScreenCoordinatesX(x2);
        int pixelY2 = convertToScreenCoordinatesY(y2);
    
        int p1Code = getRegionCode(pixelX1, pixelY1);
        int p2Code = getRegionCode(pixelX2, pixelY2);
    
        int[] p1 = new int[] {pixelX1, pixelY1};
        int[] p2 = new int[] {pixelX2, pixelY2};
    
        float gradient;
        if (pixelX1 == pixelX2) {gradient = Float.MAX_VALUE;}
        else {gradient = (float)(pixelY2 - pixelY1) / (float)(pixelX2 - pixelX1);}
        
        if ((p1Code & p2Code) != INSIDE) { return; } // if on same side of screen

        else if (p1Code != INSIDE && p2Code != INSIDE) { // if both arent inside screen
            if (!isSegmentContainedByScreen(p1[0], p1[1], p2[0], p2[1])) { return; }
        }

        else if ((p1Code | p2Code) != INSIDE) { // if at least one is outside screen
            p1 = clipPoint(pixelX1, pixelY1, gradient);
            p2 = clipPoint(pixelX2, pixelY2, gradient);
        }
    
        if (p1 != new int[]{-1,-1} && p2 != new int[]{-1,-1}) {
            bresenham(p1[0], p1[1], p2[0], p2[1]);
        }
    }

    public boolean ccw(int Ax, int Ay, int Bx, int By, int Cx, int Cy) {
        long crossProduct = (long)(Cy - Ay) * (Bx - Ax) - (long)(By - Ay) * (Cx - Ax);
        return crossProduct > 0;
    }

    public boolean isSegmentContainedByScreen(int x1, int y1, int x2, int y2) {
        boolean intersectsTop = intersectSegments(x1, y1, x2, y2, 0, 0, width, 0);
        boolean intersectsBottom = intersectSegments(x1, y1, x2, y2, 0, height, width, height);
        boolean intersectsLeft = intersectSegments(x1, y1, x2, y2, 0, 0, 0, height);
        boolean intersectsRight = intersectSegments(x1, y1, x2, y2, width, 0, width, height);

        if (intersectsTop || intersectsBottom || intersectsLeft || intersectsRight) {
            return false;
        }
        return true;
    }

    public boolean intersectSegments(int Ax, int Ay, int Bx, int By, int Cx, int Cy, int Dx, int Dy) {        
        return ccw(Ax, Ay, Bx, By, Cx, Cy) != ccw(Ax, Ay, Bx, By, Dx, Dy) && 
            ccw(Cx, Cy, Dx, Dy, Ax, Ay) != ccw(Cx, Cy, Dx, Dy, Bx, By);
    }

    public int[] clipPoint(int x, int y, float gradient) {
        int nx = x;
        int ny = y;
        
        final double nearZero = 1e-6;
        
        int code;
        int count = 0;
        while ((code = getRegionCode(nx, ny)) != INSIDE) {
            if (++count > 2) { return new int[]{-1, -1}; } // doesnt intersect screen

            // System.out.println(nx + " : " + ny + ", gra: " + gradient);
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
        return new int[]{ nx, ny };
    }

    public void bresenham(int x1, int y1, int x2, int y2) {
        
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        
        int sx = x1 < x2 ? 1 : -1; // get direction to move
        int sy = y1 < y2 ? 1 : -1;
        
        int err = dx - dy;
        
        do {    
            // if moving away from center of the screen we can 
            // safely exit if we're outside of the screen
            if (x1 > panel.getWidth() - 1 && sx == 1) { break; }
            if (x1 < 0 && sx == -1) { break; }
            if (y1 > panel.getHeight() - 1 && sy == 1) { break; }
            if (y1 < 0 && sy == -1) { break; }
            
            panel.setPixel(x1, y1, Color.RED.getRGB());
            
            int e2 = 2 * err;
            
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        } while (x1 != x2 || y1 != y2);
    }

    public int getRegionCode(double x, double y) {
        int out = INSIDE;
        if (x >= width) { out |= RIGHT; }
        else if (x < 0) { out |= LEFT; }

        if (y < 0) { out |= TOP; }
        else if (y >= height) { out |= BOTTOM; }
        
        return out;
    }

    public int convertToScreenCoordinatesX(double x) {
        // System.out.println(x);
        return (int)Math.round(x * width + width / 2);
    }

    public int convertToScreenCoordinatesY(double y) {
        return (int) (-y * height + height / 2);
    }
}