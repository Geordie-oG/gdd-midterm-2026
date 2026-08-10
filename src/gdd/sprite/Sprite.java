package gdd.sprite;

import java.awt.Image;
import java.awt.Rectangle;

abstract public class Sprite {

    protected boolean visible;
    protected Image image;
    protected boolean dying;
    protected int visibleFrames = 10;

    protected int x;
    protected int y;
    protected int dx;

    public Sprite() {
        visible = true;
    }

    /**
     * Default update hook for stationary sprites. Moving sprites override this.
     */
    public void act() {
        // Intentionally empty.
    }

    public boolean collidesWith(Sprite other) {
        if (other == null || !this.isVisible() || !other.isVisible()) {
            return false;
        }
        return getBounds().intersects(other.getBounds());
    }

    public Rectangle getBounds() {
        int width = image == null ? 0 : image.getWidth(null);
        int height = image == null ? 0 : image.getHeight(null);
        return new Rectangle(x, y, width, height);
    }

    public void die() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void visibleCountDown() {
        if (visibleFrames > 0) {
            visibleFrames--;
        } else {
            visible = false;
        }
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public boolean isDying() {
        return this.dying;
    }
}
