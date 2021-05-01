package com.example.textgen.graph;

public class RedBlackBST<E> {
  public static class BSTNode<E> {
    private double key; // Key
    private E value;  // Value
    private BSTNode<E> left, right;
    private boolean color;
    private int size;
    private double highest_left_p;

    public BSTNode(double key, E e, boolean color, int size) {
      this.key = key;
      this.value = e;
      this.color = color;
      this.size = size;
    }
  }
  
  private BSTNode<E> root;
  private static final boolean RED = true;
  private static final boolean BLACK = false;
  public RedBlackBST() {}

  /***************************************************************************
    *  Node helper methods.
  ***************************************************************************/
  public boolean isRed(BSTNode<E> x) {
    if (x == null) return false;
    return x.color == this.RED;
  }
  public int size(BSTNode<E> x) {
    if (x == null) return 0;
    return x.size;
  }
  public int size() {
    return size(this.root);
  }
  public boolean isEmpty() {
    return this.root == null;
  }
  public String toString() {
    StringBuilder buffer = new StringBuilder(50);
    print(buffer, this.root, "└── ", "    ");
    return buffer.toString();
  }

  private void print(StringBuilder buffer, BSTNode<E> node, String prefix, String childrenPrefix) {
    if (node == null) return;
    buffer.append(prefix);
    buffer.append(node.value.toString());
    buffer.append(String.format(":%f", node.key));
    buffer.append('\n');
    if (node.left != null) print(buffer, node.left, childrenPrefix + "├── ", childrenPrefix + "│   ");
    if (node.right != null) print(buffer, node.right, childrenPrefix + "└── ", childrenPrefix + "    ");
  }
  /***************************************************************************
    *  Standard BST search.
  ***************************************************************************/

  public E get() {
    double key = Math.random();
    return this.get(key, this.root);
  }
  public E get(double key, BSTNode<E> node) {
    while (node != null) {
      if (key <= node.highest_left_p) {
        node = node.left;
      } else if (key > node.key) {
        node = node.right;
      } else {
        return node.value;
      }
    }
    return null;
  }
  
  /***************************************************************************
    *  Red-black tree insertion.
  ***************************************************************************/
  public void put(double key, E value) {
    if (value == null) {
      // delete
      return;
    }
    root = this.put(this.root, key, value);
    this.root.color = this.BLACK;
  }
  private BSTNode<E> put(BSTNode<E> node, double key, E value) {
    if (node == null) return new BSTNode<>(key, value, this.RED, 1);
    
    if (key < node.key) {
      node.left = this.put(node.left, key, value);
    } else if (key > node.key) {
      node.right = this.put(node.right, key, value);
    } else {
      node.value = value;
    }

    if (this.isRed(node.right) && !this.isRed(node.left)) node = this.rotateLeft(node);
    if (this.isRed(node.left) && this.isRed(node.left.left)) node = this.rotateRight(node);
    if (this.isRed(node.left) && this.isRed(node.right)) this.flipColors(node);
    node.size = size(node.right) + size(node.left) + 1;

    return node;
  }

  public void postOrder() { postOrder(this.root); }
  private void postOrder(BSTNode<E> node) {
    if (node == null) { return; }
    postOrder(node.left);
    postOrder(node.right);
    node.highest_left_p = highestLeft(node);
  }
  private double highestLeft(BSTNode<E> node) {
    if (node.left == null) return 0;
    BSTNode<E> highestLeft = node.left;
    while (highestLeft.right != null) {
      highestLeft = highestLeft.right;
    }
    return highestLeft.key;
  }

  /***************************************************************************
    *  Red-black tree deletion.
  ***************************************************************************/

  /***************************************************************************
    *  Red-black tree helper functions.
  ***************************************************************************/

  private BSTNode<E> rotateRight(BSTNode<E> node) {
    // assert (node != null) && isRed(node.left);
    BSTNode<E> x = node.left;
    node.left = x.right;
    x.right = node;
    x.color = x.right.color;
    x.right.color = RED;
    node.size = node.size;
    node.size = size(node.left) + size(node.right) + 1;
    return x;
  }
  private BSTNode<E> rotateLeft(BSTNode<E> node) {
    // assert (node != null) && isRed(node.right);
    BSTNode<E> x = node.right;
    node.right = x.left;
    x.left = node;
    x.color = x.left.color;
    x.left.color = RED;
    x.size = node.size;
    node.size = size(node.left) + size(node.right) + 1;
    return x;
  }
  
  private void flipColors(BSTNode<E> node) {
    // node must have opposite color of its two children
    // assert (node != null) && (node.left != null) && (node.right != null);
    // assert (!isRed(node) &&  isRed(node.left) &&  isRed(node.right))
    //    || (isRed(node)  && !isRed(node.left) && !isRed(node.right));
    node.color = !node.color;
    node.left.color = !node.left.color;
    node.right.color = !node.right.color;
  }

  /***************************************************************************
    *  Saving graph functions.
  ***************************************************************************/
  
  public void saveGraphToCSV(StringBuilder sb) {
    saveGraphToCSV(sb, this.root);
  }

  public void saveGraphToCSV(StringBuilder buffer, BSTNode<E> node) {
    if (node == null) return;
    buffer.append(node.value.toString() + " ");
    buffer.append(String.format(":%f", node.key) + ",");
    buffer.append('\n');
    if (node.left != null) saveGraphToCSV(buffer, node.left);
    if (node.right != null) saveGraphToCSV(buffer, node.right);
  }
}