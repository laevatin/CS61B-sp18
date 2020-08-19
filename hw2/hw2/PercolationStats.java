package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private PercolationFactory pf;
    private int[] simulateData;
    private int T;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.pf = pf;
        this.T = T;
        simulateData = new int[T];
        for (int i = 0; i < T; i += 1) {
            simulateData[i] = (double) simulateOnce(N) / (double) (N * N);
        }
    }  // perform T independent experiments on an N-by-N grid
    public double mean() {
        return StdStats.mean(simulateData);
    }  // sample mean of percolation threshold
    public double stddev() {
        return StdStats.stddev(simulateData);
    }  // sample standard deviation of percolation threshold
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }  // low endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }  // high endpoint of 95% confidence interval
    private int simulateOnce(int N) {
        Percolation perc = pf.make(N);
        int numberOfOpenSites = 0;
        while (!perc.percolates()) {
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            if (!perc.isOpen(row, col)) {
                perc.open(row, col);
                numberOfOpenSites += 1;
            }
        }
        return numberOfOpenSites;
    }

}

