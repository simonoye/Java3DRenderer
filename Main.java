public class Main {
    public static void main(String[] args) {
        Mesh mesh = new Mesh(
            new int[]{4,4,4,4,4,4},
            new int[]{
                2,3,7,6,
                0,4,5,1,
                0,2,6,4,
                1,3,7,5,
                0,2,3,1,
                4,6,7,5
            },
            new Point[]{
            new Point(-1,-1, 1),
            new Point( 1,-1, 1),
            new Point(-1, 1, 1),
            new Point( 1, 1, 1),
            new Point(-1,-1,-1),
            new Point( 1,-1,-1),
            new Point(-1, 1,-1),
            new Point( 1, 1,-1),
        });
        
        Camera cam = new Camera(new Point(0, 0, 5), new Rotation(0, 0, 0));
        GUI gui = new GUI(800,800);
        Renderer renderer = new Renderer(cam, gui);

        try {
            double time = 0;
            while (true) {
                Thread.sleep(10);
                time += 10;
                double t = time / 1000.0;     
                
                gui.clear();
                //renderer.drawLine(mesh.points[0], mesh.points[1]);
                // renderer.renderMeshPoints(mesh); 
                renderer.drawMesh(mesh);
                gui.panel.repaint();

                cam.position.x = 5 * Math.cos(t);
                cam.position.z = 5 * Math.sin(t);
                cam.rotation.y = - Math.atan2(cam.position.x, cam.position.z);
                // System.out.println(cam.rotation.y);
            }
        } catch (InterruptedException e) {}
    }
}
