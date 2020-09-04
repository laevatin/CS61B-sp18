package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

public class Solver {

    private final class SearchNode {
        private final WorldState state;
        private final int numOfMoves;
        private final SearchNode previous;
        private final int priority;

        public SearchNode(WorldState ws, int moves, SearchNode prev) {
            state = ws;
            numOfMoves = moves;
            previous = prev;
            priority = numOfMoves + ws.estimatedDistanceToGoal();
        }
    }

    private class NodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode node1, SearchNode node2) {
            if (node1 == null && node2 == null) {
                return 0;
            }
            if (node1 == null) {
                return -1;
            } else if (node2 == null) {
                return 1;
            } else {
                return node1.priority - node2.priority;
            }
        }
    }

    private final int numOfMoves;
    private final List<WorldState> solution;

    public Solver(WorldState initial) {
        MinPQ<SearchNode> nodeMinPQ = new MinPQ<>(new NodeComparator());
        SearchNode tmp = new SearchNode(initial, 0, null);

        while (!tmp.state.isGoal()) {
            int m = tmp.numOfMoves;

            for (WorldState nextState : tmp.state.neighbors()) {
                if (tmp.previous == null || !nextState.equals(tmp.previous.state)) {
                    nodeMinPQ.insert(new SearchNode(nextState, m + 1, tmp));
                }
            }

            tmp = nodeMinPQ.delMin();
        }

        numOfMoves = tmp.numOfMoves;
        solution = new LinkedList<>();
        while (tmp != null) {
            solution.add(tmp.state);
            tmp = tmp.previous;
        }

        Collections.reverse(solution);

    }

    public int moves() {
        return numOfMoves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }

}
