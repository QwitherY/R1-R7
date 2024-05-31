/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {

    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    //   AF(graph) = 一个诗歌生成器，该诗歌生成器所用的单词为graph中的顶点，单词"w1"后面跟着"w2"的次数为graph中从w1到w2的边的权重
    // Representation invariant:
    //   graph中的顶点为非空格非换行符的非空字符串
    //   graph中的顶点都是小写的单词
    // Safety from rep exposure:
    //   graph为private
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(corpus));
        String source = "";
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            for (String target: line.split(" ")) {
                target = target.toLowerCase();
                graph.add(target);
                if (!source.isEmpty()) {
                    int curWeight = 0;
                    if (graph.targets(source).containsKey(target)) {
                        curWeight = graph.targets(source).get(target);
                    }
                    graph.set(source, target, curWeight + 1);
                }
                source = target;
            }
        }
        bufferedReader.close();
        checkRep();
    }
    
    // TODO checkRep
    /**
     * 检测RI是否被保持
     */
    private void checkRep() {
        for (String vertex: graph.vertices()) {
            assert !vertex.isEmpty();
            assert !vertex.equals("\n");
            assert !vertex.equals(" ");
            assert vertex.equals(vertex.toLowerCase());
        }
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String output = "";
        String source = "";
        int newLinesNum = input.split("\n").length;
        for (String splitByNewlines: input.split("\n")) {
            int spacesNum = splitByNewlines.split(" ").length;
            for (String target: splitByNewlines.split(" ")) {
                if (!source.isEmpty()) {
                    int maxWeight = -1;
                    String maxBridge = "";
                    for (String bridge: graph.targets(source.toLowerCase()).keySet()) {
                        if (graph.targets(source.toLowerCase()).get(bridge).equals(graph.sources(target.toLowerCase()).get(bridge)) && (maxWeight == -1 || graph.targets(source.toLowerCase()).get(bridge) > maxWeight)) {
                            maxWeight = graph.targets(source.toLowerCase()).get(bridge);
                            maxBridge = bridge;
                        }
                    }
                    if (maxWeight != -1) {
                        output = output.concat(maxBridge + " ");
                    }
                }
                source = target;
                output = output.concat(source);
                spacesNum--;
                if (spacesNum > 0) {
                    output = output.concat(" ");
                }
            }
            newLinesNum--;
            if (newLinesNum > 1) {
                output = output.concat("\n");
            }
        }
        return output;
    }
    
    // TODO toString()
    /**
     * 返回诗歌生成器的字符串表示
     * @return 诗歌生成器的字符串表示，格式为"vertices{word1,word2,...};edges{source-weight->target,...}"，其中单词都是小写
     */
    @Override
    public String toString() {
        return graph.toString();
    }
}
