package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

public class Game {
    TERenderer ter = new TERenderer();
    Random random;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private class Position {
        public int x;
        public int y;

        public Position(int xPos, int yPos){
            x = xPos;
            y = yPos;
        }
    }
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        random = new Random(999);
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        initWorld(world);
        genWorld(world, 1);
        //drawHallway(world, new Position(10,1), new Position(1,10));
        ter.renderFrame(world);
    }

    /**
     * Fill the world with Tileset.NOTHING.
     * @param world the 2D TETile[][] representing the state of the world.
     */
    private void initWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void genWorld(TETile[][] world, double chance) {
        Position pos = new Position(1, 1);
        Queue<Position> rooms = new LinkedList<>();
        for (int x = 1; x < WIDTH - 2; x += 1) {
            for (int y = 1; y < HEIGHT - 2; y += 1) {
                if (RandomUtils.bernoulli(random, chance / 50) && !world[x][y].equals(Tileset.FLOOR)) {
                    pos.x = x;
                    pos.y = y;
                    Position room = tryDrawRoom(world, pos);
                    if (room != null) {
                        rooms.add(room);
                    }
                }
            }
        }

        while(!rooms.isEmpty()) {
            Position cur = rooms.poll();
            Position next = rooms.peek();
            if (next != null){
                drawHallway(world, cur, next);
            }
        }

        drawWall(world);
        //first generate a world that is connected, then draw the wall.
    }

    /**
     * Draw a single room in the world with the coordinates of its lower left corner.
     * @param world the 2D TETile[][] representing the state of the world.
     * @param pos the position of its lower left corner.
     * @param len the length of the room (without the walls), should be larger than 1.
     * @param wid the width of the room (without the walls), should be larger than 1.
     */
    private void drawRoom(TETile[][] world, Position pos, int len, int wid) {
        int xStart = pos.x;
        int yStart = pos.y;
        for (int i = xStart; i < xStart + wid; i += 1) {
            for (int j = yStart; j < yStart + len; j += 1) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Try to draw a room in the position pos
     * @return if the room is drawn successfully, returns a random position inside the room,
     * otherwise, returns null.
     */
    private Position tryDrawRoom(TETile[][] world, Position pos) {
        int len = RandomUtils.uniform(random, 2, 7);
        int wid = RandomUtils.uniform(random, 2, 7);

        if (!checkSpace(world, pos, len, wid)) {
            return null;
        }
        drawRoom(world, pos, len, wid);

        int x = RandomUtils.uniform(random, pos.x, pos.x + wid);
        int y = RandomUtils.uniform(random, pos.y, pos.y + len);

        return new Position(x, y);
    }

    /**
     * Checks whether the space described by xStart, yStart and len, wid is NOTHING. If all
     * of the space is NOTHING, returns true.
     */
    private boolean checkSpace(TETile[][] world, Position pos, int len, int wid) {
        int xStart = pos.x;
        int yStart = pos.y;

        if ((xStart + wid > WIDTH - 1) || (yStart + len > HEIGHT - 1)) {
            return false;
        }
        for (int i = xStart; i < xStart + wid; i += 1) {
            for (int j = yStart; j < yStart + len; j += 1) {
                if ((world[i][j]).equals(Tileset.FLOOR)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawHallway(TETile[][] world, Position pos1, Position pos2) {
        int length = Math.abs(pos1.x - pos2.x) + 1;
        Position connectPos;

        if (pos1.x > pos2.x) {
            drawLine(world, pos2, length, 1);
            connectPos = new Position(pos1.x, pos2.y);
        } else {
            drawLine(world, pos1, length, 1);
            connectPos = new Position(pos2.x, pos1.y);
        }

        length = Math.abs(pos1.y - pos2.y) + 1;

        if (connectPos.y > pos2.y) {
            drawLine(world, pos2, length, 0);
        } else if (connectPos.y < pos2.y) {
            drawLine(world, connectPos, length, 0);
        } else if (connectPos.y > pos1.y) {
            drawLine(world, pos1, length, 0);
        } else if (connectPos.y < pos1.y) {
            drawLine(world, connectPos, length, 0);
        } else {
            return;
        }

    }

    /**
     * Draw a single line in the world.
     * @param len the length of the room (without the walls), should be larger than 1.
     * @param axis 1 represents the direction to the right, 0 represents the direction to the top.
     */
    private void drawLine(TETile[][] world, Position start, int len, int axis) {
        if (axis == 0) {
            for (int y = start.y; y < start.y + len; y += 1) {
                world[start.x][y] = Tileset.FLOOR;
            }
        } else if (axis == 1) {
            for (int x = start.x; x < start.x + len; x += 1) {
                world[x][start.y] = Tileset.FLOOR;
            }
        } else {
            throw new RuntimeException("wrong axis");
        }
    }

    private void drawWall(TETile[][] world) {
        Position pos = new Position(0, 0);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                pos.x = x;
                pos.y = y;
                if ((world[x][y].equals(Tileset.NOTHING)) && checkAdjacent(world, pos)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    private boolean checkAdjacent(TETile[][] world, Position pos) {
        int x = pos.x;
        int y = pos.y;

        /*if (x == 0 && y == 0) {
            return world[1][1].equals(Tileset.FLOOR);
        } else if (x == 0 && y == HEIGHT - 1) {
            return world[1][HEIGHT - 2].equals(Tileset.FLOOR);
        } else if (x == WIDTH - 1 && y == 0) {
            return world[WIDTH - 2][1].equals(Tileset.FLOOR);
        } else if (x == WIDTH - 1 && y == HEIGHT - 1) {
            return world[WIDTH - 2][HEIGHT - 2].equals(Tileset.FLOOR);
        } else if (x == 0) {
            return world[1][y].equals(Tileset.FLOOR) ||
                    world[1][y + 1].equals(Tileset.FLOOR) ||
                    world[1][y - 1].equals(Tileset.FLOOR);
        } else if (y == 0) {
            return world[x][1].equals(Tileset.FLOOR) ||
                    world[x - 1][1].equals(Tileset.FLOOR) ||
                    world[x + 1][1].equals(Tileset.FLOOR);
        } else if (x == WIDTH - 1) {
            return world[WIDTH - 2][y].equals(Tileset.FLOOR) ||
                    world[WIDTH - 2][y - 1].equals(Tileset.FLOOR) ||
                    world[WIDTH - 2][y + 1].equals(Tileset.FLOOR);
        } else if (y == HEIGHT - 1) {
            return world[x][HEIGHT - 2].equals(Tileset.FLOOR) ||
                    world[x - 1][HEIGHT - 2].equals(Tileset.FLOOR) ||
                    world[x + 1][HEIGHT - 2].equals(Tileset.FLOOR);
        } else {
            return world[x][y - 1].equals(Tileset.FLOOR) ||
                    world[x - 1][y - 1].equals(Tileset.FLOOR) ||
                    world[x + 1][y - 1].equals(Tileset.FLOOR) ||
                    world[x][y + 1].equals(Tileset.FLOOR) ||
                    world[x - 1][y + 1].equals(Tileset.FLOOR) ||
                    world[x + 1][y + 1].equals(Tileset.FLOOR) ||
                    world[x - 1][y].equals(Tileset.FLOOR) ||
                    world[x + 1][y].equals(Tileset.FLOOR);
        }*/
        return checkequal(world, x - 1, y - 1) || checkequal(world, x, y - 1) ||
                checkequal(world, x + 1, y - 1) || checkequal(world, x - 1, y) ||
                checkequal(world, x + 1, y) || checkequal(world, x - 1, y + 1) ||
                checkequal(world, x, y + 1) || checkequal(world, x + 1, y + 1);
    }

    private boolean checkequal(TETile[][] world, int x, int y) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return false;
        } else {
            return !world[x][y].equals(Tileset.NOTHING) && !world[x][y].equals(Tileset.WALL);
        }
    }
    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
}
