package com.speyejack.learning.neat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Species {

	private List<Genome> genomes = new ArrayList<Genome>();
	private double totalFit = 0;
	private int currGenome = 0;
	private int staleness = 0;

	public void addGenome(Genome g) {
		genomes.add(g);
	}

	public boolean hasNextGenome() {
		return currGenome < genomes.size();
	}
	
	public void setTotalFit(double fit){
		totalFit = fit;
	}
	
	public double getTotalFit(){
		return totalFit;
	}

	public List<Genome> getGenomes() {
		return genomes;
	}

	public Genome getNextGenome() {
		return genomes.get(currGenome++);
	}
	
	public Genome peekGenome(){
		return genomes.get(currGenome);
	}

	public Genome getMaxGenome() {
		int max = 0;
		double topFitness = 0;
		for (int i = 0; i < genomes.size(); i++) {
			if (genomes.get(i).getFitness() > topFitness) {
				max = i;
				topFitness = genomes.get(max).getFitness();
			}
		}
		return genomes.get(max);
	}
	
	public void cullSpecies(){
		List<Genome> nGenome = new ArrayList<Genome>();
		nGenome.add(genomes.get(0));
		genomes = nGenome;
		currGenome = 0;
	}

	public void sortGenomes() {
		if (hasNextGenome())
			return;
		genomes.sort(new Comparator<Genome>() {
			@Override
			public int compare(Genome o1, Genome o2) {
				return Double.compare(o1.getFitness(), o2.getFitness());
			}
		});

	}

	public boolean isCompatible(Genome g) {
		return Genome.isSameSpecies(genomes.get(0), g);
	}

	public Genome getRandomGenome(){
		if (staleness > Config.StaleSpecies){
			return genomes.get(RoutineF.nextInt(Math.min(genomes.size(), 2)));
		} else {
			return genomes.get(RoutineF.nextInt(genomes.size()));
		}
	}
	
	public void resetSpecies(){
		currGenome = 0;
	}
	
	public void checkStaleness(){
		
	}
	
}
