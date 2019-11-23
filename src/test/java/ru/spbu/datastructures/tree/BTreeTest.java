package ru.spbu.datastructures.tree;

import org.junit.Test;

public class BTreeTest {

    @Test
    public void putDemo() {
        BTree<Integer, Integer> tree = new BTree<>();
        for (int i = 0; i < 4; i++)
            tree.put(i, i * 10);
        System.out.println(tree);
        System.out.println("---------------------------------------");

        for (int i = 4; i < 8; i++)
            tree.put(i, i * 10);
        System.out.println(tree);
        System.out.println("---------------------------------------");

        for (int i = 8; i < 12; i++)
            tree.put(i, i * 10);
        System.out.println(tree);
        System.out.println("---------------------------------------");
    }

    @Test
    public void nodesDemo() {
        BTree<Integer, Integer> tree = new BTree<>();

        for (int i = 0; i < 16; i++)
            tree.put(i, i * 10);

        System.out.println(tree);

        for (Object node: tree.traverse())
            System.out.println(node);
    }

    @Test
    public void intsDemo() {
        BTree<Integer, Integer> tree = new BTree<>();

        for (int i = 0; i < 64; i++)
            tree.put(i, i * 10);

        System.out.println(tree);

        System.out.println("45: " + tree.get(45));
        System.out.println("34: " + tree.get(34));
    }

    @Test
    public void dnsDemo() {
        BTree<String, String> tree = new BTree<>();

        System.out.println(tree);

        putAll(tree);

        System.out.println(tree);

        System.out.println("cs.princeton.edu:  " + tree.get("www.cs.princeton.edu"));
        System.out.println("hardvardsucks.com: " + tree.get("www.harvardsucks.com"));
        System.out.println("simpsons.com:      " + tree.get("www.simpsons.com"));
        System.out.println("apple.com:         " + tree.get("www.apple.com"));
        System.out.println();

        System.out.println("size:    " + tree.size());
        System.out.println("height:  " + tree.height());
        System.out.println(tree);
        System.out.println();
    }

    private void putAll(BTree<String, String> tree) {
        tree.put("www.cs.princeton.edu", "128.112.136.12");
        tree.put("www.cs.princeton.edu", "128.112.136.11");
        tree.put("www.princeton.edu", "128.112.128.15");
        tree.put("www.yale.edu", "130.132.143.21");
        tree.put("www.simpsons.com", "209.052.165.60");
        tree.put("www.apple.com", "17.112.152.32");
        tree.put("www.amazon.com", "207.171.182.16");
        tree.put("www.ebay.com", "66.135.192.87");
        tree.put("www.cnn.com", "64.236.16.20");
        tree.put("www.google.com", "216.239.41.99");
        tree.put("www.nytimes.com", "199.239.136.200");
        tree.put("www.microsoft.com", "207.126.99.140");
        tree.put("www.dell.com", "143.166.224.230");
        tree.put("www.slashdot.org", "66.35.250.151");
        tree.put("www.espn.com", "199.181.135.201");
        tree.put("www.weather.com", "63.111.66.11");
        tree.put("www.yahoo.com", "216.109.118.65");
    }
}
