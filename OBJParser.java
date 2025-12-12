import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import HelperClasses.OBJ;
import HelperClasses.Vec3;
import HelperClasses.Triangle;

public class OBJParser {
    BufferedReader reader;
    OBJ obj;
    ArrayList<Vec3> points; 
    ArrayList<Vec3> normals; 

    public OBJParser(String filename) throws IOException{
        reader = new BufferedReader(new FileReader(new File(filename)));
        obj = new OBJ();
        points = new ArrayList<>();
        normals = new ArrayList<>();
        parseOBJ();
    }

    public void parseOBJ() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Remove leading/trailing whitespace
            if (line.isEmpty() || line.startsWith("#")) { continue; } // Skip empty lines and comments
            String[] parts = line.split("\\s+"); // Split on any whitespace (handles multiple spaces)

            String keyword = parts[0];

            switch (keyword) {
                case "v":
                    parseVertex(parts);
                    break;
                case "f": 
                    parseFace(parts);
                    break;
                case "vn":
                    parseNormals(parts);
                    break;
            }
        }
    }

    public void parseNormals(String[] line) {
        if (line.length < 4) { return; }
        if (line[1].isEmpty() || line[2].isEmpty() || line[3].isEmpty()) { return; }
            
        double x = Double.parseDouble(line[1]);
        double y = Double.parseDouble(line[2]);
        double z = Double.parseDouble(line[3]);

        normals.add(new Vec3(x, y, z));
    }

    public void parseFace(String[] line) {
        if (line.length < 4) { return; }
        Vec3[] vertices = new Vec3[line.length - 1];

        for (int i = 1; i < line.length; ++i) {
            if (line[i].isEmpty()) { continue; }
            String[] part = line[i].split("/");
            if (part.length == 0 || part[0].isEmpty()) { continue; }

            vertices[i - 1] = points.get(Integer.parseInt(part[0]) - 1);
            vertices[i - 1].normal = normals.get(Integer.parseInt(part[2]) - 1);
        }
        Vec3 normal = getNormal(vertices[0], vertices[1], vertices[2]);

        for (int i = 1; i < vertices.length - 1; ++i) {
            Vec3[] triVerts = new Vec3[]{vertices[0], vertices[i], vertices[i+1]};
            obj.triangles.add(new Triangle(triVerts, normal));
        }
    }

    private Vec3 getNormal(Vec3 v1, Vec3 v2, Vec3 v3) {
        Vec3 A = v2.subtract(v1);
        Vec3 B = v3.subtract(v2);
        return A.cross(B);
    }

    public void parseVertex(String[] line) {
        if (line.length != 4) { return; }
        if (line[1].isEmpty() || line[2].isEmpty() || line[3].isEmpty()) { return; }
            
        double x = Double.parseDouble(line[1]);
        double y = Double.parseDouble(line[2]);
        double z = Double.parseDouble(line[3]);

        points.add(new Vec3(x, y, z));
    }
}