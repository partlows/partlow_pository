package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Sorts geographic points by surface distance to a specific point
 * 
 * Wentworth Institute of Technology COMP 2350 Lab Assignment 3
 * 
 * @author partlows
 *
 */

public class LAB3 {
	public static Random rand = new Random();

	/**
	 * Executes the quickSort algorithm. Sorts an array of type Coord.
	 * 
	 * @param destinations
	 *            - Array of coordinates to sort based on a starting coordinate.
	 */
	public static void quickSort(Coord[] destinations) {
		int l = 0;
		int r = destinations.length - 1;
		int pivot;

		// partitions array and chooses a pivot, and does this recursively
		if (l < r) {
			pivot = partition(destinations, 0, r);
			quickSort(destinations, l, pivot - 1);
			quickSort(destinations, pivot + 1, r);
		}
	}

	/**
	 * Second quickSort method, used by the partition method to continue
	 * partitioning.
	 * 
	 * @param destinations
	 *            - Array to sort
	 * @param l
	 *            - leftmost index in partition
	 * @param r
	 *            - rightmost index in partition
	 */
	public static void quickSort(Coord[] destinations, int l, int r) {
		if (l < r) {
			int pivot = partition(destinations, l, r);
			quickSort(destinations, l, pivot - 1);
			quickSort(destinations, pivot + 1, r);
		}
	}

	/**
	 * Executes the randomQuickSort algorithm. Sorts an array of type Coord.
	 * 
	 * @param destinations
	 *            -Array to sort
	 */
	public static void randQuickSort(Coord[] destinations) {
		int l = 0;
		int r = destinations.length - 1;
		if (l < r) {
			int pivot = rPartition(destinations, l, r);
			randQuickSort(destinations, l, pivot - 1);
			randQuickSort(destinations, pivot + 1, r);
		}
	}

	/**
	 * Second randomQuickSort method, used by the rPartition method to continue
	 * partitioning
	 * 
	 * @param destinations
	 *            -Array to sort
	 * @param l
	 *            - leftmost index in partition
	 * @param r
	 *            - rightmost index in partition
	 */
	public static void randQuickSort(Coord[] destinations, int l, int r) {
		if (l < r) {
			int pivot = rPartition(destinations, l, r);
			quickSort(destinations, l, pivot - 1);
			quickSort(destinations, pivot + 1, r);
		}
	}

	/**
	 * Partition method for random quicksort. Chooses a random pivot, and partitions
	 * the array based on that.
	 * 
	 * @param destinations
	 *            -Array to partition
	 * @param l
	 *            -leftmost index of partition
	 * @param r
	 *            -rightmost index of partition
	 * @return -pivot
	 */
	public static int rPartition(Coord[] destinations, int l, int r) {

		int z = rand.nextInt((r - l) + 1) + l;
		Coord swap = destinations[l];
		destinations[l] = destinations[z];
		destinations[z] = swap;
		return partition(destinations, l, r);
	}

	/**
	 * Parition method for quicksort. Choses a set pivot, and partitions the array
	 * based on that
	 * 
	 * @param destinations
	 *            -Array to partition
	 * @param l
	 *            -leftmost index of partition
	 * @param r
	 *            -rightmost index of partition
	 * @return -pivot
	 */
	public static int partition(Coord[] destinations, int l, int r) {
		Coord pivot = destinations[l];
		Coord swap;
		int i = l;
		for (int j = l + 1; j <= r; j++) {
			if (destinations[j].getDist() <= pivot.getDist()) {
				i++;
				swap = destinations[i];
				destinations[i] = destinations[j];
				destinations[j] = swap;
			}
		}
		swap = destinations[i];
		destinations[i] = destinations[l];
		destinations[l] = swap;
		return i;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// Call system sort with a lambda expression on the comparator
	public static void systemSort(Coord[] destinations) {
		Arrays.sort(destinations, (a, b) -> Double.compare(a.getDist(), b.getDist()));
	}

	// Insertion sort eventually sorts an array
	public static void insertionSort(Coord[] a) {

		for (int i = 1; i < a.length; i++) {
			Coord tmpC = a[i];
			int j;
			for (j = i - 1; j >= 0 && tmpC.getDist() < a[j].getDist(); j--)
				a[j + 1] = a[j];
			a[j + 1] = tmpC;
		}
	}

	private static Coord getOrigin(Scanner s) {
		double lat = s.nextDouble();
		double lon = s.nextDouble();

		Coord ret = new Coord(lat, lon);
		return ret;
	}

	private static Coord[] getDests(Scanner s, Coord start) {
		ArrayList<Coord> a = new ArrayList<>();

		while (s.hasNextDouble()) {
			a.add(new Coord(s.nextDouble(), s.nextDouble(), start));
		}

		Coord[] ret = new Coord[a.size()];
		a.toArray(ret);

		return ret;
	}

	private static void printCoords(Coord[] a) {
		for (int i = 0; i < a.length; ++i) {
			System.out.println(a[i]);
		}
		System.out.println();
		System.out.println(
				"Paste these results into https://www.darrinward.com/lat-long/ if you want to visualize the coordinates");
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf(
				"Enter the sorting algorithm to use [i]nsertion sort, [q]uicksort, [r]andomized quicksort, or [s]ystem quicksort): ");
		char algo = s.next().charAt(0);

		System.out.printf(
				"Enter your starting coordinate in \"latitude longitude\" format as doubles: (e.g. 42.3366322 -71.0942150): ");
		Coord start = getOrigin(s);

		System.out.printf(
				"Enter your end coordinates one at a time in \"latitude longitude\" format as doubles: (e.g. 38.897386 -77.037400). End your input with a non-double character:%n");
		Coord[] destinations = getDests(s, start);

		s.close();

		switch (algo) {
		case 'i':
			insertionSort(destinations);
			break;
		case 'q':
			quickSort(destinations);
			break;
		case 'r':
			randQuickSort(destinations);
			break;
		case 's':
			systemSort(destinations);
			break;
		default:
			System.out.println("Invalid search algorithm");
			System.exit(0);
			break;
		}

		printCoords(destinations);

	}

}
