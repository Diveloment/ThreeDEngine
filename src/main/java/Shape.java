import java.util.ArrayList;
import java.util.List;

public class Shape {
    public Vertex[] vertices = {};
    protected Scene panel;
    protected List<Polygon> polygons = new ArrayList<>();
    protected double scale = 1.0;
    protected Vector3 position = new Vector3(0.0, 0.0, 0.0);

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        Vector3 offset = position.subtract(this.position); // Вычисляем вектор смещения
        this.position = position;

        for (Vertex v : vertices) {
            v.x += offset.x;
            v.y += offset.y;
            v.z += offset.z;
        }

        Update();
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Scene getPanel() {
        return panel;
    }

    public void setPanel(Scene panel) {
        this.panel = panel;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public Shape() {
        this.panel = panel;
        Update();
        AddToDrawBuffer();
    }

    public void rotate(Vector3 rotationAngles) {
        // 1. Вычислить центр куба
        Vector3 cubeCenter = new Vector3(0, 0, 0);
        for (Vertex v : vertices) {
            cubeCenter.x += v.x;
            cubeCenter.y += v.y;
            cubeCenter.z += v.z;
        }
        cubeCenter.x /= vertices.length;
        cubeCenter.y /= vertices.length;
        cubeCenter.z /= vertices.length;

        // 2. Перенести куб к началу координат
        for (Vertex v : vertices) {
            v.x -= cubeCenter.x;
            v.y -= cubeCenter.y;
            v.z -= cubeCenter.z;
        }

        // 3. Применить вращение
        rotateX(rotationAngles.x);
        rotateY(rotationAngles.y);
        rotateZ(rotationAngles.z);

        // 4. Вернуть куб в исходное положение
        for (Vertex v : vertices) {
            v.x += cubeCenter.x;
            v.y += cubeCenter.y;
            v.z += cubeCenter.z;
        }

        Update();
    }

    public void rotateX(double angle) {
        double[][] rotationMatrix = {
                {1, 0, 0},
                {0, Math.cos(angle), -Math.sin(angle)},
                {0, Math.sin(angle), Math.cos(angle)}
        };
        applyRotation(rotationMatrix);
    }

    public void rotateY(double angle) {
        double[][] rotationMatrix = {
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)}
        };
        applyRotation(rotationMatrix);
    }

    public void rotateZ(double angle) {
        double[][] rotationMatrix = {
                {Math.cos(angle), -Math.sin(angle), 0},
                {Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 1}
        };
        applyRotation(rotationMatrix);
    }

    private void applyRotation(double[][] rotationMatrix) {
        for (Vertex v : vertices) {
            double x_new = rotationMatrix[0][0] * v.x + rotationMatrix[0][1] * v.y + rotationMatrix[0][2] * v.z;
            double y_new = rotationMatrix[1][0] * v.x + rotationMatrix[1][1] * v.y + rotationMatrix[1][2] * v.z;
            double z_new = rotationMatrix[2][0] * v.x + rotationMatrix[2][1] * v.y + rotationMatrix[2][2] * v.z;

            v.x = x_new;
            v.y = y_new;
            v.z = z_new;
        }

        Update();
    }


    public void Update() {
    }

    public void AddToDrawBuffer() {
        for (Polygon polygon : polygons) {
            panel.putPolygon(polygon);
        }
    }
}