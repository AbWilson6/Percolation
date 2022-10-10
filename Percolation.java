import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // creates new variable size referring to n by n grid size
    private int size;

    // creates a new variables of type WeightedQuickUnion and represents
    // the grid that allows WeightedQuickUnion methods

    // WeightedQuickUnion object (time complexity testing)
    private WeightedQuickUnionUF unionGrid;

    // QuickFind object (time complexity testing)
    // private QuickFindUF unionGrid;

    // create variable representing the grid
    private boolean[][] grid;

    // create the virtual top and virtual bottom to help with lowering
    // time complexity and increasing efficiency
    private int virtualTop; // virtualTop
    private int virtualBottom; // virtualBottom


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // check to make sure n is a valid integer
        if (n <= 0) {
            throw new IllegalArgumentException("Must be greater than zero");
        }
        // set size equal to n
        size = n;

        // initializing a 2D array of type boolean automatically sets
        // all values to false (closed state)
        grid = new boolean[size][size];

        // initializes variable site which contains all the sites in the grid
        // which includes the n by n grid plus the 2 virtual sites

        // weightedQuickUnion object initializing
        unionGrid = new WeightedQuickUnionUF(n * n + 2);

        // quickFind object initializing (unit testing)
        // unionGrid = new QuickFindUF(n * n + 2);

        // initializing virtualTop and virtualBottom
        // virtualTop is one added site so + 1
        virtualTop = n * n;
        // virtualBottom is a second added site so + 2
        virtualBottom = n * n + 1;

    }

    // create helper function to make sure the site is within the bounds (within n
    // and greater than or equal to 0)
    private void validatePoint(int row, int col) {
        if (row >= size || row < 0) {
            throw new IllegalArgumentException("Row is out of range");
        }
        if (col >= size || col < 0) {
            throw new IllegalArgumentException("Column is out of range");
        }
    }

    // create helper function to convert my row and column value into one ID value
    private int arrayConverter(int row, int col) {
        return ((size * row) + col);
    }

    // create a helper function to check if an open site's neighbors are also open
    // if they are open, union them together
    private void checkNeighbors(int row, int col) {

        // find the id index of the site
        int siteId = arrayConverter(row, col);

        // if on the top row, union the virtual top with the site id
        // top row is 0 because that's the first row
        if (row == 0) {
            unionGrid.union(siteId, virtualTop);
        }

        // don't connect bottom row to virtual bottom yet to avoid backwash
        // connect to bottom for backwash-non safe
        // if (row == size - 1) {
        //     unionGrid.union(siteId, virtualBottom);
        // }

        // check the neighboring sites to the current site to see if
        // union should be called
        if (col != size - 1 && isOpen(row, col + 1)) {
            unionGrid.union(siteId, arrayConverter(row, col + 1));
        }
        if (col != 0 && isOpen(row, col - 1)) {
            unionGrid.union(siteId, arrayConverter(row, col - 1));
        }
        if (row != size - 1 && isOpen(row + 1, col)) {
            unionGrid.union(siteId, arrayConverter(row + 1, col));
        }
        if (row != 0 && isOpen(row - 1, col)) {
            unionGrid.union(siteId, arrayConverter(row - 1, col));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // check if the site is within the grid size
        validatePoint(row, col);

        // if value is false (meaning closed site), set the value to true (open site)
        if (!grid[row][col]) {
            // opens the site by setting it to true
            grid[row][col] = true;
            // unions any neighboring open sites
            checkNeighbors(row, col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // validate the site
        validatePoint(row, col);

        // if the site is open, return true
        if (grid[row][col]) {
            return true;
        }
        // if grid is not open or full, it must be closed, return false
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // validate the site
        validatePoint(row, col);

        // find id index of site using helping function
        int siteID = arrayConverter(row, col);

        // to check if a site is full, must check if there is a connected component
        // to the top with that site that you're on
        if (unionGrid.find(siteID) == unionGrid.find(virtualTop)) {
            return true;
        }
        return false;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        // create counter to keep track of open sites
        int counter = 0;
        // iterate through each site of the grid
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // if value at site is true, then it is open and add to counter
                if (grid[row][col]) {
                    counter++;
                }
            }
        }
        // return counter after going through whole grid
        return counter;
    }

    // does the system percolate?
    public boolean percolates() {

        // include to avoid backwash, don't include if backwash non-safe
        // iterate through each site on the bottom row, if it is full, connect
        // that site to the virtual bottom (avoids backwash)
        for (int bottomRowcol = 0; bottomRowcol < size; bottomRowcol++) {
            if (isFull(size - 1, bottomRowcol)) {
                int siteId = arrayConverter(size - 1, bottomRowcol);
                unionGrid.union(siteId, virtualBottom);
            }
        }

        // if the virtual top is connected to the virtual bottom, then
        // the system percolates
        if (unionGrid.find(virtualTop) == unionGrid.find(virtualBottom)) {
            return true;
        }
        return false;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // create a new percolation object - a grid
        Percolation percGrid = new Percolation(5);

        // open some random sites
        percGrid.open(0, 2);
        percGrid.open(1, 2);
        percGrid.open(2, 2);
        percGrid.open(3, 2);
        percGrid.open(4, 2);

        // check if a site is open
        System.out.print(percGrid.isOpen(1, 2));

        // check if a site is full
        System.out.print(percGrid.isFull(2, 2));

        // check if it percolates
        System.out.print(percGrid.percolates());

    }

}
