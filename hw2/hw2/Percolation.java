package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;
    private WeightedQuickUnionUF wqu;
    private int N;
    private int dummyNode;
    private int openNumber;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.N = N;
        grid = new int[N][N];
        dummyNode = (int) (Math.pow(N,2));
        wqu = new WeightedQuickUnionUF(dummyNode+2);
        openNumber = 0;
    }         // create N-by-N grid, with all sites initially blocked

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        int converted = convert(row, col);
        if (row == 0) {
            wqu.union(converted, dummyNode);
        } else if (row == N-1) {
            wqu.union(converted, dummyNode+1);
        }
        if (row > 0 && isOpen(row-1, col)) {
            wqu.union(convert(row-1, col), converted);
        }
        if (row < N - 1 && isOpen(row+1, col)) {
            wqu.union(convert(row+1, col), converted);
        }
        if (col > 0 && isOpen(row, col-1)) {
            wqu.union(convert(row, col-1), converted);
        }
        if (col < N - 1 && isOpen(row, col+1)) {
            wqu.union(convert(row, col+1), converted);
        }
        if (wqu.connected(converted, dummyNode)) {
            grid[row][col] = 2;
        } else {
            grid[row][col] = 1;
        }
        openNumber += 1;
    }         // open the site (row, col) if it is not open already

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return grid[row][col] != 0;
    }        // is the site (row, col) open?

    public boolean isFull(int row, int col) {
        if (grid[row][col] == 2) {
            return true;
        } else if (wqu.connected(convert(row, col), dummyNode)){
            grid[row][col] = 2;
            return true;
        } else {
            return false;
        }
    }        // is the site (row, col) full?

    public int numberOfOpenSites() {
        return openNumber;
    }        // number of open sites

    public boolean percolates() {
        return wqu.connected(dummyNode, dummyNode+1);
    }       // does the system percolate?

    public static void main(String[] args) {

    }       // use for unit testing (not required)

    private int convert(int row, int col) {
        return row * N + col;
    }


}
