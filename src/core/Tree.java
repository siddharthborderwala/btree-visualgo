package core;

public class Tree {
    /**
     * Root of the B-Tree
     */
    public Node root;

    /**
     * Minimum degree
     */
    public final int t;

    /**
     * Constructor of B-Tree
     *
     * @param _t minimum degree
     */
    public Tree(int _t) {
        root = null;
        t = _t;
    }

    /**
     * function to traverse the tree
     */
    public void traverse() {
        if (root != null) root.traverse();
    }

    /**
     * Function to search for a key
     *
     * @param k key to search for
     * @return node if found, else null
     */
    public Node search(int k) {
        return root == null ? null : root.search(k);
    }

    /**
     * Function to insert a new key into the B-Tree
     *
     * @param k key to insert in the tree
     */
    public void insert(int k) {
        // If tree is empty
        if (root == null) {
            // Allocate memory for root
            root = new Node(t, true);
            root.keys[0] = k;  // Insert key
            root.n = 1;  // Update number of keys in root
        } else {
            // If tree is not empty

            // If root is full, then tree grows in height
            if (root.n == 2 * t - 1) {
                // Allocate memory for new root
                Node s = new Node(t, false);
                // Make old root as child of new root
                s.C[0] = root;
                // Split the old root and move 1 key to the new root
                s.splitChild(0, root);
                // New root has two children now.  Decide which of the
                // two children is going to have new key
                int i = 0;
                if (s.keys[0] < k)
                    i++;
                s.C[i].insertNonFull(k);
                // Change root
                root = s;
            } else root.insertNonFull(k);
            // If root is not full, call insertNonFull for root
        }
    }

    /**
     * Function to remove a key from the tree
     *
     * @param k key to remove from the tree
     */
    public void remove(int k) {
        if (root == null) System.out.println("The tree is empty");

        // Call the remove function for root
        root.remove(k);

        // If the root node has 0 keys, make its first child as the new root
        //  if it has a child, otherwise set root as NULL
        if (root.n == 0) {
            if (root.leaf) root = null;
            else root = root.C[0];
        }
    }
}
