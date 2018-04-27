package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/* Adds floating point numbers with varying precision 
 * 
 * Sam Partlow
 * Wentworth Institute of Technology
 * COMP 2350
 * Lab Assignment 2
 * 
 */

public class LAB2 {

	/**
	 * 
	 * @param a
	 *            - the array to be made into a heap and summed
	 * @return - first element in the heap which is the sum
	 */
	public static float heapAdd(float[] a) {
		if (a.length == 0) {
			return 0;
		}
		int heapSize = a.length;

		float sum = 0;

		// creating the heap

		for (int i = 0; i < heapSize; i++) {
			pullup(a, i);
		}

		// System.out.println(Arrays.toString(a)); // test

		// start of heap add
		while (heapSize > 1) {
			float min1;
			float min2;

			// extracting first min
			min1 = a[0];

			// extracting first min
			a[0] = a[heapSize - 1];
			heapSize--;
			pushdown(a, 0, heapSize);

			// finding second min
			min2 = a[0];

			// extracting second min
			a[0] = a[heapSize - 1];
			heapSize--;
			pushdown(a, 0, heapSize);

			// addition of the mins
			sum = min1 + min2;

			// adding the sum to the end of the heap
			a[heapSize] = sum;
			heapSize++;
			pullup(a, heapSize - 1);
		}

		return a[0]; // return the error-minimized sum of floats
	}

	/**
	 * Looks at a specific index and pulls it up the heap if it is smaller than its
	 * parent
	 * 
	 * @param a
	 *            - the heap
	 * @param i
	 *            - the index of the small element
	 */
	private static void pullup(float[] a, int i) {
		int parent = (i - 1) / 2;
		float swap = 0;

		if (a[i] < a[parent] && (parent >= 0)) {
			swap = a[parent];
			a[parent] = a[i];
			a[i] = swap;
			pullup(a, parent);
		}
	}

	/**
	 * Looks at speciifc element in heap and pushes it down if its greater than its
	 * children
	 * 
	 * @param a
	 *            - the heap
	 * @param i
	 *            - the index of the large element
	 * @param heapSize
	 *            - the size of the heap
	 */
	private static void pushdown(float[] a, int i, int heapSize) {
		int minIndex = 0;
		int leftChild = (2 * i) + 1;
		int rightChild = (2 * i) + 2;
		float swap;

		// determines the min child of a[i]
		if (leftChild < heapSize) {
			if (leftChild < heapSize && rightChild < heapSize) {
				if (a[leftChild] <= a[rightChild]) {
					minIndex = leftChild;
				} else {
					minIndex = rightChild;
				}
			} else if (leftChild < heapSize && rightChild >= heapSize) {
				minIndex = leftChild;
			}
			if (minIndex != i && a[i] > a[minIndex]) {
				swap = a[i];
				a[i] = a[minIndex];
				a[minIndex] = swap;
				pushdown(a, minIndex, heapSize);
			}
		}
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// sum an array of floats sequentially - high rounding error
	public static float seqAdd(float[] a) {
		float ret = 0;

		for (int i = 0; i < a.length; i++)
			ret += a[i];

		return ret;
	}

	// sort an array of floats and then sum sequentially - medium rounding error
	public static float sortAdd(float[] a) {
		Arrays.sort(a);
		return seqAdd(a);
	}

	// scan linearly through an array for two minimum values,
	// remove them, and put their sum back in the array. repeat.
	// minimized rounding error
	public static float min2ScanAdd(float[] a) {
		int min1, min2;
		float tmp;

		if (a.length == 0)
			return 0;

		for (int i = 0, end = a.length; i < a.length - 1; i++, end--) {

			if (a[0] < a[1]) {
				min1 = 0;
				min2 = 1;
			} // initialize
			else {
				min1 = 1;
				min2 = 0;
			}

			for (int j = 2; j < end; j++) { // find two min indices
				if (a[min1] > a[j]) {
					min2 = min1;
					min1 = j;
				} else if (a[min2] > a[j]) {
					min2 = j;
				}
			}

			tmp = a[min1] + a[min2]; // add together
			if (min1 < min2) { // put into first slot of array
				a[min1] = tmp; // fill second slot from end of array
				a[min2] = a[end - 1];
			} else {
				a[min2] = tmp;
				a[min1] = a[end - 1];
			}
		}

		return a[0];
	}

	// read floats from a Scanner
	// returns an array of the floats read
	private static float[] getFloats(Scanner s) {
		ArrayList<Float> a = new ArrayList<Float>();

		while (s.hasNextFloat()) {
			float f = s.nextFloat();
			if (f >= 0)
				a.add(f);
		}
		return toFloatArray(a);
	}

	// copies an ArrayList to an array
	private static float[] toFloatArray(ArrayList<Float> a) {
		float[] ret = new float[a.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the adding algorithm to use ([h]eap, [m]in2scan, se[q], [s]ort): ");
		char algo = s.next().charAt(0);

		System.out
				.printf("Enter the non-negative floats that you would like summed, followed by a non-numeric input: ");
		float[] values = getFloats(s);
		float sum = 0;

		s.close();

		if (values.length == 0) {
			System.out.println("You must enter at least one value");
			System.exit(0);
		} else if (values.length == 1) {
			System.out.println("Sum is " + values[0]);
			System.exit(0);

		}

		switch (algo) {
		case 'h':
			sum = heapAdd(values);
			break;
		case 'm':
			sum = min2ScanAdd(values);
			break;
		case 'q':
			sum = seqAdd(values);
			break;
		case 's':
			sum = sortAdd(values);
			break;
		default:
			System.out.println("Invalid adding algorithm");
			System.exit(0);
			break;
		}

		System.out.printf("Sum is %f\n", sum);

	}

}
