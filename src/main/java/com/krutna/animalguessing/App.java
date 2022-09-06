package com.krutna.animalguessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App implements Runnable {

    private final Node<String, String> head;

    private final BufferedReader reader;

    public App(final Node<String, String> head) {
        this.head =  head;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) throws IOException {
        var head = new Node<>("lives on land", new Node<>("whale"), new Node<>("cat"));
        var application = new App(head);
        application.run();
    }

    @Override
    public void run() {
        var toContinue = true;
        while (toContinue) {
            System.out.println("Make a guess about an animal, and I'll try to guess.");

            var node = head;
            while (node.getNodeType() == Node.NodeType.Node) {
                System.out.printf("Does this animal %s? [yes, no]: ", node.getTrait());
                var selection = readLine().equals("yes");

                node = selection ? node.getRight() : node.getLeft();
            }

            processOnLeaf(node);

            System.out.print("Continue? [yes, no]: ");
            toContinue = readLine().equals("yes");
        }
    }

    private void processOnLeaf(Node<String, String> node) {
        System.out.printf("Is %s the riddled one? [yes, no]: ", node.getLeaf());

        var isRiddled = readLine().equals("yes");
        if (!isRiddled) {
            System.out.print("Name your one: ");
            var newAnimal = readLine();

            System.out.print("How it's different from? ");
            var trait = readLine();

            node.updateWith(trait, newAnimal);
        }
    }

    private String readLine() {
        try {
            return reader.readLine().trim().toLowerCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
