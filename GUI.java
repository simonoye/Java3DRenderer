import javax.swing.JFrame;

public class GUI {
    int width, height;
    RenderPanel panel;
    JFrame frame;

    public GUI(int width, int height) {
        this.width = width;
        this.height = height;

        panel = new RenderPanel(width, height);
        
        frame = new JFrame("PIECE OF SHIT");
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {        
        int pixelX1 = getPixelX(x1);
        int pixelY1 = getPixelY(y1);
        int pixelX2 = getPixelX(x2);
        int pixelY2 = getPixelY(y2);
        
        int dx = Math.abs(pixelX2 - pixelX1);
        int dy = Math.abs(pixelY2 - pixelY1);

        int sx = pixelX1 < pixelX2 ? 1 : -1; // get direction to move
        int sy = pixelY1 < pixelY2 ? 1 : -1;

        int err = dx - dy;

        do {
            if (pixelX1 > panel.getWidth() - 1 && sx == 1) { break; }
            if (pixelX1 < 0 && sx == -1) { break; }
            if (pixelY1 > panel.getHeight() - 1 && sy == 1) { break; }
            if (pixelY1 < 0 && sy == -1) { break; }

            panel.setPixel(pixelX1, pixelY1, 0xFFFFFF);
            
            int e2 = 2 * err;
            
            if (e2 > -dy) {
                err -= dy;
                pixelX1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                pixelY1 += sy;
            }
        } while (pixelX1 != pixelX2 || pixelY1 != pixelY2);
    }

    public void clear() {
        panel.clear();
    }

    public int getPixelX(double x) {
        return (int)Math.round(x * width + width / 2);
    }

    public int getPixelY(double y) {
        return (int) (-y * height + height / 2);
    }
}