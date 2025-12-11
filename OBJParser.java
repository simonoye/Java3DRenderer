import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

    public OBJParser(String filename) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(new File(filename)));
        obj = new OBJ();
        points = new ArrayList<>();
        normals = new ArrayList<>();
    }

    public void parseOBJ() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
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
        double x = Double.parseDouble(line[1]);
        double y = Double.parseDouble(line[2]);
        double z = Double.parseDouble(line[3]);

        normals.add(new Vec3(x, y, z));
    }

    public void parseFace(String[] line) {
        Vec3[] vertices = new Vec3[line.length - 1];
        // Point[] triNormals = new Point[line.length - 1];

        for (int i = 1; i < line.length; ++i) {
            String[] part = line[i].split("/");
            vertices[i - 1] = points.get(Integer.parseInt(part[0]) - 1);
            // triNormals[i - 1] = normals.get(Integer.parseInt(part[2]) - 1);
        }

        if (vertices.length == 3) { 
            obj.triangles.add(new Triangle(vertices, getNormal(vertices))); 
        }
        else {
            for (int i = 0; i < vertices.length - 2; ++i) {
                obj.triangles.add(new Triangle(new Vec3[]{vertices[i], vertices[i+1], vertices[i+2]}));
            }
        }

    }

    private Vec3 getNormal(Vec3[] vertices) {
        Vec3 A = vertices[1].subtract(vertices[0]);
        Vec3 B = vertices[2].subtract(vertices[1]);
        return A.cross(B);
    }

    public void parseVertex(String[] line) {
        double x = Double.parseDouble(line[1]);
        double y = Double.parseDouble(line[2]);
        double z = Double.parseDouble(line[3]);

        points.add(new Vec3(x, y, z));
    }
}