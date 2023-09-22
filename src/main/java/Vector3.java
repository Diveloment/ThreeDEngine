import org.joml.Vector3f;

class Vector3 {
    double x, y, z;

    public Vector3() {
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public Vector3 normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
        return this;
    }

    public double dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        double resultX = this.y * other.z - this.z * other.y;
        double resultY = this.z * other.x - this.x * other.z;
        double resultZ = this.x * other.y - this.y * other.x;
        return new Vector3(resultX, resultY, resultZ);
    }

    public double distanceTo(Vector3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3 calculateReflectionDirection(Vector3 incident, Vector3 normal) {
        // Вычисляем скалярное произведение векторов incident и normal
        double dotProduct = incident.dot(normal);

        // Вычисляем отраженный вектор
        Vector3 reflection = incident.subtract(normal.multiply(2 * dotProduct));

        // Нормализуем отраженный вектор
        return reflection.normalize();
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public String toString() {
        return "Vector3: (" + x + ", " + y + ", " + z + ")";
    }

    public void set(Vector3f position) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }

    public Vector3f toVector3f() {
        Vector3f v = new Vector3f((float) this.x, (float) this.y, (float) this.z);
        return new Vector3f((float) this.x, (float) this.y, (float) this.z);
    }
}
