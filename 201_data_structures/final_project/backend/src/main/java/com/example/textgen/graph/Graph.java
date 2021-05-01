// Weighted Directed Graph
// Graph (has many / HashMap) GraphNodes (has many / BT) Edges (has one) GraphNode 
// GraphNode (has many / Range Tree) GraphNodes
package com.example.textgen.graph;

import java.util.*;
import java.util.Map.Entry;
import java.lang.*;
import java.io.*;

public class Graph {
  public static class GraphNode {
    private String word;
    private Map<GraphNode, Integer> edges;
    private RedBlackBST<GraphNode> probabilities;
    private Map<GraphNode, Integer> cumProbabilities;
    private boolean compiled;

    public GraphNode(String word) {
      this.word = word;
      this.edges = new HashMap<>();
      this.probabilities = new RedBlackBST<>();
      this.cumProbabilities = new HashMap<>();
      this.compiled = false;
    }

    @Override
    public String toString() { return this.word; }
    public boolean isCompiled() { return this.compiled; }
    public void setCompiled(boolean compiled) { this.compiled = compiled; }
    public String printGraph(StringBuilder buffer) {
      buffer.append(this.word);
      // buffer.append("\n");
      // buffer.append(probabilities);
      buffer.append(edges);
      buffer.append("\n");
      return buffer.toString();
    }
    @Override
    public int hashCode() { return this.word.hashCode(); }
    @Override
    public boolean equals(Object o) { return this.word.equals((GraphNode) o); }
    public boolean equals(GraphNode o) { return this.word.equals(o.getWord()); }
    public String getWord() { return this.word; }

    public void setEdge(GraphNode node) {
      Integer count = this.edges.get(node);
      if (count == null) {
        count = 1;
      } else if (count < Integer.MAX_VALUE) {
        count++;
      }
      this.edges.put(node, count + 1);
    }

    public int getOutDegree() {
        int outDegree = 0;
        for (int weight : edges.values()) {
            outDegree += weight;
        }
        return outDegree;
    }

    /***************************************************************************
      *  GraphNode: Compile
    ***************************************************************************/

    public void compile() {
      System.out.println("Compiling... Word: " + this.word);
      int sum = 0;
      for (GraphNode node : this.edges.keySet()) {
        List<Double> list = maxNWord(node, 5);
        double max = 0;
        for (Double p : list) {
          if (p > max) max = p;
        }
        int probability = (int) max * 100000;
        this.cumProbabilities.put(node, sum + probability);
        sum += probability;
      }
      for (Entry<GraphNode, Integer> entry : this.cumProbabilities.entrySet()) {
        double cum_p = (double) entry.getValue() / sum;
        cum_p = Double.isNaN(cum_p) ? 1 : cum_p;
        if (cum_p != 0) this.probabilities.put(cum_p, entry.getKey());
      }
      this.probabilities.postOrder();
      this.compiled = true;
    }

    public List<Double> maxNWord(GraphNode node, int count) {
      List<Double> cum_list = new ArrayList<>();
      Map<GraphNode, Double> cum_prob = cumProbabilities(node);
      for (Entry<GraphNode, Double> childNode : cum_prob.entrySet()) {
        if (count == 0) {
          return cumProbabilities(childNode.getKey(), true); // containing cumulative prob
        } else {
          for (Double val : maxNWord(childNode.getKey(), count - 1)) { // put all map in map
            cum_list.add(val * childNode.getValue());
          }
        }
      }
      return cum_list;
    }

    public List<Double> cumProbabilities(GraphNode node, boolean list) {
      if (list == false) return null;
      List<Double> cum_list = new ArrayList<>();
      for (Double entry : cumProbabilities(node).values()) {
        cum_list.add(entry);
      }
      return cum_list;
    }
    public Map<GraphNode, Double> cumProbabilities(GraphNode node) {
      int sum = 0;
      Map<GraphNode, Double> map = new HashMap<>();

      for (Entry<GraphNode, Integer> entry : node.edges.entrySet()) {
        map.put(entry.getKey(), Double.valueOf(entry.getValue() + sum));
        sum += entry.getValue();
      }
      final int summ = sum;
      map.replaceAll((k, v) -> v / summ);
      return map;
    }
  }
  
  /***************************************************************************
    *  Graph
  ***************************************************************************/

  private Map<String, GraphNode> dict;
  private final String SOS = "\\SOS\\";
  private final String EOS = "\\EOS\\";
  public Graph() {
    this.dict = new HashMap<>();
    this.dict.put(this.SOS, new GraphNode(this.SOS));
    this.dict.put(this.EOS, new GraphNode(this.EOS));
  }
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder(300);
    for (GraphNode node : this.dict.values()) {
      node.printGraph(buffer);
    }
    return buffer.toString();
  }

  /***************************************************************************
    *  Graph: Remove Rarely Used Word
  ***************************************************************************/

  public void removeRarelyUsedWords() {
    // Go through entire graph to calculate in-degrees
    Map<GraphNode, Integer> inDegrees = new HashMap<>();
    for (GraphNode node : dict.values()) {
        if (node.getWord().equals(this.SOS) || node.getWord().equals(this.EOS)) continue;
        for (GraphNode other : node.edges.keySet()) {
            if (inDegrees.get(other) == null) inDegrees.put(other, 0);
            inDegrees.put(other, inDegrees.get(other) + node.edges.get(other));
        }
    }

    final int THRESHOLD = 6;
    for (Entry<GraphNode, Integer> entry : inDegrees.entrySet()) {
        if (entry.getValue() <= THRESHOLD
            && entry.getKey().getOutDegree() <= THRESHOLD) {
            // Node contains rarely used word; delete
            GraphNode rareWord = entry.getKey();
            for (GraphNode node : dict.values()) {
                if (node.edges.containsKey(rareWord)) {
                    node.edges.remove(rareWord);
                }
            }
            dict.remove(rareWord.word);
        }
    }
  }

  /***************************************************************************
    *  Graph: Add Prediction (Word Predict Word)
  ***************************************************************************/
  public void add(String prevStr, String currStr) { this.add(prevStr, currStr, false); }
  public void add(String prevStr, String currStr, boolean isEOS) {
    currStr = currStr.trim();
    if (currStr.equals("")) return;
    GraphNode currNode = this.dict.get(currStr);
    // === EOS ===
    if (isEOS && currNode != null) {
      currNode.setEdge(this.dict.get(this.EOS));
      return;
    }
    // === Current Node ===
    if (currNode == null) {
      currNode = new GraphNode(currStr);
      this.dict.put(currStr, currNode);
    }
    // === Prev Node (null -> SOS) ===
    GraphNode prevNode = (prevStr == null || prevStr.equals("")) ? this.dict.get(this.SOS) : this.dict.get(prevStr.trim());
    // === Set Edge ===
    prevNode.setEdge(currNode);
  }

  /***************************************************************************
    *  Graph: Predict Sentence
  ***************************************************************************/
  public String predict(String first, int num_words) {
    if (first == null) return "<Word Not Recognised>";
    first = first.trim();
    GraphNode node = this.dict.get(first.isBlank() ? this.SOS : first);
    // GraphNode node = this.dict.get(this.SOS);
    if (node == null) return "<Word Not Recognised>";
    // === adding words into buffer ===
    StringBuilder buffer = new StringBuilder();
    if (!node.isCompiled()) node.compile();
    while (!node.probabilities.isEmpty() && num_words-- > 0) {
      GraphNode prediction = node.probabilities.get();
      if (prediction == null) break;
      if (prediction.getWord().equals(this.EOS)) break;
      if (prediction.getWord().equals(this.SOS)) continue;
      buffer.append(prediction.getWord());
      buffer.append(" ");
      node = prediction;
      if (!node.isCompiled()) node.compile();
    }
    return buffer.toString();
  }

  /***************************************************************************
    *  Graph: Compile
  ***************************************************************************/
  public void compile() {
    System.out.println("\nCompiling...\n");
    for (GraphNode node : this.dict.values()) {
      node.compile();
    }
  }
  
  public void saveGraphToCSV() {
    try (PrintWriter writer = new PrintWriter(new File("testtesttest.csv"))) {
      StringBuilder sb = new StringBuilder();
      for (GraphNode node : this.dict.values()) {
        sb.append(node.word + ",");
        RedBlackBST<GraphNode> rbt = node.probabilities;
        rbt.saveGraphToCSV(sb);
      }
      writer.write(sb.toString());
      System.out.println("done saving to csv!");
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }


  // public void loadGraphFromCSV() {
  //   String csvFile = "testtesttest.csv";
  //   BufferedReader br = null;
  //   String line = "";
  //   String cvsSplitBy = ",";

  //   try {
  //     br = new BufferedReader(new FileReader(csvFile));
  //     while ((line = br.readLine()) != null) {
  //       // use comma as separator
  //       String[] word = line.split(cvsSplitBy);

  //       Node node = this.findNode(word[0]);
  //       if (node == null) {
  //         node = new Node(word[0]);
  //         this.words.add(node);
  //       }
        
  //       for (int i = 1; i < word.length; i++) {
  //         String[] wordArr = word[i].split(" ");
          
  //         Node nodeEdge = this.findNode(wordArr[0]);
  //         if (nodeEdge == null) {
  //           nodeEdge = new Node(wordArr[0]);
  //           this.words.add(nodeEdge);
            
  //         }
  //         Edge edge = node.setEdge(nodeEdge);

  //         edge.setWeight(Integer.parseInt(wordArr[1]));
  //       }
  //     }

  //   } catch (IOException e) {
  //     e.printStackTrace();
  //   } finally {
  //     if (br != null) {
  //       try {
  //         br.close();
  //       } catch (IOException e) {
  //         e.printStackTrace();
  //       }
  //     }
  //   }

  // }


}
