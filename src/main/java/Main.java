import org.joml.Vector3f;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scene scene = new Scene();
        Camera camera = new Camera(new Vector3f(0, 0, -15), new Vector3f(0, 0, 0), new Vector3f(-1, 0, 0));
        KeyHandler keyHandler = new KeyHandler();
        camera.setScene(scene);

        Cube cube = new Cube(scene);
        cube.setPosition(new Vector3(0, 0, 50));

        keyHandler.setOnPressedAction((key) -> {
            if (key == 'w')
                cube.setPosition(cube.getPosition().add(new Vector3(0, 0, 1)));
            if (key == 's')
                cube.setPosition(cube.getPosition().add(new Vector3(0, 0, -1)));
            if (key == 'd')
                cube.setPosition(cube.getPosition().add(new Vector3(1, 0, 0)));
            if (key == 'a')
                cube.setPosition(cube.getPosition().add(new Vector3(-1, 0, 0)));
        });

        double i = 0;
        int deltaTime = 35;
        while (true) {
            Vector3 rotationAngles = new Vector3(Math.toRadians((double) deltaTime / 20), Math.toRadians((double) deltaTime / 10), Math.toRadians((double) deltaTime / 15));
            cube.rotate(rotationAngles.multiply(0.25));
            //cube.setPosition(new Vector3((Math.cos(i * deltaTime * 0.0025) * 10) - 0, 0, 0));
            camera.drawScene();
            i += 0.01 * deltaTime;
            Thread.sleep(deltaTime);
        }
    }
}
