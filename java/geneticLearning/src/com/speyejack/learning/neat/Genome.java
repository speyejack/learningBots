package com.speyejack.learning.neat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class Genome implements Serializable {
	private static final long serialVersionUID = 1623338740460732086L;
	private List<Gene> genes = new ArrayList<Gene>();
	private double fitness = 0;
	private double adjFitness = 0;
	private String id = UUID.randomUUID().toString();
	private int maxInputs;
	private int size;
	private int maxOutputs;
	private int rank;
	private Innovation inno;
	// Debug params
//	private Genome par1;
//	private Genome par2;
	private int gen;
	private List<Double> fitHistory = new ArrayList<Double>(); 

	public Genome(Genome g1, Genome g2) {
		if (g1.getFitness() < g2.getFitness()) {
			Genome temp = g1;
			g1 = g2;
			g2 = temp;
		}

//		par1 = g1;
//		par2 = g2;
		gen = Math.max(g1.gen, g2.gen) + 1;

		HashMap<Integer, Gene> geneMap2 = new HashMap<Integer, Gene>();
		for (Iterator<Gene> gIter = g2.getGenes().iterator(); gIter.hasNext();) {
			Gene g = gIter.next();
			geneMap2.put(g.getInnovation(), g);
		}

		for (Iterator<Gene> gIter = g1.getGenes().iterator(); gIter.hasNext();) {
			Gene g = gIter.next();
			if (geneMap2.get(g.getInnovation()) != null && RoutineF.nextInt(2) == 1) {
				genes.add(Gene.copyGene(geneMap2.get(g.getInnovation())));
			} else {
				genes.add(Gene.copyGene(g));
			}
		}

		this.maxInputs = g1.maxInputs;
		this.maxOutputs = g1.maxOutputs;
		this.size = g1.size;
		this.inno = g1.getInnovation();

		 Genome.mutate(this);
	}

	public Genome(int inputSize, int outputSize, Innovation inno) {
		this.maxInputs = inputSize;
		this.maxOutputs = outputSize;
		this.size = inputSize + outputSize;
		this.inno = inno;
//		this.par1 = null;
//		this.par2 = null;
		while (!Genome.geneMutate(this))
			;

	}

	private List<Gene> getGenes() {
		return genes;
	}

	private void addGene(Gene l) {
		genes.add(l);
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fit) {
		fitness = fit;
	}
	
	public void addFitness(double fit) {
		fitHistory.add(fit);
		fitness += fit;
	}

	public double getAdjFitness() {
		return adjFitness;
	}

	public void setAdjFitness(double adjFit) {
		adjFitness = adjFit;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setGen(int gen) {
		this.gen = gen;
	}

	public String getId() {
		return id;
	}

	public Innovation getInnovation() {
		return inno;
	}

	public int getNumInputs() {
		return maxInputs;
	}

	public int getNumOutputs() {
		return maxOutputs;
	}

	public void forceMutation() {
		mutate(this);
	}

	public Network buildNetwork() {
		List<Link> links = new ArrayList<Link>();
		HashMap<Integer, Neuron> map = new HashMap<Integer, Neuron>(size);
		List<Neuron> neurons = new ArrayList<Neuron>();

		for (int i = 0; i < maxInputs; i++) {
			Neuron n = new Neuron(i, Neuron.INPUT_NODE);
			neurons.add(n);
			map.put(i, n);
		}

		for (int i = 0; i < maxOutputs; i++) {
			Neuron n = new Neuron(i + maxInputs, Neuron.OUTPUT_NODE);
			neurons.add(n);
			map.put(i + maxInputs, n);
		}

		// for (Iterator<Gene> geneIter = genes.iterator(); geneIter.hasNext();)
		// {
		for (int i = 0; i < genes.size(); i++) {
			// Gene gene = geneIter.next();
			Gene gene = genes.get(i);

			if (gene.isEnabled()) {
				if (!map.containsKey(gene.getInto())) {
					Neuron n = new Neuron(gene.getInto(), Neuron.HIDDEN_NODE);
					map.put(gene.getInto(), n);
					neurons.add(n);
				}
				if (!map.containsKey(gene.getOut())) {
					Neuron n = new Neuron(gene.getOut(), Neuron.HIDDEN_NODE);
					map.put(gene.getOut(), n);
					neurons.add(n);
				}
				Link l = new Link(map.get(gene.getInto()), map.get(gene.getOut()), gene.getWeight());
				links.add(l);
				l.setId(i);
			}
		}
		try {
			neurons.sort(new Comparator<Neuron>() {

				@Override
				public int compare(Neuron o1, Neuron o2) {
					int c = Integer.compare(o1.getRank(), o2.getRank());
					// System.out.println("R1: " + o1.getRank() + " R2: " +
					// o2.getRank() + " C: " + c);
					return c;
				}
			});
		} catch (IllegalArgumentException e) {
			System.out.println("Broken contract thingy");
			List<Neuron> brokenRanks = new ArrayList<Neuron>();
			for (Iterator<Neuron> nIter = neurons.iterator(); nIter.hasNext();) {
				Neuron n = nIter.next();
				if (n.getRank() < 0) {
					brokenRanks.add(n);
				}
			}
			for (Iterator<Neuron> bIter = brokenRanks.iterator(); bIter.hasNext();)
				bIter.next().getRank();
		}

		// DEBUG

		// List<Neuron> broken = new ArrayList<Neuron>();
		// for (Iterator<Neuron> nIter = neurons.iterator(); nIter.hasNext();) {
		// Neuron n = nIter.next();
		// if (n.getRank() < 0)
		// broken.add(n);
		// }
		// if (broken.size() > 0) {
		// try {
		// ObjectOutputStream os = new ObjectOutputStream(new
		// FileOutputStream(new File(Config.GenomeFileName)));
		// os.writeObject(this);
		// os.close();
		// System.out.println("Found one");
		// System.exit(1);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		return new Network(neurons);
	}

	private static Gene randomGene(Genome g) {
		return g.getGenes().get(RoutineF.nextInt(g.getGenes().size()));
	}

	private static int randomNeuronId(Genome g) {
		return RoutineF.nextInt(g.size);
	}

	private static int randomOtherNeuronId(Genome g, int id) {
		if (isInput(g, id))
			return RoutineF.nextInt(g.maxInputs, g.size);
		else if (isOutput(g, id)) {
			int otherid = RoutineF.nextInt(g.size - g.maxOutputs);
			if (otherid >= g.maxInputs)
				otherid += g.maxOutputs;
			return otherid;
		} else
			return RoutineF.nextInt(g.size);
	}

	private static boolean containsGene(Genome genome, Gene g1) {
		for (Iterator<Gene> geneIter = genome.genes.iterator(); geneIter.hasNext();) {
			Gene g2 = geneIter.next();
			if ((g1.getInto() == g2.getInto() && g1.getOut() == g2.getOut())
					|| (g1.getInto() == g2.getOut() && g2.getOut() == g1.getInto()))
				return true;
		}
		return false;
	}

	private static boolean isInput(Genome genome, int nId) {
		return (nId < genome.maxInputs);
	}

	private static boolean isOutput(Genome genome, int nId) {
		return (nId >= genome.maxInputs && nId < (genome.maxOutputs + genome.maxInputs));
	}

	private static void mutate(Genome g) {
		if (Config.GeneMutationChance > RoutineF.nextDouble(1))
			Genome.geneMutate(g);

		if (Config.NodeMutationChance > RoutineF.nextDouble(1))
			Genome.nodeMutate(g);

		if (Config.PreturbMutationChance > RoutineF.nextDouble(1))
			Genome.weightMutate(g);

//		if (Config.EnableMutationChange > RoutineF.nextDouble(1))
//			Genome.enableMutate(g);
//
//		if (Config.DisableMutationChange > RoutineF.nextDouble(1))
//			Genome.disableMutate(g);

	}

	private static boolean geneMutate(Genome g) {
		int n1 = randomNeuronId(g);
		int n2 = randomOtherNeuronId(g, n1);

		if (n1 == n2)
			return false;

		// if ((n1 > n2 && !isOutput(g, n2))
		// || (isOutput(g, n1))) {
		if (isOutput(g, n1) || isInput(g, n2)) {
			int temp = n1;
			n1 = n2;
			n2 = temp;
		}
		Gene gene = new Gene(n1, n2, RoutineF.nextDouble(-1, 1), 0);
		if (containsGene(g, gene))
			return false;
		// System.out.println(gene);
		int innovationNum = g.getInnovation().getInnovation(n1, n2);
		gene.setInnovation(innovationNum);
		g.addGene(gene);
		return true;

	}

	private static boolean nodeMutate(Genome g) {
		if (g.getGenes().isEmpty()) {
			return false;
		}

		Gene gene = g.getGenes().get(RoutineF.nextInt(g.getGenes().size()));
		if (!gene.isEnabled()) {
			return false;
		}

		gene.disable();
		Gene ng1 = new Gene(gene.getInto(), g.size, gene.getWeight(),
				g.getInnovation().getInnovation(gene.getInto(), g.size));
		Gene ng2 = new Gene(g.size, gene.getOut(), 1, g.getInnovation().getInnovation(g.size, gene.getOut()));
		g.size++;
		// System.out.println("Node: " + g.size);
		// System.out.println(ng1);
		// System.out.println(ng2);
		g.addGene(ng1);
		g.addGene(ng2);

		return true;
	}

	private static boolean weightMutate(Genome g) {
		for (Iterator<Gene> gIter = g.getGenes().iterator(); gIter.hasNext();) {
			if (Config.PerturbChance > RoutineF.nextDouble(1))
				gIter.next().setWeight(RoutineF.nextDouble(-1, 1));
			else if (Config.StepChance > RoutineF.nextDouble(1)) {
				Gene gene = gIter.next();
				if (RoutineF.nextInt(2) == 0)
					gene.setWeight(gene.getWeight() + Config.StepSize);
				if (gene.getWeight() > 1)
					gene.setWeight(1);
				else if (gene.getWeight() < -1)
					gene.setWeight(-1);
				else
					gene.setWeight(gene.getWeight() - Config.StepSize);
				if (gene.getWeight() > 1)
					gene.setWeight(1);
				else if (gene.getWeight() < -1)
					gene.setWeight(-1);
			}

		}
		return true;
	}

	private static boolean enableMutate(Genome g) {
		randomGene(g).enable();
		return true;
	}

	private static boolean disableMutate(Genome g) {
		Gene gene = randomGene(g);
		boolean found = false;
		for (Iterator<Gene> gIter = g.getGenes().iterator(); gIter.hasNext();) {
			Gene geneN = gIter.next();
			if (gene.getOut() == geneN.getOut() && geneN.isEnabled() && gene != geneN) {
				found = true;
				break;
			}
		}
		if (found) {
			gene.disable();
			return true;
		}

		return false;
	}

	public static boolean isSameSpecies(Genome g1, Genome g2) {
		if (g1.genes.size() < g2.genes.size()) {
			Genome temp = g1;
			g1 = g2;
			g2 = temp;
		}

		HashMap<Integer, Gene> geneMap1 = new HashMap<Integer, Gene>();
		for (Iterator<Gene> gIter = g1.genes.iterator(); gIter.hasNext();) {
			Gene gene = gIter.next();
			geneMap1.put(gene.getInnovation(), gene);
		}
		HashMap<Integer, Gene> geneMap2 = new HashMap<Integer, Gene>();
		for (Iterator<Gene> gIter = g2.genes.iterator(); gIter.hasNext();) {
			Gene gene = gIter.next();
			geneMap2.put(gene.getInnovation(), gene);
		}

		int excess = geneMap1.size() - geneMap2.size();
		int disjoint = 0;
		int weight = 0;
		int count = 0;

		for (Iterator<Entry<Integer, Gene>> itr = geneMap1.entrySet().iterator(); itr.hasNext();) {
			Entry<Integer, Gene> entry = itr.next();

			if (geneMap2.get(entry.getKey()) == null)
				disjoint++;
			else {
				weight += Math.abs(entry.getValue().getWeight() - geneMap2.get(entry.getKey()).getWeight());
				count++;
			}
		}

		for (Iterator<Entry<Integer, Gene>> itr = geneMap2.entrySet().iterator(); itr.hasNext();) {
			if (geneMap1.get(itr.next().getKey()) == null) {
				disjoint++;
			}
		}
		int n = Math.max(geneMap1.size(), geneMap2.size());
		return Config.DeltaThreshold > (Config.DeltaExcess * excess / n) + (Config.DeltaDisjoint * disjoint / n)
				+ (Config.DeltaWeights * weight / count);
	}

	public static double calculateAdjFitness(Species s, Genome g) {
		double diviser = 0;
		for (Iterator<Genome> gIter = s.getGenomes().iterator(); gIter.hasNext();) {
			Genome gt = gIter.next();
			if (gt != g) {
				if (isSameSpecies(g, gt))
					diviser++;
			}
		}
		if (diviser == 0)
			diviser = 1;
		return g.getFitness() / diviser;
	}
}
