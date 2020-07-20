package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void addHexagon(TETile[][] world, int xPos, int yPos, int s, TETile t) {
        int xStart = xPos + s;
        int yStart = yPos;
        // Draws the top s lines.
        for (int y = yStart; y < yStart + s; y += 1) {
            int indent = y - yStart + 1;
            for (int x = xStart - indent; x < xStart + indent; x += 1) {
                world[x][y] = t;
            }
        }
        // Draws the bottom s lines.
        yStart += s;
        for (int y = yStart; y < yStart + s; y += 1) {
            int indent = yStart - y + s;
            for (int x = xStart + indent - 1; x >= xStart - indent; x -= 1) {
                world[x][y] = t;
            }
        }
    }
}
