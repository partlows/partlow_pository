package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Provides a solution to the 0-1 knapsack problem using a dynamic programming approach.
 * 
 * @author Sam Partlow
 * 
 * Wentworth Institute of Technology 
 * COMP 2350 
 * Lab Assignment 8
 * 
 */

public class LAB8 {

	/**
	 * Solves the 0-1 Knapsack problem using a dynamic programming approach.
	 * 
	 * @param table
	 *            -table of items
	 * @param weight
	 *            -max weight of knapsack
	 * @return -array taken items
	 */
	public static Item[] FindDynamic(Item[] table, int weight) {
		ArrayList<Item> sack = new ArrayList<Item>();

		int col = weight + 1;
		int row = table.length + 1;
		int[][] knapsack = new int[row][col];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (i == 0 || j == 0) {
					knapsack[i][j] = 0;
				} else {
					if (table[i - 1].weight <= j) {
						knapsack[i][j] = max(table[i - 1].price + knapsack[i - 1][j - table[i - 1].weight],
								knapsack[i - 1][j]);
					} else {
						knapsack[i][j] = knapsack[i - 1][j];
					}
				}
				best_price = knapsack[i][j];
			}
		}

		int i = row - 1;
		int j = col - 1;

		while (i > 0 && j > 0) {
			if (knapsack[i][j] != knapsack[i - 1][j]) {
				sack.add(table[i - 1]);
				j -= table[i - 1].weight;
				i--;
			} else {
				i--;
			}
		}

		int size = sack.size();
		Item[] stolenItems = new Item[size];

		for (int k = 0; k < size; k++) {
			stolenItems[k] = sack.remove(0);
		}

		return stolenItems;
	}

	/**
	 * Computes maximum of two values
	 * 
	 * @param val1
	 *            -first value to compare
	 * @param val2
	 *            -second value to compare
	 * @return
	 */
	public static int max(int val1, int val2) {
		if (val1 >= val2) {
			return val1;
		} else {
			return val2;
		}
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// set by calls to Find* methods
	private static int best_price = 0;

	public static class Item {
		public int weight;
		public int price;
		public int index;

		public Item(int w, int p, int i) {
			weight = w;
			price = p;
			index = i;
		}

		public String toString() {
			return "(" + weight + "#, $" + price + ")";
		}
	}

	// enumerates all subsets of items to find maximum price that fits in knapsack
	public static Item[] FindEnumerate(Item[] table, int weight) {

		if (table.length > 31) { // bitshift fails for larger sizes
			System.err.println("Problem size too large. Exiting");
			System.exit(0);
		}

		int nCr = 1 << table.length; // bitmask for included items
		int bestSum = -1;
		boolean[] bestUsed = {};
		boolean[] used = new boolean[table.length];

		for (int i = 0; i < nCr; i++) { // test all combinations
			int temp = i;

			for (int j = 0; j < table.length; j++) {
				used[j] = (temp % 2 == 1);
				temp = temp >> 1;
			}

			if (TotalWeight(table, used) <= weight) {
				if (TotalPrice(table, used) > bestSum) {
					bestUsed = Arrays.copyOf(used, used.length);
					bestSum = TotalPrice(table, used);
				}
			}
		}

		int itemCount = 0; // count number of items in best result
		for (int i = 0; i < bestUsed.length; i++)
			if (bestUsed[i])
				itemCount++;

		Item[] ret = new Item[itemCount];
		int retIndex = 0;

		for (int i = 0; i < bestUsed.length; i++) { // construct item list
			if (bestUsed[i]) {
				ret[retIndex] = table[i];
				retIndex++;
			}
		}
		best_price = bestSum;
		return ret;

	}

	// returns total price of all items that are marked true in used array
	private static int TotalPrice(Item[] table, boolean[] used) {
		int ret = 0;
		for (int i = 0; i < table.length; i++)
			if (used[i])
				ret += table[i].price;

		return ret;
	}

	// returns total weight of all items that are marked true in used array
	private static int TotalWeight(Item[] table, boolean[] used) {
		int ret = 0;
		for (int i = 0; i < table.length; i++) {
			if (used[i])
				ret += table[i].weight;
		}

		return ret;
	}

	// adds items to the knapsack by picking the next item with the highest
	// price:weight ratio. This could use a max-heap of ratios to run faster, but
	// it runs in n^2 time wrt items because it has to scan every item each time
	// an item is added
	public static Item[] FindGreedy(Item[] table, int weight) {
		boolean[] used = new boolean[table.length];
		int itemCount = 0;

		while (weight > 0) { // while the knapsack has space
			int bestIndex = GetGreedyBest(table, used, weight);
			if (bestIndex < 0)
				break;
			weight -= table[bestIndex].weight;
			best_price += table[bestIndex].price;
			used[bestIndex] = true;
			itemCount++;
		}

		Item[] ret = new Item[itemCount];
		int retIndex = 0;

		for (int i = 0; i < used.length; i++) { // construct item list
			if (used[i]) {
				ret[retIndex] = table[i];
				retIndex++;
			}
		}

		return ret;
	}

	// finds the available item with the best price:weight ratio that fits in
	// the knapsack
	private static int GetGreedyBest(Item[] table, boolean[] used, int weight) {

		double bestVal = -1;
		int bestIndex = -1;
		for (int i = 0; i < table.length; i++) {
			double ratio = (table[i].price * 1.0) / table[i].weight;
			if (!used[i] && (ratio > bestVal) && (weight >= table[i].weight)) {
				bestVal = ratio;
				bestIndex = i;
			}
		}

		return bestIndex;
	}

	public static int getBest() {
		return best_price;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		int weight = 0;
		System.out.printf(
				"Enter <objects file> <knapsack weight> <algorithm>, ([d]ynamic programming, [e]numerate, [g]reedy).\n");
		System.out.printf("(e.g: objects/small 10 g)\n");
		file1 = s.next();
		weight = s.nextInt();

		ArrayList<Item> tableList = new ArrayList<Item>();

		try (Scanner f = new Scanner(new File(file1))) {
			int i = 0;
			while (f.hasNextInt())
				tableList.add(new Item(f.nextInt(), f.nextInt(), i++));
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}

		Item[] table = new Item[tableList.size()];
		for (int i = 0; i < tableList.size(); i++)
			table[i] = tableList.get(i);

		String algo = s.next();
		Item[] result = {};

		switch (algo.charAt(0)) {
		case 'd':
			result = FindDynamic(table, weight);
			break;
		case 'e':
			result = FindEnumerate(table, weight);
			break;
		case 'g':
			result = FindGreedy(table, weight);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Index of included items: ");
		for (int i = 0; i < result.length; i++)
			System.out.printf("%d ", result[i].index);
		System.out.printf("\nBest total price: %d\n", best_price);
	}

}
