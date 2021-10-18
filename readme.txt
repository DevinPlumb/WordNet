/* *****************************************************************************
 *  Name:     Devin Plumb
 *  NetID:    dplumb
 *  Precept:  P06
 *
 *  Hours to complete assignment (optional): ~8
 *
 **************************************************************************** */

Programming Assignment 6: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */

    I used two HashMaps to store the information in synsets.txt. The first
    HashMap associates an integer id number as a key with a string consisting
    of a set of synonyms as a value. The second HashMap associates an individual
    noun to an integer id number. These two data structures made it very easy to
    convert back and forth between nouns and id numbers (used in calculating
    distances in the associated digraph) while meeting the given timing
    constraints.

    Other symbol tables could have worked, but this one was easy and fast.

/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */

    I convert the information in hypernyms.txt into a Digraph, which I then use
    to initialize a ShortestCommonAncestor, which I store as an instance
    variable. This allows me to implement the distance and sca functions by
    calling functions of the ShortestCommonAncestor, which makes manipulating
    the data much easier.

    Originally I intended to store the information in hypernyms.txt in a
    Digraph, but this caused my distance and sca functions to make excessive
    calls to the ShortestCommonAncestor constructor.

/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithms as a function of the number of vertices V and the
 *  number of edges E in the digraph?
 **************************************************************************** */

Description: I had to do two things: check that it is acyclic and check that it
has a single root. Checking that it is acyclic required using a DirectedCycle
object and calling the hasCycle function, which uses a depth first search.
Checking that it has a single root required running through the vertices and
counting the number of vertices with outdegree of 0. In the worst case, these
would require O(V+E) and O(V) time, respectively.

Order of growth of running time: O(V+E)


/* *****************************************************************************
 *  Describe concisely your algorithm to compute the shortest common ancestor
 *  in ShortestCommonAncestor. For each method, give the order of growth of
 *  the best- and worst-case running times (as a function of the number of
 *  vertices V and the number of edges E in the digraph)?
 *
 *  If you use hashing, assume the uniform hashing assumption so that put()
 *  and get() take constant time per operation.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't forget
 *  to count the time needed to initialize the marked[], edgeTo[], and
 *  distTo[] arrays.
 **************************************************************************** */

Description: Originally, I used BreadthFirstDirectedPaths. However, this
prevents you from completing the challenge portion of the assignment, because
BreadthFirstDirectedPaths creates arrays proportional to V, regardless of the
source vertices. Instead, I used a private class to create a variation on
BreadthFirstDirectedPaths that uses a hashmap. If a vertex in the digraph is
reachable from the set of source vertices, I place an integer representing the
minimum distance to it in the hashmap. Thus, I can use the same hashmap to check
whether that vertex is marked and, if it is marked, to check the distance to it.
The size of the hashmap is proportional to the number of vertices and edges
reachable from the source vertices. Finally, my helper function runs through the
vertices reachable from one of the sets of source vertices and checks whether
each is reachable from the other source set. If so, it checks whether the
distance to reach them is less than the current minimum, and if so it updates
the minimum.


                                 running time
method                  best case            worst case
--------------------------------------------------------
length()                   O(1)                O(V+E)

ancestor()                 O(1)                O(V+E)

lengthSubset()             O(1)                O(V+E)

ancestorSubset()           O(1)                O(V+E)



/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

    N/A


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */

    My ShortestCommonAncestor methods should meet the additional performance
    requirements.
