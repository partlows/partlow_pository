package edu.wit.cs.comp2350;

/**
 * Wentworth Institute of Technology COMP 2350 Lab Assignment 4
 * 
 * Class that creates the Red Black Tree data structure which extends the
 * LocationHolder class.
 * 
 * @author partlows
 *
 */
public class RBTree extends LocationHolder {

	/**
	 * sets a disk location's color to red.
	 * 
	 * Use this method on fix-insert instead of directly coloring nodes red to avoid
	 * setting nil as red.
	 */
	private void setRed(DiskLocation z) {
		if (z != nil)
			z.color = RB.RED;
	}

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
		if (d.right != null && d.right != nil) {
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
		while (x.left != nil) {
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
		if (d.left != null && d.left != nil) {
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
		while (x.right != nil) {
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
		if (p == nil || d.equals(p.right)) {
			return p;
		} else {
			return down(p);
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
		if (curr == nil || curr == null) {
			return parent;
		}
		if (curr.isGreaterThan(d)) {
			return findParent(d, curr.left, curr);
		} else {
			return findParent(d, curr.right, curr);
		}

	}

	/**
	 * Inserts a new DiskLocation into the RedBlack Tree and sets its color to red,
	 * or black if it's the root.
	 */
	@Override
	public void insert(DiskLocation d) {
		d.parent = findParent(d, root, nil);
		if (d.parent == nil) {
			root = d;
			nil.color = RB.BLACK;
		} else {
			if (d.parent.isGreaterThan(d)) {
				d.parent.left = d;
			} else {
				d.parent.right = d;
			}
		}
		d.left = nil;
		d.right = nil;

		setRed(d);
		fixInsert(d);
	}

	/**
	 * fixes the insertion to self balance the tree
	 * 
	 * @param z
	 *            -the node to fix
	 */
	public void fixInsert(DiskLocation z) {
		while (z.parent != nil && z.parent.color.equals(RB.RED)) {
			if (z.parent == z.parent.parent.left) {
				DiskLocation y = z.parent.parent.right;
				if (y.color.equals(RB.RED)) {
					z.parent.color = RB.BLACK;
					y.color = RB.BLACK;
					setRed(z.parent.parent);
					z = z.parent.parent;
				} else {
					if (z == z.parent.right) {
						z = z.parent;
						rotateLeft(z);
					}
					z.parent.color = RB.BLACK;
					setRed(z.parent.parent);
					rotateRight(z.parent.parent);
				}
			} else {
				DiskLocation y = z.parent.parent.left;
				if (y.color.equals(RB.RED)) {
					z.parent.color = RB.BLACK;
					y.color = RB.BLACK;
					setRed(z.parent.parent);
					z = z.parent.parent;
				} else {
					if (z == z.parent.left) {
						z = z.parent;
						rotateRight(z);
					}
					z.parent.color = RB.BLACK;
					setRed(z.parent.parent);
					rotateLeft(z.parent.parent);
				}
			}
		}
		root.color = RB.BLACK;
	}

	/**
	 * determines the height of a tree
	 */
	public int height() {
		DiskLocation curr = root;
		if (curr == null) {
			return -1;
		}
		if (curr.left == null & curr.right == null) {
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
			return -1;
		}
		if (curr.left == null & curr.right == null) {
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

	/**
	 * rotates the tree left from the given location
	 * 
	 * @param x
	 *            - the value to rotate from
	 */
	public void rotateLeft(DiskLocation x) {
		DiskLocation y = x.right;
		x.right = y.left;

		if (y.left != nil) {
			y.left.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == nil) {
			root = y;
		} else if (x == x.parent.left) {
			x.parent.left = y;
		} else {
			x.parent.right = y;
		}
		y.left = x;
		x.parent = y;
	}

	/**
	 * rotates the tree right from the given location
	 * 
	 * @param x
	 *            - the value to rotate from
	 */
	public void rotateRight(DiskLocation x) {
		DiskLocation y = x.left;
		x.left = y.right;

		if (y.right != nil) {
			y.right.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == nil) {
			root = y;
		} else if (x == x.parent.right) {
			x.parent.right = y;
		} else {
			x.parent.left = y;
		}
		y.right = x;
		x.parent = y;
	}

	public static void main(String[] args) {
		RBTree rb = new RBTree();
		DiskLocation d1 = new DiskLocation(1, 2);
		rb.insert(d1);
		System.out.println(rb.height());
	}
}
