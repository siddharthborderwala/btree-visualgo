package core;

public class Node {
    /**
     * An array of keys
     */
    public int[] keys;
    /**
     * Minimum degree (range of no. of trees)
     */
    public int t;
    /**
     * An array of children
     */
    public Node[] C;
    /**
     * Current number of keys
     */
    public int n;
    /**
     * If the node is leaf
     */
    public boolean leaf;

    /**
     * @param t    minimum degree
     * @param leaf if node is leaf node
     */
    public Node(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        // Allocate memory for maximum number of possible keys and child pointers
        keys = new int[2 * t - 1];
        C = new Node[2 * t];
        // Initialize the number of keys as 0
        n = 0;
    }

    /**
     * A function to traverse all nodes in a subtree rooted with this node
     */
    public void traverse() {
        // There are n keys and n+1 children, traverse through n keys and first n children
        int i;
        for (i = 0; i < n; i++) {
            // If this is not leaf, then before printing key[i],
            // traverse the subtree rooted with child C[i].
            if (!leaf)
                C[i].traverse();
            System.out.print(" " + keys[i]);
        }
        // Print the subtree rooted with last child
        if (!leaf)
            C[i].traverse();
    }

    /**
     * @param k key to search for
     * @return the node if found, or null
     */
    public Node search(int k) {
        // Find the first key greater than or equal to k
        int i = 0;
        while (i < n && k > keys[i]) i++;
        // If the found key is equal to k, return this node
        if (keys[i] == k) return this;
        // If key is not found here and this is a leaf node
        if (leaf) return null;
        // Go to the appropriate child
        return C[i].search(k);
    }


    /**
     * A utility function to insert a new key in the subtree rooted with
     * this node. The assumption is, the node must be non-full when this
     * function is called.
     *
     * @param k new key
     */
    public void insertNonFull(int k) {
        // Initialize index as index of rightmost element
        int i = n - 1;
        // If this is a leaf node
        if (leaf) {
            // The following loop does two things
            // a) Finds the location of new key to be inserted
            // b) Moves all greater keys to one place ahead
            while (i >= 0 && keys[i] > k) {
                keys[i + 1] = keys[i];
                i--;
            }

            // Insert the new key at found location
            keys[i + 1] = k;
            n = n + 1;
        } else // If this node is not leaf
        {
            // Find the child which is going to have the new key
            while (i >= 0 && keys[i] > k)
                i--;

            // See if the found child is full
            if (C[i + 1].n == 2 * t - 1) {
                // If the child is full, then split it
                splitChild(i + 1, C[i + 1]);

                // After split, the middle key of C[i] goes up and
                // C[i] is splitted into two.  See which of the two
                // is going to have the new key
                if (keys[i + 1] < k)
                    i++;
            }
            C[i + 1].insertNonFull(k);
        }
    }

    /**
     * A utility function to split the child y of this node
     * Note that y must be full when this function is called
     *
     * @param i the index of y in child array
     * @param y the child
     */
    public void splitChild(int i, Node y) {
        // Create a new node which is going to store (t-1) keys
        // of y
        Node z = new Node(y.t, y.leaf);
        z.n = t - 1;

        // Copy the last (t-1) keys of y to z
        if (t - 1 >= 0) System.arraycopy(y.keys, t, z.keys, 0, t - 1);

        // Copy the last t children of y to z
        if (!y.leaf)
            if (t >= 0) System.arraycopy(y.C, t, z.C, 0, t);

        // Reduce the number of keys in y
        y.n = t - 1;

        // Since this node is going to have a new child,
        // create space of new child
        if (n + 1 - i + 1 >= 0) System.arraycopy(C, i + 1, C, i + 1 + 1, n + 1 - i + 1);

        // Link the new child to this node
        C[i + 1] = z;

        // A key of y will move to this node. Find the location of
        // new key and move all greater keys one space ahead
        if (n - i >= 0) System.arraycopy(keys, i, keys, i + 1, n - i);

        // Copy the middle key of y to this node
        keys[i] = y.keys[t - 1];

        // Increment count of keys in this node
        n = n + 1;
    }
}
