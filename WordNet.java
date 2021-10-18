/* *****************************************************************************
 *  Name:    Devin Plumb
 *  NetID:   dplumb
 *  Precept: P06
 *
 *  Description:  Immutable data type that implements a WordNet data type.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {

    // converts id number to string of synonyms
    private final HashMap<Integer, String> idMap;
    // converts an individual synonym to the set of corresponding id numbers
    private final HashMap<String, Queue<Integer>> synMap;
    // calculates distances between related words
    private final ShortestCommonAncestor wordNet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new IllegalArgumentException("null synsets");
        }
        if (hypernyms == null) {
            throw new IllegalArgumentException("null hypernyms");
        }

        idMap = new HashMap<>();
        synMap = new HashMap<>();
        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            String[] line = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String synset = line[1];
            String[] syns = synset.split(" ");
            idMap.put(id, synset);
            for (String syn : syns) {
                Queue<Integer> ids = synMap.get(syn);
                if (ids == null) {
                    ids = new Queue<Integer>();
                }
                ids.enqueue(id);
                synMap.put(syn, ids);
            }
        }

        Digraph digraph = new Digraph(idMap.size());
        In hypernymsIn = new In(hypernyms);
        while (!hypernymsIn.isEmpty()) {
            String[] information = hypernymsIn.readLine().split(",");
            int i = information.length;
            int v = Integer.parseInt(information[0]);
            for (int w = 1; w < i; w++) {
                digraph.addEdge(v, Integer.parseInt(information[w]));
            }
        }
        wordNet = new ShortestCommonAncestor(digraph);
    }

    // all WordNet nouns
    public Iterable<String> nouns() {
        return synMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("null argument");
        }
        return synMap.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        Queue<Integer> ids1 = synMap.get(noun1);
        Queue<Integer> ids2 = synMap.get(noun2);
        if (ids1 == null || ids2 == null) {
            throw new IllegalArgumentException("argument not a wordnet noun");
        }
        int ancestor = wordNet.ancestorSubset(ids1, ids2);
        return idMap.get(ancestor);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        Queue<Integer> ids1 = synMap.get(noun1);
        Queue<Integer> ids2 = synMap.get(noun2);
        if (ids1 == null || ids2 == null) {
            throw new IllegalArgumentException("argument not a wordnet noun");
        }
        return wordNet.lengthSubset(ids1, ids2);
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        for (String i : wn.nouns()) {
            StdOut.println(i);
            StdOut.println(wn.isNoun(i));
        }
        String random1 = "'hood";
        String random2 = "1530s";
        StdOut.println(wn.sca(random1, random2));
        StdOut.println(wn.distance(random1, random2));
    }

}
