import HelperClasses.Camera;
import HelperClasses.Mesh;
import HelperClasses.Shapes;
import HelperClasses.Point;
import HelperClasses.Rotation;


public class Main {
    public static void main(String[] args) { 
        Camera cam1 = new Camera(new Point(0, 0, -5), new Rotation(0, 0, 0));
        // Camera cam = new Camera();
        GUI gui = new GUI(800,800);
        Renderer renderer = new Renderer(cam1, gui);
        Mesh shape = Shapes.sierpinskiTetrahedron(3);
        // Mesh shape = Shapes.triangle();

        long lastTime = System.nanoTime();

        while (true) {
            long startTime = System.nanoTime();
            double t = (startTime - lastTime) / 1_000_000_000.0; // seconds since start

            // renderer.cam.rotateAroundOrigin(t, 2 - 2 * Math.sin(t / 5));
            renderer.cam.rotateAroundOrigin(t, 3.5);
            // renderer.cam.rotation.y = Math.sin(t);
            
            renderer.out.clearBuffer();

            renderer.drawMesh(shape);

            renderer.out.drawBuffer();
            gui.panel.updateScreen();

            long endTime = System.nanoTime();
            double deltaSeconds = (endTime - startTime) / 1_000_000_000.0;
            int fps = (int)Math.round(1 / deltaSeconds);
            renderer.out.panel.setFPS(fps, deltaSeconds * 1000);
            // System.out.println(totalFPS / count);
        }
    }
}