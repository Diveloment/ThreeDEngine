import org.joml.Vector3f;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scene scene = new Scene();
        Camera camera = new Camera(new Vector3f(0, 0, -15), new Vector3f(0, 0, 0), new Vector3f(-1, 0, 0));
        KeyHandler keyHandler = new KeyHandler();
        camera.setScene(scene);

        /*Cube cube = new Cube(scene);
        cube.setPosition(new Vector3(0, 0, 15));*/

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 1; k++) {
                    Cube cube = new Cube(scene);
                    cube.setPosition(new Vector3(i * 15, -15 + (k * 15), 15 + (j * 15)));
                }
            }
        }

        keyHandler.setOnPressedAction((key) -> {
            if (key == 'w')
                camera.translate(new Vector3(0, 0, 1));
            if (key == 's')
                camera.translate(new Vector3(0, 0, -1));
            if (key == 'd')
                camera.translate(new Vector3(1, 0, 0));
            if (key == 'a')
                camera.translate(new Vector3(-1, 0, 0));
        });

        double i = 0;
        int deltaTime = 5;
        while (true) {
            Vector3 rotationAngles = new Vector3(Math.toRadians((double) deltaTime / 20), Math.toRadians((double) deltaTime / 10), Math.toRadians((double) deltaTime / 15));
            //cube.rotate(rotationAngles.multiply(0.25));
            //cube.setPosition(new Vector3((Math.cos(i * deltaTime * 0.0025) * 10) - 0, 0, 0));
            camera.drawScene();
            i += 0.01 * deltaTime;
            Thread.sleep(deltaTime);
        }
    }
}
