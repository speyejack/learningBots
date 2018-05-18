package com.speyejack.learning.neat;

import java.util.Random;

public class RoutineF {
	private static Random random = new Random();

	public static void setRandomSeed(long seed) {
		random.setSeed(seed);
	}

	public static int nextInt(int lower, int upper) {
		int bound = upper - lower;
		return random.nextInt(bound) + lower;
	}

	public static int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public static double nextDouble(double lower, double upper) {
		return random.nextDouble() * (upper - lower) + lower;
	}

	public static double nextDouble(double bound) {
		return random.nextInt() * bound;
	}

	public static double sigmoid(double x) {
		return 2 / (1 + Math.exp(-4.9 * x)) - 1;
	}
}
