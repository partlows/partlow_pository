package edu.wit.cs.comp2350;

/**
 * Wentworth Institute of Technology 
 * COMP 2350 
 * Lab Assignment 4
 *
 * Class that implements the Binary Search Tree data stucture, which extends
 * LocationHolder, a class for storing the locations for a Disk Drive
 * 
 * @author Sam Partlow
 *
 */
public class BST extends LocationHolder {

	/**
	 * Finds the location of a given disk location
	 */
	public DiskLocation find(DiskLocation d) {
		DiskLocation curr = root;
		if (d != null) {
			if (d.equals(curr)) {
				return curr;
			} else if (d.isGreaterThan(curr)) {
				return find(d, curr.right);
			} else {
				return find(d, curr.left);
			}
		} else {
			return nil;
		}
	}

	/**
	 * finds the location of a given disk location
	 * 
	 * @param d
	 *            -given disk location
	 * @param curr
	 *            - current location in the tree
	 * @return - location of disk location
	 */
	private DiskLocation find(DiskLocation d, DiskLocation curr) {
		if (d.equals(curr)) {
			return curr;
		} else if (d.isGreaterThan(curr)) {
			return find(d, curr.right);
		} else {
			return find(d, curr.left);
		}
	}

	/**
	 * finds the next sequential disk location
	 */
	public DiskLocation next(DiskLocation d) {
		if (!(d.right == null)) {
			return treeMinimum(d.right);
		} else {
			return up(d);
		}
	}

	/**
	 * finds the minimum value of a under a subtree
	 * 
	 * @param x
	 *            - subtree
	 * @return - minimum value
	 */
	public DiskLocation treeMinimum(DiskLocation x) {
		while (x.left != null) {
			x = x.left;
		}
		return x;
	}

	/**
	 * goes up the tree
	 * 
	 * @param d
	 *            - location to go up from
	 * @return - up value or parent
	 */
	public DiskLocation up(DiskLocation d) {
		DiskLocation p = d.parent;
		if ((p == nil) || d.equals(p.left)) {
			return p;
		} else {
			return up(p);
		}
	}

	/**
	 * finds the previous disk location
	 */
	public DiskLocation prev(DiskLocation d) {
		if (!(d.left == null)) {
			return treeMaximum(d.left);
		} else {
			return down(d);
		}
	}

	/**
	 * finds the maximum value of a subtree
	 * 
	 * @param x
	 *            - subtree
	 * @return - maximum value
	 */
	private DiskLocation treeMaximum(DiskLocation x) {
		while (x.right != null) {
			x = x.right;
		}
		return x;
	}

	/**
	 * goes down the tree
	 * 
	 * @param d
	 *            - location to go down from
	 * @return - down value or parent
	 */
	private DiskLocation down(DiskLocation d) {
		DiskLocation p = d.parent;
		if ((p == nil) || d.equals(p.right)) {
			return p;
		} else {
			return down(d);
		}
	}

	/**
	 * inserts a disk location into the tree
	 */
	public void insert(DiskLocation d) {
		d.parent = findParent(d, root, nil);
		if (d.parent == nil) {
			root = d;
		} else {
			if (d.parent.isGreaterThan(d)) {
				d.parent.left = d;
			} else {
				d.parent.right = d;
			}
		}
	}

	/**
	 * finds the parent of a disk location
	 * 
	 * @param d
	 *            - the disk location being added to the tree
	 * @param curr
	 *            - the current disk location
	 * @param parent
	 *            - parent of the current location
	 * @return findParent of left or right subtree
	 */
	private DiskLocation findParent(DiskLocation d, DiskLocation curr, DiskLocation parent) {
		if (curr == null) {
			return parent;
		}
		if (curr.isGreaterThan(d)) {
			return findParent(d, curr.left, curr);
		} else {
			return findParent(d, curr.right, curr);
		}

	}

	/**
	 * determines the height of a tree
	 */
	public int height() {
		DiskLocation curr = root;
		if (curr == null) {
			return 0;
		} else {
			int leftHeight = height(curr.left);
			int rightHeight = height(curr.right);

			if (leftHeight > rightHeight) {
				return leftHeight;
			} else {
				return rightHeight;
			}
		}
	}

	/**
	 * determines the height of a tree
	 * 
	 * @param curr
	 *            -current location
	 * @return ] - height of tree
	 */
	public int height(DiskLocation curr) {
		if (curr == null) {
			return 0;
		} else {
			int leftHeight = height(curr.left);
			int rightHeight = height(curr.right);

			if (leftHeight > rightHeight) {
				return leftHeight + 1;
			} else {
				return rightHeight + 1;
			}
		}

	}
}
