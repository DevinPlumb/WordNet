/* *****************************************************************************
 *  Name:    Devin Plumb
 *  NetID:   dplumb
 *  Precept: P06
 *
 *  Description:  Finds the least similar word to the others, based on a
 *                WordNet.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wn; // stores the wordnet as an instance variable

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int n = nouns.length;
        int[] distances = new int[n];
        int maximum = 0;
        int maximumIndex = 0;
        for (int i = 0; i < n; i++) {
            distances[i] = 0;
            for (int j = 0; j < n; j++) {
                distances[i] += wn.distance(nouns[i], nouns[j]);
            }
            if (distances[i] > maximum) {
                maximum = distances[i];
                maximumIndex = i;
            }
        }
        return nouns[maximumIndex];
    }

    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
