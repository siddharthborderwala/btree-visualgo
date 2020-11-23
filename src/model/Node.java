package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Node<E extends Comparable<E>> implements Serializable {
    private static final long serialVersionUID = 123456789;

    private int fullNumber;
    private Node<E> father;
    private final ArrayList<Node<E>> children = new ArrayList<>();
    private final ArrayList<E> keys = new ArrayList<>();

    public Node() {
    }

    public Node(int order) {
        fullNumber = order - 1;
    }

    /**
     * @return true, if node is a leaf
     */
    public boolean isLastInternalNode() {
        if (keys.size() == 0)
            return false;
        for (Node<E> node : children)
            if (node.keys.size() != 0)
                return false;
        return true;
    }

    /**
     * @return the father
     */
    public Node<E> getFather() {
        return father;
    }

    /**
     * @param father the father to set
     */
    public void setFather(Node<E> father) {
        this.father = father;
    }

    public ArrayList<Node<E>> getChildren() {
        return children;
    }

    /**
     * @param index the index to get
     * @return the child
     */
    public Node<E> getChild(int index) {
        return children.get(index);
    }

    /**
     * @param index the index to add
     * @param node  the node to be added
     */
    public void addChild(int index, Node<E> node) {
        children.add(index, node);
    }

    /**
     * @param index the index to remove
     */
    public void removeChild(int index) {
        children.remove(index);
    }

    /**
     * @param index the index to get
     * @return the key
     */
    public E getKey(int index) {
        return keys.get(index);
    }

    /**
     * @param index   the index to add
     * @param element the element be added
     */
    public void addKey(int index, E element) {
        keys.add(index, element);
    }

    /**
     * @param index the index to remove
     */
    public void removeKey(int index) {
        keys.remove(index);
    }

    public ArrayList<E> getKeys() {
        return keys;
    }

    /**
     * @return true, if keys.size() == order - 1
     */
    public boolean isFull() {
        return fullNumber == keys.size();
    }

    /**
     * @return true, if keys.size() > order - 1
     */
    public boolean isOverflow() {
        return fullNumber < keys.size();
    }

    /**
     * @return true, if keys is empty
     */
    public boolean isNull() {
        return keys.isEmpty();
    }

    /**
     * @return keys size
     */
    public int getSize() {
        return keys.size();
    }
}
