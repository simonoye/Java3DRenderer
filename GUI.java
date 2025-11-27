import javax.swing.JFrame;

public class GUI {
    int width, height;
    RenderPanel panel;
    JFrame frame;

    public GUI(int width, int height) {
        this.width = width;
        this.height = height;

        panel = new RenderPanel(width, height);
        
        frame = new JFrame("AWESOMESAUCE");
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void drawPoint(double x, double y) {
        panel.setPixel(pixelCoordinateX(x), pixelCoordinateY(y), 0xFFFFFF);
    }

    public void clear() {
        panel.clear();
    }

    public int pixelCoordinateX(double x) {
        return (int)Math.round(x * width + width / 2);
    }

    public int pixelCoordinateY(double y) {
        return (int) (-y * height + height / 2);
    }
}