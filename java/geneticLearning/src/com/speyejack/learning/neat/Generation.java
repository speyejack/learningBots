package com.speyejack.learning.neat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class Generation {
	private List<Species> species = new ArrayList<Species>();
	private int genNumber = 0;
	private int currSpecies = 0;
	private Innovation innovation;

	public Generation(Innovation inno, int inputSize, int outputSize) {
		this.innovation = new Innovation();
		for (int i = 0; i < Config.Population; i++) {
			addGenome(new Genome(inputSize, outputSize, innovation));
		}
	}

	public Generation(Generation gen) {
		this.species = gen.species;
		this.genNumber = gen.getGenNumber() + 1;
		this.innovation = gen.innovation;
		innovation.clearInnovation();
		List<Genome> gRanks = new ArrayList<Genome>();
		double gtotal = 0;
		// Debug stuff
		//End Debug stuff
		for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
			Species s = sIter.next();
			s.resetSpecies();
			double stotal = 0;
			for (Iterator<Genome> gIter = s.getGenomes().iterator(); gIter.hasNext();) {
				Genome g = gIter.next();
				double adjfit = g.getFitness() * 10.0 / s.getGenomes().size();
				g.setAdjFitness(adjfit);
				gRanks.add(g);
				stotal += adjfit;
			}
			s.sortGenomes();
			s.setTotalFit(stotal);
			gtotal += stotal;
		}
		if (gtotal <= 0) {
			for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
				Species s = sIter.next();
				for (Iterator<Genome> gIter = s.getGenomes().iterator(); gIter.hasNext();) {
					gIter.next().forceMutation();
				}
			}
			return;
		}
		gRanks.sort(new Comparator<Genome>() {

			@Override
			public int compare(Genome o1, Genome o2) {
				return Double.compare(o2.getAdjFitness(), o1.getAdjFitness());
			}
		});

		for (int i = 0; i < gRanks.size(); i++) {
			gRanks.get(i).setRank(i);
			gRanks.get(i).setFitness(0);
		}
		int capRank = gRanks.size() / 2 - 1;
		for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
			Species s = sIter.next();
			s.getGenomes().removeIf(new Predicate<Genome>() {

				@Override
				public boolean test(Genome t) {
					return t.getRank() > capRank;
				}
			});
		}
		this.species.removeIf(new Predicate<Species>() {

			@Override
			public boolean test(Species t) {
				return t.getGenomes().size() < 1;
			}
		});
		gRanks.removeIf(new Predicate<Genome>() {

			@Override
			public boolean test(Genome t) {
				return t.getRank() > capRank;
			}
		});
		List<Genome> children = new ArrayList<Genome>();
		List<Species> deadSpecies = new ArrayList<Species>();
		for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
			Species s = sIter.next();
			int breedAmount = (int) ((double) s.getTotalFit() / (double) gtotal * (Config.Population - gRanks.size()));
			if (s.getGenomes().size() == 0) {
				deadSpecies.add(s);
			}
			for (int i = 0; i < breedAmount - s.getGenomes().size(); i++) {
				// Only uses top genome if staleness too high
				Genome g1 = s.getRandomGenome();
				Genome g2 = s.getRandomGenome();
				children.add(new Genome(g1, g2));
				children.get(children.size() - 1).setGen(this.genNumber);
			}
		}
		species.removeAll(deadSpecies);

		int count = 0;
		for (int i = 0; i < species.size(); i++)
			count += species.get(i).getGenomes().size();
		while (children.size() + count < Config.Population) {
			Genome g1 = getRandomSpecies().getRandomGenome();
			Genome g2 = getRandomSpecies().getRandomGenome();
			children.add(new Genome(g1, g2));
		}
		for (int i = 0; i < children.size(); i++) {
			addGenome(children.get(i));
		}
		
	}
	
	public void reset(){
		currSpecies = 0;
		for (Iterator<Species> iter = species.iterator(); iter.hasNext();){
			iter.next().resetSpecies();
		}
	}

	public Species getRandomSpecies() {
		return species.get(RoutineF.nextInt(species.size()));
	}

	private boolean hasNextGenome() {
		if (currSpecies + 1 < species.size())
			return true;
		else
			return species.get(currSpecies).hasNextGenome();
	}

	private Genome getNextGenome() {
		if (!species.get(currSpecies).hasNextGenome())
			currSpecies++;
		return species.get(currSpecies).getNextGenome();
	}

	public boolean hasNextOrganisum() {
		return hasNextGenome();
	}

	public Organisum getNextOrganisum() {
		return new Organisum(getNextGenome());
	}
	
	public List<Organisum> getAllOrganisums(){
		List<Organisum> orgs = new ArrayList<Organisum>();
		for (int i = 0; i < species.size(); i++){
			List<Genome> genomes = species.get(i).getGenomes();
			for (int j = 0; j < genomes.size();j++){
				orgs.add(new Organisum(genomes.get(j)));
			}
		}
		return orgs;
	}

	public Genome peekGenome() {
		if (!species.get(currSpecies).hasNextGenome())
			return species.get(currSpecies + 1).peekGenome();
		return species.get(currSpecies).peekGenome();
	}

	public Genome getGenome(int index) {
		int si = 0;
		while (index >= species.get(si).getGenomes().size())
			index -= species.get(si++).getGenomes().size();
		return species.get(si).getGenomes().get(index);
	}

	public Genome getTopGenome() {
		double fit = 0;
		Genome top = species.get(0).getGenomes().get(0);
		for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
			for (Iterator<Genome> gIter = sIter.next().getGenomes().iterator(); gIter.hasNext();) {
				Genome g = gIter.next();
				if (g.getFitness() > fit) {
					fit = g.getFitness();
					top = g;
				}
			}
		}
		return top;
	}

	public int getGenNumber() {
		return genNumber;
	}

	public void addGenome(Genome g) {
		for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
			Species s = sIter.next();
			if (Genome.isSameSpecies(g, s.getGenomes().get(0))) {
				s.getGenomes().add(g);
				return;
			}
		}
		Species s = new Species();
		s.addGenome(g);
		species.add(s);
	}

	public int[] getGenStats() {
		double max = Integer.MIN_VALUE;
		double min = Integer.MAX_VALUE;
		int total = 0;
		int count = 0;
		for (Iterator<Species> sIter = species.iterator(); sIter.hasNext();) {
			for (Iterator<Genome> gIter = sIter.next().getGenomes().iterator(); gIter.hasNext();) {
				Genome g = gIter.next();
				double fit = g.getFitness();
				if (fit > max)
					max = fit;
				if (fit < min)
					min = fit;
				total += fit;
				count++;
			}
		}
		return new int[] { genNumber, (int) min, (total / count), (int) max };
	}
}
