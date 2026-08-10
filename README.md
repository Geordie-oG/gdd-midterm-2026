# GDD Space Invaders Midterm

This fork adds the required movement, MAP-tile walls, collision behavior, and
sound effects to the supplied Space Invaders starter.

## Implemented features

- Movement in all directions with arrow keys or WASD, including diagonals.
- Scrolling, destructible walls stored directly in the integer `MAP` as tile
  value `2`; walls are not enemy objects.
- Player collision with a wall triggers an explosion and sound effect.
- A shot destroys one wall tile and triggers an explosion and sound effect.
- Firing an accepted shot plays a separate laser sound effect.
- The HUD tracks all scheduled invaders and displays `MISSION COMPLETE!` after
  the final invader is destroyed.

## Controls

- Move: Arrow keys or WASD
- Fire: Space bar

## Build and run

```bash
mkdir -p build/classes
javac -d build/classes src/gdd/*.java src/gdd/scene/*.java src/gdd/powerup/*.java src/gdd/sprite/*.java
java -cp build/classes gdd.Main
```

## References

This project is based on the
[Java Space Invaders](https://github.com/janbodnar/Java-Space-Invaders)
repository.
