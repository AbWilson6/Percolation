import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    // create variable to keep track of time
    private static double time;

    // create variable to keep track of trials
    private int T;

    // create variable to keep track of open sites over all trials
    private double[] percolatedTrials;

    // create variable for mean
    private double mean;

    // create variable for standard deviation
    private double stdDev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }

        // set T
        T = trials;

        // instantiate array percolationTrials
        percolatedTrials = new double[T];

        // run the trials
        for (int trial = 0; trial < T; trial++) {

            // set percolation grid with n
            Percolation grid = new Percolation(n);

            while (!grid.percolates()) {
                // Generate two random numbers to represent the site row and column
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);

                // if the site is already open, skip
                if (!grid.isOpen(row, col)) {
                    // if the site is not open, open it
                    grid.open(row, col);
                }
            }
            // once the system percolates, add the number of open sites it took
            // to the percolatedTrials array
            percolatedTrials[trial] = ((double) grid.numberOfOpenSites() / (double) (n * n));
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(percolatedTrials);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stdDev = StdStats.stddev(percolatedTrials);
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean - ((1.96 * stdDev) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean + ((1.96 * stdDev) / Math.sqrt(T));
    }

    // test client (see below)
    public static void main(String[] args) {
        // Get n value from user
        int n = Integer.parseInt(args[0]);

        // Get T value from user
        int tri = Integer.parseInt(args[1]);

        // start stopwatch before function you want to see runtime for
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, tri);
        // stop stopwatch
        time = stopwatch.elapsedTime();

        StdOut.println("mean()  " + stats.mean());
        StdOut.println("stddev()    " + stats.stddev());
        StdOut.println("confidenceLow   " + stats.confidenceLow());
        StdOut.println("confidenceHigh  " + stats.confidenceHigh());
        StdOut.println("elapsed time    " + time);
    }

}
