package com.speyejack.learning.neat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.speyejack.learning.gui.FitGraphPanel;

public class Pool {
	private int maxFitness = 0;
	private Generation generation;
	private Innovation innovation;
	private long seed;
	private List<int[]> history;
	FitGraphPanel fitgraph;
	private List<Genome> topGenomes;

	public Pool(int inputSize, int outputSize) {
		seed = new Random().nextLong();
		setRandomSeed(seed);
		generation = new Generation(innovation, inputSize, outputSize);
		history = new ArrayList<int[]>();
		topGenomes = new ArrayList<Genome>();
	}

	public void setRandomSeed(long seed) {
		this.seed = seed;
		RoutineF.setRandomSeed(seed);
	}

	public Organisum getTopOrganisum() {
		return new Organisum(topGenomes.get(topGenomes.size() - 1));
	}

	public boolean hasNextOrganisum() {
		return generation.hasNextOrganisum();
	}

	public Organisum nextOrganisum() {
		return generation.getNextOrganisum();
	}

	public Genome peekGenome() {
		return generation.peekGenome();
	}

	public Genome getGenome(int index) {
		if (index > Config.Population)
			return null;
		return generation.getGenome(index);
	}

	public Genome getTopGenome() {
		return topGenomes.get(topGenomes.size() - 1);
	}

	public int getMaxFitness() {
		return maxFitness;
	}

	public Generation getGeneration() {
		return generation;
	}

	public void resetPool() {
		generation.reset();
	}

	public void advancePool() {
		int[] stats = generation.getGenStats();
		history.add(stats);
		topGenomes.add(generation.getTopGenome());
		fitgraph.addData(stats);
		generation = new Generation(generation);
	}

	public void addFitPanel(FitGraphPanel graph) {
		fitgraph = graph;
	}
}
