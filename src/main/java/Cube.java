import java.util.List;

public class Cube extends Shape {
    private Vertex[] cubeVertices = {
            new Vertex(-5, -5, -5),
            new Vertex(5, -5, -5),
            new Vertex(5, 5, -5),
            new Vertex(-5, 5, -5),
            new Vertex(-5, -5, 5),
            new Vertex(5, -5, 5),
            new Vertex(5, 5, 5),
            new Vertex(-5, 5, 5)
    };
    private List<Polygon> cubePolygons = List.of(
            new Polygon(//front
                    cubeVertices[3],
                    cubeVertices[2],
                    cubeVertices[1],
                    cubeVertices[0],
                    'F'
            ),
            new Polygon(//back
                    cubeVertices[4],
                    cubeVertices[5],
                    cubeVertices[6],
                    cubeVertices[7],
                    'B'
            ),
            new Polygon(//top
                    cubeVertices[0],
                    cubeVertices[1],
                    cubeVertices[5],
                    cubeVertices[4],
                    'T'
            ),
            new Polygon(//bottom
                    cubeVertices[7],
                    cubeVertices[6],
                    cubeVertices[2],
                    cubeVertices[3],
                    'b'
            ),
            new Polygon(//left
                    cubeVertices[0],
                    cubeVertices[4],
                    cubeVertices[7],
                    cubeVertices[3],
                    'L'
            ),
            new Polygon(//right
                    cubeVertices[2],
                    cubeVertices[6],
                    cubeVertices[5],
                    cubeVertices[1],
                    'R'
            )
    );

    public Cube(Scene panel) {
        this.panel = panel;
        this.vertices = cubeVertices;
        this.polygons = cubePolygons;
        AddToDrawBuffer();
    }
}