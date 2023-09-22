class Vertex {
    double x, y, z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vector3 toVector3() {
        return new Vector3(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "Vertex: (" + x + ", " + y + ", " + z + ")";
    }
}