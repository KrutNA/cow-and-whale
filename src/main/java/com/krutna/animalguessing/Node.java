package com.krutna.animalguessing;

public class Node<E,T> {
    enum NodeType {
        Node,
        Leaf
    }

    private NodeType nodeType;
    private E leaf;
    private Node<E,T> left, right;
    private T trait;

    public Node(T trait, Node<E,T> left, Node<E,T> right) {
        this.nodeType = NodeType.Node;
        this.left = left;
        this.right = right;
        this.trait = trait;
        this.leaf = null;
    }

    public Node(E leaf) {
        this.nodeType = NodeType.Leaf;
        this.left = null;
        this.right = null;
        this.trait = null;
        this.leaf = leaf;
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public Node<E,T> getLeft() {
        return left;
    }

    public Node<E,T> getRight() {
        return right;
    }

    public T getTrait() {
        return trait;
    }

    public E getLeaf() {
        return leaf;
    }

    public void updateWith(T trait, E leaf) {
        this.nodeType = NodeType.Node;
        left = new Node<>(leaf);
        right = new Node<>(this.leaf);
        this.trait = trait;
        this.leaf = null;
    }
}
