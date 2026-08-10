package gdd.sprite;

import static gdd.Global.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 270;
    private static final int START_Y = 540;
    private int currentSpeed = 3;
    private int dy;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean movingUp;
    private boolean movingDown;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        var ii = new ImageIcon(IMG_PLAYER);

        // Scale the image to use the global scaling factor
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(new ImageIcon(scaledImage).getImage());

        setX(START_X);
        setY(START_Y);
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public int setSpeed(int speed) {
        if (speed < 1) {
            speed = 1; // Ensure speed is at least 1
        }
        this.currentSpeed = speed;
        updateVelocity();
        return currentSpeed;
    }

    @Override
    public void act() {
        x += dx;
        y += dy;

        int imageWidth = getImage().getWidth(null);
        int imageHeight = getImage().getHeight(null);
        x = Math.max(0, Math.min(x, BOARD_WIDTH - imageWidth));
        y = Math.max(0, Math.min(y, BOARD_HEIGHT - imageHeight));
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        setDirectionKey(key, true);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        setDirectionKey(key, false);
    }

    private void setDirectionKey(int key, boolean pressed) {
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movingLeft = pressed;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movingRight = pressed;
        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            movingUp = pressed;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            movingDown = pressed;
        }

        updateVelocity();
    }

    private void updateVelocity() {
        int horizontalDirection = (movingRight ? 1 : 0) - (movingLeft ? 1 : 0);
        int verticalDirection = (movingDown ? 1 : 0) - (movingUp ? 1 : 0);

        if (horizontalDirection != 0 && verticalDirection != 0) {
            int diagonalSpeed = Math.max(1,
                    (int) Math.round(currentSpeed / Math.sqrt(2.0)));
            dx = horizontalDirection * diagonalSpeed;
            dy = verticalDirection * diagonalSpeed;
        } else {
            dx = horizontalDirection * currentSpeed;
            dy = verticalDirection * currentSpeed;
        }
    }
}
