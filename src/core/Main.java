package core;

public class Main {
    public static void main(String[] args) {
        Tree t = new Tree(3);
        t.insert(1);
        t.insert(3);
        t.insert(7);
        t.insert(10);
        t.insert(11);
        t.insert(13);
        t.insert(14);
        t.insert(15);

        System.out.println("Traversal of tree constructed is\n");
        t.traverse();

        t.remove(6);
        System.out.println("Traversal of tree after removing 6\n");
        t.traverse();

        t.remove(13);
        System.out.println("Traversal of tree after removing 13\n");
        t.traverse();

        t.remove(7);
        System.out.println("Traversal of tree after removing 7\n");
        t.traverse();

        t.remove(4);
        System.out.println("Traversal of tree after removing 4\n");
        t.traverse();

        t.remove(2);
        System.out.println("Traversal of tree after removing 2\n");
        t.traverse();

        t.remove(16);
        System.out.println("Traversal of tree after removing 16\n");
        t.traverse();
    }
}
