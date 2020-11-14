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
     * @param _t    minimum degree
     * @param _leaf if node is leaf node
     */
    public Node(int _t, boolean _leaf) {
        t = _t;
        leaf = _leaf;
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
     * A function that returns the index of the first key that is greater
     * or equal to k
     *
     * @param k key to find
     * @return returns the index of the first key found
     */
    public int findKey(int k) {
        int index = 0;
        while (index < n && keys[index] < k) ++index;
        return index;
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
                // C[i] is split into two.  See which of the two
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

    /**
     * A wrapper function to remove the key k in subtree rooted with
     * this node.
     *
     * @param k the key to remove
     */
    public void remove(int k) {
        int index = findKey(k);

        // The key to be removed is present in this node
        if (index < n && keys[index] == k) {
            // If the node is a leaf node - removeFromLeaf is called
            // Otherwise, removeFromNonLeaf function is called
            if (leaf) removeFromLeaf(index);
            else removeFromNonLeaf(index);
        } else {
            // If this node is a leaf node, then the key is not present in tree
            if (leaf) {
                System.out.println("The key " + k + " is does not exist in the tree\n");
                return;
            }

            // The key to be removed is present in the sub-tree rooted with this node
            // The flag indicates whether the key is present in the sub-tree rooted
            // with the last child of this node
            boolean flag = (index == n);

            // If the child where the key is supposed to exist has less that t keys,
            // we fill that child
            if (C[index].n < t) fill(index);

            // If the last child has been merged, it must have merged with the previous
            // child and so we recurse on the (index-1)th child. Else, we recurse on the
            // (index)th child which now has at least t keys
            if (flag && index > n) C[index - 1].remove(k);
            else C[index].remove(k);
        }
    }

    /**
     * A function to remove the key present in index-th position in
     * this node which is a leaf
     *
     * @param index the index(th) position
     */
    public void removeFromLeaf(int index) {
        // Move all the keys after the index-th pos one place backward
        if (n - index + 1 >= 0) System.arraycopy(keys, index + 1, keys, index + 1 - 1, n - index + 1);
        // Reduce the count of keys
        n--;
    }

    /**
     * A function to remove the key present in index-th position in
     * this node which is a non-leaf node
     *
     * @param index the index(th) position
     */
    public void removeFromNonLeaf(int index) {
        int k = keys[index];

        // If the child that precedes k (C[index]) has atleast t keys,
        // find the predecessor 'pred' of k in the subtree rooted at
        // C[index]. Replace k by pred. Recursively delete pred
        // in C[index]
        if (C[index].n >= t) {
            int pred = getPred(index);
            keys[index] = pred;
            C[index].remove(pred);
        }

        // If the child C[index] has less that t keys, examine C[index+1].
        // If C[index+1] has at least t keys, find the successor 'succ' of k in
        // the subtree rooted at C[index+1]
        // Replace k by succ
        // Recursively delete succ in C[index+1]
        else if (C[index + 1].n >= t) {
            int succ = getSucc(index);
            keys[index] = succ;
            C[index + 1].remove(succ);
        }

        // If both C[index] and C[index+1] has less that t keys,merge k and all of C[index+1]
        // into C[index]
        // Now C[index] contains 2t-1 keys
        // Free C[index+1] and recursively delete k from C[index]
        else {
            merge(index);
            C[index].remove(k);
        }
    }


    /**
     * A function to get the predecessor of the key- where the key
     * is present in the index-th position in the node
     *
     * @param index index (position) of key k
     * @return predecessor of the index(th) key
     */
    public int getPred(int index) {
        // Keep moving to the right most node until we reach a leaf
        Node cur = C[index];
        while (!cur.leaf) cur = cur.C[cur.n];

        // Return the last key of the leaf
        return cur.keys[cur.n - 1];
    }

    /**
     * A function to get the successor of the key- where the key
     * is present in the index-th position in the node
     *
     * @param index index (position) of the key k
     * @return successor of the index(th) key
     */
    public int getSucc(int index) {
        // Keep moving the left most node starting from C[index+1] until we reach a leaf
        Node cur = C[index + 1];
        while (!cur.leaf) cur = cur.C[0];
        // Return the first key of the leaf
        return cur.keys[0];
    }

    /**
     * A function to fill up the child node present in the index-th
     * position in the C[] array if that child has less than t-1 keys
     *
     * @param index the index(th) position of C[] array
     */
    public void fill(int index) {
        // If the previous child(C[index-1]) has more than t-1 keys, borrow a key
        // from that child
        if (index != 0 && C[index - 1].n >= t) {
            borrowFromPrev(index);
        }

        // If the next child(C[index+1]) has more than t-1 keys, borrow a key
        // from that child
        else if (index != n && C[index + 1].n >= t) {
            borrowFromNext(index);
        }

        // Merge C[index] with its sibling
        // If C[index] is the last child, merge it with with its previous sibling
        // Otherwise merge it with its next sibling
        else {
            if (index != n) merge(index);
            else merge(index - 1);
        }
    }

    /**
     * A function to borrow a key from the C[index-1]-th node and place
     * it in C[index]th node
     *
     * @param index the index where key needs to be placed
     */
    public void borrowFromPrev(int index) {
        Node child = C[index];
        Node sibling = C[index - 1];

        // The last key from C[index-1] goes up to the parent and key[index-1]
        // from parent is inserted as the first key in C[index]. Thus, the  loses
        // sibling one key and child gains one key

        // Moving all key in C[index] one step ahead
        if (child.n - 1 + 1 >= 0) System.arraycopy(child.keys, 0, child.keys, 1, child.n - 1 + 1);

        // If C[index] is not a leaf, move all its child pointers one step ahead
        if (!child.leaf) {
            if (child.n + 1 >= 0) System.arraycopy(child.C, 0, child.C, 1, child.n + 1);
        }

        // Setting child's first key equal to keys[index-1] from the current node
        child.keys[0] = keys[index - 1];

        // Moving sibling's last child as C[index]'s first child
        if (!child.leaf)
            child.C[0] = sibling.C[sibling.n];

        // Moving the key from the sibling to the parent
        // This reduces the number of keys in the sibling
        keys[index - 1] = sibling.keys[sibling.n - 1];

        child.n += 1;
        sibling.n -= 1;
    }

    /**
     * A function to borrow a key from the C[index+1]-th node and place it
     * in C[index]th node
     *
     * @param index the index where key needs to be placed
     */
    public void borrowFromNext(int index) {
        Node child = C[index];
        Node sibling = C[index + 1];

        // keys[index] is inserted as the last key in C[index]
        child.keys[(child.n)] = keys[index];

        // Sibling's first child is inserted as the last child
        // into C[index]
        if (!(child.leaf))
            child.C[(child.n) + 1] = sibling.C[0];

        //The first key from sibling is inserted into keys[index]
        keys[index] = sibling.keys[0];

        // Moving all keys in sibling one step behind
        if (sibling.n - 1 >= 0)
            System.arraycopy(sibling.keys, 1, sibling.keys, 0, sibling.n - 1);

        // Moving the child pointers one step behind
        if (!sibling.leaf)
            if (sibling.n >= 0) System.arraycopy(sibling.C, 1, sibling.C, 0, sibling.n);


        // Increasing and decreasing the key count of C[index] and C[index+1]
        // respectively
        child.n += 1;
        sibling.n -= 1;
    }

    /**
     * A function to merge index-th child of the node with (index+1)th child of
     * the node
     *
     * @param index index of the child to be merged
     */
    public void merge(int index) {
        Node child = C[index];
        Node sibling = C[index + 1];

        // Pulling a key from the current node and inserting it into (t-1)th
        // position of C[index]
        child.keys[t - 1] = keys[index];

        // Copying the keys from C[index+1] to C[index] at the end
        if (sibling.n >= 0) System.arraycopy(sibling.keys, 0, child.keys, 0 + t, sibling.n);

        // Copying the child pointers from C[index+1] to C[index]
        if (!child.leaf) {
            if (sibling.n + 1 >= 0) System.arraycopy(sibling.C, 0, child.C, 0 + t, sibling.n + 1);
        }

        // Moving all keys after index in the current node one step before -
        // to fill the gap created by moving keys[index] to C[index]
        if (n - index + 1 >= 0) System.arraycopy(keys, index + 1, keys, index + 1 - 1, n - index + 1);

        // Moving the child pointers after (index+1) in the current node one
        // step before
        if (n + 1 - index + 2 >= 0) System.arraycopy(C, index + 2, C, index + 2 - 1, n + 1 - index + 2);

        // Updating the key count of child and the current node
        child.n += sibling.n + 1;
        n--;
    }
}
