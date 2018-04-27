package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Comp 2350 Lab 1 Professor Kreimendahl Due 1/24/16
 * 
 * Sorts integers from command line using various algorithms
 * 
 * Wentworth Institute of Technology COMP 2350 Lab Assignment 1
 * 
 * @author Sam Partlow
 *
 */

public class LAB1 {

	/**
	 * Method to implement the counting sort algorithm. Uses a count array to track
	 * the amount of times a number appears in the input array, then puts them into
	 * an output array in order from least to greatest.
	 * 
	 * @param a
	 *            - the input array
	 * @return sortedArray - the sorted array
	 */

	public static int[] countingSort(int[] a) {
		int max = 0;

		int[] sortedArray = new int[a.length];

		for (int i = 0; i < a.length; i++) { // loop to determine the largest number in input array
			if (max < a[i]) {
				max = a[i];
			}
		}

		int[] count = new int[max + 1];

		for (int i = 0; i < a.length; i++) { // loop to increment the amount of times a number appears from the input
												// array in the count
			count[a[i]]++;
		}

		for (int i = 1; i < count.length; i++) { // loop to modify count to indicate position
			count[i] += count[(i - 1)];
		}

		for (int i = 0; i < a.length; i++) { // loop to put numbers from the count to sortedArray in ascending order
			sortedArray[count[a[i]] - 1] = a[i];
			count[a[i]]--;
		}

		return sortedArray;

	}

	/**
	 * Method to implement the Radix sort algorithm. It sorts starting with the
	 * least significant digit, up to the most significant digit. Uses a stable
	 * counting sort for arranging the digits.
	 * 
	 * @param a
	 *            - the input array
	 * @return a - the sorted array
	 */
	public static int[] radixSort(int[] a) {

		int max = 0;
		int numberOfSorts = 0;

		for (int i = 0; i < a.length; i++) { // loop to determine the largest number in the input array
			if (max < a[i]) {
				max = a[i];
			}
		}

		while (max > 0) { // loop to determine the number of significant digits
			max /= 10;
			numberOfSorts++;
		}

		for (int i = 0; i < numberOfSorts; i++) { // loops until it reaches the maximum number of sorts
			a = stableCount(a, i);
		}

		return a;
	}

	/**
	 * Method to perform a Stable count sort
	 * 
	 * @param a
	 *            - input array
	 * @param digit
	 *            - significant digit
	 * @return - sorted array
	 */
	public static int[] stableCount(int[] a, int digit) {

		int size = a.length;
		int[] sortedArray = new int[size];
		int[] count = new int[10];
		int x = (int) Math.pow(10, digit);

		for (int i = 0; i < a.length; i++) { // increments the count array for each nth-place digit
			count[(int) ((a[i] / (x)) % 10)]++;
		}

		for (int i = 1; i < count.length; i++) { // loop to modify count to indicate position
			count[i] += count[(i - 1)];
		}

		for (int i = a.length - 1; i >= 0; i--) { // loop to put numbers from the count to sortedArray in ascending
													// order
			sortedArray[count[(int) ((a[i] / (x)) % 10)] - 1] = a[i];

			count[(int) ((a[i] / (x)) % 10)]--;
		}

		return sortedArray;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	public final static int MAX_INPUT = 524287;
	public final static int MIN_INPUT = 0;

	// example sorting algorithm
	public static int[] insertionSort(int[] a) {

		for (int i = 1; i < a.length; i++) {
			int tmp = a[i];
			int j;
			for (j = i - 1; j >= 0 && tmp < a[j]; j--)
				a[j + 1] = a[j];
			a[j + 1] = tmp;
		}

		return a;
	}

	/*
	 * Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by
	 * Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm offers
	 * O(n log(n)) performance on many data sets that cause other quicksorts to
	 * degrade to quadratic performance, and is typically faster than traditional
	 * (one-pivot) Quicksort implementations.
	 */
	public static int[] systemSort(int[] a) {
		Arrays.sort(a);
		return a;
	}

	// read ints from a Scanner
	// returns an array of the ints read
	private static int[] getInts(Scanner s) {
		ArrayList<Integer> a = new ArrayList<Integer>();

		while (s.hasNextInt()) {
			int i = s.nextInt();
			if ((i <= MAX_INPUT) && (i >= MIN_INPUT))
				a.add(i);
		}

		return toIntArray(a);
	}

	// copies an ArrayList of Integer to an array of int
	private static int[] toIntArray(ArrayList<Integer> a) {
		int[] ret = new int[a.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the sorting algorithm to use ([c]ounting, [r]adix, [i]nsertion, or [s]ystem): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the integers to sort, followed by a non-integer character: ");
		int[] unsorted_values = getInts(s);
		int[] sorted_values = {};

		s.close();

		switch (algo) {
		case 'c':
			sorted_values = countingSort(unsorted_values);
			break;
		case 'r':
			sorted_values = radixSort(unsorted_values);
			break;
		case 'i':
			sorted_values = insertionSort(unsorted_values);
			break;
		case 's':
			sorted_values = systemSort(unsorted_values);
			break;
		default:
			System.out.println("Invalid sorting algorithm");
			System.exit(0);
			break;
		}

		System.out.println(Arrays.toString(sorted_values));
	}

}
