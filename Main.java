import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;

import HelperClasses.Camera;
import HelperClasses.Mesh;
import HelperClasses.OBJ;
import HelperClasses.Shapes;
import HelperClasses.Vec3;
import HelperClasses.Rotation;

public class Main {
    static Camera cam;
    static GUI gui;
    static Renderer renderer;

    public static void main(String[] args) throws IOException { 
        cam = new Camera(new Vec3(0, 0, 5), new Rotation(0, 0, 0));
        // cam = new Camera();
        gui = new GUI(800,800);
        renderer = new Renderer(cam, gui);
        Mesh shape = Shapes.sierpinskiTetrahedron(5);
        // Mesh shape = Shapes.triangle();

        long objstart = System.nanoTime();

        OBJParser objParser;
        try { objParser = new OBJParser("OBJs/porsche911.obj"); }
        // try { objParser = new OBJParser("OBJs/Porsche_911_GT2.obj"); }
        catch (FileNotFoundException e) { e.printStackTrace(); return; }
        OBJ obj = objParser.obj;
        
        double elapsedSeconds = (System.nanoTime() - objstart) / 1_000_000.0;
        System.out.println("Parse time: " + elapsedSeconds + "ms");

        // long lastTime = System.nanoTime();
        dragCam(new java.awt.Point(100,-100), new java.awt.Point(0,0), 5);

        while (true) {
            long startTime = System.nanoTime();

            if (renderer.out.panel.dragging) {
                dragCam(renderer.out.panel.iPos, renderer.out.panel.pos, 5);
            }
            
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
}