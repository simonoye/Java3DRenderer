import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RenderPanel extends JPanel {
    private BufferedImage buffer;
    private int[] pixels;
    private JLabel text;
    Point iPos;
    Point pos;
    double radius;
    double scrollChange;
    boolean dragging;

    public RenderPanel(int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(width, height));
        pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();

        setLayout(null);
        text = new JLabel("FPS: ");
        text.setFont(new Font("Arial", Font.BOLD, 26));
        text.setForeground(Color.RED);
        text.setBounds(5, -15, 400, 100);
        add(text);

        iPos = new Point();
        pos = new Point();
        scrollChange = 0;
        radius = Main.radius;
        dragging = false;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                iPos = e.getPoint();
                pos = e.getPoint();
                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                pos = e.getPoint();
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollChange = e.getPreciseWheelRotation();
                radius = Math.max(0, radius * (1 + scrollChange / 100));
            }
        });
    }

    public void setFPS(int fps, double t) {
        text.setText(String.format("<html>FPS: %d<br>Frame time: %.3fms</html>", fps, t));
    }

    public void setPixel(int x, int y, int argbColor) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            pixels[y * buffer.getWidth() + x] = argbColor; //pixels is a 1D array
        }
    }

    public void updateScreen() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }
}