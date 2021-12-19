import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author J. Neto - 18 Dec 2021 12:28
 */
public class Percolation {

    private static final int SOURCE_ID = 0;

    private final int sinkId;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;

    private int numberOfOpenSites;

    /**
     * Creates n-by-n grid, with all sites initially blocked
     * Performance requirement: O(n^2) time
     *
     * @param n size of the grid
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        sinkId = n * n + 1;
        grid = new boolean[n][n];

        /*
         * Union-Find data structure containing two extra positions
         * to represent top (source) and bottom (sink) virtual sites
         */
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    /**
     * opens the site (row, col) if it is not open already
     * @param row row    (one-indexed)
     * @param col column (one-indexed)
     */
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return; // already open, do nothing
        }

        // open site
        numberOfOpenSites++;
        grid[row - 1][col - 1] = true;
        int openedSiteId = getSiteId(row, col);

        /*
         * Connect opened site to source or top adjacent site
         */
        if (row == 1) {
            uf.union(openedSiteId, SOURCE_ID);
        } else if (isOpen(row - 1, col)) {
            uf.union(openedSiteId, getSiteId(row - 1, col));
        }

        /*
         * Connect opened site to sink or bottom adjacent site
         */
        if (row == grid.length) {
            uf.union(openedSiteId, sinkId);
        } else if (isOpen(row + 1, col)) {
            uf.union(openedSiteId, getSiteId(row + 1, col));
        }

        /*
         * Connect opened site to left adjacent site
         */
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(openedSiteId, getSiteId(row, col - 1));
        }

        /*
         * Connect opened site to right adjacent site
         */
        if (col < grid.length && isOpen(row, col + 1)) {
            uf.union(openedSiteId, getSiteId(row, col + 1));
        }
    }

    /**
     * Does the system percolate?
     * It percolates if the source is connected to the sink.
     *
     * @return {@code true} if the system percolates, {@code false} otherwise
     */
    public boolean percolates() {
        return uf.find(SOURCE_ID) == uf.find(sinkId);
    }

    /**
     * Is the site (row, col) full?
     *
     * @param row row    (one-indexed)
     * @param col column (one-indexed)
     * @return {@code true} if the site is full, {@code false} otherwise
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        return uf.find(getSiteId(row, col)) == uf.find(SOURCE_ID);
    }

    /**
     * Is the site (row, col) open?
     * <p>
     * Note that we need to subtract 1 from the row and col in order to
     * access their actual positions in the array. This is because the
     * problem statement specified that the grid should be one-indexed.
     *
     * @param row row    (one-indexed)
     * @param col column (one-indexed)
     * @return {@code true} if the site is open, {@code false} otherwise
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    /**
     * Maps the grid to the Union-Find data structure
     * by assigning a sequential ID to each site os the grid.
     * <p>
     * i.e. the top left site is assigned ID 1, the bottom right site is assigned ID N * N
     *
     * @param row row    (one-indexed)
     * @param col column (one-indexed)
     * @return the id of the site
     */
    private int getSiteId(int row, int col) {
        return ((row-1) * grid.length) + ((col-1) + 1);
    }

    /**
     * Validates the arguments row and column.
     * <p>
     * According to the problem statement, row and column indices are
     * integers between 1 and n, where (1, 1) is the upper-left site.
     *
     * @param row row    (one-indexed)
     * @param col column (one-indexed)
     */
    private void validate(int row, int col) {
        if (row < 1 || row > grid.length) {
            throw new IllegalArgumentException(String.format("row must be between 1 and %d, provided: %d", grid.length, row));
        }
        if (col < 1 || col > grid.length) {
            throw new IllegalArgumentException(String.format("col must be between 1 and %d, provided: %d", grid.length, col));
        }
    }

    /**
     * Returns the number of open sites.
     * @return the number of open sites.
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }
}
