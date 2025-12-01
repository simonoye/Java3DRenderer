import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class RenderPanel extends JPanel {
    BufferedImage buffer;
    int[] pixels;
    JLabel text;

    public RenderPanel(int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setPreferredSize(new Dimension(width, height));
        pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();

        text = new JLabel("FPS: ");
        text.setFont(new Font("Arial", Font.BOLD, 32));
        text.setForeground(Color.RED);
        text.setHorizontalAlignment(SwingConstants.LEFT);
        text.setVerticalAlignment(SwingConstants.TOP);
        add(text);
    }

    public void setFPS(int fps) {
        text.setText("FPS: " + fps);
    }

    public void setPixel(int x, int y, int argbColor) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            pixels[y * buffer.getWidth() + x] = argbColor; //pixels is a 1D array
        }
    }

    public void clear() {
        for (int i = 0; i < pixels.length; ++i) {
            pixels[i] = 0xFF0D1519;
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