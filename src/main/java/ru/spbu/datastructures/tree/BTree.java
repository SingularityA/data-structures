package ru.spbu.datastructures.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BTree<K extends Comparable<K>, V> {

    private static final int NODE_DEGREE = 4;
    private static final int MAX_KEYS = NODE_DEGREE - 1;

    private Node<K, V> root;
    private int height;
    private int size;

    // EXTERNAL NODE
    private static final class Node<K extends Comparable<K>, V> {
        private int numberOfChildren;                          // number of children
        private Entry[] data = new Entry[NODE_DEGREE + 1];     // 0 position not used
        private Node[] children = new Node[NODE_DEGREE + 1];
        private Node<K, V> parent;

        private Node(int numberOfChildren, Node<K, V> parent) {
            this.numberOfChildren = numberOfChildren;
            this.parent = parent;
        }

        public String toString() {
            return Arrays.stream(data).map(it -> {
                if (it != null)
                    return it.key + ": " + it.value;
                else
                    return null;
            }).collect(Collectors.joining(" "));
        }
    }

    // INTERNAL MODE
    private static class Entry<K extends Comparable<K>, V> {
        private K key;
        private List<V> value;

        private Entry(K key, List<V> value) {
            this.key = key;
            this.value = value;
        }
    }

    public BTree() {
        root = new Node<>(0, null);
    }

    public int size() {
        return size;
    }

    public int height() {
        return height;
    }

    public List<V> get(K key) {
        return search(root, key);
    }

    private List<V> search(Node node, K key) {
        if (node == null) {
            return null;
        }
        Entry[] data = node.data;
        Node[] children = node.children;

        for (int i = 1; i <= node.numberOfChildren; i++) {

            if (eq(key, data[i].key)) return (List<V>) data[i].value;
            else if (less(key, data[i].key)) return search(children[i - 1], key);
        }
        return search(children[node.numberOfChildren], key);
    }

    public List<Node<K, V>> traverse() {
        final List<Node<K, V>> nodes = new ArrayList<>(size);
        traverse(root, nodes);
        return nodes;
    }

    private void traverse(Node<K, V> node, List<Node<K, V>> nodes) {
        if (node == null) return;
        nodes.add(node);
        for (Node<K, V> child : node.children)
            traverse(child, nodes);
    }

    public void put(K key, V value) {
        List<V> result = search(root, key);
        if (result != null) {
            result.add(value);
            return;
        }
        List<V> list = new ArrayList<>();
        list.add(value);
        insert(root, key, list, height);
    }

    private void insert(Node<K, V> node, K key, List<V> value, int height) {
        int j;
        if (height == 0) {
            Entry<K, V> entry = new Entry<>(key, value);

            for (j = 1; j <= node.numberOfChildren; j++) {
                if (less(key, node.data[j].key)) break;
            }

            for (int i = node.numberOfChildren + 1; i > j; i--) {
                node.data[i] = node.data[i - 1];
                node.children[i] = node.children[i - 1];
            }

            node.data[j] = entry;
            node.children[j] = null; // insert position always on leaf
            node.numberOfChildren++;
            size++;

            split(node);
        } else {
            for (j = 1; j <= node.numberOfChildren; j++) {
                if (less(key, node.data[j].key)) break;
            }
            if (node.children[j - 1] == null) {
                Node<K, V> newNode = new Node<>(1, node);
                newNode.data[1] = new Entry<>(key, value);
            } else {
                insert(node.children[j - 1], key, value, height - 1);
            }
        }
    }

    private void split(Node<K, V> node) {
        if (node == null)
            return;

        // need split
        if (node.numberOfChildren > MAX_KEYS) {

            int mid = node.numberOfChildren / 2;
            Node<K, V> right = new Node<>(node.numberOfChildren - mid, node.parent);
            node.numberOfChildren = mid - 1;
            right.children[0] = node.children[mid];

            if (right.children[0] != null)
                right.children[0].parent = right;

            for (int i = 1; i <= right.numberOfChildren; i++) {
                right.data[i] = node.data[mid + i];
                right.children[i] = node.children[mid + i];
                if (right.children[i] != null)
                    right.children[i].parent = right;
            }

            if (node.parent != null) {
                int j;
                for (j = 1; j <= node.parent.numberOfChildren; j++) {
                    if (less(node.data[mid].key, node.parent.data[j].key)) break;
                }

                for (int i = node.parent.numberOfChildren + 1; i > j; i--) {
                    node.parent.data[i] = node.parent.data[i - 1];
                    node.parent.children[i] = node.parent.children[i - 1];
                }

                node.parent.data[j] = node.data[mid];
                node.parent.children[j] = right;
                node.parent.numberOfChildren++;
                split(node.parent);

            } else {
                // node is root node
                Node<K, V> newRoot = new Node<>(1, null);
                newRoot.data[1] = node.data[mid];
                newRoot.children[0] = node;
                newRoot.children[1] = right;
                newRoot.children[0].parent = newRoot;
                newRoot.children[1].parent = newRoot;
                this.height++;
                root = newRoot;
            }
        }
    }

    public String toString() {
        return toString(root, this.height, "") + "\n";
    }

    private String toString(Node<K, V> node, int height, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] data = node.data;

        if (height == 0) {
            for (int j = 1; j <= node.numberOfChildren; j++) {
                s.append(indent).append(data[j].key).append(" ").append(data[j].value).append("\n");
            }
        } else {
            for (int j = 1; j <= node.numberOfChildren; j++) {
                s.append(toString(node.children[j - 1], height - 1, indent + "     "));
                s.append(indent).append("(").append(data[j].key).append(")\n");
            }
            s.append(toString(node.children[node.numberOfChildren], height - 1, indent + "     "));
        }
        return s.toString();
    }

    private boolean less(Comparable key1, Comparable key2) {
        return key1.compareTo(key2) < 0;
    }

    private boolean eq(Comparable key1, Comparable key2) {
        return key1.compareTo(key2) == 0;
    }
}
