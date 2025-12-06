import HelperClasses.Camera;
import HelperClasses.Mesh;
import HelperClasses.Shapes;
import HelperClasses.Point;
import HelperClasses.Rotation;
import java.awt.Color;

public class Main {
    static Camera cam;
    static GUI gui;
    static Renderer renderer;

    public static void main(String[] args) { 
        cam = new Camera(new Point(1, 0, 4), new Rotation(0, 0, 0));
        // cam = new Camera();
        gui = new GUI(800,800);
        renderer = new Renderer(cam, gui);
        Mesh shape = Shapes.sierpinskiTetrahedron(5);
        // Mesh shape = Shapes.triangle();

        // long lastTime = System.nanoTime();

        while (true) {
            long startTime = System.nanoTime();
            // double t = (startTime - lastTime) / 1_000_000_000.0; // seconds since start

            if (renderer.out.panel.dragging) {
                dragCam(renderer.out.panel.iPos, renderer.out.panel.pos);
            }
            
            // renderer.cam.rotateAroundOrigin(t, 2 - 2 * Math.sin(t / 5));
            // renderer.cam.rotateAroundOrigin(t, 3.5);
            // renderer.cam.rotation.y = Math.sin(t);

            
            renderer.out.clearBuffer();

            renderer.drawWireframe(shape);
            // renderer.drawLine(new Point(0, 1, 0, Color.GRAY.getRGB()), new Point(0, -1, 0, Color.GRAY.getRGB()));

            renderer.out.drawBuffer();
            gui.panel.updateScreen();

            long endTime = System.nanoTime();
            double deltaSeconds = (endTime - startTime) / 1_000_000_000.0;
            int fps = (int)Math.round(1 / deltaSeconds);
            renderer.out.panel.setFPS(fps, deltaSeconds * 1000);
            // System.out.println(totalFPS / count);
        }
    }

    public static void dragCam(java.awt.Point iPos, java.awt.Point pos) {
        int dx = pos.x - iPos.x;
        int dy = pos.y - iPos.y;

        cam.rotateAroundOrigin((double) dx / 200, (double) dy / 200, 4);

        iPos.x = pos.x;
        iPos.y = pos.y;
    } 
}