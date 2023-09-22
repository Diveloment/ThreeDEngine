import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;

public class Camera {
    private Matrix4f viewMatrix;
    Matrix4f projectionMatrix = new Matrix4f();
    Matrix4f mvpMatrix = new Matrix4f();
    int screenHeight = 120;
    int screenWidth = 450;
    private Vector3 cameraPosition = new Vector3();
    private Vector3 cameraTarget = new Vector3();
    private Vector3f cameraUp = new Vector3f();
    private Scene scene;

    char[][] canvas = new char[screenHeight][screenWidth];

    public Camera(Vector3f position, Vector3f target, Vector3f up) {
        viewMatrix = new Matrix4f();
        viewMatrix.lookAt(position, target, up);
        projectionMatrix.setPerspective((float) Math.toRadians(100.0f), (float) 4 / 5, 0.1f, 100.0f);
        cameraPosition.set(position);
        cameraTarget.set(target);
        cameraUp.set(up);
        mvpMatrix.set(projectionMatrix);
        mvpMatrix.mul(viewMatrix);
    }

    public void drawVeticles(Vector3f[] vertices) {
        List<Point> points = new ArrayList<Point>();

        for (int i = 0; i < vertices.length; i++) {
            Vector3f vertex = vertices[i];
            Vector4f transformedVertex = new Vector4f(vertex.x, vertex.y, vertex.z, 1.0f);
            mvpMatrix.transform(transformedVertex);

            // Нормализуйте координаты
            transformedVertex.div(transformedVertex.w);

            // Преобразуйте устройственные координаты в экранные координаты
            float xScreen = (transformedVertex.x + 1.0f) * screenHeight / 2;
            float yScreen = (1.0f - transformedVertex.y) * screenWidth / 2;

            points.add(new Point((int) xScreen, (int) yScreen));
        }

        System.out.println(points);

        for (char[] row : canvas) {
            java.util.Arrays.fill(row, ' '); // Заполняем холст пробелами
        }

        for (Point point : points) {
            try {
                canvas[point.x][point.y] = '#';
            } catch (Exception e) {
            }
        }
        drawCanvasInConsole();
    }

    public void drawScene() {
        List<Polygon> polygons = scene.getPolygons();
        polygons.sort(Comparator.comparing(polygon -> polygon.computeCenter().distanceTo(cameraPosition)));
        Collections.reverse(polygons);

        for (char[] row : canvas) {
            java.util.Arrays.fill(row, ' '); // Заполняем холст пробелами
        }

        for (Polygon polygon : polygons) {
            List<Vertex> polygonVertices = polygon.getVertices();
            List<Point> points = new ArrayList<Point>();

            for (Vertex vertex : polygonVertices) {
                Vector4f transformedVertex = new Vector4f((float) vertex.x, (float) vertex.y, (float) vertex.z, 1.0f);
                mvpMatrix.transform(transformedVertex);

                // Нормализуйте координаты
                transformedVertex.div(transformedVertex.w);

                // Преобразуйте устройственные координаты в экранные координаты
                float xScreen = (transformedVertex.x + 1.0f) * screenHeight / 2;
                float yScreen = (1.0f - transformedVertex.y) * screenWidth / 2;

                if (xScreen < -screenHeight || yScreen < -screenWidth || xScreen > screenHeight*2 || yScreen > screenWidth*2) {
                    continue;
                }
                points.add(new Point((int) xScreen, (int) yScreen));
            }

            /*for (Point pt : points) {
                if (pt.x == Integer.MIN_VALUE || pt.y == Integer.MIN_VALUE) {
                    System.exit(0);
                }
            }*/

            List<Point> filledPoints = polygon.getFilledPoints(screenHeight, screenWidth, points);
            for (Point point : filledPoints) {
                try {
                    canvas[point.x][point.y] = polygon.filler;
                } catch (Exception e) {
                }
            }
        }
        drawCanvasInConsole();
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Vector3 getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(Vector3 cameraPosition) {
        viewMatrix = new Matrix4f();
        viewMatrix.lookAt(cameraPosition.toVector3f(), cameraTarget.toVector3f(), cameraUp);
        mvpMatrix.set(projectionMatrix);
        mvpMatrix.mul(viewMatrix);
        this.cameraPosition = cameraPosition;
    }

    public Vector3 getCameraTarget() {
        return cameraTarget;
    }

    public void setCameraTarget(Vector3 cameraTarget) {
        this.cameraTarget = cameraTarget;
    }

    private void drawCanvasInConsole() {
        StringBuilder output = new StringBuilder();

        for (char[] row : canvas) {
            output.append(row).append('\n');
        }

        // ANSI Escape код для перемещения курсора в начало консоли
        String clearScreen = "\033[H\033[2J";
        System.out.print(clearScreen + output);
    }
}