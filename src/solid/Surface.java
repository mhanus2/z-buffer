package solid;

import transforms.Bicubic;
import transforms.Col;
import transforms.Cubic;
import transforms.Point3D;

public class Surface extends Solid {
    public Surface() {
        Point3D[] points = new Point3D[16];
        points[0] = new Point3D(0, 0, 0);
        points[1] = new Point3D(0.33, 0, 0.25);
        points[2] = new Point3D(0.66, 0, -0.25);
        points[3] = new Point3D(1, 0, 0);
        points[4] = new Point3D(0, 0.33, -0.25);
        points[5] = new Point3D(0.33, 0.33, 0);
        points[6] = new Point3D(0.66, 0.33, 0);
        points[7] = new Point3D(1, 0.33, 0.25);
        points[8] = new Point3D(0, 0.66, 0.25);
        points[9] = new Point3D(0.33, 0.66, 0);
        points[10] = new Point3D(0.66, 0.66, 0);
        points[11] = new Point3D(1, 0.66, -0.25);
        points[12] = new Point3D(0, 1, 0);
        points[13] = new Point3D(0.33, 1, -0.25);
        points[14] = new Point3D(0.66, 1, 0.25);
        points[15] = new Point3D(1, 1, 0);

        Bicubic bicubic = new Bicubic(Cubic.BEZIER, points);

        int resolution = 100;
        for (int i = 0; i <= resolution; i++) {
            for (int j = 0; j <= resolution; j++) {
                double u = (double) i / resolution;
                double v = (double) j / resolution;
                Point3D point = bicubic.compute(u, v);
                vertexBuffer.add(new Vertex(point, new Col(255, 0, 0)));
            }
        }

        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                int index = i * (resolution + 1) + j;
                indexBuffer.add(index);
                indexBuffer.add(index + resolution + 1);
                indexBuffer.add(index + 1);

                indexBuffer.add(index + 1);
                indexBuffer.add(index + resolution + 1);
                indexBuffer.add(index + resolution + 2);
            }
        }
        partBuffer.add(new Part(TopologyType.TRIANGLES, 0, indexBuffer.size() / 3));

    }

}
