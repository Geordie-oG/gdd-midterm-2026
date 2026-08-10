package gdd;

public class Global {
    private Global() {
        // Prevent instantiation
    }

    public static final int SCALE_FACTOR = 3; // Scaling factor for sprites

    public static final int BOARD_WIDTH = 716; // Doubled from 358
    public static final int BOARD_HEIGHT = 700; // Doubled from 350
    public static final int BORDER_RIGHT = 60; // Doubled from 30
    public static final int BORDER_LEFT = 10; // Doubled from 5

    public static final int GROUND = 580; // Doubled from 290
    public static final int BOMB_HEIGHT = 10; // Doubled from 5

    public static final int ALIEN_HEIGHT = 24; // Doubled from 12
    public static final int ALIEN_WIDTH = 24; // Doubled from 12
    public static final int ALIEN_INIT_X = 300; // Doubled from 150
    public static final int ALIEN_INIT_Y = 10; // Doubled from 5
    public static final int ALIEN_GAP = 30; // Gap between aliens

    public static final int GO_DOWN = 30; // Doubled from 15
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    public static final int CHANCE = 5;
    public static final int DELAY = 17;
    public static final int PLAYER_WIDTH = 30; // Doubled from 15
    public static final int PLAYER_HEIGHT = 20; // Doubled from 10

    // Images
    public static final String IMG_ENEMY = ResourcePath.resolve("src/images/alien.png");
    public static final String IMG_PLAYER = ResourcePath.resolve("src/images/player.png");
    public static final String IMG_SHOT = ResourcePath.resolve("src/images/shot.png");
    public static final String IMG_EXPLOSION = ResourcePath.resolve("src/images/explosion.png");
    public static final String IMG_TITLE = ResourcePath.resolve("src/images/title.png");
    public static final String IMG_POWERUP_SPEEDUP = ResourcePath.resolve("src/images/powerup-s.png");

    // One-shot sound effects
    public static final String SFX_SHOT = ResourcePath.resolve("src/audio/shot.wav");
    public static final String SFX_WALL_BREAK = ResourcePath.resolve("src/audio/wall-break.wav");
    public static final String SFX_PLAYER_EXPLOSION = ResourcePath.resolve("src/audio/player-explosion.wav");
}
