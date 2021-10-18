/* *****************************************************************************
 *  Name:    Devin Plumb
 *  NetID:   dplumb
 *  Precept: P06
 *
 *  Description:  Immutable data type that implements a ShortestCommonAncestor
 *                data type.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class ShortestCommonAncestor {

    private final Digraph graph; // input graph is saved as an instance variable

    // private class used to find breadth first paths using hashing
    private class BFDPs {
        // min distance from set of source vertices to given vertex
        private final HashMap<Integer, Integer> distTo;

        // constructor for multiple sources
        public BFDPs(Digraph G, Iterable<Integer> sources) {
            distTo = new HashMap<Integer, Integer>();
            bfs(G, sources);
        }

        // BFS from multiple sources
        private void bfs(Digraph G, Iterable<Integer> sources) {
            Queue<Integer> q = new Queue<Integer>();
            for (int s : sources) {
                distTo.put(s, 0);
                q.enqueue(s);
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (distTo.get(w) == null) {
                        distTo.put(w, distTo.get(v) + 1);
                        q.enqueue(w);
                    }
                }
            }
        }

        // is the vertex reachable from the source vertices
        public boolean hasPathTo(int v) {
            return distTo.get(v) != null;
        }

        // what is the min dist between the vertex and the source vertices
        public int distTo(int v) {
            return distTo.get(v);
        }

        // the full set of reachable vertices
        public Iterable<Integer> bfdps() {
            return distTo.keySet();
        }
    }

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {

        // check the graph is not null
        if (G == null) {
            throw new IllegalArgumentException("null argument");
        }

        // check the graph is acyclic
        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("graph has cycle");
        }

        // check the graph has a single root
        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots++;
                if (roots > 1)
                    break;
            }
        }
        if (roots != 1) {
            throw new IllegalArgumentException("graph not rooted");
        }

        graph = new Digraph(G);

    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v > graph.V() || w > graph.V()) {
            throw new IllegalArgumentException("argument out of range");
        }
        Queue<Integer> subsetA = new Queue<>();
        Queue<Integer> subsetB = new Queue<>();
        subsetA.enqueue(v);
        subsetB.enqueue(w);
        return getShortest(subsetA, subsetB)[0];
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v > graph.V() || w > graph.V()) {
            throw new IllegalArgumentException("argument out of range");
        }
        Queue<Integer> subsetA = new Queue<>();
        Queue<Integer> subsetB = new Queue<>();
        subsetA.enqueue(v);
        subsetB.enqueue(w);
        return getShortest(subsetA, subsetB)[1];
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA,
                            Iterable<Integer> subsetB) {
        verify(subsetA, subsetB);
        return getShortest(subsetA, subsetB)[0];
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA,
                              Iterable<Integer> subsetB) {
        verify(subsetA, subsetB);
        return getShortest(subsetA, subsetB)[1];
    }

    // verifies the necessary conditions for subset function calls
    private void verify(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) {
            throw new IllegalArgumentException("no vertices in argument");
        }
        for (Integer i : subsetA) {
            if (i == null)
                throw new IllegalArgumentException("null item in argument");
        }
        for (Integer i : subsetB) {
            if (i == null)
                throw new IllegalArgumentException("null item in argument");
        }
    }

    // this is a helper function that takes finds the closest ancestor to two
    // subsets and the corresponding aggregate distance
    private int[] getShortest(Iterable<Integer> v, Iterable<Integer> w) {
        BFDPs vPaths = new BFDPs(graph, v);
        BFDPs wPaths = new BFDPs(graph, w);
        int length = Integer.MAX_VALUE;
        int ancestor = Integer.MAX_VALUE;
        for (int i : vPaths.bfdps()) {
            if (wPaths.hasPathTo(i) &&
                    vPaths.distTo(i) + wPaths.distTo(i) < length) {
                length = vPaths.distTo(i) + wPaths.distTo(i);
                ancestor = i;
            }
        }
        return new int[] { length, ancestor };
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        Queue<Integer> queue1 = new Queue<>();
        queue1.enqueue(6);
        queue1.enqueue(3);
        queue1.enqueue(1);
        Queue<Integer> queue2 = new Queue<>();
        queue2.enqueue(2);
        queue2.enqueue(4);
        StdOut.println(sca.lengthSubset(queue1, queue2));
        StdOut.println(sca.ancestorSubset(queue1, queue2));
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
