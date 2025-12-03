import HelperClasses.*;

public class Main {
    public static void main(String[] args) { 
        Camera cam1 = new Camera(new Point(0, 0, -5), new Rotation(0, 0, 0));
        // Camera cam = new Camera();
        GUI gui = new GUI(1500,900);
        Renderer renderer = new Renderer(cam1, gui);
        Mesh shape = Shapes.sierpinskiTetrahedron(5);

        long lastTime = System.nanoTime();
        int totalFPS = 0;
        int count = 0;

        while (true) {
            long startTime = System.nanoTime();
            double t = (startTime - lastTime) / 1_000_000_000.0; // seconds since last frame

            // renderer.cam.rotateAroundOrigin(t, 2 - 2 * Math.sin(t / 5));
            renderer.cam.rotateAroundOrigin(t, 3.5);
            
            renderer.out.clearBuffer();

            renderer.drawMesh(shape);

            renderer.out.drawBuffer();
            gui.panel.repaint();

            long endTime = System.nanoTime();
            double deltaSeconds = (endTime - startTime) / 1_000_000_000.0;
            int fps = (int)Math.round(1 / deltaSeconds);
            renderer.out.panel.setFPS(fps, deltaSeconds * 1000);
            totalFPS += fps;
            count++;
            // System.out.println(totalFPS / count);
        }
    }
}
