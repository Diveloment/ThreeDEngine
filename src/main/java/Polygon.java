import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Polygon {
    public List<Vertex> vertices;
    public double width = 0.0;
    public double height = 0.0;
    static double[][] projectionMatrix = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
    };

    public char filler = '#';

    public Polygon() {
        vertices = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public Polygon(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        vertices = new ArrayList<>();
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
        addVertex(v4);
    }

    public Polygon(Vertex v1, Vertex v2, Vertex v3) {
        vertices = new ArrayList<>();
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
    }

    public Polygon(Vertex v1, Vertex v2, Vertex v3, Vertex v4, char filler) {
        vertices = new ArrayList<>();
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
        addVertex(v4);
        this.filler = filler;
    }

    public void setProjectionMatrix(double[][] projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    private Point VertexProjection(Vertex vertex) {
        double x_new = projectionMatrix[0][0] * vertex.x + projectionMatrix[0][1] * vertex.y + projectionMatrix[0][2] * vertex.z;
        double y_new = projectionMatrix[1][0] * vertex.x + projectionMatrix[1][1] * vertex.y + projectionMatrix[1][2] * vertex.z;
        double z_new = projectionMatrix[2][0] * vertex.x + projectionMatrix[2][1] * vertex.y + projectionMatrix[2][2] * vertex.z;

        return new Point((int) x_new, (int) y_new);
    }

    public Vector3 computeNormal() {
        if (vertices.size() < 3) {
            return null;
        }

        Vertex v0 = vertices.get(0);
        Vertex v1 = vertices.get(1);
        Vertex v2 = vertices.get(2);

        // Вычисляем векторы AB и BC (или AD и DC)
        Vertex AB = new Vertex(v1.x - v0.x, v1.y - v0.y, v1.z - v0.z);
        Vertex BC = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);

        // Вычисляем кросс-произведение для получения нормали
        double normalX = AB.y * BC.z - AB.z * BC.y;
        double normalY = AB.z * BC.x - AB.x * BC.z;
        double normalZ = AB.x * BC.y - AB.y * BC.x;

        // Создаем и возвращаем нормализованный вектор нормали
        Vector3 normal = new Vector3(normalX, normalY, normalZ);
        return normal.normalize().multiply(-1);
    }

    public Vector3 computeCenter() {
        if (vertices.isEmpty()) {
            return null; // Полигон не содержит вершин
        }

        double centerX = 0;
        double centerY = 0;
        double centerZ = 0;

        for (Vertex vertex : vertices) {
            centerX += vertex.x;
            centerY += vertex.y;
            centerZ += vertex.z;
        }

        centerX /= vertices.size();
        centerY /= vertices.size();
        centerZ /= vertices.size();

        return new Vector3(centerX, centerY, centerZ);
    }

    public List<Point> getPoints() {
        if (vertices.size() < 3) {
            return List.of();
        }

        List<Point> points = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            Point vertex = VertexProjection(vertices.get(i));
            points.add(vertex);
        }

        return points;
    }

    public Point getPointByVector3(Vector3 vector) {
        return VertexProjection(new Vertex(vector));
    }

    public List<Point> getFilledPoints(int screenHeight, int screenWidth, List<Point> points) {
        List<Point> filledPoints = new ArrayList<Point>();

        if (points.isEmpty()) {
            return filledPoints;
        }

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Находим минимальные и максимальные координаты вершин полигона
        for (Point vertex : points) {
            if (vertex.x < minX) minX = vertex.x;
            if (vertex.x > maxX) maxX = vertex.x;
            if (vertex.y < minY) minY = vertex.y;
            if (vertex.y > maxY) maxY = vertex.y;
        }

        // Проходим по каждой точке в ограничивающем прямоугольнике
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Point currentPoint = new Point(x, y);

                // Проверяем, находится ли точка внутри полигона
                if (isPointInsidePolygon(currentPoint, points)) {
                    filledPoints.add(currentPoint);
                }
            }
        }

        return filledPoints;
    }

    private boolean isPointInsidePolygon(Point point, List<Point> polygon) {
        int intersections = 0;
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);

            // Проверяем пересечение луча, исходящего из текущей точки,
            // с ребром полигона
            if (point.y > Math.min(p1.y, p2.y) &&
                    point.y <= Math.max(p1.y, p2.y) &&
                    point.x <= Math.max(p1.x, p2.x) &&
                    p1.y != p2.y) {
                double xIntersection = (double) ((point.y - p1.y) * (p2.x - p1.x)) / (p2.y - p1.y) + p1.x;
                if (p1.x == p2.x || point.x <= xIntersection) {
                    intersections++;
                }
            }
        }

        // Если количество пересечений нечетное, то точка внутри полигона
        return intersections % 2 != 0;
    }


    public static Vector3 calculateIntersectionPoint(Vector3 origin, Vector3 direction, Polygon polygon) {
        // Получаем вершины полигона
        Vector3 v0 = polygon.vertices.get(0).toVector3();
        Vector3 v1 = polygon.vertices.get(1).toVector3();
        Vector3 v2 = polygon.vertices.get(2).toVector3();

        // Вычисляем ребра треугольника
        Vector3 edge1 = v1.subtract(v0);
        Vector3 edge2 = v2.subtract(v0);

        // Вычисляем вектор нормали к плоскости треугольника
        Vector3 normal = polygon.computeNormal();

        // Вычисляем вектор от начальной точки луча до вершины треугольника
        Vector3 h = origin.subtract(v0);

        // Вычисляем произведение скалярное и векторное между направлением луча и нормалью
        double a = -normal.dot(direction);
        double b;

        if (Math.abs(a) < 1e-6) {
            // Луч параллелен плоскости треугольника
            return null;
        }

        // Вычисляем коэффициент для пересечения
        b = normal.dot(h) / a;

        if (b < 0) {
            // Луч направлен в обратную сторону
            return null;
        }

        // Вычисляем точку пересечения на плоскости треугольника
        Vector3 intersectionPoint = origin.add(direction.multiply(b));

        // Вычисляем барицентрические координаты
        double u = edge1.dot(edge1) * edge2.dot(h) - edge1.dot(edge2) * edge2.dot(edge1);
        double v = (edge1.dot(edge1) * h.dot(edge2) - edge1.dot(edge2) * h.dot(edge1)) / u;
        u = u / (edge1.dot(edge1) * edge2.dot(edge2) - edge1.dot(edge2) * edge2.dot(edge1));

        // Проверяем, что точка пересечения лежит внутри треугольника
        if (u >= 0 && v >= 0 && (u + v) <= 1) {
            return intersectionPoint;
        }

        return null; // Луч не пересекает треугольник
    }


    public void setSceneSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
