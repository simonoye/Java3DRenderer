import java.io.IOException;

import HelperClasses.Camera;
import HelperClasses.OBJ;
import HelperClasses.Vec3;
import HelperClasses.Rotation;

public class Main {
    static Camera cam;
    static GUI gui;
    static Renderer renderer;
    static double radius = 5;

    public static void main(String[] args) throws IOException { 
        cam = new Camera(new Vec3(0, 0, 5), new Rotation(0, 0, 0));
        gui = new GUI(1500,900);
        renderer = new Renderer(cam, gui, true);

        long objstart = System.nanoTime();

        OBJParser objParser;
        try { objParser = new OBJParser("OBJs/bus.obj"); }
        catch (IOException e) { e.printStackTrace(); return; }
        OBJ obj = objParser.obj;
        
        double elapsedSeconds = (System.nanoTime() - objstart) / 1_000_000.0;
        System.out.println("OBJ parse time: " + elapsedSeconds + "ms");

        dragCam(new java.awt.Point(100,-100), new java.awt.Point(0,0), radius);
        // long lastTime = System.nanoTime();

        while (true) {
            long startTime = System.nanoTime();

            zoomCam();
            // if (renderer.out.panel.dragging) {
                dragCam(renderer.out.panel.iPos, renderer.out.panel.pos, radius);
            // }
            // System.err.println(renderer.out.panel.scrollChange);
            
            renderer.out.clearBuffer();

            renderer.drawOBJ(obj);
            renderer.drawAxis(2, 10);

            renderer.out.drawBuffer();
            gui.panel.updateScreen();

            long endTime = System.nanoTime();
            double deltaSeconds = (endTime - startTime) / 1_000_000_000.0;
            int fps = (int)Math.round(1 / deltaSeconds);
            renderer.out.panel.setFPS(fps, deltaSeconds * 1000);
        }
    }

    public static void dragCam(java.awt.Point iPos, java.awt.Point pos, double radius) {
        int dx = pos.x - iPos.x;
        int dy = pos.y - iPos.y;

        cam.rotateAroundOrigin((double) dx / 200, (double) dy / 200, radius);

        iPos.x = pos.x;
        iPos.y = pos.y;
    } 

    public static void zoomCam() {
        // double scrollChange = renderer.out.panel.scrollChange;
        // if (Math.abs(scrollChange) == 0.1) { return; }

        // radius = Math.max(0, radius + scrollChange);
        radius = renderer.out.panel.radius;
    }
}