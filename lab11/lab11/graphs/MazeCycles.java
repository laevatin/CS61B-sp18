package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound = false;
    private int[] from;
    private int s;

    public MazeCycles(Maze m) {
        super(m);
        s = maze.xyTo1D(1,1);
        distTo[s] = 0;
        from = new int[m.V()];
        from[s] = s;
    }

    @Override
    public void solve() {
        dfs(s);
    }

    private void dfs(int v) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (cycleFound) {
                return;
            }

            if (!marked[w]) {
                from[w] = v;
                dfs(w);
            } else if (from[v] != w) {
                cycleFound = true;
                from[w] = v;

                int cur = v;
                edgeTo[cur] = from[cur];
                while (cur != w) {
                    cur = from[cur];
                    edgeTo[cur] = from[cur];
                }
                announce();
                return;
            }

        }

    }

}

