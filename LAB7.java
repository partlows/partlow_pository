package edu.wit.cs.comp2350;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/** Aligns strings in two text files by matching their longest common substring 
 * @author Samuel Partlow
 * Wentworth Institute of Technology
 * COMP 2350
 * Lab Assignment 7
 * 
 */

public class LAB7 {

	/**
	 * Method to calculate, and build the longest common subsequence of two strings
	 * 
	 * @param text1
	 *            -first word
	 * @param text2
	 *            -second word
	 * @return -formatted longest common subsequence
	 */
	public static String[] findLCSdyn(String text1, String text2) {
		int row = text1.length();
		int col = text2.length();

		int lcs[][] = new int[row + 1][col + 1];

		char[] t1 = text1.toCharArray(); // converts the words from a string to an array of chars
		char[] t2 = text2.toCharArray();

		StringBuilder newString1 = new StringBuilder(); // string builder object to construct lcs
		StringBuilder newString2 = new StringBuilder();

		int tempMax = 0;

		for (int i = 0; i <= row; i++) {	// calculates longest common subsequence
			for (int j = 0; j <= col; j++) {
				if (i == 0 || j == 0) {
					lcs[i][j] = 0;
				} else if (t1[i - 1] == t2[j - 1]) {
					lcs[i][j] = lcs[i - 1][j - 1] + 1;
				} else {
					lcs[i][j] = max(lcs[i][j - 1], lcs[i - 1][j]);
				}
			}
		}

		int i = row;
		int j = col;
		while (i >= 0 && j >= 0) {		// loop to construct lcs
			if (i == 0 && j != 0) {
				newString2.insert(0, t2[j - 1]);
				newString1.insert(0, '-');
				j--;
			} else if (j == 0 & i != 0) {
				newString1.insert(0, t1[i - 1]);
				newString2.insert(0, '-');
				i--;
			} else if (j != 0 && i != 0) {
				if (t1[i - 1] != t2[j - 1]) {
					tempMax = max(lcs[i][j - 1], lcs[i - 1][j]);
					if (tempMax == lcs[i][j - 1]) { // up
						newString2.insert(0, t2[j - 1]);
						newString1.insert(0, '-');
						j--;
					} else if (tempMax == lcs[i - 1][j]) { // left
						newString1.insert(0, t1[i - 1]);
						newString2.insert(0, '-');
						i--;
					}
				} else {
					newString1.insert(0, t1[i - 1]);
					newString2.insert(0, t2[j - 1]);
					j--;
					i--;
				}
			} else {
				j--;
			}
		}

		longest = lcs[row][col];
		return new String[] { newString1.toString(), newString2.toString() };
	}

	private static int max(int x, int y) {
		int max = 0;
		if (x == y) {
			max = x;
		} else if (x > y) {
			max = x;
		} else {
			max = y;
		}

		return max;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	private static int longest = -1;

	// recursive helper for DFS
	private static void dfs_solve(int i1, int i2, String s1, String s2, char[] out1, char[] out2, int score,
			int index) {

		if ((i1 >= s1.length()) && (i2 >= s2.length())) {
			if (score > longest) {
				out1[index] = '\0';
				out2[index] = '\0';
				longest = score;
				sol1 = String.valueOf(out1).substring(0, String.valueOf(out1).indexOf('\0'));
				sol2 = String.valueOf(out2).substring(0, String.valueOf(out2).indexOf('\0'));
			}
		} else if ((i1 >= s1.length()) && (i2 < s2.length())) { // at the end of first string
			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);
		} else if ((i1 < s1.length()) && (i2 >= s2.length())) { // at the end of second string
			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		} else {
			if (s1.charAt(i1) == s2.charAt(i2)) { // matching next character
				out1[index] = s1.charAt(i1);
				out2[index] = s2.charAt(i2);
				dfs_solve(i1 + 1, i2 + 1, s1, s2, out1, out2, score + 1, index + 1);
			}

			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);

			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		}

	}

	// Used for DFS solution
	private static String sol1, sol2;

	// recursively searches for longest substring, checking all possible alignments
	public static String[] findLCSdfs(String text1, String text2) {
		int max_len = text1.length() + text2.length() + 1;
		char[] out1 = new char[max_len];
		char[] out2 = new char[max_len];

		dfs_solve(0, 0, text1, text2, out1, out2, 0, 0);

		String[] ret = new String[2];
		ret[0] = sol1;
		ret[1] = sol2;
		return ret;
	}

	// returns the length of the longest string
	public static int getLongest() {
		return longest;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1, file2, text1 = "", text2 = "";
		System.out.printf(
				"Enter <text1> <text2> <algorithm>, ([dfs] - depth first search, [dyn] - dynamic programming): ");
		System.out.printf("(e.g: text/a.txt text/b.txt dfs)\n");
		file1 = s.next();
		file2 = s.next();

		try {
			text1 = new String(Files.readAllBytes(Paths.get(file1)));
			text2 = new String(Files.readAllBytes(Paths.get(file2)));
		} catch (IOException e) {
			System.err.println("Cannot open files " + file1 + " and " + file2 + ". Exiting.");
			System.exit(0);
		}

		String algo = s.next();
		String[] result = { "" };

		switch (algo) {
		case "dfs":
			result = findLCSdfs(text1, text2);
			break;
		case "dyn":
			result = findLCSdyn(text1, text2);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Best cost: %d\nLongest string alignment:\n%s\n\n%s\n", longest, result[0], result[1]);
	}
}
