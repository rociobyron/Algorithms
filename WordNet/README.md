# WordNet

The class `SAP` takes a digraph and returns the shortest ancestral path between two given vertices as well as the common ancestor. The client of the class takes the name of an input file as a command-line argument, and n+1 values of that input file, `vertexA1` to `vertexAn` and `vertexB`. It then builds a digraph based on an input file of vertices and directed edges, and returns on screen the common ancestor between vertices A and vertex B.

The class `WordNet` represents a WordNet with a digraph, the vertices being the synsets, and the edges representing directional hypernyms ("v - > w" equivalent to "v is a hypernym of w"). The client of the class takes an input file name as a command-line argument, and two nouns. It then builds the corresponding WordNet, returning on screen the common ancestor to `nounA` and `nounB` and the distance between them (see <a href="http://wordnet.princeton.edu">http://wordnet.princeton.edu</a>).

For more information, visit the [official assignment description](http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html).

For the official

Key concepts: Directed graphs, shortest path algorithm.

## How to compile and run

### Mac OS/ Linux

__SAP__
```
$ javac -cp "../lib/*" SAP.java
$ java -cp "../lib/*:." SAP "./input/digraph-wordnet.txt" vertexA1 vertexA2 ... vertexAn vertexB
```

__WordNet__
```
$ javac -cp "../lib/*" SAP.java WordNet.java
$ java -cp "../lib/*:." WordNet "./input/synsets.txt" "./input/hypernyms.txt" nounA nounB
```

### Windows

__SAP__
```
$ javac -cp "../lib/*" SAP.java
$ java -cp "../lib/*;." SAP "./input/digraph-wordnet.txt" vertexA1 vertexA2 ... vertexAn vertexB
```

__WordNet__
```
$ javac -cp "../lib/*" SAP.java WordNet.java
$ java -cp "../lib/*;." WordNet "./input/synsets.txt" "./input/hypernyms.txt" nounA nounB
```
