package data_structures.Tree;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class BinarySearch<T> {
    
    private class Node {
        T data;
        Node left, right;

        Node(T data) {
            this.data = data;
            left = right = null;
        }
    }

    private Node root;
    private Comparator<T> comparator;

    public BinarySearch(Comparator<T> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    // Insert
    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node insertRec(Node root, T data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }

        if (comparator.compare(data, root.data) < 0)
            root.left = insertRec(root.left, data);
        else if (comparator.compare(data, root.data) > 0)
            root.right = insertRec(root.right, data);

        return root;
    }

    // Search
    public T search(T key) {
        Node res = searchRec(root, key);
        return (res != null) ? res.data : null;
    }

    private Node searchRec(Node root, T key) {
        if (root == null || comparator.compare(key, root.data) == 0)
            return root;

        if (comparator.compare(key, root.data) < 0)
            return searchRec(root.left, key);

        return searchRec(root.right, key);
    }

    // Delete
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node root, T key) {
        if (root == null) return root;

        if (comparator.compare(key, root.data) < 0)
            root.left = deleteRec(root.left, key);
        else if (comparator.compare(key, root.data) > 0)
            root.right = deleteRec(root.right, key);
        else {
            // Node with only one child or no child
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            // Node with two children: Get inorder successor (smallest in the right subtree)
            root.data = minValue(root.right);

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.data);
        }
        return root;
    }

    private T minValue(Node root) {
        T minv = root.data;
        while (root.left != null) {
            minv = root.left.data;
            root = root.left;
        }
        return minv;
    }
    
    // In-order Traversal (Trả về List đã sắp xếp)
    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        inorderRec(root, list);
        return list;
    }

    private void inorderRec(Node root, List<T> list) {
        if (root != null) {
            inorderRec(root.left, list);
            list.add(root.data);
            inorderRec(root.right, list);
        }
    }
    
    public void clear() {
        root = null;
    }
}