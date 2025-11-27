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
        
        Triangle[] triangles = new Triangle[] {
            new Triangle(
                new Point( 1, 0, 0.5),
                new Point(-1, 0, 0.5),
                new Point( 0,Math.sqrt(2), 0)
            ),
            new Triangle(
                new Point(1,0,0.5), 
                new Point(-1, 0, 0.5), 
                new Point(0,-Math.sqrt(2), 0)
            )
        };

        Camera cam = new Camera(new Point(0, 0, 5), new Rotation(0, 0, 0));
        Renderer renderer = new Renderer(cam);
        
        GUI gui = new GUI(800,800);
        try {
            double time = 0;
            while (true) {
                Thread.sleep(10);
                time += 16.67;
                double t = time / 1000.0;     
                
                gui.clear();
                renderer.renderMesh(mesh, gui);
                gui.panel.repaint();

                cam.position.x = 5 * Math.cos(t);
                cam.position.z = 5 * Math.sin(t);
                cam.rotation.y = - Math.atan2(cam.position.x, cam.position.z);
            }
        } catch (InterruptedException e) {}
    }
}
