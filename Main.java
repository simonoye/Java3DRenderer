import java.io.IOException;

public class Main {
    public static void main(String[] args) { 
        //System.out.print("\033[H\033[2J");

        Camera cam1 = new Camera(new Point(0, 1, 0), new Rotation(0, 0, 0));
        Camera cam = new Camera(new Point(0, 0, 0));
        GUI gui = new GUI(800,800);
        Renderer renderer = new Renderer(cam, gui);
        
        try {
            long lastTime = System.nanoTime();
            while (true) {
                long startTime = System.nanoTime();
                Thread.sleep((long)10);
                
                double t = (startTime - lastTime) / 1_000_000_000.0; // seconds since last frame
                
                renderer.cam.rotateAroundOrigin(t, 5 - t);
                // System.out.println(renderer.cam.position);
                // renderer.cam.position.z = -5 + t;

                
                gui.clear();
                renderer.drawWireframe(Shapes.sierpinskiTetrahedron(5));
                gui.panel.repaint();
                
                long endTime = System.nanoTime();
                
                double deltaSeconds = (endTime - startTime) / 1_000_000_000.0;
                // System.out.println("FPS: " + (1 / deltaSeconds));
                // System.out.println("Frame time: " + (deltaSeconds * 1000) + "ms");
                
                // System.out.println(cam.rotation.y);
            }
        } catch (InterruptedException e) {}
    }
}
