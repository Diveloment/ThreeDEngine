import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler implements KeyListener {

    private Set<KeyAction> onPressedActions;

    public KeyHandler() {
        this.onPressedActions = new HashSet<>();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.addKeyListener(this);
            frame.setUndecorated(true); // Убираем декорации окна (заголовок и рамку)
            frame.setBackground(new Color(0, 0, 0, 0)); // Прозрачный фон
            frame.setAlwaysOnTop(true);
            Dimension screenSize = new Dimension(100, 100);
            frame.setSize(screenSize);
            frame.setLocation(0, 0);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public void setOnPressedAction(KeyAction action) {
        this.onPressedActions.add(action);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        runOnPressedActions(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void runOnPressedActions(char keyChar) {
        for (KeyAction action : onPressedActions) {
            action.perform(keyChar);
        }
    }

    interface KeyAction {
        void perform(char key);
    }
}
