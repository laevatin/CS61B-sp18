package hw4.puzzle;

import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState {

    private final int[][] tiles;
    private final int size;
    private final int BLANK = 0;

    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = new int[size][size];

        for (int i = 0; i < size; i += 1) {
            this.tiles[i] = Arrays.copyOf(tiles[i], size);
        }
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i > size - 1 || j < 0 || j > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public int size() {
        return size;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int posx = -1;
        int posy = -1;
        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (tiles[i][j] == BLANK) {
                    posx = j;
                    posy = i;
                }
            }
        }
        int[][] neighbor = new int[size][size];

        for (int i = 0; i < size; i += 1) {
            neighbor[i] = Arrays.copyOf(tiles[i], size);
        }

        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                if (Math.abs(i - posy) + Math.abs(j - posx) == 1) {
                    neighbor[posy][posx] = neighbor[i][j];
                    neighbor[i][j] = BLANK;
                    neighbors.enqueue(new Board(neighbor));
                    neighbor[i][j] = neighbor[posy][posx];
                    neighbor[posy][posx] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int diff = 0;

        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                int expected = i * size + j + 1;
                if (expected == size * size) {
                    continue;
                }

                if (tiles[i][j] != expected) {
                    diff += 1;
                }
            }
        }

        return diff;
    }

    public int manhattan() {
        int diff = 0;

        for (int i = 0; i < size; i += 1) {
            for (int j = 0; j < size; j += 1) {
                int actualValue = tiles[i][j];
                if (actualValue == 0) {
                    continue;
                }
                int expectedRow = (actualValue - 1) / size;
                int expectedColumn = (actualValue - 1) % size;
                diff += (Math.abs(expectedRow - i) + Math.abs(expectedColumn - j));
            }
        }
        return diff;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board other = (Board) y;
        if (Arrays.deepEquals(other.tiles, this.tiles)) {
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
