package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import gdd.SoundEffects;
import gdd.SpawnDetails;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.sprite.Alien1;
import gdd.sprite.Enemy;
import gdd.sprite.Explosion;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene1 extends JPanel {

    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;

    private static final int BLOCK_HEIGHT = 50;
    private static final int BLOCK_WIDTH = 50;
    private static final int TILE_EMPTY = 0;
    private static final int TILE_STAR = 1;
    private static final int TILE_WALL = 2;
    private static final int SHOT_SPEED = 20;
    private static final int PLAYER_EXPLOSION_FRAMES = 40;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();

    private Timer timer;
    private final Game game;

    // Tile codes: 0 = empty, 1 = decorative star, 2 = destructible wall.
    // Walls live only in this map; they are never added to the enemy list.
    private final int[][] MAP = {
        {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {2, 2, 2, 0, 1, 0, 0, 0, 0, 0, 1, 0, 2, 2, 2},
        {2, 2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 2},
        {0, 2, 2, 0, 0, 0, 1, 0, 1, 0, 0, 0, 2, 2, 0},
        {0, 0, 2, 2, 0, 1, 0, 0, 0, 1, 0, 2, 2, 0, 0},
        {0, 0, 2, 2, 2, 0, 0, 1, 0, 0, 2, 2, 2, 0, 0},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {2, 2, 2, 0, 0, 1, 0, 0, 0, 1, 0, 0, 2, 2, 2},
        {0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0},
        {0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0},
        {2, 2, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 2, 2},
        {0, 2, 2, 0, 0, 0, 1, 0, 1, 0, 0, 0, 2, 2, 0},
        {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
        {2, 2, 2, 2, 0, 0, 0, 1, 0, 0, 0, 2, 2, 2, 2},
        {0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
        {1, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 1},
        {2, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 2},
        {0, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 0},
        {1, 0, 0, 0, 2, 2, 0, 0, 0, 2, 2, 0, 0, 0, 1},
        {2, 2, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 2, 2},
        {0, 0, 2, 2, 0, 1, 0, 0, 0, 1, 0, 2, 2, 0, 0},
        {0, 0, 0, 2, 2, 2, 0, 1, 0, 2, 2, 2, 0, 0, 0},
        {2, 2, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 2, 2},
        {1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1}
    };

    private HashMap<Integer, SpawnDetails> spawnMap = new HashMap<>();
    private AudioPlayer audioPlayer;
    private SoundEffects soundEffects;
    private int playerExplosionFramesRemaining;

    private static final class MapTile {

        private final int mapRow;
        private final int column;
        private final Rectangle bounds;

        private MapTile(int mapRow, int column, Rectangle bounds) {
            this.mapRow = mapRow;
            this.column = column;
            this.bounds = bounds;
        }
    }

    public Scene1(Game game) {
        this.game = game;
        // initBoard();
        // gameInit();
        loadSpawnDetails();
    }

    private void initAudio() {
        try {
            String filePath = gdd.ResourcePath.resolve("src/audio/scene1.wav");
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void loadSpawnDetails() {
        // TODO load this from a file
        spawnMap.put(50, new SpawnDetails("PowerUp-SpeedUp", 100, 0));
        spawnMap.put(200, new SpawnDetails("Alien1", 200, 0));
        spawnMap.put(300, new SpawnDetails("Alien1", 300, 0));

        spawnMap.put(400, new SpawnDetails("Alien1", 400, 0));
        spawnMap.put(401, new SpawnDetails("Alien1", 450, 0));
        spawnMap.put(402, new SpawnDetails("Alien1", 500, 0));
        spawnMap.put(403, new SpawnDetails("Alien1", 550, 0));

        spawnMap.put(500, new SpawnDetails("Alien1", 100, 0));
        spawnMap.put(501, new SpawnDetails("Alien1", 150, 0));
        spawnMap.put(502, new SpawnDetails("Alien1", 200, 0));
        spawnMap.put(503, new SpawnDetails("Alien1", 350, 0));
    }

    private void initBoard() {

    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.black);

        gameInit();
        initAudio();
        soundEffects = new SoundEffects();

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        try {
            if (audioPlayer != null) {
                audioPlayer.stop();
                audioPlayer = null;
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
        if (soundEffects != null) {
            soundEffects.close();
            soundEffects = null;
        }
    }

    private void gameInit() {

        enemies = new ArrayList<>();
        powerups = new ArrayList<>();
        explosions = new ArrayList<>();
        shots = new ArrayList<>();
        playerExplosionFramesRemaining = 0;
        inGame = true;
        message = "Game Over";

        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 6; j++) {
        // var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
        // ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
        // enemies.add(enemy);
        // }
        // }
        player = new Player();
        // shot = new Shot();
    }

    private void drawMap(Graphics g) {
        for (MapTile star : getVisibleTiles(TILE_STAR)) {
            drawStarCluster(g, star.bounds.x, star.bounds.y,
                    star.bounds.width, star.bounds.height);
        }

        for (MapTile wall : getVisibleTiles(TILE_WALL)) {
            drawWallTile(g, wall.bounds);
        }
    }

    private List<MapTile> getVisibleTiles(int tileType) {
        List<MapTile> visibleTiles = new ArrayList<>();
        int scrollOffset = frame % BLOCK_HEIGHT;
        int baseRow = frame / BLOCK_HEIGHT;
        int rowsNeeded = (BOARD_HEIGHT / BLOCK_HEIGHT) + 2;

        for (int screenRow = 0; screenRow < rowsNeeded; screenRow++) {
            int mapRow = (baseRow + screenRow) % MAP.length;
            int y = BOARD_HEIGHT - (screenRow * BLOCK_HEIGHT) + scrollOffset;

            if (y >= BOARD_HEIGHT || y + BLOCK_HEIGHT <= 0) {
                continue;
            }

            for (int column = 0; column < MAP[mapRow].length; column++) {
                if (MAP[mapRow][column] == tileType) {
                    Rectangle bounds = new Rectangle(column * BLOCK_WIDTH, y,
                            BLOCK_WIDTH, BLOCK_HEIGHT);
                    visibleTiles.add(new MapTile(mapRow, column, bounds));
                }
            }
        }
        return visibleTiles;
    }

    private void drawWallTile(Graphics g, Rectangle wall) {
        int x = wall.x;
        int y = wall.y;

        g.setColor(new Color(35, 85, 105));
        g.fillRect(x + 1, y + 1, wall.width - 2, wall.height - 2);
        g.setColor(new Color(80, 190, 210));
        g.drawRect(x + 2, y + 2, wall.width - 5, wall.height - 5);
        g.setColor(new Color(15, 45, 65));
        g.drawRect(x + 7, y + 7, wall.width - 15, wall.height - 15);
        g.drawLine(x + 8, y + 8, x + wall.width - 9, y + wall.height - 9);
        g.drawLine(x + wall.width - 9, y + 8, x + 8, y + wall.height - 9);
    }

    private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
        // Set star color to white
        g.setColor(Color.WHITE);

        // Draw multiple stars in a cluster pattern
        // Main star (larger)
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g.fillOval(centerX - 2, centerY - 2, 4, 4);

        // Smaller surrounding stars
        g.fillOval(centerX - 15, centerY - 10, 2, 2);
        g.fillOval(centerX + 12, centerY - 8, 2, 2);
        g.fillOval(centerX - 8, centerY + 12, 2, 2);
        g.fillOval(centerX + 10, centerY + 15, 2, 2);

        // Tiny stars for more detail
        g.fillOval(centerX - 20, centerY + 5, 1, 1);
        g.fillOval(centerX + 18, centerY - 15, 1, 1);
        g.fillOval(centerX - 5, centerY - 18, 1, 1);
        g.fillOval(centerX + 8, centerY + 20, 1, 1);
    }

    private void drawAliens(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPowreUps(Graphics g) {

        for (PowerUp p : powerups) {

            if (p.isVisible()) {

                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }

            if (p.isDying()) {

                p.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        for (Shot shot : shots) {

            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void drawBombing(Graphics g) {

        // for (Enemy e : enemies) {
        //     Enemy.Bomb b = e.getBomb();
        //     if (!b.isDestroyed()) {
        //         g.drawImage(b.getImage(), b.getX(), b.getY(), this);
        //     }
        // }
    }

    private void drawExplosions(Graphics g) {

        List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
                explosion.visibleCountDown();
                if (!explosion.isVisible()) {
                    toRemove.add(explosion);
                }
            }
        }

        explosions.removeAll(toRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.white);
        g.drawString("FRAME: " + frame, 10, 10);

        g.setColor(Color.green);

        if (inGame) {

            drawMap(g);  // Draw background stars first
            drawExplosions(g);
            drawPowreUps(g);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    private void update() {
        if (playerExplosionFramesRemaining > 0) {
            playerExplosionFramesRemaining--;
            if (playerExplosionFramesRemaining == 0) {
                endGame("Ship destroyed!");
            }
            return;
        }

        // Check enemy spawn
        // TODO this approach can only spawn one enemy at a frame
        SpawnDetails sd = spawnMap.get(frame);
        if (sd != null) {
            // Create a new enemy based on the spawn details
            switch (sd.type) {
                case "Alien1":
                    Enemy enemy = new Alien1(sd.x, sd.y);
                    enemies.add(enemy);
                    break;
                // Add more cases for different enemy types if needed
                case "Alien2":
                    // Enemy enemy2 = new Alien2(sd.x, sd.y);
                    // enemies.add(enemy2);
                    break;
                case "PowerUp-SpeedUp":
                    // Handle speed up item spawn
                    PowerUp speedUp = new SpeedUp(sd.x, sd.y);
                    powerups.add(speedUp);
                    break;
                default:
                    System.out.println("Unknown enemy type: " + sd.type);
                    break;
            }
        }

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            endGame("Game won!");
            return;
        }

        // player
        player.act();
        List<MapTile> visibleWalls = getVisibleTiles(TILE_WALL);
        if (checkPlayerWallCollision(visibleWalls)) {
            return;
        }

        // Power-ups
        for (PowerUp powerup : powerups) {
            if (powerup.isVisible()) {
                powerup.act();
                if (powerup.collidesWith(player)) {
                    powerup.upgrade(player);
                }
            }
        }

        // Enemies
        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                enemy.act(direction);
            }
        }

        updateShots(visibleWalls);

        // enemies
        // for (Enemy enemy : enemies) {
        //     int x = enemy.getX();
        //     if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
        //         direction = -1;
        //         for (Enemy e2 : enemies) {
        //             e2.setY(e2.getY() + GO_DOWN);
        //         }
        //     }
        //     if (x <= BORDER_LEFT && direction != 1) {
        //         direction = 1;
        //         for (Enemy e : enemies) {
        //             e.setY(e.getY() + GO_DOWN);
        //         }
        //     }
        // }
        // for (Enemy enemy : enemies) {
        //     if (enemy.isVisible()) {
        //         int y = enemy.getY();
        //         if (y > GROUND - ALIEN_HEIGHT) {
        //             inGame = false;
        //             message = "Invasion!";
        //         }
        //         enemy.act(direction);
        //     }
        // }
        // bombs - collision detection
        // Bomb is with enemy, so it loops over enemies
        /*
        for (Enemy enemy : enemies) {

            int chance = randomizer.nextInt(15);
            Enemy.Bomb bomb = enemy.getBomb();

            if (chance == CHANCE && enemy.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(enemy.getX());
                bomb.setY(enemy.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()
                    && bombX >= (playerX)
                    && bombX <= (playerX + PLAYER_WIDTH)
                    && bombY >= (playerY)
                    && bombY <= (playerY + PLAYER_HEIGHT)) {

                var ii = new ImageIcon(IMG_EXPLOSION);
                player.setImage(ii.getImage());
                player.setDying(true);
                bomb.setDestroyed(true);
            }

            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 1);
                if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
         */
    }

    private boolean checkPlayerWallCollision(List<MapTile> visibleWalls) {
        if (!player.isVisible()) {
            return false;
        }

        Rectangle playerBounds = player.getBounds();
        for (MapTile wall : visibleWalls) {
            if (playerBounds.intersects(wall.bounds)) {
                player.die();
                explosions.add(new Explosion(playerBounds.x, playerBounds.y));
                playerExplosionFramesRemaining = PLAYER_EXPLOSION_FRAMES;
                stopBackgroundAudio();
                if (soundEffects != null) {
                    soundEffects.playPlayerExplosion();
                }
                return true;
            }
        }
        return false;
    }

    private void updateShots(List<MapTile> visibleWalls) {
        List<Shot> shotsToRemove = new ArrayList<>();

        for (Shot shot : shots) {
            if (!shot.isVisible()) {
                shotsToRemove.add(shot);
                continue;
            }

            Rectangle currentBounds = shot.getBounds();
            int nextY = shot.getY() - SHOT_SPEED;
            Rectangle sweptBounds = new Rectangle(
                    currentBounds.x,
                    Math.min(currentBounds.y, nextY),
                    Math.max(1, currentBounds.width),
                    currentBounds.height + Math.abs(currentBounds.y - nextY));

            boolean wallWasHit = false;
            for (MapTile wall : visibleWalls) {
                if (MAP[wall.mapRow][wall.column] == TILE_WALL
                        && sweptBounds.intersects(wall.bounds)) {
                    MAP[wall.mapRow][wall.column] = TILE_EMPTY;
                    shot.die();
                    shotsToRemove.add(shot);
                    explosions.add(new Explosion(wall.bounds.x + 7, wall.bounds.y + 7));
                    if (soundEffects != null) {
                        soundEffects.playWallBreak();
                    }
                    wallWasHit = true;
                    break;
                }
            }

            if (wallWasHit) {
                continue;
            }

            boolean enemyWasHit = false;
            for (Enemy enemy : enemies) {
                if (enemy.isVisible() && sweptBounds.intersects(enemy.getBounds())) {
                    enemy.setDying(true);
                    explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                    deaths++;
                    shot.die();
                    shotsToRemove.add(shot);
                    enemyWasHit = true;
                    break;
                }
            }

            if (enemyWasHit) {
                continue;
            }

            if (nextY + currentBounds.height < 0) {
                shot.die();
                shotsToRemove.add(shot);
            } else {
                shot.setY(nextY);
            }
        }

        shots.removeAll(shotsToRemove);
    }

    private void endGame(String gameOverMessage) {
        inGame = false;
        message = gameOverMessage;
        stopBackgroundAudio();
    }

    private void stopBackgroundAudio() {
        try {
            if (audioPlayer != null) {
                audioPlayer.stop();
                audioPlayer = null;
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void doGameCycle() {
        frame++;
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (player.isVisible()) {
                player.keyPressed(e);
            }

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame && player.isVisible()) {
                System.out.println("Shots: " + shots.size());
                if (shots.size() < 4) {
                    // Create a new shot and add it to the list
                    Shot shot = new Shot(x, y);
                    shots.add(shot);
                    if (soundEffects != null) {
                        soundEffects.playShot();
                    }
                }
            }

        }
    }
}
