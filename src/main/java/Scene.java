import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Polygon> polygons = new ArrayList<>();

    public void putPolygon(Polygon polygon) {
        polygons.remove(polygon);
        polygons.add(polygon);
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}
