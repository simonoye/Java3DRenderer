public class Main {
    public static void main(String[] args) {
        Mesh mesh = new Mesh(new Point[]{
            new Point( 1, -1, -1),
            new Point( 1, -1,  1),
            new Point( 1,  1, -1),
            new Point( 1,  1,  1),
            new Point(-1, -1, -1),
            new Point(-1, -1,  1),
            new Point(-1,  1, -1),
            new Point(-1,  1,  1),
        });
        
        Camera cam = new Camera(new Point(0, 0, 5), new Rotation(0, 0, 0));
        GUI gui = new GUI(800,800);
        Renderer renderer = new Renderer(cam, gui);
        
        try {
            double time = 0;
            while (true) {
                Thread.sleep(10);
                time += 16.67;
                double t = time / 1000.0;     
                
                gui.clear();
                renderer.renderMesh(mesh); 
                gui.panel.repaint();

                cam.position.x = 5 * Math.cos(t);
                cam.position.z = 5 * Math.sin(t);
                cam.rotation.y = - Math.atan2(cam.position.x, cam.position.z);
            }
        } catch (InterruptedException e) {}
    }
}
