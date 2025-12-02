public class Main {
    public static void main(String[] args) { 
        //System.out.print("\033[H\033[2J");

        Camera cam1 = new Camera(new Point(0, 0, 0), new Rotation(0, 0, 0));
        // Camera cam = new Camera(new Point(0, 0, 0));
        GUI gui = new GUI(800,800);
        Renderer renderer = new Renderer(cam1, gui);
        
        try {
            long lastTime = System.nanoTime();
            while (true) {
                long startTime = System.nanoTime();
                Thread.sleep((long)1);
                double t = (startTime - lastTime) / 1_000_000_000.0; // seconds since last frame
                
                // renderer.cam.rotateAroundOrigin(t, 2.5 - 2.5 * Math.sin(t));
                renderer.cam.rotateAroundOrigin(t, 5);
                
                gui.clear();
                // renderer.drawWireframe(Shapes.sierpinskiTetrahedron(5));
                renderer.drawMesh(Shapes.cube());
                // renderer.drawWireframe(Shapes.bottom_cube());
                gui.panel.repaint();
                
                long endTime = System.nanoTime();
                double deltaSeconds = (endTime - startTime) / 1_000_000_000.0;
                renderer.out.panel.setFPS((int) (1 / deltaSeconds), deltaSeconds * 1000);
                // System.out.println("Frame time: " + (deltaSeconds * 1000) + "ms");
            }
        } catch (InterruptedException e) {}
    }
}
